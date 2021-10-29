package group37.compiler.wacccompiler.ast.statements;

public class StatementMatcher {

  public final static StatementNode RETURN_NODE = new KeywordStatementNode(
      0, 0, Keyword.RETURN, null
  );
  public final static StatementNode EXIT_NODE = new KeywordStatementNode(
      0, 0, Keyword.EXIT, null
  );

}
