package group37.compiler.wacccompiler.symboltable;

import group37.compiler.wacccompiler.ast.types.FunctionType;
import group37.compiler.wacccompiler.ast.types.TypeNode;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

  private Deque<Map<String, STEntry>> table;

  private FunctionType callingFunction;

  public SymbolTable() {
    this.table = new ArrayDeque<>();
  }

  public SymbolTable(Map<String, STEntry> scope) {
    this.table = new ArrayDeque<>();
    this.table.push(scope);
  }

  public boolean defined(String name) {
    for (Map m : table) {
      if (m.containsKey(name)) {
        return true;
      }
    }
    return false;
  }

  public TypeNode getType(String name) {
    for (Map<String, STEntry> m : table) {
      STEntry out = m.get(name);
      if (out != null) {
        return out.getTypeNode();
      }
    }
    return null;
  }

  private int stackPointer = 0;
  public int getOffset(String name) {
    for (Map<String, STEntry> m : table) {
      STEntry out = m.get(name);
      if (out != null) {
        return out.getOffset() - stackPointer;
      }
    }
    return 0;
  }

  private int localVariables = 0;
  public int getNewOffset() {
    localVariables++;
    int out = -localVariables * 4;
    return out;
  }

  public int getStackPointer() {
    return stackPointer;
  }

  public SymbolTable addEntry(String id, STEntry e) {
    table.peek().put(id, e);
    return this;
  }

  public void increment(int i) {
    stackPointer += 4 * i;
  }

  public void decrement() {
    stackPointer -= 4;
  }

  public void decrement(int i) {
    stackPointer -= 4 * i;
  }

  public boolean inCurScope(String id) {
    //we can use != to null here as there should never be any null entries in the table
    return table.peek().get(id) != null;
  }

  public Map<String, STEntry> getCurScope() {
    return table.peek();
  }

  public void enterScope() {
    table.push(new HashMap<>());
  }

  public void exitScope() {
    int todelete = table.peek().size();
    localVariables -= todelete;
    table.pop();
  }

  public void setCallingFunction(FunctionType f) {
    callingFunction = f;
  }

  public FunctionType getCallingFunction() {
    return callingFunction;
  }

  public void add(String name, STEntry stEntry) {
    table.peek().put(name, stEntry);
  }

}
