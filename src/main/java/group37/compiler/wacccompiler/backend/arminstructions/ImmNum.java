package group37.compiler.wacccompiler.backend.arminstructions;

public class ImmNum implements Expression {

  private final Integer n;

  public ImmNum(int n) {
    this.n = n;
  }

  @Override
  public String toString() {
    return n.toString();
  }
}
