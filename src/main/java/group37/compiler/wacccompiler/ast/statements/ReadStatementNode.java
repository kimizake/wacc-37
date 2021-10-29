package group37.compiler.wacccompiler.ast.statements;

import static group37.compiler.wacccompiler.ast.types.TypeMatcher.CHAR_NODE;
import static group37.compiler.wacccompiler.ast.types.TypeMatcher.INT_NODE;

import group37.compiler.wacccompiler.ast.statements.assignment.AssignLhsNode;
import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.symboltable.SymbolTable;

public class ReadStatementNode extends AbstractStatementNode {

  private final AssignLhsNode lhs;

  public ReadStatementNode(int line, int col,
      AssignLhsNode lhs) {
    super(line, col);
    this.lhs = lhs;
  }

  public AssignLhsNode getLhs() {
    return lhs;
  }

  @Override
  public boolean checkValid(SymbolTable context) {
    //call checkvalid on the assignlhs using the context
    boolean validLhs = lhs.checkValid(context);
    boolean correctType =
        lhs.expType(context).equals(INT_NODE) ||
        lhs.expType(context).equals(CHAR_NODE);
    return validLhs && correctType;
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitReadStatementNode(this);
  }
}
