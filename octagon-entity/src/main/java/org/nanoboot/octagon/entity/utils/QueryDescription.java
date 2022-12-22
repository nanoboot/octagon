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

package org.nanoboot.octagon.entity.utils;

import org.nanoboot.octagon.entity.api.RepositoryRegistry;
import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.octagon.entity.core.EntityAttribute;
import org.nanoboot.octagon.entity.core.EntityAttributeType;
import org.nanoboot.powerframework.json.JsonArray;
import org.nanoboot.powerframework.json.JsonObject;
import org.nanoboot.powerframework.reflection.ReflectionUtils;
import org.nanoboot.powerframework.sql.core.OrderBy;
import org.nanoboot.powerframework.sql.filter.SqlConjunction;
import org.nanoboot.powerframework.sql.filter.SqlFilter;
import org.nanoboot.powerframework.sql.filter.SqlOperation;
import org.nanoboot.powerframework.utils.NamingConvention;
import org.nanoboot.powerframework.utils.NamingConventionConvertor;
import org.nanoboot.octagon.core.exceptions.OctagonException;
import lombok.Data;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@Data
public class QueryDescription {
    private Entity emptyEntity;
    private Map<String, String> map;
    private List<OrderBy> orderByList;
    private int limit;
    private int offset;

    public QueryDescription(JsonObject jo, RepositoryRegistry repositoryRegistry) {
        System.err.println(jo.toPrettyString());
        String className = jo.getString("entityClass");
        String[] classNameArray = className.split("\\.");
        if (classNameArray == null || classNameArray.length == 0) {
            throw new OctagonException("Class name className=" + className + ". It is null or empty.");
        }
        String simpleClassName = classNameArray[classNameArray.length - 1];
        Class clazz = repositoryRegistry.find(simpleClassName).getClassOfType();
        this.emptyEntity = returnNewEmptyInstance(clazz);

        JsonObject joMap = jo.getObject("map");
        this.map = new HashMap<>();
        for (String key : joMap.keyList()) {
            map.put(key, joMap.getString(key));
        }

        orderByList = new ArrayList<>();
        JsonArray orderByListJA = jo.getArray("orderByList");
        for (int i = 0; i < orderByListJA.size(); i++) {
            JsonObject orderByAsJO = orderByListJA.getObject(i);
            OrderBy orderBy = new OrderBy(orderByAsJO);
            orderByList.add(orderBy);
        }

        this.limit = jo.getInt("limit");
        this.offset = jo.getInt("offset");
    }

    public QueryDescription(Entity emptyEntity, Map<String, String> map) {
        this.emptyEntity = emptyEntity;
        this.map = getMapFromFilterMap(map);
        List<OrderBy> orderByList = new ArrayList<>();
        int limit = 0;
        int offset = 0;
    }

    public boolean isEmpty() {
        boolean orderByListIsNotEmpty = orderByList != null && !orderByList.isEmpty();
        boolean orderByListIsEmpty = !orderByListIsNotEmpty;
        return map.isEmpty() && orderByListIsEmpty && limit == 0 && offset == 0;
    }

    public JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        jo.addString("entityClass", emptyEntity.getEntityClass().getName());

