package com.rhythm.lox;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class ScannerTest{

    @Test
    void testScanTokenTestForSingleTokens(){
        var tokenString = "(){},.-+;*!<>/";

        var scanner = new Scanner(tokenString);
        var tokens = scanner.scanTokens();

        assertEquals(TokenType.LEFT_PAREN, tokens.get(0).getType());
        assertEquals(TokenType.RIGHT_PAREN, tokens.get(1).getType());
        assertEquals(TokenType.LEFT_BRACE, tokens.get(2).getType());
        assertEquals(TokenType.RIGHT_BRACE, tokens.get(3).getType());
        assertEquals(TokenType.COMMA, tokens.get(4).getType());
        assertEquals(TokenType.DOT, tokens.get(5).getType());
        assertEquals(TokenType.MINUS, tokens.get(6).getType());
        assertEquals(TokenType.PLUS, tokens.get(7).getType());
        assertEquals(TokenType.SEMICOLON, tokens.get(8).getType());
        assertEquals(TokenType.STAR, tokens.get(9).getType());
        assertEquals(TokenType.BANG, tokens.get(10).getType());
        assertEquals(TokenType.LESS, tokens.get(11).getType());
        assertEquals(TokenType.GREATER, tokens.get(12).getType());
        assertEquals(TokenType.SLASH, tokens.get(13).getType());
        return;
    }

    @Test
    void testScanTokensForMultiTokenOperators(){
        var tokenString = "<=>===!==";

        var scanner = new Scanner(tokenString);
        var tokens = scanner.scanTokens();
        assertEquals(TokenType.LESS_EQUAL, tokens.get(0).getType());
        assertEquals(TokenType.GREATER_EQUAL, tokens.get(1).getType());
        assertEquals(TokenType.EQUAL_EQUAL, tokens.get(2).getType());
        assertEquals(TokenType.BANG_EQUAL, tokens.get(3).getType());
        assertEquals(TokenType.EQUAL, tokens.get(4).getType());
    }

    @Test
    void testScanTokenForSingleLineComment(){
        var tokenString = "// this is a test comment\n";

        var scanner = new Scanner(tokenString);
        var tokens = scanner.scanTokens();

        assertEquals(1, tokens.size());
        assertEquals(TokenType.EOF, tokens.get(0).getType());
    }

    @Test 
    void testScanTokenForMultiLineComment(){
        var tokenString = "/* this is a test comment */";

        var scanner = new Scanner(tokenString);
        var tokens = scanner.scanTokens();
        assertEquals(1, tokens.size());
        assertEquals(TokenType.EOF, tokens.get(0).getType());
        
    }

    @Test
    void testScanTokenForMultiLineCommentWithMultipleLines(){
        var tokenString = "/* this is a test comment\n anotherline */";

        var scanner = new Scanner(tokenString);
        var tokens = scanner.scanTokens();
        assertEquals(1, tokens.size());
        assertEquals(TokenType.EOF, tokens.get(0).getType());
        
    }

    @Test
    void testMultiLineCommentUnterminatedError() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        Scanner scanner = new Scanner("/* This is a comment");

        scanner.scanTokens();

        String expectedMessage = "[line 1] Error: Unterminated MultiLine char";
        assertTrue(errContent.toString().contains(expectedMessage));
    }

    @Test
    void testScanTokenForString(){

        Scanner scanner = new Scanner("\"A test String Literal\"");
        var tokens = scanner.scanTokens();

        assertEquals(TokenType.STRING, tokens.get(0).getType());
        assertEquals("A test String Literal", tokens.get(0).getLiteral());
    }

    @Test
    void testScanTokenForStringWithUnterminatedString(){

        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        Scanner scanner = new Scanner("\" This is an Unterminated String");

        scanner.scanTokens();

        String expectedMessage = "[line 1] Error: Unterminated String";
        assertTrue(errContent.toString().contains(expectedMessage));
    }
    
    @Test
    void testScanTokenForNumber(){

        Scanner scanner = new Scanner("123");
        var tokens = scanner.scanTokens();

        assertEquals(TokenType.NUMBER, tokens.get(0).getType());
        assertEquals(123.0, tokens.get(0).getLiteral());
    }

    @Test
    void testScanTokenForNumberIsAFloat(){

        Scanner scanner = new Scanner("123.23");
        var tokens = scanner.scanTokens();

        assertEquals(TokenType.NUMBER, tokens.get(0).getType());
        assertEquals(123.23, tokens.get(0).getLiteral());
    }
    @Test
    void testScanTokenForNumberIsInvalid(){

        Scanner scanner = new Scanner("123.");
        var tokens = scanner.scanTokens();

        assertEquals(TokenType.NUMBER, tokens.get(0).getType());
        assertEquals(TokenType.DOT, tokens.get(1).getType());

    }

    @Test
    void testScanTokenWithKeywords() {
        // Input string with keywords
        String input = "and class else false for fun if nil or print return super this true var while";
        Scanner scanner = new Scanner(input);
        var tokens = scanner.scanTokens();
        // Test each keyword
        assertEquals(TokenType.AND,    tokens.get(0).getType());
        assertEquals(TokenType.CLASS,  tokens.get(1).getType());
        assertEquals(TokenType.ELSE,   tokens.get(2).getType());
        assertEquals(TokenType.FALSE,  tokens.get(3).getType());
        assertEquals(TokenType.FOR,    tokens.get(4).getType());
        assertEquals(TokenType.FUN,    tokens.get(5).getType());
        assertEquals(TokenType.IF,     tokens.get(6).getType());
        assertEquals(TokenType.NIL,    tokens.get(7).getType());
        assertEquals(TokenType.OR,     tokens.get(8).getType());
        assertEquals(TokenType.PRINT,  tokens.get(9).getType());
        assertEquals(TokenType.RETURN, tokens.get(10).getType());
        assertEquals(TokenType.SUPER,  tokens.get(11).getType());
        assertEquals(TokenType.THIS,   tokens.get(12).getType());
        assertEquals(TokenType.TRUE,   tokens.get(13).getType());
        assertEquals(TokenType.VAR,    tokens.get(14).getType());
        assertEquals(TokenType.WHILE,  tokens.get(15).getType());
    }

    @Test
    void testScanTokenWithKeywordsAndIdentifier() {
        // Input string with keywords
        String input = "return x";
        Scanner scanner = new Scanner(input);
        var tokens = scanner.scanTokens();

        assertEquals(TokenType.RETURN,    tokens.get(0).getType());
        assertEquals(TokenType.IDENTIFIER,  tokens.get(1).getType());
    }


}