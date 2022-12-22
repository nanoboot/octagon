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

package org.nanoboot.octagonorig.web.struts;

import org.nanoboot.octagonorig.core.exceptions.OctagonException;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class HttpAttributes {

    public static Object getAttribute(HttpServletRequest request, String key, Object defaultObject){
        Object object = request.getAttribute(key);
        if (object == null) {
            return defaultObject;
        }
        return object;
    }

    public static Object getAttributeIfExists(HttpServletRequest request, String key){
        Object object = getAttribute(request, key, null);
        if (object == null) {
            throw new OctagonException(key + " http attribute expected, but not found");
        }
        return object;
    }

}
