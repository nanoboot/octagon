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

package org.nanoboot.octagon.entity.core;

import org.nanoboot.powerframework.json.JsonObject;
import org.nanoboot.powerframework.json.JsonValueType;
import org.nanoboot.powerframework.time.moment.LocalDate;
import org.nanoboot.powerframework.time.moment.UniversalDateTime;
import org.nanoboot.powerframework.utils.NamingConvention;
import org.nanoboot.powerframework.utils.NamingConventionConvertor;
import org.nanoboot.octagon.core.exceptions.OctagonException;

import java.util.*;
import org.nanoboot.octagon.entity.api.RepositoryRegistry;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public non-sealed interface Entity extends Validable {
    UUID getId();

    String getName();
    
    default String getOldEntityClass() {
        return null;
    }

    void setId(UUID id);

    @Deprecated
    void validate();


    void loadFromMap(Map<String, String> map);

    @Deprecated
    default Boolean getBooleanParam(String key, Map<String, String> map) {
        Integer integer = getIntegerParam(key, map);
        Boolean result = integer == null ? null : integer != 0;
        return result;
    }

    default void setAutofillPropertiesToNull() {
        
    }
    @Deprecated
    default Integer getIntegerParam(String key, Map<String, String> map) {
        String value = getStringParam(key, map);
        Integer integer = value == null ? null : Integer.valueOf(value);
        return integer;
    }

    @Deprecated
    default String getStringParam(String key, Map<String, String> map) {
        String value = map.get(key);
//        System.err.println("### "+key+"=" + value);
//        if (value == null || value.equals("null")) {
//            throw new OctagonException("fatal err: req value is null");
//        }
        return (value == null || value.isEmpty()) ? null : value;
    }

    @Deprecated
    default UUID getUuidParam(String key, Map<String, String> map) {
        String value = getStringParam(key, map);
        return (value == null || value.isEmpty()) ? null : UUID.fromString(value);
    }

    @Deprecated
    default LocalDate getLocalDateParam(String key, Map<String, String> map) {
        String value = getStringParam(key, map);
        return (value == null || value.isBlank()) ? null : new LocalDate(value);
    }

    @Deprecated
    default UniversalDateTime getUniversalDateTimeParam(String key, Map<String, String> map) {
        String value = getStringParam(key, map);
        return (value == null || value.isBlank()) ? null : new UniversalDateTime(value);
    }

    @Deprecated
    default JsonObject getJsonObjectParam(String key, Map<String, String> map) {
        String value = getStringParam(key, map);
        return (value == null || value.isBlank()) ? null : new JsonObject(value);
    }

    @Deprecated
    Class getEntityClass();

    @Deprecated
    default String getEntityName() {
        String result = getEntityClass().getSimpleName();
        if (result == null) {
            throw new OctagonException("Result is null.");
        }
        return result;
    }

    @Deprecated
    default String getEntityNameInPlural() {
        String entityName = getEntityName();

        if (entityName.endsWith("y")) {
            return entityName.substring(0, entityName.length() - 1) + "ies";
        }
        if (entityName.endsWith("x")) {
            return entityName + "es";
        }
        return entityName + "s";
    }

    @Deprecated
    default String getHumanEntityName() {
        String result = NamingConventionConvertor.convert(getEntityName(), NamingConvention.JAVA_CLASS, NamingConvention.HUMAN);
        result = result.substring(0, 1).toUpperCase() + result.substring(1);
        return result;
    }

    @Deprecated
    default String getHumanEntityNameInPlural() {
        String result = NamingConventionConvertor.convert(getEntityNameInPlural(), NamingConvention.JAVA_CLASS, NamingConvention.HUMAN);
        result = result.substring(0, 1).toUpperCase() + result.substring(1);
        return result;
    }

    //String[] toObjectArray();
    String[] toStringArray();

    @Deprecated
    List<EntityAttribute> getSchema();

    default EntitySchema getEntitySchema() {
        //todo : to be implemented
        return null;
    }

    @Deprecated
    default String convertBooleanToString(Boolean b) {
        return b.booleanValue() ? YES : NO;
    }

    String YES = "Yes";
    String NO = "No";

    @Deprecated
    default String executeCondition(boolean b, String ifPassed, String ifFailed) {
        return b ? ifPassed : ifFailed;
    }

    @Deprecated
    default String checkForNull(Object objectToCheck) {
        boolean b = objectToCheck == null;
        return executeCondition(b, "", ((objectToCheck instanceof String) ? (String) objectToCheck : objectToCheck.toString()));
    }

    @Deprecated
    default String getEmptyStringIfNullOrBlank(String s) {
        return s == null || s.isBlank() ? "" : s;
    }

    @Deprecated
    default void validateCreate() {

    }
    default void autofillIfNeeded(Entity entity, RepositoryRegistry repositoryRegistry) {
        
    }

    @Deprecated
    default void validateUpdate(Entity updatedEntity) {

    }

    @Deprecated
    default void validateDelete() {

    }

    @Deprecated
    default void validateQuestionAnswers(Map<String, String> answers, ActionType actionType) {

    }

    default String getPropertyValue(String propertyName) {
        int index = 0;
        int foundIndex = -1;
        for (EntityAttribute e : getSchema()) {
            if (e.getName().equals(propertyName)) {
                foundIndex = index;
                break;
            }
            index++;
        }
        if (foundIndex == -1) {
            throw new OctagonException("Property was not found for property name : " + propertyName + " in " + this.toString());
        }
        return this.toStringArray()[foundIndex];
    }

    default void setPreselectionProperty(String value) {
        //nothing to do in this default implementation
    }

    /**
     * String array of related named lists, which should be listed for the current entity detail.
     *
     * @return
     */
    @Deprecated
    default String[] getRelatedListsForEntity() {
        return null;
    }

    /**
     * String array of related actions for this entity.
     *
     * @return array of strings (name:link or only link)
     */
    @Deprecated
    default String[] getRelatedActionsForEntity() {
        return null;
    }

    default JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        Map<String, String> map = toMap();
        for (String key : map.keySet()) {
            String value = map.get(key);
            jo.add(key, value != null && value.isEmpty() ? null : value);
        }
        return jo;
    }

    default Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        int i = 0;
        String[] array = this.toStringArray();
        for (EntityAttribute e : getSchema()) {
            String key = e.getName();
            String value = array[i];
            map.put(key, value);
            i++;
        }
        return map;
    }

    default String getPreselectionPropertyName() {
        for (EntityAttribute e : getSchema()) {
            if (e.getPreselectionProperty() != null && e.getPreselectionProperty()) {
                return e.getName();
            }
        }
        return null;
    }

    default String getProperty(String key) {
        return toMap().get(key);
    }

    /**
     * Returns supported entity actions.
     *
     * @return action type array
     */
    @Deprecated
    default ActionType[] getSupportedActions() {
        return ActionType.values();
    }

    /**
     * @return null, if everything is OK, otherwise the missing field
     */
    default String hasAllMandatoryFields() {
        JsonObject jo = toJsonObject();
        for (EntityAttribute evs : getSchema()) {
            String key = evs.getName();
            if ((evs.isMandatory() || evs.isPreselectionProperty() || evs.getName().equals("name"))) {
                if (jo.getJsonValueType(key) != JsonValueType.NULL && !jo.getString(key).isEmpty()) {
                    //nothing to do
                } else {
                    return key;
                }
            } else {
                //System.err.println("field " + key + " is not mandatory");
            }
        }
        return null;
    }

    /**
     * @return question answers
     */
    default List<String> listQuestionsToAsk() {
        return null;
    }

    default String getDefaultOrder() {
        return null;
    }

    default String getDbPrefix() {
        return null;
    }

    default boolean doLogActions() {
        return true;

    }
}

