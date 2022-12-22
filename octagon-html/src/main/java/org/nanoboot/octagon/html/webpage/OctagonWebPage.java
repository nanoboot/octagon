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

import org.nanoboot.octagon.html.messages.Message;
import org.nanoboot.octagon.html.messages.MessageType;
import org.nanoboot.powerframework.web.html.WebElement;
import org.nanoboot.powerframework.web.html.WebPage;
import org.nanoboot.powerframework.web.html.attributes.Charset;
import org.nanoboot.powerframework.web.html.tags.*;
import org.nanoboot.powerframework.xml.Attribute;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class OctagonWebPage extends WebPage {
    protected final Head head;
    protected final Body body;
    private final Title title = new Title("Octagon");
    private PageHeader pageHeader;
    private Div messagesDiv;
    protected Div bodyContent;

    public OctagonWebPage() {
        Html html = getRootElement();

        head = getHead();
        body = getBody();

        html.add(head, body);
    }


    private Head getHead() {
        Head head = new Head();
        Meta meta = new Meta();
        meta.add(new Charset("utf-8"));
        head.add(meta);

        WebElement script = new WebElement("script");
        head.add(script);

        WebElement script2 = new WebElement("script");
        script2.getAttributes().setAllowAnyAttribute(true);
        script2.add(new Attribute("type", "text/javascript"));
        script2.add(new Attribute("src", "javascript/octagon.js"));
        head.add(script2);

        head.add(title);

        Link link = new Link();
        link.add(new Attribute("rel", "stylesheet"));
        link.add(new Attribute("type", "text/css"));
        link.add(new Attribute("href", "styles/styles.css"));
        head.add(link);

        Link linkBy = (Link) head.getByElementName("link");
        System.out.println("head element has element: " + linkBy.build());

        return head;
    }

    private Body getBody() {
        Body body = new Body();
        body.getAttributes().setAllowAnyAttribute(true);
        body.add(new Attribute("onload", "onPageLoad();"));
        this.pageHeader = new PageHeader();
        body.add(pageHeader);
        this.messagesDiv = new Div();
        body.add(messagesDiv);
        bodyContent = new Div();
        body.add(bodyContent);
        body.add(new PageFooter());
        return body;
    }

    public void addInfoMessage(String text) {
        addMessage(MessageType.INFO, text);
    }

    public void addErrorMessage(String text) {
        addMessage(MessageType.ERROR, text);
    }

    /**
     * Adds message (INFO type).
     * @param text
     */
    public void addMessage(String text) {
        messagesDiv.add(Message.create(MessageType.INFO, text));
    }
    public void addMessage(MessageType messageType, String text) {
        messagesDiv.add(Message.create(messageType, text));
    }

    public void setTitleInnerText(String text) {
        String finalTitle = "Octagon - " + text;
        this.title.setInnerText(finalTitle);
        pageHeader.setTitle(finalTitle);
    }
}
