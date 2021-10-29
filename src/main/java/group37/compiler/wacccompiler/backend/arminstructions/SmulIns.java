package group37.compiler.wacccompiler.backend.arminstructions;

public class SmulIns extends ArmIns {

  Register r1, r2;

  public SmulIns(Register r1, Register r2) {
    this.r1 = r1;
    this.r2 = r2;
  }

  @Override
  public String toString() {
    return "SMULL " + r1.toString() + ", " + r2.toString()
        + ", " + r1.toString() + ", " + r2.toString();
  }
}
