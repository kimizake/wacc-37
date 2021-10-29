package group37.compiler.wacccompiler.ast.types;

import group37.compiler.wacccompiler.ast.types.BaseTypeNode.BaseType;

public class TypeMatcher {

  public final static BaseTypeNode INT_NODE  = new BaseTypeNode(BaseType.INT);
  public final static BaseTypeNode BOOL_NODE  = new BaseTypeNode(BaseType.BOOL);
  public final static BaseTypeNode CHAR_NODE  = new BaseTypeNode(BaseType.CHAR);
  public final static BaseTypeNode STRING_NODE  = new BaseTypeNode(BaseType.STRING);

}
