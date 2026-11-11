package specxtract;

import java.util.ArrayList;
import java.util.List;

import specxtract.Token.Type;

public class Lexer {

    private final String src;                       // the whole file as text
    private int pos = 0;                            // the character we are looking at now
    private int line = 1;                           // current line number
    private final List<Token> tokens = new ArrayList<>();   // the result

    public Lexer(String src) {
        this.src = src;
    }

    public List<Token> tokenize() {
        while (pos < src.length()) {
            char c = src.charAt(pos);

            if (c == '\n') {                                    // new line: count it
                line++;
                pos++;
            } else if (Character.isWhitespace(c)) {             // space or tab: ignore
                pos++;
            } else if (c == '/' && nextChar() == '/') {         // // comment
                skipLineComment();
            } else if (c == '/' && nextChar() == '*') {         // /* comment */
                skipBlockComment();
            } else if (Character.isJavaIdentifierStart(c)) {    // starts with a letter, _ or $
                readWord();
            } else if (Character.isDigit(c)) {                  // starts with a digit
                readNumber();
            } else if (c == '"') {                              // "text"
                readString();
            } else if (c == '\'') {                             // 'a'
                readChar();
            } else {                                            // ( ) { } @ ; , ...
                readSymbol(c);
            }
        }
        tokens.add(new Token(Type.EOF, "", line));              // marks the end
        return tokens;
    }

    // ------------------------------------------------------------ helpers

    /** The character after the current one (or '\0' if there is none). */
    private char nextChar() {
        return pos + 1 < src.length() ? src.charAt(pos + 1) : '\0';
    }

    private void add(Type type, String text) {
        tokens.add(new Token(type, text, line));
    }

    // ------------------------------------------------------------ comments

    /** Skip from // to the end of the line. */
    private void skipLineComment() {
        while (pos < src.length() && src.charAt(pos) != '\n') pos++;
    }

    /** Skip from /* to the closing star-slash, still counting lines. */
    private void skipBlockComment() {
        pos += 2;
        while (pos < src.length() && !(src.charAt(pos) == '*' && nextChar() == '/')) {
            if (src.charAt(pos) == '\n') line++;
            pos++;
        }
        pos += 2;
    }

    // ------------------------------------------------------------ words, numbers

    /** A name or keyword: public, class, AuthController, login, String ... */
    private void readWord() {
        int start = pos;
        while (pos < src.length() && Character.isJavaIdentifierPart(src.charAt(pos))) pos++;
        add(Type.IDENT, src.substring(start, pos));
    }

    /** A number such as 42 or 3.14 */
    private void readNumber() {
        int start = pos;
        while (pos < src.length()
                && (Character.isLetterOrDigit(src.charAt(pos)) || src.charAt(pos) == '.')) pos++;
        add(Type.NUMBER, src.substring(start, pos));
    }

    // ------------------------------------------------------------ strings

    private void readString() {
        pos++;                                      // skip the opening "
        add(Type.STRING, readUntil('"'));
    }

    private void readChar() {
        pos++;                                      // skip the opening '
        add(Type.CHAR, readUntil('\''));
    }

    /** Reads characters until the closing quote. A backslash escapes the next character. */
    private String readUntil(char quote) {
        StringBuilder sb = new StringBuilder();
        while (pos < src.length()) {
            char c = src.charAt(pos);
            if (c == '\\' && pos + 1 < src.length()) {      // escape such as \" 
                sb.append(src.charAt(pos + 1));
                pos += 2;
            } else if (c == quote) {                        // closing quote: done
                pos++;
                break;
            } else if (c == '\n') {                         // missing closing quote: stop here
                break;
            } else {
                sb.append(c);
                pos++;
            }
        }
        return sb.toString();
    }

    // ------------------------------------------------------------ symbols

    /** One-character pieces: ( ) { } [ ] < > @ , ; . = : and everything else. */
    private void readSymbol(char c) {
        Type type;
        switch (c) {
            case '@': type = Type.AT;       break;
            case '(': type = Type.LPAREN;   break;
            case ')': type = Type.RPAREN;   break;
            case '{': type = Type.LBRACE;   break;
            case '}': type = Type.RBRACE;   break;
            case '[': type = Type.LBRACKET; break;
            case ']': type = Type.RBRACKET; break;
            case '<': type = Type.LT;       break;
            case '>': type = Type.GT;       break;
            case ',': type = Type.COMMA;    break;
            case ';': type = Type.SEMI;     break;
            case '.': type = Type.DOT;      break;
            case '=': type = Type.EQ;       break;
            case ':': type = Type.COLON;    break;
            default:  type = Type.SYMBOL;
        }
        add(type, String.valueOf(c));
        pos++;
    }
}
