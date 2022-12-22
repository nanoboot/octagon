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

import org.nanoboot.octagon.plugin.main.obsolete.classes.Node;
import org.nanoboot.octagon.plugin.main.obsolete.classes.Directory;

import java.util.List;
import java.util.UUID;

/**
 * Directory repository.
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public interface DirectoryRepository {
    /**
     * Finds directory by its id.
     * @param id id to use
     * @return directory
     */
    Directory findDirectoryById(UUID id);

    /**
     * Updates directory.
     * @param directory directory to update
     */
    void updateDirectory(Directory directory);

    /**
     * Adds directory.
     * @param directory directory to add.
     */
    void addDirectory(Directory directory);

    /**
     * Deletes directory.
     * @param directory directory to delete.
     */
    void deleteDirectory(Directory directory);

    /**
     * Lists directories for a node.
     * @param node node to use
     * @return directories
     */
    List<Directory> listDirectoriesForNode(Node node);
}
