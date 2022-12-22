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

package org.nanoboot.octagon.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.nanoboot.octagon.core.exceptions.OctagonException;

/**
 * Octagon config class.
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class OctagonPluginsPropertiesFactory {

    private static final String OCTAGONPLUGINSPROPERTIES = "octagon-plugins.properties";
    private static final String PLUGIN_CONF = "pluginConf";
    /**
     * Data path.
     */
    @Getter
    @Setter
    private OctagonConfPath octagonConfPath;

    public Properties createInstance() {
        Properties properties = new Properties();
        String path = octagonConfPath.getOctagonDataDir();
        File propFile = new File(path + File.separator + PLUGIN_CONF + File.separator + OCTAGONPLUGINSPROPERTIES);
        System.out.println(propFile.getAbsolutePath()+"propFile.exists()"+propFile.exists());
        FileInputStream fis;
        try {
            fis = new FileInputStream(propFile);
        } catch (FileNotFoundException ex) {
            throw new OctagonException(ex);
        }
        try {
            properties.load(fis);
        } catch (IOException ex) {
            throw new OctagonException(ex);
        }

        return properties;
    }

    public String getConfigPathFor(String configFileName) {
        String result = "file:" + octagonConfPath.getOctagonDataDir() + File.separator + configFileName;
        System.err.println("result=" + result);
        return result;
    }
}
