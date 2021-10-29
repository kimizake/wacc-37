package group37.compiler.wacccompiler.ast.expression;

import group37.compiler.wacccompiler.ast.AbstractWaccNode;
import group37.compiler.wacccompiler.ast.statements.assignment.AssignLhsNode;
import group37.compiler.wacccompiler.ast.statements.assignment.AssignRhsNode;
import group37.compiler.wacccompiler.ast.types.PairTypeNode;
import group37.compiler.wacccompiler.ast.types.TypeNode;
import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.backend.arminstructions.ArmProgram.LibFunction;
import group37.compiler.wacccompiler.symboltable.SymbolTable;

public class PairElemNode extends AbstractWaccNode implements AssignLhsNode, AssignRhsNode {

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitPairElemNode(this);
  }

  @Override
  public String getId() {
    return ((IdentifierNode) expression).getId();
  }

  @Override
  public LibFunction getReadFunction(SymbolTable context) {
    TypeNode type = expression.expType(context);
    if (type instanceof PairTypeNode) {
      type = fst ? ((PairTypeNode) type).getLeftType()
          : ((PairTypeNode) type).getRightType();
    }
    return type.getTypeNodeReadFunc();
  }

  private final ExpressionNode expression;
  private final boolean fst;

  public PairElemNode(int line, int col,
      ExpressionNode expression, boolean fst) {
    super(line, col);
    this.expression = expression;
    this.fst = fst;
  }

  public ExpressionNode getExpression() {
    return expression;
  }

  public boolean isFst() {
    return fst;
  }

  @Override
  public boolean checkValid(SymbolTable context) {
    return expression.expType(context) instanceof PairTypeNode;
  }

  @Override
  public TypeNode expType(SymbolTable context) {
    return expression.expType(context);
  }
}
