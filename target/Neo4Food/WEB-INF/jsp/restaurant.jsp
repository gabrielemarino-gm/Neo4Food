<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.ListDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.RestaurantDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.DishDTO" %>
<%@ page import="com.sun.org.apache.xpath.internal.operations.Bool" %>
<%@ page import="it.unipi.lsmsd.neo4food.constants.Constants" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Restaurant Page</title>
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

        var permanent = "";
        var contaPiatti = 0;

        function addItem(id, name, price, currency)
        {
            // {    GESTIONE RICHIESTA SERVER
            data = {
                action: "add",
                objectId: id,
                objectName: name,
                objectPrice: price,
                objectCurrency: currency,
                transferObj: permanent,
            };
            $.post("<c:url value="/checkout"/>", data, function (result){
                permanent = result;
                //    ---Gestione aggiunta prodotto---
                // {    GESTIONE FRONTEND

                // Rendo vidibile il div che contiene il resoconto dell'ordine
                if (contaPiatti == 0)
                {
                    $("#orderDiv").show();
                }

                // Rendo visibile il bottone meno
                let currentId = "#remove" + id;
                $(currentId).show();

                // Aggiungo la quantita' di piatti ordinati
                contaPiatti++;
                $("#totalDiv").text(contaPiatti);

                // Aggiungo il totale della spesa
                console.log($("#moneyDiv").text());
                let moneyStr = $("#moneyDiv").text();
                let money = parseFloat(moneyStr);
                money = money + parseFloat(price);
                $("#moneyDiv").text(money.toFixed(2) + " " + currency);

                // Rendo vidibile il div del piatto che indica la quantita ordinata
                currentId = "#countDiv" + id;
                $(currentId).show();

                // Prendo il count corrente
                let countStr = $(currentId).text();
                let count = parseInt(countStr);

                count++;
                console.log("Count + = " + count)
                // Aggiungo dentro il Div della quantita
                $(currentId).text(count);
                // }
                //    --------------------------------
            }).fail(function (xhr, status, error){
                alert(xhr+"\n"+status+"\n"+error);
            });
            // }
        }

        function removeItem(id, name, price, currency)
        {

            // {    GESTIONE RICHIESTA SERVER
            data = {
                action: "remove",
                objectId: id,
                transferObj: permanent,
            };
            $.post("<c:url value="/checkout"/>", data, function (result){
                permanent = result;
                //    ---Gestione rimozione prodotto---
                // {    GESTIONE FRONTEND

                // Aggiungo la quantita' di piatti ordinati
                contaPiatti--;
                $("#totalDiv").text(contaPiatti);

                // Aggiungo il totale della spesa
                let moneyStr = $("#moneyDiv").text();
                let money = parseFloat(moneyStr);
                money = money - parseFloat(price);
                console.log("Price "+parseFloat(price));
                console.log("Money "+money);
                $("#moneyDiv").text(money.toFixed(2) + " " + currency);

                // Rendo vidibile il div del piatto che indica la quantita ordinata
                currentId = "#countDiv" + id;

                // Prendo il count corrente
                let countStr = $(currentId).text();
                let count = parseInt(countStr);
                count--;
                console.log("Count - = " + count)
                // Se è zero nascondo la quantita, altrimenti aggiorno il valore
                if (count == 0) {
                    currentId = "#countDiv" + id;
                    $(currentId).text(count);
                    $(currentId).hide();
                    currentId = "#remove" + id;
                    $(currentId).hide();

                } else {
                    $(currentId).text(count);
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
            }).fail(function (xhr, status, error){
                alert(xhr+"\n"+status+"\n"+error);
            });
            // }
        }

        function set(){
            $('#final').val(permanent);
        }
    </script>
</head>
<body>
<%
    RestaurantDTO details = (RestaurantDTO) request.getAttribute("restaurantDTO");
    ListDTO<DishDTO> list = details.getDishes();
%>
<%--                Header con login o nomeutente--%>
<%@include file="template/header.jsp"%>
<div class="-top-6 overflow-hidden h-48 z-50">
    <img class="blur-md w-full" src="https://ilfattoalimentare.it/wp-content/uploads/2017/06/junk-food-hamburger-patatine-fast-food-pizza-dolci-Fotolia_130389179_Subscription_Monthly_M.jpg" alt="imgFood">
</div>

<div class="relative mx-auto w-2/3 rounded-lg bg-principale text-center py-3 -my-11 shadow-md px-5">
    <%--        Restaurant detailed infos--%>
    <div class="text-3xl font-bold"><%= details.getName() %></div>
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

        <div class="ml-auto flex flex-wrap">
            <%
                String money = details.getPricerange();
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

<div class="flex flex-wrap justify-center">
    <div class="relative mx-auto w-p70 flex flex-wrap my-16 justify-center">
        <%--            List of available dishes--%>
        <% for(DishDTO i: list.getList())
        {
            String price = i.getPrice() == 0.0 ? "-.-": i.getPrice().toString(); %>
        <div class="bg-principale rounded-xl w-80 text-center px-5 py-3 mr-5 mt-8 ml-3 relative shadow-md">

            <div class="text-xl font-bold"><%= i.getName()%></div>
            <div class="h-3"></div>
            <div class="text-left"><%= i.getDescription()%></div>
            <div class="h-10"></div>
            <div class="absolute bottom-3 left-4 font-bold"><%= price %> <%= i.getCurrency() %></div>
            <div>
                <button style="display: none;" class="buttonMeno absolute bottom-3 right-20" id="remove<%=i.getId()%>" onclick="removeItem('<%= i.getId() %>','<%= i.getName().replaceAll("'","\\\\'") %>','<%= i.getPrice() %>','<%= i.getCurrency() %>')">
                    <img class="h-6" src="img/meno.png" alt="meno">
                </button>
                <div class="absolute bottom-3 right-14" style="display: none;" id="countDiv<%=i.getId()%>">0</div>
                <%if (i.getPrice() != 0.0 && isLogged)
                {%>
                    <button class="absolute bottom-3 right-4" onclick="addItem('<%= i.getId() %>','<%= i.getName().replaceAll("'","\\\\'") %>','<%= i.getPrice() %>','<%= i.getCurrency() %>')">
                        <img class="h-6" src="img/plus.png" alt="plus">
                    </button>
                <%}%>
            </div>
        </div>
        <%  } %>
    </div>


    <div id="orderDiv" style="display: none;" class="fixed bottom-3 w-2/3 h-10 rounded-3xl bg-test_col">
        <form id="ordini" method="post" action="<c:url value="/checkout"/>">
        <button type="submit" onclick="set()">
            <input id="final" type="hidden" name="incremental" value="">
            <input type="hidden" name="restaurant" value="<%= details.getName() %>">
            <input type="hidden" name="action" value="checkout">

            <div class="flex flex-wrap px-5 py-2 justify-center">
                <div class="tempOrder mr-2">Take: </div>
                <div class="tempOrder mr-2" id="totalDiv"></div>
                <div class="tempOrder mr-2">for</div>
                <div class="tempOrder mr-2" id="moneyDiv">0</div>
                <div>Checkout</div>
            </div>

        </button>
        </form>
    </div>

</div>
<%@include file="template/footer.jsp"%>
</body>
</html>

