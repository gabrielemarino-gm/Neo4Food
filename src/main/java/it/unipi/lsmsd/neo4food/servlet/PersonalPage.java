package it.unipi.lsmsd.neo4food.servlet;

import it.unipi.lsmsd.neo4food.constants.Constants;
import it.unipi.lsmsd.neo4food.service.ServiceProvider;
import it.unipi.lsmsd.neo4food.dto.ListDTO;
import it.unipi.lsmsd.neo4food.dto.OrderDTO;
import it.unipi.lsmsd.neo4food.dto.UserDTO;
import it.unipi.lsmsd.neo4food.model.User;

import java.io.*;
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

        if(actionType != null){
            if(actor.equals("user"))
            {
                String target = userRequest(request, response);
                targetJSP = target != null ? target : targetJSP;
            }
            else if(actor.equals("restaurant"))
            {
                targetJSP = "WEB-INF/jsp/personalrestaurant.jsp";
                restaurantRequest(request, response);
            }
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

    private String userRequest(HttpServletRequest request, HttpServletResponse response){
        String actionType = request.getParameter("action");
        if(actionType.equals("change")){
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

            Boolean result = ServiceProvider.getUserService().
                                updateUser(new User(uid, null,null,
                                null, newFirstname,newLastname,
                                newPhone,newAddress,newZipcode,
                                newPaymentmethod,newPaymentnumber)
                                );
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
            }else{
//              Update fallito
//                Segnalo messaggio all utente
                request.setAttribute("message", "Update failed");
            }

        }
        else if(actionType.equals("orders")){
//            Carico lista ordini per utente
            String uid = request.getParameter("aid");

            ListDTO<OrderDTO> orders = ServiceProvider.getOrderService()
                                        .getOrders(uid, false);

            request.setAttribute("orders", orders);

            return "WEB-INF/jsp/orders.jsp";
        }

        return null;
    }

    private void restaurantRequest(HttpServletRequest request, HttpServletResponse response){
        String actionType = request.getParameter("action");

    }
}
