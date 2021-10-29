package group37.compiler.wacccompiler.ast.expression.literal;

import group37.compiler.wacccompiler.ast.expression.AbstractExpressionNode;
import group37.compiler.wacccompiler.ast.types.TypeNode;
import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.backend.arminstructions.ArmProgram.LibFunction;
import group37.compiler.wacccompiler.symboltable.SymbolTable;

public class PairLiteralNode extends AbstractExpressionNode implements LiteralNode {

  public PairLiteralNode(int line, int col) {
    super(line, col);
  }

  public enum PairLiteral {
    NULL
  }

  @Override
  public LibFunction getPrintFunction(SymbolTable context) {
    return LibFunction.PRINT_REFERENCE;
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitPairLiteralNode(this);
  }

  private PairLiteral literal = PairLiteral.NULL;

  @Override
  public boolean checkValid(SymbolTable context) {
    return true;
  }

  @Override
  public TypeNode expType(SymbolTable context) {
    return null;
  }

}
