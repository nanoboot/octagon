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
package org.nanoboot.octagon.jakarta.listeners;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.nanoboot.octagon.entity.OctagonException;
import org.nanoboot.octagon.jakarta.utils.OctagonJakartaUtils;

/**
 *
 * @author robertvokac
 */
public class ApplicationCodeListener implements ServletContextListener {

    public static String APPLICATION_CODE = null;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();

        String applicationCode = ctx.getInitParameter(OctagonJakartaUtils.OCTAGON_APPLICATION_CODE);

        if (applicationCode == null || applicationCode.isBlank()) {
            throw new OctagonException("Missing mandatory Param Name" + OctagonJakartaUtils.OCTAGON_APPLICATION_CODE);
        }
        //System.setProperty(OctagonJakartaUtils.OCTAGON_APPLICATION_CODE, applicationCode);
        APPLICATION_CODE = applicationCode;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }


}
