<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.DishDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.OrderDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.ListDTO" %>
<%@ page import="java.util.List" %>

<html>
<head>
    <title>Orders history</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <%@ include file="/WEB-INF/jsp/template/head_includes.jsp" %>
</head>
<body>

<%
    ListDTO<OrderDTO> orders = (ListDTO<OrderDTO>) request.getAttribute("orders");
    int count = orders.getItemCount();
%>

<%@include file="template/header.jsp"%>
<div class="z-40 h-48 overflow-hidden w-full">
    <img class="w-full blur-md" src="https://ilfattoalimentare.it/wp-content/uploads/2017/06/junk-food-hamburger-patatine-fast-food-pizza-dolci-Fotolia_130389179_Subscription_Monthly_M.jpg" alt="imgFood" />
</div>

<div class="flex flex-wrap justify-center my-24">
    <%
        if (count > 0)
        {
            for(OrderDTO order: orders.getList())
            {
    %>
    <div class="bg-principale rounded-md px-4 py-4 w-5/6 mt-5 shadow-md">
        <div>Customer Username:&nbsp&nbsp<%= order.getUser() %> </div>
        <div>Customer Address:&nbsp&nbsp<%= order.getAddress() %>, <%= order.getZipcode() %> </div>
        <div>Total:&nbsp&nbsp<%= order.getTotal()%>&nbspUSD</div>
        <div class="relative mx-auto w-5/6 mt-4 rounded-xl px-4 py-2 bg-white">
            <% for(DishDTO dish: order.getDishes())
            {
            %>
            <div class="font-bold"><%= dish.getQuantity() %>&nbsp &nbsp &nbsp &nbsp<%= dish.getName() %></div>
            <%                          }
            %>
        </div>
    </div>

    <%               }
    } else {
//            No orders

    }
    %>
</div>
<%@include file="template/footer.jsp"%>
</body>
</html>
