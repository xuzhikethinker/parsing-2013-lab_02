package tools;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

public class LexicalAnalyzer {
    final private static String EXCEPTION_FORMAT_STRING = "Illegal character '%c' at position";

    private ParseException makeParseException() {
        return new ParseException(String.format(EXCEPTION_FORMAT_STRING, (int) currentCharacter), currentPosition);
    }

    private InputStream inputStream;
    private int currentCharacter;
    private int currentPosition;
    private Token currentToken;

    public LexicalAnalyzer(InputStream inputStream) throws ParseException {
        this.inputStream = inputStream;
        currentPosition = 0;
        nextCharacter();
    }

    private void nextCharacter() throws ParseException {
        currentPosition++;

        try {
            currentCharacter = inputStream.read();
        } catch (IOException e) {
            throw new ParseException(e.getMessage(), currentPosition);
        }
    }

    private boolean isBlank(int character) {
        return character == ' ';
        //return character == ' ' || character == '\n' || character == '\t' || character == '\r';
    }

    private boolean isLetter(int character) {
        return (character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z');
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void nextToken() throws ParseException {
        while (isBlank(currentCharacter)) {
            nextCharacter();
        }

        if (isLetter(currentCharacter)) {
            nextCharacter();
            currentToken = Token.VARIABLE;
            return;
        }

        switch (currentCharacter) {
            case '(':
                nextCharacter();
                currentToken = Token.LEFT_PARENTHESIS;
                break;
            case ')':
                nextCharacter();
                currentToken = Token.RIGHT_PARENTHESIS;
                break;
            case '|':
                nextCharacter();
                currentToken = Token.OR_OPERATOR;
                break;
            case '^':
                nextCharacter();
                currentToken = Token.XOR_OPERATOR;
                break;
            case '&':
                nextCharacter();
                currentToken = Token.AND_OPERATOR;
                break;
            case '!':
                nextCharacter();
                currentToken = Token.NOT_OPERATOR;
                break;
            case -1:
                currentToken = Token.END;
                break;
            default:
                throw makeParseException();
        }
    }
}
