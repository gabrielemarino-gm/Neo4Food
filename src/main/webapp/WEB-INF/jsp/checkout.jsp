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
        <form>
            <h1>Order recap</h1>
            <div>
                <div>Customer: <%= order.getUser() %></div>
                <div>Restaurant: <%= order.getRestaurant() %></div>
                <div>Address: <%= order.getAddress() %>, <%= order.getZipcode()%></div>
                <div>Payment method: <input id="pm" required type="text" value="<%= order.getPaymentMethod() != null ? order.getPaymentMethod() : "" %>" placeholder="Payment method"></div>
                <div>Card number: <input id="pn" required type="text" value="<%= order.getPaymentNumber() != null ? order.getPaymentNumber() : "" %>" placeholder="Payment number"></div>
                <div>
<%--                    List of dish here--%>
                    <% List<DishDTO> list = order.getDishes();
                    for(DishDTO item: list){%>
                    <div>
                        <div><%= item.getName() %> x <%= item.getQuantity() %></div>
                    </div>
                    <%}%>
                </div>
                <div>Total: <%= new DecimalFormat("#0.00").format(order.getTotal()) %> <%= order.getDishes().get(0).getCurrency() %></div>
                <button onclick="send()">Confirm order</button>
            </div>
        </form>
    </div>
    <%@include file="template/footer.jsp"%>
    </body>
</html>