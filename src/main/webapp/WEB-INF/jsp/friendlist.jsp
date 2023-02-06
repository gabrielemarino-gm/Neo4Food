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
    <%UserDTO userDTO = (UserDTO) request.getAttribute("userDTO");;%>
    <script type="text/javascript" src="<c:url value="/js/jquery-3.6.3.min.js"/>"></script>
    <script type="text/javascript">
        toSend={
            action: "getRecommendation",
            username: "<%= userDTO.getUsername() %>",
        }
            function getRecommendationRequest() {
            $.post("<c:url value='/social'/>", toSend, function (result){
                json = JSON.parse(result);

                for(i = 0; i<json.list.length; i++) {
                    var consigliato = json.list[i];
                    console.log(consigliato)

                    $("#boxRec").append('<div><div>Username:' + consigliato.username + '</div><div>Followers:' + consigliato.nfollowers + '</div> </div>');
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
    </script>

</head>
<body>
<%@ include file="template/header.jsp"%>
<% ListDTO<UserDTO> listDTO = (ListDTO<UserDTO>) request.getAttribute("listDTO");

    List<UserDTO> list = listDTO.getList();

            ;%>

    <div class="py-8">
<%  for (UserDTO item: list)
    {
%>
        <div class="mx-auto bg-principale rounded-md w-5/6 flex px-5 py-6">
            <div>"<%=item.getUsername()%>" </div>
            <button class="ml-auto px-3 rounded-lg border-2 hover:bg-button" onclick="removeFollow('<%=item.getUsername()%>')">Remove follow</button>
        </div>
<%  }
%>

    </div>
    <button onclick="getRecommendationRequest()" class="mx-auto flex px-96 mt-4 text-center rounded-lg border-2 hover:bg-button">Get Recommendations</button>

    <div id="boxRec">
    </div>


<%@ include file="template/footer.jsp"%>
</body>
</html>