<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unipi.lsmsd.neo4food.constants.Constants" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.*" %>

<html>
<head>

<%
        RestaurantDTO details = (RestaurantDTO) session.getAttribute(Constants.AUTHENTICATION_FIELD);
        List<OrderDTO> ordini = (List<OrderDTO>) request.getAttribute("orderList");
%>
    <title>Personal Page Restaurant</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <%@ include file="/WEB-INF/jsp/template/head_includes.jsp" %>
    <script type="text/javascript" src="<c:url value="/js/jquery-3.6.3.min.js"/>"></script>
    <script type="text/javascript">

        // Ricarico la pagina automaticamente ogni minuto cosi' da visualizzare nuovi ordini
        setTimeout(function() {location.reload();}, 60000);

        let pageReviewActive = false;
        let bgReviewDivID = "#review";
        let bgAddReviewDivID = "#addreview";

        let toSend = {
            action: "getComments",
            page: 0,
            restaurantId: "<%= details.getId() %>"
        };

        function openReviews()
        {
            // $("body").css({"overflow": "hidden"});
            // DENTRO QUESTO IF STO APRENDO IL BOTTONE PER LA PRIMA VOLTA O DOPO AVERLO CHIUSO
            if (!pageReviewActive)
            {
                $(bgReviewDivID).show();
                $("body").css({"overflow": "hidden"});
                pageReviewActive = true;

                $.post("<c:url value="/social"/>", toSend, function(result)
                {
                    received = JSON.parse(result);

                    if (received.itemCount == 0)
                    {
                        $("#boxRev").append('<div  class="relative mx-auto w-2/3 text-center">No Reviews</div>');
                    }
                    else
                    {
                        for(i=0; i<received.list.length; i++)
                        {
                            var commento = received.list[i];

                            // Aggiungo le stelle anziche' il numero
                            var stelle = "";
                            var nStar = 0;
                            var rateInt = parseInt(commento.rate);

                            for(; nStar<rateInt; nStar++)
                                stelle += '<img class="h-5" src="img/star.png" alt="star">';

                            var rate = commento.rate*10;
                            nStar = rateInt;

                            if (rate%10 >= 5)
                            {
                                stelle += '<img class="h-5" src="img/half_star.png" alt="star">';
                                nStar = rateInt+1;
                            }

                            for (; nStar<5; nStar++)
                                stelle += '<img class="h-5" src="img/empty_star.png" alt="star">';

                            $("#boxRev").append('' +
                                '<div class="my-8 border-b py-3">' +
                                    '<div class="font-bold">'+commento.username+'</div>' +
                                    '<div class="px-5">'+commento.review+'</div>' +
                                    '<div class="flex flex-wrap float-right -m-6 mr-1">'+stelle+'</div>' +
                                '</div>');

                            if(i == <%= Constants.MAX_COMMENTS - 1 %>)
                            {
                                $("#boxRev").append('<button class="ml-4 px-3 rounded-xl border-2 hover:bg-button" id="moreReviews" onclick="openReviews()">Load more</button>');
                            }
                        }
                    }

                    toSend.page++;

                }).fail(function (xhr, status, error)
                {
                    alert(xhr+"\n"+status+"\n"+error);
                });

            }
            else
            {
                // QUI IL BOX E' GIA' ATTIVO
                $("body").css({"overflow": "hidden"});

                $("#moreReviews").remove();
                $.post("<c:url value="/social"/>", toSend, function (result)
                {
                    json = JSON.parse(result);
                    
                    if (json.itemCount == 0)
                    {
                        $("#boxRev").append('<div  class="relative mx-auto w-2/3 text-center">No more Reviews</div>');
                    }
                    else
                    {
                        for(i=0; i<json.list.length; i++)
                        {
                            var commento = json.list[i];

                            // Aggiungo le stelle anziche' il numero
                            var stelle = "";
                            var nStar=0;
                            var rateInt = parseInt(commento.rate);
                            for(; nStar<rateInt; nStar++)
                                stelle += '<img class="h-5" src="img/star.png" alt="star">';

                            var rate = commento.rate*10;
                            nStar = rateInt;

                            if (rate%10 >= 5)
                            {
                                stelle += '<img class="h-5" src="img/half_star.png" alt="star">';
                                nStar = rateInt+1;
                            }

                            for (; nStar<5; nStar++)
                                stelle += '<img class="h-5" src="img/empty_star.png" alt="star">';

                            $("#boxRev").append('' +
                                '<div class="my-8 border-b py-3">' +
                                    '<div class="font-bold">'+commento.username+'</div>' +
                                    '<div class="px-5">'+commento.review+'</div>' +
                                    '<div class="flex flex-wrap float-right -m-6 mr-1">'+stelle+'</div>' +
                                '</div>');

                            if(i == <%= Constants.MAX_COMMENTS %>){
                                $("#boxRev").append('' +
                                    '<button class="ml-4 px-3 rounded-xl border-2 hover:bg-button" id="moreReviews" onclick="openReviews()">' +
                                        'Load more' +
                                    '</button>');
                            }
                        }
                    }

                    toSend.page++;

                }).fail(function (xhr, status, error)
                {
                    alert(xhr+"\n"+status+"\n"+error);
                });
            }
        }

        function closeReviews()
        {
            $(bgReviewDivID).hide();
            $("body").css({"overflow": ""});
            pageReviewActive = false;
            $('#boxRev').empty();
            toSend.page = 0;
        }

    </script>
