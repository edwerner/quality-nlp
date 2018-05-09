import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceSample;
import opennlp.tools.sentdetect.SentenceSampleStream;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class Servlet extends HttpServlet {

  private static final long serialVersionUID = -4751096228274971485L;
  public List<String> inputArray;
  String sentences[];

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.setAttribute("inputArray", inputArray);
    request.getRequestDispatcher("/index.jsp").forward(request, response);
    // trainModel();
    detectSentence();
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String input = request.getParameter("input");
    String lastInput = null;
    String output = null;
    
    if (input != null) {
      output = searchSentences(input);
      if (output != null) {
        inputArray.add(output);
      }
    }
    if (!inputArray.isEmpty()) {
      lastInput = inputArray.get(inputArray.size() - 1);
      if (!input.equals(lastInput) && !inputArray.isEmpty()) {
        inputArray.add(input);
      }
    } else if (inputArray.isEmpty()) {
      inputArray.add(input);
    }
    List<?> shallowCopy = inputArray.subList(0, inputArray.size());
    Collections.reverse(shallowCopy);
    request.setAttribute("inputArray", shallowCopy);
    request.getRequestDispatcher("/index.jsp").forward(request, response);
  };

  public String searchSentences(String input) {
    List<String> sentenceList;
    if (sentences != null) {
      for (String sentence : sentences) {
        String[] split = sentence.split(" ");
        sentenceList = Arrays.asList(split);
        if (sentenceList.contains(input)) {
          System.out.println(sentence);
          return sentence;
        }
      }
    }
    return null;
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

  @SuppressWarnings("deprecation")
  public void trainModel() throws IOException {
    Charset charset = Charset.forName("UTF-8");
    ObjectStream<String> lineStream = null;
    try {
      lineStream = new PlainTextByLineStream(new FileInputStream(
          "C:\\Program Files\\Apache Software Foundation\\apache-opennlp-1.8.4\\models\\en-sent.train"), charset);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    ObjectStream<SentenceSample> sampleStream = new SentenceSampleStream(lineStream);
    SentenceModel model;
    try {
      model = SentenceDetectorME.train("en", sampleStream, true, null, TrainingParameters.defaultParams());
    } finally {
      sampleStream.close();
    }
    // OutputStream modelOut = null;
    // File modelFile = new File("C:\\Program Files\\Apache Software
    // Foundation\\apache-opennlp-1.8.4\\models\\model.txt");
    // try {
    // modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
    // model.serialize(modelOut);
    // } finally {
    // if (modelOut != null)
    // modelOut.close();
    // }
  }

  public void detectSentence() {
    InputStream modelIn = null;
    try {
      modelIn = new FileInputStream(
          "C:\\Program Files\\Apache Software Foundation\\apache-opennlp-1.8.4\\models\\en-sent.bin");
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    }
    try {
      SentenceModel model = new SentenceModel(modelIn);
      SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
      File file = new File(
          "C:\\Program Files\\Apache Software Foundation\\apache-opennlp-1.8.4\\models\\en-sent.train");

      String line = null;
      String textLine = null;
      FileReader fileReader = new FileReader(file);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      while ((line = bufferedReader.readLine()) != null) {
        textLine += line;
      }

      sentences = sentenceDetector.sentDetect(textLine);

      // WhitespaceTokenizer tokenizer = WhitespaceTokenizer.INSTANCE;
      // for(String s : sentences) {
      // String tokens[] = tokenizer.tokenize(s);
      // for (String token : tokens) {
      // System.out.println(token);
      // }
      // }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (modelIn != null) {
        try {
          modelIn.close();
        } catch (IOException e) {

        }
      }
    }
  }

}