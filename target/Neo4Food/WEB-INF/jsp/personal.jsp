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
        <div>
            <%@ include file="template/header.jsp"%>
        </div>
        <div>
            <%
                UserDTO userInfo = (UserDTO) request.getSession().getAttribute(Constants.AUTHENTICATION_FIELD);
            %>
            <div>
                <div>
                    <a>ID</a>
                </div>
                <div>
                    <a> <%= userInfo.getId() %></a>
                </div>
            </div>
            <div>
                <div>
                    <a>Username</a>
                </div>
                <div>
                    <a> <%= userInfo.getUsername() %></a>
                </div>
            </div>
            <div>
                <div>
                    <a>First Name</a>
                </div>
                <div>
                    <a> <%= userInfo.getFirstName() %></a>
                </div>
            </div>
            <div>
                <div>
                    <a>Last Name</a>
                </div>
                <div>
                    <a> <%= userInfo.getLastName() %></a>
                </div>
            </div>
            <div>
                <div>
                    <a>Email</a>
                </div>
                <div>
                    <a> <%= userInfo.getEmail() %></a>
                </div>
            </div>
            <div>
                <div>
                    <a>Phone Number</a>
                </div>
                <div>
                    <a> <%= userInfo.getPhoneNumber() %></a>
                </div>
            </div>
            <div>
                <div>
                    <a>Address</a>
                </div>
                <div>
                    <a> <%= userInfo.getAddress() %></a>
                </div>
            </div>
            <div>
                <div>
                    <a>Zipcode</a>
                </div>
                <div>
                    <a> <%= userInfo.getZipcode() %></a>
                </div>
            </div>
        </div>
    </body>
</html>
