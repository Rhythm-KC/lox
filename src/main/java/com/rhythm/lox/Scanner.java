package com.rhythm.lox;

import static com.rhythm.lox.TokenType.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {
    private static final Map<String, TokenType> keywords;

  static {
    keywords = new HashMap<>();
    keywords.put("and",    AND);
    keywords.put("class",  CLASS);
    keywords.put("else",   ELSE);
    keywords.put("false",  FALSE);
    keywords.put("for",    FOR);
    keywords.put("fun",    FUN);
    keywords.put("if",     IF);
    keywords.put("nil",    NIL);
    keywords.put("or",     OR);
    keywords.put("print",  PRINT);
    keywords.put("return", RETURN);
    keywords.put("super",  SUPER);
    keywords.put("this",   THIS);
    keywords.put("true",   TRUE);
    keywords.put("var",    VAR);
    keywords.put("while",  WHILE);
  }
    


    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1; 

    Scanner (String source){
        this.source = source;
    }

    public List<Token> scanTokens(){

        while (!isEnd()){
            start = current;
            scanToken();
        }

        this.tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private Boolean isEnd(){
        return current >= source.length();
    }

    private void scanToken(){
        char c = advance();
        switch (c) {
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break; 
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            case '/':
                if (match('/')){
                    while (peek() != '\n' && !isEnd()) advance();
                }else if(match('*')){
                    multiLineComment();
                }
                else{
                    addToken(SLASH);
                }
            case ' ':
            case '\r':
            case '\t':
              // Ignore whitespace.
              break;
            case '\n':
              line++;
              break;
            
            case '"': string(); break;


            default:
                if (isDigit(c)){
                    number();
                }
                else if (isAlpha(c)){
                    identifier();
                }
                Lox.error(line, "Unexpected character.");
            break;
        }
    }

    private char advance(){
        return source.charAt(current++);
    }

    private void addToken(TokenType type){
        addToken(type, null);
    }
    private void addToken(TokenType type, Object literal){
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    private boolean match(char expectedLiteral){
        if (isEnd()) return false;    
        if (source.charAt(current) != expectedLiteral) return false;
        current ++;
        return true;
    }

    private char peek(){
        if (isEnd()) return '\0';
        return source.charAt(current);
    }

    private void string(){
        while (peek() != '"' && !isEnd()){
            if (peek() == '\n') line++;
            advance();
        }

        if (isEnd()){
            Lox.error(line, "Unterminated String");
            return;
        }

        var value = source.subSequence(start + 1, current -1);
        addToken(STRING, value);
    }

    private Boolean isDigit(char c){
        return c >='0' && c <= '9';
    }

    private void number(){
        while (isDigit(peek())) advance();
        if (peek() == '.' && isDigit(peekNext())){
            advance();
            while (isDigit(peek())) advance();
        }
        addToken(NUMBER, 
                 Double.parseDouble(source.substring(start, current)));
    }

    private char peekNext(){
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current +1);
    }

    private Boolean isAlpha(char c){
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
               (c == '_');
    }
    private Boolean isAlphaNumeric(char c){
        return isAlpha(c) || isDigit(c);
    }

    private void identifier(){
        while(isAlphaNumeric(peek())) advance();
        var ident = source.substring(start, current);
        var tokenType = keywords.get(ident);
        if (tokenType == null) tokenType = IDENTIFIER; 
        addToken(tokenType);
    }
    private void multiLineComment(){
        while (!match('*') && peek() != '/'){
            if (peek() == '\0'){
                Lox.error(line, "Unterminated MultiLine char");
                break;
            }
            if (peek() == '\n') line++;
            advance();
        }                    
    }

}
