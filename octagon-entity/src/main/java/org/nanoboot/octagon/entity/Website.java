///////////////////////////////////////////////////////////////////////////////////////////////
// Octagon.
// Copyright (C) 2023-2023 the original author or authors.
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

package org.nanoboot.octagon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;


/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.0.0
 */
@Data
@AllArgsConstructor
@ToString
public class Website {

    private Integer number;
    private String url;
    private String archives;
    private String webArchiveSnapshot;
    private String language;
    private Boolean contentVerified;
    private Boolean archiveVerified;
    private Integer variantNumber;
    private String comment;
    private String recordingId;
    private String recordingComment;
    public String getArchiveUrl() {
        String archiveWebUrl = System.getProperty("octagon.archiveWebUrl");
        
        if(archiveWebUrl.isEmpty()) {
            return "";
        }
        if(archives != null && !archives.isEmpty()) {
            return archiveWebUrl + "/" + getUrl();
        } else {
            return "";
        }
    }
    public Boolean getDeadUrl() {
        return this.webArchiveSnapshot != null;
    }
}
