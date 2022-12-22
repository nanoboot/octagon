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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.nanoboot.powerframework.utils.dependencies.DependencyNode;
import org.nanoboot.octagon.plugin.api.core.Plugin;
import org.nanoboot.powerframework.utils.dependencies.DependencyResolver;
import org.nanoboot.powerframework.utils.dependencies.DependencyResolverJGraphTImpl;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class PluginSorterImpl implements PluginSorter {

    @Override
    public List<Plugin> sort(List<Plugin> plugins) {
       
            Map<String, Plugin> map = new HashMap<>();
            for (Plugin p : plugins) {
                map.put(p.getId(), p);
            }

            List<Plugin> result = new ArrayList<Plugin>();
            List<DependencyNode> nodes = new ArrayList<>();

            List<String> systemPluginIds = new ArrayList<>();
            for (Plugin p : plugins) {
                if (p.isSystemPlugin()) {
                    systemPluginIds.add(p.getId());
                }
            }

            for (Plugin p : plugins) {
                List<String> dependencies = Arrays.asList(p.getDependsOnAsArray());
                DependencyNode newNode = new DependencyNode(p.getId(), dependencies);
                if (!p.getId().equals("system")) {
                    newNode.getDependencies().add("system");
                }
                for (String dpi : systemPluginIds) {
                    if (p.isSystemPlugin()) {
                        break;
                    }
                    if (p.getId().equals("system")) {
                        continue;
                    }
                    newNode.getDependencies().add(dpi);
                }

                nodes.add(newNode);
            }
            DependencyResolver dr = new DependencyResolverJGraphTImpl();
            List<String> ids = dr.resolve(nodes);

            for (String id : ids) {
                result.add(map.get(id));
            }
            return result;

    }
}
