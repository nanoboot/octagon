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
import java.util.List;
import java.util.Properties;
import org.nanoboot.octagon.plugin.api.core.Plugin;
import org.nanoboot.octagon.plugin.api.core.PluginStub;
import org.nanoboot.powerframework.json.JsonObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class PluginSorterImplTest {

    public PluginSorterImplTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of sort method, of class PluginSorterImpl.
     */
    @Test
    public void testSort() {
        System.out.println("sort");
        //prepare
        List<Plugin> plugins = new ArrayList<>();

        plugins.add(new PluginImpl("development", Arrays.asList("task")));
        plugins.add(new PluginImpl("task"));
        plugins.add(new PluginImpl("person", Arrays.asList("task")));
        plugins.add(new PluginImpl("encyclopedia"));
        plugins.add(new PluginImpl("system"));
        plugins.add(new PluginImpl("favorite", true));
        plugins.add(new PluginImpl("action-log", true));
        plugins.add(new PluginImpl("pinning", true));
        plugins.add(new PluginImpl("reminder", true));
        plugins.add(new PluginImpl("whining", true));

        PluginSorterImpl instance = new PluginSorterImpl();
        List<Plugin> expResult = new ArrayList<>();
        expResult.add(new PluginImpl("system"));
        expResult.add(new PluginImpl("favorite", true));
        expResult.add(new PluginImpl("action-log", true));
        expResult.add(new PluginImpl("pinning", true));
        expResult.add(new PluginImpl("reminder", true));
        expResult.add(new PluginImpl("whining", true));
        expResult.add(new PluginImpl("task"));
        expResult.add(new PluginImpl("encyclopedia"));
        expResult.add(new PluginImpl("development", Arrays.asList("task")));
        expResult.add(new PluginImpl("person", Arrays.asList("task")));
        
        //execute
        List<Plugin> result = instance.sort(plugins);
        //assert
        assertEquals(expResult.size(), result.size());
        for(int i =0;i<result.size();i++){
        assertEquals(expResult.get(i).getId(), result.get(i).getId());
        }
    }

    class PluginImpl implements Plugin {

        private final String name;
        private String dependsOn = "";
        private boolean system;

        private PluginImpl(String name) {
            this.name = name;
        }

        private PluginImpl(String name, List<String> asList) {
            this.name = name;
            int lastIndex = asList.size() - 1;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < asList.size(); i++) {
                boolean isLastIndex = i == lastIndex;
                sb.append(asList.get(i));
                if (!isLastIndex) {
                    sb.append(',');
                }
            }
            this.dependsOn = sb.toString();
        }

        private PluginImpl(String name, boolean systemIn) {
            this(name);
            this.system = systemIn;
        }

        public String getDependsOn() {
            return dependsOn;
        }

        @Override
        public String getGroup() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getId() {
            return name;
        }

        @Override
        public String getVersion() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String init(Properties propertiesConfiguration) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public PluginStub getPluginStub() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public boolean isSystemPlugin() {
            return system;
        }
        @Override
        public String toString() {
            return name;
        }
    }

}
