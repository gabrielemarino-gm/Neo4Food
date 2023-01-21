<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
    </head>
    <body>
        <div>
            <div>
<%--                Header con login o nomeutente--%>
                <%@include file="template/header.jsp"%>
            </div>
            <%
                String requestMessage = (String) request.getAttribute("message");
            %>
            <section>
                <% if (requestMessage != null) { %>
                <div role="alert"><%= requestMessage %></div>
                <% } %>
                <div>
                    <h2>Search restaurants</h2>
                    <form method="post" action="<c:url value="/ricerca"/>">
                        <input type="text" name="zipcode" placeholder="ZIP Code">
                        <input type="hidden" name="action" value="search">
                        <button type="submit">Search</button>
                    </form>
                </div>
            </section>
        </div>
    </body>
</html>