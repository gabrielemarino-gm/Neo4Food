<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
    </head>
    <body>
        <div>
            <%--                Header con login o nomeutente--%>
            <%@include file="template/header.jsp"%>
        </div>
            <%
                String requestMessage = (String) request.getAttribute("message");
            %>
            <% if (requestMessage != null) { %>
            <div role="alert"><%= requestMessage %></div>
                <% } %>
            <div>
                <%
                    String message = (String) request.getAttribute("zipcode");
                %>
                <a><%= message %></a>
            </div>
    </body>
</html>
