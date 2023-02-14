package it.unipi.lsmsd.neo4food.servlet;

import java.io.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;

import it.unipi.lsmsd.neo4food.constants.Constants;
import it.unipi.lsmsd.neo4food.dto.AnalyticsDTO;
import it.unipi.lsmsd.neo4food.dto.ListDTO;
import it.unipi.lsmsd.neo4food.service.ServiceProvider;
import it.unipi.lsmsd.neo4food.dto.RestaurantDTO;


@WebServlet("/restaurant")
public class RestaurantPage extends HttpServlet
{
    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String targetJSP = "WEB-INF/jsp/error/404.jsp";;

        String res = request.getParameter("rid");
        String actionType = request.getParameter("action");


//      Un utente ha cercato il ristorante e di conseguenza cliccato per avere più info
        if("details".equals(actionType))
        {
            targetJSP = "WEB-INF/jsp/restaurant.jsp";
            RestaurantDTO ret = ServiceProvider.getRestaurantService()
                                .getRestaurantDetails(res,true,false);
            request.setAttribute("restaurantDTO", ret);
        }
        else if(actionType.equals("stats"))
        {
            String me = ((RestaurantDTO)request.getSession().getAttribute(Constants.AUTHENTICATION_FIELD)).getId();

//          Devo passare alla pagina delle statistiche
            ListDTO<AnalyticsDTO> orari = ServiceProvider.getAggregationService().getBestHours(me);
            ListDTO<AnalyticsDTO> piatto = ServiceProvider.getAggregationService().getBestDishDay(me);
            AnalyticsDTO fatturato = ServiceProvider.getAggregationService().getDailyRevenue(me);
            ListDTO<AnalyticsDTO> moda = ServiceProvider.getAggregationService().getModaOrders(me);


            request.setAttribute("Orari", orari);
            request.setAttribute("Piatto", piatto);
            request.setAttribute("Fatturato", fatturato);
            request.setAttribute("Moda", moda);
            targetJSP =  "WEB-INF/jsp/restaurantStats.jsp";
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
