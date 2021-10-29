package group37.compiler.wacccompiler.ast.statements.assignment;

import group37.compiler.wacccompiler.ast.AbstractWaccNode;
import group37.compiler.wacccompiler.ast.expression.IdentifierNode;
import group37.compiler.wacccompiler.ast.statements.StatementNode;
import group37.compiler.wacccompiler.ast.types.TypeNode;
import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.symboltable.STEntry;
import group37.compiler.wacccompiler.symboltable.SymbolTable;

public class IdentAssignNode extends AbstractWaccNode implements StatementNode {

  private final TypeNode type;
  private final IdentifierNode id;
  private final AssignRhsNode rhs;

  public IdentAssignNode(int line, int col, TypeNode type,
      IdentifierNode id, AssignRhsNode rhs) {
    super(line, col);
    this.type = type;
    this.id = id;
    this.rhs = rhs;
  }

  public TypeNode getType() {
    return type;
  }

  public IdentifierNode getId() {
    return id;
  }

  public AssignRhsNode getRhs() {
    return rhs;
  }

  @Override
  public boolean checkValid(SymbolTable context) {
    if (rhs == null) {
      return false;
    }
    boolean defined = context.inCurScope(id.getId());
    if (!defined) {
      if (rhs.checkValid(context) && rhs.expType(context).equals(type)) {
        context.add(id.getId(), new STEntry(type));
        return true;
      }
    }
    return false;
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitIdentAssignNode(this);
  }
}
