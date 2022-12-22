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

package org.nanoboot.octagon.plugin.favorite.impl.repos;

import org.nanoboot.octagon.plugin.favorite.classes.Favorite;
import org.nanoboot.octagon.plugin.favorite.api.FavoriteRepository;
import org.nanoboot.octagon.plugin.favorite.impl.mappers.FavoriteMapper;
import org.nanoboot.octagon.entity.api.MapperApi;
import org.nanoboot.octagon.entity.impl.repos.RepositoryImpl;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class FavoriteRepositoryImplSQLiteMyBatis extends RepositoryImpl<Favorite> implements FavoriteRepository {
    public FavoriteRepositoryImplSQLiteMyBatis(MapperApi<Favorite> mapper, Class type) {
        super(mapper, type);
    }

    @Override
    public boolean isFavorite(String s) {
        return ((FavoriteMapper)mapper).isFavorite(s);
    }
}
