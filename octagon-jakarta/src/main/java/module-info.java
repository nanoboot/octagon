///////////////////////////////////////////////////////////////////////////////////////////////
// Octagon.
// Copyright (C) 2023-2023 the original author or authors.
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
 * @since 2.0.0
 */
module octagon.jakarta {
    requires octagon.entity;
    requires octagon.persistence.api;
    requires octagon.persistence.impl.mock;
    requires spring.context;
    requires jakarta.jakartaee.web.api;
    requires lombok;
    requires spring.web;
    requires powerframework.time;
    requires powerframework.io;
    requires org.xerial.sqlitejdbc;
    requires asciidoctorj;
    requires asciidoctorj.api;
    requires dev.mccue.guava.io;
    exports org.nanoboot.octagon.jakarta.filters;
    exports org.nanoboot.octagon.jakarta.listeners;
    exports org.nanoboot.octagon.jakarta.utils;
}
