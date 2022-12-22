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

package org.nanoboot.octagon.plugin.main.obsolete.persistence.api;

import org.nanoboot.octagon.plugin.main.obsolete.classes.Log;
import org.nanoboot.octagon.plugin.main.classes.User;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Log repository.
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public interface LogRepository {
    /**
     * Finds log by id.
     * @param id id to use
     * @return log instance
     */
    Log findLogById(UUID id);

    /**
     * Adds log.
     * @param log log to add
     */
    void addLog(Log log);

    /**
     * Deletes log.
     * @param log log to delete
     */
    void deleteLog(Log log);

    /**
     * Lists logs.
     * @param types types of the logs
     * @param from start date
     * @param to end date
     * @param user user to use
     * @return list of logs
     */
    List<Log> listLogs(String[] types, Date from, Date to, User user);

}
