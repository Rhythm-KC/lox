package com.rhythm.lox.Utils;

import com.rhythm.lox.Scanner.Token;
import com.rhythm.lox.Scanner.TokenType;

public class LoxError {
    private static Boolean hadError = false;

    public static void error(int line, String message) {
        report(line, "", message);
    }

    public static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
          report(token.line, " at end", message);
        } else {
          report(token.line, " at '" + token.lexeme + "'", message);
        }
    }
    
    private static void report(int line, String where,
                               String message) {
      System.err.println(
          "[line " + line + "] Error" + where + ": " + message);
        LoxError.hadError = true;
    }

    public static void setToNoError(){
        LoxError.hadError = false;
    }

    public static Boolean hasError(){
        return LoxError.hadError;
    }

}
