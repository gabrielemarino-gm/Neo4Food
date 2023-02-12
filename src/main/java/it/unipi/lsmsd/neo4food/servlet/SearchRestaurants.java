package it.unipi.lsmsd.neo4food.servlet;

import java.io.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;

import it.unipi.lsmsd.neo4food.constants.Constants;
import it.unipi.lsmsd.neo4food.dto.UserDTO;
import it.unipi.lsmsd.neo4food.service.ServiceProvider;
import it.unipi.lsmsd.neo4food.dto.ListDTO;
import it.unipi.lsmsd.neo4food.dto.RestaurantDTO;

@WebServlet("/ricerca")
public class SearchRestaurants extends HttpServlet
{
    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String targetJSP = "WEB-INF/jsp/lista.jsp";
        String actionType = request.getParameter("action");

        if (actionType == null)
        {
            targetJSP = "WEB-INF/jsp/ricerca.jsp";
        }
        else if ("search".equals(actionType))
        {
            int page = Integer.parseInt(request.getParameter("page"));
            String zipcode = request.getParameter("zipcode");
            String filter = request.getParameter("filter");
            ListDTO<RestaurantDTO> list = new ListDTO<>();
            if(filter.equals("recc"))
            {
                list = ServiceProvider.getSocialService().getRecommendationRestaurant(
                                ((UserDTO)request.getSession().getAttribute(Constants.AUTHENTICATION_FIELD)).getUsername(),
                                zipcode);
            }
            else
            {
                list = ServiceProvider.getRestaurantService().getRestaurantsForSearchPage(page, zipcode, filter);
            }

            request.setAttribute("listDTO", list);
            request.setAttribute("zipcode", zipcode);
            request.setAttribute("filter", filter);
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
