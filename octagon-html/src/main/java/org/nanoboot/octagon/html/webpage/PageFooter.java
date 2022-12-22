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

package org.nanoboot.octagon.html.webpage;

import org.nanoboot.powerframework.web.html.WebElement;
import org.nanoboot.powerframework.web.html.tags.A;
import org.nanoboot.powerframework.web.html.tags.Br;
import org.nanoboot.powerframework.web.html.tags.Div;
import org.nanoboot.powerframework.web.html.tags.H1;
import org.nanoboot.powerframework.xml.Attribute;
import org.nanoboot.powerframework.xml.ElementType;
import org.nanoboot.powerframework.xml.NoElement;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class PageFooter extends Div {

    public PageFooter() {
        this.add(new WebElement("hr", ElementType.NOT_PAIRED));
        String versionRow = "Octagon " + this.getClass().getPackage().getImplementationVersion();
        this.add(new NoElement(versionRow));
        this.add(new Br());
        this.add(new NoElement("Created by Robert Vokáč."));
    }

}
