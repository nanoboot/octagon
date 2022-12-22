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

package org.nanoboot.octagon.html.forms;

import org.nanoboot.powerframework.web.html.WebElement;
import org.nanoboot.powerframework.web.html.attributes.Id;
import org.nanoboot.powerframework.web.html.attributes.Name;
import org.nanoboot.powerframework.web.html.tags.Input;
import org.nanoboot.powerframework.xml.Attribute;
import org.nanoboot.powerframework.xml.NoElement;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class SqlExplorerForm extends OctagonForm {
    public SqlExplorerForm(String sql, String type, String foreignKeysLegend) {
        super("sqlExplorer", "execute", null, null);

        WebElement sqlLabel = new WebElement("label");
        sqlLabel.add(new Attribute("for", "sql"));
        sqlLabel.setInnerText("SQL" + ":");

        WebElement sqlTextArea = new WebElement("textarea");
        sqlTextArea.getAttributes().setAllowAnyAttribute(true);

        sqlTextArea.add(
                new Attribute("name", "sql"),
                new Attribute("id", "sql"),
                new Attribute("cols", "80"),
                new Attribute("rows", "10")
        );
        if (sql != null && !sql.isBlank()) {
            sqlTextArea.setInnerText(sql);
        }
        this.addRow(sqlLabel, sqlTextArea);



        WebElement typeLabel = new WebElement("label");
        typeLabel.add(new Attribute("for", "type"));
        typeLabel.setInnerText("Type" + ":");

        OctagonInput typeInput = new OctagonInput("text", "type", "type", null);
        typeInput.add(new Attribute("size", "40"));
        if (type != null && !type.isBlank()) {
            typeInput.add(new Attribute("value", type));
        }
        this.addRow(typeLabel, typeInput);




        WebElement foreignKeysLegendLabel = new WebElement("label");
        foreignKeysLegendLabel.add(new Attribute("for", "foreignKeysLegend"));
        foreignKeysLegendLabel.setInnerText("Foreign keys legend" + ":");

        OctagonInput foreignKeysLegendInput = new OctagonInput("text", "foreignKeysLegend", "foreignKeysLegend", null);
        foreignKeysLegendInput.add(new Attribute("size", "40"));
        if (foreignKeysLegend != null && !foreignKeysLegend.isBlank()) {
            foreignKeysLegendInput.add(new Attribute("value", foreignKeysLegend));
        }
        this.addRow(foreignKeysLegendLabel, foreignKeysLegendInput);


        add(new NoElement("&nbsp;"));

        WebElement saveSqlQueryLabel = new WebElement("label");
        saveSqlQueryLabel.add(new Attribute("for", "saveSqlQuery"));
        saveSqlQueryLabel.setInnerText("Save");

        Input saveSqlQueryCheckbox = new Input();
        saveSqlQueryCheckbox.getAttributes().setAllowAnyAttribute(true);
        saveSqlQueryCheckbox.getAttributes().addAll(
                new Attribute("type", "checkbox"),
                new Name("saveSqlQuery"),
                new Id("saveSqlQuery"),
                new Attribute("value", "yes")
        );
        getElements().setAllowAnyElement(true);
        add(saveSqlQueryLabel);
        add(saveSqlQueryCheckbox);
    }
}
