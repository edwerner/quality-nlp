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
import java.text.DecimalFormat;
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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
  private List<Person> personList;
  private String[] sentences;
  private final String TRAINING_DATA = "C:\\Program Files\\Apache Software Foundation\\apache-opennlp-1.8.4\\models\\en-sent.train";
  private final String TOKENIZER = "C:\\Program Files\\Apache Software Foundation\\apache-opennlp-1.8.4\\models\\en-token.bin";
  private final String PERSONIZER = "C:\\Program Files\\Apache Software Foundation\\apache-opennlp-1.8.4\\models\\en-ner-person.bin";
  private final String TRAINED_CENSUS_MODEL = "C:\\Program Files\\Apache Software Foundation\\apache-opennlp-1.8.4\\models\\census";
  private final int PERSON_COUNT = 32561;
  private DoccatModel model1;
  private DocumentCategorizerME inputCategorizer;
  private String matchFound;
  private List<String> occupationList;
  private List<String> ageList;
  private List<String> educationList;
  private List<String> maritalStatusList;
  private List<String> countryList;
  private List<String> incomeList;
  private List<String> genderList;
  private List<String> raceList;
  private HashMap<String, Integer> map;
  private HashMap<String, String> percentageMap;
  public String countryPercentage;
  public String occupationPercentage;
  public String educationPercentage;
  public String maritalStatusPercentage;
  public String incomePercentage;
  public String genderPercentage;
  public String racePercentage;
  private static DecimalFormat decimalFormat;
  private JsonObject outerJsonObject;
  private JsonObject innerJsonObject;
  private Gson gson;

  @Override
  public void init() throws ServletException {
    trainModel();
    detectSentence();
    gson = new Gson();
    map = new HashMap<String, Integer>();
    percentageMap = new HashMap<String, String>();
    decimalFormat = new DecimalFormat(".##");
    createAttributeLists();
    outerJsonObject = new JsonObject();
    System.out.println("Servlet " + this.getServletName() + " has started");
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    JsonObject jsonObject = new JsonObject();
    String json = gson.toJson(percentageMap);
    jsonObject.addProperty("data", json);
    request.setAttribute("map", jsonObject);
    request.getRequestDispatcher("/index.jsp").forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

  public void createAttributeLists() {

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
          }

          if (!educationList.contains(person.getEducationLevel())) {
            educationList.add(person.getEducationLevel());
          }

          if (!maritalStatusList.contains(person.getMaritalStatus())) {
            maritalStatusList.add(person.getMaritalStatus());
          }

          if (!countryList.contains(person.getCountry())) {
            countryList.add(person.getCountry());
          }

          if (!incomeList.contains(person.getIncome())) {
            incomeList.add(person.getIncome());
          }

          if (!genderList.contains(person.getGender())) {
            genderList.add(person.getGender());
          }

          if (!raceList.contains(person.getRace())) {
            raceList.add(person.getRace());
          }
        }
        personList.add(person);
      }
    }
    setAttributeCounts();
  }

  private void setAttributeCounts() {

    for (String country : countryList) {
      for (Person person : personList) {
        if (new String(person.getCountry()).equals(country)) {
          if (map.get(country) != null) {
            map.put(country, map.get(country) + 1);
          } else {
            map.put(country, 1);
          }
          countryPercentage = decimalFormat.format((double) map.get(country) / (double) PERSON_COUNT * 100);
          percentageMap.put(country, gson.toJson(countryPercentage));
        }
      }
    }

    for (String occupation : occupationList) {
      for (Person person : personList) {
        if (new String(person.getOccupation()).equals(occupation)) {
          if (map.get(occupation) != null) {
            map.put(occupation, map.get(occupation) + 1);
          } else {
            map.put(occupation, 1);
          }
          occupationPercentage = decimalFormat.format((double) map.get(occupation) / (double) PERSON_COUNT * 100);
          percentageMap.put(occupation, gson.toJson(occupationPercentage));
        }
      }
    }

    for (String education : educationList) {
      for (Person person : personList) {
        if (person.getEducationLevel().equals(education)) {
          if (map.get(education) != null) {
            map.put(education, map.get(education) + 1);
          } else {
            map.put(education, 1);
          }
          educationPercentage = decimalFormat.format((double) map.get(education) / (double) PERSON_COUNT * 100);
          percentageMap.put(education, gson.toJson(educationPercentage));
        }
      }
    }

    for (String maritalStatus : maritalStatusList) {
      for (Person person : personList) {
        if (person.getMaritalStatus().equals(maritalStatus)) {
          if (map.get(maritalStatus) != null) {
            map.put(maritalStatus, map.get(maritalStatus) + 1);
          } else {
            map.put(maritalStatus, 1);
          }
          maritalStatusPercentage = decimalFormat.format((double) map.get(maritalStatus) / (double) PERSON_COUNT * 100);
          percentageMap.put(maritalStatus, gson.toJson(maritalStatusPercentage));
        }
      }
    }

    for (String income : incomeList) {
      for (Person person : personList) {
        if (map.get(income) != null) {
          map.put(income, map.get(income) + 1);
        } else {
          map.put(income, 1);
        }
        incomePercentage = decimalFormat.format((double) map.get(income) / (double) PERSON_COUNT * 100);
        percentageMap.put(income, gson.toJson(incomePercentage));
      }
    }

    for (String gender : genderList) {
      for (Person person : personList) {
        if (person.getGender().equals(gender)) {
          if (map.get(gender) != null) {
            map.put(gender, map.get(gender) + 1);
          } else {
            map.put(gender, 1);
          }
          genderPercentage = decimalFormat.format((double) map.get(gender) / (double) PERSON_COUNT * 100);
          percentageMap.put(gender, gson.toJson(genderPercentage));
        }
      }
    }

    for (String race : raceList) {
      for (Person person : personList) {
        if (person.getRace().equals(race)) {
          if (map.get(race) != null) {
            map.put(race, map.get(race) + 1);
          } else {
            map.put(race, 1);
          }
          racePercentage = decimalFormat.format((double) map.get(race) / (double) PERSON_COUNT * 100);
          percentageMap.put(race, gson.toJson(racePercentage));
        }
      }
    }
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
        sentences = sentenceDetector.sentDetect(readFileToString(TRAINING_DATA));;
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