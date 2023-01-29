package it.unipi.lsmsd.neo4food.servlet;

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

        if(actionType != null){
            if(actionType.equals("change")){
    //      Cambio i dati e ricarico la pagina personale
            String uid = request.getParameter("uid");

    //      New fields
            String newFirstname = request.getParameter("fname");
            String newLastname = request.getParameter("lname");
            String newPhone = request.getParameter("phone");
            String address = request.getParameter("address");
            String zipcode = request.getParameter("zipcode");
            String newPaymentmethod = request.getParameter("pmethod");
            String newPaymentnumber = request.getParameter("pnumber");
    //          -------

                System.out.println(newFirstname);
                System.out.println(newLastname);
                System.out.println(newPhone);
                System.out.println(address);
                System.out.println(zipcode);
                System.out.println(newPaymentmethod);
                System.out.println(newPaymentnumber);

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
