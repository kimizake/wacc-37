package group37.compiler.wacccompiler.backend.arminstructions;

public class Data {

  private final String data;

  public Data(String data) {
    this.data = data;
  }

  private int countRealChars() {
    int cnt = 0;
    for (int i = 0; i < data.length(); i++, cnt++)
      if (data.charAt(i) == '\\') i++;
    return cnt;
  }

  @Override
  public String toString() {
    return "\t.word " + (countRealChars() - 2) + "\n" + "\t.ascii " + data;
  }
}
