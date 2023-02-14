<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.*" %>
<%@ page import="com.google.gson.Gson" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Restaurant</title>
    <%@ include file="/WEB-INF/jsp/template/head_includes.jsp" %>
    <%--Lista presa da RestaurantDTO--%>
    <%
        RestaurantDTO details = (RestaurantDTO) request.getAttribute("restaurantDTO");
        List<DishDTO> dishList = details.getDishes();
    %>
    <style>
        .tempOrder
        {
            color: #FFF4EA;
        }
        .orderButton:hover
        {
            color: #7C2714;
        }

        .rating {
            background-color: #FFF4EA;
            --dir: right;
            --fill: gold;
            --fillbg: rgba(100, 100, 100, 0.15);
            --heart: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M12 21.328l-1.453-1.313q-2.484-2.25-3.609-3.328t-2.508-2.672-1.898-2.883-0.516-2.648q0-2.297 1.57-3.891t3.914-1.594q2.719 0 4.5 2.109 1.781-2.109 4.5-2.109 2.344 0 3.914 1.594t1.57 3.891q0 1.828-1.219 3.797t-2.648 3.422-4.664 4.359z"/></svg>');
            --star: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M12 17.25l-6.188 3.75 1.641-7.031-5.438-4.734 7.172-0.609 2.813-6.609 2.813 6.609 7.172 0.609-5.438 4.734 1.641 7.031z"/></svg>');
            --stars: 5;
            --starsize: 3rem;
            --symbol: var(--star);
            --value: 1;
            --w: calc(var(--stars) * var(--starsize));
            --x: calc(100% * (var(--value) / var(--stars)));
            block-size: var(--starsize);
            inline-size: var(--w);
            position: relative;
            touch-action: manipulation;
            -webkit-appearance: none;
        }
        [dir="rtl"] .rating {
            --dir: left;
        }
        .rating::-moz-range-track {
            background: linear-gradient(to var(--dir), var(--fill) 0 var(--x), var(--fillbg) 0 var(--x));
            block-size: 100%;
            mask: repeat left center/var(--starsize) var(--symbol);
        }
        .rating::-webkit-slider-runnable-track {
            background: linear-gradient(to var(--dir), var(--fill) 0 var(--x), var(--fillbg) 0 var(--x));
            block-size: 100%;
            mask: repeat left center/var(--starsize) var(--symbol);
            -webkit-mask: repeat left center/var(--starsize) var(--symbol);
        }
        .rating::-moz-range-thumb {
            height: var(--starsize);
            opacity: 0;
            width: var(--starsize);
        }
        .rating::-webkit-slider-thumb {
            height: var(--starsize);
            opacity: 0;
            width: var(--starsize);
            -webkit-appearance: none;
        }
        .rating, .rating-label {
            display: block;
            font-family: ui-sans-serif, system-ui, sans-serif;
        }
        .rating-label {
            margin-block-end: 1rem;
        }

        /* NO JS */
        .rating--nojs::-moz-range-track {
            background: var(--fillbg);
        }
        .rating--nojs::-moz-range-progress {
            background: var(--fill);
            block-size: 100%;
            mask: repeat left center/var(--starsize) var(--star);
        }
        .rating--nojs::-webkit-slider-runnable-track {
            background: var(--fillbg);
        }
        .rating--nojs::-webkit-slider-thumb {
            background-color: var(--fill);
            box-shadow: calc(0rem - var(--w)) 0 0 var(--w) var(--fill);
            opacity: 1;
            width: 1px;
        }
        [dir="rtl"] .rating--nojs::-webkit-slider-thumb {
            box-shadow: var(--w) 0 0 var(--w) var(--fill);
        }
    </style>
    <script type="text/javascript" src="<c:url value="/js/jquery-3.6.3.min.js"/>"></script>
    <script type="text/javascript">

        let contaPiatti = 0;
        let totalDishId = '#totalDiv';
        let totalCostId = '#moneyDiv';
        let confirmDiv = '#orderDiv';


        function addItem(id)
        {
            let removeButtonId = "#remove" + id;
            let dishQuantityId = "#quantity" + id;
            let dishCostId = "#cost" + id;
            let currencyId = "#currency" + id;

            //    ---Gestione aggiunta prodotto---
            // {    GESTIONE FRONTEND

            // Rendo vidibile il div che contiene il resoconto dell'ordine
            // Se il div era invisibile, lo rendo visibile
            if (contaPiatti == 0)
            {
                $(confirmDiv).show();
            }

            // Rendo visibile il bottone meno
            $(removeButtonId).show();

            // Aumento la quantita' totale di piatti ordinati
            contaPiatti++;
            $(totalDishId).text(contaPiatti);

            // Aggiungo il prezzo del piatto al totale della spesa
            // Prendo total corrente
            let totalCost = parseFloat($(totalCostId).text());
            // Prendo prezzo piatto
            let price = parseFloat($(dishCostId).val());

            // Il nome del div della quantita e somma totale si potrebbero mettere globali anziche scriverli ogni volta
            totalCost = totalCost + price;
            // Aggiorno il testo del totale
            $(totalCostId).text(totalCost.toFixed(2) + " " + $(currencyId).text());

            // Rendo vidibile il div del piatto che indica la quantita' ordinata
            $(dishQuantityId).attr("type", "text");


            // Prendo il count corrente
            let count = parseInt($(dishQuantityId).val());
            count++;
            // Aggiungo dentro il Div della quantita
            $(dishQuantityId).val(count);
            // }
            //    --------------------------------
        }

        function removeItem(id)
        {
            //    ---Gestione rimozione prodotto---
            // {    GESTIONE FRONTEND

            // Rimuovo la quantita' di piatti ordinati
            contaPiatti--;
            $(totalDishId).text(contaPiatti);

            let dishCostId = "#cost" + id;
            let dishQuantityId = '#quantity' + id;
            let dishCurrencyId = '#currency' + id;
            let removeButtonId = '#remove' + id;

            // Rimuovo il totale della spesa
            let price = parseFloat($(dishCostId).val());

            let totalCost = parseFloat($(totalCostId).text());
            totalCost = totalCost - price;

            let currency = $(dishCurrencyId).text();
            $(totalCostId).text(totalCost.toFixed(2) + " " + currency);


            // Prendo il count corrente
            let count = parseInt($(dishQuantityId).val());
            count--;
            $(dishQuantityId).val(count);

            // Se è zero nascondo la quantita, altrimenti aggiorno il valore
            if (count == 0)
            {
                $(dishQuantityId).attr("type","hidden");
                // type -> hidden
                $(removeButtonId).hide();
            }

            // Rendo invisibile il div che contiene il resoconto dell'ordine
            if (contaPiatti == 0)
            {
                $("#orderDiv").hide();
                // Rendo invisibile tutti i bottoni meno
                $(".buttonMeno").hide();
            }
            // }
            //    ---------------------------------
        }

        function submitForm()
        {

            $('.inputboxQty').each(function (){
                $(this).prop("disabled", false);
            });

            document.getElementById("ordini").submit();
        }

        //
        // REVIEWS
        //

        let pageReviewActive = false;
        let bgReviewDivID = "#review";
        let bgAddReviewDivID = "#addreview";

        toSend = {
            action: "getComments",
            page: 0,
            restaurantId: "<%= details.getId() %>",
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

                $.post("<c:url value="/social"/>", toSend, function (result) {
                    json = JSON.parse(result);

                    if (json.itemCount == 0)
                    {
                        $("#boxRev").append('<div  class="relative mx-auto w-2/3 text-center">No Reviews</div>');
                    }
                    else
                    {
                        for(i=0; i<json.list.length; i++)
                        {
                            var commento = json.list[i];

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

                            $("#boxRev").append('<div class="my-8 border-b py-3"><div class="font-bold">'+commento.username+'</div><div class="px-5">'+commento.review+'</div><div class="flex flex-wrap float-right -m-6 mr-1">'+stelle+'</div></div>');

                            if(i == <%= Constants.MAX_COMMENTS - 1 %>)
                            {
                                $("#boxRev").append('<button class="ml-4 px-3 rounded-xl border-2 hover:bg-button" id="moreReviews" onclick="openReviews()">Load more</button>');
                            }
                        }
                    }

                    toSend.page++;

                }).fail(function (xhr, status, error){
                    alert(xhr+"\n"+status+"\n"+error);
                });
//          )
            }
            else
            {
                // QUI IL BOX E' GIA' ATTIVO
                $("body").css({"overflow": "hidden"});

                $("#moreReviews").remove();
                $.post("<c:url value="/social"/>", toSend, function (result) {
                    json = JSON.parse(result);

                    if (json.itemCount == 0)
                    {
                        $("#boxRev").append('<div  class="relative mx-auto w-2/3 text-center">No more Reviews</div>');
                    }
                    else{
                        for(i = 0; i<json.list.length; i++)
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

                            $("#boxRev").append('<div class="my-8 border-b py-3"><div class="font-bold">'+commento.username+'</div><div class="px-5">'+commento.review+'</div><div class="flex flex-wrap float-right -m-6 mr-1">'+stelle+'</div></div>');

                            if(i == <%= Constants.MAX_COMMENTS %>){
                                $("#boxRev").append('<button class="ml-4 px-3 rounded-xl border-2 hover:bg-button" id="moreReviews" onclick="openReviews()">Load more</button>');
                            }
                        }
                    }

                    toSend.page++;

                }).fail(function (xhr, status, error){
                    alert(xhr+"\n"+status+"\n"+error);
                });
            }
        }

        function closeReviews() {
            $(bgReviewDivID).hide();
            $("body").css({"overflow": ""});
            pageReviewActive = false;
            $('#boxRev').empty();
            toSend.page = 0;
        }

    </script>
