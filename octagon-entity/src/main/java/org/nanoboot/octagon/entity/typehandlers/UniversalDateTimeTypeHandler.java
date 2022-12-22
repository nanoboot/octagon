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

package org.nanoboot.octagon.entity.typehandlers;

import org.nanoboot.powerframework.time.moment.UniversalDateTime;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@MappedJdbcTypes(JdbcType.OTHER)
@MappedTypes(UniversalDateTime.class)
public class UniversalDateTimeTypeHandler extends BaseTypeHandler<UniversalDateTime> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    UniversalDateTime parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter.toString(), JdbcType.VARCHAR.TYPE_CODE);
    }

    @Override
    public UniversalDateTime getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        return fromString(rs.getObject(columnName));
    }

    @Override
    public UniversalDateTime getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        return fromString(rs.getObject(columnIndex));
    }

    @Override
    public UniversalDateTime getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return fromString(cs.getObject(columnIndex));
    }

    private UniversalDateTime fromString(Object universalDateTimeAsString) {
        if (universalDateTimeAsString == null) {
            return null;
        }
        return new UniversalDateTime((String) universalDateTimeAsString);
    }
}
