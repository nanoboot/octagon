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

import org.nanoboot.octagon.entity.classes.EntityLabel;
import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.octagon.entity.core.EntityAttribute;
import org.nanoboot.octagon.entity.core.EntityAttributeType;
import org.nanoboot.octagon.entity.utils.QueryDescription;
import org.nanoboot.octagon.html.misc.DeprecatedLabel;
import org.nanoboot.octagon.entity.api.LabelRepository;
import org.nanoboot.octagon.entity.api.NamedListRepository;
import org.nanoboot.powerframework.web.html.WebElement;
import org.nanoboot.powerframework.web.html.attributes.Action;
import org.nanoboot.powerframework.web.html.attributes.Id;
import org.nanoboot.powerframework.web.html.attributes.Method;
import org.nanoboot.powerframework.web.html.attributes.Name;
import org.nanoboot.powerframework.web.html.tags.*;
import org.nanoboot.powerframework.xml.Attribute;
import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.html.forms.BooleanSelect;
import org.nanoboot.octagon.html.forms.ListSelect;
import org.nanoboot.octagon.html.forms.NamedListSelect;
import org.nanoboot.octagon.html.links.ActionsLinks;
import org.nanoboot.octagon.html.misc.ClassNameFormInput;
import org.nanoboot.octagon.html.misc.FilterSubmitButton;
import org.nanoboot.octagon.html.misc.HiddenProcessFormInput;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class EntitiesTable extends OctagonTable {
    private final List<Entity> entities;
    private final QueryDescription queryDescription;
    private final Entity emptyEntity;
    private final LabelRepository labelRepository;

    private final NamedListRepository namedListRepository;
    private final List<String> pinningList;
    private final List<String> reminderList;
    private final List<String> favoriteList;


    public EntitiesTable(List<Entity> entities, QueryDescription queryDescription, Entity emptyEntity, LabelRepository labelRepository, NamedListRepository namedListRepository, List<String> pinningList, List<String> reminderList, List<String> favoriteList) {
        this.entities = entities;
        this.queryDescription = queryDescription;
        this.emptyEntity = emptyEntity;
        this.labelRepository = labelRepository;
        this.namedListRepository = namedListRepository;
        this.pinningList = pinningList;
        this.reminderList = reminderList;
        this.favoriteList = favoriteList;
        if (entities.isEmpty()) {
            this.getElements().setAllowAnyElement(true);
            this.add(new Br());
            this.getNoElement().setPlainText("There is no data.");
            this.add(new Br());
        }

        this.add(getShowHideRow());
        this.add(getHeaderRow());
        this.add(getFilterRow(this.queryDescription));

        int rowNumber = 1;
        for (Entity e : this.entities) {
            this.add(getStandardRow(e, rowNumber++));
        }
        this.getAttributes().setAllowAnyAttribute(true);
        this.add(new Attribute("style", "font-size:90%;margin-top:20px;"));
    }

    private Tr getShowHideRow() {
        Tr tr = new Tr();
        tr.add(new Th(""));

        WebElement button0 = new WebElement("button", "Show all columns");
        button0.getAttributes().setAllowAnyAttribute(true);
        button0.add(new Attribute("type", "button"));

        StringBuilder jSCode = new StringBuilder();
        jSCode.append("showColumns([");


        for (EntityAttribute e : emptyEntity.getSchema()) {
            jSCode.append("'").append("column_").append(e.getName()).append("'").append(",");
        }
        jSCode.append("]);");

        button0.add(new Attribute("onclick", jSCode.toString()));
        tr.add(new Th(button0));
        tr.add(new Th(""));
        tr.add(new Th(""));
        tr.add(new Th(""));

        for (EntityAttribute e : emptyEntity.getSchema()) {
            WebElement button = new WebElement("button", "Hide");
            button.getAttributes().setAllowAnyAttribute(true);
            button.add(new Attribute("type", "button"));

            String className = "column_" + e.getName();
            String javaScriptCode = "hideColumn('" + className + "')";


            button.add(new Attribute("onclick", javaScriptCode));

            Td td = new Td(button);
            td.getAttributes().setAllowAnyAttribute(true);
            td.add(new Attribute("class", "column_" + e.getName()));

            tr.add(td);
        }
        return tr;
    }

    private Tr getHeaderRow() {
        Tr tr = new Tr();

        tr.add(new Th("#"));
        tr.add(new Th(ActionsLinks.ACTION));
        tr.add(new Th("Pinned"));
        tr.add(new Th("Reminded"));
        tr.add(new Th("Favorite"));
        ;
        for (EntityAttribute e : emptyEntity.getSchema()) {
            boolean deprecated = e.getDeprecated() != null && e.getDeprecated().equals(Boolean.TRUE);
            Th th = new Th(deprecated ? new DeprecatedLabel(e.getHumanName()).toString() : e.getHumanName());

            th.getAttributes().setAllowAnyAttribute(true);
            th.add(new Attribute("class", "column_" + e.getName()));
            if (e.getHelp() != null) {
                th.add(new Attribute("title", e.getHelp()));
            }
            tr.add(th);
        }
        return tr;
    }

    private Tr getFilterRow(QueryDescription entityFilter) {
        Tr tr = new Tr();
        tr.add(new Th(""));

        Form filterForm = new Form();
        filterForm.getAttributes().addAll(
                new Method("get"),
                new Action("list?type=" + emptyEntity.getEntityName()),
                new Id("listFilter")
        );

        WebElement saveQueryLabel = new WebElement("label");
        saveQueryLabel.add(new Attribute("for", "saveQuery"));
        saveQueryLabel.setInnerText("Save");

        Input saveQueryCheckbox = new Input();
        saveQueryCheckbox.getAttributes().setAllowAnyAttribute(true);
        saveQueryCheckbox.getAttributes().addAll(
                new Attribute("type", "checkbox"),
                new Name("saveQuery"),
                new Id("saveQuery"),
                new Attribute("value", "yes")
        );

        filterForm.add(new HiddenProcessFormInput());
        filterForm.add(new ClassNameFormInput(emptyEntity.getEntityName()));
        //filterForm.add(new ResetButton());//todo
        filterForm.add(new FilterSubmitButton());

        //onclick="" style="cursor: pointer;"
        WebElement resetButton = new WebElement("button", "Reset");
        resetButton.getAttributes().setAllowAnyAttribute(true);
        resetButton.add(new Attribute("type", "button"));
        resetButton.add(new Attribute("onclick", "location.href='list" + emptyEntity.getEntityNameInPlural() + "';"));
        resetButton.add(new Attribute("style", "cursor: pointer;"));
        filterForm.getElements().setAllowAnyElement(true);
        filterForm.add(resetButton);

        filterForm.getElements().setAllowAnyElement(true);
        filterForm.add(saveQueryLabel);
        filterForm.add(saveQueryCheckbox);


        tr.add(new Td(filterForm.toString()));

        tr.add(new Th(""));//pin
        tr.add(new Th(""));//rem
        tr.add(new Th(""));//fav

        List<EntityAttribute> schema = emptyEntity.getSchema();

        for (EntityAttribute e : schema) {
            String filterValue = entityFilter.getMap().get(e.getName());
            if (e.getName().equals("id")) {
                //nothing to do
                Td td = new Td("");
                td.getAttributes().setAllowAnyAttribute(true);
                td.add(new Attribute("class", "column_" + e.getName()));
                tr.add(td);
                continue;
            }
            if (e.getType().equals(EntityAttributeType.BOOLEAN)) {
                BooleanSelect booleanSelect = new BooleanSelect(e, "listFilter", "filter_");

                if (filterValue != null) {
                    booleanSelect.set(!filterValue.equals("0") ? Boolean.TRUE : Boolean.FALSE);
                }
                Td td = new Td(booleanSelect.toString());
                td.getAttributes().setAllowAnyAttribute(true);
                td.add(new Attribute("class", "column_" + e.getName()));
                tr.add(td);
                continue;
            }
            if (e.getType().equals(EntityAttributeType.LIST)) {
                ListSelect listSelect = new ListSelect(e, "listFilter", "filter_");
                if (filterValue != null) {
                    listSelect.setDefault(filterValue);
                }
                Td td = new Td(listSelect.toString());
                td.getAttributes().setAllowAnyAttribute(true);
                td.add(new Attribute("class", "column_" + e.getName()));
                tr.add(td);
                continue;
            }
            if (e.getType().equals(EntityAttributeType.NAMED_LIST)) {
                List<EntityLabel> list = namedListRepository.generate(e.getNamedList(), null);
                if (list != null) {
                    NamedListSelect namedListSelect = new NamedListSelect(e, list, "listFilter", "filter_");
                    if (filterValue != null) {
                        namedListSelect.setDefault(filterValue);
                    }
                    Td td = new Td(namedListSelect.toString());
                    td.getAttributes().setAllowAnyAttribute(true);
                    td.add(new Attribute("class", "column_" + e.getName()));
                    tr.add(td);
                    continue;
                }
            }
            //else
            Input input = new Input();
            input.getAttributes().setAllowAnyAttribute(true);
            input.getAttributes().addAll(
                    new Name("filter_" + e.getName()),
                    new Id("filter_" + e.getName()),
                    new Attribute("size", "8"),
                    new Attribute("form", "listFilter")
            );
            if (filterValue != null) {
                input.getAttributes().addAll(
                        new Attribute("value", filterValue)
                );
            }
            Td td = new Td(input.toString());
            td.getAttributes().setAllowAnyAttribute(true);
            td.add(new Attribute("class", "column_" + e.getName()));


            tr.add(td);

        }

        return tr;
    }

    private Tr getStandardRow(Entity e, int rowNumber) {
        Tr tr = new Tr();
        tr.add(new Th(String.valueOf(rowNumber)));

        tr.add(new Td(new ActionsLinks(e).toString()));

        tr.add(new Td(pinningList.contains(e.getId().toString()) ? "<span style=\"color:green;font-weight:bold;\">Yes</span>" : "<span style=\"color:grey;\">No</span>"));
        tr.add(new Td(reminderList.contains(e.getId().toString()) ? "<span style=\"color:green;font-weight:bold;\">Yes</span>" : "<span style=\"color:grey;\">No</span>"));
        tr.add(new Td(favoriteList.contains(e.getId().toString()) ? "<span style=\"color:green;font-weight:bold;\">Yes</span>" : "<span style=\"color:grey;\">No</span>"));

        String[] strings = e.toStringArray();
        List<EntityAttribute> schemas = e.getSchema();
        int i = 0;
        for (String string : strings) {
            EntityAttribute schema = schemas.get(i);
            if (schema == null) {
                throw new OctagonException("Schema is null");
            }

            String cellContent;
            string = transformIfForeignKey(string, schema, labelRepository);
            cellContent = string;

            if (schema.getName().equals("name")) {
                String entityName = e.getEntityName();
                UUID id = e.getId();
                String id1 = id.toString();
                String cellContent1 = getForeignLink(entityName, cellContent, id1).toString();
                cellContent = cellContent1;
            }


            cellContent = transformToLinkIfObjectId(cellContent, e, schema, labelRepository);
            cellContent = transformToLinkIIfNeeded(cellContent, schema);
            if (!cellContent.contains("<a href")) {
                cellContent = HtmlUtils.htmlEscape(cellContent);
            }
            Td td = new Td(cellContent);
            td.getAttributes().setAllowAnyAttribute(true);
            td.add(new Attribute("class", "column_" + schema.getName()));
            if (string.equals(strings[0])) {
                td.getAttributes().setAllowAnyAttribute(true);
                td.getAttributes().add(new Attribute("style", "font-size: 75%"));
            }

            tr.add(td);

            i++;
        }

        return tr;
    }

}
