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

package org.nanoboot.octagon.html.messages;

import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.powerframework.web.html.WebElement;
import org.nanoboot.powerframework.xml.Attribute;
import org.nanoboot.powerframework.xml.ElementType;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public abstract sealed class Message extends WebElement permits ErrorMessage, InfoMessage {
    public Message(String cssColor, String text) {
        super("span", ElementType.PAIRED);
        this.getAttributes().setAllowAnyAttribute(true);
        this.add(new Attribute("style", "color: " + cssColor + "; font-weight: bold;"));
        this.getNoElement().setInnerText(text);
    }
    public abstract MessageType getMessageType();
    public static Message create(MessageType messageType, String text) {
        return switch (messageType) {
            case INFO -> new InfoMessage(text);
            case ERROR -> new ErrorMessage(text);
            default -> throw new OctagonException("Unsupported MessageType " + messageType);
        };
    }
}
