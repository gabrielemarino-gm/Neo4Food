<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <link href="/Neo4Food_war_exploded/dist/output.css" rel="stylesheet" />
    <link href="/Neo4Food_war_exploded/dist/text.css" rel="stylesheet" />
    <style>
        h1, h2, h3, h4, h5, p, a, button, div
        {
            color: #7C2714;
            font-family: gill sans, sans-serif;
        }
    </style>

    <script>
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
    <style type="text/css" id="operaUserStyle"></style>
    <title>Login Page</title>
    <script src="chrome-extension://mooikfkahbdckldjjndioackbalphokd/assets/prompt.js"></script>
</head>

<body>
<header class="text-1xs h-12 bg-principale px-5 font-bold">
    <a href="/Neo4Food_war_exploded/ricerca"><img class="float-left h-12" src="img/logo_2.png" alt="logo" /></a>

    <button class="float-right my-3 rounded-lg px-3 hover:bg-button">
        <a href="/Neo4Food_war_exploded/login">Login</a>
    </button>
</header>
<div class="-top-6 z-40 h-48 overflow-hidden" style="top">
    <img class="w-full blur-md" src="https://ilfattoalimentare.it/wp-content/uploads/2017/06/junk-food-hamburger-patatine-fast-food-pizza-dolci-Fotolia_130389179_Subscription_Monthly_M.jpg" alt="imgFood" />
</div>
<section>
    <div class="mx-auto my-5 text-center " id="external">
        <div id="loginForm" class="mx-auto h-auto w-96 rounded-lg bg-principale pb-7 shadow-2xl">
            <div class="border-gray-300 rounded-t-lg border-b-2 bg-button p-1"><h3 class="py-5 text-3xl font-bold ">Login</h3></div>
            <form method="post" action="/Neo4Food_war_exploded/login" class="pt-7">
                <input required="" class="h-9 w-52 rounded-lg px-3  shadow-xl" type="email" name="email" placeholder="E-Mail" />
                <input required="" class="my-4 h-9 w-52 rounded-lg px-3 shadow-xl" type="password" name="password" placeholder="Password" />
                <input type="hidden" name="action" value="login" />
                </br>
                <button class="my-3 w-36 rounded-lg border-2 shadow-xl" type="submit">Login</button>
            </form>
            <button class="my-3 w-36 rounded-lg border-2 shadow-xl" onclick="hideshow()">SignUp</button>
            <br>
            <div class="flex justify-center">
                <div class="form-check border-2 w-52 rounded-lg shadow-xl my-3">


                    <label class="form-check-label text-gray-800 " for="flexCheckDefault">
                        Are you a Restaurant?
                    </label>
                    <input type="checkbox" class="form-checkbox bg-test_col-100 border border-principale-300 text-button-500 focus:ring-200 "  >
                </div>

            </div>


        </div>

        <div id="signupForm" style="display: none" class="mx-auto h-auto w-96 rounded-lg bg-principale py-7">
            <h3 class="py-5 text-4xl ">Login</h3>
            <form method="post" action="/Neo4Food_war_exploded/login">
                <input required="" class="h-9 w-52 rounded-lg px-3" type="text" name="username" placeholder="Username" />
                <input required="" class="my-4 h-9 w-52 rounded-lg px-3" type="email" name="email" placeholder="E-Mail" />
                <input required="" class="my-4 h-9 w-52 rounded-lg px-3" type="text" name="firstname" placeholder="First name" />
                <input required="" class="my-4 h-9 w-52 rounded-lg px-3" type="text" name="lastname" placeholder="Last name" />
                <input required="" class="my-4 h-9 w-52 rounded-lg px-3" type="text" name="phonenumber" placeholder="Phone number" />
                <input required="" class="my-4 h-9 w-52 rounded-lg px-3" type="text" name="address" placeholder="Address" />
                <input required="" class="my-4 h-9 w-52 rounded-lg px-3" type="text" name="zipcode" placeholder="Zipcode" />
                <input required="" class="my-4 h-9 w-52 rounded-lg px-3" type="password" name="password" placeholder="Password" />
                <input type="hidden" name="action" value="signup" />

                <button class="my-3 w-52 rounded-lg border-2" type="submit">Signup</button>
            </form>
            <button class="my-3 w-52 rounded-lg border-2" onclick="hideshow()">SignIn</button>
        </div>
    </div>
</section>
</body>


</html>