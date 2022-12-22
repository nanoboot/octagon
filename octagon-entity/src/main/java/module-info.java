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

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
module octagon.entity {
    exports org.nanoboot.octagon.entity.core;
    exports org.nanoboot.octagon.entity.misc;
    exports org.nanoboot.octagon.entity.typehandlers;
    exports org.nanoboot.octagon.plugin.whining.classes;
    exports org.nanoboot.octagon.plugin.reminder.classes;
    exports org.nanoboot.octagon.plugin.favorite.classes;
    exports org.nanoboot.octagon.plugin.actionlog.api;
    exports org.nanoboot.octagon.plugin.favorite.api;
    exports org.nanoboot.octagon.entity.api;
    exports org.nanoboot.octagon.plugin.pinning.api;
    exports org.nanoboot.octagon.entity.classes;
    exports org.nanoboot.octagon.plugin.pinning.classes;
    exports org.nanoboot.octagon.plugin.reminder.api;
    exports org.nanoboot.octagon.plugin.whining.api;
    exports org.nanoboot.octagon.entity.impl.repos;
    exports org.nanoboot.octagon.entity.utils;
    exports org.nanoboot.octagon.plugin.actionlog.classes;
    exports org.nanoboot.octagon.entity.impl.mappers;
    exports org.nanoboot.octagon.plugin.actionlog.impl.mappers;
    exports org.nanoboot.octagon.plugin.actionlog.impl.repos;
    exports org.nanoboot.octagon.plugin.pinning.impl.mappers;
    exports org.nanoboot.octagon.plugin.pinning.impl.repos;
    exports org.nanoboot.octagon.plugin.reminder.impl.mappers;
    exports org.nanoboot.octagon.plugin.reminder.impl.repos;
    exports org.nanoboot.octagon.plugin.favorite.impl.mappers;
    exports org.nanoboot.octagon.plugin.favorite.impl.repos;
    exports org.nanoboot.octagon.plugin.whining.impl.mappers;
    exports org.nanoboot.octagon.plugin.whining.impl.repos;
    exports org.nanoboot.octagon.plugin.reminder.impl.typehandlers;
    requires org.mybatis;
    requires java.sql;
    requires spring.jdbc;
    requires octagon.core;
    requires powerframework.json;
    requires powerframework.time;
    requires powerframework.utils;
    requires powerframework.reflection;
    requires powerframework.sql;
    requires lombok;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j;
}
