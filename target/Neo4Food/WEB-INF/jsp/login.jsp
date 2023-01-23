<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <%@ include file="/WEB-INF/jsp/template/head_includes.jsp" %>
    </head>
    <body>
        
<%--                Header della pagina--%>
        <%@ include file="template/header.jsp"%>
            <%
                String requestMessage = (String) request.getAttribute("message");
            %>
        <section>
            <% if (requestMessage != null) { %>
            <div role="alert"><%= requestMessage %></div>
            <% } %>
            <div class="mx-auto my-32 text-center" id="external" id="external">
<%--                Form di login--%>
                <div class="py-7 mx-auto h-auto w-96 rounded-lg bg-principale">
                    <h3 class="text-4xl py-5">Login</h3>
                    <form method="post" action="<c:url value="/login"/>">
                        <input class="h-9 w-52 rounded-lg px-3" type="email" name="username" placeholder="E-Mail"/>
                        <input class="h-9 my-4 w-52 rounded-lg px-3" type="password" name="password" placeholder="Password">
                        <input type="hidden" name="action" value="login">
<%--                                Questo campo nascosto serve per controllare dall'altra parte se sto facendo login o signup--%>
                        <button class="my-3 w-52 rounded-lg border-2"type="submit">Login</button>
                    </form>
<%--                        Aggiungere form a scomparsa per il Submit che si alterna con Login--%>
                </div>
            </div>
        </section>
    </body>
</html>
