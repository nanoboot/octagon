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

package org.nanoboot.octagon.plugin.actionlog.impl.repos;

import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.entity.core.EntityReference;
import org.nanoboot.octagon.plugin.actionlog.api.ActionLogRepository;
import org.nanoboot.octagon.plugin.actionlog.classes.ActionLog;
import org.nanoboot.octagon.plugin.actionlog.impl.mappers.ActionLogMapper;
import org.nanoboot.octagon.entity.api.MapperApi;
import org.nanoboot.octagon.entity.impl.repos.RepositoryImpl;
import org.nanoboot.powerframework.time.moment.UniversalDateTime;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class ActionLogRepositoryImplSQLiteMyBatis extends RepositoryImpl<ActionLog> implements ActionLogRepository {

    public ActionLogRepositoryImplSQLiteMyBatis(MapperApi<ActionLog> mapper, Class type) {
        super(mapper, type);
    }

    @Override
    public Integer findMaxRevisionNumber(EntityReference entityReference) {
        if (entityReference == null) {
            throw new OctagonException("type is null");
        }
        String type = entityReference.getType();
        String objectId = entityReference.getObjectId();
        if (type == null) {
            throw new OctagonException("type is null");
        }
        if (objectId == null) {
            throw new OctagonException("objectId is null");
        }

        return ((ActionLogMapper) mapper).findMaxRevisionNumber(type, objectId);
    }

    @Override
    public UniversalDateTime getDateOfLastModification(String type, String objectId) {
        String udtString = ((ActionLogMapper) mapper).getDateOfLastModification(type, objectId);
        if(udtString == null) {
            return null;
        }
        return new UniversalDateTime(udtString);
    }

}
