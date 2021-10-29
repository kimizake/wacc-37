package group37.compiler.wacccompiler.backend;

import group37.compiler.wacccompiler.ast.*;
import group37.compiler.wacccompiler.ast.expression.*;
import group37.compiler.wacccompiler.ast.expression.literal.*;
import group37.compiler.wacccompiler.ast.statements.*;
import group37.compiler.wacccompiler.ast.statements.assignment.AssignLhsNode;
import group37.compiler.wacccompiler.ast.statements.assignment.CallNode;
import group37.compiler.wacccompiler.ast.statements.assignment.NewpairNode;
import group37.compiler.wacccompiler.backend.arminstructions.ArmProgram.LibFunction;
import group37.compiler.wacccompiler.backend.arminstructions.BranchIns;
import group37.compiler.wacccompiler.backend.arminstructions.BranchLinkIns;
import group37.compiler.wacccompiler.backend.arminstructions.CharLit;
import group37.compiler.wacccompiler.backend.arminstructions.CompIns;
import group37.compiler.wacccompiler.backend.arminstructions.Condition;
import group37.compiler.wacccompiler.backend.arminstructions.Data;
import group37.compiler.wacccompiler.backend.arminstructions.DataInsOpcode;
import group37.compiler.wacccompiler.backend.arminstructions.DataProcessIns;
import group37.compiler.wacccompiler.backend.arminstructions.DefineLabel;
import group37.compiler.wacccompiler.backend.arminstructions.ArmIns;
import group37.compiler.wacccompiler.backend.arminstructions.ImmNum;
import group37.compiler.wacccompiler.backend.arminstructions.Label;
import group37.compiler.wacccompiler.backend.arminstructions.SingOpDataIns;
import group37.compiler.wacccompiler.backend.arminstructions.MulIns;
import group37.compiler.wacccompiler.backend.arminstructions.PopIns;
import group37.compiler.wacccompiler.backend.arminstructions.PushIns;
import group37.compiler.wacccompiler.backend.arminstructions.Register;
import group37.compiler.wacccompiler.backend.arminstructions.SdtIns;
import group37.compiler.wacccompiler.backend.arminstructions.SdtOpcode;
import group37.compiler.wacccompiler.ast.statements.assignment.AssignNode;
import group37.compiler.wacccompiler.ast.statements.assignment.IdentAssignNode;
import group37.compiler.wacccompiler.backend.arminstructions.*;
import group37.compiler.wacccompiler.symboltable.STEntry;
import group37.compiler.wacccompiler.symboltable.SymbolTable;

import java.util.*;

import static group37.compiler.wacccompiler.ast.types.TypeMatcher.STRING_NODE;
import static group37.compiler.wacccompiler.backend.arminstructions.ArmProgram.LibFunction.*;

public class CodeGenAstVisitor implements WaccAstVisitor<Void> {

  //all functions add on their respective code to the end of the code list
  private List<ArmIns> instructions;
  //this is used to store string literals
  private List<Data> dataSegment;
  //this is used to store the set of lib fucntions in use
  private Set<LibFunction> libFunctions;
  //translations always put their result in the first element of the reg list
  private Deque<Register> generalRegs;
  //the symbol table contains the stack pointer offset and type of each identifier
  private SymbolTable symbolTable;

  public List<ArmIns> getInstructions() {
    return instructions;
  }

  public List<Data> getDataSegment() {
    return dataSegment;
  }

  public Set<LibFunction> getUsedLibFunctions() {
    return libFunctions;
  }

  @Override
  public Void visit(WaccAstNode node) {
    node.accept(this);
    return null;
  }

  //never visited
  @Override
  public Void visitArrayTypeNode(WaccAstNode node) {
    return null;
  }

  //never visited
  @Override
  public Void visitFunctionTypeNode(WaccAstNode node) {
    return null;
  }

  /*
  visit the program root node initialising the final data that the arm program
  class needs
  visits all functions and the main statement
   */
  @Override
  public Void visitProgramNode(WaccAstNode node) {
    instructions = new ArrayList<>();
    dataSegment = new ArrayList<>();
    libFunctions = new LinkedHashSet<>();
    generalRegs = Register.getGeneralRegs();
    ProgramNode program = (ProgramNode) node;
    for (FunctionNode f : program.getFunctions()) {
      visit(f);
    }
    symbolTable = new SymbolTable();
    symbolTable.enterScope();
    instructions.add(new DefineLabel("main"));
    instructions.add(new PushIns(Register.LR));
    int declarations = getNumberOfDeclarations(program.getStatement());
    subFromSp(declarations);
    visit(program.getStatement());
    addToSp(declarations);
    instructions.add(new SdtIns(SdtOpcode.LDR, Register.R0, new ImmNum(0)));
    instructions.add(new PopIns(Register.PC));
    instructions.add(new ltorgLabel());
    return null;
  }

