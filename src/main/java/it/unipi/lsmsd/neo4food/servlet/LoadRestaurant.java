package it.unipi.lsmsd.neo4food.servlet;

import java.io.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;

import com.mongodb.client.MongoCollection;
import it.unipi.lsmsd.neo4food.dao.mongo.RestaurantsMongoDAO;
import it.unipi.lsmsd.neo4food.dto.ListDTO;

@WebServlet("/restaurant")
public class LoadRestaurant extends HttpServlet{
    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String rId = (String) request.getAttribute("rid");
        String targetJSP = "WEB-INF/jsp/restaurant.jsp";
//        GET MORE INFO ABOUT RESTAURANT
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
