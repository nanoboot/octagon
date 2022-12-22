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

package org.nanoboot.octagon.plugin.api.factories;

import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.entity.api.EntityRegistryEntry;
import org.nanoboot.octagon.entity.api.MapperApi;
import org.nanoboot.octagon.entity.api.QuestionRepository;
import org.nanoboot.octagon.entity.api.Repository;
import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.octagon.entity.impl.repos.EntityRegistryImpl;
import org.nanoboot.octagon.entity.impl.repos.RepositoryImpl;
import org.nanoboot.octagon.plugin.api.core.PluginStub;
import org.nanoboot.octagon.plugin.api.core.PluginStubEntityEntry;
import org.nanoboot.powerframework.reflection.ReflectionUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.mapper.MapperFactoryBean;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class EntityRegistryImplFactory {
    @Setter
    private PluginLoader pluginLoader;
    @Setter
    private SqlSessionFactory sqlSessionFactory;
    @Setter
    protected QuestionRepository questionRepository;

    public EntityRegistryImpl create() {
        EntityRegistryImpl entityRegistry = new EntityRegistryImpl();
        List<EntityRegistryEntry> entityRegistryEntries = new ArrayList<>();

        for(PluginStub pluginStub:pluginLoader.getPluginStubs()) {
            for(PluginStubEntityEntry pluginStubEntityEntry :pluginStub.getPluginStubEntityEntries()) {
                Class<? extends MapperApi<? extends Entity>> mapperInterface = pluginStubEntityEntry.getMapperInterface();
                Class<? extends Repository> repositoryClass = pluginStubEntityEntry.getRepositoryClass();

                MapperFactoryBean mapperFactoryBean = new MapperFactoryBean();
                mapperFactoryBean.setSqlSessionFactory(sqlSessionFactory);
                mapperFactoryBean.setMapperInterface(mapperInterface);

                Constructor constructor = ReflectionUtils.getConstructor(
                        repositoryClass,
                        MapperApi.class,
                        Class.class);

                Repository<? extends Entity> repository = null;
                try {
                    repository = (Repository<? extends Entity>) ReflectionUtils
                            .newInstance(constructor, ((org.nanoboot.octagon.entity.api.MapperApi) mapperFactoryBean.getObject()), pluginStubEntityEntry.getEntityClass());
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new OctagonException("Loading repository class implementation failed.", e);
                }
                ((RepositoryImpl)repository).setQuestionRepository(questionRepository);
                EntityRegistryEntry entityRegistryEntry = new EntityRegistryEntry(
                        pluginStubEntityEntry.getEntityGroupName(),
                        pluginStubEntityEntry.getEntityClass(),
                        pluginStubEntityEntry.getEntitySchema(),
                        repository,
                        pluginStubEntityEntry.getSortkeyInGroup(),
                        pluginStubEntityEntry.isPlaceSpaceAfter()

                );
                entityRegistryEntries.add(entityRegistryEntry);
                System.out.println("repo " + repository.getClassOfType().getSimpleName() + " has size: " + repository.list().size());
//                //
//                //test
//                System.out.println("Listing repository " + repository.getClassOfType());
//                for(Entity e:repository.list()) {
//                    System.out.print("Next entity:");
//                    System.out.println(e.getName() + " \n ");
//                    for(String str:e.toStringArray()) {
//                        System.out.print(e);
//                        System.out.print(" :: ");
//                    }
//                }
//                //

            }

        }
        entityRegistry.loadList(entityRegistryEntries);

        return entityRegistry;
    }
}
