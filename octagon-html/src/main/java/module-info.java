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
module octagon.html {
    exports org.nanoboot.octagon.html.forms;
    exports org.nanoboot.octagon.html.fragments;
    exports org.nanoboot.octagon.html.links;
    exports org.nanoboot.octagon.html.misc;
    exports org.nanoboot.octagon.html.tables;
    exports org.nanoboot.octagon.html.webpage;
    exports org.nanoboot.octagon.html.messages;
    requires lombok;
    requires powerframework.web;
    requires powerframework.xml;
    requires powerframework.reflection;
    requires powerframework.utils;
    requires octagon.core;
    requires spring.web;
    requires octagon.entity;
}
