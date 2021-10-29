package group37.compiler.wacccompiler.ast.statements;

import group37.compiler.wacccompiler.ast.AbstractWaccNode;

public abstract class AbstractStatementNode extends AbstractWaccNode implements StatementNode {

  protected AbstractStatementNode(int line, int col) {
    super(line, col);
  }
}
