package group37.compiler.frontend;

import java.util.ArrayList;
import java.util.List;

public class ErrorLogger {

  private static String[] lines;

  private ErrorLogger() {}


  public static void addCode(String code) {
    lines = code.split("\\r?\\n");
  }

  private static List<ErrorMessage> errorMessages;

  public static void addMessage(ErrorMessage errorMessage) {
    if (errorMessages == null) {
      errorMessages = new ArrayList<>();
    }
    errorMessages.add(errorMessage);
  }

  public static void printMessages() {
    for (ErrorMessage e : errorMessages) {
      printMessage(e);
    }
  }

  private static void printMessage(ErrorMessage e) {
    System.err.println(e.getErrorType()
        + " ERROR at (" + e.getLineNumber() + ":" + e.getColumnNumber() + "): " + e.getMessage());
    if (e.getLineNumber() < lines.length) {
      System.err.println(lines[e.getLineNumber()-1]);
      for (int i = 0; i < e.getColumnNumber(); i++) {
        System.err.print(" ");
      }
      System.err.println("^");
    }
  }
}
