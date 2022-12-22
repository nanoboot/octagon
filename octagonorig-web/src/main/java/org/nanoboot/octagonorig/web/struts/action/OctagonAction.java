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

import com.opensymphony.xwork2.ActionSupport;
import org.nanoboot.octagonorig.web.struts.RequestAttributes;
import org.nanoboot.octagonorig.web.struts.RequestInfo;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public abstract class OctagonAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
    protected HttpServletRequest request;

    protected HttpServletResponse response;
    private WebApplicationContext context;
    protected RequestInfo requestInfo;

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    public final String execute() {
        init();
        String result = null;
        result = authorize();
        boolean authorized = result != null;
        if (authorized) {
            return result;
        }
        result = process();
        return result;

    }

    /**
     * Intended to be overridden
     */
    public void init() {

        this.requestInfo = new RequestInfo(request);
        this.request.setAttribute(RequestAttributes.REQUEST_INFO, requestInfo);

    }

    /**
     * return null, if the authorization was successful
     *
     * @return
     */
    private String authorize() {
        return null;
    }

    public abstract String process();

    public WebApplicationContext getContext() {
        if (this.context == null) {

            this.context =
                    WebApplicationContextUtils.getRequiredWebApplicationContext(
                            ServletActionContext.getServletContext()
                    );
        }
        return this.context;
    }
}
