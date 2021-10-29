package group37.compiler.wacccompiler.ast.types;

import group37.compiler.wacccompiler.backend.WaccAstVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FunctionType implements TypeNode {

  public FunctionType(TypeNode[] args) {
    this.args = args;
  }

  //return type of the function is stored as the first element
  private TypeNode[] args;


  @Override
  public boolean equals(TypeNode type) {
    return type instanceof FunctionType && Arrays
        .deepEquals(((FunctionType) type).args, args);
  }

  public TypeNode returnType() {
    return args[0];
  }

  public List<TypeNode> args() {
    List<TypeNode> out = new ArrayList<TypeNode>(Arrays.asList(args));
    out.remove(0);
    return out;
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitFunctionTypeNode(this);
  }

}
