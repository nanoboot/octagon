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

import org.nanoboot.octagon.entity.api.LabelRepository;
import org.nanoboot.powerframework.utils.NamingConvention;
import org.nanoboot.powerframework.utils.NamingConventionConvertor;
import org.nanoboot.powerframework.web.html.WebElement;
import org.nanoboot.powerframework.web.html.tags.*;
import org.nanoboot.powerframework.xml.Attribute;
import org.nanoboot.octagon.core.exceptions.OctagonException;

import java.util.HashMap;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class SqlExplorerTable extends Table {
    private final LabelRepository labelRepository;
    private final HashMap<String, String> foreignKeysLegendMap;
    private final String type;
    //index of column TYPE, if is present, otherwise -1
    private int TYPEcolumnIndex = -1;

    public SqlExplorerTable(String[][] array, LabelRepository labelRepository, String type, String foreignKeysLegend) {
        this.labelRepository = labelRepository;
        this.foreignKeysLegendMap = new HashMap<>();
        this.type = type;

        String[] foreignKeysLegendArray = null;
        if (foreignKeysLegend != null && !foreignKeysLegend.isBlank()) {
            foreignKeysLegendArray = foreignKeysLegend.split(" ");
            for (String e : foreignKeysLegendArray) {
                String[] array2 = e.split("=");
                if (array2.length != 2) {
                    throw new OctagonException("array2.length!= 2");
                }
                String columnName = array2[0];
                String foreignKey = array2[1];
                foreignKeysLegendMap.put(columnName, foreignKey);
            }
        }

        for (String columnName : array[0]) {

            if (columnName.endsWith(("_ID")) && !foreignKeysLegendMap.containsKey(columnName) && !columnName.equals("OBJECT_ID") && !columnName.equals("PARENT_ID")) {

                String foreignKey = columnName.replace("_ID", "");
                foreignKey = NamingConventionConvertor.convert(foreignKey, NamingConvention.DATABASE, NamingConvention.JAVA);

                foreignKeysLegendMap.put(columnName, foreignKey);
            }
        }


        WebElement showAllColumnsButton = new WebElement("button");
        showAllColumnsButton.getAttributes().setAllowAnyAttribute(true);
        showAllColumnsButton.add(new Attribute("type", "button"));
        StringBuilder jsCodeFragment = new StringBuilder();
        int index = 0;
        int columnCount = array[0].length;
        int lastIndex = columnCount - 1;
        //["Saab", "Volvo", "BMW"];
        for (String columnName : array[0]) {
            jsCodeFragment.append("'column_").append(columnName).append("'");
            if (index < lastIndex) {
                jsCodeFragment.append(", ");
            }
            index++;
        }

        int columnNameIndex = 0;
        for (int i = 0; i < array[0].length; i++) {
            String columnName = array[0][columnNameIndex];
            if (columnName.equals("TYPE")) {
                TYPEcolumnIndex = columnNameIndex;
                break;
            }
            columnNameIndex++;
        }
        showAllColumnsButton.add(new Attribute("onclick", "showColumnsWithoutCookies([" + jsCodeFragment.toString() + "])"));
        showAllColumnsButton.setInnerText("Show all columns");
        this.getElements().setAllowAnyElement(true);

        Tr row0 = new Tr();
        row0.add(new Td(""));
        row0.add(new Td(showAllColumnsButton));
        this.add(row0);

        addHideRow(array[0]);
        addThRow(array[0]);
        for (int i = 1; i < array.length; i++) {
            addTdRow(array[i], array[0], i);
        }
    }

    private void addHideRow(String[] array) {
        Tr tr = new Tr();
        tr.add(new Td(""));
        for (String columnName : array) {
            WebElement button = new WebElement("button");
            button.getAttributes().setAllowAnyAttribute(true);
            button.add(new Attribute("type", "button"));
            button.add(new Attribute("onclick", "hideColumnWithoutCookies('column_" + columnName + "')"));
            button.setInnerText("Hide");
            Td td = new Td(button);

            td.getAttributes().setAllowAnyAttribute(true);
            td.add(new Attribute("class", "column_" + columnName));

            tr.add(td);
        }
        add(tr);
    }

    private void addThRow(String[] array) {
        Tr tr = new Tr();
        tr.add(new Td("#"));
        for (String columnName : array) {
            Th th = new Th(columnName);

            th.getAttributes().setAllowAnyAttribute(true);
            th.add(new Attribute("class", "column_" + columnName));

            tr.add(th);
        }
        add(tr);
    }

    private void addTdRow(String[] array, String[] columnNames, int rowNumber) {
        Tr tr = new Tr();
        tr.add(new Td(String.valueOf(rowNumber)));
        int columnIndex = 0;
        String id = null;
        for (String value : array) {
            String finalValue = value == null ? "" : value;
            String columnName = columnNames[columnIndex];
            if (columnName.toLowerCase().equals("id")) {
                id = finalValue;
            }
            if (finalValue != null && !finalValue.isEmpty() && foreignKeysLegendMap.containsKey(columnName)) {
                String foreignKey = foreignKeysLegendMap.get(columnName);
                String label = labelRepository.getLabel(foreignKey, value);
                String href = "read?className=" + foreignKey.substring(0, 1).toUpperCase() + foreignKey.substring(1) + "&id=" + value;
                finalValue = new A(href, label).setInnerText(label).toString();
            }
            if (finalValue != null && !finalValue.isEmpty() && type != null && !type.isBlank() && columnName.equals("NAME") && id != null) {
                finalValue = new A("read?className=" + type + "&id=" + id, finalValue).setInnerText(finalValue).toString();
            }
            if (finalValue != null && !finalValue.isEmpty() && columnName.equals(("OBJECT_ID")) && !foreignKeysLegendMap.containsKey(columnName) && TYPEcolumnIndex != -1) {

                String type = array[TYPEcolumnIndex];
                String foreignKey = type.substring(0,1).toLowerCase() + type.substring(1);

                String label = labelRepository.getLabel(foreignKey, value);
                String href = "read?className=" + type + "&id=" + value;
                finalValue = new A(href, label).setInnerText(label).toString();
            }
            Td td = new Td(finalValue);

            td.getAttributes().setAllowAnyAttribute(true);
            td.add(new Attribute("class", "column_" + columnName));

            tr.add(td);
            columnIndex++;
        }
        add(tr);
    }

}
