package group37.compiler.wacccompiler.ast.types;

import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.backend.arminstructions.ArmProgram.LibFunction;

public class PairTypeNode implements TypeNode {

  private final TypeNode leftType;
  private final TypeNode rightType;

  public PairTypeNode(TypeNode leftType, TypeNode rightType) {
    this.leftType = leftType;
    this.rightType = rightType;
  }

  @Override
  public LibFunction getTypeNodePrintFunc() {
    return LibFunction.PRINT_REFERENCE;
  }

  public TypeNode getLeftType() {
    return leftType;
  }

  public TypeNode getRightType() {
    return rightType;
  }

  //currently does not work for nested pairs as it will check for complete type equality
  @Override
  public boolean equals(TypeNode type) {
    return type instanceof PairTypeNode &&
        ((PairTypeNode) type).leftType.equals(leftType) &&
        ((PairTypeNode) type).rightType.equals(rightType);
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitPairTypeNode(this);
  }
}
