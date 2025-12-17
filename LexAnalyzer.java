package LexAnalyzer;
/* LexAnalyzer.java - a lexical analyzer system for simple arithmetic expressions */
import java.io.*;

public class LexAnalyzer {
    /* Global declarations */
    /* Variables */
    static int charClass;
    static char[] lexeme = new char[100];
    static char nextChar;
    static int lexLen;
    static int token;
    static int nextToken;
    static FileReader fileReader;
    static int nextByte; // To hold the integer value from read()

    /* Character classes */
    static final int LETTER = 0;
    static final int DIGIT = 1;
    static final int UNKNOWN = 99;
    static final int EOF_CLASS = -1;

    /* Token codes */
    static final int INT_LIT = 10;
    static final int IDENT = 11;
    static final int ASSIGN_OP = 20;
    static final int ADD_OP = 21;
    static final int SUB_OP = 22;
    static final int MULT_OP = 23;
    static final int DIV_OP = 24;
    static final int LEFT_PAREN = 25;
    static final int RIGHT_PAREN = 26;
    static final int EOF = -1;

    /******************************************************/
    /* main driver */
    public static void main(String[] args) {
        try {
            File file = new File("C:\\Users\\moham\\eclipse-workspace\\LexAnalyzer\\src\\LexAnalyzer\\input.txt");
            if (!file.exists()) {
                System.out.println("ERROR - cannot open input.txt");
                return;
            }
            fileReader = new FileReader(file);
            getChar();
            do {
                lex();
            } while (nextToken != EOF);
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /******************************************************/
    /* lookup - a function to lookup operators and parentheses
       and return the token */
    static int lookup(char ch) {
        switch (ch) {
            case '(':
                addChar();
                nextToken = LEFT_PAREN;
                break;
            case ')':
                addChar();
                nextToken = RIGHT_PAREN;
                break;
            case '+':
                addChar();
                nextToken = ADD_OP;
                break;
            case '-':
                addChar();
                nextToken = SUB_OP;
                break;
            case '*':
                addChar();
                nextToken = MULT_OP;
                break;
            case '/':
                addChar();
                nextToken = DIV_OP;
                break;
            default:
                addChar();
                nextToken = EOF;
                break;
        }
        return nextToken;
    }

    /******************************************************/
    /* addChar - a function to add nextChar to lexeme */
    static void addChar() {
        if (lexLen <= 98) {
            lexeme[lexLen++] = nextChar;
            // In Java char array, we don't strictly need null terminator for strings constructed from it,
            // but we maintain the logic. The String construction will use lexLen.
        } else {
            System.out.println("Error - lexeme is too long");
        }
    }

    /******************************************************/
    /* getChar - a function to get the next character of
       input and determine its character class */
    static void getChar() {
        try {
            nextByte = fileReader.read();
            if (nextByte != -1) {
                nextChar = (char) nextByte;
                if (Character.isLetter(nextChar))
                    charClass = LETTER;
                else if (Character.isDigit(nextChar))
                    charClass = DIGIT;
                else
                    charClass = UNKNOWN;
            } else {
                charClass = EOF_CLASS;
                nextChar = 0; // null char
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /******************************************************/
    /* getNonBlank - a function to call getChar until it
       returns a non-whitespace character */
    static void getNonBlank() {
        while (Character.isWhitespace(nextChar) && charClass != EOF_CLASS) {
            getChar();
        }
    }

    /******************************************************/
    /* lex - a simple lexical analyzer for arithmetic
       expressions */
    static int lex() {
        lexLen = 0;
        getNonBlank();
        
        // Check for EOF immediately after getNonBlank
        if (charClass == EOF_CLASS) {
             nextToken = EOF;
             String lexemeStr = "EOF";
             System.out.println("Next token is: " + nextToken + ", Next lexeme is " + lexemeStr);
             return nextToken;
        }

        switch (charClass) {
            /* Parse identifiers */
            case LETTER:
                addChar();
                getChar();
                while (charClass == LETTER || charClass == DIGIT) {
                    addChar();
                    getChar();
                }
                nextToken = IDENT;
                break;

            /* Parse integer literals */
            case DIGIT:
                addChar();
                getChar();
                while (charClass == DIGIT) {
                    addChar();
                    getChar();
                }
                nextToken = INT_LIT;
                break;

            /* Parentheses and operators */
            case UNKNOWN:
                lookup(nextChar);
                getChar();
                break;

            /* EOF */
            case EOF_CLASS:
                nextToken = EOF;
                lexeme[0] = 'E';
                lexeme[1] = 'O';
                lexeme[2] = 'F';
                // Java handles string length differently
                break;
        } /* End of switch */

        String lexemeStr = new String(lexeme, 0, lexLen);
        if (nextToken != EOF) {
             System.out.println("Next token is: " + nextToken + ", Next lexeme is " + lexemeStr);
        }
        return nextToken;
    }
}

