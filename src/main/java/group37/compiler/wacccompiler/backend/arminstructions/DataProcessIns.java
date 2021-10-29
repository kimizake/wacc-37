package group37.compiler.wacccompiler.backend.arminstructions;

import java.security.InvalidParameterException;

public class DataProcessIns extends ArmIns {

  // for AND,EOR,SUB,RSB,ADD,ADC,SBC,RSC,ORR,BIC

  private final Register rn;
  private final DataInsOpcode opcode;
  private final DataInsOpcode opcode2;
  private final Register rd;
  private final Op2 op2;
  private final Op2 op3;

  private void checkOpcode(DataInsOpcode opcode) throws InvalidParameterException {
    if (!(opcode == DataInsOpcode.AND ||
        opcode == DataInsOpcode.EOR ||
        opcode == DataInsOpcode.SUB ||
        opcode == DataInsOpcode.RSB ||
        opcode == DataInsOpcode.ADD ||
        opcode == DataInsOpcode.ADC ||
        opcode == DataInsOpcode.SBC ||
        opcode == DataInsOpcode.ORR ||
        opcode == DataInsOpcode.BIC ||
        opcode == DataInsOpcode.RSC)) {
      throw new InvalidParameterException();
    }
  }

  public DataProcessIns(DataInsOpcode opcode, Register rd, Register rn, Op2 op2) {
    checkOpcode(opcode);
    this.rn = rn;
    this.opcode = opcode;
    this.op2 = op2;
    this.rd = rd;
    this.op3 = null;
    this.opcode2 = null;
  }


  public DataProcessIns(DataInsOpcode opcode, Register rd, Register rn, Op2 op2, DataInsOpcode opcade2, Op2 op3) {
    checkOpcode(opcode);
    this.rn = rn;
    this.opcode = opcode;
    this.op2 = op2;
    this.rd = rd;
    this.opcode2 = opcade2;
    this.op3 = op3;
  }



  @Override
  public String toString() {
    String prefix = op2 instanceof Expression ? "#" : "";
    String setcodes = codes ? "S" : "";
    if(opcode2 != null){
      //ie with shift
      return opcode.toString() + setcodes + " " + rd.toString() + ", " + rn.toString() + ", " + prefix + op2.toString() +", " + opcode2.toString() + " #" + op3;
    }
    else {
      return opcode.toString() + setcodes + " " + rd.toString() + ", " + rn.toString() + ", " + prefix + op2.toString();
    }
  }

  private boolean codes;
    public DataProcessIns withSetCodes(boolean b) {
      codes = b;
      return this;
    }
}
