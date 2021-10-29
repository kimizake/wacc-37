import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class ExitCodeTest {

  private String base_path = "src/test/java/valid/";

  @Rule
  public Timeout testTimeout =  Timeout.seconds(10);

  public void testExitCode(String test_file_name, int code) {
    try {
      System.out.println("testing " + test_file_name);
      Process p = Runtime.getRuntime().exec("./compile " + test_file_name);
      assertThat(p.waitFor(), is(0));
      String[] split_path = test_file_name.split("/");
      String asm_name = split_path[split_path.length - 1].split("\\.")[0] + ".s";
      Process q = Runtime.getRuntime().exec("./emulate.sh " + asm_name);
      assertThat(q.waitFor(), is(code));
    } catch (IOException | InterruptedException e) {
      fail("IO or Interrupt Exception while running compiler or emulator");
    }
  }

  @Test
  public void checkBasicExit1ExitCode() {
    testExitCode(base_path + "basic/exit/exit-1.wacc", 255);
  }

  @Test
  public void checkBasicExitBasicExitCode() {
    testExitCode(base_path + "basic/exit/exitBasic.wacc", 7);
  }

  @Test
  public void checkBasicExitBasic2ExitCode() {
    testExitCode(base_path + "basic/exit/exitBasic2.wacc", 42);
  }

  @Test
  public void checkBasicExitWrapExitCode() {
    testExitCode(base_path + "basic/exit/exitWrap.wacc", 0);
  }


  @Test
  public void checkExpressionsLongExpr() {
    testExitCode(base_path + "expressions/longExpr.wacc", 153);
  }

  @Test
  public void checkExpressionsLongExpr2() {
    testExitCode(base_path + "expressions/longExpr2.wacc", 10);
  }

  @Test
  public void checkExpressionsLongExpr3() {
    testExitCode(base_path + "expressions/longExpr3.wacc", 9);
  }


  @Test
  public void checkExpressionsLongSplitExpr() {
    testExitCode(base_path + "expressions/longSplitExpr.wacc", 153);
  }

  @Test
  public void checkExpressionsLongSplitExpr2() {
    testExitCode(base_path + "expressions/longSplitExpr2.wacc", 128);
  }


  @Test
  public void checkArrayNegBounds() {
    testExitCode(base_path + "runtimeErr/arrayOutOfBounds/arrayNegBounds.wacc", 255);
  }

  @Test
  public void checkArrayOutOfBounds() {
    testExitCode(base_path + "runtimeErr/arrayOutOfBounds/arrayOutOfBounds.wacc", 255);
  }

  @Test
  public void checkArrayOutOfBoundsWithWrite() {
    testExitCode(base_path + "runtimeErr/arrayOutOfBounds/arrayOutOfBoundsWrite.wacc", 255);
  }

  @Test
  public void checkDivideByZero() {
    testExitCode(base_path + "runtimeErr/divideByZero/divideByZero.wacc", 255);
  }

  @Test
  public void checkDivZero() {
    testExitCode(base_path + "runtimeErr/divideByZero/divZero.wacc", 255);
  }

  @Test
  public void checkModByZero() {
    testExitCode(base_path + "runtimeErr/divideByZero/modByZero.wacc", 255);
  }

  @Test
  public void checkDoubleFree() {
    testExitCode(base_path + "runtimeErr/doubleFrees/doubleFree.wacc", 134);
  }

  @Test
  public void checkHiddenDoubleFree() {
    testExitCode(base_path + "runtimeErr/doubleFrees/hiddenDoubleFree.wacc", 134);
  }

  @Test
  public void checkIntJustOverflow() {
    testExitCode(base_path + "runtimeErr/integerOverflow/intJustOverflow.wacc", 255);
  }

  @Test
  public void checkIntMultOverflow() {
    testExitCode(base_path + "runtimeErr/integerOverflow/intmultOverflow.wacc", 255);
  }

  @Test
  public void checkIntNegateOverflow() {
    testExitCode(base_path + "runtimeErr/integerOverflow/intnegateOverflow.wacc", 255);
  }

  @Test
  public void checkIntNegateOverflow2() {
    testExitCode(base_path + "runtimeErr/integerOverflow/intnegateOverflow2.wacc", 255);
  }

  @Test
  public void checkIntNegateOverflow3() {
    testExitCode(base_path + "runtimeErr/integerOverflow/intnegateOverflow3.wacc", 255);
  }

  @Test
  public void checkIntNegateOverflow4() {
    testExitCode(base_path + "runtimeErr/integerOverflow/intnegateOverflow4.wacc", 255);
  }

  @Test
  public void checkIntUnderflow() {
    testExitCode(base_path + "runtimeErr/integerOverflow/intUnderflow.wacc", 255);
  }

  @Test
  public void checkIntWayOverflow() {
    testExitCode(base_path + "runtimeErr/integerOverflow/intWayOverflow.wacc", 255);
  }

  @Test
  public void checkFreeNull() {
    testExitCode(base_path + "runtimeErr/nullDereference/freeNull.wacc", 255);
  }

  @Test
  public void checkReadNull1() {
    testExitCode(base_path + "runtimeErr/nullDereference/readNull1.wacc", 255);
  }

  @Test
  public void checkReadNull2() {
    testExitCode(base_path + "runtimeErr/nullDereference/readNull2.wacc", 255);
  }

  @Test
  public void checkSetNull1() {
    testExitCode(base_path + "runtimeErr/nullDereference/setNull1.wacc", 255);
  }

  @Test
  public void checkSetNull2() {
    testExitCode(base_path + "runtimeErr/nullDereference/setNull2.wacc", 255);
  }

  @Test
  public void checkUseNull1() {
    testExitCode(base_path + "runtimeErr/nullDereference/useNull1.wacc", 255);
  }

  @Test
  public void checkUseNull2() {
    testExitCode(base_path + "runtimeErr/nullDereference/useNull2.wacc", 255);
  }


  
  @Test
  public void checkExitSimple() {
    testExitCode(base_path + "sequence/exitSimple.wacc", 42);
  }


  @Test
  public void checkIntAssignment() {
    testExitCode(base_path + "sequence/intAssignment.wacc", 20);
  }

  @Test
  public void checkVarNames() {
    testExitCode(base_path + "variables/_VarNames.wacc", 19);
  }

  @Test
  public void checkLongVarNames() {
    testExitCode(base_path + "variables/longVarNames.wacc", 5);
  }

  @Test
  public void checkScopeRedefine() {
    testExitCode(base_path + "scope/scopeSimpleRedefine.wacc", 12);
  }
}
