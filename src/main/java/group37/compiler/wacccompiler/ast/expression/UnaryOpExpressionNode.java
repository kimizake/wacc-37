package group37.compiler.wacccompiler.ast.expression;

import static group37.compiler.wacccompiler.ast.types.TypeMatcher.BOOL_NODE;
import static group37.compiler.wacccompiler.ast.types.TypeMatcher.CHAR_NODE;
import static group37.compiler.wacccompiler.ast.types.TypeMatcher.INT_NODE;

import group37.compiler.wacccompiler.ast.types.TypeNode;
import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.backend.arminstructions.ArmProgram.LibFunction;
import group37.compiler.wacccompiler.backend.arminstructions.Label;
import group37.compiler.wacccompiler.symboltable.SymbolTable;

public class UnaryOpExpressionNode extends AbstractExpressionNode {

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitUnaryOpExpressionNode(this);
  }

  public enum UnaryOp {
    LOGICAL_NEGATION,
    MATHEMATICAL_NEGATION,
    LEN,
    ORD,
    CHR
  }

  @Override
  public LibFunction getPrintFunction(SymbolTable context) {
    switch (op) {
      case ORD: return LibFunction.PRINT_INT;
      case MATHEMATICAL_NEGATION: return LibFunction.PRINT_INT;
      case LOGICAL_NEGATION: return LibFunction.PRINT_BOOL;
      case LEN: return LibFunction.PRINT_INT;
      case CHR: return LibFunction.PUT_CHAR;
    }
    return null;
  }

  private final UnaryOp op;
  private ExpressionNode expression;

  public UnaryOpExpressionNode(int line, int col,
      UnaryOp op) {
    super(line, col);
    this.op = op;
  }

  public void setExpression(ExpressionNode expression) {
    this.expression = expression;
  }

  public ExpressionNode getExpression() {
    return expression;
  }

  public UnaryOp getOp() {
    return op;
  }

  @Override
  public boolean checkValid(SymbolTable context) {
    return false;
  }

  @Override
  public TypeNode expType(SymbolTable context) {
    switch (op) {
      case LOGICAL_NEGATION: return BOOL_NODE;
      case MATHEMATICAL_NEGATION: return INT_NODE;
      case ORD: return INT_NODE;
      case LEN: return INT_NODE;
      case CHR: return CHAR_NODE;
    }
    return null;
  }
}
