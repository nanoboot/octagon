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

package org.nanoboot.octagon.plugin.main.obsolete.classes;

import lombok.Getter;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public enum NodeType {

    /**
     * Json node intended to be validated.
     * An error is thrown in case, the validation fails.
     */
    VALIDATED_JSON(1),

    /**
     * Json node intended to be validated.
     * A warning is thrown in case, the validation fails.
     */
    CHECKED_JSON(2),

    /**
     * Json node intended not to be validated.
     * No schema is defined.
     * There is no validation done on this node.
     */

    FREE_FORM_JSON(3),

    /**
     * Json member must be null.
     */
    NO_JSON(10);

    /**
     * Value representation of the node type.
     */
    @Getter
    private final int value;

    /**
     * Constructor.
     * @param valueIn number representation of the node type
     */
    NodeType(final int valueIn) {
        this.value = valueIn;
    }

    /**
     * Returns node type or null based on the given value.
     * @param value value of the node type to find
     * @return node type if exists for the given value, otherwise null.
     */
    static NodeType ofValue(final int value) {
        for (NodeType dt : NodeType.values()) {
            if (dt.getValue() == value) {
                return dt;
            }
        }
        return null;
    }
}
