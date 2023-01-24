package it.unipi.lsmsd.neo4food.servlet;

import java.io.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;

import com.mongodb.client.MongoCollection;
import it.unipi.lsmsd.neo4food.dao.mongo.RestaurantsMongoDAO;
import it.unipi.lsmsd.neo4food.dto.ListDTO;
import it.unipi.lsmsd.neo4food.dto.RestaurantDTO;

@WebServlet("/ricerca")
public class RicercaRistoranti extends HttpServlet
{
    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String targetJSP = "WEB-INF/jsp/lista.jsp";
        String actionType = (String) request.getParameter("action");

        if (actionType == null){
            targetJSP = "WEB-INF/jsp/ricerca.jsp";
        } else if ("search".equals(actionType)) {
            int page = Integer.parseInt(request.getParameter("page"));
            String zipcode = (String) request.getParameter("zipcode");
            RestaurantsMongoDAO restaurants = new RestaurantsMongoDAO();
            ListDTO<RestaurantDTO> list = restaurants.getRestaurants(page, zipcode);
            request.setAttribute("listDTO", list);
            request.setAttribute("zipcode", zipcode);
            request.setAttribute("page", page);
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