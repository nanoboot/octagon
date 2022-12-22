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

/**
 * Octagon config class.
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class OctagonConfigFactory {
    /**
     * Data path.
     */
    @Getter
    @Setter
    private OctagonConfPath octagonConfPath;

    public org.springframework.beans.factory.config.PropertyPlaceholderConfigurer createInstance() {
        PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
        String path = octagonConfPath.getOctagonDataDir();

        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource mailResource = resourceLoader.getResource(getConfigPathFor("octagon-mail.properties"));
        Resource octagonResource = resourceLoader.getResource(getConfigPathFor("octagon.properties"));

        Resource[] locations = new Resource[]{mailResource, octagonResource};
        propertyPlaceholderConfigurer.setLocations(locations);
        return propertyPlaceholderConfigurer;
    }

    public String getConfigPathFor(String configFileName) {
        String result = "file:" + octagonConfPath.getOctagonDataDir() + File.separator + configFileName;
        System.err.println("result="+result);
        return result;
    }
}
