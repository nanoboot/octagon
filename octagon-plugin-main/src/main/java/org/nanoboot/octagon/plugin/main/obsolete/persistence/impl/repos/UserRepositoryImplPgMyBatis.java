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

package org.nanoboot.octagon.plugin.main.obsolete.persistence.impl.repos;

import org.nanoboot.octagon.plugin.main.classes.User;
import org.nanoboot.octagon.plugin.main.obsolete.persistence.api.UserRepository;
import org.nanoboot.octagon.plugin.main.obsolete.persistence.impl.mappers.UserMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class UserRepositoryImplPgMyBatis implements UserRepository {

    /**
     * Mapper for user table.
     */
    @Getter
    @Setter
    private UserMapper userMapper;
    @Override
    public final User findUserById(final UUID id) {
        return userMapper.findUserById(id.toString());
    }

    @Override
    public final User findUserByNick(final String nick) {
        return userMapper.findUserByNick(nick);
    }

    @Override
    public final void updateUser(final User user) {
        userMapper.updateUser(user);
    }

    @Override
    public final void deactivateUser(final User user) {
        userMapper.deactivateUser(user.getId().toString());
    }

    @Override
    public final void addUser(final User user) {
        System.err.println(user.toString());
        userMapper.addUser(user);
    }

    @Override
    public final List<User> listUsers() {
        return userMapper.listUsers();
    }
}
