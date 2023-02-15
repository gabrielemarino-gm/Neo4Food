<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.DishDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.OrderDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.ListDTO" %>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat" %>

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
    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm");
%>

    <%@include file="template/header.jsp"%>
    <div class="z-40 h-48 overflow-hidden w-full">
        <img class="w-full blur-md" src="https://ilfattoalimentare.it/wp-content/uploads/2017/06/junk-food-hamburger-patatine-fast-food-pizza-dolci-Fotolia_130389179_Subscription_Monthly_M.jpg" alt="imgFood" />
    </div>
    <div class="mt-4 font-bold flex flex-wrap justify-center text-3xl">Your Past Orders</div>
    <div class="flex flex-wrap justify-center my-8">
<%
            if (count > 0)
            {
              for(OrderDTO order: orders.getList())
                {

                    if (isRestaurant)
                    {
%>
                        <div class="bg-principale rounded-md px-4 py-4 w-5/6 mt-5 shadow-md">
                            <div>Customer username:&nbsp&nbsp<%= order.getUser() %> </div>
                            <div>Customer address:&nbsp&nbsp<%= order.getAddress() %>, <%= order.getZipcode() %> </div>
                            <div>Date of Creation:&nbsp&nbsp<%= formatter.format(order.getCreationDate())%></div>
                            <div>Total:&nbsp&nbsp<%= Math.round(order.getTotal() * 100.0) / 100.0%>&nbsp<%= order.getCurrency() %></div>
                            <div class="relative mx-auto w-5/6 mt-4 rounded-xl px-4 py-2 bg-white">
<%
                                for(DishDTO dish: order.getDishes())
                                {
%>
                                    <div class="font-bold flex">
                                        <input type="text" class="w-5/6 bg-white" disabled value="<%= dish.getQuantity() %>&nbsp; &nbsp; &nbsp; &nbsp;<%= dish.getName() %>">
                                        <div class="absolute right-10">
                                            <%=dish.getPrice()%>&nbsp;<%=dish.getCurrency()%>
                                        </div>
                                    </div>
<%                              }
%>
                            </div>
                        </div>

<%                  }
                    else // Visuale Utente
                    {
%>
                        <div class="bg-principale rounded-md px-5 py-4 w-5/6 mt-5 shadow-md">
                            <div>Restaurant:&nbsp&nbsp<%= order.getRestaurant() %> </div>
                            <div>Date of Creation:&nbsp&nbsp<%= formatter.format(order.getCreationDate())%></div>
                            <div>Date of Delivery:&nbsp&nbsp<%= (order.getDeliveryDate()!=null)? formatter.format(order.getDeliveryDate()):"Not delivered yet"%></div>
                            <div>Total:&nbsp&nbsp<%= Math.round(order.getTotal() * 100.0) / 100.0%>&nbsp<%= order.getCurrency() %></div>
                            <div class="relative mx-auto w-5/6 mt-4 rounded-xl px-4 py-2 bg-white">
<%
                                    for(DishDTO dish: order.getDishes())
                                    {
%>

                                        <div class="font-bold flex">
                                            <input type="text" class="w-5/6 bg-white" disabled value="<%= dish.getQuantity() %>&nbsp; &nbsp; &nbsp; &nbsp;<%= dish.getName() %>">
                                            <div class="absolute right-10">
                                                <%=dish.getPrice()%>&nbsp;<%=dish.getCurrency()%>
                                            </div>
                                        </div>
<%                                 }
%>
                            </div>
                        </div>
<%                  }
                }
            }
            else
            {
//              No orders
%>
                <div class="font-bold">No orders to view</div>
<%
            }
%>
    </div>
    <%@include file="template/footer.jsp"%>
</body>
</html>
