<%--
  Created by IntelliJ IDEA.
  User: gabriele
  Date: 12/02/23
  Time: 10:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unipi.lsmsd.neo4food.constants.Constants" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.*" %>
<html>
<head>
<%
      RestaurantDTO details = (RestaurantDTO) session.getAttribute(Constants.AUTHENTICATION_FIELD);
      List<OrderDTO> ordini = (List<OrderDTO>) request.getAttribute("orderList");
%>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Restaurant Statistics</title>
    <%@ include file="/WEB-INF/jsp/template/head_includes.jsp" %>
    <script type="text/javascript" src="<c:url value="/js/jquery-3.6.3.min.js"/>"></script>
</head>

<body>
    <%@include file="template/header.jsp"%>

    <div class="flex flex-wrap justify-center mt-4">
        <div class="bg-principale rounded-xl w-80 text-center px-5 py-3 mr-5 mt-8 relative shadow-md">
            <h1 class="font-bold">Busiest Time</h1>
        </div>

        <div class="bg-principale rounded-xl w-80 text-center px-5 py-3 mr-5 mt-8 relative shadow-md">
            <h1 class="font-bold">Best Month's Dishes</h1>
        </div>

        <div class="bg-principale rounded-xl w-80 text-center px-5 py-3 mr-5 mt-8 relative shadow-md">
            <h1 class="font-bold">Daily Revenue</h1>
        </div>

        <div class="bg-principale rounded-xl w-80 text-center px-5 py-3 mr-5 mt-8 relative shadow-md">
            <h1 class="font-bold">Moda Orders</h1>
        </div>

        <div class="bg-principale rounded-xl w-80 text-center px-5 py-3 mr-5 mt-8 relative shadow-md">
            <h1 class="font-bold">Avarege Customers Age</h1>
        </div>

    </div>


    <%@include file="template/footer.jsp"%>
</body>
</html>
