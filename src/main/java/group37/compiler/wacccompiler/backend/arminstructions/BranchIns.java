package group37.compiler.wacccompiler.backend.arminstructions;

public class BranchIns extends ArmIns {

  protected final Expression offset;

  public BranchIns(Expression offset) {
    this.condition = Condition.AL;
    this.offset = offset;
  }

  public BranchIns(Condition condition, Expression offset) {
    this.condition = condition;
    this.offset = offset;
  }

  @Override
  public String toString() {
      return "B" + condition.toString() + " " + offset.toString();
  }

}
