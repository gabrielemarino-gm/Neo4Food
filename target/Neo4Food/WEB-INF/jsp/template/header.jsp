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
<header>
<<<<<<< Updated upstream
    <img class="" src="img/food_delivery_2.png" alt="logo">
<%-- Se ce un nomeutente lo stampo--%>
    <% if (isLogged) {%>
    <button class="border-2 rounded-lg px-4"><a href="<c:url value="/personal"/>"><%= username %></a></button>
=======
    <img class="" src="../../../img/food_delivery_2.png" alt="logo">
<%-- Se ce un nomeutente lo stampo--%>
    <% if (isLogged) {%>
    <button class="border-2 rounded-lg px-4"><a><%= username %></a></button>
>>>>>>> Stashed changes
    <% } else { %>
<%-- Altrimenti metto link alla pagina di login--%>
    <button class=" hover:border-2 rounded-lg w-20"><a href="<c:url value="/login"/>">Login</a></button>
    <% } %>
</header>
