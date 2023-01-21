<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%
    String userid = null;
    String username = null;
    Boolean isLogged = false;

    if (session != null && session.getAttribute("userToken") != null){
        isLogged = true;
        username = (String) session.getAttribute("username");
    }
%>
<!DOCTYPE html>
<html lang="en">
    <head>
    </head>
    <body>
        <div>
<%--            Se ce un nomeutente lo stampo--%>
        <% if (isLogged) {%>
            <a><%= username %></a>
        <% } else { %>
<%--            Altrimenti metto link alla pagina di login--%>
            <a href="<c:url value="/loginUtente"/>">Login</a>
            <% } %>
        </div>
    </body>
</html>