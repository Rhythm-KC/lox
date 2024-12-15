package com.rhythm.lox.Utils;

public class LoxError {
    private static Boolean hadError = false;

    public static void error(int line, String message) {
        report(line, "", message);
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
