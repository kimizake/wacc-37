package group37.compiler;

import static java.lang.System.exit;

import group37.compiler.frontend.ErrorLogger;
import group37.compiler.frontend.Lex;
import group37.compiler.frontend.Parse;
import group37.compiler.wacccompiler.WaccCompiler;
import group37.compiler.wacccompiler.ast.WaccAstNode;
import group37.compiler.wacccompiler.backend.CodeGenAstVisitor;
import group37.compiler.wacccompiler.backend.arminstructions.ArmProgram;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CompilerMain {

  public static void main(String[] args) throws IOException {
    String filename = Paths.get(args[0]).getFileName().toString();
    filename = filename.substring(0, filename.length() - 5);
    Compiler compiler = new WaccCompiler();
    String code = new String(
        Files.readAllBytes(Paths.get(args[0]))
    );
    ErrorLogger.addCode(code);
    Lex lexer = compiler.performLex(code);
    if (lexer == null) {
      ErrorLogger.printMessages();
      exit(100);
    }
    Parse parser = compiler.parse(lexer);
    if (parser == null) {
      ErrorLogger.printMessages();
      exit(100);
    }
    //this is where the semantic checker would be... IF I HAD ONE!!

    CodeGenAstVisitor codeGenAstVisitor = new CodeGenAstVisitor();
    codeGenAstVisitor.visit((WaccAstNode) parser.getParse());
    ArmProgram armProgram = new ArmProgram(
        codeGenAstVisitor.getInstructions(),
        codeGenAstVisitor.getDataSegment(),
        codeGenAstVisitor.getUsedLibFunctions());
    armProgram.generateFile(filename + ".s");
  }

}
