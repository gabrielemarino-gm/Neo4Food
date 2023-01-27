<%--
  Created by IntelliJ IDEA.
  User: franc
  Date: 27/01/2023
  Time: 00:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.DishDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.RestaurantDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.ListDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.OrderDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.constants.Constants" %>
<%@ page import="java.util.List" %>


<html>
<head>
    <title>Personal Restaurant</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link href="/Neo4Food_war_exploded/dist/output.css" rel="stylesheet" />
</head>
<body>

    <%
        RestaurantDTO details = (RestaurantDTO) session.getAttribute(Constants.AUTHENTICATION_FIELD);
        ListDTO<OrderDTO> list = details.getOrders();
        List<OrderDTO> ordini = list.getList();
    %>

    <%@include file="template/header.jsp"%>
    <div class="z-40 h-48 overflow-hidden w-full">
        <img class="w-full blur-md" src="https://ilfattoalimentare.it/wp-content/uploads/2017/06/junk-food-hamburger-patatine-fast-food-pizza-dolci-Fotolia_130389179_Subscription_Monthly_M.jpg" alt="imgFood" />
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

            <a class="ml-5" href="">View reviews</a>
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
        <%
            for(OrderDTO order: ordini)
            {
                String price = i.getPrice() == 0.0 ? "-.-": i.getPrice().toString(); %>
        <%  } %>
    </div>

</body>
</html>
