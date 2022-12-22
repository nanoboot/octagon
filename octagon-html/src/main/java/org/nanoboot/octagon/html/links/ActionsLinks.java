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

import org.nanoboot.powerframework.web.html.tags.Div;
import org.nanoboot.octagon.entity.core.Entity;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class ActionsLinks extends Div {
    public final static String ACTION = "Action";
    public ActionsLinks(Entity e) {
        ViewLink viewLink = new ViewLink(e);
        UpdateLink updateLink = new UpdateLink(e);
        DeleteLink deleteLink = new DeleteLink(e);
        HistoryLink historyLink = new HistoryLink(e.getId().toString());
        CloneLink cloneLink = new CloneLink(e.getEntityName(), e.getId().toString());
        PinLink pinLink = new PinLink(e.getEntityName(), e.getId().toString());
        RemindLink remindLink = new RemindLink(e.getEntityName(), e.getId().toString());
        FavLink favLink = new FavLink(e.getEntityName(), e.getId().toString());

        this.add(viewLink);
        this.add(updateLink);
        this.add(deleteLink);
        this.add(historyLink);
        this.add(cloneLink);
        this.add(pinLink);
        this.add(remindLink);
        this.add(favLink);
//        <a href="readTodo?id=<%=todo.getId()%>">view</a> <a
//                href="updateTodo?id=<%=todo.getId()%>">update</a> <a
//                href="deleteTodo?id=<%=todo.getId()%>">delete</a></td>
    }
}
