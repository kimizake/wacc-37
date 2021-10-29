package group37.compiler.wacccompiler.backend.arminstructions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArmProgram {

  private final List<ArmIns> instructions;
  private final List<Data> dataSegment;

  public ArmProgram(List<ArmIns> instructions, List<Data> dataSegment,
      Set<LibFunction> libFunctions) {
    this.dataSegment = dataSegment;
    this.instructions = instructions;
    this.libFunctionSet = libFunctions;
  }

  /*
  translate the arm instructions and data segment to string and write them
  to the file given by FILEPATH
   */
  public boolean generateFile(String filepath) {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
      addLibFunctionsToProgram();
      if (dataSegment.size() > 0) {
        writer.write(".data\n\n");
        for (int i = 0; i < dataSegment.size(); i++) {
          writer.write("msg_" + i + ":\n");
          writer.write(dataSegment.get(i).toString() + "\n");
        }
        writer.write("\n\n");
      }
      writer.write(".text\n\n.global main\n");
      for (ArmIns i : instructions) {
        String prefix =
            i instanceof DefineLabel || i instanceof StringIns ? "" : "\t";
        writer.write(prefix + i.toString() + "\n");
      }
      writer.close();
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  private void addInstruction(ArmIns instruction) {
    instructions.add(instruction);
  }

  private void addData(Data d) {
    dataSegment.add(d);
  }

  private int getDataSize() {
    return dataSegment.size();
  }

  //set of all the library functions used
  private final Set<LibFunction> libFunctionSet;

  //calculate, with dependencies and all all library functions to instruction list
  //also adds needed data such as format string in the right place
  private void addLibFunctionsToProgram() {
    Set<LibFunction> toAdd = new HashSet<>();
    for (LibFunction f : libFunctionSet) {
      toAdd.add(f);
      if (f.getDependencies() != null) {
        toAdd.addAll(f.getDependencies());
      }
    }
    for (LibFunction f : toAdd) {
      f.addToProgram(this);
    }
  }

  //enum for storing lib functions with add method and dependency gets
  public enum LibFunction {
    PRINT_STRING {
      @Override
      public Set<LibFunction> getDependencies() {
        return null;
      }

      @Override
      public void addToProgram(ArmProgram program) {
        int msg = program.getDataSize();
        program.addData(new Data("\"%.*s\\0\""));
        program.addInstruction(new StringIns(PRINT_STRING.toString() + ":\n"
            + "\tPUSH {lr}\n"
            + "\tLDR r1, [r0]\n"
            + "\tADD r2, r0, #4\n"
            + "\tLDR r0, =msg_" + msg + "\n"
            + "\tADD r0, r0, #4\n"
            + "\tBL printf\n"
            + "\tMOV r0, #0\n"
            + "\tBL fflush\n"
            + "\tPOP {pc}"));
      }
    },
    PRINT_REFERENCE {
      @Override
      public Set<LibFunction> getDependencies() {
        return null;
      }

      @Override
      public void addToProgram(ArmProgram program) {
        int msg = program.getDataSize();
        program.addData(new Data("\"%p\\0\""));
        program.addInstruction(new StringIns(PRINT_REFERENCE.toString() + ":\n"
            + "\tPUSH {lr}\n"
            + "\tMOV r1, r0\n"
            + "\tLDR r0, =msg_" + msg + "\n"
            + "\tADD r0, r0, #4\n"
            + "\tBL printf\n"
            + "\tMOV r0, #0\n"
            + "\tBL fflush\n"
            + "\tPOP {pc}"));
      }
    },
    PRINT_LN {
      @Override
      public Set<LibFunction> getDependencies() {
        return null;
      }

      @Override
      public void addToProgram(ArmProgram program) {
        int msg = program.getDataSize();
        program.addData(new Data("\"\\0\""));
        program.addInstruction(new StringIns(PRINT_LN.toString() + ":\n"
            + "\tPUSH {lr}\n"
            + "\tLDR r0, =msg_" + msg + "\n"
            + "\tADD r0, r0, #4\n"
            + "\tBL puts\n"
            + "\tMOV r0, #0\n"
            + "\tBL fflush\n"
            + "\tPOP {pc}"));
      }
    },
    PRINT_CHAR_ARRAY {
      @Override
      public Set<LibFunction> getDependencies() {
        return null;
      }

      @Override
      public void addToProgram(ArmProgram program) {
        program.addInstruction(new StringIns("PRINT_CHAR_ARRAY:\n"
            + "\tPUSH {LR}\n"
            + "\tLDR R4, =8\n"
            + "\tSUB SP, SP, R4\n"
            + "\tLDR R4, [SP, #12]\n"
            + "\tLDR R4, [R4]\n"
            + "\tSTR R4, [SP, #4]\n"
            + "\tLDR R4, =0\n"
            + "\tSTR R4, [SP]\n"
            + "PCA_LOOP:\n"
            + "\tLDR R4, [SP]\n"
            + "\tLDR R5, [SP, #12]\n"
            + "\tLDR R5, [R5]\n"
            + "\tCMP R4, R5\n"
            + "\tMOVLT R4, #1\n"
            + "\tMOVGE R4, #0\n"
            + "\tCMP R4, #0\n"
            + "\tBEQ PCA_LOOP_1\n"
            + "\tLDR R4, [SP, #12]\n"
            + "\tLDR R5, [SP]\n"
            + "\tADD R5, R5, #1\n"
            + "\tLDR R6, =4\n"
            + "\tMUL R5, R5, R6\n"
            + "\tADD R4, R4, R5\n"
            + "\tLDR R4, [R4]\n"
            + "\tMOV R0, R4\n"
            + "\tBL putchar\n"
            + "\tLDR R4, [SP]\n"
            + "\tLDR R5, =1\n"
            + "\tADD R4, R4, R5\n"
            + "\tSTR R4, [SP]\n"
            + "\tB PCA_LOOP\n"
            + "PCA_LOOP_1:\n"
            + "\tLDR R4, =8\n"
            + "\tADD SP, SP, R4\n"
            + "\tPOP {PC}\n"
            + "\t.ltorg"));
      }
    },
    PUT_CHAR {
      @Override
      public Set<LibFunction> getDependencies() {
        return null;
      }

      @Override
      public String toString() {
        return "putchar";
      }

      @Override
      public void addToProgram(ArmProgram program) {
        //Nothing to do as it is a c function!
      }
    },
    PRINT_BOOL {
      @Override
      public Set<LibFunction> getDependencies() {
        return null;
      }

      public void addToProgram(ArmProgram program) {
        int msg = program.getDataSize();
        program.addData(new Data("\"true\\0\""));
        program.addData(new Data("\"false\\0\""));
        program.addInstruction(new StringIns(PRINT_BOOL.toString() + ":\n"
            + "\tPUSH {lr}\n"
            + "\tCMP r0, #0\n"
            + "\tLDRNE r0, =msg_" + msg + "\n"
            + "\tLDREQ r0, =msg_" + (msg + 1) + "\n"
            + "\tADD r0, r0, #4\n"
            + "\tBL printf\n"
            + "\tMOV r0, #0\n"
            + "\tBL fflush\n"
            + "\tPOP {pc}"));
      }
    },
    PRINT_INT {
      @Override
      public Set<LibFunction> getDependencies() {
        return null;
      }

      public void addToProgram(ArmProgram program) {
        int msg = program.getDataSize();
        program.addData(new Data("\"%d\\0\""));
        program.addInstruction(new StringIns(PRINT_INT.toString() + ":\n"
            + "\tPUSH {lr}\n"
            + "\tMOV r1, r0\n"
            + "\tLDR r0, =msg_" + msg + "\n"
            + "\tADD r0, r0, #4\n"
            + "\tBL printf\n"
            + "\tMOV r0, #0\n"
            + "\tBL fflush\n"
            + "\tPOP {pc}"));
      }
    },
    READ_INT {
      @Override
      public Set<LibFunction> getDependencies() {
        return null;
      }

      @Override
      public void addToProgram(ArmProgram program) {
        int msg = program.getDataSize();
        program.addData(new Data("\"%d\\0\""));
        program.addInstruction(new StringIns(READ_INT.toString() + ":\n"
            + "\tPUSH {lr}\n"
            + "\tMOV r1, r0\n"
            + "\tLDR r0, =msg_" + msg + "\n"
            + "\tADD r0, r0, #4\n"
            + "\tBL scanf\n"
            + "\tPOP {pc}"));
      }
    },
    READ_CHAR {
      @Override
      public Set<LibFunction> getDependencies() {
        return null;
      }

      @Override
      public void addToProgram(ArmProgram program) {
        int msg = program.getDataSize();
        program.addData(new Data("\" %c\\0\""));
        program.addInstruction(new StringIns(READ_CHAR.toString()
            + ":\n"
            + "\tPUSH {lr}\n"
            + "\tMOV r1, r0\n"
            + "\tLDR r0, =msg_" + msg + "\n"
            + "\tADD r0, r0, #4\n"
            + "\tBL scanf\n"
            + "\tPOP {pc}"));
      }
    },
    THROW_RUNTIME_ERROR {
      @Override
      public Set<LibFunction> getDependencies() {
        Set<LibFunction> out = new HashSet<>();
        out.add(PRINT_STRING);
        if (PRINT_STRING.getDependencies() != null) {
          out.addAll(PRINT_STRING.getDependencies());
        }
        return out;
      }

      @Override
      public void addToProgram(ArmProgram program) {
        program.addInstruction(new StringIns(THROW_RUNTIME_ERROR.toString()
            + ":\n"
            + "\tBL " + PRINT_STRING.toString() + "\n"
            + "\tMOV r0, #-1\n"
            + "\tBL exit"));
      }
    },
    CHECK_DIV_ZERO {
      @Override
      public Set<LibFunction> getDependencies() {
        Set<LibFunction> out = new HashSet<>();
        out.add(THROW_RUNTIME_ERROR);
        if (THROW_RUNTIME_ERROR.getDependencies() != null) {
          out.addAll(THROW_RUNTIME_ERROR.getDependencies());
        }
        return out;
      }

      @Override
      public void addToProgram(ArmProgram program) {
        int msg = program.getDataSize();
        program.addData(new Data("\"DivideByZeroError: "
            + "divide or modulo by zero\\n\\0\""));
        program.addInstruction(new StringIns(CHECK_DIV_ZERO.toString()
            + ":\n"
            + "\tPUSH {lr}\n"
            + "\tCMP r1, #0\n"
            + "\tLDREQ r0, =msg_" + msg + "\n"
            + "\tBLEQ " + THROW_RUNTIME_ERROR.toString() + "\n"
            + "\tPOP {pc}"));
      }
    },
    CHECK_ARRAY_BOUNDS {
      @Override
      public Set<LibFunction> getDependencies() {
        Set<LibFunction> out = new HashSet<>();
        out.add(PRINT_STRING);
        if (PRINT_STRING.getDependencies() != null) {
          out.addAll(PRINT_STRING.getDependencies());
        }
        out.add(THROW_RUNTIME_ERROR);
        if (THROW_RUNTIME_ERROR.getDependencies() != null) {
          out.addAll(THROW_RUNTIME_ERROR.getDependencies());
        }
        return out;
      }

      @Override
      public void addToProgram(ArmProgram program) {
        int msg1 = program.getDataSize();

        program.addData(new Data("\"ArrayIndexOutOfBoundsError: "
            + "negative index\\n\\0\""));

        int msg2 = program.getDataSize();

        program.addData(new Data("\"ArrayIndexOutOfBoundsError: "
            + "index too large\\n\\0\""));

        program.addInstruction(new StringIns(CHECK_ARRAY_BOUNDS.toString()
            + ":\n"
            + "\tPUSH {lr}\n"
            + "\tCMP r0, #0\n"
            + "\tLDRLT r0, =msg_" + msg1 + "\n"
            + "\tBLLT " + THROW_RUNTIME_ERROR.toString() + "\n"
            + "\tLDR r1, [r1]\n"
            + "\tCMP r0, r1\n"
            + "\tLDRCS r0, =msg_" + msg2 + "\n"
            + "\tBLCS " + THROW_RUNTIME_ERROR.toString() + "\n"

            + "\tPOP {pc}"));

      }

    },
    THROW_OVERFLOW_ERROR {
      @Override
      public Set<LibFunction> getDependencies() {
        Set<LibFunction> out = new HashSet<>();
        out.add(THROW_RUNTIME_ERROR);
        if (THROW_RUNTIME_ERROR.getDependencies() != null) {
          out.addAll(THROW_RUNTIME_ERROR.getDependencies());
        }
        return out;
      }

      @Override
      public void addToProgram(ArmProgram program) {
        int msg = program.getDataSize();
        program.addData(new Data("\"OverflowError: the result is too "
            + "small/large to store in a 4-byte signed-integer.\\n\""));
        program.addInstruction(new StringIns(THROW_OVERFLOW_ERROR.toString()
            + ":\n"
            + "\tLDR r0, =msg_" + msg + "\n"
            + "\tBL " + THROW_RUNTIME_ERROR.toString()));
      }
    },
    FREE_PAIR {
      @Override
      public Set<LibFunction> getDependencies() {
        Set<LibFunction> out = new HashSet<>();
        out.add(THROW_RUNTIME_ERROR);
        if (THROW_RUNTIME_ERROR.getDependencies() != null) {
          out.addAll(THROW_RUNTIME_ERROR.getDependencies());
        }
        return out;
      }

      @Override
      public void addToProgram(ArmProgram program) {
        int msg = program.getDataSize();
        program.addData(new Data("\"NullReferenceError: dereference"
            + " a null reference\\n\\0\""));
        program.addInstruction(new StringIns(FREE_PAIR.toString()
            + ":\n"
            + "\tPUSH {lr}\n"
            + "\tPUSH {R0}\n"
            + "\tCMP r0, #0\n"
            + "\tLDREQ r0, =msg_" + msg + "\n"
            + "\tBEQ " + THROW_RUNTIME_ERROR.toString() + "\n"
            + "\tPOP {R0}\n"
            + "\tBL free\n"
            + "\tPOP {pc}"));
      }
    },
    CHECK_NULL {
      @Override
      public Set<LibFunction> getDependencies() {
        Set<LibFunction> out = new HashSet<>();
        out.add(THROW_RUNTIME_ERROR);
        if (THROW_RUNTIME_ERROR.getDependencies() != null) {
          out.addAll(THROW_RUNTIME_ERROR.getDependencies());
        }
        return out;
      }

      @Override
      public void addToProgram(ArmProgram program) {
        int msg = program.getDataSize();
        program.addData(new Data("\"NullReferenceError: dereference"
            + " a null reference\\n\\0\""));
        program.addInstruction(new StringIns(CHECK_NULL.toString()
            + ":\n"
            + "\tPUSH {lr}\n"
            + "\tCMP r0, #0\n"
            + "\tLDREQ r0, =msg_" + msg + "\n"
            + "\tBLEQ " + THROW_RUNTIME_ERROR.toString() + "\n"
            + "\tPOP {pc}"));
      }
    };

    public abstract void addToProgram(ArmProgram program);

    public abstract Set<LibFunction> getDependencies();
  }

}
