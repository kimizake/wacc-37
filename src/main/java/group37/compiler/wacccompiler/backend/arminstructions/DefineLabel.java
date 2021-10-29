package group37.compiler.wacccompiler.backend.arminstructions;

public class DefineLabel extends ArmIns {

  private final String label;

  public DefineLabel(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return label + ":";
  }
}
