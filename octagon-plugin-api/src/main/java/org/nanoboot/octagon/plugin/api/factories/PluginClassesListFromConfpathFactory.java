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

import org.nanoboot.octagon.core.config.OctagonConfPath;
import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.plugin.api.core.Plugin;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class PluginClassesListFromConfpathFactory {
    public static final String JAR_EXTENSION = ".jar";
    /**
     * Data path.
     */
    @Getter
    @Setter
    private OctagonConfPath octagonConfPath;

    public List<Class<? extends Plugin>> loadPluginsFromConfPath() {
        List<Class<? extends Plugin>> list = new ArrayList<>();

        String confPath = octagonConfPath.getOctagonDataDir();
        File pluginJarsDir = new File(confPath + File.separator + "pluginData" + File.separator + "plugins");

        List<File> pluginJarFiles = new ArrayList<>();
        for (File f : pluginJarsDir.listFiles()) {
            if (f.isFile() && f.getName().endsWith(JAR_EXTENSION)) {
                System.out.println("Found jar file !!!!!! " + f.getAbsolutePath());
                pluginJarFiles.add(f);
            }
        }
        List<String> pluginClasses = new ArrayList<>();
        for (File jar : pluginJarFiles) {
            JarInputStream jarStream = null;
            try {
                jarStream = new JarInputStream(new FileInputStream(jar.getAbsolutePath()));
            } catch (IOException e) {
                throw new OctagonException(e);
            }
            Manifest mf = jarStream.getManifest();
            Attributes attributes = mf.getMainAttributes();

            String pluginClass = attributes.getValue("Plugin-Class");
            if (pluginClass == null) {
                String msg = "Directory plugins contains jar file, which has MANIFEST.MF without entry \"Plugin-Class\": " + jar.getAbsolutePath();
                throw new OctagonException(msg);
            }
            pluginClasses.add(pluginClass);
        }

        for (String pluginClass : pluginClasses) {
            try {
                list.add((Class<? extends Plugin>) Class.forName(pluginClass));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new OctagonException("Loading a plugin failed while finding class", e);
            }
        }

        return list;
    }

}
