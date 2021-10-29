package group37.compiler.wacccompiler.symboltable;

import group37.compiler.wacccompiler.ast.types.TypeNode;

public class STEntry {
  private TypeNode typeNode;
  private int offset;

  public STEntry(TypeNode typeNode, int offset) {
    this.typeNode = typeNode;
    this.offset = offset;
  }

  public STEntry(TypeNode typeNode) {
    this.typeNode = typeNode;
  }

  TypeNode getTypeNode() {
    return typeNode;
  }

  int getOffset() {
    return offset;
  }

}
