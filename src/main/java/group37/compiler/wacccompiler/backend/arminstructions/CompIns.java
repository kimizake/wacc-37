package group37.compiler.wacccompiler.backend.arminstructions;

import java.security.InvalidParameterException;

public class CompIns extends ArmIns {

  private final Register rn;
  private final DataInsOpcode opcode;
  private final Op2 op2;

  private String extention = "";

  public CompIns(DataInsOpcode opcode, Register rn, Op2 op2) {
    if (opcode == DataInsOpcode.CMP ||
        opcode == DataInsOpcode.CMN ||
        opcode == DataInsOpcode.TEQ ||
        opcode == DataInsOpcode.TST) {
      this.rn = rn;
      this.opcode = opcode;
      this.op2 = op2;
    } else {
      throw new InvalidParameterException();
    }
  }

  public CompIns withExtention(String extention) {
    this.extention = extention;
    return this;
  }

  @Override
  public String toString() {
    String prefix = op2 instanceof Expression ? "#" : "";
    return opcode.toString() + " " + rn.toString() + ", " + prefix
        + op2.toString() + extention;
  }
}
