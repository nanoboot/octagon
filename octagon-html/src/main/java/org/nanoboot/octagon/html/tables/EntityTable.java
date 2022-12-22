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

package org.nanoboot.octagon.html.tables;

import org.nanoboot.octagon.html.links.ActionsLinks;
import org.nanoboot.octagon.html.misc.DeprecatedLabel;
import org.nanoboot.octagon.entity.api.LabelRepository;
import org.nanoboot.powerframework.web.html.tags.*;
import org.nanoboot.powerframework.xml.Attribute;
import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.octagon.entity.core.EntityAttribute;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class EntityTable extends OctagonTable {
    private final LabelRepository labelRepository;

    public EntityTable(Entity entity, LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
        int i = 0;
        String columnName;
        List<EntityAttribute> schemas = entity.getSchema();
        for (String columnValue : entity.toStringArray()) {
            EntityAttribute schema = schemas.get(i);
            columnName = schema.getHumanName();

            Tr tr = new Tr();

            boolean deprecated = schema.getDeprecated() != null && schema.getDeprecated().equals(Boolean.TRUE);
            Th th = new Th(deprecated ? new DeprecatedLabel(columnName).toString() : columnName);
            th.getAttributes().setAllowAnyAttribute(true);




            th.add(new Attribute("style", "text-align:left;"));

            if(schema.getHelp() != null) {
                th.add(new Attribute("title", schema.getHelp()));
            }
            String cellContent;

            if (schema == null) {
                throw new OctagonException("Schema is null");
            }

            columnValue = transformIfForeignKey(columnValue, schema, labelRepository);
            cellContent = columnValue;

            cellContent = transformToLinkIfObjectId(cellContent, entity, schema, labelRepository);
            cellContent = transformToLinkIIfNeeded(cellContent, schema);
            if (!cellContent.contains("<a href")) {
                cellContent = HtmlUtils.htmlEscape(cellContent);
            }
            Td td = new Td(cellContent);
            tr.add(th, td);
            this.add(tr);
            i++;
        }


        Tr tr = new Tr();

        Th th = new Th(ActionsLinks.ACTION);
        Td td = new Td(new ActionsLinks(entity).toString());

        tr.add(th, td);
        this.add(tr);
    }
//    <table>
//    <tr>
//        <th>Id</th>
//        <td><%=thing.getId()%></td>
//    </tr>
//    <tr>
//        <th>Path</th>
//        <td><%=thing.getPath()%></td>
//    </tr>
//    <tr>
//        <th>Name</th>
//        <td><%=thing.getName()%></td>
//    </tr>
//    <tr>
//        <th>Alias</th>
//        <td><%=thing.getAlias()%></td>
//    </tr>
//    <tr>
//        <th>Since</th>
//        <td><%=thing.getSince()%></td>
//    </tr>
//    <tr>
//    <th>Price Kč</th>
//        <td><%=thing.getPriceKc()%></td>
//    </tr>
//    <tr>
//        <th>Note</th>
//        <td><%=thing.getNote()%></td>
//    </tr>
//
//    <tr>
//        <th>Action</th>
//        <td><a href="readThing?id=<%=thing.getId()%>">view</a> <a
//    href="updateThing?id=<%=thing.getId()%>">update</a> <a
//    href="deleteThing?id=<%=thing.getId()%>">delete</a></td>
//    </tr>
//</table>
}
