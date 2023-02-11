<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.ListDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.constants.Constants" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Friend List</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <%@ include file="/WEB-INF/jsp/template/head_includes.jsp" %>
    <% UserDTO userDTO = (UserDTO) session.getAttribute(Constants.AUTHENTICATION_FIELD); %>

    <script type="text/javascript" src="<c:url value="/js/jquery-3.6.3.min.js"/>"></script>
    <script type="text/javascript">

        let page=0;

        function nextPage()
        {
            page += 1;
            return page;
        }

        function previousPage()
        {
            if (page > 0)
                page -= 1;
            return page;
        }

        function getRecommendationByFollowRequest()
        {
            let toSend = {
                action: "getRecommendationByFollow",
                username: "<%= userDTO.getUsername() %>"
            };

            $("#boxRec").show();
            $.post("<c:url value='/social'/>", toSend, function (result)
            {
                json = JSON.parse(result);

                $("#boxRec").empty();

                if(!json || !json.list.length)
                {
                    $("#boxRec").append('' +
                        '<div class="mt-5 bg-principale rounded-md w-5/6 px-5 py-6 mx-auto shadow-md text-center">' +
                            '<div>There is nothing to display here.</div>' +
                        '</div>'
                    );
                }
                else
                {
                    for (i = 0; i < json.list.length; i++)
                    {
                        var consigliato = json.list[i];

                        $("#boxRec").append('' +
                            '<div class="mt-5 bg-principale rounded-md w-5/6 px-5 py-6 mx-auto shadow-md flex">' +
                                '<div>' +
                                    '<div>' + consigliato.username + '</div>' +
                                    '<div class="px-5 text-xs">' + consigliato.nfollowers + ' follower</div>' +
                                '</div>' +
                                '<button class="ml-auto px-3 rounded-lg border-2 hover:bg-button" onclick="setFollow(\''+ consigliato.username + '\')"> Follow </button>' +
                            '</div>'
                        );
                    }
                }
            }).fail(function (xhr, status, error)
            {
                alert(xhr+"\n"+status+"\n"+error);
            });
        }

        function getRecommendationByRestaurantRequest()
        {
            let toSend={
                action: "getRecommendationByRestaurant",
                username: "<%= userDTO.getUsername() %>"
            };

            $("#boxRec").show();
            $.post("<c:url value='/social'/>", toSend, function (result)
            {
                json = JSON.parse(result);
                $("#boxRec").empty();
                if(!json || !json.list.length)
                {
                    $("#boxRec").append('' +
                        '<div class="mt-5 bg-principale rounded-md w-5/6 px-5 py-6 mx-auto shadow-md text-center">' +
                            '<div>There is nothing to display here.</div>' +
                        '</div>'
                    );
                }
                else
                {
                    for(i = 0; i<json.list.length; i++)
                    {
                        var consigliato = json.list[i];

                        $("#boxRec").append('' +
                            '<div class="mt-5 bg-principale rounded-md w-5/6 px-5 py-6 mx-auto shadow-md flex">' +
                                '<div>' +
                                    '<div>' + consigliato.username + '</div>' +
                                    '<div class="px-5 text-xs">' + consigliato.nfollowers + ' follower</div>' +
                                '</div>' +
                                '<button class="ml-auto px-3 rounded-lg border-2 hover:bg-button" onclick="setFollow(\''+ consigliato.username + '\')"> Follow </button>' +
                            '</div>'
                        );
                    }
                }

            }).fail(function (xhr, status, error)
            {
                    alert(xhr+"\n"+status+"\n"+error);
            });
        }

        function getInfluencer()
        {
            toSend={
                action: "getInfluencer"
            };

            $("#boxRec").show();
            $.post("<c:url value='/social'/>", toSend, function (result)
            {
                json = JSON.parse(result);
                $("#boxRec").empty();

                if(!json || !Object.keys(json).length)
                {
                    $("#boxRec").append('' +
                        '<div class="mt-5 bg-principale rounded-md w-5/6 px-5 py-6 mx-auto shadow-md text-center">' +
                            '<div>There is nothing to display here.</div>' +
                        '</div>'
                    );
                }
                else
                {
                    for (i = 0; i < json.list.length; i++)
                    {
                        var consigliato = json.list[i];

                        $("#boxRec").append('' +
                            '<div class="mt-5 bg-principale rounded-md w-5/6 px-5 py-6 mx-auto shadow-md flex">' +
                                '<div>' +
                                    '<div>' + consigliato.username + '</div>' +
                                    '<div class="px-5 text-xs">' + consigliato.nfollowers + ' follower</div>' +
                                '</div>' +
                                '<button class="ml-auto px-3 rounded-lg border-2 hover:bg-button" onclick="setFollow(\''+ consigliato.username + '\')"> Follow </button>' +
                            '</div>'
                        );

                    }
                }
            }).fail(function (xhr, status, error)
            {
                alert(xhr+"\n"+status+"\n"+error);
            });
        }

        function removeFollow(username, idDiv)
        {
            let toSend = {
                "action": "removeFollow",
                "actor": "<%= userDTO.getUsername() %>",
                "target": username
            };

            $.post("<c:url value='/social'/>", toSend, function (result)
            {
                $("#div"+username).remove();

            }).fail(function (xhr, status, error)
            {
                alert(xhr+"\n"+status+"\n"+error);
            });
        }

        function setFollow(username)
        {
//          Se si sta aggiungendo un follower che è già fra la lista dei seguiti non lo aggiungo
            if ($('#div'+username).length) // IF EXISTS (-cit Pistolesi)
                return;

            let toSend = {
                "action": "setFollow",
                "actor": "<%= userDTO.getUsername() %>",
                "target": username
            };

            $.post("<c:url value='/social'/>", toSend, function (result)
            {
                $("#boxFollow").append('' +
                    '<div id="div'+ username +'" class="mx-auto bg-principale rounded-md w-5/6 flex px-5 py-6 shadow-md mt-3">' +
                        '<div>' + username + '</div>' +
                        '<button class="ml-auto px-3 rounded-lg border-2 hover:bg-button" onclick="removeFollow(\''+username+'\')"> Remove follow </button>' +
                    '</div>'
                );

            }).fail(function (xhr, status, error)
            {
                alert(xhr+"\n"+status+"\n"+error);
            });
        }

        function getFollowersNext()
        {
            page = page + 1;

            let toSend = {
                action: "getFollowersNextPage",
                username:"<%= userDTO.getUsername() %>",
                page: page,
            }

            $.post("<c:url value='/social'/>", toSend, function (result)
            {
                json = JSON.parse(result);

                $("#boxFollow").empty();
                if(!json || !Object.keys(json).length)
                {
                    $("#boxFollow").empty();
                    $("#boxFollow").append("<div>There is nothing to display</div>");
                }
                else
                {
                    $("#boxFollow").empty();
                    for (i = 0; i < json.list.length; i++)
                    {
                        var follower = json.list[i];

                        $("#boxFollow").append('' +
                            '<div id="div'+ follower.username +'" class="mx-auto bg-principale rounded-md w-5/6 flex px-5 py-6 shadow-md mt-3">' +
                            '<div>' + follower.username + '</div>' +
                                '<button class="ml-auto px-3 rounded-lg border-2 hover:bg-button" onclick="removeFollow(\''+follower.username+'\')"> Remove follow </button>' +
                            '</div>'
                        );
                    }
                }
            }).fail(function (xhr, status, error)
            {
                    alert(xhr+"\n"+status+"\n"+error);
            });
        }

        function getFollowersPrevious()
        {
            if (page > 0)
            {
                page = page - 1;

                let  toSend={
                    action: "getFollowersNextPage",
                    username:"<%= userDTO.getUsername() %>",
                    page: page,
                }

                $.post("<c:url value='/social'/>", toSend, function (result)
                {

                    json = JSON.parse(result);

//                  Elimino tutto il contenuto del div boxFollow per poi riempirlo con la pagina successiva
                    $("#boxFollow").empty();
                    if (!json || !Object.keys(json).length)
                    {
                        $("#boxFollow").empty();
                        $("#boxFollow").append("<div>There is nothing to display</div>");
                    }
                    else
                    {
                        $("#boxFollow").empty();
                        for (i = 0; i < json.list.length; i++)
                        {
                            var follower = json.list[i];
                            $("#boxFollow").append('' +
                                '<div id="div'+follower.username+'" class="mx-auto bg-principale rounded-md w-5/6 flex px-5 py-6 shadow-md mt-3">' +
                                    '<div>' + follower.username + '</div>' +
                                    '<button class="ml-auto px-3 rounded-lg border-2 hover:bg-button" onclick="removeFollow(\'' + follower.username + '\')"> Remove follow </button>' +
                                ' </div>'
                            );
                        }
                    }

                }).fail(function (xhr, status, error)
                {
                    alert(xhr + "\n" + status + "\n" + error);
                });
            }
        }

    </script>

