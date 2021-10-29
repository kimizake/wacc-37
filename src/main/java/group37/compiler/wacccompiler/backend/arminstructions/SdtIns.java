package group37.compiler.wacccompiler.backend.arminstructions;

public class SdtIns extends ArmIns {

  private final Register rd;
  private final SdtOpcode opcode;
  private final Address address;

  public SdtIns(SdtOpcode opcode, Register rd, Address address) {
    this.rd = rd;
    this.opcode = opcode;
    this.address = address;
  }

  @Override
  public String toString() {
    String prefix = address instanceof Expression ? "=" : "";
    return opcode.toString() + " " + rd.toString() + ", " + prefix + address.toString();
  }

}
