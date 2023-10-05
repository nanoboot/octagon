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
package org.nanoboot.octagon.jakarta.utils;

import org.nanoboot.octagon.entity.OctagonException;

/**
 *
 * @author robertvokac
 */
public class OctagonJakartaUtils {
    private static String APPLICATION_CODE = null;
    public static final String OCTAGON_APPLICATION_CODE = "octagon.application-code";
    
    private OctagonJakartaUtils() {
        //Not mean to be instantiated
    }
    public static String getApplicationCode() {
        if(APPLICATION_CODE == null) {
            APPLICATION_CODE = System.getProperty(OCTAGON_APPLICATION_CODE);
            if(APPLICATION_CODE == null || APPLICATION_CODE.isBlank()) {
                throw new OctagonException("Missing mandatory System property " + OCTAGON_APPLICATION_CODE);
            }
        }
        return APPLICATION_CODE;
    }
    
}
