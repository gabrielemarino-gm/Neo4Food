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

@WebServlet("/login")
public class Login extends HttpServlet
{
    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String actionType = request.getParameter("action");
        String targetJSP = "WEB-INF/jsp/ricerca.jsp";
//        Se actionType == null la richiesta viene da header.jsp

        if (actionType == null)
        {
            targetJSP = "WEB-INF/jsp/login.jsp";
        }

//        Se actionType vale "login" la richiesta viene da login.jsp
        else if ("login".equals(actionType))
        {
//          Login come ristorante
            if (request.getParameter("isRestaurant") != null)
            {
//              Se fallita torno a login
                if(!loginAsRestaurant(request,response))
                {
                    targetJSP = "WEB-INF/jsp/login.jsp";
                }
                else
                {
//                  Se non e' fallita lo mando alla pagina ristorante
                    targetJSP = "WEB-INF/jsp/personalrestaurant.jsp";
                    String rid = ((RestaurantDTO) request.getSession().getAttribute(Constants.AUTHENTICATION_FIELD)).getId();

                    List<OrderDTO> lista = ServiceProvider.getRestaurantService()
                                            .getRestaurantDetails(rid,false,true)
                                            .getOrders();

                    request.setAttribute("orderList", lista);

                }
            }
//          Login come utente
            else
            {
//              Login fallito
                if(!loginAsUser(request,response))
                {
                    targetJSP = "WEB-INF/jsp/login.jsp";
                }
            }
        }
//      Se actionType vale "Signup" la richiesta e' un signup
        else if ("signup".equals(actionType))
        {
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String firstname = request.getParameter("firstname");
            String lastname = request.getParameter("lastname");
            String phonenumber = request.getParameter("phonenumber");
            String zipcode = request.getParameter("zipcode");
            String password = request.getParameter("password");
            String address = request.getParameter("address");


            if (!ServiceProvider.getUserService().userExists(username ,email))
            {
                User newUser = new User(email, password, username, firstname, lastname, address, phonenumber, zipcode);

                UserDTO registered = ServiceProvider.getUserService().registerUser(newUser);

//                Se la registrazione ha successo, aggiungo il nodo su neo4j
                if(registered != null)
                {
                    try
                    {
//                        Creo nodo su neo4j
                        ServiceProvider.getSupportService().addUser(registered.getUsername());
                        HttpSession session = request.getSession();
                        session.setAttribute(Constants.AUTHENTICATION_FIELD, registered);
                        session.setAttribute("username", registered.getUsername());

                    }
                    catch (Exception e)
                    {
//                      Cancellare utente da mongodb se l'inserimento del nodo in neo4j non e' stato possibile
                        ServiceProvider.getUserService().removeUser(registered);

                        targetJSP = "WEB-INF/jsp/login.jsp";
                        request.setAttribute("message", "Some error occurred");
                    }
                }
                else
                {
//                  Something strange happened
                    targetJSP = "WEB-INF/jsp/login.jsp";
                    request.setAttribute("message", "Some error occurred");
                }

            }
            else
            {
//              Credenziali gia in utilizzo
                targetJSP = "WEB-INF/jsp/login.jsp";
                request.setAttribute("message", "Credentials still in use");
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