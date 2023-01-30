<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.DishDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.RestaurantDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.OrderDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.constants.Constants" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unipi.lsmsd.neo4food.dao.mongo.RestaurantMongoDAO" %>

<html>
<head>
    <title>Personal Restaurant</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <%@ include file="/WEB-INF/jsp/template/head_includes.jsp" %>
</head>
<body>

<%
    RestaurantDTO details = (RestaurantDTO) session.getAttribute(Constants.AUTHENTICATION_FIELD);
    List<OrderDTO> ordini = new RestaurantMongoDAO().getRestaurantDetails(details.getId(),false,false,true).getOrders();
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
<%--            TODO SERVLET E PAGINA PER VEDERE LE REVIEWS--%>
<%--            TODO SERVLET E PAGINA PER VEDERE LE REVIEWS--%>
<%--            TODO SERVLET E PAGINA PER VEDERE LE REVIEWS--%>
            <form method="post" action="">
            <button class="ml-5">View reviews</button>
            </form>

        </div>
    </div>

    <div class="flex flex-wrap justify-center my-24">
<%
            if (ordini != null)
            {
                for(OrderDTO order: ordini)
                {
%>
                    <div class="bg-principale rounded-md px-4 py-4 w-5/6 mt-5 shadow-md">
                        <div>Customer username:&nbsp&nbsp<%= order.getUser() %> </div>
                        <div>Customer address:&nbsp&nbsp<%= order.getAddress() %>, <%= order.getZipcode() %> </div>
                        <div>Total:&nbsp&nbsp<%= order.getTotal()%>&nbspUSD</div>
                        <div class="relative mx-auto w-5/6 mt-4 rounded-xl px-4 py-2 bg-white">
<%                          for(DishDTO dish: order.getDishes())
                            {
%>
                                <div class="font-bold"><%= dish.getQuantity() %>&nbsp &nbsp &nbsp &nbsp<%= dish.getName() %></div>
<%                          }
%>
                        </div>
                        <form method="post" action="<c:url value="/checkout"/>">
                            <input type="hidden" name="action" value="send">
                            <input type="hidden" name="oid" value="<%= order.getId() %>">
                            <input type="hidden" name="rid" value="<%= details.getId() %>">
                            <button class="float-right mt-5 border-2 rounded-xl px-3 hover:bg-button" type="submit"> Delivered </button>
                        </form>
                     </div>

<%               }
            }
%>
    </div>
    <%@include file="template/footer.jsp"%>
</body>
</html>
