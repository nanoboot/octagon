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
import org.nanoboot.octagon.html.forms.DeleteFormContainer;
import org.nanoboot.powerframework.web.html.WebElement;
import org.nanoboot.powerframework.web.html.tags.Br;
import org.nanoboot.octagon.entity.core.Entity;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class DeleteEntityFragment extends AbstractEntityFragment {
    public DeleteEntityFragment(Entity entity, Entity emptyEntity) {
        super(emptyEntity.getEntityClass(), ActionType.DELETE);

        if(entity == null) {
            add(new Br());
            WebElement p = new WebElement("p");
            p.setInnerText("You have deleted the " + emptyEntity.getHumanEntityName() + ".");
            add(p);
            add(new Br());
        } else {
            WebElement p = new WebElement("p");
            p.setInnerText("Going to delete " + entity.getEntityName() + ": <br />" + entity.toString());
            add(p);
            add(new DeleteFormContainer(entity));
        }
//        super(entity, "create" + entity.getEntityName(), "Add");

    }

}
