<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page import="it.unipi.lsmsd.neo4food.constants.Constants" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.ListDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.DishDTO" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%
        ListDTO lista = (ListDTO) request.getAttribute("dishes");
        RestaurantDTO me = (RestaurantDTO) session.getAttribute(Constants.AUTHENTICATION_FIELD);
    %>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Management</title>
    <%@ include file="/WEB-INF/jsp/template/head_includes.jsp" %>
    <script type="text/javascript" src="<c:url value="/js/jquery-3.6.3.min.js"/>"></script>
    <script type="text/javascript">
        memory = {};
        modifyActive = false;

        toSend = {
            actor: "restaurant",
            action: "",
            rid: "<%= me.getId() %>",
            did: "",
            dname: "",
            ddesc: "",
            dprice: 0,
            dcurr: ""
        }

        function modifyConfirm(id){
            if(!modifyActive){
                modifyActive = true;
            //     Un bottone modifica e' stato cliccato
            //     Disattivo tutti i bottoni
                $('.modifyButtons').each(
                    function(){
                        $(this).attr("disabled", true);
                    }
                );

                buttonConfirm = "#modifyConfirm" + id;
                buttonRevert = "#revert" + id;
                namef = "#name" + id;
                pricef = "#price" + id;
                descf = "#desc" + id;

                // Riattivo quello della converma e del revert
                $(buttonConfirm).attr("disabled", false);
                $(buttonConfirm).text("Apply");
                $(buttonRevert).show();
            //     Salvo i campi
                memory.name = $(namef).val();
                memory.price = $(pricef).val();
                memory.desc = $(descf).val();

            //     Attivo i campi
                $(namef).attr("disabled", false);
                $(pricef).attr("disabled", false);
                $(descf).attr("disabled", false);
            //


            }
            else{
            //      L'unico bottone conferma e' stato cliccato
            //      Invio conferma modifica e ricarico la pagina

                toSend.action= "modDish";
                toSend.did = id;
                toSend.dname = $(namef).val();
                toSend.ddesc = $(descf).val();
                toSend.dprice = $(pricef).val();

                $.post("<c:url value="/personal"/>", toSend, function (result){
                    alert(result);
                    //     If ok remove its div to avoid page reload
                    revert(id, false);

                }).fail(function (xhr, status, error){
                    alert("Emotional damage!");

                    revert(id, true);
                });

            }
        }

        function revert(id, rev){
            if(modifyActive){
                modifyActive = false;

                buttonConfirm = "#modifyConfirm" + id;
                buttonRevert = "#revert" + id;
                namef = "#name" + id;
                pricef = "#price" + id;
                descf = "#desc" + id;

                $('.modifyButtons').each(
                    function(){
                        $(this).attr("disabled", false);
                    }
                );

                $(buttonConfirm).text("Modify");
                $(buttonRevert).hide();

                // Revert valori precedenti
                if(rev) {
                    $(namef).val(memory.name);
                    $(pricef).val(memory.price);
                    $(descf).val(memory.desc);
                }

                // disabilito quei campi
                $(namef).attr("disabled", true);
                $(pricef).attr("disabled", true);
                $(descf).attr("disabled", true);

            }
        }

        function remDish(id){
            toSend.action = "remDish";
            toSend.did = id;

            $.post("<c:url value="/personal"/>", toSend, function (result){
            //     If ok remove its div to avoid page reload

            $("#"+id).remove();

            }).fail(function (xhr, status, error){
                alert("Dish removed");
            });
        }

        function addDish()
        {
            toSend.action = "addDish";
            toSend.dname = $("#newName").val();
            toSend.dprice = $('#newPrice').val();
            toSend.dcurr = $('#newCurr option:selected').val();
            toSend.ddesc = $('#newDesc').val();

            $.post("<c:url value="/personal"/>", toSend, function (result){
                if(result != "") {
                    text = '' +
                        '<div id="' + result + '" class="relative mr-5 mt-8 ml-3 w-80 rounded-xl bg-principale px-5 py-3 text-center shadow-md">' +
                            '<textarea class="w-5/6 text-center font-bold resize-none bg-principale" id="name' + result + '" type="text" required disabled name="dname">' + toSend.dname + '</textarea>' +
                            '<input class="w-9/12 mt-2 bg-principale" id="price' + result + '" type="number" required disabled name="dprice" value="' + toSend.dprice + '"><a>' + toSend.dcurr + '</a>' +
                            '<textarea class="w-5/6 h-24 mt-4 resize-none bg-principale" id="desc' + result + '" type="text" disabled name="ddesc">' + toSend.ddesc + '</textarea>' +
                            '<div class="mt-3 float-right">' +
                                '<button class="modifyButtons border rounded-lg px-3 hover:bg-button" id="modifyConfirm' + result + '" type="button" onclick="modifyConfirm(\'' + result + '\')">Modify</button>' +
                                '<button class="border rounded-lg px-3 hover:bg-button" style="display:none;" id="revert' + result + '" type="button" onclick="revert(\'' + result + '\', true)">Revert</button>' +
                                '<button class="border rounded-lg px-3 hover:bg-button" id="remove' + result + '" type="button" onclick="remDish(\'' + result + '\')">Remove</button>' +
                            '</div>' +
                        '</div>';
                    $("#dishesBox").append(text);
                }

                closeAddDish();
            }).fail(function (xhr, status, error){
                alert("Don't be lazy");
            });

        }

        function openAddDish(){
            $("#newDishBox").show();
            $("body").css({"overflow": "hidden;"});
        }

        function closeAddDish(){
            $("#newDishBox").hide();
            $("body").css({"overflow": ""});

            $('#newName').val("");
            $('#newPrice').val("");
            $('#newCurr').val("");
            $('#newDesc').val("");
        }

    </script>

