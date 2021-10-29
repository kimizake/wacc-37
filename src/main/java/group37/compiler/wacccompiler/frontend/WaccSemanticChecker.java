package group37.compiler.wacccompiler.frontend;

import group37.compiler.frontend.ErrorLogger;
import group37.compiler.frontend.ErrorMessage;
import group37.compiler.frontend.Parse;
import group37.compiler.frontend.SemanticChecker;
import group37.compiler.wacccompiler.ast.FunctionNode;
import group37.compiler.wacccompiler.ast.ProgramNode;
import group37.compiler.wacccompiler.ast.WaccAstNode;
import group37.compiler.wacccompiler.symboltable.STEntry;
import group37.compiler.wacccompiler.symboltable.SymbolTable;

public class WaccSemanticChecker implements SemanticChecker<WaccAstNode> {

  @Override
  public boolean checkValid(Parse<WaccAstNode> parse) {

    ProgramNode root = (ProgramNode) parse.getParse();
    SymbolTable globalScope = new SymbolTable();
    globalScope.enterScope();
    boolean valid = true;

    for (FunctionNode f : root.getFunctions()) {
      //add functions into the global scope checking if they have already been declared after each one
      String id = f.getId();
      if (!globalScope.defined(id)) {
        globalScope.add(id, new STEntry(f.getTypeSignature()));
      } else {
        valid = false;
        String msg = "function " + id + " has already been defined";
        ErrorMessage errorMessage = new ErrorMessage(f.getLine(), f.getCol(), ErrorMessage.ErrorType.SEMANTIC, msg);
        ErrorLogger.addMessage(errorMessage);
      }
    }

    for (FunctionNode f : root.getFunctions()) {
      //check each function by checking its statement and using its parameters as a symboltable
      SymbolTable fScope = new SymbolTable(globalScope.getCurScope());
      fScope.setCallingFunction(f.getTypeSignature());
      if (!f.checkValid(fScope)) {
        valid = false;
        System.out.println(f.getLine() + " " + f.getCol() + f.getId() + "semantic error");
      }
    }

    //check the statement of the program body using the global scope of function adding into the same
    //way as if it was a function
    SymbolTable programScope = new SymbolTable(globalScope.getCurScope());
    if (!root.getStatement().checkValid(programScope)) {
      valid = false;
      System.out.println("Semantic error in main function");
    }

    return valid;
  }

}
