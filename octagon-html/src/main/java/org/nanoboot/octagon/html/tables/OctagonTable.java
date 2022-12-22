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

import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.html.links.OctagonForeignKeyLink;
import org.nanoboot.octagon.entity.core.EntityAttributeType;
import org.nanoboot.octagon.entity.api.LabelRepository;
import org.nanoboot.powerframework.web.html.tags.A;
import org.nanoboot.powerframework.web.html.tags.Table;
import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.octagon.entity.core.EntityAttribute;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class OctagonTable extends Table {

    protected String transformIfForeignKey(
            String columnValue,
            EntityAttribute schema,
            LabelRepository labelRepository) {
        if (columnValue != null && !columnValue.isBlank() && schema.getForeignKey() != null && labelRepository != null) {
            String label = labelRepository.getLabel(schema.getForeignKey(), columnValue);
            String id = columnValue;
            String foreignKey = schema.getForeignKey();

            String foreignLink = getForeignLink(foreignKey, label, id).toString();

            return foreignLink;
        } else {
            return columnValue;
        }
    }

    protected A getForeignLink(String foreignKey, String label, String id) {
        return new OctagonForeignKeyLink(foreignKey, label, id);
    }

    protected String transformToLinkIIfNeeded(String cellContent, EntityAttribute schema) {
        if (schema.getType() == EntityAttributeType.LINK || (cellContent.startsWith("http") && !cellContent.contains(" "))) {
            return new A(cellContent, cellContent).setInnerText(cellContent).toString();
        } else {
            return cellContent;
        }
    }

    protected String transformToLinkIfObjectId(String cellContent, Entity e, EntityAttribute schema, LabelRepository labelRepository) {
        if (schema.getName().equals("objectId") && cellContent != null && !cellContent.isBlank()) {
            String type = e.getPropertyValue("type");
            String objectId = cellContent;
            String label = objectId;
            try {
                label = labelRepository.getLabel(Character.toLowerCase(type.charAt(0)) + type.substring(1), objectId);
            } catch (Exception ex) {
                ex.printStackTrace();
                label = label + " (label not found)";
                //nothing to do
            }

            return new A("read?className=" + type + "&id=" + objectId, label).setInnerText(label).toString();
        } else {
            return cellContent;
        }
    }
}