</head>
<body>
    <%@ include file="/WEB-INF/jsp/template/header.jsp" %>
    <div>
        <div class="text-center mt-10">
            <button class="border-2 rounded-lg w-5/6 h-7 bg-principale hover:bg-button" onclick="openAddDish()">Add new dish</button>
        </div>

    </div>

    <div id="dishesBox" class="flex flex-wrap justify-center">
<%--    Modifica piatti qui --%>
<%      if(lista.getItemCount() > 0)
        {
            List<DishDTO> dishes = lista.getList(); %>

<%--        Il ristorante ha dei piatti nel menu --%>
<%          for(DishDTO d : dishes)
            {
%>
<%--            Tutte le cazzatelle per modificare il piatto --%>

                <div id="<%= d.getId() %>" class="relative mr-5 mt-8 ml-3 w-80 rounded-xl bg-principale px-5 py-3 text-center shadow-md">
<%--                Name--%>
                    <textarea class="w-5/6 text-center font-bold resize-none bg-principale" id="name<%= d.getId() %>" type="text" required disabled name="dname"><%= d.getName() %></textarea>
<%--                Price--%>
                    <input class="w-9/12 mt-2 bg-principale" id="price<%= d.getId() %>" type="number" required disabled name="dprice" value="<%= d.getPrice() %>"><a><%= d.getCurrency() %></a>
<%--                Description--%>
                    <textarea class="w-5/6 h-24 mt-4 resize-none bg-principale"  id="desc<%= d.getId() %>" type="text" disabled name="ddesc"><%= d.getDescription() %></textarea>
<%--               Hidden things --%>

                    <div class="mt-3 float-right">
                        <button class="modifyButtons border-2 rounded-lg px-3 hover:bg-button" id="modifyConfirm<%=d.getId()%>" type="button" onclick="modifyConfirm('<%=d.getId()%>')">
                            Modify
                        </button>
                        <button class="border-2 rounded-lg px-3 hover:bg-button" style="display:none;" id="revert<%= d.getId() %>" type="button" onclick="revert('<%=d.getId()%>', true)">
                            Revert
                        </button>
                        <button class="border-2 rounded-lg px-3 hover:bg-button" id="remove<%= d.getId() %>" type="button" onclick="remDish('<%=d.getId()%>')">
                            Remove
                        </button>
                    </div>
                </div>
<%          }
%>
<%      }
        else
        {
%>
    <%--    Il ristorante non ha piatti nel menu --%>
            <h1> No dishes yet... Add some! :D</h1>
<%      }
%>
        <div id="newDishBox" style="display: none;" class="z-50 fixed h-full w-full bg-black bg-opacity-20 -my-16">
            <div class="relative mx-auto w-5/6 h-1/2 rounded-lg bg-principale py-3 shadow-md px-5 overflow-auto my-28">
                <%--            Add Reviews--%>
                <div id="boxAddDish" class="items-center mt-5">

                    <input class="rounded-md px-3 w-5/6 mt-4" id="newName" type="text" required placeholder="Dish name">
                    <input class="rounded-md px-3 w-8/12 mt-4 mr-8" id="newPrice" type="number" required placeholder="0.00">
                    <select id="newCurr">
                        <option value="USD" selected="selected">USD</option>
                        <option value="EUR">EUR</option>
                    </select>
                    <textarea class="h-34 rounded-md px-3 py-3 w-5/6 mt-4 resize-none" id="newDesc" type="text" placeholder="Description (Optional)"></textarea>

                    <button class="absolute bottom-5 right-7 border rounded-lg px-3 hover:bg-button" onclick="addDish()"> ADD</button>
                </div>
                <button class="absolute top-3 right-3 px-1 rounded-xl" onclick="closeAddDish()"><img class="h-7" src="img/x.png" alt="X"/></button>
            </div>
        </div>
    </div>


<%@ include file="/WEB-INF/jsp/template/footer.jsp" %>
</body>
</html>