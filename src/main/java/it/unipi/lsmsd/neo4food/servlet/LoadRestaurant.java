package it.unipi.lsmsd.neo4food.servlet;

import java.io.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;

import it.unipi.lsmsd.neo4food.dao.mongo.RestaurantsMongoDAO;
import it.unipi.lsmsd.neo4food.dto.RestaurantDTO;

@WebServlet("/restaurant")
public class LoadRestaurant extends HttpServlet{
    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String targetJSP = "WEB-INF/jsp/restaurant.jsp";

        String res = (String) request.getParameter("rid");
        RestaurantsMongoDAO restaurants = new RestaurantsMongoDAO();
        RestaurantDTO ret = restaurants.getRestaurantDetails(res);
//        System.out.println(ret.getId());
//        System.out.println(ret.getDishes().toString());
        request.setAttribute("details", ret);

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
