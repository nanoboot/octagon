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

import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.entity.api.EntityGroup;
import org.nanoboot.octagon.entity.api.MapperApi;
import org.nanoboot.octagon.entity.api.Repository;
import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.octagon.entity.core.EntitySchema;
import org.nanoboot.powerframework.reflection.ReflectionUtils;
import lombok.Getter;
import org.apache.ibatis.type.TypeHandler;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import lombok.Setter;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public final class PluginStubImpl implements PluginStub {

    @Getter
    private final List<PluginStubEntityEntry> pluginStubEntityEntries = new ArrayList<>();
    @Getter
    private final List<TypeHandler<?>> typeHandlers = new ArrayList<>();
    @Getter
    private final List<EntityGroup> entityGroups = new ArrayList<>();
    //
    @Getter
    private final List<String> batchClasses = new ArrayList<>();
    @Getter
    private final List<String> webAppClasses = new ArrayList<>();
    @Getter
    @Setter
    private String pluginId;

    @Override
    public <T extends Entity> void registerEntity(String entityGroupName,
            Class<T> entityClass,
            EntitySchema entitySchema,
            Class<? extends MapperApi<T>> mapperInterface,
            Class<? extends Repository> repositoryClass,
            int sortkeyInGroup,
            boolean placeSpaceAfter) {
        PluginStubEntityEntry pluginStubEntityEntry = new PluginStubEntityEntry(
                entityGroupName,
                entityClass,
                entitySchema,
                mapperInterface,
                repositoryClass,
                sortkeyInGroup,
                placeSpaceAfter);
        pluginStubEntityEntries.add(pluginStubEntityEntry);
    }

    @Override
    public void registerEntityGroup(String entityGroupName, int sortkey) {
        entityGroups.add(new EntityGroup(entityGroupName, sortkey));
    }

    @Override
    public void registerApp(App app) {
        throw new OctagonException("Not yet implemented");
    }

    @Override
    public void registerWebApp(String webAppClass) {
        Class clazz = loadClassFromClassName(webAppClass, WebApp.class);
        webAppClasses.add(webAppClass);
    }

    @Override
    public void registerBatch(String batchClass) {
        Class clazz = loadClassFromClassName(batchClass, Batch.class);
        batchClasses.add(batchClass);
    }

    private Class loadClassFromClassName(String fullClassName, Class expectedClass) {
        Class clazz = null;
        try {
            clazz = Class.forName(fullClassName);
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            throw new OctagonException("Cannot instantiate class " + fullClassName);
        }
        if (!expectedClass.isAssignableFrom(clazz)) {
            String msg = "Class " + fullClassName + " does not implement interface " + expectedClass.getName();
            throw new OctagonException(msg);
        }
        return clazz;
    }

    @Override
    public void registerServiceApi(ServiceApi serviceApi) {
        throw new OctagonException("Not yet implemented");
    }

    @Override
    public void registerServiceImpl(ServiceImpl serviceImpl) {
        throw new OctagonException("Not yet implemented");
    }

    @Override
    public void registerTypeHandler(Class<? extends TypeHandler<?>> typeHandlerClass) {
        Constructor constructor = ReflectionUtils.getConstructor(typeHandlerClass);
        TypeHandler<?> typeHandler = (TypeHandler<?>) ReflectionUtils.newInstance(constructor);
        typeHandlers.add(typeHandler);
    }

    public String toDescription() {
        StringBuilder sb = new StringBuilder();

        sb.append("\nEntities:");
        for (PluginStubEntityEntry e : getPluginStubEntityEntries()) {
            sb.append("\n\t# " + e.toString());
        }
        sb.append("\nTypehandlers:");
        for (TypeHandler e : getTypeHandlers()) {
            sb.append("\n\t# " + e.getClass());
        }
        return sb.toString();
    }

}
