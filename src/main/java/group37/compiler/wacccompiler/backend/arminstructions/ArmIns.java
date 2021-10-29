package group37.compiler.wacccompiler.backend.arminstructions;

public abstract class ArmIns {

  Condition condition = null;

  public ArmIns withCondition(Condition condition) {
    this.condition = condition;
    return this;
  }

}
