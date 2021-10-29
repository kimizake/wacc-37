package group37.compiler.wacccompiler.backend.arminstructions;

public class StringIns extends ArmIns {

  String ins;

  StringIns(String ins) {
    this.ins = ins;
  }
  @Override
  public String toString() {
    return ins;
  }

}
