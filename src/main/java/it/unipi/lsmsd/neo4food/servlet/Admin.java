package it.unipi.lsmsd.neo4food.servlet;

import com.google.gson.Gson;
import it.unipi.lsmsd.neo4food.constants.Constants;
import it.unipi.lsmsd.neo4food.dto.*;
import it.unipi.lsmsd.neo4food.service.ServiceProvider;
import it.unipi.lsmsd.neo4food.model.User;
import org.bson.Document;

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

        if(request.getParameter("token") != null)
        {
            String token = request.getParameter("token");
    //        TODO
            if(ServiceProvider.getAdminService().isTokenValid(token))
            {
    //            Login OK
                Document toServe = ServiceProvider.getAdminService().getPopulation();

                request.setAttribute("uCount", toServe.getLong("uCount"));
                request.setAttribute("rCount", toServe.getLong("rCount"));
                request.setAttribute("oCount", toServe.getLong("oCount"));
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

                response.getWriter().print("Ratings Updated!");
                response.getWriter().flush();
                return;
            }
            else if(actionType.equals("updatePrices"))
            {
                ServiceProvider.getAggregationService().setAvgPrices();

                response.getWriter().print("Prices Updated!");
                response.getWriter().flush();
                return;
            }
            else if(actionType.equals("analytics"))
            {
                serveAnalytics(request, response);
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

    private void serveAnalytics(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String type = request.getParameter("type");

        if(type.equals("zips"))
        {
//          Zipcode - Numero Ordini
            ListDTO<AnalyticsDTO> anaList =  ServiceProvider.getAggregationService().getOrdersPerZip();

            String toSend = (new Gson()).toJson(anaList);
            response.getWriter().print(toSend);
            response.getWriter().flush();
        }
        else if (type.equals("mostActive"))
        {
//          Nome utente - N recensioni - media
            ListDTO<AnalyticsDTO> anaList =  ServiceProvider.getUtilityService().getMostActiveUsers();

            String toSend = (new Gson()).toJson(anaList);
            response.getWriter().print(toSend);
            response.getWriter().flush();
        }
        else if (type.equals("profits"))
        {
//          Nome ristorante - Totale - Valuta
            ListDTO<AnalyticsDTO> anaList =  ServiceProvider.getAggregationService().getLastMonthProfits();

            String toSend = (new Gson()).toJson(anaList);
            response.getWriter().print(toSend);
            response.getWriter().flush();
        }
        else if (type.equals("caviale"))
        {
//              Nome piatto - Prezzo - Nome ristorante

            ListDTO<AnalyticsDTO> anaList =  ServiceProvider.getAggregationService().getCaviale();

            String toSend = (new Gson()).toJson(anaList);
            response.getWriter().print(toSend);
            response.getWriter().flush();
        }
        else if (type.equals("deliveryTime"))
        {
            String zipcode = request.getParameter("zip");
            ListDTO<AnalyticsDTO> anaList =  ServiceProvider.getAggregationService().getDeliveryTime(zipcode);

            String toSend = (new Gson()).toJson(anaList);

            response.getWriter().print(toSend);
            response.getWriter().flush();
        }
    }
}