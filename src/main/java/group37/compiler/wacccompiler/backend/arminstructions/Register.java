package group37.compiler.wacccompiler.backend.arminstructions;

import java.util.ArrayDeque;
import java.util.Deque;

public enum Register implements Op2 {
  R0,
  R1,
  R2,
  R3,
  R4,
  R5,
  R6,
  R7,
  R8,
  SB,
  R10,
  R11,
  IP,
  SP,
  LR,
  PC;

  public static Deque<Register> getAllRegs() {
    Deque<Register> all = new ArrayDeque<>();
    for (Register r : Register.values()) {
      all.addLast(r);
    }
    return all;
  }

  public static Deque<Register> getGeneralRegs() {
    Deque<Register> all = new ArrayDeque<>();
    all.addFirst(R11);
    all.addFirst(R10);
    all.addFirst(R8);
    all.addFirst(R7);
    all.addFirst(R6);
    all.addFirst(R5);
    all.addFirst(R4);
    return all;
  }
}
