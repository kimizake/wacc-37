package group37.compiler.wacccompiler.ast;

import static group37.compiler.wacccompiler.ast.statements.StatementMatcher.RETURN_NODE;

import group37.compiler.wacccompiler.ast.expression.IdentifierNode;
import group37.compiler.wacccompiler.ast.statements.SequenceNode;
import group37.compiler.wacccompiler.ast.statements.StatementNode;
import group37.compiler.wacccompiler.ast.types.FunctionType;
import group37.compiler.wacccompiler.ast.types.TypeNode;
import group37.compiler.wacccompiler.backend.WaccAstVisitor;
import group37.compiler.wacccompiler.symboltable.STEntry;
import group37.compiler.wacccompiler.symboltable.SymbolTable;
import java.util.List;

public class FunctionNode extends AbstractWaccNode {

  private final TypeNode type;
  private final IdentifierNode identifier;
  private final ParameterListNode parameters;
  private final StatementNode statement;


  public FunctionNode(int line, int col, TypeNode type,
      IdentifierNode identifier, ParameterListNode parameters,
      StatementNode statement) {
    super(line, col);
    this.type = type;
    this.identifier = identifier;
    this.parameters = parameters;
    this.statement = statement;
  }

  public boolean checkValidReturn() {
    return statement.checkValidReturn();
  }

  public TypeNode getType() {
    return type;
  }

  public IdentifierNode getIdentifier() {
    return identifier;
  }

  public ParameterListNode getParameters() {
    return parameters;
  }

  public StatementNode getStatement() {
    return statement;
  }

  public boolean checkValid(SymbolTable context) {
    //make a new context with the parameters and add to symbol table
    context.enterScope();
    if (parameters != null) {
      for (ParameterNode p : parameters.getParameters()) {
        context.add(p.getId(), new STEntry(p.getType()));
      }
    }
    boolean out = statement.checkValid(context);
    context.exitScope();
    return out;
  }

  public FunctionType getTypeSignature() {
    int argc = 1;
    if (parameters != null) {
      argc += parameters.getParameters().size();
    }
    TypeNode[] signature = new TypeNode[argc];
    signature[0] = type;
    for (int i = 1; i < argc; i++) {
      signature[i] = parameters.getParameters().get(i - 1).getType();
    }
    return new FunctionType(signature);
  }

  public String getId() {
    return identifier.getId();
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitFunctionNode(this);
  }
}
