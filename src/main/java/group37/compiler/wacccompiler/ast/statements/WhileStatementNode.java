package group37.compiler.wacccompiler.ast.statements;

import static group37.compiler.wacccompiler.ast.statements.StatementMatcher.RETURN_NODE;
import static group37.compiler.wacccompiler.ast.types.TypeMatcher.BOOL_NODE;

import group37.compiler.wacccompiler.ast.expression.ExpressionNode;
import group37.compiler.wacccompiler.ast.types.TypeNode;
import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.symboltable.SymbolTable;

public class WhileStatementNode extends AbstractStatementNode {

  private final ExpressionNode condition;
  private final StatementNode statement;

  public WhileStatementNode(int line, int col,
      ExpressionNode condition,
      StatementNode statement) {
    super(line, col);
    this.condition = condition;
    this.statement = statement;
  }

  @Override
  public int countReturns() {
    return statement.countReturns();
  }

  @Override
  public boolean checkValidReturn() {
    return statement.checkValidReturn();
  }

  public StatementNode getStatement() {
    return statement;
  }

  public ExpressionNode getCondition() {
    return condition;
  }

  @Override
  public boolean checkValid(SymbolTable context) {
    //call checkvalid on the expression and then checkvalid on the statement
    TypeNode expType = condition.expType(context);
    if (expType == null) {
      return false;
    }
    boolean validCondition = expType.equals(BOOL_NODE) ;
    boolean validStatement = statement.checkValid(context);
    return validCondition && validStatement;
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitWhileStatementNode(this);
  }
}
