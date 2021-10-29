package group37.compiler.wacccompiler.backend.arminstructions;


public class SingOpDataIns extends ArmIns {

  private final Register rd;
  private final Op2 op2;
  private final DataInsOpcode op;

  public SingOpDataIns(DataInsOpcode op, Register rd, Op2 op2) {
    this.rd = rd;
    this.op2 = op2;
    this.op = op;
  }

  @Override
  public String toString() {
    String prefix = op2 instanceof Expression ? "#" : "";
    String c = condition != null ? condition.toString() : "";
    return op.toString() + c + " " + rd.toString() + ", " + prefix + op2.toString();
  }
}
