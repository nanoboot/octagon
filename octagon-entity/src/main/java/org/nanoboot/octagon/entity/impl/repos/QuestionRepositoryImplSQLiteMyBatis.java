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

package org.nanoboot.octagon.entity.impl.repos;

import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.entity.api.QuestionRepository;
import org.nanoboot.octagon.entity.impl.mappers.QuestionMapper;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@Data
@AllArgsConstructor
public class QuestionRepositoryImplSQLiteMyBatis implements QuestionRepository {
    private QuestionMapper questionMapper;

    public String ask(String questionName, String arg, String type) {
        questionName = questionName + "_" + type;
        switch (questionName) {
            case "hasChildren_PersonTask":
                return questionMapper.hasChildren_PersonTask(arg);
            case "hasNotYetClosedChildren_PersonTask":
                return questionMapper.hasNotYetClosedChildren_PersonTask(arg);
            case "isParentClosed_PersonTask":
                return questionMapper.isParentClosed_PersonTask(arg);




            case "hasChildren_Epic":
                return questionMapper.hasChildren_Epic(arg);
            case "hasNotYetClosedChildren_Epic":
                return questionMapper.hasNotYetClosedChildren_Epic(arg);
            case "isParentClosed_Epic":
                return questionMapper.isParentClosed_Epic(arg);


            case "hasChildren_Story":
                return questionMapper.hasChildren_Story(arg);
            case "hasNotYetClosedChildren_Story":
                return questionMapper.hasNotYetClosedChildren_Story(arg);
            case "isParentClosed_Story":
                return questionMapper.isParentClosed_Story(arg);


            case "hasChildren_DevTask":
                return questionMapper.hasChildren_DevTask(arg);
            case "hasNotYetClosedChildren_DevTask":
                return questionMapper.hasNotYetClosedChildren_DevTask(arg);
            case "isParentClosed_DevTask":
                return questionMapper.isParentClosed_DevTask(arg);


            case "hasChildren_DevSubTask":
                return questionMapper.hasChildren_DevSubTask(arg);
            case "hasNotYetClosedChildren_DevSubTask":
                return questionMapper.hasNotYetClosedChildren_DevSubTask(arg);
            case "isParentClosed_DevSubTask":
                return questionMapper.isParentClosed_DevSubTask(arg);


            case "hasChildren_Proposal":
                return questionMapper.hasChildren_Proposal(arg);
            case "hasNotYetClosedChildren_Proposal":
                return questionMapper.hasNotYetClosedChildren_Proposal(arg);
            case "isParentClosed_Proposal":
                return questionMapper.isParentClosed_Proposal(arg);

            case "hasChildren_NewFeature":
                return questionMapper.hasChildren_NewFeature(arg);
            case "hasNotYetClosedChildren_NewFeature":
                return questionMapper.hasNotYetClosedChildren_NewFeature(arg);
            case "isParentClosed_NewFeature":
                return questionMapper.isParentClosed_NewFeature(arg);

            case "hasChildren_Enhancement":
                return questionMapper.hasChildren_Enhancement(arg);
            case "hasNotYetClosedChildren_Enhancement":
                return questionMapper.hasNotYetClosedChildren_Enhancement(arg);
            case "isParentClosed_Enhancement":
                return questionMapper.isParentClosed_Enhancement(arg);

            case "hasChildren_Bug":
                return questionMapper.hasChildren_Bug(arg);
            case "hasNotYetClosedChildren_Bug":
                return questionMapper.hasNotYetClosedChildren_Bug(arg);
            case "isParentClosed_Bug":
                return questionMapper.isParentClosed_Bug(arg);

            case "hasChildren_Incident":
                return questionMapper.hasChildren_Incident(arg);
            case "hasNotYetClosedChildren_Incident":
                return questionMapper.hasNotYetClosedChildren_Incident(arg);
            case "isParentClosed_Incident":
                return questionMapper.isParentClosed_Incident(arg);

            case "hasChildren_Problem":
                return questionMapper.hasChildren_Problem(arg);
            case "hasNotYetClosedChildren_Problem":
                return questionMapper.hasNotYetClosedChildren_Problem(arg);
            case "isParentClosed_Problem":
                return questionMapper.isParentClosed_Problem(arg);


            default:
                throw new OctagonException("Question with name " + questionName + " is not implemented.");
        }
    }

}
