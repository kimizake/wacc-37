package group37.compiler.wacccompiler.ast.expression;

import group37.compiler.wacccompiler.ast.statements.assignment.AssignLhsNode;
import group37.compiler.wacccompiler.ast.types.ArrayTypeNode;
import group37.compiler.wacccompiler.ast.types.TypeNode;
import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.backend.arminstructions.ArmProgram.LibFunction;
import group37.compiler.wacccompiler.symboltable.SymbolTable;
import java.util.List;

public class ArrayElemNode extends AbstractExpressionNode implements AssignLhsNode {

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitArrayElemNode(this);
  }

  private final IdentifierNode idNode;
  private final List<ExpressionNode> index;

  public ArrayElemNode(int line, int col,
      IdentifierNode idNode,
      List<ExpressionNode> index) {
    super(line, col);
    this.idNode = idNode;
    this.index = index;
  }

  public List<ExpressionNode> getIndexList(){
    return index;
  }

  public String getId() {
    return idNode.getId();
  }

  @Override
  public boolean checkValid(SymbolTable context) {
    if (!context.defined(idNode.getId())) {
      TypeNode typeNode = context.getType(idNode.getId());
      return typeNode instanceof ArrayTypeNode;
    }
    return false;
  }

  public IdentifierNode getIdNode() {
    return idNode;
  }

  public List<ExpressionNode> getIndex() {
    return index;
  }

  //may need to change for not defined yet
  @Override
  public TypeNode expType(SymbolTable context) {
    return context.getType(idNode.getId());
  }

  @Override
  public LibFunction getPrintFunction(SymbolTable context) {
    return getPrintFunctionHelper(context.getType(idNode.getId()), index.size());
  }

  private LibFunction getPrintFunctionHelper(TypeNode type, int acc) {
    if (acc == 0)
      return type.getTypeNodePrintFunc();

    if (type instanceof ArrayTypeNode)
      return getPrintFunctionHelper(((ArrayTypeNode) type).getType(), --acc);
    else
      return type.getTypeNodePrintFunc();
  }
}