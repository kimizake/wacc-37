package group37.compiler.frontend;

public interface Lexer<LexType> {

  Lex<LexType> performLex(String code);

}