</head>
<body>

<%--                Header con login o nomeutente--%>
<%@include file="template/header.jsp"%>

<script type="text/javascript">
    function openAddReview()
    {
        $(bgAddReviewDivID).show();
        $("body").css({"overflow": "hidden;"});
    }

    function sendReview(){
        let review = {
            action: "addReview",
            who: '<%= username %>',
            to: "<%= details.getId()%>",
            rate: $('#givenRating').val(),
            text: $('#givenReview').val(),
        }

        $.post("<c:url value="/social"/>", review, function (result) {

            $(bgAddReviewDivID).hide();
            $('#givenRating').val(2.5)
            $('#givenReview').val("")
        }).fail(function (xhr, status, error){
            alert(xhr+"\n"+status+"\n"+error);
        });
    }

    function closeAddReview()
    {
        $(bgAddReviewDivID).hide();
        $("body").css({"overflow": ""});
    }

</script>

<div class="-top-6 overflow-hidden h-48 z-50">
    <img class="blur-md w-full" src="https://ilfattoalimentare.it/wp-content/uploads/2017/06/junk-food-hamburger-patatine-fast-food-pizza-dolci-Fotolia_130389179_Subscription_Monthly_M.jpg" alt="imgFood">
</div>

<%--        DETTAGLI RISTORNTE    --%>
<div class="relative mx-auto w-2/3 rounded-lg bg-principale text-center py-3 -my-11 shadow-md px-5">
    <%--        Restaurant detailed infos--%>
    <div class="text-3xl font-bold"><%= details.getName() %></div>
    <div class=""><%= details.getAddress() %></div>
    <div class=""><%= details.getEmail() %></div>

        <div class="h-5"></div>
        <div class="flex flex-wrap">
