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
public class PluginTest {

    public PluginTest() {
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
     * Test of compareTo method, of class Plugin.
     */
    @Test
    public void testCompareTo() {
        System.out.println("compareTo");
        //prepare
        Plugin a = new PluginImpl("a", "");
        Plugin b = new PluginImpl("b", "");

        int expResult = 0;

        //execute
        int result = a.compareTo(b);

        //assert
        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class Plugin.
     */
    @Test
    public void testCompareTo2() {
        System.out.println("compareTo");
        //prepare
        Plugin a = new PluginImpl("a", "");
        Plugin b = new PluginImpl("b", "a");

        int expResult = -1;

        //execute
        int result = a.compareTo(b);

        //assert
        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class Plugin.
     */
    @Test
    public void testCompareTo3() {
        System.out.println("compareTo");
        //prepare
        Plugin a = new PluginImpl("a", "b");
        Plugin b = new PluginImpl("b", "");

        int expResult = 1;

        //execute
        int result = a.compareTo(b);

        //assert
        assertEquals(expResult, result);
    }

    public class PluginImpl implements Plugin {

        private final String id;
        private final String dependsOn;
        private final boolean isSystemPlugin;

        public PluginImpl(String idIn, String dependsOnIn) {
            this(idIn, dependsOnIn, false);
        }

        public PluginImpl(String idIn, String dependsOnIn, boolean isSystemPluginIn) {
            this.id = idIn;
            this.dependsOn = dependsOnIn;
            this.isSystemPlugin = isSystemPluginIn;
        }

        public String getGroup() {
            return "";
        }

        public String getId() {
            return id;
        }

        public String getVersion() {
            return "";
        }

        public String init(Properties propertiesConfiguration) {
            return "";
        }

        public PluginStub getPluginStub() {
            return null;
        }

        public String getDependsOn() {
            return this.dependsOn;
        }

        public boolean isSystemPlugin() {
            return isSystemPlugin;
        }
    }

}
