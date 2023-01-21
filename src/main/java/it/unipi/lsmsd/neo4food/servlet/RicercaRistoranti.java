package it.unipi.lsmsd.neo4food.servlet;

import java.io.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;

@WebServlet("/ricerca")
public class RicercaRistoranti extends HttpServlet
{
    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String targetJSP = "WEB-INF/jsp/lista.jsp";
        String actionType = (String) request.getParameter("action");
        if (actionType == null){
            targetJSP = "WEB-INF/jsp/ricerca.jsp";
        } else if (actionType != null && "search".equals(actionType)) {
            String zipcode = (String) request.getParameter("zipcode");
            request.setAttribute("zipcode", zipcode);
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
