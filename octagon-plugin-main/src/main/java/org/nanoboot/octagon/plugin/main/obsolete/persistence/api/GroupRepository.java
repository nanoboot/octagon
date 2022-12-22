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

import org.nanoboot.octagon.plugin.main.obsolete.classes.Group;
import org.nanoboot.octagon.plugin.main.classes.User;

import java.util.List;
import java.util.UUID;

/**
 * Group repository.
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public interface GroupRepository {
    /**
     * Finds group by its id.
     * @param id id of the group
     * @return group
     */
    Group findGroupById(UUID id);

    /**
     * Finds group by its nick.
     * @param nick nick to be used
     * @return group
     */
    Group findGroupByNick(String nick);

    /**
     * Updates group.
     * @param group group to update
     */
    void updateGroup(Group group);

    /**
     * Adds group.
     * @param group group to add
     */
    void addGroup(Group group);

    /**
     * Lists all groups.
     * @return groups
     */
    List<Group> listGroups();

    /**
     * Lists groups using the owner.
     * @param owner User
     * @return groups
     */
    List<Group> listGroups(User owner);
}
