import org.junit.Rule;
import org.junit.rules.Timeout;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class TestErrorCode {

  @Rule
  public Timeout testTimeout =  Timeout.seconds(10);

  public static void testErrorCode(String test_file_name, int code) {
    try {
      System.out.println("testing " + test_file_name);
      Process p = Runtime.getRuntime().exec("./compile " + test_file_name);
      assertThat(p.waitFor(), is(code));
    } catch (IOException | InterruptedException e) {
      fail("IO or Interrupt Exception while running compiler");
    }
  }


  public static void testAllFolder(String path, int code) {
    File dir = new File(path);
    File[] allFiles = dir.listFiles();
    if (allFiles != null) {
      for (File testFile : allFiles) {
        if (testFile.isDirectory())
          continue;
        if (testFile.getPath().endsWith(".wacc"))
          testErrorCode(testFile.getPath(), code);
      }
    }
  }

}
