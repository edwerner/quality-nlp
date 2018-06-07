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
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizer;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
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
  private final String TOKENIZER = "C:\\Program Files\\Apache Software Foundation\\apache-opennlp-1.8.4\\models\\en-token.bin";
  private final String PERSONIZER = "C:\\Program Files\\Apache Software Foundation\\apache-opennlp-1.8.4\\models\\en-ner-person.bin";
  private final String SPEECH_MODEL = "C:\\Program Files\\Apache Software Foundation\\apache-opennlp-1.8.4\\models\\en-pos-maxent.bin";
  private final String CHUNKER = "C:\\Program Files\\Apache Software Foundation\\apache-opennlp-1.8.4\\models\\en-chunker.bin";
  private final String TRAINED_NAME_MODEL = "C:\\Program Files\\Apache Software Foundation\\apache-opennlp-1.8.4\\models\\names";
  private TokenizerME tokenizer;
  private NameFinderME nameFinder;
  private DoccatModel model1;
  private DocumentCategorizerME inputCategorizer;
  private int randomNumber;

  @Override
  public void init() throws ServletException {
    inputArray = new ArrayList<String>();
    duplicateList = new ArrayList<String>();
    trainModel();
    detectSentence();
    randomNumber = getRandomNumber();
    System.out.println("Servlet " + this.getServletName() + " has started");
  }

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

  public void trainModel() {
    InputStream dataIn = null;

    // try {
    // dataIn = new FileInputStream(TRAINING_DATA);
    // ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
    // ObjectStream<DocumentSample> sampleStream = new
    // DocumentSampleStream(lineStream);
    // int cutoff = 2;
    // int trainingIterations = 30;
    // model1 = DocumentCategorizerME.train("en", sampleStream);

    try {
      dataIn = new FileInputStream(TRAINED_NAME_MODEL);
      DoccatModel tokenizerModel = new DoccatModel(dataIn);
      inputCategorizer = new DocumentCategorizerME(tokenizerModel);

      // BufferedOutputStream modelOut = new BufferedOutputStream(new
      // FileOutputStream("names"));
      // model1.serialize(modelOut);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (dataIn != null) {
        try {
          dataIn.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public String searchSentences(String input) {

    classifyTextInput(input);

    List<String> sentenceList;
    if (sentences != null) {
      for (String sentence : sentences) {
        if (!duplicateList.contains(sentence)) {
          String[] split = sentence.split("\\s+");
          
          sentenceList = Arrays.asList(split);
          System.out.println("String: " + sentence);
          System.out.println("0: " + split[0]);
          System.out.println("1: " + split[1]);
          System.out.println("2: " + split[2]);
          System.out.println("3: " + split[3]);
//          if (sentenceList.contains(input)) {
            

            // WhitespaceTokenizer whitespaceTokenizer= WhitespaceTokenizer.INSTANCE;
            // String[] tokens = whitespaceTokenizer.tokenize(sentence);

            // //Generating the POS tags
            // //Load the parts of speech model
            // File file = new File(SPEECH_MODEL);
            // POSModel posModel = new POSModelLoader().load(file);
            //
            // //Constructing the tagger
            // POSTaggerME tagger = new POSTaggerME(posModel);
            //
            // //Generating tags from the tokens
            // String[] tags = tagger.tag(tokens);
            //
            //
            //
            //
            //
            // Span nameSpans[] = nameFinder.find(split);

            // for(Span s: nameSpans){
            // System.out.print(s.toString());
            // System.out.print(" : ");
            // for(int index = s.getStart(); index < s.getEnd(); index++) {
            // System.out.print(split[index] + " ");
            // }

            // //Loading the chunker model
            // InputStream inputStream = null;
            // try {
            // inputStream = new FileInputStream(CHUNKER);
            // } catch (FileNotFoundException e) {
            // e.printStackTrace();
            // }
            // ChunkerModel chunkerModel = null;
            // try {
            // chunkerModel = new ChunkerModel(inputStream);
            // } catch (InvalidFormatException e) {
            // e.printStackTrace();
            // } catch (IOException e) {
            // e.printStackTrace();
            // }

            // Instantiate the ChunkerME class
            // ChunkerME chunkerME = new ChunkerME(chunkerModel);

            // Generating the chunks
            // String result[] = chunkerME.chunk(tokens, tags);
            // String question = "";
            // for(int i=0;i< result.length;i++) {
            // if (tags[i].equals("WP")) {
            // System.out.println("WHO TAG");
            // }
            // System.out.println(tokens[i] + " - " + tags[i] + " - " + result[i]);
            // }

            // Generating the tagged chunk spans
            // Span[] span = chunkerME.chunkAsSpans(tokens, tags);
            //
            // for (Span s : span) {
            // System.out.println(s.toString());
            // }

            // for (String s : result) {
            // System.out.println(s);
            // }
            // String[] tokens = tokenizer.tokenize(input);
            //
            // //Finding the names in the sentence
            // System.out.println();
            // }
            duplicateList.add(sentence);
            return sentence;
//          }
        }
      }
    }
    return null;
  }
  
  private int getRandomNumber() {
    Random rand = new Random(); 
    int value = rand.nextInt(5162);
    return value;
  }

  private void classifyTextInput(String input) {
    String inputCaps = input.toUpperCase();
//    inputCategorizer = new DocumentCategorizerME(model1);
    double[] outcomes = inputCategorizer.categorize(inputCaps);
    String category = inputCategorizer.getCategory(randomNumber);
//     String category = inputCategorizer.getBestCategory(outcomes);

    if (category.equalsIgnoreCase(input)) {
      System.out.println("NAME MATCH: " + category);
    }

//    System.out.println("**********CATEGORY: " + category);

    // if (category.equalsIgnoreCase("1")) {
    // System.out.println("The question was about Alice");
    // } else {
    // System.out.println("The question was not about Alice");
    // }
  }

  @Override
  public void destroy() {
    System.out.println("Servlet " + this.getServletName() + " has stopped");
  }

  // @SuppressWarnings("deprecation")
  // public void trainModel() throws IOException {
  // Charset charset = Charset.forName("UTF-8");
  // ObjectStream<String> lineStream = null;
  // try {
  // lineStream = new PlainTextByLineStream(new FileInputStream(
  // "C:\\Program Files\\Apache Software
  // Foundation\\apache-opennlp-1.8.4\\models\\en-sent.train"), charset);
  // } catch (FileNotFoundException e) {
  // e.printStackTrace();
  // }
  // ObjectStream<SentenceSample> sampleStream = new
  // SentenceSampleStream(lineStream);
  // SentenceModel model;
  // try {
  // model = SentenceDetectorME.train("en", sampleStream, true, null,
  // TrainingParameters.defaultParams());
  // } finally {
  // sampleStream.close();
  // }
  // }

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

  @SuppressWarnings("deprecation")
  public void detectSentence() {
    InputStream modelIn = null;
    InputStream dataIn = null;

    // try {
    // modelIn = new FileInputStream(
    // "C:\\Program Files\\Apache Software
    // Foundation\\apache-opennlp-1.8.4\\models\\en-sent.bin");
    //
    // System.out.println("******Train Data Start********");
    // dataIn = new FileInputStream(TRAINING_DATA);
    // ObjectStream<String> lineStream = null;
    // try {
    // lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
    // } catch (UnsupportedEncodingException e) {
    // e.printStackTrace();
    // }
    // ObjectStream<DocumentSample> sampleStream = new
    // DocumentSampleStream(lineStream);
    //
    // try {
    // model1 = DocumentCategorizerME.train("en", sampleStream);
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    //
    // // save the model to local
    // BufferedOutputStream modelOut = new BufferedOutputStream(new
    // FileOutputStream("naive-bayes.bin"));
    // try {
    // model1.serialize(modelOut);
    // } catch (IOException e) {
    // e.printStackTrace();
    // }

    // test the model file by subjecting it to prediction
    // DocumentCategorizer doccat = new DocumentCategorizerME(model1);
    // String[] docWords = "Alice followed the White Rabbit".replaceAll("[^A-Za-z]",
    // " ").split(" ");
    // double[] aProbs = doccat.categorize(docWords);

    // // print the probabilities of the categories
    // System.out
    // .println("\n---------------------------------\nCategory :
    // Probability\n---------------------------------");
    // for (int i = 0; i < doccat.getNumberOfCategories(); i++) {
    // System.out.println(doccat.getCategory(i) + " : " + aProbs[i]);
    // }
    // System.out.println("---------------------------------");
    //
    // System.out
    // .println("\n" + doccat.getBestCategory(aProbs) + " : is the predicted
    // category for the given sentence.");
    //
    // System.out.println("******Train Data End********");

    // } catch (FileNotFoundException e1) {
    // e1.printStackTrace();
    // }

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
        InputStream personInputStream = new FileInputStream(PERSONIZER);
        TokenizerModel tokenModel = new TokenizerModel(tokenInputStream);

        tokenizer = new TokenizerME(tokenModel);
        TokenNameFinderModel tokenNameFinderModel = new TokenNameFinderModel(personInputStream);

        // Instantiating the NameFinder class
        nameFinder = new NameFinderME(tokenNameFinderModel);
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