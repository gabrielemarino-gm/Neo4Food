<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.RestaurantDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.DishDTO" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Restaurant</title>
    <%@ include file="/WEB-INF/jsp/template/head_includes.jsp" %>
    <style>
        .tempOrder
        {
            color: #FFF4EA;
        }
        .orderButton:hover
        {
            color: #7C2714;
        }
    </style>
    <script type="text/javascript" src="<c:url value="/js/jquery-3.6.3.min.js"/>"></script>
    <script type="text/javascript">

        var contaPiatti = 0;
        var pageReviewActive = 0;

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
            $(dishQuantityId).val(count)
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
            let price = parseFloat($(dishCostId).val())

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
        function submitForm(){

            $('.inputboxQty').each(function (){
                $(this).prop("disabled", false);
            });

            document.getElementById("ordini").submit();
        }

        function openReviews()
        {
            let bgReviewDivID = "#review";

            if (!pageReviewActive)
            {
                $(bgReviewDivID).show();
                $("body").css({"overflow": "hidden;"});
                pageReviewActive = 1;
            }
            else
            {
                $(bgReviewDivID).hide();
                $("body").css({"overflow": ""});
                pageReviewActive = 0;
            }
        }

        function openAddReview()
        {
            let bgReviewDivID = "#addreview";

            if (!pageReviewActive)
            {
                $(bgReviewDivID).show();
                $("body").css({"overflow": "hidden;"});
                pageReviewActive = 1;
            }
            else
            {
                $(bgReviewDivID).hide();
                $("body").css({"overflow": ""});
                pageReviewActive = 0;
            }
        }
    </script>
</head>
<body>
<%--Lista presa da RestaurantDTO--%>
<%

    RestaurantDTO details = (RestaurantDTO) request.getAttribute("restaurantDTO");
    List<DishDTO> dishList = details.getDishes();
%>
<%--                Header con login o nomeutente--%>
<%@include file="template/header.jsp"%>
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
<%
                }
%>
            </button>

<%          if (isLogged)
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

<%--        LISTA DEI PIATTI    --%>
<div class="flex flex-wrap justify-center">
    <form id="ordini" method="post" action="<c:url value="/checkout"/>">
        <div class="relative mx-auto w-p70 flex flex-wrap my-16 justify-center">
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
                <button type="button" onclick="submitForm()">
                    <input type="hidden" name="restaurant" value="<%= details.getName() %>">
                    <input type="hidden" name="rid" value="<%= details.getId() %>">
                    <input type="hidden" name="action" value="checkout">

                    <div class="flex flex-wrap px-5 py-2 justify-center">
                        <div class="tempOrder mr-2">Take: </div>
                        <div class="tempOrder mr-2" id="totalDiv"></div>
                        <div class="tempOrder mr-2">for</div>
                        <div class="tempOrder mr-2" id="moneyDiv">0</div>
                        <div>Checkout</div>
                    </div>

                </button>
            </div>
        </div>
    </form>

    <div id="review" style="display: none;" class="z-50 fixed h-full w-full bg-black bg-opacity-20 -my-bgReview">
        <div class="relative mx-auto w-5/6 h-1/2 mt-20 rounded-lg bg-principale py-3 shadow-md px-5">
<%--        List of Reviews--%>
            <button class="absolute bottom-3 right-3 px-3 rounded-xl border-2 hover:bg-button" onclick="openReviews()">Close</button>
        </div>
    </div>

    <div id="addreview" style="display: none;" class="z-50 fixed h-full w-full bg-black bg-opacity-20 -my-bgReview">
        <div class="relative mx-auto w-5/6 h-1/2 mt-20 rounded-lg bg-principale py-3 shadow-md px-5">
<%--        List of Reviews--%>
            <button class="absolute bottom-3 right-3 px-3 rounded-xl border-2 hover:bg-button" onclick="openAddReview()">Close</button>
        </div>
    </div>
</div>
<%@include file="template/footer.jsp"%>
</body>
</html>

