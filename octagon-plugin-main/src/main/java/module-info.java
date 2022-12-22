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

import org.nanoboot.octagon.plugin.main.obsolete.persistence.api.UserDetailRepository;
import org.nanoboot.octagon.plugin.main.obsolete.persistence.api.UserRepository;
import org.nanoboot.octagon.plugin.main.obsolete.persistence.impl.repos.UserDetailRepositoryImplPgMyBatis;
import org.nanoboot.octagon.plugin.main.obsolete.persistence.impl.repos.UserRepositoryImplPgMyBatis;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
module octagon.plugin.main {
    requires lombok;
    requires org.mybatis;
    requires java.sql;
    requires org.json;
    requires octagon.entity;
    requires octagon.core;
    requires powerframework.time;
    requires powerframework.json;
    requires octagon.plugin.api;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j;
    exports org.nanoboot.octagon.plugin.main.classes;
    provides UserDetailRepository with UserDetailRepositoryImplPgMyBatis;
    provides UserRepository with UserRepositoryImplPgMyBatis;
}
