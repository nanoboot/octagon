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

package org.nanoboot.octagon.plugin.api.core;

import javax.servlet.http.HttpServletRequest;
import lombok.Setter;
import org.nanoboot.octagon.core.utils.RegistryImpl;
import org.nanoboot.octagon.entity.api.Repository;
import org.nanoboot.octagon.entity.api.RepositoryRegistry;
import org.nanoboot.octagon.plugin.actionlog.api.ActionLogRepository;
import org.nanoboot.octagon.plugin.api.factories.PluginLoader;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public abstract class WebAppBase extends ObjectDependencyBase implements WebApp {

    protected RegistryImpl<Repository> repositoryRegistry;
    protected ActionLogRepository actionLogRepository;
    protected PluginLoader pluginLoader;
    @Setter
    protected HttpServletRequest httpServletRequest;

    public void addObjectDependencyPostHandler(String clazzName, Object object) {
        if (clazzName.equals(RepositoryRegistry.class.getName())) {
            this.repositoryRegistry = (RegistryImpl<Repository>) getObjectDependency(RepositoryRegistry.class);
        }
        if (clazzName.equals(ActionLogRepository.class.getName())) {
            this.actionLogRepository = (ActionLogRepository) getObjectDependency(ActionLogRepository.class);
        }
        
        if (clazzName.equals(PluginLoader.class.getName())) {
            this.pluginLoader = (PluginLoader) getObjectDependency(PluginLoader.class);
        }
    }

}