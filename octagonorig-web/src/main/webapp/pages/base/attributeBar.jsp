<%@ page import="org.goldrabbit.octagon.core.ItemTypeCategory" %>
<%@ page import="org.goldrabbit.octagon.core.ItemType" %>
<%@ page import="org.goldrabbit.octagon.web.MessageSource" %>
<div class="toolbar_left">
    <%
        for(ItemTypeCategory itemTypeCategory:ItemTypeCategory.values()) {
        for(ItemType itemType:itemTypeCategory.getItemTypes()) {
            String code = itemType.name().toLowerCase();
    %>
    <a href="<%=code%>"><img src="icons/item_types/<%=code%>.svg" class="icon" title="<%=MessageSource.getMessage("en", "core.item-types." + itemTypeCategory.name().toLowerCase() + "." + itemType.name().toLowerCase())%>" width="32" height="32"></a>

    <%

        }
    %>

    <img src="icons/item_types/icon_separator.svg" class="icon" title="Node" width="32" height="32">
    <%
        }
    %>

</div>
<div class="toolbar_separator_left"></div>


<div class="toolbar_right">

</div>