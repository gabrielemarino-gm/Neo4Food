<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <%@ include file="/WEB-INF/jsp/template/head_includes.jsp" %>

    <title>Admin page</title>
    <script type="text/javascript" src="<c:url value="/js/jquery-3.6.3.min.js"/>"></script>
    <script type="text/javascript">

        function updateRatings(){
            toSend = {
                action: "updateRatings"
            };
            $.post("<c:url value="/admin">", toSend, function (result) {
            //     Do stuff

            }).fail(function(xhr, status, error){

            });
        }

        function updatePrices(){
            toSend = {
                action: "updatePrices"
            };

            $.post("<c:url value="/admin">", toSend, function (result) {
                //     Do stuff

            }).fail(function(xhr, status, error){

            });
        }


    </script>
</head>
<body>
<div id="message"><h1 id="messageField"></h1></div>

<button onclick="updateRatings()">Update ratings</button>

<button onclick="updatePrices()">Update prices</button>

<div>
<%--    Altre statistiche per admin --%>
</div>
</body>
</html>