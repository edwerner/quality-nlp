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

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceSample;
import opennlp.tools.sentdetect.SentenceSampleStream;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import opennlp.tools.util.TrainingParameters;

public class Servlet extends HttpServlet {

  private static final long serialVersionUID = -4751096228274971485L;
  public List<String> inputArray;
  public List<String> outputArray;
  private ArrayList<String> duplicateList;
  private String[] sentences;
  private final String TRAINING_DATA = "C:\\Program Files\\Apache Software Foundation\\apache-opennlp-1.8.4\\models\\en-sent.train";
  private final String TOKENIZER =  "C:\\Program Files\\Apache Software Foundation\\apache-opennlp-1.8.4\\models\\en-token.bin";
  private final String PERSONIZER = "C:\\Program Files\\Apache Software Foundation\\apache-opennlp-1.8.4\\models\\en-ner-person.bin";
  private final String SPEECH_MODEL = "C:\\Program Files\\Apache Software Foundation\\apache-opennlp-1.8.4\\models\\en-pos-maxent.bin";
  private final String CHUNKER = "C:\\Program Files\\Apache Software Foundation\\apache-opennlp-1.8.4\\models\\en-chunker.bin";
  private TokenizerME tokenizer;
  private NameFinderME nameFinder;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.setAttribute("inputArray", inputArray);
    request.getRequestDispatcher("/index.jsp").forward(request, response);
    // trainModel();
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String input = request.getParameter("input");
    String lastInput = null;

    if (input != null && searchSentences(input) != null) {
      inputArray.add(input);
      inputArray.add(searchSentences(input));
    }
    request.setAttribute("inputArray", inputArray);
    request.getRequestDispatcher("/index.jsp").forward(request, response);
  };

  public String searchSentences(String input) {
    List<String> sentenceList;
    if (sentences != null) {
      for (String sentence : sentences) {
        if (!duplicateList.contains(sentence)) {
          
          String[] split = sentence.split(" ");
          sentenceList = Arrays.asList(split);
          if (sentenceList.contains(input)) {
       
            WhitespaceTokenizer whitespaceTokenizer= WhitespaceTokenizer.INSTANCE; 
            String[] tokens = whitespaceTokenizer.tokenize(sentence);
            
            //Generating the POS tags 
            //Load the parts of speech model 
            File file = new File(SPEECH_MODEL);
            POSModel posModel = new POSModelLoader().load(file);
            
            //Constructing the tagger 
            POSTaggerME tagger = new POSTaggerME(posModel);        
            
            //Generating tags from the tokens 
            String[] tags = tagger.tag(tokens);
            
          //Loading the chunker model 
            InputStream inputStream = null;
            try {
              inputStream = new FileInputStream(CHUNKER);
            } catch (FileNotFoundException e) {
              e.printStackTrace();
            } 
            ChunkerModel chunkerModel = null;
            try {
              chunkerModel = new ChunkerModel(inputStream);
            } catch (InvalidFormatException e) {
              e.printStackTrace();
            } catch (IOException e) {
              e.printStackTrace();
            }  
            
            //Instantiate the ChunkerME class 
            ChunkerME chunkerME = new ChunkerME(chunkerModel);
             
            //Generating the chunks 
            String result[] = chunkerME.chunk(tokens, tags); 
        
            for (String s : result) {
              System.out.println(s);
            }
//            String[] tokens = tokenizer.tokenize(input);
//            
//            //Finding the names in the sentence 
//            Span nameSpans[] = nameFinder.find(tokens);
//            System.out.println(nameSpans.length);
            
            duplicateList.add(sentence);
            return sentence;
          }
        }
      }
    }
    return null;
  }

  @Override
  public void init() throws ServletException {
    inputArray = new ArrayList<String>();
    duplicateList = new ArrayList<String>();
    detectSentence();
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
  }

  public String readFileToString(String pathToFile) throws Exception {
    StringBuilder strFile = new StringBuilder();
    BufferedReader reader = new BufferedReader(new FileReader(pathToFile));
    char[] buffer = new char[512];
    int num = 0;
    while ((num = reader.read(buffer)) != -1) {
      String current = String.valueOf(buffer, 0, num);
      strFile.append(current);
      buffer = new char[512];
    }
    reader.close();
    return strFile.toString();
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

      try {
        sentences = sentenceDetector.sentDetect(readFileToString(TRAINING_DATA));
        
        InputStream tokenInputStream = new FileInputStream(TOKENIZER); 
//        InputStream personInputStream = new FileInputStream(PERSONIZER);
        TokenizerModel tokenModel = new TokenizerModel(tokenInputStream);
            
        tokenizer = new TokenizerME(tokenModel);
//        TokenNameFinderModel tokenNameFinderModel = new TokenNameFinderModel(personInputStream);
        
        //Instantiating the NameFinder class 
//        nameFinder = new NameFinderME(tokenNameFinderModel); 
        
       
        
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (modelIn != null) {
        try {
          modelIn.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

}