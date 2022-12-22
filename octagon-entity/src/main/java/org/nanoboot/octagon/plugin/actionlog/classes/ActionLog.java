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

package org.nanoboot.octagon.plugin.actionlog.classes;

import org.nanoboot.octagon.entity.core.ActionType;
import org.nanoboot.octagon.entity.core.EntityAttribute;
import org.nanoboot.octagon.entity.core.EntityAttributeType;
import org.nanoboot.powerframework.json.JsonObject;
import org.nanoboot.powerframework.time.moment.UniversalDateTime;
import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.entity.core.Entity;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@Data
public class ActionLog implements Entity {
    protected static List<EntityAttribute> SCHEMA;

    /**
     * UUID identification of this entity.
     */
    private UUID id;

    private UniversalDateTime universalDateTime;
    private ActionType actionType;
    //    /**
//     * example: ProductGroup
//     */
    private String type;
    private UUID objectId;

    private JsonObject data;
    private String name;
    private String comment;

    private Integer revisionNumber;

    @Override
    public void validate() {
        if (actionType.equals(ActionType.CREATE.name()) || actionType.equals(ActionType.UPDATE.name()) || actionType.equals(ActionType.DELETE.name())) {
            if (data == null) {
                throw new OctagonException("Data must not be null, if action type is create, update or delete");
            }
            if (revisionNumber == null) {
                throw new OctagonException("Revision number must not be null.");
            }
            if (revisionNumber < 0) {
                throw new OctagonException("Revision number must be zero or positive.");
            }
        }
    }

    @Override
    public void loadFromMap(Map<String, String> map) {
        setUniversalDateTime(getUniversalDateTimeParam("universalDateTime", map));
        String actionTypeParam = getStringParam("actionType", map);
        setActionType(actionTypeParam == null ? null : ActionType.valueOf(actionTypeParam));
        setType(getStringParam("type", map));
        setObjectId(getUuidParam("objectId", map));

        setData(getJsonObjectParam("data", map));
        setName(getStringParam("name", map));
        setComment(getStringParam("comment", map));

        setRevisionNumber(getIntegerParam("revisionNumber", map));
    }

    public Class getEntityClass() {
        return getClass();
    }

    @Override
    public String[] toStringArray() {
        return new String[]{
                id == null ? "" : id.toString(),

                universalDateTime == null ? "" : universalDateTime.toString(),
                actionType == null ? "" : actionType.name(),
                type == null ? "" : type,
                objectId == null ? "" : objectId.toString(),

                data == null ? "" : data.toString(),
                name == null ? "" : name,
                comment == null ? "" : comment,

                revisionNumber == null ? "" : revisionNumber.toString(),
        };
    }

    @Override
    public List<EntityAttribute> getSchema() {
        if (SCHEMA == null) {
            SCHEMA = new ArrayList<>();

            SCHEMA.add(EntityAttribute.getIdEntityAttribute());

            SCHEMA.add(new EntityAttribute("universalDateTime", EntityAttributeType.UNIVERSAL_DATE_TIME).withMandatory(true));
            SCHEMA.add(new EntityAttribute("actionType", Arrays.asList(ActionType.values()).stream().map(ActionType::name).collect(Collectors.toList())).withMandatory(true));
            SCHEMA.add(new EntityAttribute("type").withMandatory(true));
            SCHEMA.add(new EntityAttribute("objectId", EntityAttributeType.UUID));

            SCHEMA.add(new EntityAttribute("data", EntityAttributeType.JSON_OBJECT));
            SCHEMA.add(new EntityAttribute("name").withMandatory(true).withMandatory(true));
            SCHEMA.add(new EntityAttribute("comment", EntityAttributeType.TEXT_AREA));

            SCHEMA.add(new EntityAttribute("revisionNumber", EntityAttributeType.INTEGER));
        }
        return SCHEMA;
    }

    @Override
    public ActionType[] getSupportedActions() {
        return new ActionType[]{
                ActionType.MENU,
                ActionType.READ,
                ActionType.LIST};
    }

    public void setCurrentUdt() {
        this.setUniversalDateTime(UniversalDateTime.now());
    }

    public void setObjectId(Entity entityBefore, Entity entityAfter) {
        UUID id = entityAfter != null ? entityAfter.getId() : (entityBefore != null ? entityBefore.getId() : null);
        setObjectId(id);
    }

    public void setData(Entity entityBefore, Entity entityAfter, Entity emptyEntity) {
        JsonObject data = new JsonObject();
        JsonObject entityBeforeJO = entityBefore == null ? new JsonObject() : entityBefore.toJsonObject();
        JsonObject entityAfterJO = entityAfter == null ? new JsonObject() : entityAfter.toJsonObject();

        if (getActionType() == ActionType.UPDATE) {
            JsonObject updated = new JsonObject();
            for (String key : emptyEntity.getSchema().stream().map(EntityAttribute::getName).collect(Collectors.toList())) {
                Object beforeValue = entityBeforeJO.hasKey(key) ? entityBeforeJO.get(key) : null;
                String before = beforeValue == null ? null : (String) beforeValue;
                Object afterValue = entityAfterJO.hasKey(key) ? entityAfterJO.get(key) : null;
                String after = afterValue == null ? null : (String) afterValue;
                if (before == null && after == null) {
                    continue;
                }
                if ((before == null && after != null) || (before != null && after == null) || (!before.equals(after))) {
                    updated.add(key, new JsonObject().add("before", before).add("after", after));
                }
            }

            data.add("updated", updated);
        }

        if (getActionType() == ActionType.CREATE) {
            data.add("created", entityAfterJO);
        }
        if (getActionType() == ActionType.DELETE) {
            data.add("deleted", entityBeforeJO);
        }
        if (data.isEmpty()) {
            data = null;
        }
        this.setData(data);
    }
}
