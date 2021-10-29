package group37.compiler.wacccompiler.ast.statements.assignment;

import group37.compiler.wacccompiler.ast.statements.StatementNode;
import group37.compiler.wacccompiler.ast.types.TypeNode;
import group37.compiler.wacccompiler.backend.arminstructions.ArmProgram.LibFunction;
import group37.compiler.wacccompiler.symboltable.SymbolTable;
//import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public interface AssignLhsNode extends StatementNode {

  TypeNode expType(SymbolTable context);

  default LibFunction getReadFunction(SymbolTable context) {
    throw new UnsupportedOperationException();
  }

  default String getId() {
    throw new UnsupportedOperationException();
  }

}
