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
//        Di base dopo aver cercato vado a lista.jsp
        String targetJSP = "WEB-INF/jsp/lista.jsp";
        String actionType = (String) request.getAttribute("action");
//        Se actionType e' null significa che non ho fatto una ricerca e questa pagina e' stata chiamata da index.jsp
        if (actionType == null || "search".equals(actionType)) {
            targetJSP = "WEB-INF/jsp/ricerca.jsp";
        } else {
//        In questo caso ho lo zipcode
        String zipcode = (String) request.getAttribute("zipcode");
//        Faccio la ricerca dei ristoranti per zipcode
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
