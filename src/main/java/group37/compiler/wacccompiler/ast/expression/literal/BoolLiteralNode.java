package group37.compiler.wacccompiler.ast.expression.literal;

import group37.compiler.wacccompiler.ast.expression.AbstractExpressionNode;
import group37.compiler.wacccompiler.ast.types.BaseTypeNode;
import group37.compiler.wacccompiler.ast.types.BaseTypeNode.BaseType;
import group37.compiler.wacccompiler.ast.types.TypeNode;
import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.backend.arminstructions.ArmProgram.LibFunction;
import group37.compiler.wacccompiler.symboltable.SymbolTable;

public class BoolLiteralNode extends AbstractExpressionNode implements LiteralNode {

  private final boolean literal;

  @Override
  public LibFunction getPrintFunction(SymbolTable context) {
    return LibFunction.PRINT_BOOL;
  }

  public boolean getLiteral() {
    return literal;
  }

  public BoolLiteralNode(int line, int col, boolean literal) {
    super(line, col);
    this.literal = literal;
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitBoolLiteralNode(this);
  }

  @Override
  public boolean checkValid(SymbolTable context) {
    return true;
  }

  @Override
  public TypeNode expType(SymbolTable context) {
    return new BaseTypeNode(BaseType.BOOL);
  }
}
