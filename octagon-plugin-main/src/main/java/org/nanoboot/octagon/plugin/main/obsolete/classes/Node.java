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

import lombok.Data;
import lombok.ToString;
import org.json.JSONObject;

import java.util.UUID;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@ToString
@Data
public class Node {
    /**
     * Identification of the node.
     */
    private UUID id;
    /**
     * Access.
     */
    private Access access;
    /**
     * Name.
     */
    private String name;
    /**
     * Description.
     */
    private String description;
    /**
     * System id.
     */
    private String systemId;
    /**
     * Order number.
     */
    private int orderNumber;
    /**
     * Space UUID.
     */
    private UUID space;
    /**
     * Path.
     */
    private String path;
    /**
     * Content.
     */
    private String content;
    /**
     * Content type.
     */
    private String contentType;
    /**
     * Json object.
     */
    private JSONObject json;
    /**
     * Schema node UUID.
     */
    private UUID schema;
    /**
     * Type.
     */
    private NodeType type;
    /**
     * Additional json object.
     */
    private JSONObject additionalJson;
    /**
     * Template.
     */
    private boolean template;
    /**
     * Concept.
     */
    private boolean concept;
    /**
     * Referenced node UUID.
     */
    private UUID referencedNode;

}
