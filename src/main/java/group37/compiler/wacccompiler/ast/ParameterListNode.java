package group37.compiler.wacccompiler.ast;

import group37.compiler.wacccompiler.backend.WaccAstVisitor;

import java.util.List;

public class ParameterListNode extends AbstractWaccNode {

  private final List<ParameterNode> parameters;

  public ParameterListNode(int line, int col,
      List<ParameterNode> parameters) {
    super(line, col);
    this.parameters = parameters;
  }

  public List<ParameterNode> getParameters() {
    return parameters;
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitParameterListNode(this);
  }
}
