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

package org.nanoboot.octagon.struts.actions.entity;

import org.nanoboot.octagon.html.fragments.AbstractEntityFragment;
import org.nanoboot.octagon.html.fragments.DeleteEntityFragment;
import org.nanoboot.octagon.entity.core.Entity;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class DeleteEntityAction extends EntityAction {

    public String execute() {

        printWarningTextIfNotProd();
        throwExceptionIfRepositoryRegistryIsNull();
        String id = request.getParameter("id");
        if (id == null) {
            setError("id parameter required.");
            setTitle(null);
            return "error";
        }
        Entity t = findEntityRepo().read(id);
        String authorizationResult = authorizeActionTypeForEntity(t);
        if(authorizationResult != null) {
            setError(authorizationResult);
            setTitle(null);
            return "success";
        }

        if (isGoingToProcessForm()) {
            findEntityRepo().delete(id);
            this.persistActionLog(t, null, null);
            this.setMessage("Entity deleted: " + t.getName());
            this.request.setAttribute("fragment", new AbstractEntityFragment(t.getEntityClass(), getActionType(), t).build());
            setTitle(t.getName());
            return "success";
        }

        DeleteEntityFragment fragment = new DeleteEntityFragment(t, returnNewEmptyInstance());
        this.request.setAttribute("fragment", fragment.build());
        setTitle(t.getName());
        return "success";
    }

}
