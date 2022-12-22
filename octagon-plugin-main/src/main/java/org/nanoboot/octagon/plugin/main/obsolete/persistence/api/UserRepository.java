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

import org.nanoboot.octagon.plugin.main.classes.User;

import java.util.List;
import java.util.UUID;

/**
 * User repository.
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public interface UserRepository {
    /**
     * Finds user by its id.
     * @param id id of the user
     * @return User instance
     */
    User findUserById(UUID id);

    /**
     * Finds user by nick.
     * @param nick nick to be used
     * @return user instance
     */
    User findUserByNick(String nick);

    /**
     * Updates the user.
     * @param user user to update
     */
    void updateUser(User user);

    /**
     * Deactivates the user.
     * @param user user to deactivate
     */
    void deactivateUser(User user);

    /**
     * Adds user.
     * @param user user to add
     */
    void addUser(User user);

    /**
     * Lists all users.
     * @return all users
     */
    List<User> listUsers();
}
