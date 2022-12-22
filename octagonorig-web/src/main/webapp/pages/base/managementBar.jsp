<%@ page import="Role" %>
<%@ page import="org.goldrabbit.octagon.web.MessageSource" %>
<%@ page import="org.goldrabbit.octagon.web.RequestInfo" %>
<div class="toolbar_left">
    <a href="home"><img src="icons/places/circle.svg" width="32" height="32" class="icon"/></a>
    <a href="secretPage">secretPage</a>
</div>
<div class="toolbar_separator_left"></div>


<div class="toolbar_separator_right"></div>

<div class="toolbar_right">

    <%
        RequestInfo requestInfo = RequestInfo.getFromRequest(request);
        String userNick;
        if (requestInfo.getUserType().isAnonymous()) {
            userNick = MessageSource.getMessage("core.user.none");
        } else {
            userNick = requestInfo.getUser().getNick();
        }


    %>
    <% if (requestInfo.getUserType().isConcrete() && requestInfo.getUser().hasRole(Role.ADMIN.name())) {
    %>
    <a href="admin"><img src="icons/places/admin.svg" width="32" height="32" class="icon"/></a>
    <% } %>

    <a href="settings"><img src="icons/places/settings.svg" width="32" height="32" class="icon"/></a>
    <span style="margin-bottom:10px;height:32px;"><b><%=MessageSource.getMessage("core.user.user")%>:</b> <%=userNick%></span>

    <%

        if (requestInfo.getUserType().isAnonymous()) {
    %>
    <a href="loginForm"><img src="icons/places/login.svg" width="32" height="32" class="icon"
                         title="<%=MessageSource.getMessage("core.user.log-in")%>"/></a>
    <%
    } else {
    %>
    <a href="logout"><img src="icons/places/logout.svg" width="32" height="32" class="icon"
    <a href="logout"><img src="icons/places/logout.svg" width="32" height="32" class="icon"
                          title="<%=MessageSource.getMessage("core.user.log-out")%>"/></a>
    <%
        }

    %>


</div>