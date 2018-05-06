import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Servlet extends HttpServlet {

    private static final long serialVersionUID = -4751096228274971485L;
    public List<String> inputArray;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
      inputArray.clear();
      request.setAttribute("inputArray", inputArray);
      request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
      String input = request.getParameter("input");
      inputArray.add(input);
      request.setAttribute("inputArray", inputArray);
      request.getRequestDispatcher("/index.jsp").forward(request, response);
      System.out.println(input);
    }

    @Override
    public void init() throws ServletException {
        inputArray = new ArrayList<String>();
        System.out.println("Servlet " + this.getServletName() + " has started");
    }

    @Override
    public void destroy() {
        System.out.println("Servlet " + this.getServletName() + " has stopped");
    }

}