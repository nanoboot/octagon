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

package org.nanoboot.octagon.html.webpage;

import org.nanoboot.octagon.entity.api.EntityGroup;
import org.nanoboot.octagon.entity.api.EntityGroupRegistry;
import org.nanoboot.octagon.entity.api.EntityRegistry;
import org.nanoboot.octagon.entity.api.EntityRegistryEntry;
import org.nanoboot.powerframework.utils.NamingConvention;
import org.nanoboot.powerframework.utils.NamingConventionConvertor;
import org.nanoboot.powerframework.web.html.WebElement;
import org.nanoboot.powerframework.web.html.tags.*;
import org.nanoboot.powerframework.xml.Attribute;
import org.nanoboot.powerframework.xml.ElementType;
import org.nanoboot.powerframework.xml.NoElement;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class HomeWebPage extends OctagonWebPage {

    @Data
    @AllArgsConstructor
    class OctagonMenuEntityEntry implements Comparable<OctagonMenuEntityEntry> {

        private String groupName;
        private int groupSortkey;
        private String classSimpleName;
        private int entitySortkey;
        private boolean placeSpaceAfter;

        @Override
        public int compareTo(OctagonMenuEntityEntry o) {
            if (groupName.equals(o.getGroupName())) {
                if (entitySortkey == o.getEntitySortkey()) {
                    return 0;
                }
                return entitySortkey > o.getEntitySortkey() ? 1 : -1;
            } else {
                if (groupSortkey == o.getGroupSortkey()) {
                    return 0;
                }
                return groupSortkey > o.getGroupSortkey() ? 1 : -1;
            }
        }

        @Override
        public String toString() {
            return groupName + "\t" + groupSortkey + "\t" + classSimpleName + "\t" + entitySortkey;
        }
    }

    public HomeWebPage(EntityGroupRegistry entityGroupRegistry, EntityRegistry entityRegistry) {
        this.setTitleInnerText("Knowledge management system");
        bodyContent.add(new H1().setInnerText("Entity groups"));

        List<OctagonMenuEntityEntry> octagonMenuEntityEntries = new ArrayList<>();

        for (EntityRegistryEntry ere : entityRegistry.list()) {

            String groupName = ere.getEntityGroupName();

            EntityGroup entityGroup = null;
            try {
                entityGroup = entityGroupRegistry.find(groupName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            int groupSortkey = entityGroup == null ? Integer.MAX_VALUE : entityGroup.getSortkey();
            String simpleClassName = ere.getEntityClass().getSimpleName();
            int entitySortkeyInGroup = ere.getSortkeyInGroup();
            boolean placeSpaceAfter = ere.isPlaceSpaceAfter();
            OctagonMenuEntityEntry octagonMenuEntityEntry = new OctagonMenuEntityEntry(
                    groupName, groupSortkey, simpleClassName, entitySortkeyInGroup, placeSpaceAfter);
            octagonMenuEntityEntries.add(octagonMenuEntityEntry);
        }

        bodyContent.add(new H2().setInnerText("Tools"));
        bodyContent.add(new NoElement("""
                <ul><li><a href="sqlExplorer">SQL explorer</a></li>
                """));
        bodyContent.add(new NoElement("""
                <li><a href="webapp?webAppClassSimpleName=ListOfWebApps">Web apps</a></li></ul>
                """));
        Collections.sort(octagonMenuEntityEntries);
        String currentGroupName = "null";
        Ul ul = null;
        for (OctagonMenuEntityEntry e : octagonMenuEntityEntries) {
            if (!currentGroupName.equals(e.getGroupName())) {
                currentGroupName = e.getGroupName();
                String humanGroupName = NamingConventionConvertor.convert(e.getGroupName(), NamingConvention.JAVA_FIELD, NamingConvention.HUMAN);
                humanGroupName = Character.toUpperCase(humanGroupName.charAt(0)) + humanGroupName.substring(1);
                if (ul != null) {
                    bodyContent.add(ul);
                }
                bodyContent.add(new H2().setInnerText(humanGroupName));

                ul = new Ul();
            }
            Li li = new Li();
            A a = new A();
            a.add(new Attribute("href", "menu?className=" + e.getClassSimpleName()));
            a.setInnerText(e.getClassSimpleName());
            li.add(a);
            li.getAttributes().setAllowAnyAttribute(true);
            ul.add(li);
            if (e.isPlaceSpaceAfter()) {
                li.add(new Attribute("style", "margin-bottom:20px;"));
            }
        }
        if (ul != null) {
            bodyContent.add(ul);
        }
    }

}
