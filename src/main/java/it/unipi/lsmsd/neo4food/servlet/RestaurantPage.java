package it.unipi.lsmsd.neo4food.servlet;

import java.io.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;

import it.unipi.lsmsd.neo4food.dao.mongo.AggregationMongoDAO;
import it.unipi.lsmsd.neo4food.dto.AnalyticsDTO;
import it.unipi.lsmsd.neo4food.dto.ListDTO;
import it.unipi.lsmsd.neo4food.service.ServiceProvider;
import it.unipi.lsmsd.neo4food.dto.RestaurantDTO;
import java.util.List;


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
        else if(actionType.equals("stats"))
        {

//          Devo passare alla pagina delle statistiche
            ListDTO<AnalyticsDTO> orari = ServiceProvider.getAggregationService().getBestHours(res);
            ListDTO<AnalyticsDTO> piatto = ServiceProvider.getAggregationService().getBestDishMonth(res);
            AnalyticsDTO fatturato = ServiceProvider.getAggregationService().getDailyRevene(res);
            ListDTO<AnalyticsDTO> moda = ServiceProvider.getAggregationService().getModaOrders(res);

            request.setAttribute("Orari", orari);
            request.setAttribute("Piatto", piatto);
            request.setAttribute("Fatturato", fatturato);
            request.setAttribute("Moda", moda);
            targetJSP =  "WEB-INF/jsp/restaurantStats.jsp";

            RequestDispatcher dispatcher = request.getRequestDispatcher(targetJSP);
            dispatcher.forward(request, response);
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
