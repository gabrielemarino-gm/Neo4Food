<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
  <head>
      <title>Title</title>
      <script type="text/javascript" src="<c:url value="/js/jquery-3.6.3.min.js"/>"></script>
  </head>
  <body>
  <h1>AAA</h1>
  <form method="post" action="<c:url value="/testing"/>">
    <input type="hidden" name="action" value="isFill">
  <% for(int i = 0; i< 5; i++){ %>
    <input type="hidden" name="iter" value="<%= i %>-th iter">
  <% } %>
    <button type="submit">Submit</button>
  </form>
  </body>
</html>
