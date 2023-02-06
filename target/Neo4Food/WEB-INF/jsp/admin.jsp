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
            let messageTimeout;

            function updateRatings(){
                toSend = {
                    action: "updateRatings"
                };
                $('#updateRatings').attr("disabled", true);
                $('#updateRatings').text("Wait please...");

                $.post("<c:url value="/admin"/>", toSend, function (result) {
                //  Do stuff on success
                    setMessage(result);
                }).fail(function(xhr, status, error){
                    setMessage("Something wrong occurred");
                });
                $('#updateRatings').attr("disabled",false);
                $('#updateRatings').text("Update ratings");
            }

            function updatePrices(){
                toSend = {
                    action: "updatePrices"
                };
                $('#updatePrices').attr("disabled", true);
                $('#updatePrices').text("Wait please...");

                $.post("<c:url value="/admin"/>", toSend, function (result) {
                    //     Do stuff on success
                    setMessage(result);
                }).fail(function(xhr, status, error){
                    setMessage("Something wrong occurred");
                });

                $('#updatePrices').attr("disabled", false);
                $('#updatePrices').text("Update prices");
            }

            function setMessage(message){
                messageTimeout = setTimeout(hideMessage, 5);
                $('#message').hide();
                $('#messageField').text(message);
            }

            function hideMessage(){
                $('#message').hide();
                $('#messageField').text("");
            }
        </script>
    </head>
    <body>
        <div id="message"><h1 id="messageField"></h1></div>
        <div>
            <div><h1>Total users: <%= (long)request.getAttribute("uCount") %></h1></div>
            <div><h1>Total restaurants: <%= (long)request.getAttribute("rCount") %></h1></div>
            <div><h1>Total orders: <%= (long)request.getAttribute("oCount") %></h1></div>
        </div>
        <div>
            <h1>WARNING: Those actions may take several minutes, be careful...</h1>
        <button id="updateRatings" onclick="updateRatings()">Update ratings</button>
        <button id="updatePrices" onclick="updatePrices()">Update prices</button>
        </div>
        <div>
        <%--    Altre statistiche per admin --%>

        </div>
    </body>
</html>