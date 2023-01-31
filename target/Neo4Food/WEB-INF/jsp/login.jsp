<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link href="/Neo4Food_war_exploded/dist/output.css" rel="stylesheet" />
    <title>Login</title>
    <%@ include file="/WEB-INF/jsp/template/head_includes.jsp" %>
    <script type="text/javascript" src="<c:url value="/js/jquery-3.6.3.min.js"/>"></script>
    <script type="text/javascript">
        function hideshow(){
            var x = document.getElementById("loginForm");
            var y = document.getElementById("signupForm");
            if (x.style.display == "none"){
                x.style.display = "block";
                y.style.display = "none";
            }
            else{
                x.style.display = "none";
                y.style.display = "block";
            }
        }
    </script>
</head>

<body>
<%@include file="template/header.jsp"%>
<div class="z-40 h-48 overflow-hidden w-full">
    <img class="w-full blur-md" src="https://ilfattoalimentare.it/wp-content/uploads/2017/06/junk-food-hamburger-patatine-fast-food-pizza-dolci-Fotolia_130389179_Subscription_Monthly_M.jpg" alt="imgFood" />
</div>
<section>
    <div class="mx-auto -my-20 text-center relative" id="external">
        <div id="loginForm" class="mx-auto h-auto w-96 rounded-lg bg-principale pb-7 shadow-2xl">
            <div class="border-gray-300 rounded-t-lg border-b-2 bg-button p-1"><h3 class="py-5 text-3xl font-bold ">Login</h3>
                <% if(request.getAttribute("message") != null){ %>
                <div> <%= request.getAttribute("message").toString() %></div>
                <%}%></div>
            <form id="formL" method="post" action="<c:url value="/login"/>" class="pt-7">
                <input required="" class="h-9 w-52 rounded-lg px-3  shadow-xl" type="email" name="email" placeholder="E-Mail" />
                <input required="" class="my-4 h-9 w-52 rounded-lg px-3 shadow-xl" type="password" name="password" placeholder="Password" />
                <input type="hidden" name="action" value="login" id="actionType"/>
                </br>
                <button class="my-3 w-36 rounded-lg border-2 shadow-xl hover:bg-button" type="submit" id="loginButton">Login</button>
                <button type="button" class="my-3 w-36 rounded-lg border-2 shadow-xl" onclick="hideshow()">SignUp</button>
                <div class="flex justify-center">
                    <div class="form-check border-2 w-52 rounded-lg shadow-xl my-3">

                        <label class="form-check-label text-gray-800">
                            Are you a Restaurant?
                        </label>
                        <input type="checkbox" class="form-checkbox bg-test_col-100 border border-principale-300 text-button-500 focus:ring-200"name="isRestaurant">
                    </div>
                </div>
            </form>

        </div>

        <div id="signupForm" style="display:none;" class="mx-auto h-auto w-96 rounded-lg bg-principale py-7 shadow-2xl">
            <div class="border-gray-300 rounded-t-lg border-b-2 bg-button p-1 -my-7"><h3 class="py-5 text-3xl font-bold">SignUp</h3></div>
            <form method="post" action="<c:url value="/login"/>">
                <input required="" class="mt-14 h-9 w-52 rounded-lg px-3" type="text" name="username" placeholder="Username" />
                <input required="" class="mt-4 h-9 w-52 rounded-lg px-3 shadow-xl" type="email" name="email" placeholder="E-Mail" />
                <input required="" class="mt-4 h-9 w-52 rounded-lg px-3 shadow-xl" type="text" name="firstname" placeholder="First name" />
                <input required="" class="mt-4 h-9 w-52 rounded-lg px-3 shadow-xl" type="text" name="lastname" placeholder="Last name" />
                <input required="" class="mt-4 h-9 w-52 rounded-lg px-3 shadow-xl" type="text" name="phonenumber" placeholder="Phone number" />
                <input required="" class="mt-4 h-9 w-52 rounded-lg px-3 shadow-xl" type="text" name="address" placeholder="Address" />
                <input required="" class="mt-4 h-9 w-52 rounded-lg px-3 shadow-xl" type="text" name="zipcode" placeholder="Zipcode" />
                <input required="" class="mt-4 h-9 w-52 rounded-lg px-3 shadow-xl" type="password" name="password" placeholder="Password" />
                <input type="hidden" name="action" value="signup" />

                <button class="mt-4 w-52 rounded-lg border-2 hover:bg-button" type="submit">Signup</button>
            </form>
            <button class="mt-4 w-52 rounded-lg border-2 hover:bg-button" type="submit" onclick="hideshow()">SignIn</button>
        </div>
    </div>
</section>
<%@include file="template/footer.jsp"%>
</body>


</html>
