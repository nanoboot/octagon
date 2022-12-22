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
import org.nanoboot.powerframework.web.html.WebElement;
import org.nanoboot.powerframework.web.html.tags.A;
import org.nanoboot.powerframework.web.html.tags.H1;
import org.nanoboot.octagon.entity.core.Entity;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class MenuEntityFragment extends  AbstractEntityFragment{
    public MenuEntityFragment(Class entityClass) {
        super(entityClass, ActionType.MENU);
        Entity emptyEntity = returnNewEmptyInstance(entityClass);
        H1 h1 = new H1();
        h1.setInnerText(emptyEntity.getEntityNameInPlural());
        this.add(h1);
        WebElement ul = new WebElement("ul");
        A a1 = new A("list?className=" + emptyEntity.getEntityName());
        a1.setInnerText("List " + emptyEntity.getHumanEntityNameInPlural());
        A a2 = new A("create?className=" + emptyEntity.getEntityName());
        a2.setInnerText("Add " + emptyEntity.getHumanEntityName());
        WebElement li1 = new WebElement("li", a1.toString());
        WebElement li2 = new WebElement("li", a2.toString());
        ul.add(li1, li2);
        this.add(ul);
    }
}

//<h1>Things</h1>
//<ul>
//<li><a href="listTodos">List todos</a></li>
//<li><a href="createTodo">Add todo</a></li>
//</ul>
