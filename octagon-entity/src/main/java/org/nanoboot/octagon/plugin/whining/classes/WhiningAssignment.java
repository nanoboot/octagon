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

package org.nanoboot.octagon.plugin.whining.classes;

import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.octagon.entity.core.EntityAttribute;
import org.nanoboot.octagon.entity.core.EntityAttributeType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@Data
public class WhiningAssignment implements Entity {
    private static List<EntityAttribute> SCHEMA;

    /**
     * UUID identification of this entity.
     */
    private UUID id;

    private String title;
    private UUID whiningId;

    private UUID queryId;
    private UUID sqlQueryId;

    private Integer sortkey;

    @Override
    public String getName() {
        return getTitle();
    }

    @Override
    public void validate() {
        if (queryId == null && sqlQueryId == null) {
            throw new OctagonException("queryId and sqlQueryId are both empty. One of them must be set.");
        }
        if (queryId != null && sqlQueryId != null) {
            throw new OctagonException("queryId and sqlQueryId are both set. Only one of them can be set.");
        }
    }

    @Override
    public void loadFromMap(Map<String, String> map) {
        setTitle(getStringParam("title", map));
        setWhiningId(getUuidParam("whiningId", map));

        setQueryId(getUuidParam("queryId", map));
        setSqlQueryId(getUuidParam("sqlQueryId", map));

        setSortkey(getIntegerParam("sortkey", map));

    }

    public Class getEntityClass() {
        return getClass();
    }

    @Override
    public String[] toStringArray() {
        return new String[]{
                id == null ? "" : id.toString(),

                title == null ? "" : title,
                whiningId == null ? "" : whiningId.toString(),

                queryId == null ? "" : queryId.toString(),
                sqlQueryId == null ? "" : sqlQueryId.toString(),

                sortkey == null ? "" : sortkey.toString(),
        };
    }
    @Override
    public List<EntityAttribute> getSchema() {
        if (SCHEMA == null) {
            SCHEMA = new ArrayList<>();

            SCHEMA.add(EntityAttribute.getIdEntityAttribute());

            SCHEMA.add(new EntityAttribute("title").withMandatory(true));
            SCHEMA.add(new EntityAttribute("whiningId", "whining", "getWhinings" ).withMandatory(true));

            SCHEMA.add(new EntityAttribute("queryId", "query", "getQueries"));
            SCHEMA.add(new EntityAttribute("sqlQueryId", "sqlQuery", "getSqlQueries"));

            SCHEMA.add(new EntityAttribute("sortkey", EntityAttributeType.INTEGER));


        }
        return SCHEMA;
    }
}
