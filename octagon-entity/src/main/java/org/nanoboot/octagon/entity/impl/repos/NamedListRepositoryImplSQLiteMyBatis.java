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

import java.util.Collections;
import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.entity.api.LabelRepository;
import org.nanoboot.octagon.entity.api.NamedListRepository;
import org.nanoboot.octagon.entity.classes.EntityLabel;
import org.nanoboot.octagon.entity.impl.mappers.NamedListMapper;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@Data
@AllArgsConstructor
public class NamedListRepositoryImplSQLiteMyBatis implements NamedListRepository {

    private NamedListMapper namedListMapper;
    private LabelRepository labelRepository;

    @Override
    public List<EntityLabel> generate(String name, String forId) {
        System.err.println("name=" + name+" forId="+forId);
        switch(name) {
            case "getWhinings": return getAllLabelsForEntity("Whining");
            case "getQueries": return getAllLabelsForEntity("Query");
            case "getSqlQueries": return getAllLabelsForEntity("SqlQuery");

            case "getPersonTasks": return getAllLabelsForEntity("PersonTask");

            case "getBookThings": return namedListMapper.getBookThings();
            case "getArticles": return getAllLabelsForEntity("Article");
            case "getCategories": return getAllLabelsForEntity("Category");
            case "getProductGroups": return getAllLabelsForEntity("ProductGroup");
            case "getProjectGroups": return getAllLabelsForEntity("ProjectGroup");
            case "getProducts": return getAllLabelsForEntity("Product");
            case "getProjects": return getAllLabelsForEntity("Project");
            case "getUsers": return getAllLabelsForEntity("User");
            case "productId": return getAllLabelsForEntity("Product");
            case "getVersionsForProduct": return namedListMapper.getVersionsForProduct(forId);
            case "getProductComponentsForProduct": return namedListMapper.getProductComponentsForProduct(forId);
            case "getProductModulesForProduct": return namedListMapper.getProductModulesForProduct(forId);
            case "getProductMilestonesForProduct": return namedListMapper.getProductMilestonesForProduct(forId);
            case "getEpicsForProduct": return namedListMapper.getEpicsForProduct(forId);
            case "getStoriesForProduct": return namedListMapper.getStoriesForProduct(forId);
            case "getDevTasksForProduct": return namedListMapper.getDevTasksForProduct(forId);
            case "getProposalsForProduct": return namedListMapper.getProposalsForProduct(forId);
            case "getNewFeaturesForProduct": return namedListMapper.getNewFeaturesForProduct(forId);
            case "getEnhancementsForProduct": return namedListMapper.getEnhancementsForProduct(forId);
            case "getBugsForProduct": return namedListMapper.getBugsForProduct(forId);
            case "getIncidentsForProduct": return namedListMapper.getIncidentsForProduct(forId);
            case "getProblemsForProduct": return namedListMapper.getProblemsForProduct(forId);
            case "graphId": return getAllLabelsForEntity("Graph");
            case "getGraphs": return getAllLabelsForEntity("Graph");
            case "getVertexesForGraph": return namedListMapper.getVertexesForGraph(forId);
            case "getEdgesForGraph": return namedListMapper.getEdgesForGraph(forId);
            default: throw new OctagonException("Generating for " + name + " failed");
        }

    }
    private List<EntityLabel> getAllLabelsForEntity(String simpleClassName) {
        final List<EntityLabel> labels = labelRepository.getLabels(simpleClassName);
        return labels;
    }
}
