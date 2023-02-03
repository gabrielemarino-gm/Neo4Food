<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.RestaurantDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.constants.Constants" %>
<%
    String username = null;
    String restaurantname = null;
    Boolean isLogged = false;
    Boolean isRestaurant = false;
    
    if (session != null)
        if(session.getAttribute(Constants.AUTHENTICATION_FIELD) != null){
        isLogged = true;
        if (session.getAttribute("username") != null){
            username = (String) session.getAttribute("username");
        }
        else if (session.getAttribute("restaurantname") != null){
            isRestaurant = true;
            restaurantname = (String) session.getAttribute("restaurantname");
        }
    }
    %>

    <script type="text/javascript" src="<c:url value="/js/jquery-3.6.3.min.js"/>"></script>
    <script>
        function showSearch()
        {
            $("#userSearchText").show();
        }
    </script>


    <header class="bg-principale px-5 h-12 font-bold text-1xs shadow-md">
<%--        Il pulsante del logo ti manda a ricerca solo se non si e' ristorante--%>
            <% if(!isRestaurant){ %>
            <a href="<c:url value="/ricerca"/>"><img class="h-12 float-left" src="img/logo_2.png" alt="logo"></a>
            <%}else{%>
            <a><img class="h-12 float-left" src="img/logo_2.png" alt="logo"></a>
            <%}%>
        <%-- Se sono loggato mostro pulsante di logout--%>
<%          if (isLogged)
            {%>
            <%--    Il pulsante di logout fa sloggare tutti--%>
                <button class="my-3 px-3 float-right rounded-lg hover:bg-button">
                    <a href="<c:url value="/logout"/>">Logout</a>
                </button>
<%--                Il pulsante per andare agli ordini agisce in maniera diversa--%>
<%--                Se si e' ristorante o cliente--%>
                <form method="post" action="<c:url value="/personal"/>">
                    <input type="hidden" name="aid" value="<%= isRestaurant ? ((RestaurantDTO) session.getAttribute(Constants.AUTHENTICATION_FIELD)).getId() : username%>">
                    <input type="hidden" name="actor" value="<%= isRestaurant ? "restaurant" : "user" %>">
                    <input type="hidden" name="action" value="orders">
                    <button class="my-3 px-3 float-right rounded-lg hover:bg-button">
                        <a>Order history</a>
                    </button>
                </form>
<%              if(!isRestaurant)
                {%>
<%--                Se non sono ristorante voglio andare alla pagina personale utente--%>
                    <form method="post" action="<c:url value="/personal"/>">
                        <input type="hidden" name="actor" value="user">
                        <input type="hidden" name="action" value="personal">
                        <button class="my-3 px-3 float-right rounded-lg hover:bg-button">
                            <a><%= username %></a>
                        </button>
                    </form>
                    <%--    Il pulsante per fare una ricerca --%>
                    <button id="userSearchButton" class="flex my-3 px-3 float-right rounded-lg hover:border-2" onclick="showSearch()">
                        <img class="h-5 mr-3" src="img/lente.png" alt="lente">
                        <input style="display:none;" id="userSearchText" required class="rounded-xl px-3 shadow-md" type="text" name="userSearch" placeholder="Search by username">
                    </button>
<%              }
                else
                {%>
<%--                Se sono ristorante voglio andare alla pagina personale del ristorante--%>
                    <form method="post" action="<c:url value="/personal"/>">
                        <input type="hidden" name="actor" value="restaurant">
                        <input type="hidden" name="action" value="personal">
                        <button class="my-3 px-3 float-right rounded-lg hover:bg-button">
                            <a><%= restaurantname %></a>
                        </button>
                    </form>
<%              }%>
        <%-- Altrimenti metto link alla pagina di login--%>
        <%  }
            else
            {
        %>      <button class="my-3 px-3 float-right rounded-lg hover:bg-button">
                    <a href="<c:url value="/login"/>">Login</a>
                </button>
        <%  }
        %>

        </header>
