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

package org.nanoboot.octagon.entity.classes;

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
public class Query implements Entity {
    protected static List<EntityAttribute> SCHEMA;

    /**
     * UUID identification of this entity.
     */
    private UUID id;

    private String name;
    //example: ProductGroup
    private String type;

    private String data;
    private String description;
    private String todo;
    @Override
    public void validate() {

    }

    @Override
    public void loadFromMap(Map<String, String> map) {
        setName(getStringParam("name", map));
        setType(getStringParam("type", map));
        setData(getStringParam("data", map));
        setDescription(getStringParam("description", map));
        setTodo(getStringParam("todo", map));
    }

    public Class getEntityClass() {
        return getClass();
    }

    @Override
    public String[] toStringArray() {
        return new String[]{
                id == null ? "" : id.toString(),
                name == null ? "" : name,
                type == null ? "" : type,
                data == null ? "" : data,
                description == null ? "" : description,

                todo == null ? "" : todo,
        };
    }
    @Override
    public List<EntityAttribute> getSchema() {
        if (SCHEMA == null) {
            SCHEMA = new ArrayList<>();

            SCHEMA.add(EntityAttribute.getIdEntityAttribute());

            SCHEMA.add(new EntityAttribute("name").withMandatory(true));
            SCHEMA.add(new EntityAttribute("type").withMandatory(true));
            SCHEMA.add(new EntityAttribute("data", EntityAttributeType.TEXT_AREA).withMandatory(true));
            SCHEMA.add(new EntityAttribute("description"));
            SCHEMA.add(new EntityAttribute("todo"));
        }
        return SCHEMA;
    }
    @Override
    public String[] getRelatedListsForEntity() {
        return new String[]{"getParentWhiningsForQuery"};
    }
    public String[] getRelatedActionsForEntity() {
        return new String[]{
                "Assign to whining:create?className=WhiningAssignment&queryId=",
        };
    }
}
