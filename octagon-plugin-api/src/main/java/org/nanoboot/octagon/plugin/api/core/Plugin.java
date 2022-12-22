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

import java.util.Properties;
import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.core.utils.KeyI;
import org.nanoboot.powerframework.json.JsonObject;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public interface Plugin extends KeyI, Comparable<Plugin> {

    String SIGNATURE_SEPARATOR = ":";

    String getGroup();

    String getId();

    String getVersion();

    default String getSignature() {
        return getGroup() + SIGNATURE_SEPARATOR + getId() + SIGNATURE_SEPARATOR + getVersion();
    }

    default String getKey() {
        return getSignature();
    }

    default String getName() {
        return getId();
    }

    default String getDescription() {
        return getName();
    }

    default String getDependsOn() {
        return "";
    }
    default boolean hasMigrationSchema() {
        return false;
    }

    default String[] getDependsOnAsArray() {
        if(getDependsOn().isEmpty()) {
            return new String[]{};
        }
        return this.getDependsOn().split(",");
    }

    default boolean doesDependOn(String pluginId) {
        for (String s : getDependsOnAsArray()) {
            if (s.equals(pluginId)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param propertiesConfiguration
     * @return null, if the initialization was successfull, otherwise the error
     * description
     */
    String init(Properties propertiesConfiguration);

    PluginStub getPluginStub();

    default int compareTo(Plugin o) {
        Plugin plugin1 = this;
        Plugin plugin2 = o;
        boolean isPlugin1ASystemPlugin = plugin1.isSystemPlugin();
        boolean isPlugin2ASystemPlugin = plugin2.isSystemPlugin();
        if(plugin1.getId().equals(plugin2.getId())) {
            return 0;
        }
        boolean plugin1DependsOnPlugin2 = plugin1.doesDependOn(plugin2.getId());
        boolean plugin2DependsOnPlugin1 = plugin2.doesDependOn(plugin1.getId());
        if (plugin1DependsOnPlugin2 & plugin2DependsOnPlugin1) {
            String msg = "Cannot create dependency graph. Plugins " + plugin1.getId() +
                    " and " + plugin2.getId() + " do have bidirectional dependency (each other)";
            throw new OctagonException(msg);
        }
        if(plugin1DependsOnPlugin2) {
            return 1;
        }
        if(plugin2DependsOnPlugin1) {
            return -1;
        }
        if(plugin1.getId().equals("system")) {
            return -1;
        }
        
        if(plugin2.getId().equals("system")) {
            return 1;
        }
        if(isPlugin1ASystemPlugin &&!isPlugin2ASystemPlugin) {
            return -1;
        }
        if(!isPlugin1ASystemPlugin &&isPlugin2ASystemPlugin) {
            return 1;
        }
        return 0;
    }
    default boolean isSystemPlugin() {
        return false;
    }
}
