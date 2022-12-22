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

import org.nanoboot.octagon.entity.classes.EntityLabel;
import org.nanoboot.octagon.entity.core.Entity;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public interface Crudl<T extends Entity> {
    void create(T t);

    T read(String id);
    
    default T read(UUID id) {
        return read(id.toString());
    }

    void update(T t);

    void delete(String id);

    List<T> list(String wherePart);

    default List<T> list() {
        return list(null);
    }

    Class<T> getClassOfType();

    String getLabel(String id);

    List<EntityLabel> getLabels();

}
