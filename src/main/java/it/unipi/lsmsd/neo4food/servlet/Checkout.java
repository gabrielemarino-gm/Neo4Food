package it.unipi.lsmsd.neo4food.servlet;

import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import it.unipi.lsmsd.neo4food.constants.Constants;
import it.unipi.lsmsd.neo4food.dao.mongo.UserMongoDAO;
import it.unipi.lsmsd.neo4food.dto.OrderDTO;
import it.unipi.lsmsd.neo4food.dto.DishDTO;
import it.unipi.lsmsd.neo4food.dto.UserDTO;

@WebServlet("/checkout")
public class Checkout extends HttpServlet
{
    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String targetJSP = "WEB-INF/jsp/checkout.jsp";
        String actionType = request.getParameter("action");

        if ("checkout".equals(actionType)) {
//            Creo nuovo ordine
            OrderDTO order = new OrderDTO();
            UserDTO user = (UserDTO) request.getSession().getAttribute(Constants.AUTHENTICATION_FIELD);

            order.setUser(user.getUsername());
            order.setAddress(user.getAddress());
            order.setZipcode(user.getZipcode());
            order.setRestaurant(request.getParameter("restaurant"));
            order.setRestaurantId(request.getParameter("rid"));

            String[] names = request.getParameterValues("dishName");
            String[] prices = request.getParameterValues("dishCost");
            String[] quantities = request.getParameterValues("dishQuantity");
            String[] currencies = request.getParameterValues("dishCurrency");

            double total = 0;
            String currency = "";

            List<DishDTO> dishes = new ArrayList<DishDTO>();
            for(int i = 0; i< names.length; i++)
            {
                if(Integer.parseInt(quantities[i]) > 0)
                {
//                  Creo un piatto
                    DishDTO dishDTO = new DishDTO();
                    dishDTO.setName(names[i]);
                    dishDTO.setPrice(Double.parseDouble(prices[i]));
                    dishDTO.setCurrency(currencies[i]);
                    dishDTO.setQuantity(Integer.parseInt(quantities[i]));

//                    Assegno all'ordine la currency del primo piatto
                    if(currency.equals("") && !currencies[i].equals("")){ order.setCurrency(currencies[i]); }
                    dishes.add(dishDTO);
                    total += Double.parseDouble(prices[i]) * Integer.parseInt(quantities[i]);
                }
            }

//            Setto piatti e totale
            order.setDishes(dishes);
            order.setTotal(total);

            request.setAttribute("order", order);
        }
//        Completo l'ordine con gli ultimi dati e lo inserisco nel database
        else if ("confirm".equals(actionType))
        {
            String obj = request.getParameter("incremental");

            OrderDTO order = new Gson().fromJson(obj, OrderDTO.class);

            order.setPaymentMethod(request.getParameter("pm"));
            order.setPaymentNumber(request.getParameter("pn"));
            order.setCreationDate(new Date());

            new UserMongoDAO().insertOrder(order);
            return;
        }

        // Risposta al JSP ??
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