  /*
  helper function for adding instructions to increment the stack pointer in
  both the symbol table and the final assembly
   */
  private Void addToSp(int declarations) {
    if (declarations > 0) {
      instructions.add(new SdtIns(SdtOpcode.LDR, generalRegs.peek(),
          new ImmNum(declarations * 4)));
      instructions.add(
          new DataProcessIns(DataInsOpcode.ADD, Register.SP, Register.SP,
              generalRegs.peek()));
    }
    symbolTable.increment(declarations);
    return null;
  }

  /*
  helper function for adding instructions to decrement the stack pointer in
  both the symbol table and the final assembly
   */
  private Void subFromSp(int declarations) {
    if (declarations > 0) {
      instructions.add(new SdtIns(SdtOpcode.LDR, generalRegs.peek(),
          new ImmNum(declarations * 4)));
      instructions.add(
          new DataProcessIns(DataInsOpcode.SUB, Register.SP, Register.SP,
              generalRegs.peek()));
    }
    symbolTable.decrement(declarations);
    return null;
  }

  //never visited
  @Override
  public Void visitParameterNode(WaccAstNode node) {
    return null;
  }

  /*
  adds the parameters for a function into the current symbol table and changes
  the stack pointer accordingly
   */
  @Override
  public Void visitParameterListNode(WaccAstNode node) {
    ParameterListNode ps = (ParameterListNode) node;
    int offset = 4;
    for (ParameterNode p : ps.getParameters()) {
      symbolTable.addEntry(p.getId(), new STEntry(p.getType(), offset));
      offset += 4;
    }
    return null;
  }

  @Override
  public Void visitFunctionNode(WaccAstNode node) {
    //get new symbol table for this function and child scopes
    symbolTable = new SymbolTable();
    symbolTable.enterScope();
    FunctionNode f = (FunctionNode) node;
    instructions.add(new DefineLabel("f_" + f.getId()));
    instructions.add(new PushIns(Register.LR));
    /*
    the stack pointer is now defined to be at the top of the link register
    ie 0.
    the arguments will be at 4, 8, 12...
    link register will be at 0
    the local variables will be at -4, -8, -12...
     */
    //load parameters into scope (if any)
    if (f.getParameters() != null) {
      visit(f.getParameters());
    }
    //add instructions from statements
    int declarations = getNumberOfDeclarations(f.getStatement());
    subFromSp(declarations);
    visit(f.getStatement());
    addToSp(declarations);
    //return
    instructions.add(new PopIns(Register.PC));
    instructions.add(new ltorgLabel());
    return null;
  }

  //never visited
  @Override
  public Void visitBaseTypeNode(WaccAstNode node) {
    return null;
  }

  //never visited
  @Override
  public Void visitPairTypeNode(WaccAstNode node) {
    return null;
  }

  /*
  perform code transformation for while loop, visiting statement and condition
  as well as inserting labels for correct control flow
   */
  @Override
  public Void visitWhileStatementNode(WaccAstNode node) {
    WhileStatementNode w = (WhileStatementNode) node;
    Label top = new Label();
    Label bottom = new Label();
    instructions.add(new DefineLabel(top.getLabel()));
    visit(w.getCondition());
    instructions.add(new CompIns(DataInsOpcode.CMP, generalRegs.peek(),
        new ImmNum(0)));
    instructions.add(new BranchIns(Condition.EQ, bottom));
    symbolTable.enterScope();
    int declarations = getNumberOfDeclarations(w.getStatement());
    subFromSp(declarations);
    visit(w.getStatement());
    symbolTable.exitScope();
    addToSp(declarations);
    instructions.add(new BranchIns(top));
    instructions.add(new DefineLabel(bottom.getLabel()));
    return null;
  }

  //defined to do nothing
  @Override
  public Void visitSkipStatementNode(WaccAstNode node) {
    return null;
  }

