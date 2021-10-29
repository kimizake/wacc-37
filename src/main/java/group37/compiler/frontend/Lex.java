package group37.compiler.frontend;

public class Lex<LexType> {

  private LexType lex;

  public Lex(LexType lex) {
    this.lex = lex;
  }

  public LexType getLex() {
    return lex;
  }

}
