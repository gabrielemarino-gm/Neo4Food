package it.unipi.lsmsd.neo4food;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;


@WebServlet("/home")
public class Test extends HttpServlet
{
    private String msg;
    public void init()
    {
        msg = "Test!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body>");
        out.println("<a>"+msg+"</a>");
        out.println("</body></html>");
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    {

    }


}
