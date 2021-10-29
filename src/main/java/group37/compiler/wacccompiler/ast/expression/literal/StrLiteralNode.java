package group37.compiler.wacccompiler.ast.expression.literal;

import static group37.compiler.wacccompiler.ast.types.TypeMatcher.STRING_NODE;

import group37.compiler.wacccompiler.ast.expression.AbstractExpressionNode;
import group37.compiler.wacccompiler.ast.types.TypeNode;
import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.backend.arminstructions.ArmProgram.LibFunction;
import group37.compiler.wacccompiler.backend.arminstructions.Label;
import group37.compiler.wacccompiler.symboltable.SymbolTable;

public class StrLiteralNode extends AbstractExpressionNode implements LiteralNode {

  private final String literal;

  @Override
  public LibFunction getPrintFunction(SymbolTable context) {
    return LibFunction.PRINT_STRING;
  }

  public String getLiteral() {
    return literal;
  }

  public StrLiteralNode(int line, int col, String literal) {
    super(line, col);
    this.literal = literal;
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitStrLiteralNode(this);
  }

  @Override
  public boolean checkValid(SymbolTable context) {
    return true;
  }

  @Override
  public TypeNode expType(SymbolTable context) {
    return STRING_NODE;
  }

}