  /*
  visit all the statements in a sequence block
   */
  @Override
  public Void visitSequenceNode(WaccAstNode node) {
    SequenceNode ss = (SequenceNode) node;
    for (StatementNode s : ss.getStatements()) {
      visit(s);
    }
    return null;
  }

  /*
  insert instructions for visiting a read node, inserting extra instructions
  for pair elem nodes
   */
  @Override
  public Void visitReadStatementNode(WaccAstNode node) {
    ReadStatementNode r = (ReadStatementNode) node;
    LibFunction readFunction = r.getLhs().getReadFunction(symbolTable);
    libFunctions.add(readFunction);
    if (r.getLhs() instanceof PairElemNode) {
      libFunctions.add(CHECK_NULL);
      instructions.add(new SdtIns(SdtOpcode.LDR, Register.R0, new RegWithOffset(
          Register.SP, symbolTable.getOffset(r.getLhs().getId())
      )));
      instructions.add(new BranchLinkIns(new Label(CHECK_NULL.toString())));
    }
    instructions.add(new DataProcessIns(DataInsOpcode.ADD, Register.R0,
        Register.SP, new ImmNum(symbolTable.getOffset(r.getLhs().getId()))));
    instructions.add(new BranchLinkIns(new Label(readFunction.toString())));
    return null;
  }

  /*
  insert the instructions for various keywords like return, free and print
   */
  @Override
  public Void visitKeywordStatementNode(WaccAstNode node) {
    KeywordStatementNode s = (KeywordStatementNode) node;
    Keyword keyword = s.getKeyword();
    ExpressionNode e = s.getExpression();
    visit(e);
    switch (keyword) {
      case EXIT:
        instructions.add(new SingOpDataIns(DataInsOpcode.MOV, Register.R0,
            generalRegs.peek()));
        instructions.add(new BranchLinkIns(new Label("exit")));
        break;
      case FREE:
        instructions.add(new SingOpDataIns(DataInsOpcode.MOV, Register.R0,
            generalRegs.peek()));
        instructions.add(new BranchLinkIns(new Label(FREE_PAIR.toString())));
        libFunctions.add(FREE_PAIR);
        break;
      case RETURN:
        instructions.add(new SingOpDataIns(DataInsOpcode.MOV, Register.R0,
            generalRegs.peek()));
        instructions.add(new SdtIns(SdtOpcode.LDR, generalRegs.peek(),
            new ImmNum(-symbolTable.getStackPointer())));
        instructions.add(
            new DataProcessIns(DataInsOpcode.ADD, Register.SP, Register.SP,
                generalRegs.peek()));
        instructions.add(new PopIns(Register.PC));
        break;
      case PRINTLN: {
        LibFunction printFunction = e.getPrintFunction(symbolTable);
        if (printFunction == PRINT_CHAR_ARRAY) {
          instructions.add(new PushIns(generalRegs.peek()));
        } else {
          instructions.add(new SingOpDataIns(DataInsOpcode.MOV, Register.R0,
              generalRegs.peek()));
        }
        instructions
            .add(new BranchLinkIns(new Label(printFunction.toString())));
        if (printFunction == PRINT_CHAR_ARRAY) {
          instructions.add(new SdtIns(SdtOpcode.LDR, generalRegs.peek(),
              new ImmNum(4)));
          instructions.add(new DataProcessIns(DataInsOpcode.ADD, Register.SP,
              Register.SP, generalRegs.peek()));
        }
        libFunctions.add(printFunction);
        libFunctions.add(PRINT_LN);
        instructions.add(new BranchLinkIns(new Label(PRINT_LN.toString())));
      }
      break;
      case PRINT: {
        LibFunction printFunction = e.getPrintFunction(symbolTable);
        if (printFunction == PRINT_CHAR_ARRAY) {
          instructions.add(new PushIns(generalRegs.peek()));
        } else {
          instructions.add(new SingOpDataIns(DataInsOpcode.MOV, Register.R0,
              generalRegs.peek()));
        }
        instructions
            .add(new BranchLinkIns(new Label(printFunction.toString())));
        if (printFunction == PRINT_CHAR_ARRAY) {
          instructions.add(new SdtIns(SdtOpcode.LDR, generalRegs.peek(),
              new ImmNum(4)));
          instructions.add(new DataProcessIns(DataInsOpcode.ADD, Register.SP,
              Register.SP, generalRegs.peek()));
        }
        libFunctions.add(printFunction);
      }
      break;
    }
    return null;
  }

