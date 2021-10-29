package group37.compiler.wacccompiler.ast;

import java.util.List;

public abstract class AbstractWaccNode implements WaccAstNode {

  private final int line;
  private final int col;

  public List accept() {
    return null;
  }

  public AbstractWaccNode(int line, int col) {
    this.line = line;
    this.col = col;
  }

  public int getLine() {
    return line;
  }

  public int getCol() {
    return col;
  }
}