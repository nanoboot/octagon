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

import org.nanoboot.octagon.entity.api.EntityGroup;
import org.nanoboot.octagon.entity.api.MapperApi;
import org.nanoboot.octagon.entity.api.Repository;
import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.octagon.entity.core.EntitySchema;
import org.nanoboot.powerframework.reflection.ReflectionUtils;
import org.apache.ibatis.type.TypeHandler;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public sealed interface PluginStub permits PluginStubImpl {

    default <T extends Entity> void registerEntity(String entityGroupName,
                                                   Class<T> entityClass,
                                                   Class<? extends MapperApi<T>> mapperInterface,
                                                   Class<? extends Repository> repositoryClass) {
        registerEntity(entityGroupName, entityClass, mapperInterface, repositoryClass, Integer.MAX_VALUE, false);
    }
    default <T extends Entity> void registerEntity(String entityGroupName,
                                                   Class<T> entityClass,
                                                   Class<? extends MapperApi<T>> mapperInterface,
                                                   Class<? extends Repository> repositoryClass,
                                                   int sortkeyInGroup) {
        Constructor constructor = ReflectionUtils.getConstructor(entityClass);
        Entity entity = (Entity) ReflectionUtils.newInstance(constructor);
        EntitySchema entitySchema = entity.getEntitySchema();
        registerEntity(entityGroupName, entityClass, entitySchema, mapperInterface, repositoryClass, sortkeyInGroup, false);
    }

    default <T extends Entity> void registerEntity(String entityGroupName,
                                           Class<T> entityClass,
                                           EntitySchema entitySchema,
                                           Class<? extends MapperApi<T>> mapperInterface,
                                           Class<? extends Repository> repositoryClass,
                                           int sortkeyInGroup) {
        registerEntity(entityGroupName, entityClass, entitySchema, mapperInterface, repositoryClass, sortkeyInGroup, false);
    }
    default <T extends Entity> void registerEntity(String entityGroupName,
                                                   Class<T> entityClass,
                                                   Class<? extends MapperApi<T>> mapperInterface,
                                                   Class<? extends Repository> repositoryClass,
                                                   int sortkeyInGroup,
                                                   boolean placePlaceAfter) {
        Constructor constructor = ReflectionUtils.getConstructor(entityClass);
        Entity entity = (Entity) ReflectionUtils.newInstance(constructor);
        EntitySchema entitySchema = entity.getEntitySchema();
        registerEntity(entityGroupName, entityClass, entitySchema, mapperInterface, repositoryClass, sortkeyInGroup, placePlaceAfter);
    }
    <T extends Entity> void registerEntity(String entityGroupName,
                                           Class<T> entityClass,
                                           EntitySchema entitySchema,
                                           Class<? extends MapperApi<T>> mapperInterface,
                                           Class<? extends Repository> repositoryClass,
                                           int sortkeyInGroup,
                                           boolean placeSpaceAfter);


    void registerEntityGroup(String entityGroupName, int sortkey);

    default void registerEntityGroup(String entityGroupName) {
        registerEntityGroup(entityGroupName, Integer.MAX_VALUE);
    }

    void registerApp(App app);
    
    void registerWebApp(String webAppClass);

    void registerBatch(String batchClass);

    void registerServiceApi(ServiceApi serviceApi);

    void registerServiceImpl(ServiceImpl serviceImpl);

    void registerTypeHandler(Class<? extends TypeHandler<?>> typeHandler);

    //registerWidget, registerWebApp
    List<PluginStubEntityEntry> getPluginStubEntityEntries();

    List<TypeHandler<?>> getTypeHandlers();
    List<EntityGroup> getEntityGroups();
    List<String> getBatchClasses();
    List<String> getWebAppClasses();
    void setPluginId(String pluginId);
    String getPluginId();
}
