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

package org.nanoboot.octagon.plugin.main.plugin;

import java.util.Properties;
import org.nanoboot.octagon.plugin.api.core.Plugin;
import org.nanoboot.octagon.plugin.api.core.PluginStub;
import org.nanoboot.octagon.plugin.api.core.PluginStubImpl;
import org.nanoboot.octagon.plugin.main.classes.*;
import org.nanoboot.octagon.plugin.main.persistence.impl.mappers.*;
import org.nanoboot.octagon.plugin.main.persistence.impl.repos.*;;
import org.nanoboot.octagon.plugin.main.webapps.HelloWorld;
import org.nanoboot.octagon.plugin.main.persistence.impl.typehandlers.RoleTypeHandler;
import lombok.Getter;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class MainPlugin implements Plugin {
    
    private static final String MAIN = "main";
    @Getter
    private final PluginStub pluginStub = new PluginStubImpl();
    
    @Override
    public String getGroup() {
        return MAIN;
    }
    
    @Override
    public String getId() {
        return MAIN;
    }
    
    @Override
    public String getVersion() {
        return "0.2.0";
    }
    
    @Override
    public String init(Properties propertiesConfiguration) {
        for (Object objectKey : propertiesConfiguration.keySet()) {
            String key = (String) objectKey;
            String value = propertiesConfiguration.getProperty(key);
            System.out.println("Found configuration entry for plugin main: " + key + "=" + value);
        }
        pluginStub.registerEntityGroup(MAIN, 15);
        
        int sortkeyInGroup = 10;
        pluginStub
                .registerEntity(
                        MAIN,
                        User.class,
                        UserMapper.class,
                        UserRepositoryImplSQLiteMyBatis.class, sortkeyInGroup++, true);
        
        pluginStub.registerTypeHandler(RoleTypeHandler.class);
        pluginStub.registerWebApp(HelloWorld.class.getName());
        return null;
    }
    
    @Override
    public String getDependsOn() {
        return "";
    }
    
    @Override
    public boolean hasMigrationSchema() {
        return true;
    }
}
