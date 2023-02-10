<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.RestaurantDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.constants.Constants" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.UserDTO" %>
<%
    String username = null;
    String restaurantname = null;
    Boolean isLogged = false;
    Boolean isRestaurant = false;

    if (session != null)
    {
        if (session.getAttribute(Constants.AUTHENTICATION_FIELD) != null)
        {
            isLogged = true;
            if (session.getAttribute("username") != null)
            {
                username = (String) session.getAttribute("username");
            }
            else if (session.getAttribute("restaurantname") != null)
            {
                isRestaurant = true;
                restaurantname = (String) session.getAttribute("restaurantname");
            }
        }
    }

    %>
<%--    --%>

<%--    --%>
<script type="text/javascript" src="<c:url value="/js/jquery-3.6.3.min.js"/>"></script>
<script type="text/javascript">

    let searchVisible = false;
    let textSearchVisible = false;

    function toggleSearch(){
        if(!searchVisible){
            $('#search').show();
            searchVisible = true;
        } else {
            $('#search').hide();
            $("#boxSearch").empty();
            searchVisible = false;
        }
    }
    // Per far apparire la barra la prima volta
    function showSearchText()
    {
        if(!textSearchVisible) {
            $("#userSearchText").show();
            textSearchVisible = true;
        }
    }

    function hideSearchText()
    {
        if(textSearchVisible) {
            $("#userSearchText").hide();
            textSearchVisible = false;
        }
    }


    function setFollow(username) {
        let toSendH = {
                action: "setFollow",
                username: "<%= username %>",
                username2: username
        }

        $.post("<c:url value='/social'/>", toSendH, function (result){
            json = JSON.parse(result);
        })
            .fail(function (xhr, status, error){
                alert(xhr+"\n"+status+"\n"+error);
            });
    }

    function searchUser() {

        if(!searchVisible) {
            toggleSearch();
        }

        toSendH = {
            action: "searchUser",
            username: $('#userSearchText').val()
        }

        $.post("<c:url value='/social'/>", toSendH, function (result) {
            json = JSON.parse(result);
            if(!(json.id == '0')) {
                $("#boxSearch").append("<div><div>Username:" + json.username + "</div><div>First Name:" + json.firstName + "</div><div>Last Name:" + json.lastName + "</div></div>" +
                "<Button onclick='setFollow(\"" + json.username + "\")' > FOLLOW" + "</Button>" + " </div>");
            }else
            {
                $("#boxSearch").append("<div>No user found with this name</div>");
            }
        }).fail(function (xhr, status, error) {
            alert(xhr + "\n" + status + "\n" + error);
        });
    }
</script>

<%-- GESTIONE BOTTONI HEADER --%>
    <header class="bg-principale px-5 h-12 font-bold text-1xs shadow-md">

<%--        Il pulsante del logo ti manda a ricerca se non si e' ristorante --%>
<%--        Oppure alla pagina personale se si e' un ristorante             --%>

<%      if(!isRestaurant)
        {
%>
<%--            Utente registrato o non registrato--%>
            <a href="<c:url value="/ricerca"/>"><img class="h-12 float-left" src="img/logo_2.png" alt="logo"></a>
<%      }
        else
        {
%>
<%--        Ristorante --%>
            <form method="post" action="<c:url value="/personal"/>" class="h-12 float-left">
                <input type="hidden" name="actor" value="restaurant">
                <input type="hidden" name="action" value="personal">
                <button type="submit">
                    <a><img class="h-12 float-left" src="img/logo_2.png" alt="logo"></a>
                </button>
            </form>
<%      }
%>
<%--        Se sono loggato mostro pulsante di logout--%>

<%          if (isLogged)
            {
%>
<%--            Il pulsante di logout fa sloggare tutti--%>
                <button class="my-3 px-3 float-right rounded-lg hover:bg-button">
                    <a href="<c:url value="/logout"/>">Logout</a>
                </button>
<%--            Il pulsante per andare agli ordini agisce in maniera diversa--%>
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
                {
%>
<%--                Se non sono ristorante voglio andare alla pagina personale utente--%>
                <form method="post" action="<c:url value="/social"/>">
                <button class="my-3 px-3 float-right rounded-lg hover:bg-button" >
                    <input type="hidden" name="username" value= "<%=  (String) session.getAttribute("username")%>" >
                    <input type="hidden" name="page" value=0>
                    <input type="hidden" name="action" value="getFollowers">
                    <a>Following</a>
                </button>
                </form>

                <form method="post" action="<c:url value="/personal"/>">
                    <input type="hidden" name="actor" value="user">
                    <input type="hidden" name="action" value="personal">
                    <button class="my-3 px-3 float-right rounded-lg hover:bg-button">
                        <a><%= username %></a>
                    </button>
                </form>

                <%--    TODO RICRCA UTENTI --%>

                <button id="userSearchButton" class="flex my-3 px-3 float-right rounded-lg hover:bg-button" onmouseover="showSearchText()" onmouseleave="hideSearchText()">
                    <img class="h-5 mr-3" src="img/lente.png" onclick="searchUser()" alt="lente">
                    <div onsubmit="searchUser()">
                        <input style="display:none;" id="userSearchText" required class="rounded-xl px-3 shadow-md" type="text" placeholder="Search by username">
                        <button type="button" onclick="searchUser()"></button>
                    </div>
                </button>

                <div id="search" style="display:none;"  class=" z-50 fixed h-full w-full bg-black bg-opacity-20 ">
                    <div  class=" mx-auto w-5/6 h-1/2 mt-20 rounded-lg bg-principale py-3 shadow-md px-5 overflow-auto">
                        <div id="boxSearch">

                        </div>
                        <button class=" top-3 right-3 px-1 rounded-xl" onclick="toggleSearch()"><img class="h-7" src="img/x.png" alt="X"/></button>        </div>
                </div>

<%              }
                else
                {
%>
<%--                Se sono ristorante voglio andare alla pagina personale del ristorante--%>
                    <form method="post" action="<c:url value="/personal"/>">
                        <input type="hidden" name="actor" value="restaurant">
                        <input type="hidden" name="action" value="dishes">
                        <button class="my-3 px-3 float-right rounded-lg hover:bg-button">
                            <a><%= restaurantname %></a>
                        </button>
                    </form>
<%              }
%>
        <%-- Altrimenti metto link alla pagina di login--%>
<%          }
            else
            {
%>              <button class="my-3 px-3 float-right rounded-lg hover:bg-button">
                    <a href="<c:url value="/login"/>">Login</a>
                </button>
<%          }
%>

    </header>
