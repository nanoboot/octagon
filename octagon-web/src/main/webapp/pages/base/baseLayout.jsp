<!DOCTYPE html>
<%@ page import="org.nanoboot.octagon.web.misc.utils.Utils" %>
<%@ page import="org.nanoboot.octagon.entity.core.Entity" %>

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<head>
    <% String baseUrl = Utils.getBaseUrl(request); %>
    <meta charset="utf-8"/>
    <script>
        <%
        Entity e=(Entity) request.getAttribute("emptyEntity");
        if(e!=null){
        String entityId = e.getEntityName();
        %>
        function getEntityId() {
            return '<%=entityId%>';
        }
        <% } %>
    </script>


    <script type="text/javascript" src="javascript/octagon.js"></script>
    <title>
            <%
        String title = (String)request.getAttribute("__title");
        if(title == null) {
            title = "";
        } else {
            title = " - " + title;
        }
    %>

        Octagon - <tiles:insertAttribute name="title" ignore="true"/><%=title%>
    </title>
    <link rel="stylesheet" type="text/css" href="styles/styles.css">
</head>
<body onload="onPageLoad();">

<tiles:insertAttribute name="header"/>
<span style="color: green; font-weight: bold;">
    <%
        String message = (String)request.getAttribute("message");
        if(message == null) {
            message = "";
        }
    %>
    <%=message%>
</span>
<br>
<span style="color: red; font-weight: bold;">
    <%
        String error = (String)request.getAttribute("error");
        if(error == null) {
            error = "";
        }
    %>
    <%=error%>
</span>
<tiles:insertAttribute name="body"/>

<tiles:insertAttribute name = "footer" />
</body>
</html>