<%--        Stelle --%>
            <button class="flex flex-wrap px-3 rounded-xl hover:bg-button" onclick="openReviews()">
<%
                Float rate = details.getRating();
                int rateInt = rate.intValue();
                int nStar=0;
                for (; nStar<rateInt; nStar++)
                {
%>
                    <img class="h-5" src="img/star.png" alt="star">
<%
                }

                rate = rate*10;
                nStar = rateInt;
                if (rate%10 >= 5)
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
<%
                }
%>
            </button>

<%          if (isLogged && !isRestaurant)
            {
%>
                <button class="px-3 rounded-xl hover:bg-button" onclick="openAddReview()">Add Review</button>
<%          }
%>
<%--        Soldi --%>
            <div class="ml-auto flex flex-wrap">
<%
                String money = details.getPriceRange();
                String[] splits = money.split("");
                for (nStar=0; nStar<splits.length; nStar++)
                {
%>
                    <img class="h-5" src="img/money.png" alt="star">
<%
                }
%>
            </div>
        </div>

    </div>

<%  if(!isRestaurant && isLogged)
    {
%>
<form class="flex justify-center rounded-xl mt-16 w-full" method="post" action="<c:url value="/checkout"/>">
            <% if(request.getAttribute("notUsual")!= null){ %>
                <button class="w-1/3 rounded-xl border-2 hover:bg-button" disabled type="submit">You can't use this feature now</button>
            <%}else{%>
                <button class="w-1/3 rounded-xl border-2 hover:bg-button" type="submit">Usual</button>
            <%}%>
            <input type="hidden" name="action" value="usual">
            <input type="hidden" name="restaurant" value="<%= details.getName() %>">
            <input type="hidden" name="rid" value="<%= details.getId() %>">
        </form>
<%  }
%>

