<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
    </head>
    <body>
        <div>
            <div>

            </div>
            <%
                String requestMessage = (String) request.getAttribute("message");
            %>
            <section>
                <% if (requestMessage != null) { %>
                <div class="alert alert-danger" role="alert"><%= requestMessage %></div>
                <% } %>
                <div id="external">
    <%--                Form di login--%>
                    <div>
                        <h3>Login</h3>
                        <form method="post" action="<c:url value="/loginUtente"/>">
                            <input type="email" name="username" placeholder="E-Mail"/>
                            <input type="password" name="password" placeholder="Password">
                            <input type="hidden" name="action" value="login">
<%--                                Questo campo nascosto serve per controllare dall'altra parte se sto facendo login o signup--%>
                            <button type="submit">Login</button>
                        </form>
                    </div>
                </div>
            </section>
        </div>
    </body>
</html>
