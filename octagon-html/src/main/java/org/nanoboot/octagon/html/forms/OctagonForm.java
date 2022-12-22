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
import org.nanoboot.octagon.html.misc.*;
import org.nanoboot.octagon.entity.api.NamedListRepository;
import org.nanoboot.powerframework.web.html.WebElement;
import org.nanoboot.powerframework.web.html.tags.Div;
import org.nanoboot.powerframework.web.html.tags.Form;
import org.nanoboot.powerframework.web.html.tags.Input;
import org.nanoboot.powerframework.xml.Attribute;
import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.octagon.entity.core.EntityAttribute;
import org.nanoboot.octagon.entity.core.EntityAttributeType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class OctagonForm extends Form {
    private final NamedListRepository namedListRepository;
    private final Entity entity;
    private boolean built = false;
    private final String submitButtonLabel;
    @Getter
    @Setter
    private boolean ignoreReadonly;

    @Getter
    @Setter
    private boolean addHiddenProcessFormInput = true;

    public OctagonForm(String action, String submitButtonLabel, Entity entity, NamedListRepository namedListRepository) {
        this.namedListRepository = namedListRepository;
        this.entity = entity;
        this.submitButtonLabel = submitButtonLabel;

        getElements().setAllowAnyElement(true);

        getAttributes().addAll(
                new Attribute("method", "get"),
                new Attribute("action", action)
        );
    }


    public String build() {
        if (!built) {
            if (addHiddenProcessFormInput) {
                add(new HiddenProcessFormInput());
            }
            add(new SubmitButton(submitButtonLabel));
            built = true;
        }
        if (entity != null) {
            add(new ClassNameFormInput(entity.getEntityName()));
        }
        return super.build();
    }

    public void addRow(EntityAttribute e, String value) {

        if (e == null) {
            throw new OctagonException("EntityValueSchema is null.");
        }
        if (value == null) {
            throw new OctagonException("value is null.");
        }
        boolean valueIsNotNull = value != null;
        boolean valueIsNotBlank = !value.isBlank();
        boolean useReadOnly = !ignoreReadonly;
        boolean readOnlyResult = e.getReadonly() == null ? false : e.getReadonly().booleanValue();
        boolean readonly = valueIsNotNull && valueIsNotBlank && useReadOnly && readOnlyResult;
        boolean deprecated = e.getDeprecated() != null && e.getDeprecated().equals(Boolean.TRUE);

//        System.err.println("##" + e.getName() + "# " + "value != null" + value != null);
//        System.err.println("##" + e.getName() + "# " + "!value.isBlank()" + valueIsNotBlank);
//        System.err.println("##" + e.getName() + "# " + "e.getReadonly() == null" + e.getReadonly() == null);
//        System.err.println("##" + e.getName() + "# " + "e.getReadonly() == null ? false : e.getReadonly().booleanValue()" + readOnlyResult);
        if (e.getName().equals("id") && (value == null || value.isBlank())) {
            //nothing to do
            return;
        }

        WebElement label = new WebElement("label");
        label.add(new Attribute("for", e.getName()));
        boolean isMandatory = e.getMandatory() != null && e.getMandatory();
        boolean isPreselectionProperty = e.getPreselectionProperty() != null && e.getPreselectionProperty();

        label.setInnerText(e.getHumanName() + ":" + (isMandatory || isPreselectionProperty ? new MandatoryAsterisk() : ""));
        if (e.getHelp() != null) {
            label.add(new Attribute("title", e.getHelp()));
        }
        WebElement formPart = getFormPart(e, value, readonly ? true : e.getName().equals("id") || (e.getPreselectionProperty() != null && e.getPreselectionProperty()) || e.isAutofill());

        if(deprecated) {
            label.getAttributes().setAllowAnyAttribute(true);
            label.add(new Attribute("style", DeprecatedLabel.DEPRECATED_STYLE));
        }
        this.addRow(label, formPart);
    }

    private WebElement getFormPart(EntityAttribute e, String value, boolean readonly) {
        if (e.getPreselectionProperty() != null && e.getPreselectionProperty().booleanValue()) {
            readonly = true;
        }
        if (e.getType().equals(EntityAttributeType.BOOLEAN)) {
            BooleanSelect booleanSelect = new BooleanSelect(e);
            String booleanSelectValue = value;
            if (booleanSelectValue != null && !booleanSelectValue.isBlank()) {
                booleanSelect.set(booleanSelectValue.equals("Yes") ? Boolean.TRUE : Boolean.FALSE);
            }
            if (readonly) {
                booleanSelect.add(new Attribute("readonly", "readonly"));
            }
            return booleanSelect;
        }
        if (e.getType().equals(EntityAttributeType.LIST)) {
            ListSelect listSelect = new ListSelect(e);
            listSelect.setDefault(value);
            if (readonly) {
                listSelect.add(new Attribute("readonly", "readonly"));
            }
            return listSelect;
        }

        if (e.getType().equals(EntityAttributeType.NAMED_LIST)) {
            String namedListArgPropertyValue = e.getNamedListArgPropertyName() == null ? null : entity.getPropertyValue(e.getNamedListArgPropertyName());
            String name = e.getNamedList();
            if (name.equals("?")) {
                throw new OctagonException("name is ?");
            }
            List<EntityLabel> generatedList = namedListRepository.generate(name, namedListArgPropertyValue);

            for (EntityLabel el : generatedList) {
                if (el.getLabel() == null) {

                    throw new OctagonException("el.getLabel() == null for " + name + ":" + namedListArgPropertyValue);
                }
            }
            if (generatedList != null) {
                NamedListSelect namedListSelect = new NamedListSelect(e, generatedList);

                namedListSelect.setDefault(value);
                System.err.println("generating list returned list with size " + generatedList.size() + " for " + e.getNamedList() + " " + namedListArgPropertyValue + "... " + "e.getNamedListArgPropertyName() = " + e.getNamedListArgPropertyName() + "### " + e.getName());
                if (readonly) {
                    namedListSelect.getAttributes().setAllowAnyAttribute(true);
                    namedListSelect.add(new Attribute("disabled", "disabled"));
                    Div div = new Div();
                    div.add(namedListSelect);

                    HiddenInput hiddenInput = new HiddenInput(e.getName(), e.getName(), value);
                    div.add(hiddenInput);
                    return div;
                }

                return namedListSelect;
            } else {
                System.err.println("generating list returned null for " + e.getNamedList() + " " + namedListArgPropertyValue);
            }
        }
        if (e.getType().equals(EntityAttributeType.TEXT_AREA)) {
            WebElement textArea = new WebElement("textarea");
            textArea.getAttributes().setAllowAnyAttribute(true);
            textArea.add(
                    new Attribute("name", e.getName()),
                    new Attribute("id", e.getName()),
                    new Attribute("cols", "40"),
                    new Attribute("rows", "10")
            );
            if (value != null && !value.isBlank()) {
                textArea.setInnerText(value);
            }
            if (readonly) {
                textArea.add(new Attribute("readonly", "readonly"));
            }
            return textArea;
        }


        //else
        Input input = new Input();
        input.getAttributes().setAllowAnyAttribute(true);
        input.add(
                new Attribute("type", "text"),
                new Attribute("name", e.getName()),
                new Attribute("id", e.getName()),
                new Attribute("size", "8")
        );
        if (value != null && !value.isBlank()) {
            input.add(new Attribute("value", value));
        }
        if (readonly) {
            input.add(new Attribute("readonly", "readonly"));
        }
        return input;

    }

    public void addRow(WebElement name, WebElement value) {
        if (built) {
            String msg = "Cannot add row. This form was already built (build() method was already called)";
            throw new OctagonException(msg);
        }
        Div formRow = new Div();
        formRow.add(new Attribute("class", "form_row"));

        Div formCol25 = new Div();
        formCol25.add(new Attribute("class", "form_col-25"));
        Div formCol75 = new Div();
        formCol75.add(new Attribute("class", "form_col-75"));

        formCol25.add(name);
        formCol75.add(value);
        formRow.add(formCol25, formCol75);
        add(formRow);

    }
}
