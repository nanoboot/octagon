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

package org.nanoboot.octagonorig.web.rest.controllers;

import org.nanoboot.octagonorig.domain.User;
import org.nanoboot.octagonorig.persistence.api.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {
    /**
     * Repository used to Access users.
     */
//    private UserRepository userRepository = new UserRepositoryMock();

    @Autowired
    private UserRepository userRepository;

    class UserRepositoryMock implements UserRepository {

        @Override
        public User findUserById(UUID id) {
            return null;
        }

        @Override
        public User findUserByNick(String nick) {
            return null;
        }

        @Override
        public void updateUser(User user) {

        }

        @Override
        public void deactivateUser(User user) {

        }

        @Override
        public void addUser(User user) {
            System.err.println(user.toString());
        }

        @Override
        public List<User> listUsers() {
            return null;
        }
    }
    /**
     * Creates new user.
     * @param user User to be added
     * @return result of this operation
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public final @ResponseBody ResponseEntity<Object> add(@RequestBody final User user) {

        userRepository.addUser(user);
        //Create resource location
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        //Send location in response
        System.out.println(location);
        return ResponseEntity.created(location).build();

    }
}
