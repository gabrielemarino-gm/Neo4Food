package it.unipi.lsmsd.neo4food.servlet;

import it.unipi.lsmsd.neo4food.constants.Constants;
import it.unipi.lsmsd.neo4food.dao.mongo.UserMongoDAO;
import it.unipi.lsmsd.neo4food.dto.UserDTO;

import java.io.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;

@WebServlet("/login")
public class LoginUtente extends HttpServlet
{
    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String targetJSP = "WEB-INF/jsp/ricerca.jsp";
        String actionType = (String) request.getParameter("action");

        if (actionType == null){
            targetJSP = "WEB-INF/jsp/login.jsp";
        }
        else if (actionType != null && "login".equals(actionType)) {
            String username = (String) request.getParameter("username");
            String password = (String) request.getParameter("password");
            UserMongoDAO users = new UserMongoDAO();
            UserDTO result = users.getUser(username, password);
            System.out.println(result.getId());
            if (!result.getId().equals("0")) {
                HttpSession session = request.getSession();
                session.setAttribute(Constants.AUTHENTICATION_FIELD, result);
                session.setAttribute("username", result.getUsername());
            } else {
//                No match
                targetJSP = "WEB-INF/jsp/login.jsp";
                request.setAttribute("message", "Wrong credentials");
            }
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(targetJSP);
        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        doRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        doRequest(request, response);
    }
}
