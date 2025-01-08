package com.rhythm.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.rhythm.lox.Parser.Expr;
import com.rhythm.lox.Scanner.Scanner;
import com.rhythm.lox.Scanner.Token;
import com.rhythm.lox.Scanner.TokenType;
import com.rhythm.lox.Utils.LoxError;
import com.rhythm.lox.Parser.AstPrinter;
import com.rhythm.lox.Parser.Parser;;

public class Lox{
    static boolean hadError = false;
    public static void main(String[] args) throws IOException{

        if (args.length > 1){
            System.out.println("Usage: jlox [script]");
            System.exit(65);
        }
        else if (args.length ==1){
           runFile(args[0]);
        }
        else{
            runPrompt();
        }
    }
    
    private static void runFile(String path) throws IOException{
        System.out.println(path);
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        if (LoxError.hasError()){
            System.exit(65);
        }
        
    }

    private static void runPrompt() throws IOException {

        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) { 
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
            LoxError.setToNoError();
        }
    }
        
    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        for (Token token : tokens){
            System.out.println(token);
        }

            Parser parser = new Parser(tokens);
            Expr expression = parser.parse();

            if (hadError) return;

            System.out.println(new AstPrinter().print(expression));
    }
}
