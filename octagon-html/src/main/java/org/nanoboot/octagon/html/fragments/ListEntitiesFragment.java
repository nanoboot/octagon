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

package org.nanoboot.octagon.html.fragments;

import org.nanoboot.octagon.entity.api.LabelRepository;
import org.nanoboot.octagon.entity.api.NamedListRepository;
import org.nanoboot.octagon.entity.api.RepositoryRegistry;
import org.nanoboot.octagon.entity.core.ActionType;
import org.nanoboot.octagon.entity.utils.QueryDescription;
import org.nanoboot.octagon.plugin.favorite.api.FavoriteRepository;
import org.nanoboot.octagon.plugin.pinning.api.PinningRepository;
import org.nanoboot.octagon.plugin.reminder.api.ReminderRepository;
import org.nanoboot.octagon.html.tables.EntitiesTable;
import org.nanoboot.powerframework.web.html.WebElement;
import org.nanoboot.powerframework.web.html.tags.*;
import org.nanoboot.powerframework.xml.Attribute;
import org.nanoboot.powerframework.xml.NoElement;
import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.octagon.entity.core.EntityAttribute;

import java.util.List;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class ListEntitiesFragment extends AbstractEntityFragment {
    public ListEntitiesFragment(Class entityClass, List<Entity> entities, QueryDescription queryDescription, LabelRepository labelRepository, NamedListRepository namedListRepository, RepositoryRegistry repositoryRegistry) {
        super(entityClass, ActionType.LIST);
        this.add(new H2(new NoElement("Column visibility")));
        this.add(getFlipColumnVisibilityTable(entityClass));

        this.add(new H2(new NoElement("Data")));

        List<String> pinningList = ((PinningRepository)repositoryRegistry.find("Pinning")).makePinningList();
        List<String> reminderList = ((ReminderRepository)repositoryRegistry.find("Reminder")).makeReminderWithTypesList();
        List<String> favoriteList = ((FavoriteRepository)repositoryRegistry.find("Favorite")).makeFavoriteWithTypesList();

        this.add(new EntitiesTable(entities, queryDescription, returnNewEmptyInstance(entityClass), labelRepository, namedListRepository, pinningList, reminderList, favoriteList));
    }

    private Table getFlipColumnVisibilityTable(Class entityClass) {
        Table showHideColumnsTable = new Table();
        Entity emptyEntity = returnNewEmptyInstance(entityClass);
        Tr tr1 = new Tr();
        showHideColumnsTable.add(tr1);

        for (EntityAttribute e : emptyEntity.getSchema()) {
            WebElement button = new WebElement("span", e.getHumanName());
            button.getAttributes().setAllowAnyAttribute(true);
            button.add(new Attribute("style", "border:0px solid black;background-color:#1E90FF;padding:10px;margin:10px;"));


            String className = "column_" + e.getName();
            String javaScriptCode = "flipColumnVisibility('" + className + "');";

            button.add(new Attribute("onclick", javaScriptCode));
            button.add(new Attribute("id", "flipLabel_" + e.getName()));

            Td td = new Td(button);
            td.getAttributes().setAllowAnyAttribute(true);
            td.add(new Attribute("style", "padding:0;white-space: nowrap;border:0px solid black;"));
            td.getAttributes().setAllowAnyAttribute(true);

            tr1.add(td);
            tr1.getAttributes().setAllowAnyAttribute(true);

        }
        tr1.add(new Attribute("style", "border:0px solid black;"));
        showHideColumnsTable.getAttributes().setAllowAnyAttribute(true);
        showHideColumnsTable.add(new Attribute("class","column_visibility_table"));
        showHideColumnsTable.add(new Attribute("style", "border:0px solid black;"));
        return showHideColumnsTable;
    }
}
