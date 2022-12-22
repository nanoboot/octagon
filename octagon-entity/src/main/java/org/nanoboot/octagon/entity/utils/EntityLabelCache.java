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

package org.nanoboot.octagon.entity.utils;

import org.nanoboot.octagon.entity.classes.EntityLabel;
import org.nanoboot.powerframework.time.duration.Period;
import org.nanoboot.powerframework.time.moment.UniversalDateTime;
import org.nanoboot.powerframework.time.utils.TimeUnit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class EntityLabelCache {
    private Map<String, String> map = new HashMap<>();
    private UniversalDateTime lastReset = UniversalDateTime.now();

    public EntityLabelCache(List<EntityLabel> list) {
        for (EntityLabel e : list) {
            map.put(e.getId(), e.getLabel());
        }
    }

    public int getMinutesSinceLastReset() {
        return (int) new Period(lastReset, UniversalDateTime.now()).getDuration().toTotal(TimeUnit.MINUTE);
    }

    public boolean hasId(String id) {
        return map.containsKey(id);
    }

    public void put(String id, String label) {
        map.put(id, label);
    }

    public String getLabelForId(String id) {
        return map.get(id);
    }

    public void reset(List<EntityLabel> list) {
        clear();
        for (EntityLabel e : list) {
            map.put(e.getId(), e.getLabel());
        }
        lastReset = UniversalDateTime.now();
    }
    private void clear() {
        map.clear();
    }
    public boolean isEmpty() {
        return map.isEmpty();
    }

}
