<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.UserDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.constants.Constants" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
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

        </head>

            function revertChanges(){
                confirm = true;

            }

        </script>

    <div class="z-40 h-48 overflow-hidden w-full" style="top">
        <img class="w-full blur-md" src="https://ilfattoalimentare.it/wp-content/uploads/2017/06/junk-food-hamburger-patatine-fast-food-pizza-dolci-Fotolia_130389179_Subscription_Monthly_M.jpg" alt="imgFood" />
    </div>

    <div class="relative m-3 flex justify-center -my-20">
        <form id="changeStuff" action="<c:url value="/personal"/>">
            <input type="hidden" name="action" value="change">
            <input type="hidden" name="uid" value="<%= userInfo.getId()%>">
            <div class="w-3/6 rounded-lg bg-principale text-center shadow-lg">
                <div class="border-gray-300 rounded-t-lg border-b bg-button py-3 px-6 font-bold">My Account <%= userInfo.getUsername() %></div>
                <div class="border-gray-300 text-gray-600 flex justify-between border-t py-3 px-2">
                    <div class=" text-sm flex justify-between">
                        <div class="font-bold">First Name:&nbsp</div>
                        <input required disabled class="boxes" name="fname" value="<%= userInfo.getFirstName() %>">
                    </div>
                    <div class="flex justify-between text-sm">
                        <div class="font-bold">Last Name:&nbsp</div>
                        <input required disabled class="boxes" name="lname" value="<%= userInfo.getLastName() %>">
                    </div>
                </div>
                <div class="border-gray-300 text-gray-600 flex justify-between border-t py-3 px-2">
                    <div class="flex justify-between text-sm">
                        <div class="font-bold">E-mail:&nbsp</div>
                        <%= userInfo.getEmail() %>
                    </div>
                    <div class="flex justify-between text-sm">
                        <div class="font-bold">Phone:&nbsp</div>
                        <input required disabled class="boxes" name="phone" value="<%= userInfo.getPhoneNumber() %>">
                    </div>
                </div>
                <div class="border-gray-300 text-gray-600 flex justify-between border-t py-3 px-2">
                    <div class="flex justify-between text-sm">
                        <div class="font-bold">Address:&nbsp</div>
                        <input required disabled class="boxes" name="address" value="<%= userInfo.getAddress() %>">
                    </div>
                    <div class="flex justify-between text-sm">
                        <div class="font-bold">Zipcode:&nbsp</div>
                        <input required disabled class="boxes" name="zipcode" value="<%= userInfo.getZipcode() %>">
                    </div>
                </div>
                <div class="border-gray-300 text-gray-600 flex justify-between border-t py-3 px-2">
                        <div class="flex justify-between text-sm">
                            <div class="font-bold">Payment method:&nbsp</div>
                            <input disabled class="boxes" name="pmethod" value="<%= userInfo.getPaymentMethod() %>">
                        </div>
                        <div class="flex justify-between text-sm">
                            <div class="font-bold">Payment number:&nbsp</div>
                            <input disabled class="boxes" name="pnumber" value=""><%= userInfo.getPaymentNumber() %>
                        </div>
                    </div>
            </div>
            <button id="changeConfirm" type="button" onclick="modifyOrConfirm()">Change infos</button>
            <button id="undo" type="button" onclick="revertChanges()">Revert</button>
        </form>
    </div>
    <%@include file="template/footer.jsp"%>
    </body>
</html>
