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

package org.nanoboot.octagon.struts.actions.webapp;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.nanoboot.octagon.struts.actions.*;
import org.nanoboot.octagon.entity.api.RepositoryRegistry;
import org.nanoboot.octagon.plugin.actionlog.api.ActionLogRepository;
import org.nanoboot.octagon.entity.api.LabelRepository;
import org.nanoboot.octagon.entity.api.SqlQueryRepository;
import org.nanoboot.octagon.core.exceptions.OctagonException;

import lombok.Setter;
import org.nanoboot.octagon.plugin.api.core.PluginStub;
import org.nanoboot.octagon.plugin.api.core.WebApp;
import org.nanoboot.octagon.plugin.api.factories.PluginLoader;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class WebAppAction extends OctagonAction {

    @Setter
    private RepositoryRegistry repositoryRegistry;
    @Setter
    private LabelRepository labelRepository;

    private SqlQueryRepository sqlQueryRepository;
    private ActionLogRepository actionLogRepository;
    @Setter
    private PluginLoader pluginLoader;

    public String execute() {
        if (repositoryRegistry == null) {
            throw new OctagonException("Spring dependency repositoryRegistry is not satisfied (repositoryRegistry is null).");
        } else {
            sqlQueryRepository = (SqlQueryRepository) repositoryRegistry.find("SqlQuery");
            actionLogRepository = (ActionLogRepository) repositoryRegistry.find("ActionLog");
        }
//        this.setMessage("<a href=\"sqlExplorer\">Reset</a>");
        String webAppClassSimpleName = request.getParameter(WEB_APP_CLASS_SIMPLE_NAME);
        if (webAppClassSimpleName == null) {
            throw new OctagonException("Parameter " + WEB_APP_CLASS_SIMPLE_NAME + " is mandatory, but was not passed.");
        }
        String webAppClassFullName = null;
        for (PluginStub ps : pluginLoader.getPluginStubs()) {
            for (String webAppClassFullNameIterated : ps.getWebAppClasses()) {

                if (webAppClassFullNameIterated.endsWith("." + webAppClassSimpleName)) {
                    System.out.println("Found a class " + webAppClassFullNameIterated + ", which implements web app" + webAppClassSimpleName);
                    webAppClassFullName = webAppClassFullNameIterated;
                    break;
                }
            }
        }
        if (webAppClassFullName == null) {
            throw new OctagonException("There was found no class, which implements webapp " + webAppClassSimpleName + ".");
        }
        String fragment = null;
        try {
            fragment = startWebAppBatch(webAppClassFullName);
        } catch (Exception e) {
            String msg = "Starting web app " + webAppClassFullName + " failed: " + e.getMessage();
            System.err.println(msg);
            e.printStackTrace();
            throw new OctagonException(msg);
        }

//                sqlQueryRepository.create(sqlQuery);
//                persistActionLog(actionLogRepository, ActionType.CREATE, null, sqlQuery, sqlQuery, null);
//                String msg = "Sql query was saved as <a href=\"read?className=SqlQuery&id=" + sqlQuery.getId().toString() + "\">" + sqlQuery.getName() + "</a>";
//                setMessage(msg);
        this.request.setAttribute("fragment", fragment);
        return "success";
    }
    private static final String WEB_APP_CLASS_SIMPLE_NAME = "webAppClassSimpleName";

    private String startWebAppBatch(String webAppClass) {
        System.out.println("Starting webApp " + webAppClass);
        Class webAppClazz;
        try {
            webAppClazz = Class.forName(webAppClass);
        } catch (ClassNotFoundException ex) {
            throw new OctagonException(ex.getMessage());
        }
        Constructor constructor;
        try {
            constructor = webAppClazz.getConstructor();
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new OctagonException(ex.getMessage());
        }
        WebApp webApp;
        try {
            webApp = (WebApp) constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new OctagonException(ex.getMessage());
        }
        webApp.setHttpServletRequest(request);
        webApp.addObjectDependency(RepositoryRegistry.class.getName(), repositoryRegistry);
        webApp.addObjectDependency(ActionLogRepository.class.getName(), actionLogRepository);
        webApp.addObjectDependency(PluginLoader.class.getName(), pluginLoader);
        String webAppAsHtmlString = "<a href=\"webapp?webAppClassSimpleName=" + webAppClazz.getSimpleName()+ "\">Home</a><br>" + webApp.toHtml();
        return webAppAsHtmlString;
    }
}
