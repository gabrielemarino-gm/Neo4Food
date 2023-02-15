package it.unipi.lsmsd.neo4food.servlet;

import com.google.gson.Gson;
import it.unipi.lsmsd.neo4food.constants.Constants;
import it.unipi.lsmsd.neo4food.dao.neo4j.SocialNeoDAO;
import it.unipi.lsmsd.neo4food.dto.CommentDTO;
import it.unipi.lsmsd.neo4food.dto.ListDTO;
import it.unipi.lsmsd.neo4food.dto.RestaurantDTO;
import it.unipi.lsmsd.neo4food.dto.UserDTO;
import it.unipi.lsmsd.neo4food.service.ServiceProvider;

import java.io.*;
import java.util.List;
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

        if (actionType.equals("getComments"))
        {
            String rest = request.getParameter("restaurantId");
            int page = Integer.parseInt(request.getParameter("page"));

            ListDTO<CommentDTO> commentList = ServiceProvider.getSocialService().getComments(rest, page);

            response.getWriter().println(new Gson().toJson(commentList));
            response.getWriter().flush();

            return;
        }
//      Creo una nuova review
        else if (actionType.equals("addReview"))
        {
            String actor = request.getParameter("who");
            String target = request.getParameter("to");
            Double mark = Double.parseDouble(request.getParameter("rate"));
            String text = request.getParameter("text") != null ? request.getParameter("text") : "";
            ServiceProvider.getSocialService().setRating(actor, target, mark, text);

            return;
        }

//      Usato in friendlist.jsp
//      Usato in header.jsp
        else if(actionType.equals("setFollow"))
        {
            String actor = request.getParameter("actor");
            String target = request.getParameter("target");

            ServiceProvider.getSocialService().setFollow(actor, target);
            return;
        }
        else if (actionType.equals("removeFollow"))
        {
            String actor = request.getParameter("actor");
            String target = request.getParameter("target");

            ServiceProvider.getSocialService().removeFollow(actor, target);
            return;
        }

//      chiamato da in header.jsp
//      ritorna a friendlist.jsp
        else if (actionType.equals("getFollowers"))
        {
            targetJSP = "WEB-INF/jsp/friendlist.jsp";
            String username = request.getParameter("username");
            int page = Integer.parseInt(request.getParameter("page"));
            ListDTO<UserDTO> userList = ServiceProvider.getSocialService().getFollowers(username, page);

            request.setAttribute("listDTO", userList);

        }
        else if (actionType.equals("getFollowersNextPage"))
        {
            String username = request.getParameter("username");
            int page = Integer.parseInt(request.getParameter("page"));
            ListDTO<UserDTO> userList = ServiceProvider.getSocialService().getFollowers(username, page);

            response.getWriter().println((new Gson()).toJson(userList));
            response.getWriter().flush();
            return;
        }
//      Used in friendlist.jsp
        else if (actionType.equals("getRecommendationByFollow"))
        {
            String username = request.getParameter("username");
            ListDTO<UserDTO> userList = ServiceProvider.getSocialService().getRecommendationFriendOfFriend(username);

            response.getWriter().println((new Gson()).toJson(userList));
            response.getWriter().flush();
            return;
        }
//        Used in friendlist.jsp
        else if (actionType.equals("getRecommendationByRestaurant"))
        {
            String username = request.getParameter("username");

            ListDTO<UserDTO> userList = ServiceProvider.getSocialService().getRecommendationUserRestaurant(username);
            response.getWriter().println((new Gson()).toJson(userList));
            response.getWriter().flush();
            return;
        }
        else if (actionType.equals("getInfluencer"))
        {
            ListDTO<UserDTO> userList = ServiceProvider.getUtilityService().getInfluencers();

            response.getWriter().println((new Gson()).toJson(userList));
            response.getWriter().flush();
            return;
        }
//      Usato nel campo ricerca dell'header
        else if(actionType.equals("searchUser"))
        {
            String username = request.getParameter("username");

            response.getWriter().println((new Gson()).toJson(ServiceProvider.getUserService().getUsers(username)));
            response.getWriter().flush();
            return;
        }


        RequestDispatcher dispatcher = request.getRequestDispatcher(targetJSP);
        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        this.doRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        this.doRequest(request, response);
    }
}
