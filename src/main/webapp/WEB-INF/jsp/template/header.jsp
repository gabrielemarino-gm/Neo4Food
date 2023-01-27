<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%
    String userid = null;
    String username = null;
    String restaurantname = null;
    Boolean isLogged = false;
    Boolean isRestaurant=false;

    if (session != null && session.getAttribute("userToken") != null){
        isLogged = true;
        if (session.getAttribute("username") != null){
            username = (String) session.getAttribute("username");
        }
        else if (session.getAttribute("name") != null){
            isRestaurant= true;
            restaurantname = (String) session.getAttribute("name");
        }
    }
%>
<header class="bg-principale px-5 h-12 font-bold text-1xs">
    <a href="<c:url value="/ricerca"/>"><img class="h-12 float-left" src="img/logo_2.png" alt="logo"></a>
    
<%-- Se ce un nomeutente lo stampo--%>
    <% if (isLogged && !isRestaurant) {%>
        <button class="my-3 px-3 float-right rounded-lg hover:bg-button">
            <a href="<c:url value="/personal"/>"><%= username %></a>
        </button>
        <button class="my-3 px-3 float-right rounded-lg hover:bg-button">
            <a href="<c:url value="/logout"/>">Logout</a>
        </button>
    <% } else if(isLogged && isRestaurant){ %>
<%-- Altrimenti metto link alla pagina di login--%>
        <button class="my-3 px-3 float-right rounded-lg hover:bg-button">
            <a href="<c:url value="/personalrestaurant"/>"><%= restaurantname %></a>
        </button>
    <% } else { %> <button class="my-3 px-3 float-right rounded-lg hover:bg-button">
    <a href="<c:url value="/login"/>">Login</a>
</button>
    <%}%>

</header>
