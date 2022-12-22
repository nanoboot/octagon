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

package org.nanoboot.octagon.entity.impl.repos;

import org.nanoboot.octagon.entity.api.RelatedListRepository;
import org.nanoboot.octagon.entity.classes.RelatedList;
import org.nanoboot.octagon.entity.impl.mappers.NamedListMapper;
import org.nanoboot.octagon.entity.impl.mappers.RelatedListMapper;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@Data
@AllArgsConstructor
public class RelatedListRepositoryImplSQLiteMyBatis implements RelatedListRepository {

    private NamedListMapper namedListMapper;
    private RelatedListMapper relatedListMapper;

    @Override
    public RelatedList generate(String name, String forId) {
        System.err.println("name=" + name+" forId="+forId);
        switch(name) {
            case "getAssignedQueriesForWhining": return createRelatedList("Queries", "query", relatedListMapper.getAssignedQueriesForWhining(forId));
            case "getAssignedSqlQueriesForWhining": return createRelatedList("Sql queries", "sqlQuery", relatedListMapper.getAssignedSqlQueriesForWhining(forId));

            case "getParentWhiningsForQuery": return createRelatedList("Parent whinings", "whining", relatedListMapper.getParentWhiningsForQuery(forId));
            case "getParentWhiningsForSqlQuery": return createRelatedList("Parent whinings", "whining", relatedListMapper.getParentWhiningsForSqlQuery(forId));

            case "getActionLogsFor": return createRelatedList("Action logs", "personTask", relatedListMapper.getActionLogsFor(forId));
            case "getSubPersonTasks": return createRelatedList("Sub RV tasks", "personTask", relatedListMapper.getSubPersonTasks(forId));

            case "getArticlesForParentCategory": return createRelatedList("Articles", "article", relatedListMapper.getArticlesForParentCategory(forId));
            case "getCategoriesForParentCategory": return createRelatedList("Subcategories", "category", relatedListMapper.getCategoriesForParentCategory(forId));
            case "getParentCategoriesForCategory": return createRelatedList("Parent categories", "category", relatedListMapper.getParentCategoriesForCategory(forId));
            case "getParentCategoriesForArticle": return createRelatedList("Parent categories", "category", relatedListMapper.getParentCategoriesForArticle(forId));
            case "getProductsForProductGroup": return createRelatedList("Products", "product", relatedListMapper.getProductsForProductGroup(forId));
            case "getProjectsForProjectGroup": return createRelatedList("Projects", "project", relatedListMapper.getProjectsForProjectGroup(forId));

            case "getProductModulesForProduct": return createRelatedList("Product modules", "productModule", relatedListMapper.getProductModulesForProduct(forId));
            case "getProductComponentsForProduct": return createRelatedList("Product components", "productComponent", relatedListMapper.getProductComponentsForProduct(forId));
            case "getProductMilestonesForProduct": return createRelatedList("Product milestones", "productMilestone", relatedListMapper.getProductMilestonesForProduct(forId));
            case "getProductReleasesForProduct": return createRelatedList("Product releases", "productRelease", relatedListMapper.getProductReleasesForProduct(forId));
            case "getProductVersionsForProduct": return createRelatedList("Product versions", "productVersion", relatedListMapper.getProductVersionsForProduct(forId));
            case "getGitlabReposForProduct": return createRelatedList("Gitlab repos", "gitlabRepo", relatedListMapper.getGitlabReposForProduct(forId));
            case "getEpicsForProduct": return createRelatedList("Epics", "epic", relatedListMapper.getEpicsForProduct(forId));

            case "getProposalsForProduct": return createRelatedList("Proposals", "proposal", relatedListMapper.getProposalsForProduct(forId));
            case "getNewFeaturesForProduct": return createRelatedList("New features", "newFeature", relatedListMapper.getNewFeaturesForProduct(forId));
            case "getEnhancementsForProduct": return createRelatedList("Enhancements", "enhancement", relatedListMapper.getEnhancementsForProduct(forId));
            case "getBugsForProduct": return createRelatedList("Bugs", "bug", relatedListMapper.getBugsForProduct(forId));
            case "getIncidentsForProduct": return createRelatedList("Incidents", "incident", relatedListMapper.getIncidentsForProduct(forId));
            case "getProblemsForProduct": return createRelatedList("Problems", "problem", relatedListMapper.getProblemsForProduct(forId));

            case "getProductReleasesForProductVersion": return createRelatedList("Product releases", "productRelease", relatedListMapper.getProductReleasesForProductVersion(forId));

            case "getStoriesForEpic": return createRelatedList("Stories", "story", relatedListMapper.getStoriesForEpic(forId));
            case "getDevTasksForStory": return createRelatedList("Dev tasks", "devTask", relatedListMapper.getDevTasksForStory(forId));
            case "getDevSubTasksForDevTask": return createRelatedList("Dev sub tasks", "devSubTask", relatedListMapper.getDevSubTasksForDevTask(forId));
            //
            case "getVertexesForGraph": return createRelatedList("Vertexes", "vertex", relatedListMapper.getVertexesForGraph(forId));
            case "getEdgesForGraph": return createRelatedList("Edges", "edge", relatedListMapper.getEdgesForGraph(forId));
            //
            case "getGraphTagsForVertex": return createRelatedList("Tags", "graphTag", relatedListMapper.getGraphTagsForVertex(forId));
            case "getGraphPropertiesForVertex": return createRelatedList("Properties", "graphProperty", relatedListMapper.getGraphPropertiesForVertex(forId));
            case "getGraphTagsForEdge": return createRelatedList("Tags", "graphTag", relatedListMapper.getGraphTagsForEdge(forId));
            case "getGraphPropertiesForEdge": return createRelatedList("Properties", "graphProperty", relatedListMapper.getGraphPropertiesForEdge(forId));
            //
            case "getVertexConnectionsForVertex": return createRelatedList("Vertex connections", "edge", relatedListMapper.getVertexConnectionsForVertex(forId));
            //

            default: return null;
        }
    }
}
