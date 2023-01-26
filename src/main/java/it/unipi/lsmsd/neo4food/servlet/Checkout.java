package it.unipi.lsmsd.neo4food.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet("/checkout")
public class Checkout extends HttpServlet
{
    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String targetJSP = "WEB-INF/jsp/checkout.jsp";
        String actionType = request.getParameter("action").toString();

        if("checkout".equals(actionType))
        {
            System.out.println("if(\"checkout\".equals(actionType)) \n\t" + request.getParameter("incremental"));
        }
        else if("test".equals(actionType))
        {
            // Ricevo la richiesta HTTP, e la stampo
            List<String> a = (request.getParameter("transferObj").equals("empty")) ? new ArrayList<String>() : Arrays.asList(request.getParameter("transferObj"));
            System.out.println(request.getParameter("transferObj"));

            a.add(request.getParameter("objectId"));

            System.out.println("object= " + request.getParameter("objectId")+", List= "+ a);
            String toSend = a.toString();
            System.out.println("Ho creato l'oggetto da inviare:");
            System.out.println(toSend);

            System.out.println("Provo a inviare:");

            try
            {
                response.getWriter().print(toSend);
                response.getWriter().flush();
                System.out.println("Inviato!");
            }
            catch (Exception e)
            {
                System.out.println("ERROR: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            return;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(targetJSP);
        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        doRequest(request,response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        doRequest(request,response);
    }
}
