package group37.compiler.wacccompiler.ast.types;

import static group37.compiler.wacccompiler.ast.types.TypeMatcher.CHAR_NODE;

import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.backend.arminstructions.ArmProgram.LibFunction;

public class ArrayTypeNode implements TypeNode {

  public TypeNode getType() {
    return type;
  }

  private final TypeNode type;

  public ArrayTypeNode(TypeNode type) {
    this.type = type;
  }

  @Override
  public boolean equals(TypeNode type) {
    return type instanceof ArrayTypeNode && ((ArrayTypeNode) type).type.equals(type);
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitArrayTypeNode(this);
  }

  @Override
  public LibFunction getTypeNodePrintFunc() {
    if (type.equals(CHAR_NODE)) {
      return LibFunction.PRINT_CHAR_ARRAY;
    }
    return LibFunction.PRINT_REFERENCE;
  }

  @Override
  public LibFunction getTypeNodeReadFunc() {
    return type.getTypeNodeReadFunc();
  }
}
