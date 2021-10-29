package group37.compiler.wacccompiler.backend.arminstructions;

public class BranchLinkIns extends BranchIns {

  public BranchLinkIns(Expression offset) {
    super(offset);
  }

  public BranchLinkIns(Condition condition, Expression offset) {
    super(condition, offset);
  }

  @Override
  public String toString() {
    return "BL" + condition.toString() + " " + offset.toString();
  }
}
