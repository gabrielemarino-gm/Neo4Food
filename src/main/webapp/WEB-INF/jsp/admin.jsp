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

            function setMessage(message)
            {
                messageTimeout = setTimeout(hideMessage, 5000);

                if (message === "Ratings Updated!" || message === "Prices Updated!")
                {
                    $('#message').show();
                    $('#messageField').css("color", "green");
                    $('#messageField').text(message);
                }
                else
                {
                    $('#message').show();
                    $('#messageField').css("color", "red");
                    $('#messageField').text(message);
                }
            }

            function hideMessage()
            {
                $('#message').hide();
                $('#messageField').text("");
            }


            function updateRatings()
            {
                toSend = {
                    action: "updateRatings"
                };

                $('#updateRatings').attr("disabled", true);
                $('#updateRatings').text("Wait please...");

                $.post("<c:url value="/admin"/>", toSend, function (result)
                {
                //  Do stuff on success
                    setMessage(result);
                    $('#updateRatings').attr("disabled",false);
                    $('#updateRatings').text("Update ratings");

                }).fail(function(xhr, status, error){
                    setMessage("Something wrong occurred");
                });
            }

            function updatePrices()
            {
                toSend = {
                    action: "updatePrices"
                };

                $('#updatePrices').attr("disabled", true);
                $('#updatePrices').text("Wait please...");

                $.post("<c:url value="/admin"/>", toSend, function (result)
                {
//                  Do stuff on success
                    setMessage(result);
                    $('#updatePrices').attr("disabled", false);
                    $('#updatePrices').text("Update prices");

                }).fail(function(xhr, status, error)
                {
                    setMessage("Something wrong occurred");
                });
            }


//     --------- Analytics zipcode ------------
            let zipBox = '#bestZipcodeBox';

            function getZips()
            {
                toSend = {
                  action: "analytics",
                  type: "zips"
                };

                $('#zipsButton').attr("disabled", true);
                $('#zipsButton').text("Wait please...");

                $.post("<c:url value="/admin"/>", toSend, function (result)
                {
                    json = JSON.parse(result);
                    //     Do stuff on success

                    $(zipBox).empty();
                    if(json.itemCount > 0)
                    {
                        for (i = 0; i < json.itemCount; i++)
                        {
                            obj = json.list[i];
                            i++;
                            $(zipBox).append('' +
                                '<div class="flex mt-1 w-full justify-center">' +
                                    '<div class="mr-2">' + i + '.' + '</div>' +
                                    '<div class="mr-7">' + obj.zipcode + '</div>' +
                                    '<div>Orders:&nbsp;' + obj.count + '</div>' +
                                '</div>'
                            );
                            i--;
                        }
                    }

                    $('#zipsButton').attr("disabled", false);
                    $('#zipsButton').text("Most active zipcodes");


                }).fail(function(xhr, status, error)
                {
                    setMessage("Something wrong occurred");
                });
            }

//     --------- Analytics mostActiveUsers ---------
            let mostActiveBox = '#mostActiveBox';

            function getMostActive()
            {
                toSend = {
                    action: "analytics",
                    type: "mostActive"
                };

                $('#mostActiveButton').attr("disabled", true);
                $('#mostActiveButton').text("Wait please...");

                $.post("<c:url value="/admin"/>", toSend, function (result)
                {
//                  Do stuff on success
                    json = JSON.parse(result);

                    $(mostActiveBox).empty();
                    if(json.itemCount > 0)
                    {
                        for (i = 0; i < json.itemCount; i++)
                        {
                            obj = json.list[i];
                            $(mostActiveBox).append('' +
                                '<div class="mt-5 text-center">' +
                                    '<div class="font-bold">' + obj.user + '</div>' +
                                    '<div>Done Reviews:&nbsp;' + obj.count + '</div>' +
                                    '<div>Avg Reviews:&nbsp;' + (parseFloat(obj.dub)).toFixed(2)+'</div>' +
                                '</div>'
                            );
                        }
                    }

                    $('#mostActiveButton').attr("disabled", false);
                    $('#mostActiveButton').text("Most active users");

                }).fail(function(xhr, status, error)
                {
                    setMessage("Something wrong occurred");
                });

            }

