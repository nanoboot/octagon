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

package org.nanoboot.octagon.entity.impl.repos;

import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.entity.api.SqlExplorer;
import lombok.Data;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@Data
public class SqlExplorerImpl implements SqlExplorer {
    private DataSource dataSource;

    @Override
    public String[][] execute(String sql) {
        String[][] result = null;

        Connection conn = createConnection();
        Statement statement;
        try {
            statement = conn.createStatement();
        } catch (SQLException e) {
            throw new OctagonException("Creating statement failed: " + e);
        }
        ResultSet resultSet = null;
        List<String> columnNames = new ArrayList<>();
        List<List<String>> rows = new ArrayList<>();
        try {
            resultSet = statement.executeQuery(sql);

            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = rsmd.getColumnName(i);
                columnNames.add(columnName);
            }

            while (resultSet.next()) {

                List<String> row = new ArrayList<>();
                for (int i = 0; i < columnNames.size(); i++) {
                    String columnName = columnNames.get(i);
                    String value = resultSet.getString(columnName);
                    row.add(value);
                }
                rows.add(row);
            }

            conn.close();
        } catch (SQLException e) {
            throw new OctagonException("Executing sql " + sql + " failed: " + e);
        } finally {
            closeConnectionAndResultSet(conn, resultSet);
        }
        result = new String[rows.size() + 1][];
        result[0] = convertListToArray(columnNames);
        for(int i = 0; i < rows.size(); i++) {
            result[i + 1] = convertListToArray(rows.get(i));
        }
        return result;
    }

    private String[] convertListToArray(List<String> list) {
        String[] array = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }
    private void closeConnectionAndResultSet(Connection connection, ResultSet resultSet) {
        try {
            if(resultSet != null) {
                resultSet.close();
            }
            if(!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new OctagonException("Closing connection and result set failed: " + e);
        }
    }
    private Connection createConnection() {
        if (dataSource == null) {
            throw new OctagonException("datasource == null");
        }
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new OctagonException("Creating connection failed " + e);
        }
    }
}
