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
        String targetJSP = "WEB-INF/jsp/ricerca.jsp";
        String actionType = (String) request.getParameter("action");
        if (actionType != null) {
            //        Todo controlli vari
            String username = (String) request.getParameter("username");
            String password = (String) request.getParameter("password");
            if (password != null) {
                HttpSession session = request.getSession();
                session.setAttribute("userToken", password);
                session.setAttribute("username", username);
            }
        } else {
//            Pagina di login chiama dal link, reindirizzo a login.jsp
            targetJSP = "WEB-INF/jsp/login.jsp";
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
