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
import org.nanoboot.octagon.plugin.main.obsolete.classes.UserAssignment;

import java.util.List;
import java.util.UUID;

/**
 * User assignment repository.
 * User assignment cannot be updated.
 * The old one must be deleted and a new one must be added.
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public interface UserAssignmentRepository {
    /**
     * Finds user by its id.
     * @param id id to use
     * @return user assignment
     */
    UserAssignment findUserAssignmentById(UUID id);

    /**
     * Adds user assignment.
     * @param userAssignment user assignment to add.
     */
    void addUserAssignment(UserAssignment userAssignment);

    /**
     * Deletes user assignment.
     * @param userAssignment user assignment to delete
     */
    void deleteUserAssignment(UserAssignment userAssignment);

    /**
     * Returns all group of a user.
     * @param user user to use
     * @return user assignments
     */
    List<UserAssignment> listUserAssignmentsForUser(User user);

    /**
     * Return all users in a group.
     * @param group group to use
     * @return user assignments
     */
    List<UserAssignment> listUserAssignmentsForGroup(Group group);
}
