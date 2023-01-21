package it.unipi.lsmsd.neo4food.servlet;

import java.io.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;

@WebServlet("/loginUtente")
public class LoginUtente extends HttpServlet
{
    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
//        TODO CONTROLLI VARI
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String actionType = request.getParameter("action");
        String targetJSP = "WEB-INF/jsp/login.jsp";
        if (password != null)
        {
            String messaggio = "Your username were " + username;
            request.setAttribute("message", messaggio);
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
