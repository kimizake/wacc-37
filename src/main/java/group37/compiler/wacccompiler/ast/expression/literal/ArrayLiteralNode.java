package group37.compiler.wacccompiler.ast.expression.literal;

import group37.compiler.wacccompiler.ast.expression.AbstractExpressionNode;
import group37.compiler.wacccompiler.ast.expression.ExpressionNode;
import group37.compiler.wacccompiler.ast.statements.assignment.AssignRhsNode;
import group37.compiler.wacccompiler.ast.types.TypeNode;
import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.backend.arminstructions.Expression;
import group37.compiler.wacccompiler.symboltable.SymbolTable;
import java.util.List;

public class ArrayLiteralNode extends AbstractExpressionNode {

  private final List<ExpressionNode> expressionNodeList;

  private int arraySize;

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitArrayLiteralNode(this);
  }

  public ArrayLiteralNode(int line, int col,
      List<ExpressionNode> expressionNodeList) {
    super(line, col);
    this.expressionNodeList = expressionNodeList;
    arraySize = expressionNodeList.size();

  }

  @Override
  public boolean checkValid(SymbolTable context) {
    if (expressionNodeList.size() == 0) {
      return true;
    } else {
      TypeNode type = expressionNodeList.get(0).expType(context);
      for (int i = 1; i < expressionNodeList.size(); i++) {
        if (!expressionNodeList.get(i).expType(context).equals(type)) {
          return false;
        }
      }
      return true;
    }
  }

  @Override
  public TypeNode expType(SymbolTable context) {
    return expressionNodeList.get(0).expType(context);
  }

  public int getArraySize(){
    return arraySize;
  }

  public List<ExpressionNode> getExpressionNodeList(){
    return expressionNodeList;
  }

}
