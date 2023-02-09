<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.ListDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.constants.Constants" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.*" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unipi.lsmsd.neo4food.model.User" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Research</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Restaurants List</title>
    <%@ include file="/WEB-INF/jsp/template/head_includes.jsp" %>
    <%UserDTO userDTO = (UserDTO) request.getAttribute("userDTO");%>
    <script type="text/javascript" src="<c:url value="/js/jquery-3.6.3.min.js"/>"></script>
    <script type="text/javascript">
        toSend={
            action: "getRecommendationByFollow",
            username: "<%= userDTO.getUsername() %>",
        }
        page=0

        function nextPage(){
            page+=1
            return page
        }
        function previousPage(){
            if (page>0) {
                page -= 1
            }
            return page
        }

        toSend4={
            action: "setFollow",
            username: "<%= userDTO.getUsername() %>",
            username2: ""
        }


            function getRecommendationByFollowRequest() {

            $.post("<c:url value='/social'/>", toSend, function (result){
                json = JSON.parse(result);

                $("#boxRec").empty();

                if(!json || !json.list.length){

                    $("#boxRec").append("<div>There is nothing to display based on your followers.</div>");
                }else {
                    for (i = 0; i < json.list.length; i++) {
                        var consigliato = json.list[i];
                        console.log(consigliato)

                        $("#boxRec").append("<div><div>Username:" + consigliato.username + "</div><div>Followers:" + consigliato.nfollowers + "</div><div>" +
                            "<Button onclick='setFollow(\"" + consigliato.username + "\")' > FOLLOW" + "</Button>" + " </div>");

                    }
                }
            })
        .fail(function (xhr, status, error){
            alert(xhr+"\n"+status+"\n"+error);
        });
        }


        toSend3={
            action: "getRecommendationByRestaurant",
            username: "<%= userDTO.getUsername() %>",
        }
        function getRecommendationByRestaurantRequest() {
            $.post("<c:url value='/social'/>", toSend3, function (result){
                json = JSON.parse(result);
                $("#boxRec").empty();
                if(!json || !json.list.length){
                    $("#boxRec").append("<div>There is nothing to display based on your rated restaurant.</div>");
                }else {
                for(i = 0; i<json.list.length; i++) {
                    var consigliato = json.list[i];

                        $("#boxRec").append("<div><div>Username:" + consigliato.username + "</div><div>Followers:" + consigliato.nfollowers + "</div><div><Button onclick='setFollow(\"" + consigliato.username + "\")' > FOLLOW" + "</Button>" + " </div>");
                    }
                    }

            })
                .fail(function (xhr, status, error){
                    alert(xhr+"\n"+status+"\n"+error);
                });
        }

        toSend2={
            action: "removeFollow",
            username: "<%= userDTO.getUsername() %>",
            username2: ""
        }

        function removeFollow(username) {
          toSend2.username2 = username

            $.post("<c:url value='/social'/>", toSend2, function (result){
                json = JSON.parse(result);
            })
                .fail(function (xhr, status, error){
                    alert(xhr+"\n"+status+"\n"+error);
                });
        }

        toSend5={
            action: "getInfluencer",
        }
        function getInfluencer() {
            $.post("<c:url value='/social'/>", toSend5, function (result){

                json = JSON.parse(result);
                $("#boxRec").empty();
                if(!json || !Object.keys(json).length){
                    $("#boxRec").append("<div>There is nothing to display</div>");
                }else {
                    for (i = 0; i < json.list.length; i++) {
                        var consigliato = json.list[i];

                        $("#boxRec").append("<div ><div>Username:" + consigliato.username + "</div><div>Followers:" + consigliato.nfollowers + "</div><div><Button onclick='setFollow(\"" + consigliato.username + "\")' > FOLLOW" + "</Button>" + " </div>");
                    }
                }
            })
                .fail(function (xhr, status, error){
                    alert(xhr+"\n"+status+"\n"+error);
                });
        }

        toSend6={
            action: "getFollowersNextPage",
            username:"<%= userDTO.getUsername() %>",
            page,
        }
        function getFollowersNext() {
            page=page+1
            toSend6.page=page;
            $.post("<c:url value='/social'/>", toSend6, function (result){
                console.log(result)
                json = JSON.parse(result);

                $("#boxFollow").empty();
                if(!json || !Object.keys(json).length){
                    $("#boxFollow").empty();
                    $("#boxFollow").append("<div>There is nothing to display</div>");
                }else {
                    $("#boxFollow").empty();
                    for (i = 0; i < json.list.length; i++) {
                        var follower = json.list[i];

                        $("#boxFollow").append("<div class='mx-auto bg-principale rounded-md w-5/6 flex px-5 py-6' ><div>Username:" + follower.username + "</div><button class='ml-auto px-3 rounded-lg border-2 hover:bg-button' onclick='removeFollow(\"" + follower.username + "\")' > REMOVE FOLLOW" + "</button>" + " </div>");
                    }
                }


            })
                .fail(function (xhr, status, error){
                    alert(xhr+"\n"+status+"\n"+error);
                });
        }

        function getFollowersPrevious() {
            if (page > 0) {
                page = page - 1
                toSend6.page = page;
                $.post("<c:url value='/social'/>", toSend6, function (result) {
                    console.log(result)
                    json = JSON.parse(result);

                    $("#boxFollow").empty();
                    if (!json || !Object.keys(json).length) {
                        $("#boxFollow").empty();
                        $("#boxFollow").append("<div>There is nothing to display</div>");
                    } else {
                        $("#boxFollow").empty();
                        for (i = 0; i < json.list.length; i++) {
                            var follower = json.list[i];

                            $("#boxFollow").append("<div class='mx-auto bg-principale rounded-md w-5/6 flex px-5 py-6' ><div>Username:" + follower.username + "</div><button class='ml-auto px-3 rounded-lg border-2 hover:bg-button' onclick='removeFollow(\"" + follower.username + "\")' > REMOVE FOLLOW" + "</button>" + " </div>");
                        }
                    }


                })
                    .fail(function (xhr, status, error) {
                        alert(xhr + "\n" + status + "\n" + error);
                    });
            }
        }

    </script>

