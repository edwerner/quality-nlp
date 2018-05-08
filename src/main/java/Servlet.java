import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class Servlet extends HttpServlet {

    private static final long serialVersionUID = -4751096228274971485L;
    public List<String> inputArray;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
      request.setAttribute("inputArray", inputArray);
      request.getRequestDispatcher("/index.jsp").forward(request, response);
      detectSentence();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
      String input = request.getParameter("input");
      String lastInput = null;
      if (!inputArray.isEmpty()) {
        lastInput = inputArray.get(inputArray.size() - 1);
        if (!input.equals(lastInput) && !inputArray.isEmpty()) {
          inputArray.add(input);
        }
      } else if (inputArray.isEmpty()) {
        inputArray.add(input);
      } 
      request.setAttribute("inputArray", inputArray);
      request.getRequestDispatcher("/index.jsp").forward(request, response);
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
    
    public void detectSentence() {
      InputStream modelIn = null;
      try {
        modelIn = new FileInputStream("C:\\Program Files\\Apache Software Foundation\\apache-opennlp-1.8.4\\models\\en-sent.bin");
      } catch (FileNotFoundException e1) {
        e1.printStackTrace();
      }
      try {
        SentenceModel model = new SentenceModel(modelIn);
        SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
        String sentences[] = sentenceDetector.sentDetect("First sentence. Second sentence.");
        System.out.println(sentences[0]);
      }
      catch (IOException e) {
        e.printStackTrace();
      }
      finally {
        if (modelIn != null) {
          try {
            modelIn.close();
          }
          catch (IOException e) {
          }
        }
        
      }
    }

}