//  --------- Analytics guadagni ---------
            let profitBox = '#profitBox';
            function getProfits()
            {
                toSend = {
                    action: "analytics",
                    type: "profits"
                };

                $('#profitButton').attr("disabled", true);
                $('#profitButton').text("Wait please...");

                $.post("<c:url value="/admin"/>", toSend, function (result)
                {
                    //     Do stuff on success
                    json = JSON.parse(result);

                    $(profitBox).empty();
                    if(json.itemCount > 0)
                    {
                        for (i = 0; i < json.itemCount; i++)
                        {
                            obj = json.list[i];
                            $(profitBox).append('' +
                                '<div class="mt-5 text-center">' +
                                    '<div class="font-bold">' + obj.restaurant + '</div>' +
                                    '<div>Profit:&nbsp;' + obj.dub + '&nbsp;' + obj.currency + '</div>' +
                                '</div>'
                            );
                        }
                    }

                    $('#profitButton').attr("disabled", false);
                    $('#profitButton').text("Profits actual month");

                }).fail(function(xhr, status, error)
                {
                    setMessage("Something wrong occurred");
                });
            }

//  --------- Analytics caviale ---------
            let cavialeBox = '#cavialeBox';
            function getCaviale(){
                toSend = {
                    action: "analytics",
                    type: "caviale"
                };

                $('#cavialeButton').attr("disabled", true);
                $('#cavialeButton').text("Wait please...");

                $.post("<c:url value="/admin"/>", toSend, function (result)
                {
//                  Do stuff on success
                    json = JSON.parse(result);

                    $(cavialeBox).empty();
                    if(json.itemCount > 0)
                    {
                        for (i = 0; i < json.itemCount; i++)
                        {
                            obj = json.list[i];
                            $(cavialeBox).append('' +
                                '<div class="mt-5 text-center">' +
                                    '<div><p class="font-bold">Dish:&nbsp;</p>' + obj.dish + '</div>' +
                                    '<div><p class="font-bold">Price:&nbsp;</p>' + obj.dub + '&nbsp;' + obj.currency + '</div>' +
                                    '<div><p class="font-bold">Restaurant:&nbsp;</p>' + obj.restaurant+'</div>' +
                                '</div>'
                            )
                        }
                    }

                    $('#cavialeButton').attr("disabled", false);
                    $('#cavialeButton').text("Load caviale!");

                }).fail(function(xhr, status, error)
                {
                    setMessage("Something wrong occurred");
                });
            }

            //  --------- Analytics Delivery Time ---------
            let deliveryBox = '#deliveryBox';
            function getDeliveryTime()
            {
                let zipcode = $("#deliveryInputText").val();

                toSend = {
                    action: "analytics",
                    type: "deliveryTime",
                    zip: zipcode
                };

                $('#deliveryButton').attr("disabled", true);
                $('#deliveryButton').text("Wait please...");

                $.post("<c:url value="/admin"/>", toSend, function (result)
                {
//                  Do stuff on success
                    json = JSON.parse(result);

                    $(deliveryBox).empty();
                    if(json.itemCount > 0)
                    {
                        for (i = 0; i < json.itemCount; i++)
                        {
                            obj = json.list[i];
                            $(deliveryBox).append('' +
                                '<div class="mt-5 text-center">' +
                                    '<div><p class="font-bold">' + obj.restaurant + '</p></div>' +
                                    '<div>' + (parseFloat(obj.dub)).toFixed(2) + '&nbsp;Min</div>' +
                                '</div>'
                            )
                        }
                    }

                    $('#deliveryButton').attr("disabled", false);
                    $('#deliveryButton').text("Best Delivery Time");

                }).fail(function(xhr, status, error)
                {
                    setMessage("Something wrong occurred");
                });
            }
        </script>
    </head>
    <body>
        <header class="bg-principale px-5 h-12 font-bold text-1xs shadow-md">
            <a href="<c:url value="/ricerca"/>"><img class="h-12 float-left" src="img/logo_2.png" alt="logo"></a>
            <div class="my-3 px-3 float-right rounded-lg hover:bg-button">
                Admin
            </div>
        </header>

        <div class="fixed text-center w-full mx-auto mt-2 bg-button shadow-md bg-button" id="message" style="display: none;">
            <h1 id="messageField"></h1>
        </div>

        <div class="flex justify-center mx-auto mt-10">
            <div class="bg-principale rounded-md shadow-md w-1/4 text-center py-4">
                <h1 class="font-bold text-lg">Total users: </h1>
                <h1 class="text-lg"><%= (long)request.getAttribute("uCount") %></h1>
            </div>
            <div class="w-10"></div>
            <div class="bg-principale rounded-md shadow-md w-1/4 text-center py-4">
                <h1 class="font-bold text-lg">Total restaurants: </h1>
                <h1 class="text-lg"><%= (long)request.getAttribute("rCount") %></h1>
            </div>
            <div class="w-10"></div>
            <div class="bg-principale rounded-md shadow-md w-1/4 text-center py-4">
                <h1 class="font-bold text-lg">Total orders: </h1>
                <h1 class="text-lg"><%= (long)request.getAttribute("oCount") %></h1>
            </div>
        </div>

        <h1 class="px-5 mt-8 text-xl font-bold text-center">WARNING: Updates may take several minutes, be careful!</h1>
        <div class="px-5 mt-5 flex justify-center" id="boxUpdate">
            <div class="w-2"></div>
            <button class="bg-principale rounded-md px-3 border-2 hover:bg-button" id="updateRatings" onclick="updateRatings()">Update ratings</button>
            <div class="w-16"></div>
            <button class="bg-principale rounded-md px-3 border-2 hover:bg-button" id="updatePrices" onclick="updatePrices()">Update prices</button>
            <div class="w-2"></div>
        </div>


        <div class="px-5 mt-5 flex justify-center">
