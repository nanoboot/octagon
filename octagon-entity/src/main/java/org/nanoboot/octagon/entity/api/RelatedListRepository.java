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

package org.nanoboot.octagon.entity.api;

import java.util.Collections;
import org.nanoboot.octagon.entity.classes.EntityLabel;
import org.nanoboot.octagon.entity.classes.RelatedList;

import java.util.List;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@Deprecated
public interface RelatedListRepository {
    /**
     * @param name
     * @param forId
     * @return
     */
    RelatedList generate(String name, String forId);

    default RelatedList createRelatedList(String name, String foreignKey, List<EntityLabel> list) {
        Collections.sort(list);
        for (EntityLabel el : list) {
            if (el.getLabel() == null) {
                throw new RuntimeException("label is null");
            }
        }
        return new RelatedList(name, foreignKey, list);
    }

    default RelatedList[] generate(String names[], String forId) {
        String[] relatedListsForEveryEntity = getRelatedListsForEveryEntity();
        RelatedList[] array = new RelatedList[names.length + relatedListsForEveryEntity.length];
        int index = 0;
        for (; index < names.length; index++) {
            String name = names[index];
            array[index] = generate(name, forId);
            if (array[index] == null) {
                throw new RuntimeException("Related list is null for " + name);
            }
        }
        for (; index < array.length; index++) {
            String name = relatedListsForEveryEntity[index - names.length];
            array[index] = generate(name, forId);
        }
        return array;
    }

    default String[] getRelatedListsForEveryEntity() {
        return new String[]{};//{"getActionLogsFor"};
    }
}
