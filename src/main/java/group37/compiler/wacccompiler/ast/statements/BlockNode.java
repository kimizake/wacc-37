package group37.compiler.wacccompiler.ast.statements;

import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.symboltable.SymbolTable;
import static group37.compiler.wacccompiler.ast.statements.StatementMatcher.RETURN_NODE;

public class BlockNode extends AbstractStatementNode {

  private final StatementNode statement;

  public BlockNode(int line, int col,
      StatementNode statement) {
    super(line, col);
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

  @Override
  public boolean checkValid(SymbolTable context) {
    //add a new context to the symbol table for a new scope and then...
    context.enterScope();
    boolean out = statement.checkValid(context);
    context.exitScope();
    return out;
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitBlockNode(this);
  }
}
