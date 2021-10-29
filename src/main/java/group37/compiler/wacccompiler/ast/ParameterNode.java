package group37.compiler.wacccompiler.ast;

import group37.compiler.wacccompiler.ast.expression.IdentifierNode;
import group37.compiler.wacccompiler.ast.types.TypeNode;
import group37.compiler.wacccompiler.backend.WaccAstVisitor;

public class ParameterNode extends AbstractWaccNode {

  private final TypeNode type;
  private final IdentifierNode identifier;

  public ParameterNode(int line, int col, TypeNode type,
      IdentifierNode identifier) {
    super(line, col);
    this.type = type;
    this.identifier = identifier;
  }

  public TypeNode getType() {
    return type;
  }

  public String getId() {
    return identifier.getId();
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitParameterNode(this);
  }
}
