<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.ListDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.RestaurantDTO" %>
<%@ page import="java.util.List" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <%@ include file="/WEB-INF/jsp/template/head_includes.jsp" %>
    </head>
    <body>
        <div>
            <%--                Header con login o nomeutente--%>
            <%@include file="template/header.jsp"%>
        </div>
            <%
                String requestMessage = (String) request.getAttribute("message");
                ListDTO<RestaurantDTO> listDTO = (ListDTO<RestaurantDTO>) request.getAttribute("listDTO");
                String zipcode = (String) request.getAttribute("zipcode");

                List<RestaurantDTO> list = listDTO.getList();
                int count = listDTO.getItemCount();


            %>
            <% if (requestMessage != null) { %>
            <div role="alert"><%= requestMessage %></div>
                <% } %>
            <div>
                <div>
                    <div>
                        <h2>Results for <%= zipcode %></h2>
                    </div>
                    <%  if(list!=null && !list.isEmpty()){
                            for (RestaurantDTO item: list) {
                    %>
                        <div>
                            <div>
                                <h2><%= item.getName() %></h2>
                                <a><%= item.getRating() %></a>
                            </div>
                            <div>
                                <form method="post" action="<c:url value="/restaurant"/>">
                                    <input type="hidden" name="rid" value="<%= item.getId() %>">
                                    <button type="submit"> See more</button>
                                </form>
                            </div>
                        </div>
                    <%
                        }
                    }
                    %>
                </div>
            </div>
        <%@include file="template/footer.jsp"%>
    </body>
</html>
