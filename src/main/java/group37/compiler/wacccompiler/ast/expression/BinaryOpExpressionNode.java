package group37.compiler.wacccompiler.ast.expression;

import static group37.compiler.wacccompiler.ast.types.TypeMatcher.BOOL_NODE;
import static group37.compiler.wacccompiler.ast.types.TypeMatcher.INT_NODE;

import group37.compiler.wacccompiler.ast.types.TypeNode;
import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.backend.arminstructions.ArmProgram.LibFunction;
import group37.compiler.wacccompiler.backend.arminstructions.Label;
import group37.compiler.wacccompiler.symboltable.SymbolTable;

public class BinaryOpExpressionNode extends AbstractExpressionNode {

  public BinaryOpExpressionNode(int line, int col,
      BinaryOp op) {
    super(line, col);
    this.op = op;
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitBinaryOpExpressionNode(this);
  }

  public void setLhs(ExpressionNode lhs) {
    this.lhs = lhs;
  }

  public void setRhs(ExpressionNode rhs) {
    this.rhs = rhs;
  }

  public ExpressionNode getLhs() {
    return lhs;
  }

  public ExpressionNode getRhs() {
    return rhs;
  }

  public enum BinaryOp {
    MULTIPLY,
    DIVIDE,
    MODULO,
    ADD,
    SUBTRACT,
    LESS_THAN,
    LESS_THAN_EQUAL,
    GREATER_THAN,
    GREATER_THAN_EQUAL,
    EQUAL,
    NOT_EQUAL,
    AND,
    OR;
  }

  @Override
  public LibFunction getPrintFunction(SymbolTable context) {
    switch (op) {
      case ADD: return LibFunction.PRINT_INT;
      case MULTIPLY: return LibFunction.PRINT_INT;
      case SUBTRACT: return LibFunction.PRINT_INT;
      case EQUAL: return LibFunction.PRINT_BOOL;
      case LESS_THAN: return LibFunction.PRINT_BOOL;
      case NOT_EQUAL: return LibFunction.PRINT_BOOL;
      case GREATER_THAN: return LibFunction.PRINT_BOOL;
      case LESS_THAN_EQUAL: return LibFunction.PRINT_BOOL;
      case GREATER_THAN_EQUAL: return LibFunction.PRINT_BOOL;
      case DIVIDE: return LibFunction.PRINT_INT;
      case MODULO: return LibFunction.PRINT_INT;
      case AND: return LibFunction.PRINT_BOOL;
      case OR: return LibFunction.PRINT_BOOL;
    }
    return null;
  }

  public BinaryOp getOp() {
    return op;
  }

  private final BinaryOp op;
  private ExpressionNode lhs;
  private ExpressionNode rhs;

  @Override
  public boolean checkValid(SymbolTable context) {
    if (lhs == null || rhs == null) {
      return false;
    }
    if (lhs.checkValid(context) && rhs.checkValid(context)) {
      switch (op) {
        case AND: return lhs.expType(context).equals(BOOL_NODE) &&
            rhs.expType(context).equals(BOOL_NODE);
        case OR: return lhs.expType(context).equals(BOOL_NODE) &&
            rhs.expType(context).equals(BOOL_NODE);
        default : return lhs.expType(context).equals(INT_NODE) &&
            rhs.expType(context).equals(INT_NODE);
      }
    } else {
      return false;
    }
  }

  @Override
  public TypeNode expType(SymbolTable context) {
    switch (op) {
      case AND: return BOOL_NODE;
      case OR: return BOOL_NODE;
      case GREATER_THAN_EQUAL: return BOOL_NODE;
      case LESS_THAN_EQUAL: return BOOL_NODE;
      case GREATER_THAN: return BOOL_NODE;
      case NOT_EQUAL: return BOOL_NODE;
      case LESS_THAN: return BOOL_NODE;
      case EQUAL: return BOOL_NODE;
      default : return INT_NODE;
    }
  }
}
