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

package org.nanoboot.octagon.struts.actions;

import org.nanoboot.octagon.entity.api.RepositoryRegistry;
import org.nanoboot.octagon.entity.classes.SqlQuery;
import org.nanoboot.octagon.entity.core.ActionType;
import org.nanoboot.octagon.plugin.actionlog.api.ActionLogRepository;
import org.nanoboot.octagon.entity.api.LabelRepository;
import org.nanoboot.octagon.entity.api.SqlExplorer;
import org.nanoboot.octagon.entity.api.SqlQueryRepository;
import org.nanoboot.powerframework.time.moment.UniversalDateTime;
import org.nanoboot.octagon.core.exceptions.OctagonException;

import org.nanoboot.octagon.html.forms.SqlExplorerForm;
import org.nanoboot.octagon.html.tables.SqlExplorerTable;
import lombok.Setter;
import org.springframework.web.util.HtmlUtils;

import java.util.UUID;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class SqlExplorerAction extends OctagonAction {

    @Setter
    private RepositoryRegistry repositoryRegistry;
    @Setter
    private SqlExplorer sqlExplorer;
    @Setter
    private LabelRepository labelRepository;

    private SqlQueryRepository sqlQueryRepository;
    private ActionLogRepository actionLogRepository;

    public String execute() {
        if(repositoryRegistry == null) {
            throw new OctagonException("Spring dependency repositoryRegistry is not satisfied (repositoryRegistry is null).");
        } else {
            sqlQueryRepository = (SqlQueryRepository) repositoryRegistry.find("SqlQuery");
            actionLogRepository = (ActionLogRepository) repositoryRegistry.find("ActionLog");
        }
        if (sqlExplorer == null) {
            throw new OctagonException("sqlExplorer == null");
        }
        this.setMessage("<a href=\"sqlExplorer\">Reset</a>");
        String sql = request.getParameter("sql");
        String foreignKeysLegend = request.getParameter("foreignKeysLegend");
        String type = request.getParameter("type");
        String sqlQueryId = request.getParameter("sqlQueryId");
        if (sqlQueryId != null && !sqlQueryId.isBlank()) {
            SqlQuery sqlQuery = sqlQueryRepository.read(sqlQueryId);
            sql = sqlQuery.getSql();
            foreignKeysLegend = sqlQuery.getForeignKeysLegend();
            type = sqlQuery.getType();
            setMessage("Loaded saved sql query " + /*HtmlUtils.htmlEscape(*/sqlQuery.getName()/*)*/);
        }

        SqlExplorerTable sqlExplorerTable = null;
        if (isGoingToProcessForm()) {
            if (sql == null) {
                throw new OctagonException("Parameter sql is required");
            }
            if (sql.trim().toLowerCase().startsWith("create")) {
                throw new OctagonException("Sql create is forbidden.");
            }
            if (sql.trim().toLowerCase().startsWith("update")) {
                throw new OctagonException("Sql update is forbidden.");
            }
            if (sql.trim().toLowerCase().startsWith("delete")) {
                throw new OctagonException("Sql delete is forbidden.");
            }
            if (type == null || type.isBlank()) {
                type = SqlQuery.tryToGuessType(sql);
            }
            String[][] result = null;
            try {
                result = sqlExplorer.execute(sql);
            } catch (Exception e) {
                String eMsg = e == null ? null : e.getMessage();
                if (eMsg != null) {
                    eMsg = HtmlUtils.htmlEscape(eMsg);
                }
                setError("Executing SQL failed: " + eMsg);
            }
            if (result != null && result.length <= 1) {
                setMessage("Nothing found");
            }
            if (result != null) {
                sqlExplorerTable = new SqlExplorerTable(result, labelRepository, type, foreignKeysLegend);
            }
            String saveSqlQuery = request.getParameter("saveSqlQuery");
            if (saveSqlQuery != null && !saveSqlQuery.isBlank()) {
                SqlQuery sqlQuery = new SqlQuery();
                sqlQuery.setId(UUID.randomUUID());
                sqlQuery.setName("New no named sql query " + UniversalDateTime.now().toLong());
                sqlQuery.setType("?");
                sqlQuery.setSql(sql);
                if (foreignKeysLegend != null && !foreignKeysLegend.isBlank()) {
                    sqlQuery.setForeignKeysLegend(foreignKeysLegend);
                }
                if (type != null && !type.isBlank()) {
                    sqlQuery.setType(type);
                }
                sqlQueryRepository.create(sqlQuery);
                persistActionLog(actionLogRepository, ActionType.CREATE, null, sqlQuery, sqlQuery, null);
                String msg = "Sql query was saved as <a href=\"read?className=SqlQuery&id=" + sqlQuery.getId().toString() + "\">" + sqlQuery.getName() + "</a>";
                setMessage(msg);
            }
        }
        SqlExplorerForm sqlExplorerForm = new SqlExplorerForm(sql, type, foreignKeysLegend);
        String fragment = sqlExplorerForm.toString();
        if (sqlExplorerTable != null) {
            fragment = fragment + "<br>" + sqlExplorerTable;
        }
        this.request.setAttribute("fragment", fragment);
        return "success";
    }
}
