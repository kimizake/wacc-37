package group37.compiler.wacccompiler.ast.expression.literal;

import group37.compiler.wacccompiler.ast.expression.AbstractExpressionNode;
import group37.compiler.wacccompiler.ast.types.BaseTypeNode;
import group37.compiler.wacccompiler.ast.types.BaseTypeNode.BaseType;
import group37.compiler.wacccompiler.ast.types.TypeNode;
import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.backend.arminstructions.ArmProgram.LibFunction;
import group37.compiler.wacccompiler.backend.arminstructions.Label;
import group37.compiler.wacccompiler.symboltable.SymbolTable;

public class CharLiteralNode extends AbstractExpressionNode implements LiteralNode {

  private final char literal;

  @Override
  public LibFunction getPrintFunction(SymbolTable context) {
    return LibFunction.PUT_CHAR;
  }

  public CharLiteralNode(int line, int col, char literal) {
    super(line, col);
    this.literal = literal;
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitCharLiteralNode(this);
  }

  public char getLiteral() {
    return literal;
  }

  @Override
  public boolean checkValid(SymbolTable context) {
    return true;
  }

  @Override
  public TypeNode expType(SymbolTable context){
    return new BaseTypeNode(BaseType.CHAR);
  }
}
