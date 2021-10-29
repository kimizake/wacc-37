package group37.compiler.wacccompiler.backend.arminstructions;

public class CharLit implements Expression {

  private final char c;

  public CharLit(char c) {
    this.c = c;
  }

  @Override
  public String toString() {
    return "'" + c + "'";
  }
}
