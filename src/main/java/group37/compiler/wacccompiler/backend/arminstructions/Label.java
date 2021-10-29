package group37.compiler.wacccompiler.backend.arminstructions;

public class Label implements Expression {

  private String label;

  private static long labelNo;

  public Label() {
    this.label = "L" + labelNo;
    labelNo++;
  }

  public Label(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  @Override
  public String toString() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }
}
