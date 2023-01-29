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


            <link href="/Neo4Food_war_exploded/dist/output.css" rel="stylesheet" />

            <style type="text/css" id="operaUserStyle"></style>
            <script src="chrome-extension://mooikfkahbdckldjjndioackbalphokd/assets/prompt.js"></script>
        </head>

        <body>
        <%@ include file="template/header.jsp"%>

        <%
            UserDTO userInfo = (UserDTO) request.getSession().getAttribute(Constants.AUTHENTICATION_FIELD);
        %>
        <div style="display: none;">
            <a>ID</a>
            <a> <%= userInfo.getId() %></a>
        </div>
        
        <div class="z-40 h-48 overflow-hidden w-full" style="top">
            <img class="w-full blur-md" src="https://ilfattoalimentare.it/wp-content/uploads/2017/06/junk-food-hamburger-patatine-fast-food-pizza-dolci-Fotolia_130389179_Subscription_Monthly_M.jpg" alt="imgFood" />
        </div>

        <div class="relative m-3 flex justify-center -my-20">
            <div class="w-3/6 rounded-lg bg-principale text-center shadow-lg">
                <div class="border-gray-300 rounded-t-lg border-b bg-button py-3 px-6 font-bold">My Account <%= userInfo.getUsername() %></div>

                <div class="border-gray-300 text-gray-600 flex justify-between border-t py-3 px-2">
                    <div class=" text-sm flex justify-between">
                        <div class="font-bold">First Name:&nbsp</div>
                        <%= userInfo.getFirstName() %>
                    </div>
                    <div class="flex justify-between text-sm">
                        <div class="font-bold">Last Name:&nbsp</div>
                        <%= userInfo.getLastName() %>
                    </div>
                </div>
                <div class="border-gray-300 text-gray-600 flex justify-between border-t py-3 px-2">
                    <div class="flex justify-between text-sm">
                        <div class="font-bold">E-mail:&nbsp</div>
                        <%= userInfo.getEmail() %>
                    </div>
                    <div class="flex justify-between text-sm">
                        <div class="font-bold">Phone:&nbsp</div>
                        <%= userInfo.getPhoneNumber() %>
                    </div>
                </div>
                <div class="border-gray-300 text-gray-600 flex justify-between border-t py-3 px-2">
                    <div class="flex justify-between text-sm">
                        <div class="font-bold">Address:&nbsp</div>
                        <%= userInfo.getAddress() %>
                    </div>
                    <div class="flex justify-between text-sm">
                        <div class="font-bold">Zipcode:&nbsp</div>
                        <%= userInfo.getZipcode() %>
                    </div>
                </div>
                <div class="border-gray-300 text-gray-600 flex justify-between border-t py-3 px-2">
                    <div class="flex justify-between text-sm">
                        <div class="font-bold">Payment method:&nbsp</div>
                        <%= userInfo.getPaymentMethod() %>
                    </div>
                    <div class="flex justify-between text-sm">
                        <div class="font-bold">Payment number:&nbsp</div>
                        <%= userInfo.getPaymentNumber() %>
                    </div>
                </div>
            </div>
        </div>
        <footer class="fixed bottom-0 w-full">
            <div class="bg-principale text-xs h-16">
                <p class="my-6 px-3 float-left">Neo4Food Italy S.r.l. - P.IVA 12345678910</p>
                <img class="my-5 h-6 px-6 float-right" src="https://brand.mastercard.com/content/dam/mccom/brandcenter/Logos/securecode.png" alt="mastercard">
                <img class="my-5 h-7 px-3 float-right" src="https://loghi-famosi.com/wp-content/uploads/2020/05/Visa-Logo.png" alt="visa">
                <img class="my-3 h-10 px-3 float-right" src="https://loghi-famosi.com/wp-content/uploads/2020/04/PayPal-Logo.png" alt="paypal">
            </div>
        </footer>
    </body>
</html>
