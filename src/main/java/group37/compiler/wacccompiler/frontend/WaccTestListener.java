package group37.compiler.wacccompiler.frontend;

import group37.compiler.frontend.ErrorLogger;
import group37.compiler.frontend.ErrorMessage;
import org.antlr.v4.runtime.*;

import java.util.Collections;
import java.util.List;

public class WaccTestListener extends BaseErrorListener {

  @Override
  public void syntaxError(Recognizer<?,?> recognizer,
                          Object offendingSymbol,
                          int line, int charPositionInLine,
                          String msg,
                          RecognitionException e) {
    if (true) {
      ErrorMessage errorMessage = new ErrorMessage(line, charPositionInLine, ErrorMessage.ErrorType.SYNTAX, msg);
      ErrorLogger.addMessage(errorMessage);
    } else {
      String output = "";
      if (offendingSymbol != null) {

        List<String> stack = ((Parser) recognizer).getRuleInvocationStack();
        Collections.reverse(stack);
        System.err.print("Syntax error: ");
        System.err.println("line " + line + ":" + charPositionInLine + " " + msg);
        output += msg + "\n";
        TokenStream tokens = ((Parser) recognizer).getInputStream();
        String[] lines = tokens.getTokenSource().getInputStream().toString().split("\n");
        if (line < lines.length) {
          String errLine = lines[line - 1];
          System.err.println(errLine);
          output += "\n " + errLine + "\n ";
          int begin = ((Token) offendingSymbol).getStartIndex();
          int end = ((Token) offendingSymbol).getStopIndex();
          for (int i = 0; i < charPositionInLine; i++) {
            System.err.print(" ");
            output += " ";
          }
          for (int i = begin; i < end + 1; i++) {
            System.err.print("^");
            output += "^";
          }
          System.err.println();
          output += "\n";
        }
      } else {

        System.err.print("Syntax error: ");
        System.err.println("line " + line + ":" + charPositionInLine + " " + msg);
        output += msg + "\n";
        CharStream chars = ((Lexer) recognizer).getInputStream();
        String[] lines = chars.toString().split("\n");
        if (line < lines.length) {
          String errLine = lines[line - 1];
          System.err.println(errLine);
          output += "\n" + errLine + "\n ";
          for (int i = 0; i < charPositionInLine; i++) {
            System.err.print(" ");
            output += " ";
          }
          System.err.println("^");
          output += "\n^";
        }
      }
    }
  }
}
