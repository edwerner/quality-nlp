public class SentenceDetection_RE {  
   public static void main(String args[]){ 
     
      String sentence = "Hello World"; 
     
      String regex = "[.?!]";      
      String[] splitString = (sentence.split(regex));     
      for (String string : splitString)   
         System.out.println(string);      
   }
}
