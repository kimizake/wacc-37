package group37.compiler.wacccompiler.ast.statements.assignment;

import group37.compiler.wacccompiler.ast.AbstractWaccNode;
import group37.compiler.wacccompiler.ast.expression.ExpressionNode;
import group37.compiler.wacccompiler.ast.types.PairTypeNode;
import group37.compiler.wacccompiler.ast.types.TypeNode;
import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.symboltable.SymbolTable;

public class NewpairNode extends AbstractWaccNode implements AssignRhsNode {

  private final ExpressionNode fst;
  private final ExpressionNode snd;

  public NewpairNode(int line, int col,
      ExpressionNode fst, ExpressionNode snd) {
    super(line, col);
    this.fst = fst;
    this.snd = snd;
  }

  public ExpressionNode getFst() {
    return fst;
  }

  public ExpressionNode getSnd() {
    return snd;
  }

  @Override
  public boolean checkValid(SymbolTable context) {
    return true;
  }

  @Override
  public TypeNode expType(SymbolTable context) {
    return new PairTypeNode(fst.expType(context), snd.expType(context));
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitNewpairNode(this);
  }
}
