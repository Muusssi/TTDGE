package ttdge;

public class TTDGEUtils {

  public static final String VOWELS = "aeiou";

  public static boolean begins_with_vowel(String string) {
    if (VOWELS.indexOf(Character.toLowerCase(string.charAt(0))) != -1) {
        return true;
    }
    return false;
  }

  public static String article_for(String string) {
    if (VOWELS.indexOf(Character.toLowerCase(string.charAt(0))) != -1) {
        return "an";
    }
    return "a";
  }

}
