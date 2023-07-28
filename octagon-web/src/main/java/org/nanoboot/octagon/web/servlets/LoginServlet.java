package org.nanoboot.octagon.web.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.HttpRequestHandler;

@WebServlet(
        name = "LoginServlet",
        urlPatterns = "/LoginServlet"
)
public class LoginServlet extends HttpServlet implements HttpRequestHandler {

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        String returnedUser = request.getParameter("user");
        String returnedPassword = request.getParameter("password");

        String expectedUser = "";
        String expectedPassword = "";

        String octConfpath = System.getProperty("oct.datapath");
        if (octConfpath == null || octConfpath.isEmpty()) {
            String msg = "Octagon configuration is broken : " + "oct.datapath + oct.datapath=" + octConfpath;
            returnError(response, request, msg);
            return;
        }
        File octagonProperties = new File(octConfpath + "/octagon.properties");

        String authentication = "";
        boolean anonymous = false;
        try ( InputStream input = new FileInputStream(octagonProperties.getAbsolutePath())) {

            Properties properties = new Properties();
            properties.load(input);
            authentication = properties.getProperty("authentication");
            anonymous = authentication.equals("anonymous");

        } catch (IOException ex) {
            ex.printStackTrace();
            returnError(response, request, "Loading octagon.properties failed.");
            return;
        }

        if (!anonymous && authentication != null && !authentication.isBlank()) {
            String[] authenticationArray = authentication.split("/");
            if (authenticationArray.length != 2) {
                returnError(response, request, "Octagon configuration is broken (array.length != 2). Contact Octagon administrator.");
                return;
            }
            expectedUser = authenticationArray[0];
            expectedPassword = authenticationArray[1];
        }

        String sendRedirect = request.getParameter("sendRedirect");
        sendRedirect = (sendRedirect != null && sendRedirect.isBlank()) ? null : sendRedirect;

        if (anonymous || (expectedUser.equals(returnedUser) && expectedPassword.equals(returnedPassword))) {
            HttpSession oldSession = request.getSession(false);
            //invalidate old session
            if (oldSession != null) {
                oldSession.invalidate();
            }
            //new session
            HttpSession newSession = request.getSession(true);

            newSession.setMaxInactiveInterval(120 * 60);

            response.sendRedirect(sendRedirect == null ? "index.html" : sendRedirect);
        } else {
            returnError(response, request, "Either user or password is wrong.");
        }
    }

    private void returnError(HttpServletResponse response, HttpServletRequest request, String error) throws ServletException, IOException {
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/loginPage.html");

        PrintWriter out = response.getWriter();
        out.println("<font color=red>" + error + " </font>");
        rd.include(request, response);
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
