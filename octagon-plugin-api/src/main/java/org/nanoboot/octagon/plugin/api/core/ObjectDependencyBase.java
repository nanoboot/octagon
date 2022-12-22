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

package org.nanoboot.octagon.plugin.api.core;

import java.util.HashMap;
import java.util.Map;
import org.nanoboot.octagon.core.exceptions.OctagonException;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public abstract class ObjectDependencyBase implements ObjectDependency {

    private final Map<String, Object> map = new HashMap<>();

    @Override
    public void addObjectDependency(String clazzName, Object object) {
        map.put(clazzName, object);
        addObjectDependencyPostHandler(clazzName, object);
    }
    protected void addObjectDependencyPostHandler(String clazzName, Object object) {
        
    }
    
    private void checkObjectDependency(String clazzName) {
        Class clazz;
        Object object = map.get(clazzName);
        try {
            clazz = Class.forName(clazzName);
        } catch (ClassNotFoundException ex) {
            throw new OctagonException(ex);
        }
        if (!object.getClass().isAssignableFrom(clazz)) {
            throw new OctagonException("Object " + object + " cannot be assigned to class " + clazzName);
        }
        map.put(clazzName, object);
    }


    @Override
    public Object getObjectDependency(String clazzName) {
        //checkObjectDependency(clazzName);
        return map.get(clazzName);
    }
}
