package it.unipi.lsmsd.neo4food.servlet;

import it.unipi.lsmsd.neo4food.constants.Constants;
import it.unipi.lsmsd.neo4food.dto.OrderDTO;
import it.unipi.lsmsd.neo4food.service.ServiceProvider;
import it.unipi.lsmsd.neo4food.dto.UserDTO;
import it.unipi.lsmsd.neo4food.model.User;
import it.unipi.lsmsd.neo4food.dto.RestaurantDTO;

import java.io.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;

@WebServlet("/admin")
public class Admin extends HttpServlet
{
    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String targetJSP = "/WEB-INF/jsp/admin.jsp";

        if(request.getParameter("token") != null){
            String token = request.getParameter("token");
    //        TODO
            if(ServiceProvider.getAdminService().isTokenValid(token))
            {
    //            Login OK
                request.setAttribute("uCount", ServiceProvider.getAdminService().userCount());
                request.setAttribute("rCount", ServiceProvider.getAdminService().restCount());
                request.setAttribute("oCount", ServiceProvider.getAdminService().orderCount());
            }
            else
            {
    //            Invalid request
                targetJSP = "/WEB-INF/jsp/error/404.jsp";
            }
        }
//        Altre richieste
        else
        {
            String actionType = request.getParameter("action");
            if (actionType.equals("updateRatings"))
            {
                ServiceProvider.getAggregationService().setAvgRate();

                response.getWriter().print("Ratings updated");
                response.getWriter().flush();
                return;
            }
            else if(actionType.equals("updatePrices"))
            {
                ServiceProvider.getAggregationService().setAvgPrices();

                response.getWriter().print("Prices updated");
                response.getWriter().flush();
                return;
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