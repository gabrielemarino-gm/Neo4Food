<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.UserDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.constants.Constants" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
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

            //Click pulsante modifica/conferma
            function modifyOrConfirm(){

                //Abilito i campi, il pulsante conferma e il pulsante revertChanges
                if(!confirm){
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

    <div class="relative m-3 flex justify-center -my-20">
        <form id="changeStuff" method="post" action="<c:url value="/personal"/>">
            <input type="hidden" name="action" value="change">
            <input type="hidden" name="uid" value="<%= userInfo.getId() %>">
            <div class="w-3/6 rounded-lg bg-principale text-center shadow-lg">
                <div class="border-gray-300 rounded-t-lg border-b bg-button py-3 px-6 font-bold">My Account <%= userInfo.getUsername() %></div>
                <div class="border-gray-300 text-gray-600 flex justify-between border-t py-3 px-2">
                    <div class=" text-sm flex justify-between">
                        <div class="font-bold">First Name:&nbsp</div>
                        <input required disabled class="boxette" id="fname" name="fname" value="<%= userInfo.getFirstName() %>">
                    </div>
                    <div class="flex justify-between text-sm">
                        <div class="font-bold">Last Name:&nbsp</div>
                        <input required disabled class="boxette" id="lname" name="lname" value="<%= userInfo.getLastName() %>">
                    </div>
                </div>
                <div class="border-gray-300 text-gray-600 flex justify-between border-t py-3 px-2">
                    <div class="flex justify-between text-sm">
                        <div class="font-bold">E-mail:&nbsp</div>
                        <%= userInfo.getEmail() %>
                    </div>
                    <div class="flex justify-between text-sm">
                        <div class="font-bold">Phone:&nbsp</div>
                        <input required disabled class="boxette" id="phone" name="phone" value="<%= userInfo.getPhoneNumber() %>">
                    </div>
                </div>
                <div class="border-gray-300 text-gray-600 flex justify-between border-t py-3 px-2">
                    <div class="flex justify-between text-sm">
                        <div class="font-bold">Address:&nbsp</div>
                        <input required disabled class="boxette" id="address" name="address" value="<%= userInfo.getAddress() %>">
                    </div>
                    <div class="flex justify-between text-sm">
                        <div class="font-bold">Zipcode:&nbsp</div>
                        <input required disabled class="boxette" id="zipcode" name="zipcode" value="<%= userInfo.getZipcode() %>">
                    </div>
                </div>
                <div class="border-gray-300 text-gray-600 flex justify-between border-t py-3 px-2">
                        <div class="flex justify-between text-sm">
                            <div class="font-bold">Payment method:&nbsp</div>
                            <input disabled class="boxette" id="pmethod" name="pmethod" value="<%= userInfo.getPaymentMethod() %>">
                        </div>
                        <div class="flex justify-between text-sm">
                            <div class="font-bold">Payment number:&nbsp</div>
                            <input disabled class="boxette" id="pnumber" name="pnumber" value="<%= userInfo.getPaymentNumber() %>">
                        </div>
                    </div>
            </div>
            <button id="changeConfirm" type="button" onclick="modifyOrConfirm()">Change</button>
            <button style="display: none" id="undo" type="button" onclick="revertChanges()">Revert</button>
        </form>
    </div>
    <%@include file="template/footer.jsp"%>
    </body>
</html>
