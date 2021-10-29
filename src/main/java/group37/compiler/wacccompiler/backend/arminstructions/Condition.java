package group37.compiler.wacccompiler.backend.arminstructions;

public enum Condition {
  EQ,
  NE,
  CS,
  CC,
  MI,
  PL,
  VS,
  VC,
  HI,
  LS,
  GE,
  LT,
  GT,
  LE,
  AL {
    @Override
    public String toString() {
      return "";
    }
  }
}