  /*
  insert code transformation for if statement, visiting the conditional, both
  true and false statements and inserting labels for correct control flow
   */
  @Override
  public Void visitIfStatementNode(WaccAstNode node) {
    int declarations;
    IfStatementNode i = (IfStatementNode) node;
    visit(i.getCondition());
    instructions.add(new CompIns(DataInsOpcode.CMP, generalRegs.peek(),
        new ImmNum(0)));
    Label falseLabel = new Label();
    Label trueLabel = new Label();
    instructions.add(new BranchIns(Condition.EQ, trueLabel));
    symbolTable.enterScope();
    declarations = getNumberOfDeclarations(i.getTrueBlock());
    subFromSp(declarations);
    visit(i.getTrueBlock());
    symbolTable.exitScope();
    addToSp(declarations);
    instructions.add(new BranchIns(falseLabel));
    instructions.add(new DefineLabel(trueLabel.getLabel()));
    symbolTable.enterScope();
    declarations = getNumberOfDeclarations(i.getFalseBlock());
    subFromSp(declarations);
    visit(i.getFalseBlock());
    symbolTable.exitScope();
    addToSp(declarations);
    instructions.add(new DefineLabel(falseLabel.getLabel()));
    return null;
  }

  /*
  visits block node, opening a new scope in the current symbol table and adding
  new variable declarations
   */
  @Override
  public Void visitBlockNode(WaccAstNode node) {
    BlockNode b = (BlockNode) node;
    // enter scope
    symbolTable.enterScope();
    int declarations = getNumberOfDeclarations(b.getStatement());
    subFromSp(declarations);
    // each variable declaration moves offset back by 4
    visit(b.getStatement());
    // add shift to offset
    symbolTable.exitScope();
    addToSp(declarations);
    return null;
  }

  /*
  helper function to get the number of declarations in a given statement,
  not including child scopes
   */
  private int getNumberOfDeclarations(StatementNode sn) {
    int declarations = 0;
    if (sn instanceof IdentAssignNode) {
      declarations = 1;
    } else if (sn instanceof SequenceNode) {
      for (StatementNode s : ((SequenceNode) sn).getStatements()) {
        if (s instanceof IdentAssignNode) {
          declarations += 1;
        }
      }
    }
    return declarations;
  }

  /*
  adds instructions for assignment depending what the type of the left hand side
  is
   */
  @Override
  public Void visitAssignNode(WaccAstNode node) {
    AssignNode an = (AssignNode) node;
    // eval right side
    visit(an.getRhs());
    Register rhs = generalRegs.pop();
    // eval left side
    AssignLhsNode lhs = an.getLhs();

    if (lhs instanceof IdentifierNode) {
      instructions.add(new SdtIns(SdtOpcode.STR, rhs, new RegWithOffset(
          Register.SP, symbolTable.getOffset(((IdentifierNode) lhs).getId()))));
    } else if (lhs instanceof ArrayElemNode) {
      ArrayElemNode a = (ArrayElemNode) lhs;
      int off = symbolTable.getOffset(a.getIdNode().getId());
      Register location = generalRegs.pop();
      instructions.add(new SdtIns(SdtOpcode.LDR, location, new RegWithOffset(
          Register.SP, off)));
      for (ExpressionNode e : a.getIndexList()) {
        visit(e);
        Register index = generalRegs.pop();
        instructions.add(new SingOpDataIns(DataInsOpcode.MOV, Register.R0,
            index));
        instructions.add(new SingOpDataIns(DataInsOpcode.MOV, Register.R1,
            location));
        libFunctions.add(CHECK_ARRAY_BOUNDS);
        instructions.add(new BranchLinkIns(
            new Label(CHECK_ARRAY_BOUNDS.toString())));
        instructions.add(new DataProcessIns(DataInsOpcode.ADD, index, index,
            new ImmNum(1)));
        instructions.add(new SdtIns(SdtOpcode.LDR, generalRegs.peek(),
            new ImmNum(4)));
        instructions.add(new MulIns(index, index, generalRegs.peek()));
        instructions.add(new DataProcessIns(DataInsOpcode.ADD, location,
            location, index));
        generalRegs.push(index);
      }
      if (an.getLhs().expType(symbolTable).equals(STRING_NODE)) {
        instructions.add(new SdtIns(SdtOpcode.STRB, rhs,
            new RegWithOffset(location, 0)));
      } else {
        instructions.add(new SdtIns(SdtOpcode.STR, rhs,
            new RegWithOffset(location, 0)));
      }
      generalRegs.push(location);
    } else if (lhs instanceof PairElemNode) {
      PairElemNode n = (PairElemNode) lhs;
      IdentifierNode id = (IdentifierNode) n.getExpression();
      visit(id);
      instructions.add(new SingOpDataIns(DataInsOpcode.MOV, Register.R0,
          generalRegs.peek()));
      libFunctions.add(CHECK_NULL);
      instructions.add(new BranchLinkIns(new Label(CHECK_NULL.toString())));
      if (n.isFst()) {
        instructions.add(new SdtIns(SdtOpcode.STR, rhs,
            new RegWithOffset(generalRegs.peek(), 0)));
      } else {
        instructions.add(new SdtIns(SdtOpcode.STR, rhs,
            new RegWithOffset(generalRegs.peek(), 4)));
      }
    }
    generalRegs.push(rhs);
    return null;
  }


