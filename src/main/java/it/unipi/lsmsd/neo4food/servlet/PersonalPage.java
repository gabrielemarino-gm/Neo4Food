package it.unipi.lsmsd.neo4food.servlet;

import it.unipi.lsmsd.neo4food.constants.Constants;
import it.unipi.lsmsd.neo4food.dto.*;
import it.unipi.lsmsd.neo4food.service.ServiceProvider;
import it.unipi.lsmsd.neo4food.model.User;

import java.io.*;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;

@WebServlet("/personal")
public class PersonalPage extends HttpServlet
{
    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String targetJSP = "WEB-INF/jsp/personal.jsp";
        String actionType = request.getParameter("action");
        String actor = request.getParameter("actor");

        String me = null;


        if(actionType != null)
        {
            if(actor.equals("user"))
            {
                String target = userRequest(request, response);
                targetJSP = target != null ? target : targetJSP;
            }
            else if(actor.equals("restaurant"))
            {
                if(actionType.equals("addDish") || actionType.equals("modDish") || actionType.equals("remDish"))
                {
                    editDish(request, response, actionType);
                    return;
                }

                String target = restaurantRequest(request, response);
                targetJSP = target != null ? target : "WEB-INF/jsp/personalrestaurant.jsp";
            }
        }
        else
        {
            targetJSP = "WEB-INF/jsp/login.jsp";
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

    private String userRequest(HttpServletRequest request, HttpServletResponse response)
    {
        String actionType = request.getParameter("action");
        if(actionType.equals("change"))
        {
            //      Cambio i dati e ricarico la pagina personale
            String uid = request.getParameter("uid");

            //      New fields
            String newFirstname = request.getParameter("fname");
            String newLastname = request.getParameter("lname");
            String newPhone = request.getParameter("phone");
            String newAddress = request.getParameter("address");
            String newZipcode = request.getParameter("zipcode");
            String newPaymentmethod = request.getParameter("pmethod");
            String newPaymentnumber = request.getParameter("pnumber");
            //          -------
//                Actually perform update
            UserDTO newData = new UserDTO();

            newData.setId(uid);
            newData.setFirstName(newFirstname);
            newData.setLastName(newLastname);
            newData.setPhoneNumber(newPhone);
            newData.setAddress(newAddress);
            newData.setZipcode(newZipcode);
            newData.setPaymentMethod(newPaymentmethod);
            newData.setPaymentNumber(newPaymentnumber);

            Boolean result = ServiceProvider.getUserService().updateUser(newData);
            if(result)
            {
//              Update andato a buon fine
                UserDTO user = (UserDTO) request.getSession().getAttribute(Constants.AUTHENTICATION_FIELD);

                user.setFirstName(newFirstname);
                user.setLastName(newLastname);
                user.setPhoneNumber(newPhone);
                user.setAddress(newAddress);
                user.setZipcode(newZipcode);
                user.setPaymentMethod(newPaymentmethod);
                user.setPaymentNumber(newPaymentnumber);

                request.getSession().setAttribute(Constants.AUTHENTICATION_FIELD, user);
                request.setAttribute("message", "Updated successfully");

                return "WEB-INF/jsp/personal.jsp";
            }
            else
            {
//              Update fallito
//              Segnalo messaggio all utente
                request.setAttribute("message", "Update failed");
            }

        }
        else if(actionType.equals("orders"))
        {
//            Carico lista ordini per utente
            String uid = request.getParameter("aid");

            ListDTO<OrderDTO> orders = ServiceProvider.getOrderService()
                                        .getOrders(uid, false);

            request.setAttribute("orders", orders);

            return "WEB-INF/jsp/orders.jsp";
        }
        else if(actionType.equals("personal"))
        {
            return "WEB-INF/jsp/personal.jsp";
        }
        else
        {
            return "WEB-INF/jsp/login.jsp";
        }

        return null;
    }

    private String restaurantRequest(HttpServletRequest request, HttpServletResponse response)
    {
        String actionType = request.getParameter("action");

        if(actionType.equals("personal"))
        {
            String rid = ((RestaurantDTO) request.getSession().getAttribute(Constants.AUTHENTICATION_FIELD)).getId();

            List<OrderDTO> lista = ServiceProvider.getRestaurantService()
                    .getRestaurantDetails(rid,false,true)
                    .getOrders();
            request.setAttribute("orderList", lista);
            return "WEB-INF/jsp/personalrestaurant.jsp";

        }
        else if (actionType.equals("orders"))
        {
            String rid = request.getParameter("aid");

            ListDTO<OrderDTO> orders = ServiceProvider.getOrderService()
                    .getOrders(rid, true);

            request.setAttribute("orders", orders);

            return "WEB-INF/jsp/orders.jsp";

        }
        else if(actionType.equals("dishes"))
        {
            String me = ((RestaurantDTO)request.getSession().getAttribute(Constants.AUTHENTICATION_FIELD)).getId();

            ListDTO<DishDTO> lista = new ListDTO<>();
            List<DishDTO> temp = ServiceProvider.getRestaurantService().getRestaurantDetails(me,true,false).getDishes();
            lista.setList(temp);
            lista.setItemCount(temp.size());

            request.setAttribute("dishes", lista);

            return "WEB-INF/jsp/dishes.jsp";
        }
        else if(actionType.equals("stats"))
        {
            return "WEB-INF/jsp/restaurantStats.jsp";
        }

        return null;
    }

    private void editDish(HttpServletRequest request, HttpServletResponse response, String actionType) throws IOException
    {
        String rid = request.getParameter("rid");
        String did = request.getParameter("did");
        String dname = request.getParameter("dname");
        String ddesc = request.getParameter("ddesc") != null ? request.getParameter("ddesc") : "";
        double dprice = Double.parseDouble((request.getParameter("dprice")));
        String dcurr = request.getParameter("dcurr");

        if(actionType.equals("addDish"))
        {
            if(dname == null || dname.equals("") || dcurr == null){throw new RuntimeException();}

            DishDTO toAdd = new DishDTO();
//            ------------
            toAdd.setRestaurantId(rid);
            toAdd.setName(dname);
            toAdd.setPrice(dprice);
            toAdd.setCurrency(dcurr);
            toAdd.setDescription(ddesc);
//            ------------
            String res = ServiceProvider.getRestaurantService().addDish(toAdd);

            response.getWriter().write(res);
            response.getWriter().flush();
        }
        else if(actionType.equals("remDish"))
        {
            DishDTO toRemove = new DishDTO();
//            ------------
            toRemove.setRestaurantId(rid);
            toRemove.setId(did);
//            ------------
            int res = ServiceProvider.getRestaurantService().remDish(toRemove);

            response.getWriter().write(res + " removed");
            response.getWriter().flush();
        }
        else if(actionType.equals("modDish"))
        {
            if(dname == null || dname.equals("")){throw new RuntimeException();}

            DishDTO toModify = new DishDTO();
//            --------------
            toModify.setRestaurantId(rid);
            toModify.setId(did);
            toModify.setName(dname);
            toModify.setPrice(dprice);
            toModify.setDescription(ddesc);
//            --------------
            int res = ServiceProvider.getRestaurantService().modDish(toModify);

            response.getWriter().write(res + " affected");
            response.getWriter().flush();
        }

    }
}
