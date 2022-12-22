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
import org.nanoboot.octagon.entity.api.Repository;
import org.nanoboot.octagon.entity.api.RepositoryRegistry;
import org.nanoboot.octagon.entity.api.LabelRepository;
import org.nanoboot.octagon.entity.classes.EntityLabel;
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
public class LabelRepositoryImplSQLiteMyBatis implements LabelRepository {
    private RepositoryRegistry repositoryRegistry;

    public String getLabel(String entityName, String id) {
        entityName = Character.toUpperCase(entityName.charAt(0)) + entityName.substring(1);
        Repository repository = repositoryRegistry.find(entityName);
        return repository.getLabel(id);
    }

    @Override
    public List<EntityLabel> getLabels(String entityName) {
        entityName = Character.toUpperCase(entityName.charAt(0)) + entityName.substring(1);
        Repository repository = repositoryRegistry.find(entityName);
        final List labels = repository.getLabels();
        return labels;
    }
}
