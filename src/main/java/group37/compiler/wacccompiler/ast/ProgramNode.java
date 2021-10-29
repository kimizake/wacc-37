package group37.compiler.wacccompiler.ast;

import static group37.compiler.wacccompiler.ast.statements.StatementMatcher.EXIT_NODE;
import static group37.compiler.wacccompiler.ast.statements.StatementMatcher.RETURN_NODE;

import group37.compiler.wacccompiler.ast.statements.IfStatementNode;
import group37.compiler.wacccompiler.ast.statements.Keyword;
import group37.compiler.wacccompiler.ast.statements.KeywordStatementNode;
import group37.compiler.wacccompiler.ast.statements.SequenceNode;
import group37.compiler.wacccompiler.ast.statements.StatementNode;
import group37.compiler.wacccompiler.backend.WaccAstVisitor;

import java.util.List;

public class ProgramNode extends AbstractWaccNode {

  private final List<FunctionNode> functions;
  private final StatementNode statement;

  public ProgramNode(int line, int col,
      List<FunctionNode> functions,
      StatementNode statement) {
    super(line, col);
    this.functions = functions;
    this.statement = statement;
  }

  public List<FunctionNode> getFunctions() {
    return functions;
  }

  public StatementNode getStatement() {
    return statement;
  }

  public <T> T accept(WaccAstVisitor<T> waccAstVisitor) {
    return waccAstVisitor.visitProgramNode(this);
  }
}
