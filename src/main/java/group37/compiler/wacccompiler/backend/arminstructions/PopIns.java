package group37.compiler.wacccompiler.backend.arminstructions;

public class PopIns extends ArmIns {

  private Register register;

  public PopIns(Register register) {
    this.register = register;
  }

  @Override
  public String toString() {
    return "POP {" + register.toString() + "}";
  }
}
