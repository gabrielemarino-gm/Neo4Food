package it.unipi.lsmsd.neo4food.servlet;

import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

//            List<String> a = persistent.equals("") ? new ArrayList<String>() : new ArrayList<String>(Arrays.asList(new Gson().fromJson(persistent, String[].class)));;


//            response.getWriter().print(new Gson().toJson(a));
//            response.getWriter().flush();
            return;
        }else if("remove".equals(actionType)){
            String persistent = request.getParameter("transferObj");
            String id = request.getParameter("objectId");

            
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
