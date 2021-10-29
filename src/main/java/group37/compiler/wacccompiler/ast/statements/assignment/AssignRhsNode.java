package group37.compiler.wacccompiler.ast.statements.assignment;

import group37.compiler.wacccompiler.ast.statements.StatementNode;
import group37.compiler.wacccompiler.ast.types.TypeNode;
import group37.compiler.wacccompiler.symboltable.SymbolTable;

public interface AssignRhsNode extends StatementNode {

  TypeNode expType(SymbolTable context);

}