        JsonObject mapJO = new JsonObject();
        for (String key : map.keySet()) {
            mapJO.add(key, map.get(key));
        }
        jo.add("map", mapJO);
        JsonArray orderByListJA = new JsonArray();
        if (orderByList != null) {
            for (OrderBy ob : orderByList) {
                orderByListJA.add(ob.toJsonObject());
            }
        }
        jo.addArray("orderByList", orderByListJA);
        jo.addInt("limit", limit);
        jo.addInt("offset", offset);
        return jo;

    }

    private static Entity returnNewEmptyInstance(Class entityClass) {
        Class thisClass = entityClass;
        Constructor constructor = ReflectionUtils.getConstructor(thisClass);
        Entity emptyInstance = (Entity) ReflectionUtils
                .newInstance(constructor);
        return emptyInstance;
    }

    private static String getLimitPart(int limit, int offset) {
        if (limit <= 0) {
            return " ";
        }
        return "LIMIT " + limit + (offset > 0 ? ("OFFSET " + offset) : " ");
    }

    private static String getOrderByPart(List<OrderBy> orderByList) {
        if (orderByList == null || orderByList.isEmpty()) {
            return " ";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("ORDER BY ");
        int index = 0;
        int maxIndex = orderByList.size() - 1;
        for (OrderBy ob : orderByList) {
            sb.append(NamingConventionConvertor.convert(ob.getProperty(), NamingConvention.JAVA, NamingConvention.DATABASE));
            sb.append(" ");
            sb.append(ob.getOrderByType().name());
            if (index < maxIndex) {
                sb.append(",");
            }
            sb.append(" ");
            index++;
        }
        return sb.toString();
    }

    private static String getWherePart(Entity emptyEntity, Map<String, String> map) {
        if (map.isEmpty()) {
            return " ";
        }
        List<Object> conditions = new ArrayList<>();
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (value.isEmpty()) {
                continue;
            }
            if (value == null) {
                throw new RuntimeException("value is null for " + key);
            }
            
            String dbColumnNameFromEntityAttribute = null;
            List<EntityAttribute> schema = emptyEntity.getSchema();
            for(EntityAttribute ea: schema) {
                if(ea.getName().equals(key)) {
                    dbColumnNameFromEntityAttribute = ea.getDbColumnName();
                    break;
                }
            }
            String finalColumnName = dbColumnNameFromEntityAttribute == null ? NamingConventionConvertor.convert(key, NamingConvention.JAVA, NamingConvention.DATABASE) : dbColumnNameFromEntityAttribute;
            String columnName = '"' + finalColumnName + '"';
            
            String dbValue = null;
            if (value.startsWith("#")) {
                String rawSqlEnd = value.substring(1);
                SqlOperation operation = new SqlOperation("(" + columnName + " " + rawSqlEnd + ")");
                conditions.add(operation);
            } else {
                EntityAttributeType dbType = getDbTypeForProperty(emptyEntity, key);
                switch (dbType) {
                    case INTEGER:
                        dbValue = value;
                        break;
                    case BOOLEAN:
                        dbValue = value;
                        break;
                    default:
                        dbValue = "'" + value + "'";
                        break;
                }


                SqlOperation equal = new SqlOperation(SqlOperation.EQUAL, columnName, dbValue);
                conditions.add(equal);
            }

        }
        conditions.add(new SqlOperation(SqlOperation.EQUAL, 1, 1));
        conditions.add(new SqlOperation(SqlOperation.EQUAL, 1, 1));
        SqlConjunction and = new SqlConjunction(SqlConjunction.AND, conditions.toArray());
        System.err.println("and:::" + and.getResult());
        SqlFilter filter = new SqlFilter(and);
        System.err.println("###" + filter.getResult());
        return filter.getResult();
    }

    private static EntityAttributeType getDbTypeForProperty(Entity entity, String propertyName) {
        for (EntityAttribute evs : entity.getSchema()) {
            if (evs.getName().equals(propertyName)) {
                return evs.getType();
            }
        }
        String msg = "DB type for property was not found for " + entity + " " + propertyName;
        throw new OctagonException(msg);
    }

    private static Map<String, String> getMapFromFilterMap(Map<String, String> map0) {
        Map<String, String> map = new HashMap<>();
        Set<String> parameterNames = map0.keySet();
        for (String parameterName : parameterNames) {
            if (parameterName.startsWith("filter_")) {
                String[] array = parameterName.split("_");
                if (array.length != 2) {
                    throw new OctagonException("Fatal error. array.length must be 2, but the actual value is: " + array.length);
                }
                String filter = array[0];
                String field = array[1];
                String value = map0.get(parameterName);
                if (value == null || value.isBlank()) {
                    continue;
                }
                map.put(field, value);
            }
        }
        return map;
    }

    public String getSql() {
        String wherePart = getWherePart(emptyEntity, map);
        String orderByPart = getOrderByPart(orderByList);
        String limitPart = getLimitPart(limit, offset);
        String sql = wherePart + orderByPart + limitPart;
        System.err.println(sql);
        if (sql.trim().equals("")) {
            return null;
        }
        return sql;
    }
}
