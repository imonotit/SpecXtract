package specxtract;

public class Token {

    public enum Type {
        AT,          // @
        IDENT,       // any name or keyword: public, class, login, String ...
        STRING,      // "text"
        CHAR,        // 'a'
        NUMBER,      // 42
        LPAREN, RPAREN,        // ( )
        LBRACE, RBRACE,        // { }
        LBRACKET, RBRACKET,    // [ ]
        LT, GT,                // < >
        COMMA, SEMI, DOT, EQ, COLON,   // , ; . = :
        SYMBOL,      // any other single character: + - * ? &
        EOF          // end of file
    }

    public final Type type;   // what kind of piece it is
    public final String text; // the actual text (for STRING: without the quotes)
    public final int line;    // line number in the file

    public Token(Type type, String text, int line) {
        this.type = type;
        this.text = text;
        this.line = line;
    }

    @Override
    public String toString() {
        return text.isEmpty() ? type.toString() : type + "(" + text + ")";
    }
}
