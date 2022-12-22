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
import org.nanoboot.octagon.plugin.main.obsolete.classes.Space;

import java.util.List;
import java.util.UUID;

/**
 * Space repository.
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public interface SpaceRepository {
    /**
     * Finds space by id.
     * @param id id to use
     * @return space instance
     */
    Space findSpaceById(UUID id);

    /**
     * Finds space by name.
     * @param name name to use
     * @return space instance
     */
    Space findSpaceByName(String name);

    /**
     * Updates space.
     * @param space space to update
     */
    void updateSpace(Space space);

    /**
     * Adds space.
     * @param space space to add
     */
    void addSpace(Space space);

    /**
     * Delete space.
     * @param space space to delete
     */
    void deleteSpace(Space space);

    /**
     * Lists spaces for the given database.
     * @param database database to be use
     * @return spaces
     */
    List<Space> listSpacesForDatabase(Database database);
}
