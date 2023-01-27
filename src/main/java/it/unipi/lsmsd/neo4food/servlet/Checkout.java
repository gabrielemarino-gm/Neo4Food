package it.unipi.lsmsd.neo4food.servlet;

import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.unipi.lsmsd.neo4food.dto.OrderDTO;
import it.unipi.lsmsd.neo4food.dto.DishDTO;
import it.unipi.lsmsd.neo4food.model.Order;

@WebServlet("/checkout")
public class Checkout extends HttpServlet
{
    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String targetJSP = "WEB-INF/jsp/checkout.jsp";
        String actionType = request.getParameter("action");

        if("checkout".equals(actionType)){
//            TODO

        }
        else if("add".equals(actionType)){

            String persistent = request.getParameter("transferObj");
            String id = request.getParameter("objectId");
            String name = request.getParameter("objectName");
            Float price = Float.parseFloat(request.getParameter("objectPrice"));
            String currency = request.getParameter("objectCurrency");

            OrderDTO order = persistent.equals("") ?
                    new OrderDTO() :
                    new Gson().fromJson(persistent, OrderDTO.class);

            order.addItem(new DishDTO(id,name,price,currency));

            System.out.println(order);

            response.getWriter().print(new Gson().toJson(order));
            response.getWriter().flush();
            return;
        }else if("remove".equals(actionType)){
            String persistent = request.getParameter("transferObj");
            String id = request.getParameter("objectId");

            OrderDTO order = persistent.equals("") ?
                    new OrderDTO() :
                    new Gson().fromJson(persistent, OrderDTO.class);

            order.removeItem(id);

            System.out.println(order);

            response.getWriter().print(new Gson().toJson(order));
            response.getWriter().flush();
            return;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(targetJSP);
        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        doRequest(request,response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        doRequest(request,response);
    }
}
