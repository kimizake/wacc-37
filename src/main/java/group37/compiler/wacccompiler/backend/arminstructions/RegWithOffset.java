package group37.compiler.wacccompiler.backend.arminstructions;

public class RegWithOffset implements Address {

  private Register rn;
  private int offset;

  public RegWithOffset(Register rn, int offset) {
    this.rn = rn;
    this.offset = offset;
  }

  @Override
  public String toString() {
    String suffix = "]";
    if (offset > 0) {
      suffix = ", #" + offset + "]";
    } else if (offset < 0) {
      suffix = ", #" + offset + "]!";
    }
    return "[" + rn.toString() + suffix;
  }
}
