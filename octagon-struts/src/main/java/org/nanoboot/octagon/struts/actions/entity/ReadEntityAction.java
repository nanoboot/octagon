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

import org.nanoboot.octagon.entity.classes.RelatedList;
import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.octagon.entity.classes.Query;
import org.nanoboot.powerframework.reflection.ReflectionUtils;
import org.nanoboot.powerframework.web.html.tags.A;
import org.nanoboot.octagon.html.fragments.ViewEntityFragment;
import org.springframework.web.util.HtmlUtils;

import java.lang.reflect.Constructor;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class ReadEntityAction extends EntityAction {
    public String execute() {

        printWarningTextIfNotProd();
        throwExceptionIfRepositoryRegistryIsNull();
        String id = request.getParameter("id");
        if (id == null) {
            setError("id parameter required.");
            setTitle(null);
            return "error";
        }
        Entity t = this.findEntityRepo().read(id);
        if(t == null) {
            //not found
            this.setError("Entity not found for id = " + id);
            setTitle(null);
            return "success";
        }
        String authorizationResult = authorizeActionTypeForEntity(t);
        if(authorizationResult != null) {
            setError(authorizationResult);
            setTitle(null);
            return "success";
        }
        this.persistActionLog(t, t, null);
        this.setMessage("An entity was read: " + HtmlUtils.htmlEscape(t.toString()));
        if(t.getEntityName().equals("Query")) {
            Query q = (Query)t;
            String type = q.getType();
            Class clazz = repositoryRegistry.find(type).getClassOfType();

            Constructor constructor = ReflectionUtils.getConstructor(clazz);
            Entity dummyInstance = (Entity) ReflectionUtils
                    .newInstance(constructor);

            String plural = dummyInstance.getEntityNameInPlural();
            String href = "list?className=" + dummyInstance.getEntityName() + "&queryId=" + q.getId();
            String title = "List " + plural.toLowerCase() + " of this query";
            this.setMessage(new A(href, title).setInnerText(title).toString());
        }


        String[] relatedListNamesForEntity = t.getRelatedListsForEntity();
        RelatedList[] relatedLists = null;
        if (relatedListNamesForEntity != null) {
            relatedLists = relatedListRepository.generate(relatedListNamesForEntity, t.getId().toString());
        }

        ViewEntityFragment fragment = new ViewEntityFragment(t, labelRepository, relatedLists, repositoryRegistry);
        this.request.setAttribute("fragment", fragment.build());
        this.request.setAttribute("entity", t);
        setTitle(t.getName());
        return "success";
    }
}
