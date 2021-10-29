package group37.compiler.wacccompiler.ast.statements;

import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.symboltable.SymbolTable;
import java.util.List;

public class SequenceNode implements StatementNode {

  @Override
  public int countReturns() {
    int out = 0;
    for (StatementNode s : statements) {
      out += s.countReturns();
    }
    return out;
  }

  @Override
  public boolean checkValidReturn() {

    //all statements execpt the last must not contain a return thus
    int cnt = 0;
    int noStatements = statements.size();
    for (int i = 0; i < noStatements - 1; i++) {
      cnt += statements.get(i).countReturns();
    }
    //optimise this unless we need to count for superfluous returns
    if (cnt > 0) {
      return false;
    }
    return statements.get(noStatements - 1).checkValidReturn();
  }

  private final List<StatementNode> statements;

  public SequenceNode(List<StatementNode> statements) {
    this.statements = statements;
  }

  public List<StatementNode> getStatements() {
    return statements;
  }

  @Override
  public boolean checkValid(SymbolTable context) {
    return false;
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitSequenceNode(this);
  }
}
