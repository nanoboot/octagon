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

package org.nanoboot.octagon.struts.actions;

import com.opensymphony.xwork2.ActionSupport;
import org.nanoboot.octagon.entity.core.ActionType;
import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.octagon.plugin.actionlog.api.ActionLogRepository;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public abstract class OctagonAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
    public static final String PROCESS_FORM_PARAMETER = "__processForm";

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }
    public String execute() {
        return "success";
    }
    public boolean isGoingToProcessForm() {
        String __processForm = this.request.getParameter("__processForm");
        return __processForm != null && __processForm.equals("yes");
    }
    @Deprecated
    public Boolean getBooleanParam(String key) {
        Integer integer = getIntegerParam(key);
        Boolean result = integer == null ? null : integer != 0;
        return result;
    }
    @Deprecated
    public Integer getIntegerParam(String key) {
        String value = getParam(key);
        Integer integer = value == null ? null : Integer.valueOf(value);
        return integer;
    }
    @Deprecated
    public String getParam(String key) {
        String value = this.request.getParameter(key);
        return (value == null || value.isEmpty()) ? null : value;
    }
    protected void setMessage(String message) {
        Object currentMessage = request.getAttribute("message");
        if(currentMessage != null) {
            message = currentMessage + " " + message;
        }

        this.request.setAttribute("message", "Message: " + message);
    }
    protected void setError(String error) {
        Object currentError = request.getAttribute("error");
        if(currentError != null) {
            error = currentError + " " + error;
        }
        this.request.setAttribute("error", "Error: " + error);
    }
    protected void persistActionLog(
            ActionLogRepository actionLogRepository,
            ActionType actionType,
            Entity entityBefore,
            Entity entityAfter,
            Entity emptyEntity,
            String comment) {
        actionLogRepository.persistActionLog(actionType, entityBefore,
                entityAfter, emptyEntity, comment);
    }
}
