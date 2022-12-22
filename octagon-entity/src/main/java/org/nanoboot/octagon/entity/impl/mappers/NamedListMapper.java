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
public interface NamedListMapper {

    List<EntityLabel> getBookThings();

    List<EntityLabel> getVersionsForProduct(String productId);
    List<EntityLabel> getProductComponentsForProduct(String productId);
    List<EntityLabel> getProductModulesForProduct(String productId);
    List<EntityLabel> getProductMilestonesForProduct(String productId);

    List<EntityLabel> getEpicsForProduct(String productId);

    List<EntityLabel> getStoriesForProduct(String productId);

    List<EntityLabel> getDevTasksForProduct(String productId);

    List<EntityLabel> getProposalsForProduct(String productId);
    List<EntityLabel> getNewFeaturesForProduct(String productId);
    List<EntityLabel> getEnhancementsForProduct(String productId);
    List<EntityLabel> getBugsForProduct(String productId);
    List<EntityLabel> getIncidentsForProduct(String productId);
    List<EntityLabel> getProblemsForProduct(String productId);

    List<EntityLabel>  getVertexesForGraph(String forId);
    List<EntityLabel>  getEdgesForGraph(String forId);

}

