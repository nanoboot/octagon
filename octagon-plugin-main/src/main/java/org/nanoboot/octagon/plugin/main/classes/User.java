///////////////////////////////////////////////////////////////////////////////////////////////
// Octagon: Database frontend.
// Copyright (C) 2020-2022 the original author or authors.
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; version 2
// of the License only.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
///////////////////////////////////////////////////////////////////////////////////////////////

package org.nanoboot.octagon.plugin.main.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.octagon.entity.core.EntityAttribute;
import org.nanoboot.octagon.entity.core.EntityAttributeType;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@ToString
@Data
public class User implements Entity {

    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(User.class);
    private static final String EMAIL_REGEX_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    
    private static List<EntityAttribute> SCHEMA;
    /**
     * Identification of the user.
     */
    private UUID id;
    /**
     * Nick.
     */
    private String nick;
    /**
     * Name.
     */
    private String name;
    /**
     * Surname.
     */
    private String surname;
    /**
     * E-mail.
     */
    private String email;
    /**
     * Active.
     */
    private Boolean active;
    /**
     * Role restriction.
     */
    private Role roleRestriction;

    @Override
    public void loadFromMap(Map<String, String> map) {
        setNick(getStringParam("nick", map));
        setName(getStringParam("name", map));
        setSurname(getStringParam("surname", map));
        //
        setEmail(getStringParam("email", map));
        setActive(getBooleanParam("active", map));
        String roleRestrictionParam = getStringParam("roleRestriction", map);
        setRoleRestriction(roleRestrictionParam == null ? null : Role.valueOf(roleRestrictionParam));
    }

    @Override
    public Class getEntityClass() {
        return getClass();
    }

    @Override
    public String[] toStringArray() {
        return new String[]{
            id == null ? "" : id.toString(),
            nick == null ? "" : nick,
            name == null ? "" : name,
            surname == null ? "" : surname,
            email == null ? "" : email,
            active == null ? "" : convertBooleanToString(active),
            roleRestriction == null ? "" : roleRestriction.name(),};
    }

    @Override
    public final List<EntityAttribute> getSchema() {
        if (SCHEMA == null) {
            SCHEMA = new ArrayList<>();

            SCHEMA.add(EntityAttribute.getIdEntityAttribute());
            //
            SCHEMA.add(new EntityAttribute("nick").withMandatory(true));
            SCHEMA.add(new EntityAttribute("name").withMandatory(true));
            SCHEMA.add(new EntityAttribute("surname").withMandatory(true));
            //
            SCHEMA.add(new EntityAttribute("email").withMandatory(true).withCustomHumanName("E-mail"));
            SCHEMA.add(new EntityAttribute("active", EntityAttributeType.BOOLEAN).withMandatory(true).withDefaultValue("1"));

            List<String> roleList = Arrays.stream(Role.values())
                    .map(Role::name).collect(Collectors.toList());
            SCHEMA.add(new EntityAttribute("roleRestriction", roleList).withDefaultValue("READER"));

        }
        return SCHEMA;
    }

    @Override
    public void validate() {

        String emailRegexPattern = EMAIL_REGEX_PATTERN;
        boolean emailIsOk = Pattern.compile(emailRegexPattern)
                .matcher(email)
                .matches();
        if (!emailIsOk) {
            String msg = "Following e-mail address is not valid: " + email;
            LOG.error(msg);
            throw new OctagonException(msg);
        }

    }

}