  /*
  loads parameters into the stack for the calling function and inserts the
  branch with link instruction to call the function
   */
  @Override
  public Void visitCallNode(WaccAstNode node) {
    CallNode c = (CallNode) node;
    List<ExpressionNode> args = c.getArgs();
    //load parameters in reverse order
    for (int i = args.size() - 1; i >= 0; i--) {
      ExpressionNode exp = args.get(i);
      //load current exp val into r4
      visit(exp);
      instructions.add(new PushIns(generalRegs.peek()));
      symbolTable.decrement();
    }
    instructions.add(new BranchLinkIns(
        new Label("f_" + c.getId().getId())
    ));
    addToSp(args.size());
    instructions.add(
        new SingOpDataIns(DataInsOpcode.MOV, generalRegs.peek(), Register.R0));
    return null;
  }

  /*
  insert code for adding a new identifier into the symbol table and current
  stack
   */
  @Override
  public Void visitIdentAssignNode(WaccAstNode node) {
    IdentAssignNode in = (IdentAssignNode) node;
    // eval right side
    visit(in.getRhs());
    int offset = symbolTable.getNewOffset();
    symbolTable.addEntry(in.getId().getId(), new STEntry(in.getType(), offset));
    instructions.add(new SdtIns(SdtOpcode.STR, generalRegs.peek(),
        new RegWithOffset(Register.SP,
            symbolTable.getOffset(in.getId().getId()))));
    return null;
  }

  /*
  allocate new memory for a pair node and store the expressions within the
  initialise in the pair
   */
  @Override
  public Void visitNewpairNode(WaccAstNode node) {
    NewpairNode n = (NewpairNode) node;
    Register store = generalRegs.pop();
    Register head = generalRegs.peek();
    instructions.add(new SdtIns(SdtOpcode.LDR, Register.R0, new ImmNum(8)));
    instructions.add(new BranchLinkIns(new Label("malloc")));
    instructions.add(new SingOpDataIns(DataInsOpcode.MOV, store, Register.R0));
    visit(n.getFst());
    instructions
        .add(new SdtIns(SdtOpcode.STR, head, new RegWithOffset(store, 0)));
    visit(n.getSnd());
    instructions
        .add(new SdtIns(SdtOpcode.STR, head, new RegWithOffset(store, 4)));
    generalRegs.push(store);
    return null;
  }

  /*
  add instructions for converting a unary operation
  there is no case for ord and chr as the are effectively a typecast
   */
  @Override
  public Void visitUnaryOpExpressionNode(WaccAstNode node) {
    UnaryOpExpressionNode e = (UnaryOpExpressionNode) node;
    visit(e.getExpression());
    switch (e.getOp()) {
      case LOGICAL_NEGATION:
        instructions
            .add(new DataProcessIns(DataInsOpcode.EOR, generalRegs.peek(),
                generalRegs.peek(), new ImmNum(1)));
        break;
      case MATHEMATICAL_NEGATION:
        instructions
            .add(new DataProcessIns(DataInsOpcode.RSB, generalRegs.peek(),
                generalRegs.peek(), new ImmNum(0)).withSetCodes(true));
        libFunctions.add(THROW_OVERFLOW_ERROR);
        instructions.add(new BranchLinkIns(
            new Label(THROW_OVERFLOW_ERROR.toString()))
            .withCondition(Condition.VS));
        break;
      case LEN:
        instructions.add(new SdtIns(SdtOpcode.LDR, generalRegs.peek(),
            new RegWithOffset(generalRegs.peek(), 0)));
        break;
    }
    return null;
  }

