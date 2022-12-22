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

package org.nanoboot.octagon.plugin.favorite.classes;

import org.nanoboot.octagon.core.exceptions.OctagonException;
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
public class Favorite implements Entity {
    protected static List<EntityAttribute> SCHEMA;
    private static final int STAR_COUNT_MIN = 1;
    private static final int STAR_COUNT_MAX = 5;

    /**
     * UUID identification of this entity.
     */
    private UUID id;

    //    /**
//     * example: ProductGroup
//     */
    private String type;
    private UUID objectId;

    private String comment;

    private Integer sortkey;
    private Integer starCount;
    private Boolean ultraFavorite;
    private String group;

    @Override
    public String getName() {
        return type + " with id " + objectId;
    }

    @Override
    public void validate() {

        if ((type == null && objectId != null)) {
            throw new OctagonException("Type is not set, but object id is set.");
        }
        if ((type != null && objectId == null)) {
            throw new OctagonException("Object id is not set, but type is set.");
        }
        if ((type == null && objectId == null)) {
            throw new OctagonException("Object id and type are not set, but both these attributes are mandatory.");
        }
        if (starCount != null && (starCount < STAR_COUNT_MIN || starCount > STAR_COUNT_MAX)) {
            String msg = "Star count is " + starCount + ", but it must be in range from " + STAR_COUNT_MIN + " to " + STAR_COUNT_MAX;
            throw new OctagonException(msg);
        }
    }

    @Override
    public void loadFromMap(Map<String, String> map) {
        setType(getStringParam("type", map));
        setObjectId(getUuidParam("objectId", map));

        setComment(getStringParam("comment", map));

        setSortkey(getIntegerParam("sortkey", map));
        setStarCount(getIntegerParam("starCount", map));

        setUltraFavorite(getBooleanParam("ultraFavorite", map));
        setGroup(getStringParam("group", map));
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

                comment == null ? "" : comment,

                sortkey == null ? "" : sortkey.toString(),
                starCount == null ? "" : starCount.toString(),

                ultraFavorite == null ? "" : convertBooleanToString(ultraFavorite),
                group == null ? "" : group,
        };
    }

    @Override
    public List<EntityAttribute> getSchema() {
        if (SCHEMA == null) {
            SCHEMA = new ArrayList<>();

            SCHEMA.add(EntityAttribute.getIdEntityAttribute());
            SCHEMA.add(new EntityAttribute("type").withMandatory(true));
            SCHEMA.add(new EntityAttribute("objectId", EntityAttributeType.UUID).withMandatory(true));

            SCHEMA.add(new EntityAttribute("comment"));

            SCHEMA.add(new EntityAttribute("sortkey", EntityAttributeType.INTEGER).withMandatory(false));
            SCHEMA.add(new EntityAttribute("starCount", EntityAttributeType.INTEGER).withMandatory(false).withHelp("Range from 1 to 5. Five starts means the most interesting."));

            SCHEMA.add(new EntityAttribute("ultraFavorite", EntityAttributeType.BOOLEAN).withDefaultValue("0"));
            SCHEMA.add(new EntityAttribute("group"));
        }
        return SCHEMA;
    }

    public String getDefaultOrder() {
        return "sortkey asc";
    }

}
