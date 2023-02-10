package it.unipi.lsmsd.neo4food.servlet;

import com.google.gson.Gson;
import it.unipi.lsmsd.neo4food.dao.neo4j.SocialNeoDAO;
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

@WebServlet({"/social"})
public class Social extends HttpServlet {

    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String targetJSP = "WEB-INF/jsp/restaurant.jsp";
        String actionType = request.getParameter("action");
        String username;
        String text;

        if (actionType.equals("getComments")) {
            username = request.getParameter("restaurantId");
            int page = Integer.parseInt(request.getParameter("page"));
            ListDTO<CommentDTO> commentList = ServiceProvider.getSocialService().getComments(username, page);
            text = (new Gson()).toJson(commentList);
            if (commentList != null) {
                response.getWriter().println(text);
                response.getWriter().flush();
            } else {
                response.getWriter().println("{'itemCount'= 0}");
                response.getWriter().flush();
            }

        } else {
            String username2;
            if (actionType.equals("addReview")) {
                username = request.getParameter("who");
                username2 = request.getParameter("to");
                Double mark = Double.parseDouble(request.getParameter("rate"));
                text = request.getParameter("text") != null ? request.getParameter("text") : "";
                ServiceProvider.getSocialService().setRating(username, username2, mark, text);
                response.getWriter().println();
                response.getWriter().flush();
            } else {
                String toSend;
                if (actionType.equals("setFollow")) {
                    username = request.getParameter("username");
                    username2 = request.getParameter("username2");
                    System.out.println(username2);
                    System.out.println(username);
                    ServiceProvider.getSocialService().setFollow(username, username2);

                    toSend = (new Gson()).toJson(username);
                    response.getWriter().println(toSend);
                    response.getWriter().flush();
                } else {
                    if (actionType.equals("search")) {
                        username = request.getParameter("target");
                        username2 = request.getParameter("page");
                        System.out.println(request.getParameter("userSearch"));
                    } else {
                        ListDTO userList;

                        if (actionType.equals("getFollowers")) {
                            targetJSP = "WEB-INF/jsp/friendlist.jsp";
                            username = request.getParameter("username");
                            int page2 = Integer.parseInt(request.getParameter("page"));
                            userList = ServiceProvider.getSocialService().getFollowers(username, page2);
                            UserDTO userDTO = new UserDTO();
                            userDTO.setUsername(request.getParameter("username"));
                            request.setAttribute("listDTO", userList);
                            request.setAttribute("userDTO", userDTO);

                        } else {
                            if (actionType.equals("getFollowersNextPage")) {

                                username = request.getParameter("username");
                                int page2 = Integer.parseInt(request.getParameter("page"));
                                userList = ServiceProvider.getSocialService().getFollowers(username, page2);

                                toSend = (new Gson()).toJson(userList);
                                response.getWriter().println(toSend);
                                response.getWriter().flush();
                                return;

                            }

                            if (actionType.equals("getRecommendationByFollow")) {

                                username = request.getParameter("username");
                                System.out.println(username);
                                userList = ServiceProvider.getSocialService().getRecommendationFriendOfFriend(username);

                                toSend = (new Gson()).toJson(userList);
                                response.getWriter().println(toSend);
                                response.getWriter().flush();
                                return;
                            }

                            if (actionType.equals("getRecommendationByRestaurant")) {
                                username = request.getParameter("username");
                                ServiceProvider.getSocialService();
                                userList = SocialNeoDAO.getRecommendationUserRestaurant(username);
                                toSend = (new Gson()).toJson(userList);
                                response.getWriter().println(toSend);
                                response.getWriter().flush();
                                return;
                            }

                            if (actionType.equals("removeFollow")) {
                                username = request.getParameter("username");
                                username2 = request.getParameter("username2");
                                ServiceProvider.getSocialService().removeFollow(username, username2);
                                toSend = (new Gson()).toJson(username);
                                response.getWriter().println(toSend);
                                response.getWriter().flush();
                                return;
                            }

                            if (actionType.equals("getInfluencer")) {
                                username = request.getParameter("username");
                                userList = ServiceProvider.getUtilityService().getInfluencers();
                                toSend = (new Gson()).toJson(userList);
                                response.getWriter().println(toSend);
                                response.getWriter().flush();
                                return;
                            }

                            if(actionType.equals("searchUser")){

                                username = request.getParameter("username");

                                UserDTO userDTO = ServiceProvider.getUserService().getUser(username);

                                toSend = (new Gson()).toJson(userDTO);

                                response.getWriter().println(toSend);
                                response.getWriter().flush();
                                return;
                            }
                        }
                    }

                    RequestDispatcher dispatcher = request.getRequestDispatcher(targetJSP);
                    dispatcher.forward(request, response);
                }
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        this.doRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        this.doRequest(request, response);
    }
}
