package it.unipi.lsmsd.neo4food.servlet;

import it.unipi.lsmsd.neo4food.constants.Constants;
import it.unipi.lsmsd.neo4food.dto.OrderDTO;
import it.unipi.lsmsd.neo4food.service.ServiceProvider;
import it.unipi.lsmsd.neo4food.dto.UserDTO;
import it.unipi.lsmsd.neo4food.model.User;
import it.unipi.lsmsd.neo4food.dto.RestaurantDTO;

import java.io.*;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;

@WebServlet("/admin")
public class Admin extends HttpServlet
{
    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String targetJSP = "/WEB-INF/jsp/admin.jsp";
        String token = request.getParameter("token");
//        Controllo token
        if(ServiceProvider.getAdminService().isTokenValid(token)){
//            Login come admin

        }else{
//            Invalid request
            targetJSP = "/WEB-INF/jsp/invalid.jsp";
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

    private Boolean loginAsRestaurant(HttpServletRequest request, HttpServletResponse response)
    {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        RestaurantDTO result = ServiceProvider.getRestaurantService().getRestaurantLogin(email, password);
//      Ristorante trovato
        if (!result.getId().equals("0"))
        {
            HttpSession session = request.getSession();
            session.setAttribute(Constants.AUTHENTICATION_FIELD, result);
            session.setAttribute("restaurantname", result.getName());
            return true;
        }
        else
        //                No match
        {
            request.setAttribute("message", "Wrong credentials");
            return false;
        }
    }

    private Boolean loginAsUser(HttpServletRequest request, HttpServletResponse response)
    {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        UserDTO result = ServiceProvider.getUserService().getUserLogin(email, password);

//      Utente trovato
        if (!result.getId().equals("0"))
        {
            HttpSession session = request.getSession();
            session.setAttribute(Constants.AUTHENTICATION_FIELD, result);
            session.setAttribute("username", result.getUsername());
            return true;
        }
        else
        {
//          No match
            request.setAttribute("message", "Wrong credentials");
            return false;
        }
    }
}