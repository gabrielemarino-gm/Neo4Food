package it.unipi.lsmsd.neo4food.servlet;

import com.google.gson.Gson;
import it.unipi.lsmsd.neo4food.dto.CommentDTO;
import it.unipi.lsmsd.neo4food.dto.ListDTO;
import it.unipi.lsmsd.neo4food.service.ServiceProvider;

import java.io.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;

@WebServlet("/social")
public class Social extends HttpServlet
{
    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String targetJSP = "WEB-INF/jsp/restaurant.jsp";
        String actionType = request.getParameter("action");

        if(actionType.equals("getComments")){
            String rid = request.getParameter("restaurantId");
            int page = Integer.parseInt(request.getParameter("page"));

            ListDTO<CommentDTO> commentList = ServiceProvider.getSocialService().getComments(rid, page);

            String toSend = new Gson().toJson(commentList);

            System.out.println(toSend);

            if(commentList != null){
                response.getWriter().println(toSend);
                response.getWriter().flush();
            }
            else
            {
                response.getWriter().println("{intCount : 0}");
                response.getWriter().flush();
            }

            return;
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
