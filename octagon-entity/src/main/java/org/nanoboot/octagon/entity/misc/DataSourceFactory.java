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

package org.nanoboot.octagon.entity.misc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.nanoboot.octagon.core.config.OctagonConfPath;
import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class DataSourceFactory {

    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(DataSourceFactory.class);
    private final OctagonConfPath octagonConfPath;

    public DataSourceFactory(OctagonConfPath octagonConfPath) {
        this.octagonConfPath = octagonConfPath;
    }
  public DataSource createInstance() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        String url = createJdbcUrl("octagon");
        dataSource.setUrl(url);
        
        return dataSource;
    }

    public DataSource createInstanceProxy() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        String url = createJdbcUrl("octagon");
        dataSource.setUrl(url);

        //
        class DataSourceHandler implements InvocationHandler {

            private final DataSource originalDataSource;

            public DataSourceHandler(DataSource originalDataSourceIn) {
                this.originalDataSource = originalDataSourceIn;
            }

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
                String methodName = method.getName();
                System.out.println("DataSourceHandler was INTERCEPTED, method= " + method.getName());
                Object returnedObject = method.invoke(originalDataSource, args);

                if (methodName.equals("getConnection")) {
                    Connection conn = (Connection) returnedObject;
                    attachDatabase("system", conn);
//                    attachDatabase("action-log", conn);
//                    attachDatabase("favorite", conn);
//                    attachDatabase("pinning", conn);
//                    attachDatabase("reminder", conn);
//                    attachDatabase("whining", conn);
//                    attachDatabase("encyclopedia", conn);
//                    attachDatabase("development", conn);
//                    attachDatabase("person", conn);

                    String sql = "PRAGMA database_list";

                    try (
                             Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {

                        while (rs.next()) {
                            String name = rs.getString("name");
                            System.out.println("Found database: " + name);
                        }
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }

                }
                return returnedObject;
            }
        }

        DataSource dataSourceProxy = (DataSource) Proxy.newProxyInstance(DataSource.class.getClassLoader(),
                new Class[]{DataSource.class}, new DataSourceHandler(dataSource));
//
        return dataSourceProxy;
    }

    private void attachDatabase(String databaseName, Connection conn) {
        executeSql(conn, "ATTACH DATABASE '" + createFilePathWithName(databaseName) + "' as '" + databaseName + "';");
    }

    private String createJdbcUrl(String databaseName) {
        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:sqlite:");
        sb.append(createFilePathWithName(databaseName));
        sb.append("?foreign_keys=on;");
        
        String url = sb.toString();
        return url;
    }

    private String executeSql(Connection conn, String sql) {
        try ( Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            return null;
        } catch (SQLException e) {
            LOG.error(e);
            throw new OctagonException(e);
        }
    }

    private String createFilePathWithName(String databaseName) {
        StringBuilder sb = new StringBuilder();
        sb.append(octagonConfPath.getOctagonDataDir());
        sb.append("/");
        sb.append(databaseName);
        sb.append(".sqlite3");
        return sb.toString();
    }
}
