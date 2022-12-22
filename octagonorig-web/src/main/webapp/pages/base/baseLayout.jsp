<!DOCTYPE html>
<%@ page import="org.nanoboot.octagonorig.web.struts.utils.Utils" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<head>
    <% String baseUrl = Utils.getBaseUrl(request); %>
    <meta charset="utf-8"/>
    <title>
        Octagon<tiles:insertAttribute name="title" ignore="true"/>
    </title>
    <link rel="stylesheet" type="text/css" href="styles/styles.css">
</head>


<body>
<tiles:insertAttribute name="header"/>

<%--<tiles:insertAttribute name="managementBar"/>--%>

<%--<tiles:insertAttribute name="attributeBar"/>--%>
<%--<tiles:insertAttribute name = "editBar" />--%>
<%--<tiles:insertAttribute name = "actionBar" />--%>
<%--<tiles:insertAttribute name = "locationBar" />--%>

<%--<tiles:insertAttribute name = "infoBar" />--%>

<%--<tiles:insertAttribute name = "treeBar" />--%>

<tiles:insertAttribute name="body"/>


<%--<tiles:insertAttribute name = "footer" />--%>
</body>
</html>