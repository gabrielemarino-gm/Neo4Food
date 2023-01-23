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
<header class="bg-principale px-5 h-12 font-bold text-1xs">
    <a href="/Neo4Food_war_exploded/ricerca"><img class="h-12 float-left" src="img/logo_2.png" alt="logo"></a>
    
<%-- Se ce un nomeutente lo stampo--%>
    <% if (isLogged) {%>
    <button class="my-3 px-3 float-right hover:border-2 rounded-lg"><a><%= username %></a></button>
    <% } else { %>
<%-- Altrimenti metto link alla pagina di login--%>
    <button class="my-3 px-3 float-right hover:border-2 rounded-lg"><a href="<c:url value="/login"/>">Login</a></button>
    <% } %>
</header>
