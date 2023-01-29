<%@ page import="it.unipi.lsmsd.neo4food.dto.OrderDTO" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.DishDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="com.sun.org.apache.xpath.internal.operations.Or" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Checkout</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout</title>
    <%@ include file="/WEB-INF/jsp/template/head_includes.jsp" %>
    <%
        OrderDTO order = (OrderDTO) request.getAttribute("order");
    %>
    <script type="text/javascript" src="<c:url value="/js/jquery-3.6.3.min.js"/>"></script>
    </head>
    <body>
    <script type="text/javascript">
        toSend = {
            action: "confirm",
            incremental: '<%= new Gson().toJson(request.getAttribute("order")) %>',
            pm: "",
            pn: "",
        };

        function send(){
            toSend.pm = $('#pm').val();
            toSend.pn = $('#pn').val();

            $.post("<c:url value="/checkout"/>", toSend, function (result){
                alert("Ordine completato");
                window.location.href = "<c:url value="/ricerca"/>";
            }).fail(function (xhr, status, error){
                alert(xhr+"\n"+status+"\n"+error);
            });
        }

    </script>
    <%@include file="template/header.jsp"%>
    <div>
        <form class="relative mx-auto w-5/6">
            <h1 class="font-bold text-2xl mt-5 text-center">Order Recap</h1>
            <div class="bg-principale rounded-xl shadow-md px-5 py-5 mt-5">
                <div class="mt-2">Customer: <%= order.getUser() %></div>
                <div class="mt-2">Restaurant: <%= order.getRestaurant() %></div>
                <div class="mt-2">Address: <%= order.getAddress() %>, <%= order.getZipcode()%></div>
                <div class="mt-2">Payment method: <input class="rounded-lg px-3 ml-2" id="pm" required type="text" value="<%= order.getPaymentMethod() != null ? order.getPaymentMethod() : "" %>" placeholder="Payment method"></div>
                <div class="mt-2">Card number: <input class="rounded-lg px-3 ml-2" id="pn" required type="text" value="<%= order.getPaymentNumber() != null ? order.getPaymentNumber() : "" %>" placeholder="Payment number"></div>
                <div class="relative mx-auto w-5/6 mt-4 rounded-xl px-4 py-2 bg-white">
                    <div class="font-bold">Dishes:</div>
<%--                    List of dish here--%>
                    <% List<DishDTO> list = order.getDishes();
                    for(DishDTO item: list){%>
                        <div class="mt-3 ml-5"><%= item.getName() %> x <%= item.getQuantity() %></div>
                    <%}%>
                    <div class="mt-2 ml-88">Total: <%= new DecimalFormat("#0.00").format(order.getTotal()) %> <%= order.getDishes().get(0).getCurrency() %></div>
                </div>
                <div class="h-10"></div>
                <button type="button" class="absolute bottom-3 right-4 border-2 rounded-xl px-3 hover:bg-button" onclick="send()">Confirm order</button>
            </div>
        </form>
    </div>
    <%@include file="template/footer.jsp"%>
    </body>
</html>