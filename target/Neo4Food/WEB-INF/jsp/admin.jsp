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
                    $('#updateRatings').attr("disabled",false);
                    $('#updateRatings').text("Update ratings");
                }).fail(function(xhr, status, error){
                    setMessage("Something wrong occurred");
                });
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
                    $('#updatePrices').attr("disabled", false);
                    $('#updatePrices').text("Update prices");
                }).fail(function(xhr, status, error){
                    setMessage("Something wrong occurred");

                });
            }

            function setMessage(message){
                messageTimeout = setTimeout(hideMessage, 5000);
                $('#message').show();
                $('#messageField').text(message);
            }

            function hideMessage(){
                $('#message').hide();
                $('#messageField').text("");
            }
        //     ---------Analytics zipcode------------
            let zipBox = '#bestZipcodeBox';
            function getZips(){
                toSend = {
                  action: "analytics",
                  type: "zips"
                };

                $('#zipsButton').attr("disabled", true);
                $('#zipsButton').text("Wait please...");

                $.post("<c:url value="/admin"/>", toSend, function (result) {
                    json = JSON.parse(result);
                    //     Do stuff on success

                    $(zipBox).empty();
                    if(json.itemCount > 0) {
                        for (i = 0; i < json.itemCount; i++) {
                            commento = json.list[i];
                            $(zipBox).append('<div><div>' + commento.zipcode + '</div><div>' + commento.count + '</div></div>')

                        }
                    }

                    $('#zipsButton').attr("disabled", false);
                    $('#zipsButton').text("Load most active zipcodes");


                }).fail(function(xhr, status, error){
                    setMessage("Something wrong occurred");

                });
            }
        //     ---------Analytics mostActiveUsers---------
            let mostActiveBox = '#mostActiveBox';
            function getMostActive(){
                toSend = {
                    action: "analytics",
                    type: "mostActive"
                };

                $('#mostActiveButton').attr("disabled", true);
                $('#mostActiveButton').text("Wait please...");

                $.post("<c:url value="/admin"/>", toSend, function (result) {
                    //     Do stuff on success
                    json = JSON.parse(result);

                    $(mostActiveBox).empty();
                    if(json.itemCount > 0) {
                        for (i = 0; i < json.itemCount; i++) {
                            commento = json.list[i];
                            $(mostActiveBox).append('<div><div>' + commento.user + '</div><div>' + commento.count + '</div><div>' + commento.dub+'</div></div>')

                        }
                    }

                    $('#mostActiveButton').attr("disabled", false);
                    $('#mostActiveButton').text("Most active users");

                }).fail(function(xhr, status, error){
                    setMessage("Something wrong occurred");

                });

            }
        //     ---------Analytics guadagni---------
            let profitBox = '#profitBox';
            function getProfits(){
                toSend = {
                    action: "analytics",
                    type: "profits"
                };

                $('#profitButton').attr("disabled", true);
                $('#profitButton').text("Wait please...");

                $.post("<c:url value="/admin"/>", toSend, function (result) {
                    //     Do stuff on success
                    json = JSON.parse(result);

                    $(profitBox).empty();
                    if(json.itemCount > 0) {
                        for (i = 0; i < json.itemCount; i++) {
                            commento = json.list[i];
                            $(profitBox).append('<div><div>' + commento.restaurant + '</div><div>' + commento.dub + '</div></div>')

                        }
                    }

                    $('#profitButton').attr("disabled", false);
                    $('#profitButton').text("Load profits");

                }).fail(function(xhr, status, error){
                    setMessage("Something wrong occurred");

                });

            }
        //     ---------Analytics caviale---------
            let cavialeBox = '#cavialeBox';
            function getCaviale(){
                toSend = {
                    action: "analytics",
                    type: "caviale"
                };

                $('#cavialeButton').attr("disabled", true);
                $('#cavialeButton').text("Wait please...");

                $.post("<c:url value="/admin"/>", toSend, function (result) {
                    //     Do stuff on success

                    json = JSON.parse(result);

                    $(cavialeBox).empty();
                    if(json.itemCount > 0) {
                        for (i = 0; i < json.itemCount; i++) {
                            commento = json.list[i];
                            $(cavialeBox).append('<div><div>' + commento.dish + '</div><div>' + commento.dub + '</div><div>' + commento.restaurant+'</div></div>')

                        }
                    }

                    $('#cavialeButton').attr("disabled", false);
                    $('#cavialeButton').text("Load caviale!");

                }).fail(function(xhr, status, error){
                    setMessage("Something wrong occurred");

                });

            }

        </script>
    </head>
    <body>
        <div id="message" style="display: none"><h1 id="messageField"></h1></div>
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

            <div>
<%--                Zipcode piu attivi (Con piu ordini piazzati) TOP 10 --%>
<%--                Group by zipcode and count su ordini --%>
                <button id="zipsButton" onclick="getZips()">Load most active zipcodes</button>
                <div id="bestZipcodeBox">

                </div>
            </div>

            <div>
<%--                Utenti piu attivi (Influencer) TOP 10 --%>
<%--                Da Neo4j --%>
                <button id="mostActiveButton" onclick="getMostActive()">Most active users</button>
                <div id="mostActiveBox">

                </div>
            </div>
            <div>
<%--                Ristoranti con guadagno maggiore nel mese --%>
<%--                Group by rid su Orders --%>
                <button id="profitButton" onclick="getProfits()">Load money grabbers</button>
                <div id="profitBox">

                </div>
            </div>
            <div>
<%--                Piatti piu cari (nome, prezzo, ristorante proprietario) --%>
                <button id="cavialeButton" onclick="getCaviale()">Load caviale!</button>
                <div id="cavialeBox">

                </div>
            </div>
        </div>
    </body>
</html>