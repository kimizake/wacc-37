package group37.compiler.wacccompiler.ast.statements;

import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.symboltable.SymbolTable;

public class SkipStatementNode extends AbstractStatementNode {

  public SkipStatementNode(int line, int col) {
    super(line, col);
  }

  @Override
  public boolean checkValid(SymbolTable context) {
    return true;
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitSkipStatementNode(this);
  }
}
