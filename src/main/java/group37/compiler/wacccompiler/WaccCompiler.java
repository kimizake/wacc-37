package group37.compiler.wacccompiler;

import group37.compiler.Compiler;
import group37.compiler.wacccompiler.ast.WaccAstNode;
import group37.compiler.wacccompiler.frontend.WaccLexer;
import group37.compiler.wacccompiler.frontend.WaccParser;
import group37.compiler.wacccompiler.frontend.WaccSemanticChecker;
import org.antlr.v4.runtime.BufferedTokenStream;

public class WaccCompiler extends Compiler<BufferedTokenStream, WaccAstNode> {

  public WaccCompiler() {
    super(new WaccParser(), new WaccLexer(), new WaccSemanticChecker());
  }

}
