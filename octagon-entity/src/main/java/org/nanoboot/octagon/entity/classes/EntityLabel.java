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

package org.nanoboot.octagon.entity.classes;

import lombok.Data;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@Data
public class EntityLabel implements Comparable<EntityLabel> {
    private static final String FOUR_DASHES = "::::";
    
    private String id;
    private String label;
    private Integer sortkey;
    public EntityLabel(String id, String label, int sortkey) {
        this.id = id;
        this.label = label;
        this.sortkey = sortkey;
    }

    public EntityLabel(String id, String label) {
        this.id = id;
        this.label = label;
        //setLabel(label);
    }
    
    public void setLabel(String label) {
        if(label.contains(FOUR_DASHES)) {
            String[] array = label.split(FOUR_DASHES);
            this.sortkey = Integer.valueOf(array[0]);
            this.label = array[1];
        } else {
            this.label = label;
        }
        
    }

    @Override
    public int compareTo(EntityLabel o) {
        EntityLabel el1 = this;
        EntityLabel el2 = o;
        Integer sortkey1 = el1.sortkey == null ? 0 : el1.sortkey;
        Integer sortkey2 = el2.sortkey == null ? 0 : el2.sortkey;
        String label1 = el1.label;
        String label2 = el2.label;
        int comparison1 =  sortkey1.compareTo(sortkey2);
        if(comparison1 != 0) {
            return comparison1;
        }
        return label1.compareTo(label2);
    }
}
