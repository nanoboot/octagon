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

import java.util.Date;
import java.util.UUID;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@ToString
@Data
public class Access {
    /**
     * Identification of the access.
     */
    private UUID id;
    /**
     * User UUID.
     */
    private UUID user;
    /**
     * Group UUID.
     */
    private UUID group;
    /**
     * User access right.
     */
    private AccessRight userAccessRight;
    /**
     * Group access right.
     */
    private AccessRight groupAccessRight;
    /**
     * Other access right.
     */
    private AccessRight otherAccessRight;
    /**
     * System flag.
     */
    private boolean system;
    /**
     * Read-only flag.
     */
    private boolean readOnly;
    /**
     * Hidden flag.
     */
    private boolean hidden;
    /**
     * Deny anonymous flag.
     */
    private boolean denyAnonymous;
    /**
     * Undeletable flag.
     */
    private boolean undeletable;
    /**
     * Deleted flag.
     */
    private boolean deleted;
    /**
     * Type.
     */
    private String type;
    /**
     * Views.
     */
    private int views;
    /**
     * Icon.
     */
    private boolean[] icon;
    /**
     * Created timestamp.
     */
    private Date created;
    /**
     * Last modification timestamp.
     */
    private Date modified;
    /**
     * Last access timestamp.
     */
    private Date accessed;
    /**
     * Password for read.
     */
    private String readPassword;
    /**
     * Password for edit.
     */
    private String editPassword;

    /**
     * Password for encrypted.
     */
    private String encryptedPassword;

}
