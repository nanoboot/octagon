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

package org.nanoboot.octagon.entity.api;

import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.entity.core.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@Deprecated
public interface QuestionRepository {
    String ask(String name, String arg, String type);

    default Map<String, String> ask(List<String> names, Entity e) {
        String arg = e.getId().toString();
        String type = e.getEntityName();
        Map<String, String> map = new HashMap<>();
        for (String question : names) {
            if (question.contains("(")) {
                String separator = "\\(";
                String[] array = question.split(separator);
                question = array[0];
                arg = e.getPropertyValue(array[1].replace(")", ""));
            }

            if(arg == null || arg.isBlank()) {
                continue;
            }
            String answer = ask(question, arg, type);
            if (answer == null) {
                throw new OctagonException("Answer is null for question " + question + " and arg \"" + arg + "\"");
            }
            System.out.println(question + " ? " + answer);
            map.put(question.split("_")[0], answer);
        }
        return map;
    }
}
