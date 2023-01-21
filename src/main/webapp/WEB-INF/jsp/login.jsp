<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
    </head>
    <body>
        <div>
            <div>
<%--                Header della pagina--%>
                <%@ include file="template/header.jsp" %>
            </div>
            <%
                String requestMessage = (String) request.getAttribute("message");
            %>
            <section>
                <% if (requestMessage != null) { %>
                <div role="alert"><%= requestMessage %></div>
                <% } %>
                <div id="external">
    <%--                Form di login--%>
                    <div>
                        <h3>Login</h3>
                        <form method="post" action="<c:url value="/login"/>">
                            <input type="email" name="username" placeholder="E-Mail"/>
                            <input type="password" name="password" placeholder="Password">
                            <input type="hidden" name="action" value="login">
<%--                                Questo campo nascosto serve per controllare dall'altra parte se sto facendo login o signup--%>
                            <button type="submit">Login</button>
                        </form>
<%--                        Aggiungere form a scomparsa per il Submit che si alterna con Login--%>
                    </div>
                </div>
            </section>
        </div>
    </body>
</html>
