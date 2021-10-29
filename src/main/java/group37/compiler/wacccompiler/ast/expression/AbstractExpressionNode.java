package group37.compiler.wacccompiler.ast.expression;

import group37.compiler.wacccompiler.ast.AbstractWaccNode;

public abstract class AbstractExpressionNode extends AbstractWaccNode implements ExpressionNode {

  protected AbstractExpressionNode(int line, int col) {
    super(line, col);
  }
}