<%--        LISTA DEI PIATTI    --%>
<div class="flex flex-wrap justify-center">
    <form id="ordini" method="post" action="<c:url value="/checkout"/>">
        <div class="relative mx-auto w-p70 flex flex-wrap my-5 justify-center">
<%--        List of available dishes--%>
<%          for(DishDTO i: dishList)
            {
                String price = i.getPrice() == 0.0 ? "-.-": i.getPrice().toString();
%>
                <div class="bg-principale rounded-xl w-80 text-center px-5 py-3 mr-5 mt-8 ml-3 relative shadow-md">

                    <div class="text-xl font-bold"><%= i.getName()%></div>
                    <input type="hidden" name="dishName" value="<%= i.getName() %>"/>
                    <div class="h-3"></div>
                    <div class="text-left"><%= i.getDescription()%></div>
                    <div class="h-10"></div>
                    <div class="absolute bottom-3 left-4 font-bold"><%= price %>
                        <a id="currency<%= i.getId() %>"><%= i.getCurrency() %></a>
                        <input type="hidden" name="dishCurrency" value="<%= i.getCurrency() %>"/>
                    </div>
                    <input id="cost<%= i.getId() %>" type="hidden" name="dishCost" value="<%= price %>"/>
                    <div>
                        <button type="button" style="display: none;" class="buttonMeno absolute bottom-3 right-20" id="remove<%=i.getId()%>" onclick="removeItem('<%= i.getId() %>')">
                            <img class="h-6" src="img/meno.png" alt="meno">
                        </button>
                        <input type="hidden" id="quantity<%=i.getId()%>" disabled name="dishQuantity" class="inputboxQty w-6 bg-transparent absolute bottom-3 left-64" value="0"/>
<%                      if (i.getPrice() != 0.0 && isLogged)
                        {
%>
                            <button type="button" class="absolute bottom-3 right-4" onclick="addItem('<%= i.getId() %>')">
                                <img class="h-6" src="img/plus.png" alt="plus">
                            </button>
<%                      }
%>
                    </div>
                </div>
<%          }
%>

            <div id="orderDiv" style="display: none;" class="fixed bottom-3 w-2/3 h-10 rounded-3xl bg-test_col text-center">
                <button class="w-full" type="button" onclick="submitForm()">
                    <input type="hidden" name="restaurant" value="<%= details.getName() %>">
                    <input type="hidden" name="rid" value="<%= details.getId() %>">
                    <input type="hidden" name="action" value="checkout">

                    <div class="flex flex-wrap px-5 py-2 justify-center">
                        <div class="tempOrder mr-2">Take: </div>
                        <div class="tempOrder mr-2" id="totalDiv"></div>
                        <div class="tempOrder mr-2">for</div>
                        <div class="tempOrder mr-2" id="moneyDiv">0</div>
                    </div>

                </button>
            </div>
        </div>
    </form>

    <div id="review" style="display: none;" class="z-50 fixed h-full w-full bg-black bg-opacity-20 -my-bgReview">
        <div class="relative mx-auto w-5/6 h-1/2 mt-20 rounded-lg bg-principale py-3 shadow-md px-5 overflow-auto">
<%--        List of Reviews--%>
            <div id="boxRev">

            </div>
            <button class="absolute top-3 right-3 px-1 rounded-xl" onclick="closeReviews()"><img class="h-7" src="img/x.png" alt="X"/>
            </button>
        </div>
    </div>

    <div id="addreview" style="display: none;" class="z-50 fixed h-full w-full bg-black bg-opacity-20 -my-bgReview">
        <div class="relative mx-auto w-5/6 h-1/2 mt-20 rounded-lg bg-principale py-3 shadow-md px-5 overflow-auto">
<%--        Add Reviews--%>
            <div id="boxAddRev" class="items-center mt-5">
                <form>
                    <input class="rating" id="givenRating" max="5" oninput="this.style.setProperty('--value', `${this.valueAsNumber}`)" step="0.5" style="--value:2.5" type="range" value="2.5">
                    <textarea class="text-left text-align-start py-5 px-5 mt-5 w-full h-32 overflow-hidden resize-none" type="text" id="givenReview"></textarea>
                    <button class="float-right mt-5 px-3 rounded-xl border-2 hover:bg-button" type="button" onclick="sendReview()">Send review</button>
                </form>
            </div>
            <button class="absolute top-3 right-3 px-1 rounded-xl" onclick="closeAddReview()"><img class="h-7" src="img/x.png" alt="X"/></button>
        </div>
    </div>
</div>
<%@include file="template/footer.jsp"%>
</body>
</html>

