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
import org.nanoboot.octagon.entity.api.RepositoryRegistry;
import org.nanoboot.octagon.entity.classes.EntityLabel;
import org.nanoboot.octagon.entity.classes.RelatedList;
import org.nanoboot.octagon.entity.core.ActionType;
import org.nanoboot.octagon.plugin.favorite.api.FavoriteRepository;
import org.nanoboot.octagon.plugin.pinning.api.PinningRepository;
import org.nanoboot.octagon.plugin.reminder.api.ReminderRepository;
import org.nanoboot.octagon.html.links.OctagonForeignKeyLink;
import org.nanoboot.octagon.html.tables.EntityTable;
import org.nanoboot.powerframework.utils.NamingConvention;
import org.nanoboot.powerframework.utils.NamingConventionConvertor;
import org.nanoboot.powerframework.web.html.WebElement;
import org.nanoboot.powerframework.web.html.tags.*;
import org.nanoboot.powerframework.xml.NoElement;
import org.nanoboot.octagon.entity.core.Entity;

import java.util.List;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class ViewEntityFragment extends AbstractEntityFragment {
    public ViewEntityFragment(Entity entity, LabelRepository labelRepository, RelatedList[] relatedLists, RepositoryRegistry repositoryRegistry) {
        super(entity.getEntityClass(), ActionType.READ, entity);
        this.add(new EntityTable(entity, labelRepository));

        List<String> pinningList = ((PinningRepository)repositoryRegistry.find("Pinning")).makePinningList();
        List<String> reminderList = ((ReminderRepository)repositoryRegistry.find("Reminder")).makeReminderWithTypesList();
        List<String> favoriteList = ((FavoriteRepository)repositoryRegistry.find("Favorite")).makeFavoriteWithTypesList();

        WebElement pinningInfo = new WebElement("div");
        pinningInfo.add(new WebElement("b", "Pinned: "));
        pinningInfo.add(new NoElement(pinningList.contains(entity.getId().toString()) ? "<span style=\"color:green;font-weight:bold;\">Yes</span>" : "<span style=\"color:grey;\">No</span>"));
        this.add(pinningInfo);

        WebElement reminderInfo = new WebElement("div");
        reminderInfo.add(new WebElement("b", "Reminded: "));
        reminderInfo.add(new NoElement(reminderList.contains(entity.getId().toString()) ? "<span style=\"color:green;font-weight:bold;\">Yes</span>" : "<span style=\"color:grey;\">No</span>"));
        this.add(reminderInfo);
        WebElement favoriteInfo = new WebElement("div");
        favoriteInfo.add(new WebElement("b", "Favorite: "));
        favoriteInfo.add(new NoElement(favoriteList.contains(entity.getId().toString()) ? "<span style=\"color:green;font-weight:bold;\">Yes</span>" : "<span style=\"color:grey;\">No</span>"));
        this.add(favoriteInfo);

        this.add(new H2(new NoElement("Related actions")));
        if (entity.getRelatedActionsForEntity() != null && entity.getRelatedActionsForEntity().length != 0) {
            Ul ul = new Ul();
            this.add(ul);
            for (String action : entity.getRelatedActionsForEntity()) {

                String href;
                String title;
                if (action.contains(":")) {
                    String[] array = action.split(":");
                    title = array[0];
                    href = array[1];
                } else {
                    title = action;
                    href = action;
                }
                if (href.contains("(#preselectionProperty)")) {
                    href = href.replace("(#preselectionProperty)", entity.getProperty(entity.getPreselectionPropertyName()));
                }

//                if (href.contains("(#type)")) {
//                    href = href.replace("(#type)", entity.getProperty("type"));
//                }
                A a = new A(href + entity.getId(), title);
                a.setInnerText(title);
                Li li = new Li(a);
                ul.add(li);
            }
        } else {
            add(new NoElement("Nothing found."));
        }

        this.add(new H2(new NoElement("Related lists")));
        if (relatedLists != null && relatedLists.length != 0) {
            for (RelatedList rl : relatedLists) {
                String foreignKey = rl.getForeignKey();
                String listName = rl.getName();
                this.add(new H3(new NoElement(listName + " (" + NamingConventionConvertor.convert(foreignKey, NamingConvention.JAVA, NamingConvention.HUMAN) + ")")));
                if (rl.getList().isEmpty()) {
                    add(new NoElement("Nothing found."));
                    continue;
                }
                Ul ul = new Ul();
                for (EntityLabel el : rl.getList()) {

                    String label = el.getLabel();
                    String id = el.getId();
                    OctagonForeignKeyLink link = new OctagonForeignKeyLink(foreignKey, label == null ? "labelIsNull" : label, id);
                    Li li = new Li(link);
                    ul.add(li);
                }
                this.add(ul);
            }
        } else {
            add(new NoElement("Nothing found."));
        }
    }

}
