package it.unipi.lsmsd.neo4food.servlet;

import com.google.gson.Gson;
import it.unipi.lsmsd.neo4food.dto.CommentDTO;
import it.unipi.lsmsd.neo4food.dto.ListDTO;
import it.unipi.lsmsd.neo4food.dto.UserDTO;
import it.unipi.lsmsd.neo4food.service.ServiceProvider;

import java.io.*;
import java.util.Arrays;
import java.util.Map;
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

            if(commentList != null){
                response.getWriter().println(toSend);
                response.getWriter().flush();
            }
            else
            {
                response.getWriter().println("{'itemCount'= 0}");
                response.getWriter().flush();
            }

            return;
        } else if (actionType.equals("addReview")) {
            String author = request.getParameter("who");
            String target = request.getParameter("to");
            Double mark = Double.parseDouble(request.getParameter("rate"));
            String text = request.getParameter("text") != null ? request.getParameter("text") : "";

            ServiceProvider.getSocialService().setRating(author, target, mark, text);

            response.getWriter().println();
            response.getWriter().flush();
            return;
        }
        else if(actionType.equals("setFollow"))
        {
            String username = request.getParameter("username");
            String username2 = request.getParameter("username2");
            System.out.println(username2);
            System.out.println(username);
            ServiceProvider.getSocialService().setFollow(username,username2);

            String toSend = new Gson().toJson(username);

            response.getWriter().println(toSend);
            response.getWriter().flush();
            return ;

        }
        else if (actionType.equals("search"))
        {
            String target = request.getParameter("target");
            String page = request.getParameter("page");

//            ServiceProvider.getSocialService().findFriends();


//            response.getWriter().println();
//            response.getWriter().flush();
//            return;

        }
        else if (actionType.equals("getFollowers")) {
// lista Followers
            targetJSP = "WEB-INF/jsp/friendlist.jsp";
            String username = request.getParameter("username");
            ListDTO<UserDTO> userList = ServiceProvider.getSocialService().getFollowers(username, 0);
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(request.getParameter("username"));

            request.setAttribute("listDTO", userList);
            request.setAttribute("userDTO", userDTO);

        }
        else if(actionType.equals("getRecommendationByFollow")){

            String username = request.getParameter("username");

            ListDTO<UserDTO> userList = ServiceProvider.getSocialService().getRecommendationFriendOfFriend(username);

            String toSend = new Gson().toJson(userList);
            response.getWriter().println(toSend);
            response.getWriter().flush();
            return;

        }
        else if(actionType.equals("getRecommendationByRestaurant")){

            String username = request.getParameter("username");

            ListDTO<UserDTO> userList = ServiceProvider.getSocialService().getRecommendationUserRestaurant(username);

            String toSend = new Gson().toJson(userList);
            response.getWriter().println(toSend);
            response.getWriter().flush();
            return;

        }
        else if(actionType.equals("removeFollow")){

            String username = request.getParameter("username");
            String username2 = request.getParameter("username2");
            System.out.println(username2);
            System.out.println(username);
            ServiceProvider.getSocialService().removeFollow(username,username2);

            String toSend = new Gson().toJson(username);

            response.getWriter().println(toSend);
            response.getWriter().flush();
            return ;

        }
        else if(actionType.equals("getInfluencer"))
        {
            String username = request.getParameter("username");

            ListDTO<UserDTO> userList=ServiceProvider.getUtilityService().getInfluencers();

            String toSend = new Gson().toJson(userList);

            response.getWriter().println(toSend);
            response.getWriter().flush();
            System.out.println(toSend);
            return ;

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
