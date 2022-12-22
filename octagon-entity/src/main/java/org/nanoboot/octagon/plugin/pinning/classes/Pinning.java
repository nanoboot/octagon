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

package org.nanoboot.octagon.plugin.pinning.classes;

import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.octagon.entity.core.EntityAttribute;
import org.nanoboot.octagon.entity.core.EntityAttributeType;
import lombok.Data;

import java.util.*;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@Data
public class Pinning implements Entity {
    protected static List<EntityAttribute> SCHEMA;

    /**
     * UUID identification of this entity.
     */
    private UUID id;

//    /**
//     * example: ProductGroup
//     */
    private String type;
    private UUID objectId;
    private Integer sortkey;
    private String comment;

    @Override
    public String getName() {
        return type + " with id " + objectId;
    }

    @Override
    public void validate() {

    }

    @Override
    public void loadFromMap(Map<String, String> map) {
        setType(getStringParam("type", map));
        setObjectId(getUuidParam("objectId", map));

        setSortkey(getIntegerParam("sortkey", map));
        setComment(getStringParam("comment", map));
    }

    public Class getEntityClass() {
        return getClass();
    }

    @Override
    public String[] toStringArray() {
        return new String[]{
                id == null ? "" : id.toString(),

                type == null ? "" : type,
                objectId == null ? "" : objectId.toString(),

                sortkey == null ? "" : sortkey.toString(),
                comment == null ? "" : comment,

        };
    }
    @Override
    public List<EntityAttribute> getSchema() {
        if (SCHEMA == null) {
            SCHEMA = new ArrayList<>();

            SCHEMA.add(EntityAttribute.getIdEntityAttribute());
            SCHEMA.add(new EntityAttribute("type").withMandatory(true));
            SCHEMA.add(new EntityAttribute("objectId", EntityAttributeType.UUID).withMandatory(true));

            SCHEMA.add(new EntityAttribute("sortkey", EntityAttributeType.INTEGER));
            SCHEMA.add(new EntityAttribute("comment"));
        }
        return SCHEMA;
    }

}
