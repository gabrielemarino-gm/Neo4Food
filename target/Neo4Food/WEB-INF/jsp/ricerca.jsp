<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <%@ include file="/WEB-INF/jsp/template/head_includes.jsp" %>
    </head>
    <body>
        
<%--                Header con login o nomeutente--%>
            <%@include file="template/header.jsp"%>
            <%
                String requestMessage = (String) request.getAttribute("message");
            %>
            <section class="mx-auto my-48 text-center">
                <% if (requestMessage != null) { %>
                <div role="alert"><%= requestMessage %></div>
                <% } %>
                <div class="mx-auto h-20 w-96 rounded-lg bg-principale">
                    <h2>Search restaurants</h2>
                    <form class="my-4" method="post" action="<c:url value="/ricerca"/>">
                        <input class="rounded-lg px-3" type="text" name="zipcode" placeholder="ZIP Code">
                        <input type="hidden" name="action" value="search">
                        <button class="w-20 rounded-lg border-2" type="submit">Search</button>
                    </form>
                </div>
            </section>
        
        <%@include file="template/footer.jsp"%>
    </body>
</html>