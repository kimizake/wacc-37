package group37.compiler.wacccompiler.backend.arminstructions;

public class PushIns extends ArmIns {

  private Register register;

  public PushIns(Register register) {
    this.register = register;
  }

  @Override
  public String toString() {
    return "PUSH {" + register.toString() + "}";
  }
}
