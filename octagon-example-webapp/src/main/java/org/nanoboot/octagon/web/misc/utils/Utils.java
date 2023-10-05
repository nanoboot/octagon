///////////////////////////////////////////////////////////////////////////////////////////////
// Octagon.
// Copyright (C) 2023-2023 the original author or authors.
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
package org.nanoboot.octagon.web.misc.utils;

import dev.mccue.guava.hash.Hashing;
import dev.mccue.guava.io.Files;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.jsp.SkipPageException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.asciidoctor.Asciidoctor;
import static org.asciidoctor.Asciidoctor.Factory.create;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.0.0
 */
public class Utils {

    public static String getBaseUrl(HttpServletRequest request) {
        return request.getServerName() + ':' + request.getServerPort() + request.getContextPath() + '/';
    }

    public static String formatToHtmlWithoutEmptyWord(Object o) {
        return formatToHtml(o, false);
    }

    public static String formatToHtml(Object o) {
        return formatToHtml(o, true);
    }

    public static String formatToHtml(Object o, boolean withEmptyWord) {
        if (o == null) {
            return withEmptyWord ? "[empty]" : "";
        }
        if (o instanceof String && (((String) o)).isEmpty()) {
            return withEmptyWord ? "[empty]" : "";
        }
        if (o instanceof Boolean) {
            Boolean b = (Boolean) o;
            return b.booleanValue() ? "<span style=\"color:#00CC00;font-weight:bold;\">YES</span>" : "<span style=\"color:red;font-weight:bold;\">NO</span>";
        }
        return o.toString();
    }

    public static boolean cannotUpdate(HttpServletRequest request) {
        return !canUpdate(request);
    }

    public static boolean canUpdate(HttpServletRequest request) {
        //if(true)return true;

        String allcanupdate = System.getProperty("octagon.allcanupdate");
        if (allcanupdate != null && allcanupdate.equals("true")) {
            return true;
        }
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        Object canUpdateAttribute = session.getAttribute("canUpdate");
        if (canUpdateAttribute == null) {
            return false;
        }
        return canUpdateAttribute.equals("true");

    }

    public static String convertToAsciidocIfNeeded(String text) {

        boolean isAsciiDoc = !text.lines().limit(1).filter(l -> l.equals("_adoc_")).toList().isEmpty();
        if (!isAsciiDoc) {
            return text;
        }
        text = "." + text.substring(7);

        Asciidoctor asciidoctor = create();

        String asciidocCompiled = asciidoctor
                .convert(text, new HashMap<String, Object>());

        return "\n\n\n" + asciidocCompiled + "\n\n\n";
    }

    public static boolean runProcess(String command, File workingDirectory) {
        class StreamGobbler implements Runnable {

            private InputStream inputStream;
            private Consumer<String> consumer;

            public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
                this.inputStream = inputStream;
                this.consumer = consumer;
            }

            @Override
            public void run() {
                new BufferedReader(new InputStreamReader(inputStream)).lines()
                        .forEach(consumer);
            }
        }
        try {
            boolean isWindows = System.getProperty("os.name")
                    .toLowerCase().startsWith("windows");
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(isWindows ? "cmd.exe" : "sh", isWindows ? "/c" : "-c", command);

            builder.directory(workingDirectory);
            Process process = builder.start();
            StreamGobbler streamGobbler = 
 
            new StreamGobbler(process.getInputStream(), System.out::println);
            
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            Future<?> future = executorService.submit(streamGobbler);


            int resultCode = process.waitFor();
            boolean result = resultCode == 0;
            System.out.println("resultCode=" + resultCode);
            executorService.shutdownNow();
            return result;
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public static void throwErrorInJsp(String error, jakarta.servlet.jsp.JspWriter out) throws SkipPageException, IOException {
        out.println("<span style=\"font-weight:bold;color:red;\">" + error + "</span>");
        throw new jakarta.servlet.jsp.SkipPageException();
    }

    public static String calculateSHA512Hash(File file) {
        try {
            return Files.hash(file, Hashing.sha512()).toString();
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
}