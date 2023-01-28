package it.unipi.lsmsd.neo4food.servlet;

import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.unipi.lsmsd.neo4food.constants.Constants;
import it.unipi.lsmsd.neo4food.dao.mongo.UserMongoDAO;
import it.unipi.lsmsd.neo4food.dto.OrderDTO;
import it.unipi.lsmsd.neo4food.dto.DishDTO;
import it.unipi.lsmsd.neo4food.dto.UserDTO;

@WebServlet("/checkout")
public class Checkout extends HttpServlet {
    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String targetJSP = "WEB-INF/jsp/checkout.jsp";
        String actionType = request.getParameter("action");

        if ("checkout".equals(actionType)) {
//            TODO
            String persistent = request.getParameter("incremental");
            OrderDTO order = new Gson().fromJson(persistent, OrderDTO.class);
            UserDTO user = (UserDTO) request.getSession().getAttribute(Constants.AUTHENTICATION_FIELD);

            order.setUser(user.getUsername());
            order.setAddress(user.getAddress());
            order.setZipcode(user.getZipcode());
            order.setRestaurant(request.getParameter("restaurant"));

            request.setAttribute("order", order);
        } else if ("add".equals(actionType)) {

            String persistent = request.getParameter("transferObj");
            String id = request.getParameter("objectId");
            String name = request.getParameter("objectName");
            Double price = Double.parseDouble(request.getParameter("objectPrice"));
            String currency = request.getParameter("objectCurrency");

            OrderDTO order = persistent.equals("") ?
                    new OrderDTO() :
                    new Gson().fromJson(persistent, OrderDTO.class);

            order.addItem(new DishDTO(id, name, price, currency));

            response.getWriter().print(new Gson().toJson(order));
            response.getWriter().flush();
            return;
        } else if ("remove".equals(actionType)) {
            String persistent = request.getParameter("transferObj");
            String id = request.getParameter("objectId");

            OrderDTO order = persistent.equals("") ?
                    new OrderDTO() :
                    new Gson().fromJson(persistent, OrderDTO.class);

            order.removeItem(id);

            response.getWriter().print(new Gson().toJson(order));
            response.getWriter().flush();
            return;

        } else if ("confirm".equals(actionType)) {

            String obj = request.getParameter("incremental");
            System.out.println(obj);

            OrderDTO order = new Gson().fromJson(obj, OrderDTO.class);

            order.setPaymentMethod(request.getParameter("pm"));
            order.setPaymentNumber(request.getParameter("pn"));

            new UserMongoDAO().insertOrder(order);

//            response.getWriter().print("Success");
//            response.getWriter().flush();
            return;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(targetJSP);
        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doRequest(request, response);
    }
}
