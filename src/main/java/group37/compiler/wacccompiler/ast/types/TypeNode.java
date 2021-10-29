package group37.compiler.wacccompiler.ast.types;


import group37.compiler.wacccompiler.ast.WaccAstNode;
import group37.compiler.wacccompiler.backend.arminstructions.ArmProgram.LibFunction;

public interface TypeNode extends WaccAstNode {

  default LibFunction getTypeNodePrintFunc() {
    throw new UnsupportedOperationException();
  }

  default LibFunction getTypeNodeReadFunc() {
    throw new UnsupportedOperationException();
  }

  boolean equals(TypeNode type);

}
