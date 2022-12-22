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

package org.nanoboot.octagon.plugin.system.webapps;

import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.plugin.api.core.PluginStub;
import org.nanoboot.octagon.plugin.api.core.WebAppBase;
import org.nanoboot.powerframework.web.html.WebElement;
import org.nanoboot.powerframework.web.html.tags.A;
import org.nanoboot.powerframework.web.html.tags.Div;
import org.nanoboot.powerframework.web.html.tags.H2;
import org.nanoboot.powerframework.web.html.tags.Li;
import org.nanoboot.powerframework.web.html.tags.Ul;
import org.nanoboot.powerframework.xml.Attribute;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class ListOfWebApps extends WebAppBase {
    
    @Override
    public String toHtml() {
        //if(true)return "ahoj kriple";
        Div div = new Div();
        H2 h2 = new H2();
        h2.setInnerText("Web apps");
        div.add(h2);
        Ul ul = new Ul();
        for (PluginStub ps : pluginLoader.getPluginStubs()) {
            for (String webAppClass : ps.getWebAppClasses()) {
                String[] array = webAppClass.split("\\.");
                String webAppClassSimpleName = array[array.length - 1];
                
                A a = new A();
                a.add(new Attribute("href", "webapp?webAppClassSimpleName=" + webAppClassSimpleName));
                a.setInnerText(webAppClassSimpleName);
                
                WebElement span = new WebElement("span");
                span.setInnerText(" (plugin: " + ps.getPluginId() + ")");
                
                Li li = new Li(a.toString()+span.toString());
                ul.add(li);
            }
        }
        div.add(ul);
        final String asHtml = div.build();
        if(asHtml == "null") {
            throw new OctagonException("asHtml is null");
        }
        
        return asHtml;
    }
    
}
