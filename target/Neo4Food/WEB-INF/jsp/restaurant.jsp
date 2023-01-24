<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.ListDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.RestaurantDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.model.Dish" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Restaurant Page</title>
  <%@ include file="/WEB-INF/jsp/template/head_includes.jsp" %>
  </head>
  <body>
  <%
        RestaurantDTO details = (RestaurantDTO) request.getAttribute("details");
        ListDTO<Dish> list = details.getDishes();
  %>
<%--                Header con login o nomeutente--%>
    <%@include file="template/header.jsp"%>
    <div>
<%--      Restaurant detailed infos--%>
        <div><%= details.getName() %></div>
        <div><%= details.getRating() %></div>
    <div>

    </div>
<%--      List of available dishes--%>
    <% for(Dish i: list.getList()){ %>
        <div><%= i.getName()%></div>
        <div><%= i.getDescription()%></div>
        <div><div><%= i.getCost() %></div><div><%= i.getCurrency() %></div></div>
    <% } %>
    <div>

    </div>
    </div>
    <%@include file="template/footer.jsp"%>
  </body>
</html>
