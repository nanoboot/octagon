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

package org.nanoboot.octagonorig.web.struts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class MessageSource {
    public static final String UNKNOWN = "?";
    private static Map<String, String> enMap = new HashMap<>();
    public static String EN = "en";

    private static final Logger LOG = LogManager.getLogger(MessageSource.class);

    static {
        enMap.put("core.item-type-categories.base", "Base");
        enMap.put("core.item-type-categories.sorting", "Sorting");
        enMap.put("core.item-type-categories.metadata", "Metadata");
        enMap.put("core.item-type-categories.resources", "Resources");
        enMap.put("core.item-type-categories.planning", "Planning");
        enMap.put("core.item-type-categories.other", "Other");

        enMap.put("core.item-types.base.node", "Node");
        enMap.put("core.item-types.base.tree", "Tree");
        enMap.put("core.item-types.base.schema", "Schema");
        enMap.put("core.item-types.base.space", "Space");

        enMap.put("core.item-types.sorting.category", "Category");
        enMap.put("core.item-types.sorting.favourite", "Favourite");
        enMap.put("core.item-types.sorting.flag", "Flag");
        enMap.put("core.item-types.sorting.tag", "Tag");
        enMap.put("core.item-types.sorting.search_result", "Search result");

        enMap.put("core.item-types.metadata.property", "Property");
        enMap.put("core.item-types.metadata.comment", "Comment");
        enMap.put("core.item-types.metadata.note", "Note");
        enMap.put("core.item-types.metadata.instance", "Instance");
        enMap.put("core.item-types.metadata.type", "Type");

        enMap.put("core.item-types.resources.file", "File");
        enMap.put("core.item-types.resources.filesystem", "Filesystem");
        enMap.put("core.item-types.resources.database", "Database");

        enMap.put("core.item-types.planning.reminder", "Reminder");
        enMap.put("core.item-types.planning.task", "Task");
        enMap.put("core.item-types.planning.task_effort", "Task effort");

        enMap.put("core.item-types.management.user", "User");
        enMap.put("core.item-types.management.group", "Group");

        enMap.put("core.item-types.other.uri", "Uri");
        enMap.put("core.item-types.other.test", "Test");
        enMap.put("core.item-types.other.question", "Question");
        enMap.put("core.item-types.other.icon", "Icon");
        enMap.put("core.item-types.other.dashboard", "Dashboard");
        enMap.put("core.item-types.other.dashboard_box", "Dashboard box");

        enMap.put("core.user.none", "None");
        enMap.put("core.user.user", "User");
        enMap.put("core.user.log-in", "Log in");
        enMap.put("core.user.log-out", "Log out");

    }

    public static String getMessage(String language, String key){
        if (language.equals("en") && enMap.containsKey(key)){
            return enMap.get(key);
        } else {
            LOG.warn(("Message was not found: "+key));
            return UNKNOWN;
        }
    }

    public static String getMessage(String s) {
        return getMessage(EN, s);
    }
}
