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

package org.nanoboot.octagon.html.links;

import org.nanoboot.octagon.entity.core.ActionType;
import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.powerframework.web.html.tags.A;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class AbstractLink extends A {
    public AbstractLink(ActionType actionType, Entity entity) {
        super(getHref(actionType, entity), getTitle(actionType));
        String innerText = getTitle(actionType);
        this.setInnerText(innerText.equals("read") ? "view" : innerText);
    }

    private static String getHref(ActionType actionType, Entity entity) {
        return actionType.name().toLowerCase() + "?className=" + entity.getEntityName() + "&id=" + entity.getId();
    }

    private static String getTitle(ActionType actionType) {
        return actionType.name().toLowerCase();
    }

}
