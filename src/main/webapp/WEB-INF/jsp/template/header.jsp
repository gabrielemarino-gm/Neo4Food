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

    function toggleSearch()
    {
//      Se non si vede, rendo visibile il box per la ricerca degli amici
        if(!searchVisible)
        {
            $('#search').show();
            searchVisible = true;
        } 
        else 
        {
            $('#search').hide();
            searchVisible = false;
        }
    }

    // Per far apparire la barra la prima volta
    function showSearchText()
    {
        if(!textSearchVisible)
        {
            $("#userSearchTextDiv").show();
            $("#userSearchButton").hide();
            textSearchVisible = true;
        }
    }

    function hideSearchText()
    {
        if(textSearchVisible) 
        {
            $("#userSearchTextDiv").hide();
            $("#userSearchButton").show();
            textSearchVisible = false;
        }
    }


    function setFollowHeader(username)
    {
        let toSend = {
                action: "setFollow",
                actor: "<%= username %>",
                target: username
        }

        $.post("<c:url value='/social'/>", toSend, function (result){
            toggleSearch();
        })
        .fail(function (xhr, status, error){
            alert(xhr+"\n"+status+"\n"+error);
        });
    }

    function searchUser()
    {
        if(!searchVisible)
            toggleSearch();

        let toSend = {
            action: "searchUser",
            username: $('#userSearchText').val()
        }

        $.post("<c:url value='/social'/>", toSend, function (result)
        {
            json = JSON.parse(result);

            if(!(json.itemCount  == 0)) {
                $("#boxSearch").empty();
                for (i = 0; i < json.itemCount; i++){
                    $("#boxSearch").append('' +
                        '<div class="flex border-t mt-3">' +
                        '<div>' +
                        '<div class="font-bold">' + json.list[i].username + '</div>' +
                        '<div class="flex px-5 text-xs">' +
                        '<div>' + json.list[i].firstName + '</div>' +
                        '<div>&nbsp;' + json.list[i].lastName + '</div>' +
                        '</div>' +
                        '</div>' +
                        '<button class="ml-auto h-7 mt-2 px-3 rounded-lg border-2 hover:bg-button" onclick="setFollowHeader(\'' + json.list[i].username + '\')"> Follow </button>' +
                        '</div>'
                    );
            }
            }
            else
            {
                $("#boxSearch").empty();
                $("#boxSearch").append('<div class="text-center">No user found with this name</div>');
            }

        }).fail(function (xhr, status, error)
        {
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
                <button class="my-3 px-3 float-right rounded-lg hover:bg-button" onclick="unlog()">
                    <a href="<c:url value="/logout"/>">Logout</a>
                </button>
<%--            Il pulsante per andare agli ordini agisce in maniera diversa--%>
<%--            Se si e' ristorante o cliente--%>
                <form method="post" action="<c:url value="/personal"/>">
                    <input type="hidden" name="aid" value="<%= isRestaurant ? ((RestaurantDTO) session.getAttribute(Constants.AUTHENTICATION_FIELD)).getId() : username%>">
                    <input type="hidden" name="actor" value="<%= isRestaurant ? "restaurant" : "user" %>">
                    <input type="hidden" name="action" value="orders">
                    <button class="my-3 px-3 float-right rounded-lg hover:bg-button">
                        <a>Order history</a>
                    </button>
                </form>

<%--            SE E' LOGGATO UN UTENTE--%>
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

                    <button id="userSearchButton" class="flex my-3 px-3 float-right rounded-lg hover:bg-button" onclick="showSearchText()">
                        <img class="h-5 mr-3" src="img/lente.png" alt="lente">
                    </button>

                    <div style="display:none;" id="userSearchTextDiv" onsubmit="searchUser()">
                        <button class="flex my-3 px-3 float-right rounded-lg hover:bg-button" type="submit"><img class="h-5 mr-3" src="img/lente.png" onclick="searchUser()" alt="lente"></button>
                        <input id="userSearchText" required class="float-right my-3 rounded-xl px-3 shadow-md" type="text" placeholder="Search by username">
                    </div>


    </header>
                    <div id="search" style="display:none;"  class="z-50 fixed h-full w-full bg-black bg-opacity-20 overflow-auto">
                        <div  class=" mx-auto w-5/6 h-1/2 mt-20 rounded-lg bg-principale py-3 shadow-md px-5 overflow-auto">

                            <button class="float-right px-1 rounded-xl" onclick="toggleSearch()"><img class="h-7" src="img/x.png" alt="X"/></button>
                            <div class="mt-10" id="boxSearch">

                            </div>

                        </div>
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

<%--                Per andare alla pagine delle statistiche per il ristorante --%>
                    <form method="post" action="<c:url value="/restaurant"/>">
                        <input type="hidden" name="actor" value="restaurant">
                        <input type="hidden" name="action" value="stats">
                        <button class="my-3 px-3 float-right rounded-lg hover:bg-button">
                            <a>Statistics</a>
                        </button>
                    </form>
    </header>
<%              }
%>
<%          }
//          Altrimenti metto link alla pagina di login
            else
            {
%>              <button class="my-3 px-3 float-right rounded-lg hover:bg-button">
                    <a href="<c:url value="/login"/>">Login</a>
                </button>
    </header>
<%          }
%>

