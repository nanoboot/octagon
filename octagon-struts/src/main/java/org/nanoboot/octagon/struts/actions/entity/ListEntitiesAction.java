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

import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.octagon.entity.classes.Query;
import org.nanoboot.octagon.entity.utils.QueryDescription;
import org.nanoboot.powerframework.json.JsonObject;
import org.nanoboot.powerframework.time.moment.UniversalDateTime;
import org.nanoboot.powerframework.web.html.tags.A;
import org.nanoboot.octagon.html.fragments.ListEntitiesFragment;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class ListEntitiesAction extends EntityAction {
    public String execute() {

        printWarningTextIfNotProd();
        throwExceptionIfRepositoryRegistryIsNull();

        Entity emptyEntity = returnNewEmptyInstance();

        String authorizationResult = authorizeActionTypeForEntity(emptyEntity);
        if (authorizationResult != null) {
            setError(authorizationResult);
            setTitle(null);
            return "success";
        }
        Map<String, String> parameterMap = getParameterMap(request);

        String queryId = getParam("queryId");
        Query queryFromDB = queryId == null ? null : (Query) repositoryRegistry.find("Query").read(queryId);
        QueryDescription queryDescriptionFromDB = queryFromDB == null ? null : new QueryDescription(new JsonObject(queryFromDB.getData()), repositoryRegistry);
        if (queryDescriptionFromDB != null && !queryDescriptionFromDB.isEmpty()) {
            for (String key : queryDescriptionFromDB.getMap().keySet()) {
                parameterMap.put("filter_" + key, queryDescriptionFromDB.getMap().get(key));
            }
        }

        QueryDescription queryDescription = new QueryDescription(emptyEntity, parameterMap);
        String sql = queryDescription.getSql();
        System.err.println("sql=" + sql);
        List<Entity> list = findEntityRepo().list(sql);

        String saveQuery = request.getParameter("saveQuery");
        Query query = null;
        if (saveQuery != null && saveQuery.equals("yes")) {
            if (queryDescription.isEmpty()) {
                //nothing to do.
                setError("Query is empty, nothing to save, no query was saved.");
            } else {
                query = new Query();
                query.setId(UUID.randomUUID());
                String defaultName = "New query for entity : " + emptyEntity.getHumanEntityName() + ", udt = " + UniversalDateTime.now().toString();
                query.setName(defaultName);
                query.setType(emptyEntity.getEntityName());
                query.setData(queryDescription.toJsonObject().toMinimalString());
//            query.setDescription("Generated sql: " + queryDescription.getSql());
                repositoryRegistry.find("Query").create(query);
                //this.persistActionLog(null, query, null);

                String href = "read?className=Query&id=" + query.getId().toString();
                String title = query.getName();
                A link = new A(href, title);
                link.setInnerText(title);
                setMessage("Saved new query: " + link.toString());
            }
        }
        this.persistActionLog(null, null, emptyEntity.getEntityName() + " " + queryDescription.toJsonObject().toString());
        //deprecated
        this.request.setAttribute("entities", list);

        this.setMessage("Returned " + list.size() + " row" + (list.size() > 1 ? "s" : ""));

        ListEntitiesFragment fragment = new ListEntitiesFragment(findEntityRepo().getClassOfType(), list, queryDescription, labelRepository, namedListRepository, repositoryRegistry);
        this.request.setAttribute("fragment", fragment.build());
        this.request.setAttribute("emptyEntity", emptyEntity);
        setTitle(null);
        return "success";
    }
}
