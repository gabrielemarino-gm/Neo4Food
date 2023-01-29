<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.DishDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.RestaurantDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.ListDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.OrderDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.constants.Constants" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unipi.lsmsd.neo4food.dao.mongo.RestaurantsMongoDAO" %>

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
    List<OrderDTO> ordini = new RestaurantsMongoDAO().getRestaurantDetails(details.getId(),false,false,true).getOrders();
    System.out.println("Ordini: " + ordini);
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

            <a class="ml-5" href="">View reviews</a>

        </div>
    </div>

    <div class="flex flex-wrap justify-center">
<%
            if (ordini != null)
            {
                for(OrderDTO order: ordini)
                {
%>
                    <div>
                        <%= order.getUser() %>
                        <%= order.getAddress() %>, <%= order.getZipcode() %>
                        <%= order.getTotal()%>
                        <div>
<%                          for(DishDTO dish: order.getDishes())
                            {
%>
                                <%= dish.getName() %>
                                <%= dish.getQuantity() %>
<%                          }
%>
                        </div>
                     </div>

                    <form method="post" action="<c:url value="/checkout"/>">
                        <input name="action" value="confirm">
                        <input name="oid" value="<%= order.getId() %>">
                        <button type="submit"> Confirm </button>
                    </form>
<%               }
            }
%>
    </div>
    <%@include file="template/footer.jsp"%>
</body>
</html>
