package group37.compiler.wacccompiler.backend.arminstructions;

public class PreIndexAddr implements Address {

  private final Register rn;

  public PreIndexAddr(Register rn) {
    this.rn = rn;
  }

  @Override
  public String toString() {
    return "[" + rn.toString() + "]";
  }

}
