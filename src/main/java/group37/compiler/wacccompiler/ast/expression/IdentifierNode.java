package group37.compiler.wacccompiler.ast.expression;

import group37.compiler.wacccompiler.ast.statements.assignment.AssignLhsNode;
import group37.compiler.wacccompiler.ast.types.TypeNode;
import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.backend.arminstructions.ArmProgram.LibFunction;
import group37.compiler.wacccompiler.symboltable.SymbolTable;

public class IdentifierNode extends AbstractExpressionNode implements AssignLhsNode {

  private final String id;

  @Override
  public LibFunction getPrintFunction(SymbolTable context) {
    return context.getType(id).getTypeNodePrintFunc();
  }

  @Override
  public LibFunction getReadFunction(SymbolTable context) {
    return context.getType(id).getTypeNodeReadFunc();
  }

  public IdentifierNode(int line, int col, String id) {
    super(line, col);
    this.id = id;
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitIdentifierNode(this);
  }

  public String getId() {
    return id;
  }

  @Override
  public boolean checkValid(SymbolTable context) {
    return context.defined(id);
  }

  @Override
  public TypeNode expType(SymbolTable context) {
    return context.getType(id);
  }
}
