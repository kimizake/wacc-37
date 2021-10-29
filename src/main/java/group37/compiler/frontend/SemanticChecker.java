package group37.compiler.frontend;

public interface SemanticChecker<ParseType> {

  boolean checkValid(Parse<ParseType> parse);

}
