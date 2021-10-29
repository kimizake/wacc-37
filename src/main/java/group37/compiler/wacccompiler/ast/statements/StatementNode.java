package group37.compiler.wacccompiler.ast.statements;

import group37.compiler.wacccompiler.ast.WaccAstNode;
import group37.compiler.wacccompiler.symboltable.SymbolTable;

public interface StatementNode extends WaccAstNode {

  boolean checkValid(SymbolTable context);

  default int countReturns() {
    return 0;
  }

  default boolean checkValidReturn() {
    return false;
  }

}
