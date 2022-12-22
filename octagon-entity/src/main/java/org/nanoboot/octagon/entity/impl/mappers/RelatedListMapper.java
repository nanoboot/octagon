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

package org.nanoboot.octagon.entity.impl.mappers;

import org.nanoboot.octagon.entity.classes.EntityLabel;

import java.util.List;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public interface RelatedListMapper {
    List<EntityLabel> getAssignedQueriesForWhining(String forId);
    List<EntityLabel> getAssignedSqlQueriesForWhining(String forId);
    //
    List<EntityLabel> getParentWhiningsForQuery(String forId);
    List<EntityLabel> getParentWhiningsForSqlQuery(String forId);
    //
    List<EntityLabel> getActionLogsFor(String id);
    //
    List<EntityLabel> getSubPersonTasks(String personTaskId);
    //
    List<EntityLabel> getArticlesForParentCategory(String categoryId);
    List<EntityLabel> getCategoriesForParentCategory(String categoryId);
    List<EntityLabel> getParentCategoriesForCategory(String categoryId);

    List<EntityLabel> getParentCategoriesForArticle(String articleId);

    //
    List<EntityLabel> getProductsForProductGroup(String productGroupId);
    //
    List<EntityLabel> getProjectsForProjectGroup(String productGroupId);
    //
    List<EntityLabel> getProductModulesForProduct(String productId);
    List<EntityLabel> getProductComponentsForProduct(String productId);
    List<EntityLabel> getProductMilestonesForProduct(String productId);
    List<EntityLabel> getProductReleasesForProduct(String productId);
    List<EntityLabel> getProductVersionsForProduct(String productId);
    List<EntityLabel> getGitlabReposForProduct(String productId);
    List<EntityLabel> getEpicsForProduct(String productId);
    //
    List<EntityLabel> getProposalsForProduct(String forId);
    List<EntityLabel> getNewFeaturesForProduct(String forId);
    List<EntityLabel> getEnhancementsForProduct(String forId);
    List<EntityLabel> getBugsForProduct(String forId);
    List<EntityLabel> getIncidentsForProduct(String forId);
    List<EntityLabel> getProblemsForProduct(String forId);
    ////
    List<EntityLabel> getProductReleasesForProductVersion(String productId);
    //
    List<EntityLabel> getStoriesForEpic(String epicId);
    List<EntityLabel> getDevTasksForStory(String storyId);
    List<EntityLabel> getDevSubTasksForDevTask(String devTaskId);
    List<EntityLabel> getVertexesForGraph(String graphId);
    List<EntityLabel> getEdgesForGraph(String graphId);
    //
    List<EntityLabel> getGraphTagsForVertex(String vertexId);
    List<EntityLabel> getGraphPropertiesForVertex(String vertexId);
    List<EntityLabel> getGraphTagsForEdge(String edgeId);
    List<EntityLabel> getGraphPropertiesForEdge(String edgeId);
    //
    List<EntityLabel> getVertexConnectionsForVertex(String vertexId);



}

