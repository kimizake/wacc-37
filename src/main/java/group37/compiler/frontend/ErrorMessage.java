package group37.compiler.frontend;

public class ErrorMessage {

  public enum ErrorType {
    SYNTAX,
    SEMANTIC
  }

  private int lineNumber;
  private int columnNumber;
  private ErrorType errorType;
  private String message;

  public ErrorMessage(int lineNumber, int columnNumber, ErrorType errorType, String message) {
    this.lineNumber = lineNumber;
    this.columnNumber = columnNumber;
    this.errorType = errorType;
    this.message = message;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(int lineNumber) {
    this.lineNumber = lineNumber;
  }

  public int getColumnNumber() {
    return columnNumber;
  }

  public void setColumnNumber(int columnNumber) {
    this.columnNumber = columnNumber;
  }

  public ErrorType getErrorType() {
    return errorType;
  }

  public void setErrorType(ErrorType errorType) {
    this.errorType = errorType;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
