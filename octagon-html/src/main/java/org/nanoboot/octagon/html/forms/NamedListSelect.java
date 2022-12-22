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

import org.nanoboot.octagon.entity.classes.EntityLabel;
import org.nanoboot.powerframework.web.html.tags.Option;
import org.nanoboot.powerframework.web.html.tags.Select;
import org.nanoboot.powerframework.xml.Attribute;
import org.nanoboot.powerframework.xml.Element;
import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.entity.core.EntityAttribute;
import org.nanoboot.octagon.entity.core.EntityAttributeType;

import java.util.List;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class NamedListSelect extends Select {
    public NamedListSelect(EntityAttribute e, List<EntityLabel> list) {
        this(e, list, null, null);
    }

    public NamedListSelect(EntityAttribute e, List<EntityLabel> list, String formId, String nameAndIdAttributePrefix) {
        if (nameAndIdAttributePrefix == null || nameAndIdAttributePrefix.isBlank()) {
            nameAndIdAttributePrefix = "";
        }
        if (e.getType() != EntityAttributeType.NAMED_LIST) {
            throw new OctagonException("EntityValueType.NAMED_LIST is required, but " + e.getType() + " is given.");
        }
        this.getAttributes().addAll(
                new Attribute("name", nameAndIdAttributePrefix + e.getName()),
                new Attribute("id", nameAndIdAttributePrefix + e.getName()),
                new Attribute("size", "1")
        );
        if (formId != null) {
            this.add(new Attribute("form", formId));
        }
        Option nullOption = new Option();
        nullOption.getAttributes().add(new Attribute("label", " "));
        nullOption.setInnerText("");
        this.add(nullOption);

        for (EntityLabel el : list) {
            Option option = new Option();
            option.getAttributes().add(new Attribute("label", el.getLabel()));
            option.getAttributes().add(new Attribute("value", el.getId()));
            option.setInnerText(el.getLabel());
            this.add(option);
        }
    }

    public void setDefault(String defaultValue) {
        for (Element e : this.getElements().getList()) {
            if (e.getElementName().equals("option")) {
                if (defaultValue == null) {
                    e.getAttributes().remove("selected");
                    return;
                } else {
                    String innerText = e.getNoElement().getPlainText();
                    String value = null;
                    for (Attribute a : e.getAttributes().getList()) {
                        System.err.println(a.getName() + " " + a.getValue());
                        if (a.getName().equals("value")) {
                            value = a.getValue();
                        }
                    }
                    if ((innerText != null && innerText.equals(defaultValue)) ||
                            (value != null && value.equals(defaultValue))) {
                        e.getAttributes().add("selected", "selected");
                        return;
                    }
                }
            }
        }
        //list does not contain the default value
        Option option = new Option();
        String label = defaultValue + " (Current value - deprecated)";
        option.getAttributes().add(new Attribute("label", label));
        option.getAttributes().add(new Attribute("value", defaultValue));
        option.getAttributes().add("selected", "selected");
        option.setInnerText(label);
        this.add(option);
    }
}
