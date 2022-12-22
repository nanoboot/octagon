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

import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.plugin.api.core.Plugin;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class PluginClassesListFactory {
    @Setter
    private List<Class<? extends Plugin>> pluginClassesListFromConfpath;

    public List<Class<? extends Plugin>> create() {
        List<Class<? extends Plugin>> list = new ArrayList<>();

        list.addAll(loadBundledPlugins());
        list.addAll(loadPluginsFromConfPath());

        return list;
    }

    private List<Class<? extends Plugin>> loadBundledPlugins() {
        List<Class<? extends Plugin>> list = new ArrayList<>();

        try {
            list.add((Class<? extends Plugin>) Class.forName("org.nanoboot.octagon.plugin.system.SystemPlugin"));
            list.add((Class<? extends Plugin>) Class.forName("org.nanoboot.octagon.plugin.main.plugin.MainPlugin"));
            list.add((Class<? extends Plugin>) Class.forName("org.nanoboot.octagon.plugin.system.ActionLogPlugin"));
            list.add((Class<? extends Plugin>) Class.forName("org.nanoboot.octagon.plugin.system.PinningPlugin"));
            list.add((Class<? extends Plugin>) Class.forName("org.nanoboot.octagon.plugin.system.ReminderPlugin"));
            list.add((Class<? extends Plugin>) Class.forName("org.nanoboot.octagon.plugin.system.FavoritePlugin"));
            list.add((Class<? extends Plugin>) Class.forName("org.nanoboot.octagon.plugin.system.WhiningPlugin"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new OctagonException("Loading a plugin failed", e);
        }
        return list;
    }

    private List<Class<? extends Plugin>> loadPluginsFromConfPath() {
        if(pluginClassesListFromConfpath == null) {
            throw new OctagonException("pluginClassesListFromConfpath dependency of " + getClass().getName() + "is null");
        }
        return pluginClassesListFromConfpath;
    }
}
