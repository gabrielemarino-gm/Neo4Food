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
    <div>
        <h1>Busiest Time</h1>
    </div>

    <div>
        <h1>Busiest Time</h1>
    </div>
</body>
</html>
