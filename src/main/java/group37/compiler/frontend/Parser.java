package group37.compiler.frontend;

public interface Parser<LexType, ParseType> {

  Parse<ParseType> parse(Lex<LexType> lex);

}
