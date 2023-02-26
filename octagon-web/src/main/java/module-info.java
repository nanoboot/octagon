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
module octagon.web {
    requires spring.context;
    requires javax.servlet.api;
    requires struts2.core;
    requires octagon.struts;
    requires octagon.whining.impl;
    requires octagon.reminder.impl;
    requires octagon.mail.impl;
    requires octagon.plugin.system;
    requires octagon.plugin.api;
    requires octagon.entity;
    requires lombok;
//    opens org.nanoboot.octagon.web;
//    opens org.nanoboot.octagon.web.rest.controllers;
}
