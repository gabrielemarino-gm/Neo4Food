<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <%@ include file="/WEB-INF/jsp/template/head_includes.jsp" %>
        <style>
            h1, h2, h3, h4, h5, p, a, button
            {
                color: #7C2714;
                font-family: gill sans, sans-serif;
            }
        </style>
        <title>Home Page</title>
    </head>
    <body>
        
<%--                Header con login o nomeutente--%>
            <%@include file="template/header.jsp"%>
            <%
                String requestMessage = (String) request.getAttribute("message");
            %>
            <div class="-top-6 overflow-hidden h-96 z-50">
                <img class="w-full" src="img/sfondo.png" alt="imgFood">
            </div>
            <section class="mx-auto -my-6 text-center">
                <% if (requestMessage != null) { %>
                <div role="alert"><%= requestMessage %></div>
                <% } %>
                <div class="relative mx-auto h-28 w-96 rounded-lg bg-principale">
                    <div class="h-3 w-96"></div>
                    <h2 class="text-xl top-10 text-center">Search for a Restaurant</h2>
                    <form class="my-4" method="post" action="<c:url value="/ricerca"/>">
                        <input required class="rounded-lg px-3" type="text" name="zipcode" placeholder="ZIP Code">
                        <input type="hidden" name="page" value= 0>
                        <input type="hidden" name="action" value="search">
                        <button class="w-20 rounded-lg border-2 hover:bg-button" type="submit">Search</button>
                    </form>
                </div>
            </section>
        
        <%@include file="template/footer.jsp"%>
    </body>
</html>