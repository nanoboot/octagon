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

import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.octagon.entity.core.EntityAttribute;
import org.nanoboot.octagon.entity.api.NamedListRepository;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class AbstractCreateUpdateFormContainer extends AbstractFormContainer {

    public AbstractCreateUpdateFormContainer(Entity entity, String action, String submitButtonLabel, NamedListRepository namedListRepository, boolean ignoreReadonly) {
        super(action, submitButtonLabel, entity, namedListRepository);
        form.setIgnoreReadonly(ignoreReadonly);

        int i = 0;
        String[] stringArray = entity.toStringArray();
        for (EntityAttribute evs : entity.getSchema()) {
            String value = stringArray[i];
            i++;
            form.addRow(evs, value);
        }
    }


}


//<div class="form_container">
//<form method="post" action="createThing">
//<%--        <div class="form_row">--%>
//<%--            <div class="form_col-25"><label for="id">ID:</label></div>--%>
//<%--            <div class="form_col-75"><input type="text" name="id" id="id"/></div>--%>
//<%--        </div>--%>
//
//<div class="form_row">
//<div class="form_col-25"><label for="path1">Path 1:</label></div>
//<div class="form_col-75"><input type="text" name="path1" id="path1"/></div>
//</div>
//<div class="form_row">
//<div class="form_col-25"><label for="path2">Path 2:</label></div>
//<div class="form_col-75"><input type="text" name="path2" id="path2"/></div>
//</div>
//<div class="form_row">
//<div class="form_col-25"><label for="path3">Path 3:</label></div>
//<div class="form_col-75"><input type="text" name="path3" id="path3"/></div>
//</div>
//<div class="form_row">
//<div class="form_col-25"><label for="path4">Path 4:</label></div>
//<div class="form_col-75"><input type="text" name="path4" id="path4"/></div>
//</div>
//
//<div class="form_row">
//<div class="form_col-25"><label for="name">Name:*</label></div>
//<div class="form_col-75"><input type="text" name="name" id="name"/></div>
//</div>
//<div class="form_row">
//<div class="form_col-25"><label for="alias">Alias:*</label></div>
//<div class="form_col-75"><input type="text" name="alias" id="alias"/></div>
//</div>
//<div class="form_row">
//<div class="form_col-25"><label for="since">Since:*</label></div>
//<div class="form_col-75"><input type="text" name="since" id="since"/></div>
//</div>
//<div class="form_row">
//<div class="form_col-25"><label for="priceKc">Price Kč:*</label></div>
//<div class="form_col-75"><input type="text" name="priceKc" id="priceKc"/></div>
//</div>
//<div class="form_row">
//<div class="form_col-25"><label for="note">Note:*</label></div>
//<div class="form_col-75"><input type="text" name="note" id="note"/></div>
//</div>
//<input type="hidden" id="__processForm" name="__processForm" value="yes">
//<input type="submit" value="Add"/>
//</form>
//</div>
