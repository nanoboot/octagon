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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nanoboot.octagon.plugin.api.core.Plugin;
import org.nanoboot.dbmigration.core.main.DBMigration;
import org.nanoboot.octagon.core.config.OctagonConfPath;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class DBMigrationPluginLoadingHandlerImpl implements PluginLoadingHandler {

    private static final Logger LOG = LogManager.getLogger(DBMigrationPluginLoadingHandlerImpl.class);
    private final OctagonConfPath octagonConfPath;
    private final String jdbcUrl;

    public DBMigrationPluginLoadingHandlerImpl(OctagonConfPath octagonConfPath) {
        this.octagonConfPath = octagonConfPath;
        //TODO refactor in some way
        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:sqlite:");
        sb.append(this.octagonConfPath.getOctagonDataDir());
        sb.append("/octagon.sqlite3");
        sb.append("?foreign_keys=on;");

        this.jdbcUrl = sb.toString();
    }

    @Override
    public boolean handle(Plugin plugin) {
        if (plugin.hasMigrationSchema()) {
            LOG.info("DB Migration: START");
            boolean result = migrate(plugin);
            LOG.info("DB Migration: END");
            return result;
        } else {
            LOG.info("DB Migration: SKIPPED : NOTHING TO DO");
            return true;
        }

    }

    private boolean migrate(Plugin plugin) {
        String clazz = plugin.getClass().getName();
        System.out.println("clazz=" + clazz);
        DBMigration dbMigration = DBMigration
                .configure()

                .dataSource(this.jdbcUrl)
                .installedBy("octagon_system")
                .name(plugin.getId().replace("-", ""))
                .sqlDialect("sqlite", "org.nanoboot.dbmigration.core.persistence.impl.sqlite.DBMigrationPersistenceSqliteImpl")
                .sqlMigrationsClass(clazz)
                .load();
        dbMigration.migrate();
        return true;
    }

}
