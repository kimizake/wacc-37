package group37.compiler.wacccompiler.frontend;

import static group37.antlr.WACCParser.BOOL_LITER;
import static group37.antlr.WACCParser.CHAR_LITER;
import static group37.antlr.WACCParser.IDENT;
import static group37.antlr.WACCParser.INT_LITER;
import static group37.antlr.WACCParser.PAIR_LITER;
import static group37.antlr.WACCParser.STR_LITER;
import static group37.compiler.wacccompiler.ast.expression.BinaryOpExpressionNode.BinaryOp.ADD;
import static group37.compiler.wacccompiler.ast.expression.BinaryOpExpressionNode.BinaryOp.AND;
import static group37.compiler.wacccompiler.ast.expression.BinaryOpExpressionNode.BinaryOp.DIVIDE;
import static group37.compiler.wacccompiler.ast.expression.BinaryOpExpressionNode.BinaryOp.EQUAL;
import static group37.compiler.wacccompiler.ast.expression.BinaryOpExpressionNode.BinaryOp.GREATER_THAN;
import static group37.compiler.wacccompiler.ast.expression.BinaryOpExpressionNode.BinaryOp.GREATER_THAN_EQUAL;
import static group37.compiler.wacccompiler.ast.expression.BinaryOpExpressionNode.BinaryOp.LESS_THAN;
import static group37.compiler.wacccompiler.ast.expression.BinaryOpExpressionNode.BinaryOp.LESS_THAN_EQUAL;
import static group37.compiler.wacccompiler.ast.expression.BinaryOpExpressionNode.BinaryOp.MODULO;
import static group37.compiler.wacccompiler.ast.expression.BinaryOpExpressionNode.BinaryOp.MULTIPLY;
import static group37.compiler.wacccompiler.ast.expression.BinaryOpExpressionNode.BinaryOp.NOT_EQUAL;
import static group37.compiler.wacccompiler.ast.expression.BinaryOpExpressionNode.BinaryOp.OR;
import static group37.compiler.wacccompiler.ast.expression.BinaryOpExpressionNode.BinaryOp.SUBTRACT;
import static group37.compiler.wacccompiler.ast.expression.UnaryOpExpressionNode.UnaryOp.CHR;
import static group37.compiler.wacccompiler.ast.expression.UnaryOpExpressionNode.UnaryOp.LEN;
import static group37.compiler.wacccompiler.ast.expression.UnaryOpExpressionNode.UnaryOp.LOGICAL_NEGATION;
import static group37.compiler.wacccompiler.ast.expression.UnaryOpExpressionNode.UnaryOp.MATHEMATICAL_NEGATION;
import static group37.compiler.wacccompiler.ast.expression.UnaryOpExpressionNode.UnaryOp.ORD;
import static group37.compiler.wacccompiler.ast.types.BaseTypeNode.BaseType.BOOL;
import static group37.compiler.wacccompiler.ast.types.BaseTypeNode.BaseType.CHAR;
import static group37.compiler.wacccompiler.ast.types.BaseTypeNode.BaseType.INT;
import static group37.compiler.wacccompiler.ast.types.BaseTypeNode.BaseType.STRING;
import group37.antlr.WACCParser.ArgListContext;
import group37.antlr.WACCParser.ArrayElemContext;
import group37.antlr.WACCParser.ArrayLiterContext;
import group37.antlr.WACCParser.ArrayTypeContext;
import group37.antlr.WACCParser.AssignLHSContext;
import group37.antlr.WACCParser.AssignRHSContext;
import group37.antlr.WACCParser.BaseTypeContext;
import group37.antlr.WACCParser.BasicTypeContext;
import group37.antlr.WACCParser.ExprContext;
import group37.antlr.WACCParser.FuncContext;
import group37.antlr.WACCParser.PairElemContext;
import group37.antlr.WACCParser.PairTypeContext;
import group37.antlr.WACCParser.ParamContext;
import group37.antlr.WACCParser.ParamListContext;
import group37.antlr.WACCParser.ProgContext;
import group37.antlr.WACCParser.StatAssignLHSAssignContext;
import group37.antlr.WACCParser.StatBeginContext;
import group37.antlr.WACCParser.StatContext;
import group37.antlr.WACCParser.StatExitContext;
import group37.antlr.WACCParser.StatFreeContext;
import group37.antlr.WACCParser.StatIdentAssignContext;
import group37.antlr.WACCParser.StatIfContext;
import group37.antlr.WACCParser.StatPrintContext;
import group37.antlr.WACCParser.StatPrintLnContext;
import group37.antlr.WACCParser.StatReadContext;
import group37.antlr.WACCParser.StatReturnContext;
import group37.antlr.WACCParser.StatSeqContext;
import group37.antlr.WACCParser.StatSkipContext;
import group37.antlr.WACCParser.StatWhileContext;
import group37.antlr.WACCParser.TypeContext;
import group37.antlr.WACCParser.UnaryOperContext;
import group37.antlr.WACCParserVisitor;
import group37.compiler.wacccompiler.ast.FunctionNode;
import group37.compiler.wacccompiler.ast.ParameterListNode;
import group37.compiler.wacccompiler.ast.ParameterNode;
import group37.compiler.wacccompiler.ast.ProgramNode;
import group37.compiler.wacccompiler.ast.WaccAstNode;
import group37.compiler.wacccompiler.ast.expression.ArrayElemNode;
import group37.compiler.wacccompiler.ast.expression.BinaryOpExpressionNode;
import group37.compiler.wacccompiler.ast.expression.BinaryOpExpressionNode.BinaryOp;
import group37.compiler.wacccompiler.ast.expression.ExpressionNode;
import group37.compiler.wacccompiler.ast.expression.IdentifierNode;
import group37.compiler.wacccompiler.ast.expression.PairElemNode;
import group37.compiler.wacccompiler.ast.expression.UnaryOpExpressionNode;
import group37.compiler.wacccompiler.ast.expression.UnaryOpExpressionNode.UnaryOp;
import group37.compiler.wacccompiler.ast.expression.literal.ArrayLiteralNode;
import group37.compiler.wacccompiler.ast.expression.literal.BoolLiteralNode;
import group37.compiler.wacccompiler.ast.expression.literal.CharLiteralNode;
import group37.compiler.wacccompiler.ast.expression.literal.IntLiteralNode;
import group37.compiler.wacccompiler.ast.expression.literal.PairLiteralNode;
import group37.compiler.wacccompiler.ast.expression.literal.StrLiteralNode;
import group37.compiler.wacccompiler.ast.statements.BlockNode;
import group37.compiler.wacccompiler.ast.statements.IfStatementNode;
import group37.compiler.wacccompiler.ast.statements.Keyword;
import group37.compiler.wacccompiler.ast.statements.KeywordStatementNode;
import group37.compiler.wacccompiler.ast.statements.ReadStatementNode;
import group37.compiler.wacccompiler.ast.statements.SequenceNode;
import group37.compiler.wacccompiler.ast.statements.SkipStatementNode;
import group37.compiler.wacccompiler.ast.statements.StatementNode;
import group37.compiler.wacccompiler.ast.statements.WhileStatementNode;
import group37.compiler.wacccompiler.ast.statements.assignment.AssignLhsNode;
import group37.compiler.wacccompiler.ast.statements.assignment.AssignNode;
import group37.compiler.wacccompiler.ast.statements.assignment.AssignRhsNode;
import group37.compiler.wacccompiler.ast.statements.assignment.CallNode;
import group37.compiler.wacccompiler.ast.statements.assignment.IdentAssignNode;
import group37.compiler.wacccompiler.ast.statements.assignment.NewpairNode;
import group37.compiler.wacccompiler.ast.types.ArrayTypeNode;
import group37.compiler.wacccompiler.ast.types.BaseTypeNode;
import group37.compiler.wacccompiler.ast.types.BaseTypeNode.BaseType;
import group37.compiler.wacccompiler.ast.types.PairTypeNode;
import group37.compiler.wacccompiler.ast.types.TypeNode;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class _WaccVisit implements WACCParserVisitor<WaccAstNode> {

  private List<ExpressionNode> getExprNodes(List<ExprContext> exprContexts) {
    List<ExpressionNode> expressionNodes = new ArrayList<>();
    for (ExprContext e : exprContexts)
      expressionNodes.add((ExpressionNode) visit(e));
    return expressionNodes;
  }

  @Override
  public WaccAstNode visitArrayLiter(ArrayLiterContext ctx) {
    return new ArrayLiteralNode(
        ctx.getStart().getLine(),
        ctx.getStart().getCharPositionInLine(),
        getExprNodes(ctx.expr())
    );
  }

  @Override
  public WaccAstNode visitArrayElem(ArrayElemContext ctx) {
    return new ArrayElemNode(
        ctx.getStart().getLine(),
        ctx.getStart().getCharPositionInLine(),
        (IdentifierNode) visit(ctx.IDENT()),
        getExprNodes(ctx.expr())
    );
  }

  @Override
  public WaccAstNode visitBasicType(BasicTypeContext ctx) {
    return visit(ctx.baseType());
  }

  @Override
  public WaccAstNode visitBaseType(BaseTypeContext ctx) {
    BaseType b = null;

    if(ctx.TYPE_INT() != null)
      b = INT;
    else if(ctx.TYPE_CHAR() != null)
      b = CHAR;
    else if(ctx.TYPE_BOOL() != null)
      b = BOOL;
    else if(ctx.TYPE_STRING() != null)
      b = STRING;

    return new BaseTypeNode(
        b
    );
  }

  @Override
  public WaccAstNode visitArrayType(ArrayTypeContext ctx) {
    return new ArrayTypeNode(
        (TypeNode) visit(ctx.type())
    );
  }

  @Override
  public WaccAstNode visitPairType(PairTypeContext ctx) {
    TypeContext type1 = ctx.type(0);
    TypeContext type2 = ctx.type(1);

    return new PairTypeNode(
        type1 == null ? null : (TypeNode) visit(type1),
        type2 == null ? null : (TypeNode) visit(type2)
    );
  }

  @Override
  public WaccAstNode visitPairElem(PairElemContext ctx) {
    return new PairElemNode(
        ctx.getStart().getLine(),
        ctx.getStart().getCharPositionInLine(),
        (ExpressionNode) visit(ctx.expr()),
        ((ctx.FST() != null) && (ctx.SND() == null))
    );
  }

  @Override
  public WaccAstNode visitArgList(ArgListContext ctx) {
    return null;
  }

  @Override
  public WaccAstNode visitAssignRHS(AssignRHSContext ctx) {
    if(ctx.NEWPAIR() != null)
      return new NewpairNode(
          ctx.getStart().getLine(),
          ctx.getStart().getCharPositionInLine(),
          (ExpressionNode) visit(ctx.expr(0)),
          (ExpressionNode) visit(ctx.expr(1))
      );
    else if(ctx.CALL() != null) {
      List<ExpressionNode> args;
      if(ctx.argList() != null)
        args = getExprNodes(ctx.argList().expr());
      else
        args = new ArrayList<>();

      return new CallNode(
          ctx.getStart().getLine(),
          ctx.getStart().getCharPositionInLine(),
          (IdentifierNode) visit(ctx.IDENT()),
          args
      );
    } else if(ctx.expr(0) != null)
      return visit(ctx.expr(0));
    else if(ctx.arrayLiter() != null)
      return visit(ctx.arrayLiter());
    else
      return visit(ctx.pairElem());
  }

  @Override
  public WaccAstNode visitAssignLHS(AssignLHSContext ctx) {
    if(ctx.IDENT() != null)
      return visit(ctx.IDENT());
    else if(ctx.arrayElem() != null)
      return visit(ctx.arrayElem());
    else
      return visit(ctx.pairElem());
  }

  @Override
  public WaccAstNode visitUnaryOper(UnaryOperContext ctx) {
    UnaryOp u;

    if(ctx.BIN_NOT() != null)
      u = LOGICAL_NEGATION;
    else if(ctx.MINUS() != null)
      u = MATHEMATICAL_NEGATION;
    else if(ctx.LEN() != null)
      u = LEN;
    else if(ctx.ORD() != null)
      u = ORD;
    else if(ctx.CHR() != null)
      u = CHR;
    else
      return null;

    return new UnaryOpExpressionNode(
        ctx.getStart().getLine(),
        ctx.getStart().getCharPositionInLine(),
        u
    );
  }


  @Override
  public WaccAstNode visitExpr(ExprContext ctx) {
    TerminalNode tn;

    ParserRuleContext _ctx;

    if (ctx.OPEN_PARENTHESES() != null && ctx.CLOSE_PARENTHESES() != null) {
      return visit(ctx.expr(0));
    }

    BinaryOp b;

    if(ctx.MULTIPLY() != null)
      b = MULTIPLY;
    else if(ctx.DIVIDE() != null)
      b = DIVIDE;
    else if(ctx.MODULO() != null)
      b = MODULO;
    else if(ctx.PLUS() != null)
      b = ADD;
    else if(ctx.MINUS() != null)
      b = SUBTRACT;
    else if(ctx.BIN_AND() != null)
      b = AND;
    else if(ctx.BIN_OR() != null)
      b = OR;
    else if(ctx.GTR_THAN() != null)
      b = GREATER_THAN;
    else if(ctx.GTR_EQUAL() != null)
      b = GREATER_THAN_EQUAL;
    else if(ctx.LSS_THAN() != null)
      b = LESS_THAN;
    else if(ctx.LSS_EQUAL() != null)
      b = LESS_THAN_EQUAL;
    else if(ctx.EQUAL_TO() != null)
      b = EQUAL;
    else if(ctx.NOT_EQUAL() != null)
      b = NOT_EQUAL;
    else
      b = null;

    if (b != null && ctx.expr(0) != null && ctx.expr(1) != null) {
      BinaryOpExpressionNode b1 = new BinaryOpExpressionNode(
          ctx.getStart().getLine(),
          ctx.getStart().getCharPositionInLine(),
          b
      );
      b1.setLhs((ExpressionNode) visit(ctx.expr(0)));
      b1.setRhs((ExpressionNode) visit(ctx.expr(1)));
      return b1;
    }

    if((_ctx = ctx.unaryOper()) != null) {

      UnaryOpExpressionNode un = (UnaryOpExpressionNode) visit(_ctx);

      un.setExpression((ExpressionNode) visit(ctx.expr(0)));

      return un;
    }

    if((_ctx = ctx.arrayElem()) != null)
      return visit(_ctx);
    else if((tn = ctx.INT_LITER()) != null)
      return new IntLiteralNode(tn.getSymbol().getLine(), tn.getSymbol().getCharPositionInLine(), tn.getText(),ctx.MINUS() != null);
    else if((tn = ctx.BOOL_LITER()) != null)
      return visit(tn);
    else if((tn = ctx.CHAR_LITER()) != null)
      return visit(tn);
    else if((tn = ctx.STR_LITER()) != null)
      return visit(tn);
    else if((tn = ctx.PAIR_LITER()) != null)
      return visit(tn);
    else if((tn = ctx.IDENT()) != null)
      return visit(tn);
    else if((_ctx = ctx.expr(1)) != null) //brackets case
      return visit(_ctx);
    else
      return null;
  }

  @Override
  public WaccAstNode visitStat(StatContext ctx) {
    return visit(ctx.stat_s());
  }

  @Override
  public WaccAstNode visitStatSeq(StatSeqContext ctx) {
    StatementNode left = (StatementNode) visit(ctx.stat_s(0));
    StatementNode right = (StatementNode) visit(ctx.stat_s(1));
    List<StatementNode> statements;
    if (left instanceof SequenceNode) {
      //absorb sequence node using its list as the new list
      statements = ((SequenceNode) left).getStatements();
      //from base case size > 2 so this is fine
      statements.add(right);
    } else {
      statements = new ArrayList<>();
      statements.add(left);
      statements.add(right);
    }
    return new SequenceNode(statements);
  }

  private WaccAstNode visitStatKeyWord(int line, int col, ExprContext ctx, Keyword keyword) {
    return new KeywordStatementNode(
        line,
        col,
        keyword,
        (ExpressionNode) visit(ctx)
    );
  }

  @Override
  public WaccAstNode visitStatRead(StatReadContext ctx) {
    return new ReadStatementNode(
        ctx.getStart().getLine(),
        ctx.getStart().getCharPositionInLine(),
        (AssignLhsNode) visit(ctx.assignLHS())
    );
  }

  @Override
  public WaccAstNode visitStatFree(StatFreeContext ctx) {
    return visitStatKeyWord(
        ctx.getStart().getLine(),
        ctx.getStart().getCharPositionInLine(),
        ctx.expr(),
        Keyword.FREE
    );
  }

  @Override
  public WaccAstNode visitStatPrint(StatPrintContext ctx) {
    return visitStatKeyWord(
        ctx.getStart().getLine(),
        ctx.getStart().getCharPositionInLine(),
        ctx.expr(),
        Keyword.PRINT
    );
  }

  @Override
  public WaccAstNode visitStatPrintLn(StatPrintLnContext ctx) {
    return visitStatKeyWord(
        ctx.getStart().getLine(),
        ctx.getStart().getCharPositionInLine(),
        ctx.expr(),
        Keyword.PRINTLN
    );
  }

  @Override
  public WaccAstNode visitStatIdentAssign(StatIdentAssignContext ctx) {
    return new IdentAssignNode(
        ctx.getStart().getLine(),
        ctx.getStart().getCharPositionInLine(),
        (TypeNode) visit(ctx.type()),
        (IdentifierNode) visit(ctx.IDENT()),
        (AssignRhsNode) visit(ctx.assignRHS())
    );
  }

  @Override
  public WaccAstNode visitStatExit(StatExitContext ctx) {
    return visitStatKeyWord(
        ctx.getStart().getLine(),
        ctx.getStart().getCharPositionInLine(),
        ctx.expr(),
        Keyword.EXIT
    );
  }

  @Override
  public WaccAstNode visitStatWhile(StatWhileContext ctx) {
    return new WhileStatementNode(
        ctx.getStart().getLine(),
        ctx.getStart().getCharPositionInLine(),
        (ExpressionNode) visit(ctx.expr()),
        (StatementNode) visit(ctx.stat_s())
    );
  }

  @Override
  public WaccAstNode visitStatBegin(StatBeginContext ctx) {
    return new BlockNode(
        ctx.getStart().getLine(),
        ctx.getStart().getCharPositionInLine(),
        (StatementNode) visit(ctx.stat_s())
    );
  }

  @Override
  public WaccAstNode visitStatSkip(StatSkipContext ctx) {
    return new SkipStatementNode(
        ctx.getStart().getLine(),
        ctx.getStart().getCharPositionInLine()
    );
  }

  @Override
  public WaccAstNode visitStatReturn(StatReturnContext ctx) {
    return visitStatKeyWord(
        ctx.getStart().getLine(),
        ctx.getStart().getCharPositionInLine(),
        ctx.expr(),
        Keyword.RETURN
    );
  }

  @Override
  public WaccAstNode visitStatIf(StatIfContext ctx) {
    return new IfStatementNode(
        ctx.getStart().getLine(),
        ctx.getStart().getCharPositionInLine(),
        (ExpressionNode) visit(ctx.expr()),
        (StatementNode) visit(ctx.stat_s(0)),
        (StatementNode) visit(ctx.stat_s(1))
    );
  }

  @Override
  public WaccAstNode visitStatAssignLHSAssign(StatAssignLHSAssignContext ctx) {
    return new AssignNode(
        ctx.getStart().getLine(),
        ctx.getStart().getCharPositionInLine(),
        (AssignLhsNode) visit(ctx.assignLHS()),
        (AssignRhsNode) visit(ctx.assignRHS())
    );
  }

  @Override
  public WaccAstNode visitParam(ParamContext ctx) {
    return new ParameterNode(
        ctx.getStart().getLine(),
        ctx.getStart().getCharPositionInLine(),
        (TypeNode) visit(ctx.type()),
        (IdentifierNode) visit(ctx.IDENT())
    );
  }

  @Override
  public WaccAstNode visitParamList(ParamListContext ctx) {
    ParamContext _ctx;
    int i = 0;

    List<ParameterNode> nodes = new ArrayList<>();

    while(true) {
      _ctx = ctx.param(i);
      if(_ctx == null)
        break;
      //else
      nodes.add((ParameterNode) visit(_ctx));
      ++i;
    }

    return new ParameterListNode(
        ctx.getStart().getLine(),
        ctx.getStart().getCharPositionInLine(),
        nodes
    );
  }

  @Override
  public WaccAstNode visitFunc(FuncContext ctx) {
    ParamListContext _ctx = ctx.paramList();

    return new FunctionNode(
        ctx.getStart().getLine(),
        ctx.getStart().getCharPositionInLine(),
        (TypeNode) visit(ctx.type()),
        (IdentifierNode) visit(ctx.IDENT()),
        _ctx == null ? null : (ParameterListNode) visit(_ctx),
        (StatementNode) visit(ctx.stat())
    );
  }

  @Override
  public WaccAstNode visitProg(ProgContext ctx) {
    FuncContext _ctx;

    //Obtain function contexts.
    List<FuncContext> funcs = new ArrayList<>();
    int i = 0;
    while(true) {
      _ctx = ctx.func(i);
      if(_ctx == null)
        break;
      //else:
      funcs.add(_ctx);
      ++i;
    }

    //Obtain function nodes and add to program node
    List<FunctionNode> functionNodes = new ArrayList<>();
    for (FuncContext func : funcs) {
      functionNodes.add((FunctionNode) visit(func));
    }

    return new ProgramNode(
        ctx.getStart().getLine(),
        ctx.getStart().getCharPositionInLine(),
        functionNodes,
        (StatementNode) visit(ctx.stat())
    );
  }

  @Override
  public WaccAstNode visit(ParseTree parseTree) {
    return parseTree.accept(this);
  }

  @Override
  public WaccAstNode visitChildren(RuleNode ruleNode) {
    return null;
  }

  @Override
  public WaccAstNode visitTerminal(TerminalNode terminalNode) {
    Token token = terminalNode.getSymbol();
    int type = token.getType();
    String val = token.getText();
    switch(type) {
      case IDENT:
        return new IdentifierNode(
            token.getLine(),
            token.getCharPositionInLine(),
            val
        );

      case INT_LITER:
        return new IntLiteralNode(
            token.getLine(),
            token.getCharPositionInLine(),
            val,
            false
        );

      case CHAR_LITER:
        char c = val.charAt(1) == '\\' ? val.charAt(2) : val.charAt(1);
        return new CharLiteralNode(
            token.getLine(),
            token.getCharPositionInLine(),
            c
        );

      case BOOL_LITER:
        return new BoolLiteralNode(
            token.getLine(),
            token.getCharPositionInLine(),
            val.toLowerCase().equals("true")
        );

      case STR_LITER:
        return new StrLiteralNode(
            token.getLine(),
            token.getCharPositionInLine(),
            val
        );

      case PAIR_LITER:
        return new PairLiteralNode(
            token.getLine(),
            token.getCharPositionInLine()
        );

      default:
        return null;
    }
  }

  @Override
  public WaccAstNode visitErrorNode(ErrorNode errorNode) {
    return null;
  }
}
