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

package org.nanoboot.octagon.plugin.main.obsolete.persistence.impl.mappers;

import org.nanoboot.octagon.plugin.main.classes.User;

import java.util.List;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public interface UserMapper {
    /**
     * Finds user by id.
     * @param id unique identificator of the user
     * @return User instance
     */
    User findUserById(String id);

    /**
     * Finds user by nick.
     * @param nick unique identificator of the user
     * @return User instance
     */
    User findUserByNick(String nick);

    /**
     * Updates user in database.
     * @param user User instance
     */
    void updateUser(User user);

    /**
     * Deactivates a user.
     * @param id id of the user
     */
    void deactivateUser(String id);

    /**
     * Creates a new user in database.
     * @param user User instance
     */
    void addUser(User user);

    /**
     * Returns all users.
     * @return list of all users.
     */
    List<User> listUsers();
}

