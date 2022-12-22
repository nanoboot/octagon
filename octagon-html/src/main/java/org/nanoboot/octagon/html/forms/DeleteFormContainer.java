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

import org.nanoboot.powerframework.web.html.WebElement;
import org.nanoboot.powerframework.xml.Attribute;
import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.entity.core.Entity;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class DeleteFormContainer extends AbstractFormContainer {
    /**
     * @param entity entity must not be null, otherwise NullPointerException is thrown
     */
    public DeleteFormContainer(Entity entity) {
        super("delete?type=" + entity.getEntityName(), "Delete " + entity.getHumanEntityName() + " forever", entity, null);

        if (entity == null) {
            throw new OctagonException("entity is null");
        }
        form.setAddHiddenProcessFormInput(false);
        {
            WebElement label = new WebElement("label");
            label.setInnerText("Id");
            label.add(new Attribute("for", "id"));
            OctagonInput input = new OctagonInput("text", "id", "id", entity.getId().toString(), true);

            form.addRow(label, input);
        }
        {
            WebElement label = new WebElement("label");
            label.setInnerText("I really want to delete this " + entity.getEntityName() + ":");
            label.add(new Attribute("for", "__processForm"));
            OctagonInput input = new OctagonInput("checkbox", "__processForm", "__processForm", "yes");

            form.addRow(label, input);
        }
//        WebElement script = new WebElement("script");
//        script.add(new Attribute("type", "text/javascript"));
//        script.setInnerText("document.getElementById(\"__processForm\").checked = false;");
    }
}
//<div class="form_container">

//<form method="post" action="deleteThing">
//<div class="form_row">
//<div class="form_col-25"><label for="id">Id:</label></div>
//<div class="form_col-75"><input type="text" name="id" id="id" value="<%=thing.getId()%>" readonly/></div>
//</div>
//
//<div class="form_row">
//<div class="form_col-25"><label for="__processForm">I really want to delete this thing:</label></div>
//<div class="form_col-75"><input type="checkbox" checked name="__processForm" id="__processForm" value="yes" ></div>
//<script type=text/javascript>
//        document.getElementById("__processForm").checked = false;
//</script>
//</div>
//<input type="submit" value="Delete thing forever"/>
//</form>
//</div>
//
//<% } %>
