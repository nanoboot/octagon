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

import org.nanoboot.octagon.plugin.main.obsolete.classes.Database;

import java.util.List;
import java.util.UUID;

/**
 * Database repository.
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public interface DatabaseRepository {
    /**
     * Finds database by id.
     * @param id id to use
     * @return database instance
     */
    Database findDatabaseById(UUID id);

    /**
     * Finds database by name.
     * @param name to use
     * @return database instance
     */
    Database findDatabaseByName(String name);

    /**
     * Updates database.
     * @param database database to update
     */
    void updateDatabase(Database database);

    /**
     * Adds database.
     * @param database database to add
     */
    void addDatabase(Database database);

    /**
     * Deletes database.
     * @param database database to delete
     */
    void deleteDatabase(Database database);

    /**
     * Lists all databases.
     * @return databases
     */
    List<Database> listDatabases();
}
