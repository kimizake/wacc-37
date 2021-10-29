package group37.compiler.wacccompiler.ast.statements.assignment;

import group37.compiler.wacccompiler.ast.AbstractWaccNode;
import group37.compiler.wacccompiler.ast.statements.StatementNode;
import group37.compiler.wacccompiler.ast.types.FunctionType;
import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.symboltable.SymbolTable;

public class AssignNode extends AbstractWaccNode implements StatementNode {

  private final AssignLhsNode lhs;
  private final AssignRhsNode rhs;

  public AssignNode(int line, int col,
      AssignLhsNode lhs, AssignRhsNode rhs) {
    super(line, col);
    this.lhs = lhs;
    this.rhs = rhs;
  }

  public AssignLhsNode getLhs() {
    return lhs;
  }

  public AssignRhsNode getRhs() {
    return rhs;
  }

  @Override
  public boolean checkValid(SymbolTable context) {
    if (lhs.expType(context) instanceof FunctionType) {
      return false;
    }
    if (lhs.expType(context) == null || rhs.expType(context) == null){
      return false;
    }
    if (!lhs.expType(context).equals(rhs.expType(context))) {
      return false;
    }
    return lhs.checkValid(context) && rhs.checkValid(context);
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitAssignNode(this);
  }

}