</head>
<body>
    <%@include file="template/header.jsp"%>
    <div class="z-40 h-48 overflow-hidden w-full">
        <img class="w-full blur-md" src="https://ilfattoalimentare.it/wp-content/uploads/2017/06/junk-food-hamburger-patatine-fast-food-pizza-dolci-Fotolia_130389179_Subscription_Monthly_M.jpg" alt="imgFood" />
    </div>

    <div class="relative mx-auto w-2/3 rounded-lg bg-principale text-center py-3 -my-11 shadow-md px-5">
        <%--        Restaurant detailed infos--%>
        <div class="text-3xl font-bold"><%= details.getName() %></div>
<%          if(request.getAttribute("message") != null)
            {
%>
                <div><%= request.getAttribute("message").toString() %></div>
<%          }
%>
        <div class="h-5"></div>

        <div class="flex flex-wrap">
<%
            Float rate = details.getRating();
            int rateInt = rate.intValue();
            int nStar=0;
            for (; nStar<rateInt; nStar++)
            {
%>
                <img class="h-5" src="img/star.png" alt="star">
<%          }

            rate = rate*10;
            nStar = rateInt;
            if (rate%10 > 5)
            {
%>
                <img class="h-5" src="img/half_star.png" alt="star">
<%
                nStar = rateInt+1;
            }
            for (; nStar<5; nStar++)
            {
%>
                <img class="h-5" src="img/empty_star.png" alt="star">
<%          }
%>

            <button class="ml-5" onclick="openReviews()">View reviews</button>

        </div>
    </div>

    <div class="flex flex-wrap justify-center my-24">
<%
            // ORDINI DEL RISTORANTE
            if (ordini != null)
            {
                for(OrderDTO order: ordini)
                {
%>
                    <div class="bg-principale rounded-md px-4 py-4 w-5/6 mt-5 shadow-md">
                        <div>Customer Username:&nbsp;&nbsp;<%= order.getUser() %> </div>
                        <div>Customer Address:&nbsp;&nbsp;<%= order.getAddress() %>, <%= order.getZipcode() %> </div>
                        <div>Total:&nbsp;&nbsp;<%= order.getTotal()%>&nbsp;USD</div>
                        <div class="relative mx-auto w-5/6 mt-4 rounded-xl px-4 py-2 bg-white">
<%                          for(DishDTO dish: order.getDishes())
                            {
%>
                                <div class="font-bold flex">
                                    <input type="text" class="w-5/6 bg-white" disabled value="<%= dish.getQuantity() %>&nbsp; &nbsp; &nbsp; &nbsp;<%= dish.getName() %>">
                                    <div class="absolute right-10">
                                        <%=dish.getPrice()%>&nbsp;<%=dish.getCurrency()%>
                                    </div>
                                </div>
<%                          }
%>
                        </div>
                        <form method="post" action="<c:url value="/checkout"/>">
                            <input type="hidden" name="action" value="send">
<%--                            Id ordine per aggiornare ordine nella collection Orders--%>
                            <input type="hidden" name="oid" value="<%= order.getId() %>">
<%--                            Id ristorante per toglierlo dalla sua lista--%>
                            <input type="hidden" name="rid" value="<%= details.getId() %>">
                            <button class="float-right mt-5 border-2 rounded-xl px-3 hover:bg-button" type="submit"> Delivered </button>
                        </form>
                     </div>

<%               }
            }
%>
        <div id="review" style="display: none;" class="z-50 fixed h-full w-full bg-black bg-opacity-20 -my-bgReview">
            <div class="relative mx-auto w-5/6 h-1/2 mt-20 rounded-lg bg-principale py-3 shadow-md px-5 overflow-auto">
                <%--        List of Reviews--%>
                <div id="boxRev">

                </div>
                <button class="absolute top-3 right-3 px-1 rounded-xl" onclick="closeReviews()"><img class="h-7" src="img/x.png" alt="X"/>
                </button>
            </div>
        </div>
    </div>
    <%@include file="template/footer.jsp"%>
</body>
</html>
