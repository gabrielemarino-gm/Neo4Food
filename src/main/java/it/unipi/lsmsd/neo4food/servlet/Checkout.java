package it.unipi.lsmsd.neo4food.servlet;

import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.unipi.lsmsd.neo4food.constants.Constants;
import it.unipi.lsmsd.neo4food.dto.RestaurantDTO;
import it.unipi.lsmsd.neo4food.model.Dish;
import it.unipi.lsmsd.neo4food.service.ServiceProvider;
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

//          Creo nuovo ordine
        if ("checkout".equals(actionType)) {
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
//        By clicking usual button in restaurant page
        else if("usual".equals(actionType)){
            OrderDTO order = new OrderDTO();
            UserDTO user = (UserDTO) request.getSession().getAttribute(Constants.AUTHENTICATION_FIELD);
            String restId = request.getParameter("rid");

            order.setUser(user.getUsername());
            order.setAddress(user.getAddress());
            order.setZipcode(user.getZipcode());
            order.setRestaurant(request.getParameter("restaurant"));
            order.setRestaurantId(restId);

            List<DishDTO> dishes = ServiceProvider.getAggregationService().getUsual(user.getUsername(), restId);

            if (dishes.size() > 0) {

                String curr = "";
                double total = 0;

                for (DishDTO d : dishes){
                    if(curr.equals("")){curr = d.getCurrency();}
                    total += d.getPrice();
                }

                order.setDishes(dishes);
                order.setTotal(total);
                order.setCurrency(curr);

                request.setAttribute("order", order);

            }else{
                targetJSP = "WEB-INF/jsp/restaurant.jsp";
                request.setAttribute("notUsual", "");
                RestaurantDTO ret = ServiceProvider.getRestaurantService()
                        .getRestaurantDetails(restId,true,false);
                request.setAttribute("restaurantDTO", ret);
            }
        }
//        Completo l'ordine con gli ultimi dati e lo inserisco nel database
        else if ("confirm".equals(actionType))
        {
            String obj = request.getParameter("incremental");

            OrderDTO order = new Gson().fromJson(obj, OrderDTO.class);

            order.setPaymentMethod(request.getParameter("pm"));
            order.setPaymentNumber(request.getParameter("pn"));
            order.setCreationDate(new Date());

            ServiceProvider.getOrderService().insertOrder(order);
            return;
        }
        else if ("send".equals(actionType))
        {
//            Invio conferma ordine
            String orderid = request.getParameter("oid");
            String restarantid = request.getParameter("rid");
//            ------

            boolean result = ServiceProvider.getOrderService().sendOrder(orderid, restarantid);
            if(!result){
                request.setAttribute("message", "An error occurred while confirming the order");
            }
//            ------
//            Ricarico pagina del ristorante prendendo la lista aggiornata
            targetJSP = "/WEB-INF/jsp/personalrestaurant.jsp";
            List<OrderDTO> lista = ServiceProvider.getRestaurantService()
                    .getRestaurantDetails(restarantid,false,true)
                    .getOrders();
            request.setAttribute("orderList", lista);
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
