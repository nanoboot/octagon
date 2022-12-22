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

package org.nanoboot.octagon.core.utils;

import org.nanoboot.octagon.core.exceptions.OctagonException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class RegistryImpl<T extends KeyI> implements Registry<T> {
    public static final String EXC_MSG = "Registry was not yet initialized (loadList method was not yet called).";
    private List<T> list = new ArrayList<>();
    private Map<String, T> map = new HashMap<>();

    private boolean initialized = false;

    @Override
    public void loadList(List<T> listIn) {
        for (T t : listIn) {
            if (map.containsKey(t.getKey())) {
                System.out.println("Plugin " + t.getKey() + "was already loaded");
                continue;
            }
            list.add(t);

            map.put(t.getKey(), t);
            if (t.getOldKey() != null) {
                map.put(t.getOldKey(), t);
            }
                        
        }
        initialized = true;
    }

    @Override
    public T find(String key) {
        if (!initialized) {
            throw new OctagonException(EXC_MSG);
        }
        if (!map.containsKey(key)) {
            throw new OctagonException("There is no entry for key " + key + " in this registry.");
        }
        return map.get(key);
    }

    @Override
    public List<T> list() {
        if (!initialized) {
            throw new OctagonException(EXC_MSG);
        }
        return list;
    }
}
