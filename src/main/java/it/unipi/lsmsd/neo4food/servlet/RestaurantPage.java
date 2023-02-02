package it.unipi.lsmsd.neo4food.servlet;

import java.io.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;

import it.unipi.lsmsd.neo4food.service.ServiceProvider;
import it.unipi.lsmsd.neo4food.dto.RestaurantDTO;

@WebServlet("/restaurant")
public class RestaurantPage extends HttpServlet
{
    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String targetJSP = "WEB-INF/jsp/restaurant.jsp";

        String res = request.getParameter("rid");
        String actionType = request.getParameter("action");
        if("details".equals(actionType))
        {
            RestaurantDTO ret = ServiceProvider.getRestaurantService()
                                .getRestaurantDetails(res,true,false);
            request.setAttribute("restaurantDTO", ret);
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
