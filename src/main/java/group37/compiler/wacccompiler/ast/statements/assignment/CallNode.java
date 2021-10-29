package group37.compiler.wacccompiler.ast.statements.assignment;

import group37.compiler.wacccompiler.ast.AbstractWaccNode;
import group37.compiler.wacccompiler.ast.expression.ExpressionNode;
import group37.compiler.wacccompiler.ast.expression.IdentifierNode;
import group37.compiler.wacccompiler.ast.types.FunctionType;
import group37.compiler.wacccompiler.ast.types.TypeNode;
import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.symboltable.SymbolTable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CallNode extends AbstractWaccNode implements AssignRhsNode {

  private final IdentifierNode id;
  private final List<ExpressionNode> args;

  public CallNode(int line, int col,
      IdentifierNode id,
      List<ExpressionNode> args) {
    super(line, col);
    this.id = id;
    this.args = args;
  }

  //null???
  @Override
  public boolean checkValid(SymbolTable context) {
    TypeNode functionType = context.getType(id.getId());
    if (functionType instanceof FunctionType) {
      List<TypeNode> types = args.stream().map(x -> x.expType(context)).collect(
          Collectors.toList());

      List<TypeNode> parameters = ((FunctionType) functionType).args();

      if (parameters.size() == types.size()) {
        for (int i = 0; i < parameters.size(); i++) {
          if (!parameters.get(i).equals(types.get(i))) {
            return false;
          }
        }
        return true;
      }

      /*if (((FunctionType) functionType).args().equals(types)) {
        return true;
      }*/
    }
    return false;
  }

  public IdentifierNode getId() {
    return id;
  }

  public List<ExpressionNode> getArgs() {
    return args;
  }

  @Override
  public TypeNode expType(SymbolTable context) {
    return ((FunctionType) context.getType(id.getId())).returnType();
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitCallNode(this);
  }
}
