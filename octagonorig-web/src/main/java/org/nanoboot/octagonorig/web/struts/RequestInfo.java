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

import org.nanoboot.octagonorig.domain.User;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class RequestInfo {

    private final HttpServletRequest request;
    private final HttpSession session;
    private final Map<String, Object> map = new HashMap<>();

    @Getter
    private User user;

    public static RequestInfo getFromRequest(HttpServletRequest request) {
        return (RequestInfo) HttpAttributes.getAttributeIfExists(request, RequestAttributes.REQUEST_INFO);
    }

    public RequestInfo(HttpServletRequest newRequest) {
        this.request = newRequest;

        session = request.getSession(false);

        if (getUserType().isConcrete()) {
            user = (User) SessionAttributes.getAttributeIfExists(request, SessionAttributes.USER);
        }
    }

    public UserType getUserType() {

        if (session == null || session.getAttribute(SessionAttributes.CIRCLE_SESSION_ID) == null) {
            return UserType.ANONYMOUS;
        } else {
            return UserType.CONCRETE;
        }
    }


    public void addProperty(String string, Object object) {
        this.map.put(string, object);
    }

    public Object getProperty(String string) {
        return map.get(string);
    }

}
