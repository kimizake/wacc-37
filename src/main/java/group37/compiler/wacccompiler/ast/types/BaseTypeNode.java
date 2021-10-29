package group37.compiler.wacccompiler.ast.types;

import group37.compiler.wacccompiler.ast.WaccAstNode;
import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.backend.arminstructions.ArmProgram.LibFunction;
import java.security.InvalidParameterException;

public class BaseTypeNode implements TypeNode {

  public enum BaseType {
    INT,
    CHAR,
    BOOL,
    STRING;
  }

  public int getTypeNodeOffset() {
    switch (type) {
      case BOOL: return 1;
      case CHAR: return 1;
      case INT: return 4;
      case STRING: return 4;
    }
    return -100;
  }

  public BaseType getType(){
    return type;
  }

  @Override
  public LibFunction getTypeNodePrintFunc() {
    switch (type) {
      case BOOL: return LibFunction.PRINT_BOOL;
      case CHAR: return LibFunction.PUT_CHAR;
      case INT: return LibFunction.PRINT_INT;
      case STRING: return LibFunction.PRINT_STRING;
    }
    throw new InvalidParameterException();
  }

  @Override
  public LibFunction getTypeNodeReadFunc() {
    switch (type) {
      case BOOL:
      case CHAR: return LibFunction.READ_CHAR;
      case INT: return LibFunction.READ_INT;
      case STRING:
    }
    throw new InvalidParameterException();
  }

  private final BaseType type;

  public BaseTypeNode(BaseType type) {
    this.type = type;
  }

  @Override
  public boolean equals(TypeNode type) {
    return type instanceof BaseTypeNode && this.type == ((BaseTypeNode) type).type;
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitBaseTypeNode(this);
  }
}
