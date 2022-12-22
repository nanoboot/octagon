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

package org.nanoboot.octagon.struts.actions.entity;

import org.nanoboot.octagon.html.fragments.AbstractEntityFragment;
import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.octagon.entity.core.EntityAttribute;
import org.nanoboot.powerframework.web.html.WebElement;
import org.nanoboot.powerframework.xml.Attribute;
import org.nanoboot.octagon.html.forms.NamedListSelect;
import org.nanoboot.octagon.html.forms.OctagonForm;
import org.nanoboot.octagon.html.fragments.CreateEntityFragment;
import org.nanoboot.octagon.html.links.ViewLink;
import org.nanoboot.octagon.html.misc.ClassNameFormInput;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class CreateEntityAction extends EntityAction {

    public String execute() {
        printWarningTextIfNotProd();

        throwExceptionIfRepositoryRegistryIsNull();

        if (isGoingToProcessForm()) {
            Entity entity = returnNewEmptyInstance();
            entity.setId(UUID.randomUUID());
            entity.loadFromMap(getParameterMap(request));

            findEntityRepo().create(entity);
            this.persistActionLog(null, entity, null);

            ViewLink link = (ViewLink) new ViewLink(entity).setInnerText(entity.getName());
            this.setMessage("New row created: " + link.toString() + " " + entity.toString());

            this.request.setAttribute("fragment", new AbstractEntityFragment(entity.getEntityClass(), getActionType(), entity).build());
            setTitle(entity.getName());
            return "success";
        } else {
            String cloneFromId = request.getParameter("cloneFromId");
            System.err.println("cloneFromId=" + cloneFromId);
            Entity cloneFrom = null;
            if (cloneFromId != null) {
                cloneFrom = findEntityRepo().read(cloneFromId);
                cloneFrom.setId(null);
                cloneFrom.setAutofillPropertiesToNull();
                setMessage("Cloning from existing object. Warning: children entities are not cloned. Files and directories are not cloned.");
            }
            String preselectionProperty = null;
            EntityAttribute preselectionPropertyEvs = null;

            Entity entity = cloneFrom == null ? returnNewEmptyInstance() : cloneFrom;
            String authorizationResult = authorizeActionTypeForEntity(entity);
            if (authorizationResult != null) {
                setError(authorizationResult);
                setTitle(null);
                return "success";
            }
            for (EntityAttribute evs : entity.getSchema()) {
                if (evs.getPreselectionProperty() != null && evs.getPreselectionProperty()) {
                    preselectionProperty = evs.getName();
                    preselectionPropertyEvs = evs;
                    break;
                }
            }
            String preselectionValue = null;
            if (cloneFrom != null) {
                String preselectionPropertyName = cloneFrom.getPreselectionPropertyName();
                if (preselectionPropertyName != null) {
                    preselectionValue = cloneFrom.getPropertyValue(cloneFrom.getPreselectionPropertyName());
                }

            }
            if (preselectionProperty != null && preselectionValue == null && cloneFrom == null) {
                preselectionValue = this.request.getParameter(preselectionProperty);
                if (preselectionValue == null) {
                    OctagonForm preselectionPropertyForm = new OctagonForm("create", "Select", entity, namedListRepository);
                    preselectionPropertyForm.setAddHiddenProcessFormInput(false);
                    for (Attribute a : preselectionPropertyForm.getAttributes().getList()) {
                        if (a.getName().equals("method")) {
                            a.setValue("get");
                        }
                    }
                    preselectionPropertyForm.add(new ClassNameFormInput(entity.getEntityName()));
                    NamedListSelect namedListSelect = new NamedListSelect(preselectionPropertyEvs, namedListRepository.generate(preselectionProperty, null));
                    WebElement label = new WebElement("label");

                    label.add(new Attribute("for", entity.getName()));
                    label.setInnerText(preselectionProperty + ":");
                    preselectionPropertyForm.addRow(label, namedListSelect);
                    this.request.setAttribute("fragment", "Parameter " + preselectionProperty + " is required. <br /> Please, visit the detail of the related object and continue there." + preselectionPropertyForm.build());

                    setTitle(null);
                    return "success";
                }
            }

            Entity entityToFragment = entity;
            entityToFragment.setPreselectionProperty(preselectionValue);

            Map<String, String> tempMap = getParameterMap(request);
            Map<String, String> map = new HashMap<>();
            for (String key : tempMap.keySet()) {
                if (key != null) {
                    String value = tempMap.get(key);
                    if (value != null) {
                        map.put(key, value);
                    }
                }
            }
            //set default values, if they are empty.
            for (EntityAttribute evs : entityToFragment.getSchema()) {
                boolean defaultValueSet = evs.getDefaultValue() != null;
                String key = evs.getName();
                String value = map.containsKey(key) ? map.get(key) : null;

                if (defaultValueSet && (value == null || value.isEmpty())) {
                    map.put(evs.getName(), evs.getDefaultValue());
                }
            }
            if (cloneFrom == null) {
                entityToFragment.loadFromMap(map);
            }

            CreateEntityFragment fragment = new CreateEntityFragment(entityToFragment, namedListRepository);
            this.request.setAttribute("fragment", fragment.build());
            setTitle(entityToFragment.getName());
            return "success";
        }

    }
}
