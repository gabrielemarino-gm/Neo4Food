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
      <div class="-top-6 overflow-hidden h-48 z-50">
          <img class="blur-md w-full" src="https://ilfattoalimentare.it/wp-content/uploads/2017/06/junk-food-hamburger-patatine-fast-food-pizza-dolci-Fotolia_130389179_Subscription_Monthly_M.jpg" alt="imgFood">
      </div>

        <div class="relative mx-auto h-28 w-2/3 rounded-lg bg-principale text-center py-3 -my-11">
<%--        Restaurant detailed infos--%>
            <div class="text-3xl font-bold"><%= details.getName() %></div>
            <div><%= details.getPricerange() %></div>
            <div><%= details.getRating() %></div>
        </div>

        <div class="flex flex-wrap px-5 my-16 justify-center">
<%--        List of available dishes--%>
            <% for(Dish i: list.getList())
            {
                String price = i.getCost()==0.0 ? "-.-": i.getCost().toString(); %>
                <div class="border rounded-xl w-96 h-44 mt-5 mr-5">
                    <div><%= i.getName()%></div>
                    <div><%= i.getDescription()%></div>
                    <div><%= price %> <%= i.getCurrency() %></div>
                </div>
            <% } %>
        </div>
   <%@include file="template/footer.jsp"%>
  </body>
</html>
