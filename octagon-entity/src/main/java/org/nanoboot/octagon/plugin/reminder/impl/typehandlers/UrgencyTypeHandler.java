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

package org.nanoboot.octagon.plugin.reminder.impl.typehandlers;

import org.nanoboot.octagon.plugin.reminder.classes.Urgency;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class UrgencyTypeHandler implements TypeHandler<Urgency> {

    public Urgency getResult(ResultSet rs, String param) throws SQLException {
        return ofString(rs.getString(param));
    }

    @Override
    public Urgency getResult(ResultSet rs, int i) throws SQLException {
        return ofString(rs.getString(i));
    }

    public Urgency getResult(CallableStatement cs, int col) throws SQLException {
        return ofString(cs.getString(col));
    }

    public void setParameter(PreparedStatement ps, int paramInt, Urgency paramType, JdbcType jdbcType)
            throws SQLException {
        if (jdbcType == null || paramType == null) {
            ps.setNull(paramInt, java.sql.Types.NULL);
        } else {
            ps.setString(paramInt, paramType.name());
            //s.setObject(i, parameter.name(), jdbcType.TYPE_CODE);
        }

    }
    private Urgency ofString(String s) {
        return s == null ? null : Urgency.valueOf(s);
    }
}
