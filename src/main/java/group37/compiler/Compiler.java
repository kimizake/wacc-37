package group37.compiler;

import group37.compiler.frontend.*;

public abstract class Compiler<LexType, ParseType> {

  private final Parser<LexType, ParseType> parser;
  private final Lexer<LexType> lexer;
  private final SemanticChecker<ParseType> semanticChecker;

  protected Compiler(
      Parser<LexType, ParseType> parser,
      Lexer<LexType> lexer,
      SemanticChecker<ParseType> semanticChecker
  ) {
    if (parser == null || lexer == null || semanticChecker == null) {
      throw new IllegalArgumentException();
    } else {
      this.lexer = lexer;
      this.parser = parser;
      this.semanticChecker = semanticChecker;
    }
  }

  public boolean checkValid(Parse<ParseType> parse) {
    return semanticChecker.checkValid(parse);
  }

  public Lex<LexType> performLex(String code) {
    return lexer.performLex(code);
  }

  public Parse<ParseType> parse(Lex<LexType> lex) {
    return parser.parse(lex);
  }

}
