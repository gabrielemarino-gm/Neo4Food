<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unipi.lsmsd.neo4food.dto.UserDTO" %>
<%@ page import="it.unipi.lsmsd.neo4food.constants.Constants" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <%@ include file="/WEB-INF/jsp/template/head_includes.jsp" %>
    </head>
    <body>
        <%@ include file="template/header.jsp"%>
        <div>
            <%
                UserDTO userInfo = (UserDTO) request.getSession().getAttribute(Constants.AUTHENTICATION_FIELD);
            %>
            <div style="display: none;">
                <a>ID</a>
                <a> <%= userInfo.getId() %></a>
            </div>

            <div>
                <a>Username</a>
                <form type="submit"> <%= userInfo.getUsername() %></form>
            </div>

            <div>
                <a>First Name</a>
                <form type="submit"> <%= userInfo.getFirstName() %></form>
            </div>

            <div>
                <a>Last Name</a>
                <form type="submit"> <%= userInfo.getLastName() %></form>
            </div>

            <div>
                <a>Email</a>
                <form type="submit"> <%= userInfo.getEmail() %></form>
            </div>

            <div>
                <a>Phone Number</a>
                <form type="submit"> <%= userInfo.getPhoneNumber() %></form>
            </div>

            <div>
                <a>Address</a>
                <form type="submit"> <%= userInfo.getAddress() %></form>
            </div>

            <div>
                <a>Zipcode</a>
                <form type="submit"> <%= userInfo.getZipcode() %></form>
            </div>
        </div>
    </body>
</html>
