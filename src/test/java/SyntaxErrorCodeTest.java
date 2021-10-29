import org.junit.Test;

public class SyntaxErrorCodeTest {


  @Test
  public void checkArraySyntaxErrors() {
    TestErrorCode.testAllFolder("src/test/java/invalid/syntaxErr/array", 100);
  }

  @Test
  public void checkBasicSyntaxErrors() {
    TestErrorCode.testAllFolder("src/test/java/invalid/syntaxErr/basic", 100);
  }

  @Test
  public void checkExpressionsSyntaxErrors() {
    TestErrorCode.testAllFolder("src/test/java/invalid/syntaxErr/expressions", 100);
  }


  public void checkFunctionSyntaxErrors() {
    TestErrorCode.testAllFolder("src/test/java/invalid/syntaxErr/function", 100);
  }

  @Test
  public void checkIfSyntaxErrors() {
    TestErrorCode.testAllFolder("src/test/java/invalid/syntaxErr/if", 100);
  }

  @Test
  public void checkPairsSyntaxErrors() {
    TestErrorCode.testAllFolder("src/test/java/invalid/syntaxErr/pairs", 100);
  }

  @Test
  public void checkSequenceSyntaxErrors() {
    TestErrorCode.testAllFolder("src/test/java/invalid/syntaxErr/sequence", 100);
  }

  @Test
  public void checkVariablesSyntaxErrors() {
    TestErrorCode.testAllFolder("src/test/java/invalid/syntaxErr/variables", 100);
  }

  @Test
  public void checkWhileSyntaxErrors() {
    TestErrorCode.testAllFolder("src/test/java/invalid/syntaxErr/while", 100);
  }

}
