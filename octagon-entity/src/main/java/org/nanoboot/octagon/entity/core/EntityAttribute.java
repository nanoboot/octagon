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

package org.nanoboot.octagon.entity.core;

import org.nanoboot.powerframework.utils.NamingConvention;
import org.nanoboot.powerframework.utils.NamingConventionConvertor;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@Data
public class EntityAttribute {
    private final String name;
    private final EntityAttributeType type;
    private List<String> list;

    //todo
    private List<String> dataList;

    private String foreignKey;
    private String namedList;
    /**
     * Name of the property used as the argument for this named list query
     */
    private String namedListArgPropertyName;

    private String defaultValue;
    private Boolean mandatory;
    private Boolean readonly;

    private String help;
    private Boolean deprecated;
    private Boolean autofill;
    /**
     * Human name used, if the default computed human name is not correct.
     */
    @Getter(AccessLevel.PRIVATE)
    private String customHumanName;
    private String dbColumnName;

    /**
     * product, for example
     */
    private Boolean preselectionProperty;

    public EntityAttribute(String name) {
        this(name, EntityAttributeType.TEXT);
    }

    public EntityAttribute(String name, EntityAttributeType type) {
        this.name = name;
        this.type = type;
    }

    public EntityAttribute(String name, List<String> list) {
        this(name, EntityAttributeType.LIST);
        this.list = list;
    }

    public EntityAttribute(String name, String foreignKey, String namedList) {
        this(name, foreignKey, namedList, null);
    }

    public EntityAttribute(String name, String foreignKey, String namedList, String namedListArgPropertyName) {
        this(name, EntityAttributeType.NAMED_LIST);
        this.foreignKey = foreignKey;
        this.namedList = namedList;
        this.namedListArgPropertyName = namedListArgPropertyName;
    }

    public boolean isMandatory() {
        return mandatory != null && mandatory;
    }

    public boolean isAutofill() {
        return autofill != null && autofill;
    }
    public boolean isPreselectionProperty() {
        return preselectionProperty != null && preselectionProperty;
    }

    //All args constructor
    public EntityAttribute(String name, EntityAttributeType type, List<String> list, String foreignKey, String namedList, String namedListArgPropertyName, String defaultValue, Boolean mandatory, Boolean readonly, String help, String customHumanName, Boolean preselectionProperty, Boolean deprecated, Boolean autofill, String dbColumnName) {
        this.name = name;
        this.type = type;
        this.list = list;

        this.foreignKey = foreignKey;
        this.namedList = namedList;
        this.namedListArgPropertyName = namedListArgPropertyName;

        this.defaultValue = defaultValue;
        this.mandatory = mandatory;
        this.readonly = readonly;

        this.help = help;
        this.customHumanName = customHumanName;
        this.preselectionProperty = preselectionProperty;

        this.deprecated = deprecated;
        this.autofill = autofill;
        this.dbColumnName = dbColumnName;
    }

    public static EntityAttribute getIdEntityAttribute() {
        return new EntityAttribute("id", EntityAttributeType.ID);
    }

    public EntityAttribute withDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public EntityAttribute withMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    public EntityAttribute withReadonly(Boolean readonly) {
        this.readonly = readonly;
        return this;
    }

    public EntityAttribute withHelp(String help) {
        this.help = help;
        return this;
    }

    public EntityAttribute withCustomHumanName(String customHumanName) {
        this.customHumanName = customHumanName;
        return this;
    }

    public EntityAttribute withPreselectionProperty(Boolean preselectionProperty) {
        this.preselectionProperty = preselectionProperty;
        return this;
    }
    public EntityAttribute withDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
        return this;
    }
    public EntityAttribute withAutofill(Boolean autofill) {
        this.autofill = autofill;
        return this;
    }
    public EntityAttribute withDbColumnName(String dbColumnName) {
        this.dbColumnName = dbColumnName;return this;
    }
    public void setOther(
            String defaultValue,
            Boolean mandatory,
            Boolean readonly,

            String help,
            String customHumanName,
            Boolean preselectionProperty) {
        this.defaultValue = defaultValue;
        this.mandatory = mandatory;
        this.readonly = readonly;

        this.help = help;
        this.customHumanName = customHumanName;
        this.preselectionProperty = preselectionProperty;
    }

    public EntityAttribute copy() {
        EntityAttribute copy = new EntityAttribute(
                this.name,
                this.type,
                this.list == null ? null : new ArrayList<>(this.list),

                this.foreignKey,
                this.namedList,
                this.namedListArgPropertyName,

                this.defaultValue,
                copyBoolean(this.mandatory),
                copyBoolean(this.readonly),

                this.help,
                this.customHumanName,
                copyBoolean(this.preselectionProperty),
                copyBoolean(this.deprecated),
                copyBoolean(this.autofill),
                this.dbColumnName
        );

        return copy;
    }

    private static Boolean copyBoolean(Boolean b) {
        if (b == null) {
            return b;
        }
        return b.booleanValue() ? Boolean.TRUE : Boolean.FALSE;
    }

    public static List<EntityAttribute> copy(List<EntityAttribute> list) {
        List<EntityAttribute> listCopy = new ArrayList<>();
        for (EntityAttribute e : list) {
            listCopy.add(e.copy());
        }
        return listCopy;
    }

    public String getHumanName() {
        if (customHumanName != null) {
            return customHumanName;
        }
        String defaultHumanName = NamingConventionConvertor.convert(getName(), NamingConvention.JAVA, NamingConvention.HUMAN);

        defaultHumanName = Character.toUpperCase(defaultHumanName.charAt(0)) + defaultHumanName.substring(1);
        if (defaultHumanName.endsWith(" id")) {
            defaultHumanName = defaultHumanName.substring(0, defaultHumanName.length() - 3);
        }
        return defaultHumanName;
    }
}
