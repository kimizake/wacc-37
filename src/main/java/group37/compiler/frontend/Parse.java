package group37.compiler.frontend;

public class Parse<ParseType> {

  private ParseType parse;

  public Parse(ParseType lex) {
    this.parse = lex;
  }

  public ParseType getParse() {
    return parse;
  }

}
