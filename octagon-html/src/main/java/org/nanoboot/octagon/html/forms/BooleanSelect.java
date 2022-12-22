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

import org.nanoboot.powerframework.web.html.tags.Option;
import org.nanoboot.powerframework.web.html.tags.Select;
import org.nanoboot.powerframework.xml.Attribute;
import org.nanoboot.octagon.entity.core.EntityAttribute;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class BooleanSelect extends Select {
    private final Option nullOption;
    private final Option yesOption;
    private final Option noOption;

    public BooleanSelect(EntityAttribute e) {
        this(e, null, null);
    }
    public BooleanSelect(EntityAttribute e, String formId, String nameAndIdAttributePrefix) {
        if(nameAndIdAttributePrefix == null || nameAndIdAttributePrefix.isBlank()) {
            nameAndIdAttributePrefix = "";
        }
        this.getAttributes().addAll(
                new Attribute("name", nameAndIdAttributePrefix + e.getName()),
                new Attribute("id", nameAndIdAttributePrefix + e.getName()),
                new Attribute("size", "1")
        );
        if (formId != null) {
            this.add(new Attribute("form", formId));
        }
        this.nullOption = new Option();
        nullOption.getAttributes().add(new Attribute("label", " "));

        this.yesOption = new Option();
        yesOption.getAttributes().add(new Attribute("value", "1"));
        yesOption.setInnerText("Yes");
        this.noOption = new Option();
        noOption.getAttributes().add(new Attribute("value", "0"));
        noOption.setInnerText("No");
        this.add(nullOption, yesOption, noOption);

    }

    public void set(Boolean b) {
        Attribute selected = new Attribute("selected");
        if (b == null) {
            nullOption.add(selected);
            return;
        }
        if (b.booleanValue()) {
            yesOption.add(selected);
        }
        if (!b.booleanValue()) {
            noOption.add(selected);
        }
    }
}
