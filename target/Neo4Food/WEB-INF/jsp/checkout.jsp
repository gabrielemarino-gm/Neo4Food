<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Research</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout</title>
    <%@ include file="/WEB-INF/jsp/template/head_includes.jsp" %>
    </head>
    <body>
    <%@include file="template/header.jsp"%>
    <div><%= request.getParameter("productId").toString() %></div>

    <%@include file="template/footer.jsp"%>
    </body>
</html>