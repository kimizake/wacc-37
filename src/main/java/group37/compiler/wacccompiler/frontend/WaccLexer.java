package group37.compiler.wacccompiler.frontend;

import group37.antlr.WACCLexer;
import group37.compiler.frontend.Lex;
import group37.compiler.frontend.Lexer;
import org.antlr.v4.runtime.*;

public class WaccLexer implements Lexer<BufferedTokenStream> {

  @Override
  public Lex<BufferedTokenStream> performLex(String code) {
    CharStream input = CharStreams.fromString(code);

    // create lexer that feeds off the char stream
    WACCLexer lexer = new WACCLexer(input);
    lexer.removeErrorListeners();
    lexer.addErrorListener(new WaccTestListener());
    // buffer of tokens pulled from lexer
    BufferedTokenStream tokens = new CommonTokenStream(lexer);

    return new Lex<BufferedTokenStream>(tokens);
  }

}
