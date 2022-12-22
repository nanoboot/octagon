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

package org.nanoboot.octagon.plugin.system;

import java.util.Properties;
import org.nanoboot.octagon.entity.classes.Query;
import org.nanoboot.octagon.entity.classes.SqlQuery;
import org.nanoboot.octagon.entity.impl.mappers.QueryMapper;
import org.nanoboot.octagon.entity.impl.mappers.SqlQueryMapper;
import org.nanoboot.octagon.entity.impl.repos.QueryRepositoryImplSQLiteMyBatis;
import org.nanoboot.octagon.entity.impl.repos.SqlQueryRepositoryImplSQLiteMyBatis;
import org.nanoboot.octagon.entity.typehandlers.*;
import org.nanoboot.octagon.plugin.api.core.Plugin;
import org.nanoboot.octagon.plugin.api.core.PluginStub;
import org.nanoboot.octagon.plugin.api.core.PluginStubImpl;
import lombok.Getter;
import org.nanoboot.octagon.plugin.system.webapps.ListOfWebApps;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class SystemPlugin implements Plugin {

    @Getter
    private PluginStub pluginStub = new PluginStubImpl();

    @Override
    public String getGroup() {
        return "system";
    }

    @Override
    public String getId() {
        return "system";
    }

    @Override
    public String getVersion() {
        return "0.0.0";
    }

    @Override
    public String init(Properties propertiesConfiguration) {

        pluginStub.registerEntityGroup("system", 10);
        pluginStub
                .registerEntity(
                        "system",
                        Query.class,
                        QueryMapper.class,
                        QueryRepositoryImplSQLiteMyBatis.class, 1);
        pluginStub
                .registerEntity(
                        "system",
                        SqlQuery.class,
                        SqlQueryMapper.class,
                        SqlQueryRepositoryImplSQLiteMyBatis.class, 2);

        pluginStub.registerTypeHandler(ActionTypeTypeHandler.class);
        pluginStub.registerTypeHandler(BooleanTypeHandler.class);
        pluginStub.registerTypeHandler(JsonObjectTypeHandler.class);
        pluginStub.registerTypeHandler(LocalDateTypeHandler.class);
        pluginStub.registerTypeHandler(UniversalDateTimeTypeHandler.class);
        pluginStub.registerTypeHandler(UUIDTypeHandler.class);
        //
        pluginStub.registerWebApp(ListOfWebApps.class.getName());
        return null;
    }

    @Override
    public boolean isSystemPlugin() {
        return true;
    }

    @Override
    public boolean hasMigrationSchema() {
        return true;
    }
}
