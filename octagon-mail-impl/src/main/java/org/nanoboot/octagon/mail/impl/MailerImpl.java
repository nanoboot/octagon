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

package org.nanoboot.octagon.mail.impl;

import org.nanoboot.octagon.core.utils.Mailer;
import org.nanoboot.powerframework.mail.MailBox;
import org.nanoboot.powerframework.mail.MailMessage;
import org.nanoboot.powerframework.mail.MailMessageType;
import org.nanoboot.powerframework.mail.SmtpUser;
import lombok.Data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@Data
public class MailerImpl implements Mailer {
    private MailBox mailBox;
    private SmtpUser smtpUser;
    private String to;
    private String env;

    @Override
    public String sendMessage(String subject, String body) {
        if(mailBox == null) {
            throw new RuntimeException("mailBox == null");
        }
        if(smtpUser == null) {
            throw new RuntimeException("smtp == null");
        }
        MailMessage msg = new MailMessage();
        msg.setSubject(env.equals("prod") ? subject : "THIS IS ONLY TEST::" + subject);
        msg.setText(body);
        msg.setTo(to);
        msg.setMailMessageType(MailMessageType.HTML);

        File tempFile = new File("./" + ((int)(Math.random()*1000000)) + ".html");
        makeTextFile(tempFile, body);
        msg.setAttachment(tempFile.getAbsolutePath());
        System.out.println("tempFile=" + tempFile.getAbsolutePath());

        try {
            mailBox.send(msg, smtpUser);
            return null;
        } catch (Exception e) {
            return e == null ? "NullPointerException" : e.getMessage();
        } finally {
            tempFile.delete();
        }
    }
    //todo : move to Power Framework
    private static void makeTextFile(File f, String content) {
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Creating file " + f.getAbsolutePath() + " failed.");
        }
        try {
            FileWriter myWriter = new FileWriter(f.getAbsolutePath());
            myWriter.write(content);
            myWriter.close();
            //System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            throw new RuntimeException("Writing to file " + f.getAbsolutePath()
                    + "failed" + e.getMessage());
        }
    }
}
