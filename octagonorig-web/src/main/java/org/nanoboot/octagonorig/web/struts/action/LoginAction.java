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

package org.nanoboot.octagonorig.web.struts.action;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpSession;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@Data
public class LoginAction extends OctagonAction {

//    private AuthenticationService authenticationService = getContext().getBean("authenticationService", AuthenticationService.class);

    private static final Logger LOG = LogManager.getLogger(LoginAction.class);

    @Override
    public String process() {
        if (requestInfo.getUserType().isConcrete()) {
            return ActionResult.ACCESS_DENIED.getText();
        }

        String nick=request.getParameter("nick");
        String password=request.getParameter("password");

        boolean authenticated = true;//authenticationService.authenticate(nick, password);

        LOG.info("Authentication result for nick " + nick + ":" + authenticated);
        System.err.println("Authentication result for nick " + nick + ":" + authenticated);

        if (authenticated) {
            HttpSession session=request.getSession();
            session.setAttribute("nick",nick);
            //TODO set up session ...
            return ActionResult.OK.getText();
        } else return ActionResult.ACCESS_DENIED.getText();
    }
}
