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

package org.nanoboot.octagon.clazz.loader;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.loader.WebappClassLoader;

import java.io.File;
import java.net.MalformedURLException;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class OctagonClassLoader extends WebappClassLoader {

    public OctagonClassLoader() {
    }

    public OctagonClassLoader(final ClassLoader parent) {
        super(parent);
    }

    @Override
    public void start() throws LifecycleException {
        System.out.println("hello clazz context loader");
        String octConfPath = System.getProperty("oct.datapath");
        System.out.println("###oct.datapath=" + octConfPath);
        String[] paths = {octConfPath + "/pluginData/plugins",
                octConfPath + "/pluginData/pluginLibs"};
        // Iterate over all the non standard locations
        for (String path : paths) {
            // Get all the resources in the current location
            File[] jars = new File(path).listFiles();
            System.out.println("Found " + (jars == null ? "null" : jars.length) + " jars");
            for (File jar : jars) {
                // Check if the resource is a jar file
                if (jar.getName().endsWith(".jar") && jar.isFile() && jar.canRead()) {
                    // Add the jar file to the list of URL defined in the parent class
                    System.out.println("found jar file: " + jar.getAbsolutePath());
                    try {
                        addURL(jar.toURI().toURL());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println("hello clazz context loader 2");
        // Call start on the parent class
        super.start();
    }
}



