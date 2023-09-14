///////////////////////////////////////////////////////////////////////////////////////////////
// Octagon.
// Copyright (C) 2023-2023 the original author or authors.
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

package org.nanoboot.octagon.persistence.impl.sqlite;

import org.nanoboot.dbmigration.core.main.DBMigration;
import org.nanoboot.dbmigration.core.main.MigrationResult;

/**
 *
* @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 */
public class SqliteDatabaseMigration {

    private static SqliteDatabaseMigration INSTANCE;

    private SqliteDatabaseMigration() {
        //Not meant to be instantiated
    }

    public static SqliteDatabaseMigration getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SqliteDatabaseMigration();
        }
        return INSTANCE;
    }

    public MigrationResult migrate() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
        String jdbcUrl= "jdbc:sqlite:" + System.getProperty("octagon.confpath") + "/" + "color_shapes_archive.sqlite";
        System.err.println("jdbcUrl=" + jdbcUrl);
        String clazz = this.getClass().getName();
        DBMigration dbMigration = DBMigration
                .configure()
                .dataSource(jdbcUrl)
                .installedBy("octagon-persistence-impl-sqlite")
                .name("octagon")
                .sqlDialect("sqlite", "org.nanoboot.dbmigration.core.persistence.impl.sqlite.DBMigrationPersistenceSqliteImpl")
                .sqlMigrationsClass(clazz)
                .load();
        return dbMigration.migrate();
    }

}
