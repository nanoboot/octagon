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
import org.nanoboot.octagon.plugin.api.core.PluginRegistry;
import org.nanoboot.octagon.plugin.api.core.PluginStub;
import org.nanoboot.octagon.plugin.api.core.PluginStubImpl;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.nanoboot.octagon.core.exceptions.OctagonException;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class PluginLoader {

    @Setter
    private PluginRegistry pluginRegistry;
    @Setter
    private Properties octagonPluginsProperties;
    @Getter
    private final List<PluginStub> pluginStubs = new ArrayList<>();

    public void load() {
        List<Plugin> plugins = pluginRegistry.list();
        System.out.println("pluginSize=" + plugins.size());
        System.out.println("\n\n\n\n");
        if (octagonPluginsProperties == null) {
            throw new OctagonException("Mandatory dependency octagonPluginsProperties is not satisfied (is null).");
        }
        for (Plugin plugin : pluginRegistry.list()) {
            Properties pluginProperties = new Properties();
            for (Object objectKey : octagonPluginsProperties.keySet()) {
                String key = (String) objectKey;
                if (key.startsWith("octagon.plugins." + plugin.getId())) {
                    String value = octagonPluginsProperties.getProperty(key);
                    pluginProperties.put(key, value);
                }

            }
            plugin.init(pluginProperties);
            final PluginStub pluginStub = plugin.getPluginStub();
            pluginStub.setPluginId(plugin.getId());
            pluginStubs.add(pluginStub);
            System.out.println("\n\nPlugin " + plugin.getSignature() + " ::");
            PluginStubImpl pluginStubImpl = (PluginStubImpl) plugin.getPluginStub();
            System.out.println((pluginStubImpl.toDescription()));
        }
    }

}