  /*
  add instructions for getting an element of a pair and storing at the top
  of the register list
   */
  @Override
  public Void visitPairElemNode(WaccAstNode node) {
    PairElemNode n = (PairElemNode) node;
    IdentifierNode id = (IdentifierNode) n.getExpression();
    Register store = generalRegs.pop();
    visit(id);
    instructions.add(
        new SingOpDataIns(DataInsOpcode.MOV, Register.R0, generalRegs.peek()));
    libFunctions.add(CHECK_NULL);
    instructions.add(new BranchLinkIns(new Label(CHECK_NULL.toString())));
    if (n.isFst()) {
      instructions.add(new SdtIns(SdtOpcode.LDR, store,
          new RegWithOffset(generalRegs.peek(), 0)));
    } else {
      instructions.add(new SdtIns(SdtOpcode.LDR, store,
          new RegWithOffset(generalRegs.peek(), 4)));
    }
    generalRegs.push(store);
    return null;
  }

  /*
  load the value of an declared identifier into register
   */
  @Override
  public Void visitIdentifierNode(WaccAstNode node) {
    IdentifierNode id = (IdentifierNode) node;
    int offset = symbolTable.getOffset(id.getId());
    instructions.add(new SdtIns(SdtOpcode.LDR, generalRegs.peek(),
        new RegWithOffset(Register.SP, offset)));
    return null;
  }

