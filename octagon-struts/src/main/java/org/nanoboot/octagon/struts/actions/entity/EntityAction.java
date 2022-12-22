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

package org.nanoboot.octagon.struts.actions.entity;

import org.nanoboot.octagon.entity.api.*;
import org.nanoboot.octagon.entity.api.LabelRepository;
import org.nanoboot.octagon.entity.api.NamedListRepository;
import org.nanoboot.octagon.entity.api.RelatedListRepository;
import org.nanoboot.octagon.entity.core.ActionType;
import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.octagon.plugin.actionlog.api.ActionLogRepository;
import org.nanoboot.powerframework.reflection.ReflectionUtils;
import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.struts.actions.OctagonAction;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public abstract class EntityAction extends OctagonAction {
    @Getter
    @Setter
    protected RepositoryRegistry repositoryRegistry;

    @Getter
    @Setter
    protected LabelRepository labelRepository;
    @Getter
    @Setter
    protected NamedListRepository namedListRepository;
    @Getter
    @Setter
    protected RelatedListRepository relatedListRepository;
    @Getter
    @Setter
    protected String env = "unknown";

    public String execute() {
        return "success";
    }

    protected Repository<Entity> findEntityRepo() {
        String className = findClassName();
        return this.repositoryRegistry.find(className);
    }

    protected String findClassName() {
        String className = request.getParameter("className");
        if (className == null) {
            throw new OctagonException("Parameter className is required, but was not provided.");
        }
        return className;
    }

    protected void setTitle(String entityName) {
        String title = findClassName();
        if (entityName != null && !entityName.isBlank()) {
            title = title + " - " + entityName;
        }
        this.request.setAttribute("__title", title);
    }

    protected Entity returnNewEmptyInstance() {
        Repository repo = findEntityRepo();
        Class thisClass = repo.getClassOfType();
        Constructor constructor = ReflectionUtils.getConstructor(thisClass);
        Entity emptyInstance = (Entity) ReflectionUtils
                .newInstance(constructor);
        if (emptyInstance == null) {
            throw new OctagonException("emptyInstance is null");
        }
        return emptyInstance;
    }

    protected void throwExceptionIfRepositoryRegistryIsNull() {
        if (repositoryRegistry == null) {
            throw new OctagonException("repositoryRegistry is null - " + getClass().getName());
        }
    }

    protected Map<String, String> getParameterMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            map.put(key, request.getParameter(key));
        }
        return map;
    }

    public ActionType getActionType() {
        String className = this.getClass().getSimpleName();
        String classNameUppercased = className.toUpperCase();
        for (ActionType at : ActionType.values()) {
            if (classNameUppercased.startsWith(at.name())) {
                return at;
            }
        }
        throw new OctagonException("There was found no ActionType for Action class with name " + className);
    }

    /**
     * @param e
     * @return null, if the action is authorized, otherwise a String with the description of the reason, this action is not authorized to be processed.
     */
    public String authorizeActionTypeForEntity(Entity e) {
        String msgStart = "Action " + getActionType() + " is not authorized for entity " + e.getEntityName() + ". ";
        if (e.getSupportedActions() == null || e.getSupportedActions().length == 0) {
            return msgStart + "There is no authorized action.";
        }
        for (ActionType a : e.getSupportedActions()) {
            if (a.equals(getActionType())) {
                return null;
            }
        }
        return msgStart + "Action not found in supported action types.";
    }

    protected void persistActionLog(
            Entity entityBefore,
            Entity entityAfter,
            String comment) {
        persistActionLog((ActionLogRepository) repositoryRegistry.find("ActionLog"), getActionType(), entityBefore, entityAfter, returnNewEmptyInstance(), comment);
    }

    protected void printWarningTextIfNotProd() {
        if ("prod".equals(env.trim())) {
            return;
        }
        String js = "document.addEventListener('DOMContentLoaded', function GetFavColor() {var color = '#C0C0C0';if (color != '') {document.body.style.backgroundColor = color;}});";
        String html = "<marquee behavior=\"scroll\" direction=\"left\" scrollamount=\"4\" style=\"font-weight:bold;font-size:150%;font-family:'sans-serif';background:red;color:black;border:2px solid black; padding:10px; margin:10px;\">This is not production! This is only " + env + " environment! Do not type here any serious data!!!</marquee>";
        this.setError(html + "<script>" + js + "</script>");
    }
}
