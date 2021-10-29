package group37.compiler.wacccompiler.ast.statements;

import static group37.compiler.wacccompiler.ast.types.TypeMatcher.INT_NODE;

import group37.compiler.wacccompiler.ast.expression.ExpressionNode;
import group37.compiler.wacccompiler.ast.types.ArrayTypeNode;
import group37.compiler.wacccompiler.ast.types.PairTypeNode;
import group37.compiler.wacccompiler.ast.types.TypeNode;
import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.symboltable.SymbolTable;

public class KeywordStatementNode extends AbstractStatementNode {

  private final Keyword keyword;
  private final ExpressionNode expression;

  public KeywordStatementNode(int line, int col,
      Keyword keyword, ExpressionNode expression) {
    super(line, col);
    this.keyword = keyword;
    this.expression = expression;
  }

  @Override
  public int countReturns() {
    return keyword == Keyword.RETURN ? 1 : 0;
  }

  @Override
  public boolean checkValidReturn() {
    return keyword == Keyword.RETURN;
  }

  public ExpressionNode getExpression() {
    return expression;
  }

  public Keyword getKeyword() {
    return keyword;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof KeywordStatementNode && (((KeywordStatementNode) o).keyword == keyword);
  }

  @Override
  public boolean checkValid(SymbolTable context) {
    TypeNode expType = expression.expType(context);
    if (expType == null) {
      return false;
    }
    switch (keyword) {
      case EXIT:
        return expType.equals(INT_NODE);
      case FREE:
        expType = expression.expType(context);
        return expType instanceof ArrayTypeNode || expType instanceof PairTypeNode;
      case RETURN:
        if (context.getCallingFunction() == null) {
          return false;
        } else {
          return expType.equals(context.getCallingFunction().returnType());
        }
      case PRINTLN:
        return expression.checkValid(context);
      case PRINT:
        return expression.checkValid(context);
    }
    return false;
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitKeywordStatementNode(this);
  }
}
