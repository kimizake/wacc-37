package group37.compiler.wacccompiler.backend.arminstructions;

public class MulIns extends ArmIns {

  private final Register rd;
  private final Register rm;
  private final Register rs;

  public MulIns(Register rd, Register rm, Register rs) {
    this.rd = rd;
    this.rs = rs;
    this.rm = rm;
  }

  @Override
  public String toString() {
    String codes = setCodes ? "S" : "";
    return "MUL" + codes + " " + rd.toString() + ", " + rm.toString() + ", " + rs.toString();
  }

  private boolean setCodes;
  public MulIns withSetCodes(boolean setCodes) {
    this.setCodes = setCodes;
    return this;
  }
}