</head>
<body>
    <%@ include file="template/header.jsp"%>

    <div id="box">

    </div>
<%      ListDTO<UserDTO> listDTO = (ListDTO<UserDTO>) request.getAttribute("listDTO");
        List<UserDTO> list = listDTO.getList();
%>


    <div class="my-10">
        <div id="boxFollow">
<!--        STAMPO LA LISTA DEI FOLLOWER-->
<%          if(!list.isEmpty())
            {
                for (UserDTO item: list)
                {
%>
                    <div id="div<%=item.getUsername()%>" class="mx-auto bg-principale rounded-md w-5/6 flex px-5 py-6 shadow-md mt-3">
                        <div><%=item.getUsername()%></div>
                        <button class="ml-auto px-3 rounded-lg border-2 hover:bg-button" onclick="removeFollow('<%=item.getUsername()%>', '#div<%=item.getUsername()%>')">Remove follow</button>
                    </div>
<%              }
            }
%>
        </div>

<!--    BOTTONI PER ANDARE AVANTI NELLE PAGINE-->
        <div class="mt-3 flex justify-center">
            <button  onclick="getFollowersPrevious()"><img class="h-7" src="img/left_arrow.png" alt="prec"></button>
            <div class="w-9/12"></div>
            <button  onclick="getFollowersNext()"><img class="h-7" src="img/right_arrow.png" alt="succ"></button>
        </div>
    </div>

    <div class="flex justify-center w-5/6 mx-auto mt-2">
        <div class="w-2/12"></div>
        <button class="mx-auto px-3 text-center rounded-lg border-2 hover:bg-button" onclick="getRecommendationByFollowRequest()" >Get Recommendations By User</button>
        <button class="mx-auto px-3 text-center rounded-lg border-2 hover:bg-button" onclick="getRecommendationByRestaurantRequest()" >Get Recommendations By Restaurant</button>
        <button class="mx-auto px-3 text-center rounded-lg border-2 hover:bg-button" onclick="getInfluencer()">Get Influencer</button>
        <div class="w-2/12"></div>
    </div>

    <div class="mt-10"  id="boxRec">

    </div>

<%@ include file="template/footer.jsp"%>
</body>
</html>
