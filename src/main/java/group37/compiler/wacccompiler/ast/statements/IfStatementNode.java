package group37.compiler.wacccompiler.ast.statements;

import static group37.compiler.wacccompiler.ast.statements.StatementMatcher.RETURN_NODE;
import static group37.compiler.wacccompiler.ast.types.TypeMatcher.BOOL_NODE;

import group37.compiler.wacccompiler.ast.expression.ExpressionNode;
import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.symboltable.SymbolTable;

public class IfStatementNode extends AbstractStatementNode {

  private final ExpressionNode condition;
  private final StatementNode trueBlock;
  private final StatementNode falseBlock;

  public IfStatementNode(int line, int col,
      ExpressionNode condition,
      StatementNode trueBlock,
      StatementNode falseBlock) {
    super(line, col);
    this.condition = condition;
    this.trueBlock = trueBlock;
    this.falseBlock = falseBlock;
  }

  public ExpressionNode getCondition() {
    return condition;
  }

  @Override
  public int countReturns() {
    return trueBlock.countReturns() + falseBlock.countReturns();
  }

  @Override
  public boolean checkValidReturn() {
    return trueBlock.checkValidReturn() && falseBlock.checkValidReturn();
  }

  public StatementNode getFalseBlock() {
    return falseBlock;
  }

  public StatementNode getTrueBlock() {
    return trueBlock;
  }

  @Override
  public boolean checkValid(SymbolTable context) {
    // call checkvalid on the expression node
    boolean validCondition = condition.expType(context).equals(BOOL_NODE);
    // call checkvalid on the true and false blocks
    boolean validTrueStatement = trueBlock.checkValid(context);
    boolean validFalseStatement = falseBlock.checkValid(context);
    return validCondition && validTrueStatement && validFalseStatement;
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitIfStatementNode(this);
  }
}
