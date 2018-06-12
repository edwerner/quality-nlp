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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
  private List<Person> personList;
  private String[] sentences;
  private final String TRAINING_DATA = "C:\\Program Files\\Apache Software Foundation\\apache-opennlp-1.8.4\\models\\en-sent.train";
  private final String TOKENIZER = "C:\\Program Files\\Apache Software Foundation\\apache-opennlp-1.8.4\\models\\en-token.bin";
  private final String PERSONIZER = "C:\\Program Files\\Apache Software Foundation\\apache-opennlp-1.8.4\\models\\en-ner-person.bin";
  private final String SPEECH_MODEL = "C:\\Program Files\\Apache Software Foundation\\apache-opennlp-1.8.4\\models\\en-pos-maxent.bin";
  private final String CHUNKER = "C:\\Program Files\\Apache Software Foundation\\apache-opennlp-1.8.4\\models\\en-chunker.bin";
  private final String TRAINED_NAME_MODEL = "C:\\Program Files\\Apache Software Foundation\\apache-opennlp-1.8.4\\models\\names";
  private final String TRAINED_CENSUS_MODEL = "C:\\Program Files\\Apache Software Foundation\\apache-opennlp-1.8.4\\models\\census";
  private TokenizerME tokenizer;
  private NameFinderME nameFinder;
  private DoccatModel model1;
  private DocumentCategorizerME inputCategorizer;
  private int randomNumber;
  private String nameMatch;
  private String matchString;
  private String matchFound;
  private List<String> occupationList;
  private List<String> ageList;
  private List<String> educationList;
  private List<String> maritalStatusList;
  private List<String> countryList;
  private List<String> incomeList;
  private List<String> genderList;
  private List<String> raceList;
  private List<String> occupationCountList;
  private HashMap<String, Integer> map;

  @Override
  public void init() throws ServletException {
    inputArray = new ArrayList<String>();
    duplicateList = new ArrayList<String>();
    trainModel();
    detectSentence();
    randomNumber = getRandomNumber();
    map = new HashMap<String, Integer>();
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

    searchSentences(input);

    if (input != null) {
      inputArray.add(input);
      inputArray.add(checkForNameMatch(input));
      Collections.reverse(inputArray);
    }
    request.setAttribute("matchFound", matchFound);
    request.setAttribute("inputArray", inputArray);
    request.getRequestDispatcher("/index.jsp").forward(request, response);
  };

  public void trainModel() {
    InputStream dataIn = null;

    try {

      // dataIn = new FileInputStream(TRAINING_DATA);
      // ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
      // ObjectStream<DocumentSample> sampleStream = new
      // DocumentSampleStream(lineStream);
      // int cutoff = 2;
      // int trainingIterations = 30;
      // model1 = DocumentCategorizerME.train("en", sampleStream);

      dataIn = new FileInputStream(TRAINED_CENSUS_MODEL);
      DoccatModel tokenizerModel = new DoccatModel(dataIn);
      inputCategorizer = new DocumentCategorizerME(tokenizerModel);

      // BufferedOutputStream modelOut = new BufferedOutputStream(new
      // FileOutputStream("census"));
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

  public void searchSentences(String input) {

    personList = new ArrayList<Person>();
    ageList = new ArrayList<String>();
    occupationList = new ArrayList<String>();
    educationList = new ArrayList<String>();
    maritalStatusList = new ArrayList<String>();
    countryList = new ArrayList<String>();
    incomeList = new ArrayList<String>();
    genderList = new ArrayList<String>();
    raceList = new ArrayList<String>();

    if (sentences != null) {

      for (String sentence : sentences) {

        String[] split = sentence.split(",");

        Person person = new Person();

        for (int i = 0; i < split.length; i++) {

          person.setAge(split[0]);
          person.setOccupation(split[1]);
          person.setEducationLevel(split[3]);
          person.setMaritalStatus(split[5]);
          person.setCountry(split[13]);
          person.setIncome(split[14].replaceAll("\\.", ""));
          person.setGender(split[9]);
          person.setRace(split[8]);

          ageList.add(person.getAge());

          if (!occupationList.contains(person.getOccupation())) {
            occupationList.add(person.getOccupation());
            // System.out.println("OCCUPATION: " + person.getOccupation());
          }

          if (!educationList.contains(person.getEducationLevel())) {
            educationList.add(person.getEducationLevel());
            // System.out.println("EDUCATION: " + person.getEducationLevel());
          }

          if (!maritalStatusList.contains(person.getMaritalStatus())) {
            maritalStatusList.add(person.getMaritalStatus());
            // System.out.println("MARITAL STATUS: " + person.getMaritalStatus());
          }

          if (!countryList.contains(person.getCountry())) {
            countryList.add(person.getCountry());
            // System.out.println("COUNTRY: " + person.getCountry());
          }

          if (!incomeList.contains(person.getIncome())) {
            incomeList.add(person.getIncome());
            // System.out.println("INCOME: " + person.getIncome());
          }

          if (!genderList.contains(person.getGender())) {
            genderList.add(person.getGender());
            // System.out.println("GENDER: " + person.getGender());
          }

          if (!raceList.contains(person.getRace())) {
            raceList.add(person.getRace());
            // System.out.println("RACE: " + person.getRace());
          }
        }
        personList.add(person);
      }
    }
    setAttributeCounts();
  }

  private void setAttributeCounts() {

    int occupationCount = 0;
    int educationCount = 0;
    int maritalCount = 0;
    int countryCount = 0;
    int incomeCount = 0;
    int genderCount = 0;
    int raceCount = 0;

    // map.put("marital", maritalCount);
    // map.put("country", countryCount);
    // map.put("income", incomeCount);
    // map.put("gender", genderCount);
    // map.put("race", raceCount);
    // map.put("occupation", occupationCount);
    // map.put("education", educationCount);

    // TODO: create nested hashmap

    for (String occupation : occupationList) {
      for (Person person : personList) {
        if (person.getOccupation() != null) {
          if (person.getOccupation() == occupation) {
            occupationCount++;
            map.put(occupation, occupationCount);
            System.out.println("OCCUPATION COUNT: " + occupationCount);
          }
        }
      }
    }

    for (String education : educationList) {
      for (Person person : personList) {
        if (person.getEducationLevel() != null) {
          if (person.getEducationLevel() == education) {
            educationCount++;
            map.put(education, educationCount);
            System.out.println("EDUCATION COUNT: " + educationCount);
          }
        }
      }
    }

    for (String maritalStatus : maritalStatusList) {
      for (Person person : personList) {
        if (person.getMaritalStatus() != null) {
          if (person.getMaritalStatus() == maritalStatus) {
            maritalCount++;
            map.put(maritalStatus, maritalCount);
            System.out.println("MARITAL COUNT: " + maritalCount);
          }
        }
      }
    }

    for (String country : countryList) {
      for (Person person : personList) {
        if (person.getCountry() != null) {
          if (person.getCountry() == country) {
            countryCount++;
            map.put(country, countryCount);
            System.out.println("COUNTRY COUNT: " + countryCount);
          }
        }
      }
    }

    for (String income : incomeList) {
      for (Person person : personList) {
        if (person.getIncome() != null) {
          if (person.getIncome() == income) {
            incomeCount++;
            map.put(income, incomeCount);
            System.out.println("INCOME COUNT: " + incomeCount);
          }
        }
      }
    }

    for (String gender : genderList) {
      for (Person person : personList) {
        if (person.getGender() != null) {
          if (person.getGender() == gender) {
            genderCount++;
            map.put(gender, genderCount);
            System.out.println("GENDER COUNT: " + genderCount);
          }
        }
      }
    }

    for (String race : raceList) {
      for (Person person : personList) {
        if (person.getRace() != null) {
          if (person.getRace() == race) {
            raceCount++;
            map.put(race, raceCount);
            System.out.println("RACE COUNT: " + raceCount);
          }
        }
      }
    }

    System.out.println("SIZE: " + genderList.size());
    Iterator it = map.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry pair = (Map.Entry) it.next();
      System.out.println(pair.getKey() + " = " + pair.getValue());
      it.remove(); // avoids a ConcurrentModificationException
    }
  }

  private int getRandomNumber() {
    Random rand = new Random();
    int value = rand.nextInt(1);
    return value;
  }

  public String checkForNameMatch(String name) {
    if (name.toUpperCase().equals(matchString)) {
      nameMatch = "Name match found!";
      matchFound = "Name Match Found!";
    } else {
      nameMatch = "No name match found.";
      matchFound = null;
    }
    return nameMatch;
  }

  @Override
  public void destroy() {
    System.out.println("Servlet " + this.getServletName() + " has stopped");
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

  @SuppressWarnings("deprecation")
  public void detectSentence() {
    InputStream modelIn = null;
    InputStream dataIn = null;

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