</head>
<body>
<%@ include file="template/header.jsp"%>

<div id="box"></div>
<% ListDTO<UserDTO> listDTO = (ListDTO<UserDTO>) request.getAttribute("listDTO");

    List<UserDTO> list = listDTO.getList();
            ;%>

<div id="boxFollow">
<%
for (UserDTO item: list)

{  ;%>

<div class="mx-auto bg-principale rounded-md w-5/6 flex px-5 py-6" >
<div>"<%=item.getUsername()%>" </div>
<button class="ml-auto px-3 rounded-lg border-2 hover:bg-button" onclick="removeFollow('<%=item.getUsername()%>')">Remove follow</button>
</div>

<%}%>
</div>
<%if(!list.isEmpty()){%>

<%}%>
</div>
<button  onclick="getFollowersPrevious()" ><---PreviousPage</button>
<button  onclick="getFollowersNext()" >NextPage---></button>
<div>
    <button onclick="getRecommendationByFollowRequest()" class="mx-auto flex px-96 mt-4 text-center rounded-lg border-2 hover:bg-button">Get Recommendations By User</button>
    <button onclick="getRecommendationByRestaurantRequest()" class="mx-auto flex px-96 mt-4 text-center rounded-lg border-2 hover:bg-button">Get Recommendations By Restaurant</button>
    <button onclick="getInfluencer()" class="mx-auto flex px-96 mt-4 text-center rounded-lg border-2 hover:bg-button">Get Influencer</button>
</div>



<div class="mx-auto bg-principale rounded-md w-5/6 flex px-5 py-6">
    <div id="boxRec">
    </div>

</div>



<%@ include file="template/footer.jsp"%>
</body>
</html>
