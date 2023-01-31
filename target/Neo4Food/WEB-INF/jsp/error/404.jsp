<%--
  Created by IntelliJ IDEA.
  User: gabriele
  Date: 31/01/23
  Time: 18:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="description" content="">
  <meta name="author" content="">
  <title>Error 404 Page</title>
  <%@ include file="/WEB-INF/jsp/template/head_includes.jsp" %>
</head><!--/head-->

<body>
<div class="container text-center my-16">
  <div class="flex justify-center">
    <a href="<c:url value="/ricerca"/>"><img src="<c:url value="/img/404.png"/>" alt="" /></a>
  </div>
  <div class="my-5">
    <h1><b>OPPS!</b> We Couldn’t Find this Page</h1>
    <p>Uh... So it looks like you brock something. The page you are looking for has up and Vanished.</p>
    <h2><a href="<c:url value="/ricerca"/>">Bring me back to Neo4Food</a></h2>
  </div>
</div>


</body>
</html>