  /*
  insert instructions for translating binary operators
  will decide to use the stack if the amount of registers runs out
  will also insert runtime error checking where appropriate
   */
  @Override
  public Void visitBinaryOpExpressionNode(WaccAstNode node) {
    BinaryOpExpressionNode e = (BinaryOpExpressionNode) node;
    visit(e.getLhs());
    boolean useStack = generalRegs.size() == 2;
    Register dst, srca, srcb;
    if (useStack) {
      dst = Register.R10;
      srca = Register.R11;
      srcb = dst;
      instructions.add(new PushIns(dst));
    } else {
      dst = generalRegs.pop();
      srca = dst;
      srcb = generalRegs.peek();
    }
    visit(e.getRhs());
    if (useStack) {
      instructions.add(new PopIns(srca));
    }
    switch (e.getOp()) {
      case SUBTRACT:
        instructions.add(new DataProcessIns(DataInsOpcode.SUB, dst,
            srca, srcb).withSetCodes(true));
        libFunctions.add(THROW_OVERFLOW_ERROR);
        instructions.add(new BranchLinkIns(
            new Label(THROW_OVERFLOW_ERROR.toString()))
            .withCondition(Condition.VS));
        break;
      case MULTIPLY:
        Register tmp = useStack ? srca : srcb;
        instructions.add(new SmulIns(dst, tmp));
        instructions.add(new CompIns(DataInsOpcode.CMP, tmp, dst).withExtention(
            ", ASR #31"));
        libFunctions.add(THROW_OVERFLOW_ERROR);
        instructions.add(new BranchLinkIns(
            new Label(THROW_OVERFLOW_ERROR.toString()))
            .withCondition(Condition.NE));
        break;
      case MODULO: {
        Register divisor, dividend;
        if (useStack) {
          dividend = srca;
          divisor = dst;
        } else {
          dividend = dst;
          divisor = srcb;
        }
        instructions
            .add(new SingOpDataIns(DataInsOpcode.MOV, Register.R0, dividend));
        instructions
            .add(new SingOpDataIns(DataInsOpcode.MOV, Register.R1, divisor));
        libFunctions.add(CHECK_DIV_ZERO);
        instructions
            .add(new BranchLinkIns(new Label(CHECK_DIV_ZERO.toString())));
        instructions.add(new BranchLinkIns(new Label("__aeabi_idivmod")));
        instructions
            .add(new SingOpDataIns(DataInsOpcode.MOV, dst, Register.R1));
        break;
      }
      case DIVIDE: {
        Register divisor, dividend;
        if (useStack) {
          dividend = srca;
          divisor = dst;
        } else {
          dividend = dst;
          divisor = srcb;
        }
        instructions
            .add(new SingOpDataIns(DataInsOpcode.MOV, Register.R0, dividend));
        instructions
            .add(new SingOpDataIns(DataInsOpcode.MOV, Register.R1, divisor));
        libFunctions.add(CHECK_DIV_ZERO);
        instructions
            .add(new BranchLinkIns(new Label(CHECK_DIV_ZERO.toString())));
        instructions.add(new BranchLinkIns(new Label("__aeabi_idiv")));
        instructions
            .add(new SingOpDataIns(DataInsOpcode.MOV, dst, Register.R0));
        break;
      }
      case ADD:
        instructions.add(new DataProcessIns(DataInsOpcode.ADD, dst,
            srca, srcb).withSetCodes(true));
        libFunctions.add(THROW_OVERFLOW_ERROR);
        instructions.add(new BranchLinkIns(
            new Label(THROW_OVERFLOW_ERROR.toString()))
            .withCondition(Condition.VS));
        break;
      case AND:
        instructions.add(new DataProcessIns(DataInsOpcode.AND, dst,
            srca, srcb));
        break;
      case OR:
        instructions.add(new DataProcessIns(DataInsOpcode.ORR, dst,
            srca, srcb));
        break;
      case GREATER_THAN_EQUAL:
        instructions.add(new CompIns(DataInsOpcode.CMP, dst,
            generalRegs.peek()));
        instructions.add(new SingOpDataIns(DataInsOpcode.MOV, dst,
            new ImmNum(1)).withCondition(Condition.GE));
        instructions.add(new SingOpDataIns(DataInsOpcode.MOV, dst,
            new ImmNum(0)).withCondition(Condition.LT));
        break;
      case LESS_THAN_EQUAL:
        instructions.add(new CompIns(DataInsOpcode.CMP, dst,
            generalRegs.peek()));
        instructions.add(new SingOpDataIns(DataInsOpcode.MOV, dst,
            new ImmNum(1)).withCondition(Condition.LE));
        instructions.add(new SingOpDataIns(DataInsOpcode.MOV, dst,
            new ImmNum(0)).withCondition(Condition.GT));
        break;
      case GREATER_THAN:
        instructions.add(new CompIns(DataInsOpcode.CMP, dst,
            generalRegs.peek()));
        instructions.add(new SingOpDataIns(DataInsOpcode.MOV, dst,
            new ImmNum(1)).withCondition(Condition.GT));
        instructions.add(new SingOpDataIns(DataInsOpcode.MOV, dst,
            new ImmNum(0)).withCondition(Condition.LE));
        break;
      case NOT_EQUAL:
        instructions.add(new CompIns(DataInsOpcode.CMP, dst,
            generalRegs.peek()));
        instructions.add(new SingOpDataIns(DataInsOpcode.MOV, dst,
            new ImmNum(1)).withCondition(Condition.NE));
        instructions.add(new SingOpDataIns(DataInsOpcode.MOV, dst,
            new ImmNum(0)).withCondition(Condition.EQ));
        break;
      case LESS_THAN:
        instructions.add(new CompIns(DataInsOpcode.CMP, dst,
            generalRegs.peek()));
        instructions.add(new SingOpDataIns(DataInsOpcode.MOV, dst,
            new ImmNum(1)).withCondition(Condition.LT));
        instructions.add(new SingOpDataIns(DataInsOpcode.MOV, dst,
            new ImmNum(0)).withCondition(Condition.GE));
        break;
      case EQUAL:
        instructions.add(new CompIns(DataInsOpcode.CMP, dst,
            generalRegs.peek()));
        instructions.add(new SingOpDataIns(DataInsOpcode.MOV, dst,
            new ImmNum(1)).withCondition(Condition.EQ));
        instructions.add(new SingOpDataIns(DataInsOpcode.MOV, dst,
            new ImmNum(0)).withCondition(Condition.NE));
        break;
    }
    if (!useStack) {
      generalRegs.push(dst);
    }
    return null;
  }

