package group37.compiler.wacccompiler.ast.expression;

import group37.compiler.wacccompiler.ast.WaccAstNode;
import group37.compiler.wacccompiler.ast.statements.assignment.AssignRhsNode;
import group37.compiler.wacccompiler.ast.types.TypeNode;
import group37.compiler.wacccompiler.backend.arminstructions.ArmProgram.LibFunction;
import group37.compiler.wacccompiler.backend.arminstructions.Label;
import group37.compiler.wacccompiler.symboltable.SymbolTable;

public interface ExpressionNode extends WaccAstNode, AssignRhsNode {

  boolean checkValid(SymbolTable context);

  TypeNode expType(SymbolTable context);

  default LibFunction getPrintFunction(SymbolTable context) {
    throw new UnsupportedOperationException();
  }

}
