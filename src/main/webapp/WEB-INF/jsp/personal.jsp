<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.UserDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.constants.Constants" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Personal Page</title>
        <%@ include file="/WEB-INF/jsp/template/head_includes.jsp" %>
    </head>

    <body>
    <%@ include file="template/header.jsp"%>
    <%
        UserDTO userInfo = (UserDTO) request.getSession().getAttribute(Constants.AUTHENTICATION_FIELD);
    %>
        <script type="text/javascript" src="<c:url value="/js/jquery-3.6.3.min.js"/>"></script>
        <script type="text/javascript">
            let originalFirstName = "<%= userInfo.getFirstName() %>";
            let originalLastName = "<%= userInfo.getLastName() %>";
            let originalPhone = "<%= userInfo.getPhoneNumber() %>";
            let originalAddress = "<%= userInfo.getAddress() %>";
            let originalZipcode = "<%= userInfo.getZipcode() %>";
            let originalPMethod = "<%= userInfo.getPaymentMethod() %>";
            let originalPNumber = "<%= userInfo.getPaymentNumber() %>";

            let confirm = false;

            // Click pulsante modifica/conferma
            function modifyOrConfirm(){

                //Abilito i campi, il pulsante conferma e il pulsante revertChanges
                if(!confirm)
                {
                    confirm = true;
                    $('#changeConfirm').text("Confirm");
                    $('#undo').show();
                //    Cambiare stile bottone

                //    ----------------------
                //    Attivo tutte le box
                    $('.boxette').each(function (){
                       $(this).prop("disabled", false);
                    });
                //    Cambia stile box

                //    ----------------------
                //    Rendo visibile bottone di revert
                }
                else
                //Invio il form di modifica
                {
                    changeStuff.submit();
                }
            }

            function revertChanges(){
                confirm = false;
                $('#undo').hide();
                //Rendo di nuovo invisibile il bottone di revert
                $('#changeConfirm').text("Change");
            //    Cambia stile bottone

            //    --------------------
            //    Cambia stile boxette

            //    --------------------
                $('#fname').val(originalFirstName);
                $('#lname').val(originalLastName);
                $('#phone').val(originalPhone);
                $('#address').val(originalAddress);
                $('#zipcode').val(originalZipcode);
                $('#pmethod').val(originalPMethod);
                $('#pnumber').val(originalPNumber);

                $('.boxette').each(function (){
                    $(this).prop("disabled", true);
                });

            }

        </script>

    <div class="z-40 h-48 overflow-hidden w-full" style="top">
        <img class="w-full blur-md" src="https://ilfattoalimentare.it/wp-content/uploads/2017/06/junk-food-hamburger-patatine-fast-food-pizza-dolci-Fotolia_130389179_Subscription_Monthly_M.jpg" alt="imgFood" />
    </div>

    <div class="relative w-auto flex justify-center -my-20">
        <form id="changeStuff" method="post" action="<c:url value="/personal"/>">
            <input type="hidden" name="action" value="change">
            <input type="hidden" name="actor" value="user">
            <input type="hidden" name="uid" value="<%= userInfo.getId() %>">
            <div class="rounded-lg bg-principale text-center shadow-lg">

                <div class="border-gray-300 rounded-t-lg bg-button py-3 px-6 font-bold">
                    My Account <%= userInfo.getUsername() %><br>
                </div>

                <div class="border-gray-300 text-gray-600 flex flex-wrap border-t py-3 px-2">
                    <div class=" text-sm flex ">
                        <div class="font-bold">First Name:&nbsp</div>
                        <input class="w-56 boxette" required disabled id="fname" name="fname" value="<%= userInfo.getFirstName() %>">
                    </div>
                    <div class="flex text-sm ml-10">
                        <div class="font-bold">Last Name:&nbsp</div>
                        <input class="w-56 boxette" required disabled id="lname" name="lname" value="<%= userInfo.getLastName() %>">
                    </div>
                </div>

                <div class="border-gray-300 text-gray-600 flex flex-wrap border-t py-3 px-2">
                    <div class="flex  text-sm">
                        <div class="font-bold">E-mail:&nbsp</div>
                        <div class="w-56 text-left" ><%= userInfo.getEmail() %></div>
                    </div>
                    <div class="flex text-sm ml-17">
                        <div class="font-bold">Phone:&nbsp</div>
                        <input class="w-56 boxette" required disabled  id="phone" name="phone" value="<%= userInfo.getPhoneNumber() %>">
                    </div>
                </div>

                <div class="border-gray-300 text-gray-600 flex flex-wrap border-t py-3 px-2">
                    <div class="flex  text-sm">
                        <div class="font-bold">Address:&nbsp</div>
                        <input class="w-56 boxette" required disabled  id="address" name="address" value="<%= userInfo.getAddress() %>">
                    </div>
                    <div class="flex text-sm ml-16">
                        <div class="font-bold">Zipcode:&nbsp</div>
                        <input class="w-56 boxette" required disabled  id="zipcode" name="zipcode" value="<%= userInfo.getZipcode() %>">
                    </div>
                </div>

                <div class="border-gray-300 text-gray-600 flex flex-wrap border-t py-3 px-2">
                    <div class="flex  text-sm">
                        <div class="font-bold">Payment method:&nbsp</div>
                        <input class="w-56 boxette" disabled  id="pmethod" name="pmethod" value="<%= userInfo.getPaymentMethod() %>">
                    </div>
                    <div class="flex  text-sm">
                        <div class="font-bold">Payment number:&nbsp</div>
                        <input class="w-56 boxette" disabled  id="pnumber" name="pnumber" value="<%= userInfo.getPaymentNumber() %>">
                    </div>
                </div>

                <button class="relative bottom-2 float-right ml-2 mt-4 w-24 rounded-lg border-2 hover:bg-button" type="button" onclick="modifyOrConfirm()">Change</button>
                <button  class="relative bottom-2 float-right mt-4 w-24 rounded-lg border-2 hover:bg-button" style="display: none" id="undo" type="button" onclick="revertChanges()">Revert</button>


            </div>
<%              if(request.getAttribute("message") != null)
                {
                    if (request.getAttribute("message").equals("Updated successfully"))
                    {
%>
                        <div class="text-center my-2 ml-20" style="color: green;">
                            <%= (String) request.getAttribute("message") %>
                        </div>
<%                  }
                    else
                    {
%>
                        <div class="text-center my-2 ml-20" style="color: red;">
                            <%= (String) request.getAttribute("message") %>
                        </div>
<%                  }
                }
%>
        </form>

    </div>
    <div class="h-20"></div>
    <%@include file="template/footer.jsp"%>
    </body>
</html>
