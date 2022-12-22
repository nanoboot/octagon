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

package org.nanoboot.octagon.plugin.api.factories;

import org.nanoboot.octagon.plugin.api.core.Plugin;
import org.nanoboot.powerframework.reflection.ReflectionUtils;
import lombok.Setter;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class PluginListFactory {

    private static final Logger LOG = LogManager.getLogger(PluginListFactory.class);
    @Setter
    private List<Class<? extends Plugin>> pluginClasses;

    @Setter
    private List<? extends PluginLoadingHandler> pluginLoadingHandlers;

    public List<Plugin> create() {
        LOG.info("PluginListFactory : adding plugins");
        List<Plugin> plugins = new ArrayList<>();
        for (Class<? extends Plugin> clazz : pluginClasses) {
            Constructor constructor = ReflectionUtils.getConstructor(clazz);
            Plugin plugin = (Plugin) ReflectionUtils.newInstance(constructor);
            plugins.add(plugin);

        }
        PluginSorter pluginSorter = new PluginSorterImpl();
        plugins = pluginSorter.sort(plugins);
        LOG.info("Plugins are going to be loaded:");

        for (Plugin p : plugins) {
            LOG.info("Loading plugin: name={}, dependsOn={}", p.getName(), p.getDependsOn());

            for (PluginLoadingHandler plh : pluginLoadingHandlers) {
                LOG.info("Applying handler {} on plugin {}.", plh.getClass().getName(), p.getId());
                plh.handle(p);
            }

        }
        return plugins;
    }
}
