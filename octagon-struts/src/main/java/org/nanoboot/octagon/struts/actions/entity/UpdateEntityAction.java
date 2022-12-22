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

import org.nanoboot.octagon.html.fragments.UpdateEntityFragment;
import org.nanoboot.octagon.html.links.ViewLink;
import org.nanoboot.octagon.entity.core.Entity;
import org.springframework.web.util.HtmlUtils;

import java.util.UUID;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class UpdateEntityAction extends EntityAction {

    public String execute() {

        printWarningTextIfNotProd();
        throwExceptionIfRepositoryRegistryIsNull();
        String id = request.getParameter("id");
        if (id == null) {
            setError("id parameter required.");
            setTitle(null);
            return "error";
        }

        Entity currentEntity = this.findEntityRepo().read(id);
        String authorizationResult = authorizeActionTypeForEntity(currentEntity);
        if (authorizationResult != null) {
            setError(authorizationResult);
            setTitle(null);
            return "success";
        }
        Entity updatedEntity = null;
        if (isGoingToProcessForm()) {
            updatedEntity = returnNewEmptyInstance();
            updatedEntity.setId(UUID.fromString(id));

            updatedEntity.loadFromMap(getParameterMap(request));
            boolean nothingChanged = currentEntity.toJsonObject().toString().equals(updatedEntity.toJsonObject().toString());
            if (nothingChanged) {
                this.setMessage("Entity was not changed. Nothing was updated. " + updatedEntity.toString());
            } else {
                this.findEntityRepo().update(updatedEntity);
                this.persistActionLog(currentEntity, updatedEntity, null);

                ViewLink link = (ViewLink) new ViewLink(updatedEntity).setInnerText(updatedEntity.getName());
                this.setMessage("Entity updated: " + link.toString() + " " + HtmlUtils.htmlEscape(updatedEntity.toString()));
            }
        }

        if (updatedEntity != null) {
            currentEntity = updatedEntity;
        }

        UpdateEntityFragment fragment = new UpdateEntityFragment(currentEntity, namedListRepository);
        this.request.setAttribute("fragment", fragment.build());
        setTitle(currentEntity.getName());
        return "success";
    }
}
