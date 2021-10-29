package group37.compiler.wacccompiler.backend;


import group37.compiler.wacccompiler.ast.WaccAstNode;

public interface WaccAstVisitor<T> {

  T visit(WaccAstNode ctx);

  T visitProgramNode(WaccAstNode ctx);

  T visitParameterNode(WaccAstNode ctx);

  T visitParameterListNode(WaccAstNode ctx);

  T visitFunctionNode(WaccAstNode ctx);

  T visitFunctionTypeNode(WaccAstNode ctx);

  T visitBaseTypeNode(WaccAstNode ctx);

  T visitPairTypeNode(WaccAstNode ctx);

  T visitArrayTypeNode(WaccAstNode ctx);

  T visitWhileStatementNode(WaccAstNode ctx);

  T visitSkipStatementNode(WaccAstNode ctx);

  T visitSequenceNode(WaccAstNode ctx);

  T visitReadStatementNode(WaccAstNode ctx);

  T visitKeywordStatementNode(WaccAstNode ctx);

  T visitIfStatementNode(WaccAstNode ctx);

  T visitBlockNode(WaccAstNode ctx);

  T visitAssignNode(WaccAstNode ctx);

  T visitCallNode(WaccAstNode ctx);

  T visitIdentAssignNode(WaccAstNode ctx);

  T visitNewpairNode(WaccAstNode ctx);

  T visitUnaryOpExpressionNode(WaccAstNode ctx);

  T visitPairElemNode(WaccAstNode ctx);

  T visitIdentifierNode(WaccAstNode ctx);

  T visitBinaryOpExpressionNode(WaccAstNode ctx);

  T visitArrayElemNode(WaccAstNode ctx);

  T visitStrLiteralNode(WaccAstNode ctx);

  T visitPairLiteralNode(WaccAstNode ctx);

  T visitIntLiteralNode(WaccAstNode ctx);

  T visitCharLiteralNode(WaccAstNode ctx);

  T visitBoolLiteralNode(WaccAstNode ctx);

  T visitArrayLiteralNode(WaccAstNode ctx);

}
