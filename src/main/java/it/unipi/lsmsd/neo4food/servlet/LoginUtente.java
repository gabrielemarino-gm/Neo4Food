package it.unipi.lsmsd.neo4food.servlet;

import it.unipi.lsmsd.neo4food.constants.Constants;
import it.unipi.lsmsd.neo4food.dao.mongo.UserMongoDAO;
import it.unipi.lsmsd.neo4food.dto.UserDTO;
import it.unipi.lsmsd.neo4food.model.User;

import java.io.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;

@WebServlet("/login")
public class LoginUtente extends HttpServlet
{
    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String actionType = (String) request.getParameter("action");
        String targetJSP = "WEB-INF/jsp/ricerca.jsp";
//        Se actionType e' null la richiesta viene da header.jsp
        if (actionType == null){
            targetJSP = "WEB-INF/jsp/login.jsp";
        }
//        Se actionType vale "login" la richiesta viene da login.jsp
        else if ("login".equals(actionType)) {
            String email = (String) request.getParameter("email");
            String password = (String) request.getParameter("password");
            UserMongoDAO users = new UserMongoDAO();
            UserDTO result = users.getUser(email, password);
//            Utente trovato
            if (!result.getId().equals("0")) {
                HttpSession session = request.getSession();
                session.setAttribute(Constants.AUTHENTICATION_FIELD, result);
                session.setAttribute("username", result.getUsername());
            } else {
//                No match
                targetJSP = "WEB-INF/jsp/login.jsp";
                request.setAttribute("message", "Wrong credentials");
            }
        }
//        Se actionType vale "Signup" la richiesta e' un signup
        else if ("signup".equals(actionType)) {
            UserMongoDAO users = new UserMongoDAO();
            String username = (String) request.getParameter("username");
            String email = (String) request.getParameter("email");
            String firstname = (String) request.getParameter("firstname");
            String lastname = (String) request.getParameter("lastname");
            String phonenumber = (String) request.getParameter("phonenumber");
            String zipcode = (String) request.getParameter("zipcode");
            String password = (String) request.getParameter("password");
            String address = (String) request.getParameter("address");
            User user = new User("0",email,username,password,firstname,lastname,phonenumber,address,zipcode);

            if (users.getUser(email, password).getId().equals("0")) {
//                Can create new user
                    users.registerUser(user);
                    UserDTO registered = users.getUser(user.getEmail(), user.getPassword());
                    HttpSession session = request.getSession();
                    session.setAttribute(Constants.AUTHENTICATION_FIELD, registered);
                    session.setAttribute("username", registered.getUsername());
            } else {
//                Credenziali gia in utilizzo
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
}