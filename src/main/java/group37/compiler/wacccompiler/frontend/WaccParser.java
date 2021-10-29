package group37.compiler.wacccompiler.frontend;

import group37.antlr.*;
import group37.compiler.frontend.Lex;
import group37.compiler.frontend.Parse;
import group37.compiler.frontend.Parser;
import group37.compiler.wacccompiler.ast.WaccAstNode;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class WaccParser implements Parser<BufferedTokenStream, WaccAstNode> {

  @Override
  public Parse<WaccAstNode> parse(Lex<BufferedTokenStream> lex) {
    WACCParser parser = new WACCParser(lex.getLex());
    parser.removeErrorListeners();
    parser.addErrorListener(new WaccTestListener());
    ParseTree tree = parser.prog();
    if (parser.getNumberOfSyntaxErrors() > 0) {
      return null;
    } else {
      _WaccVisit v = new _WaccVisit();
      WaccAstNode n = v.visit(tree);
      return new Parse<>(n);
    }
  }

}
