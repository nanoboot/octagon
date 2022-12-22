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

import org.nanoboot.octagon.plugin.main.obsolete.classes.UserDetail;
import org.nanoboot.octagon.plugin.main.obsolete.persistence.api.UserDetailRepository;
import org.nanoboot.octagon.plugin.main.obsolete.persistence.impl.mappers.UserDetailMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class UserDetailRepositoryImplPgMyBatis implements UserDetailRepository {

    @Getter
    @Setter
    private UserDetailMapper userDetailMapper;
    @Override
    public UserDetail findUserDetailById(UUID id) {
        return null;
    }

    @Override
    public UserDetail findUserDetailByUser(UUID userId) {
        return null;
    }

    @Override
    public void updateUserDetail(UserDetail userDetail) {
        userDetailMapper.updateUserDetail(userDetail);
    }

    @Override
    public void addUserDetail(UserDetail userDetail) {
        userDetailMapper.addUserDetail(userDetail);

    }

}
