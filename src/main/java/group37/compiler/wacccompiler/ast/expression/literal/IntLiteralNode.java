package group37.compiler.wacccompiler.ast.expression.literal;

import static group37.compiler.wacccompiler.ast.types.TypeMatcher.INT_NODE;
import static java.lang.System.exit;

import group37.compiler.wacccompiler.ast.expression.AbstractExpressionNode;
import group37.compiler.wacccompiler.ast.types.TypeNode;
import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.backend.arminstructions.ArmProgram.LibFunction;
import group37.compiler.wacccompiler.backend.arminstructions.Label;
import group37.compiler.wacccompiler.symboltable.SymbolTable;

public class IntLiteralNode extends AbstractExpressionNode implements LiteralNode {

  private final int literal;

  @Override
  public LibFunction getPrintFunction(SymbolTable context) {
    return LibFunction.PRINT_INT;
  }

  public IntLiteralNode(int line, int col, String literal, boolean neg) {
    super(line, col);
    long x = neg ? -Long.parseLong(literal) : Long.parseLong(literal);
    if (x > Integer.MAX_VALUE ||
        x < Integer.MIN_VALUE) {
      System.out.println("Int too big at " + line + ":" + col);
      exit(100);
      this.literal = 21; //pointless but makes maven happy
    } else {
      this.literal = (int) x;
    }
  }

  public int getLiteral() {
    return literal;
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitIntLiteralNode(this);
  }

  @Override
  public boolean checkValid(SymbolTable context) {
    return true;
  }

  @Override
  public TypeNode expType(SymbolTable context) {
    return INT_NODE;
  }
}
