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

package org.nanoboot.octagon.html.fragments;

import org.nanoboot.octagon.entity.core.ActionType;
import org.nanoboot.octagon.html.links.ViewLink;
import org.nanoboot.powerframework.reflection.ReflectionUtils;
import org.nanoboot.powerframework.web.html.tags.A;
import org.nanoboot.powerframework.web.html.tags.Div;
import org.nanoboot.powerframework.web.html.tags.H1;
import org.nanoboot.powerframework.xml.NoElement;
import org.nanoboot.octagon.entity.core.Entity;

import java.lang.reflect.Constructor;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class AbstractEntityFragment extends Div {
    private final Entity emptyEntity;
    private final ActionType actionType;
    private final Entity entity;

    public AbstractEntityFragment(Class entityClass, ActionType actionType) {
        this(entityClass, actionType, null);
    }

    public AbstractEntityFragment(Class entityClass, ActionType actionType, Entity entity) {
        this.emptyEntity = returnNewEmptyInstance(entityClass);
        this.actionType = actionType;
        this.entity = entity;
        init();
    }

    private void init() {
        H1 h1 = new H1();
        String h1Content = emptyEntity.getHumanEntityName();
        if (this.actionType != ActionType.MENU) {
            h1Content = h1Content + " - " + actionType.name().toLowerCase();
        }

        h1.setInnerText(h1Content);
        this.add(new org.nanoboot.powerframework.xml.XmlTypeI[]{h1});
        A home = new A("menu?className=" + emptyEntity.getEntityName(), "menu?className=" + emptyEntity.getEntityName());
        home.setInnerText(emptyEntity.getEntityName().trim());
        this.add(home);
        this.add(new NoElement(" / "));
        this.add(new NoElement(this.actionType.name().toLowerCase()));
        if (this.entity != null) {
            this.add(new NoElement(" "));
            ViewLink link = new ViewLink(entity);
            link.setInnerText("&gt;&gt; " + entity.getName() + " &lt;&lt;");
            this.add(link);
        }
    }


    public static Entity returnNewEmptyInstance(Class entityClass) {
        Class thisClass = entityClass;
        Constructor constructor = ReflectionUtils.getConstructor(thisClass);
        Entity emptyInstance = (Entity) ReflectionUtils
                .newInstance(constructor);
        return emptyInstance;
    }
}
