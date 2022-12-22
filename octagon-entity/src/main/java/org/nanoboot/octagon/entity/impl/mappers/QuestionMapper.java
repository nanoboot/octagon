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

package org.nanoboot.octagon.entity.impl.mappers;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public interface QuestionMapper {

    String hasChildren_PersonTask(String arg);

    String hasNotYetClosedChildren_PersonTask(String arg);

    String isParentClosed_PersonTask(String arg);
    //

    String hasChildren_Epic(String arg);

    String hasNotYetClosedChildren_Epic(String arg);

    String isParentClosed_Epic(String arg);


    String hasChildren_Story(String arg);

    String hasNotYetClosedChildren_Story(String arg);

    String isParentClosed_Story(String arg);


    String hasChildren_DevTask(String arg);

    String hasNotYetClosedChildren_DevTask(String arg);

    String isParentClosed_DevTask(String arg);


    String hasChildren_DevSubTask(String arg);

    String hasNotYetClosedChildren_DevSubTask(String arg);

    String isParentClosed_DevSubTask(String arg);


    String hasChildren_Proposal(String arg);

    String hasNotYetClosedChildren_Proposal(String arg);

    String isParentClosed_Proposal(String arg);


    String hasChildren_NewFeature(String arg);

    String hasNotYetClosedChildren_NewFeature(String arg);

    String isParentClosed_NewFeature(String arg);


    String hasChildren_Enhancement(String arg);

    String hasNotYetClosedChildren_Enhancement(String arg);

    String isParentClosed_Enhancement(String arg);


    String hasChildren_Bug(String arg);

    String hasNotYetClosedChildren_Bug(String arg);

    String isParentClosed_Bug(String arg);


    String hasChildren_Incident(String arg);

    String hasNotYetClosedChildren_Incident(String arg);

    String isParentClosed_Incident(String arg);


    String hasChildren_Problem(String arg);

    String hasNotYetClosedChildren_Problem(String arg);

    String isParentClosed_Problem(String arg);



}

