<%@ taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

This page should contain list of all users.
<table>
    <tr>
        <th>Id</th>
        <th>Nick</th>
        <th>Name</th>
        <th>Surname</th>
        <th>Active</th>
    </tr>
    <c:forEach var="user" items="${users}">
        <!-- create an html table row -->
        <tr>
            <!-- create cells of row -->
            <td>${user.id}</td>
            <td>${user.nick}</td>
            <td>${user.name}</td>
            <td>${user.surname}</td>
            <td>${user.active}</td>
            <!-- close row -->
        </tr>
        <!-- close the loop -->
    </c:forEach>
</table>
Robert Vokáč