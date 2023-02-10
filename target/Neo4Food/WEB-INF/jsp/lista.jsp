<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.ListDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.constants.Constants" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.RestaurantDTO" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Research</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Restaurants List</title>
        <%@ include file="/WEB-INF/jsp/template/head_includes.jsp" %>
        <style>
            .cliccato
            {
                color: #FFF4EA;
            }
        </style>

    </head>
    <body>
            <%--                Header con login o nomeutente--%>
<%@include file="template/header.jsp"%>

<%
                String requestMessage = (String) request.getAttribute("message");
                String filter = request.getAttribute("filter") != null ? (String) request.getAttribute("filter") : "";
                String zipcode = (String) request.getAttribute("zipcode");
                int pagenum = Integer.parseInt(request.getAttribute("page").toString());

                ListDTO<RestaurantDTO> listDTO = (ListDTO<RestaurantDTO>) request.getAttribute("listDTO");
                int count = listDTO.getItemCount();
                List<RestaurantDTO> list = listDTO.getList();
%>

<%          if (requestMessage != null)
            { %>
                <div role="alert"><%= requestMessage %></div>
<%          } %>

            <div class="px-3 mx-auto my-7 text-center font-bold text-xl">
                <h2>Results for <%= zipcode %></h2>
            </div>

            <div class="my-5 flex px-2">
                <div class="w-1/3 rounded-lg">
                    <div class="px-3 h-7">
                        <h2>Try our filter:</h2>
                    </div>
                    <div class="flex flex-wrap">

<!--                    FILTRI -->
<%                          if ("".equals(filter))
                            {
%>
                                <div class="text-center h-9 mr-3 mt-3 px-4 py-2 rounded-lg bg-test_col shadow-md">
                                    <form>
                                        <input type="hidden" name="page" value="0"/>
                                        <input type="hidden" name="zipcode" value="<%= zipcode %>"/>
                                        <input type="hidden" name="action" value="search"/>
                                        <input type="hidden" name="filter" value=""/>
                                        <button class="cliccato" type="submit">Reset</button>
                                    </form>
                                </div>
<%                          }
                            else
                            {
%>
                                <div id="button" class="filters text-center h-9 mr-3 mt-3 px-4 py-2 rounded-lg bg-principale shadow-md hover:bg-button">
                                    <form>
                                        <input type="hidden" name="page" value="0"/>
                                        <input type="hidden" name="zipcode" value="<%= zipcode %>"/>
                                        <input type="hidden" name="action" value="search"/>
                                        <input type="hidden" name="filter" value=""/>
                                        <button  type="submit">Reset</button>
                                    </form>
                                </div>
<%                          }
%>

<%                          for(String x : Constants.RESTAURANTS_CATEGORIES)
                            {
                                if (x.equals(filter))
                                {
%>
                                    <div class="text-center h-9 mr-3 mt-3 px-4 py-2 rounded-lg bg-test_col shadow-md">
                                        <form>
                                            <input type="hidden" name="page" value="0"/>
                                            <input type="hidden" name="zipcode" value="<%= zipcode %>"/>
                                            <input type="hidden" name="action" value="search"/>
                                            <input type="hidden" name="filter" value="<%= x %>"/>
                                            <button class="cliccato" type="submit"><%= x %></button>
                                        </form>
                                    </div>
<%                              }
                                else
                                {
%>
                                    <div id="button<%= x %>" class="filters text-center h-9 mr-3 mt-3 px-4 py-2 rounded-lg bg-principale shadow-md hover:bg-button">
                                        <form>
                                            <input type="hidden" name="page" value="0"/>
                                            <input type="hidden" name="zipcode" value="<%= zipcode %>"/>
                                            <input type="hidden" name="action" value="search"/>
                                            <input type="hidden" name="filter" value="<%= x %>"/>
                                            <button  type="submit"><%= x %></button>
                                        </form>
                                    </div>
<%                              }
                            }

                         if(isLogged)
                         {
                            if("recc".equals(filter))
                            {
%>
                                <div id="buttonRecc" class="text-center h-9 mr-3 mt-3 px-4 py-2 rounded-lg bg-test_col shadow-md">
                                    <form>
                                        <input type="hidden" name="page" value="0"/>
                                        <input type="hidden" name="zipcode" value="<%= zipcode %>"/>
                                        <input type="hidden" name="action" value="search"/>
                                        <input type="hidden" name="filter" value="recc"/>
                                        <button class="cliccato" type="submit">Recommend me!</button>
                                    </form>
                                </div>
<%                          }
                            else
                            {
%>
                                <div id="buttonRecc" class="filters text-center h-9 mr-3 mt-3 px-4 py-2 rounded-lg bg-principale shadow-md hover:bg-button">
                                    <form>
                                        <input type="hidden" name="page" value="0"/>
                                        <input type="hidden" name="zipcode" value="<%= zipcode %>"/>
                                        <input type="hidden" name="action" value="search"/>
                                        <input type="hidden" name="filter" value="recc"/>
                                        <button  type="submit">Recommend me!</button>
                                    </form>
                                </div>
<%                          }
                         }
