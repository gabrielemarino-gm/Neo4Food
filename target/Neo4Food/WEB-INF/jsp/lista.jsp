<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.ListDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.constants.Constants" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.RestaurantDTO" %>
<%@ page import="java.util.List" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Restaurants List</title>
        <%@ include file="/WEB-INF/jsp/template/head_includes.jsp" %>
    </head>
    <body>
            <%--                Header con login o nomeutente--%>
            <%@include file="template/header.jsp"%>
            <%
                String requestMessage = (String) request.getAttribute("message");
                String zipcode = (String) request.getAttribute("zipcode");
                int pagenum = Integer.parseInt(request.getAttribute("page").toString());
                System.out.println(pagenum);
                ListDTO<RestaurantDTO> listDTO = (ListDTO<RestaurantDTO>) request.getAttribute("listDTO");
                List<RestaurantDTO> list = listDTO.getList();
                int count = listDTO.getItemCount();
            %>

            <% if (requestMessage != null) { %>
                <div role="alert"><%= requestMessage %></div>
            <% } %>

            <div class="px-3">
                <h2>Results for <%= zipcode %></h2>
            </div>

            <div class="my-5 flex px-2">
                <div class="w-1/3 border rounded-lg">
                    List of filter to be add
                </div>

                <div class="px-4 w-2/3">
                    <%  if(list!=null && !list.isEmpty())
                        {
                            for (RestaurantDTO item: list)
                            {
                    %>
                                <div class="bg-principale rounded-lg my-2">
                                    <div class="px-6">
                                        <h2><%= item.getName() %></h2>
                                        <a><%= item.getRating() %></a>
                                    </div>
                                    <div class="float-right px-3 -my-7">
                                        <form method="post" action="<c:url value="/restaurant"/>">
                                            <input type="hidden" name="rid" value="<%= item.getId() %>">
                                            <button class="px-5 rounded-lg hover:bg-button" type="submit">See more</button>
                                        </form>
                                    </div>
                                </div>
                    <%
                            }
                        }
                    %>
                    <div>
                        <div>
                            <% if(pagenum > 0){ %>
                                <div>
                                    <form class="my-4" method="post" action="<c:url value="/ricerca"/>">
                                        <input type="hidden" name="zipcode" value="<%= zipcode %>">
                                        <input type="hidden" name="page" value= <%= pagenum-1 %>>
                                        <input type="hidden" name="action" value="search">
                                        <button class="w-20 rounded-lg border-2 hover:bg-button" type="submit"> < <%= pagenum%></button>
                                    </form>
                                </div>
                            <% } %>
                            <% if (count == Constants.PAGE_SIZE){ %>
                                <div>
                                    <form class="my-4" method="post" action="<c:url value="/ricerca"/>">
                                        <input type="hidden" name="zipcode" value="<%= zipcode %>">
                                        <input type="hidden" name="page" value= <%= pagenum+1 %>>
                                        <input type="hidden" name="action" value="search">
                                        <button class="w-20 rounded-lg border-2 hover:bg-button" type="submit"><%= pagenum+1%> > </button>
                                    </form>
                                </div>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>
        <%@include file="template/footer.jsp"%>
    </body>
</html>
