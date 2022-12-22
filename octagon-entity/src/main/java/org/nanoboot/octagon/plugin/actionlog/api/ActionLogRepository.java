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

package org.nanoboot.octagon.plugin.actionlog.api;

import org.nanoboot.octagon.entity.core.ActionType;
import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.octagon.entity.core.EntityReference;
import org.nanoboot.octagon.entity.api.Repository;
import org.nanoboot.octagon.plugin.actionlog.classes.ActionLog;

import java.util.UUID;
import org.nanoboot.powerframework.time.moment.UniversalDateTime;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public interface ActionLogRepository extends Repository<ActionLog> {
    default void persistActionLog(
            ActionType actionType,
            Entity entityBefore,
            Entity entityAfter,
            Entity emptyEntity,
            String comment) {
        String entityName = emptyEntity.getEntityName();
        if (entityName.equals("ActionLog") || !emptyEntity.doLogActions()) {
            //nothing to do
            return;
        }
        ActionLog actionLog = new ActionLog();
        actionLog.setId(UUID.randomUUID());
        actionLog.setCurrentUdt();
        actionLog.setActionType(actionType);
        actionLog.setType(entityName);

        actionLog.setObjectId(entityBefore, entityAfter);


        actionLog.setData(entityBefore, entityAfter, emptyEntity);
        String actionLogName = actionType.name().toLowerCase() + " " + actionLog.getType() /*+ " " + actionLog.getObjectId()*/;
        actionLog.setName(actionLogName);
        actionLog.setComment(comment);
        if(actionType == ActionType.CREATE || actionType == ActionType.UPDATE || actionType == ActionType.DELETE) {
            Integer maxRevisionNumber = findMaxRevisionNumber(actionLog.getType(), actionLog.getObjectId().toString());
            actionLog.setRevisionNumber(maxRevisionNumber == null ? 0 : ++maxRevisionNumber);
        }

        create(actionLog);
    }
    default Integer findMaxRevisionNumber(String type, String objectId) {
        return findMaxRevisionNumber(new EntityReference(type, objectId));
    }
    Integer findMaxRevisionNumber(EntityReference entityReference);
    UniversalDateTime getDateOfLastModification(String type, String objectId);
}
