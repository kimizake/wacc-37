package group37.compiler.wacccompiler.ast;

import group37.compiler.wacccompiler.backend.WaccAstVisitor;

public interface WaccAstNode {

  <T> T accept(WaccAstVisitor<T> waccAstVisitor);

}