<%--        Altre statistiche per admin --%>
            <div class="w-1/4 px-3 rounded-md shadow-md h-72 overflow-auto">
<%--            Zipcode piu attivi (Con piu ordini piazzati) TOP 10 --%>
<%--            Group by zipcode and count su ordini --%>
                <div class="flex justify-center w-full">
                    <button class="bg-principale rounded-md px-3 border-2 hover:bg-button" id="zipsButton" onclick="getZips()">Most active zipcodes</button>
                </div>
                <div id="bestZipcodeBox"></div>
            </div>

            <div class="w-1/4 px-3 rounded-md shadow-md h-72 overflow-auto">
<%--            Utenti piu attivi (Influencer) TOP 10 --%>
<%--            Da Neo4j --%>
                <div class="flex justify-center w-full">
                    <button class="bg-principale rounded-md px-3 border-2 hover:bg-button" id="mostActiveButton" onclick="getMostActive()">Most active users</button>
                </div>
                <div id="mostActiveBox"></div>
            </div>

            <div class="w-1/4 px-3 rounded-md shadow-md h-72 overflow-auto">
<%--            Ristoranti con guadagno maggiore nel mese --%>
<%--            Group by rid su Orders --%>
                <div class="flex justify-center w-full">
                    <button class="bg-principale rounded-md px-3 border-2 hover:bg-button" id="profitButton" onclick="getProfits()">Profits actual month</button>
                </div>
                <div id="profitBox"></div>
            </div>

            <div class="w-1/4 px-3 rounded-md shadow-md h-72 overflow-auto">
<%--            Piatti piu cari (nome, prezzo, ristorante proprietario) --%>
                <div class="flex justify-center w-full">
                   <button class="bg-principale rounded-md px-3 border-2 hover:bg-button" id="cavialeButton" onclick="getCaviale()">Load caviale!</button>
                </div>
                <div id="cavialeBox"></div>
            </div>


        </div>


        <div class="px-5 mt-5 flex justify-center">
            <div class="w-1/4 px-3 rounded-md shadow-md h-72 overflow-auto">
                <div class="flex justify-center w-full">
                    <input id="deliveryInputText" class="rounded-md px-3 w-1/2 text-center" type="text" placeholder="Zip Code">
                </div>
                <div class="flex justify-center w-full">
                    <button class="mt-3 bg-principale rounded-md px-3 border-2 hover:bg-button" id="deliveryButton" onclick="getDeliveryTime()">Best Delivery Time</button>
                </div>
                <div id="deliveryBox"></div>
            </div>
        </div>


    </body>
</html>