%>

                    <div class="h-3"></div>

                </div>
            </div>

                <div class="px-4 w-2/3 position-relative">
<%                  if(list != null && !list.isEmpty())
                    {
                        for (RestaurantDTO item: list)
                        {
%>
                            <form method="post" action="<c:url value="/restaurant"/>">
                                <input type="hidden" name="rid" value= "<%= item.getId() %>" >
                                <input type="hidden" name="action" value="details">
                                <button class="h-20 w-full px-5 py-3 rounded-lg bg-principale shadow-md hover:bg-button" type="submit">
                                    <h2 class="float-left"><%= item.getName() %></h2>

                                    <div class="flex flex-wrap float-right hover:bg-button">
<%
                                        Float rate = item.getRating();
                                        int rateInt = rate.intValue();
                                        int nStar=0;
                                        for (; nStar<rateInt; nStar++)
                                        {
%>
                                            <img class="h-5" src="img/star.png" alt="star">
<%
                                        }

                                        rate = rate*10;
                                        nStar = rateInt;
                                        if (rate%10 >= 5)
                                        {
%>
                                            <img class="h-5" src="img/half_star.png" alt="star">
<%
                                            nStar = rateInt+1;
                                        }

                                        for (; nStar<5; nStar++)
                                        {
%>
                                            <img class="h-5" src="img/empty_star.png" alt="star">
<%
                                        }
%>
                                    </div>

                                </button>
                            </form>
                            <div class="h-3"></div>
<%
                        }
                    }
%>
                    <div>
                        <div class="flex justify-end items-end">

<%--                        Previous page--%>
<%                          if(pagenum > 0)
                            {
%>
                                <form class="my-4 bg-red-100" method="post" action="<c:url value="/ricerca"/>">
                                    <input type="hidden" name="zipcode" value="<%= zipcode %>">
                                    <input type="hidden" name="page" value= <%= pagenum - 1%>>
                                    <input type="hidden" name="action" value="search">
                                    <input type="hidden" name="filter" value="<%= filter %>" />
                                    <button class="mx-2 rounded-lg hover:bg-button" type="submit"><img class="h-7" src="img/left_arrow.png" alt="prec"></button>
                                </form>
<%                          }
%>
<%--                        Next page--%>
<%                          if (count == Constants.PAGE_SIZE)
                            {
%>
                                <form class="my-4 "  method="post" action="<c:url value="/ricerca"/>">
                                    <input type="hidden" name="zipcode" value="<%= zipcode %>">
                                    <input type="hidden" name="page" value= <%= pagenum + 1 %>>
                                    <input type="hidden" name="action" value="search">
                                    <input type="hidden" name="filter" value="<%= filter %>" />
                                    <button class="mx-2 rounded-lg hover:bg-button" type="submit"><img class="h-7" src="img/right_arrow.png" alt="succ"></button>
                                </form>
<%                           }
%>
                        </div>
                    </div>
                </div>
            </div>
        <%@include file="template/footer.jsp"%>
    </body>
</html>