  /*
  returns the value indexed by an array elem
  also inserts bounds checking
   */
  @Override
  public Void visitArrayElemNode(WaccAstNode node) {
    ArrayElemNode an = (ArrayElemNode) node;
    int off = symbolTable.getOffset(an.getIdNode().getId());
    Register location = generalRegs.pop();
    instructions.add(new SdtIns(SdtOpcode.LDR, location,
        new RegWithOffset(Register.SP, off)));
    for (ExpressionNode e : an.getIndexList()) {
      visit(e);
      Register index = generalRegs.pop();
      instructions.add(new SingOpDataIns(DataInsOpcode.MOV, Register.R0,
          index));
      instructions.add(new SingOpDataIns(DataInsOpcode.MOV, Register.R1,
          location));
      libFunctions.add(CHECK_ARRAY_BOUNDS);
      instructions.add(new BranchLinkIns(
          new Label(CHECK_ARRAY_BOUNDS.toString())));
      instructions.add(new DataProcessIns(DataInsOpcode.ADD, index, index,
          new ImmNum(1)));
      instructions.add(new SdtIns(SdtOpcode.LDR, generalRegs.peek(),
          new ImmNum(4)));
      instructions.add(new MulIns(index, index, generalRegs.peek()));
      instructions.add(new DataProcessIns(DataInsOpcode.ADD, location, location,
          index));
      instructions.add(new SdtIns(SdtOpcode.LDR, location,
          new RegWithOffset(location, 0)));
      generalRegs.push(index);
    }
    generalRegs.push(location);
    return null;
  }

  /*
  loads a string literal into the data segment and gets it from the data
  segment in the assembly
   */
  @Override
  public Void visitStrLiteralNode(WaccAstNode node) {
    StrLiteralNode s = (StrLiteralNode) node;
    dataSegment.add(new Data(s.getLiteral()));
    instructions.add(new SdtIns(SdtOpcode.LDR, generalRegs.peek(),
        new Label("msg_" + (dataSegment.size() - 1))));
    return null;
  }

  /*
  load into head register the value 0 (null)
   */
  @Override
  public Void visitPairLiteralNode(WaccAstNode node) {
    instructions.add(new SingOpDataIns(DataInsOpcode.MOV, generalRegs.peek(),
        new ImmNum(0)));
    return null;
  }

  /*
  load int literal into head register
   */
  @Override
  public Void visitIntLiteralNode(WaccAstNode node) {
    instructions.add(new SdtIns(SdtOpcode.LDR, generalRegs.peek(),
        new ImmNum(((IntLiteralNode) node).getLiteral())));
    return null;
  }

  /*
  load char literal into head register
   */
  @Override
  public Void visitCharLiteralNode(WaccAstNode node) {
    instructions.add(new SingOpDataIns(DataInsOpcode.MOV, generalRegs.peek(),
        new CharLit(((CharLiteralNode) node).getLiteral())));
    return null;
  }

  /*
  load bool literal into head register
   */
  @Override
  public Void visitBoolLiteralNode(WaccAstNode node) {
    int x = ((BoolLiteralNode) node).getLiteral() ? 1 : 0;
    instructions.add(new SdtIns(SdtOpcode.LDR, generalRegs.peek(),
        new ImmNum(x)));
    return null;
  }

  /*
  adds instructions for creating a heap array and stores the address of the
  array head at the head register
   */
  @Override
  public Void visitArrayLiteralNode(WaccAstNode node) {
    ArrayLiteralNode arrayLiteral = (ArrayLiteralNode) node;
    // put the array pointer in reg
    Register reg = generalRegs.pop();
    // array literal is a list of expressions
    List<ExpressionNode> expressionNodeList = arrayLiteral
        .getExpressionNodeList();
    int size = expressionNodeList.size();
    ListIterator<ExpressionNode> expressionNodeListIterator = expressionNodeList
        .listIterator();
    // create memory on heap for the array and put address in reg
    // + 1 to hold size of array
    instructions.add(new SdtIns(SdtOpcode.LDR, Register.R0,
        new ImmNum(4 * (size + 1))));
    //insert call to malloc to get heap memory
    instructions.add(new BranchLinkIns(new Label("malloc")));
    instructions.add(new SingOpDataIns(DataInsOpcode.MOV, reg, Register.R0));
    // put all expressions into array
    while (expressionNodeListIterator.hasNext()) {
      ExpressionNode e = expressionNodeListIterator.next();
      // load expression into top general register
      visit(e);
      // store value on heap
      instructions.add(new SdtIns(SdtOpcode.STR, generalRegs.peek(),
          new RegWithOffset(reg,
              4 * expressionNodeListIterator.nextIndex())));
    }
    // load and store size of array on heap at top
    instructions.add(new SdtIns(SdtOpcode.LDR, generalRegs.peek(),
        new ImmNum(size)));
    instructions.add(new SdtIns(SdtOpcode.STR, generalRegs.peek(),
        new RegWithOffset(reg, 0)));
    generalRegs.push(reg);
    return null;
  }
}
