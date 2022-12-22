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

import org.nanoboot.octagon.entity.core.EntityAttribute;
import org.nanoboot.octagon.entity.core.EntityAttributeType;
import org.nanoboot.powerframework.utils.NamingConvention;
import org.nanoboot.powerframework.utils.NamingConventionConvertor;
import org.nanoboot.octagon.entity.core.Entity;
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
public class SqlQuery implements Entity {
    protected static List<EntityAttribute> SCHEMA;

    /**
     * UUID identification of this entity.
     */
    private UUID id;

    private String name;
    //example: ProductGroup, ProductVersion
    private String type;
    private String sql;

    private String description;
    private String foreignKeysLegend;
    private String todo;

    @Override
    public void validate() {

    }

    @Override
    public void loadFromMap(Map<String, String> map) {
        setName(getStringParam("name", map));
        setType(getStringParam("type", map));
        setSql(getStringParam("sql", map));

        setDescription(getStringParam("description", map));
        setForeignKeysLegend(getStringParam("foreignKeysLegend", map));

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
                sql == null ? "" : sql,

                description == null ? "" : description,
                foreignKeysLegend == null ? "" : foreignKeysLegend,

                todo == null ? "" : todo,
        };
    }

    @Override
    public List<EntityAttribute> getSchema() {
        if (SCHEMA == null) {
            SCHEMA = new ArrayList<>();

            SCHEMA.add(EntityAttribute.getIdEntityAttribute());

            SCHEMA.add(new EntityAttribute("name").withMandatory(true));
            SCHEMA.add(new EntityAttribute("type"));
            SCHEMA.add(new EntityAttribute("sql", EntityAttributeType.TEXT_AREA).withMandatory(true));

            SCHEMA.add(new EntityAttribute("description"));
            SCHEMA.add(new EntityAttribute("foreignKeysLegend"));
            SCHEMA.add(new EntityAttribute("todo"));
        }
        return SCHEMA;
    }
    @Override
    public String[] getRelatedListsForEntity() {
        return new String[]{"getParentWhiningsForSqlQuery"};
    }
    public String[] getRelatedActionsForEntity() {
        return new String[]{
                "Execute sql query:sqlExplorer?__processForm=yes&sqlQueryId=",
                "Assign to whining:create?className=WhiningAssignment&sqlQueryId=",
        };
    }

    public static String tryToGuessType(String sql) {
        if (sql == null || sql.isEmpty()) {
            return null;
        }
        System.out.println("Trying to guess type from sql");
        String array[] = sql.split(" ");
        boolean lastWasFrom = false;
        for (String e : array) {
            if (lastWasFrom) {

                String type = e.toUpperCase();
                try {
                    type = NamingConventionConvertor.convert(type, NamingConvention.DATABASE, NamingConvention.JAVA);
                    type = Character.toUpperCase(type.charAt(0)) + type.substring(1);
                    return type;
                } catch (Exception ex) {
                    System.err.println(ex);
                    return null;
                }
            }
            if (e.toLowerCase().equals("from")) {
                lastWasFrom = true;
            }
        }
        return null;
    }
}
