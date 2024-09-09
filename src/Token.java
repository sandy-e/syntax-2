/**
 * The type of token.
 */
enum tokenType {
    FORW, BACK, LEFT, RIGHT, DOWN, UP, COLOR, REP, // commands
    PERIOD, QUOTE,
    DECIMAL,  // positive integer, integer for rep (followed by quote)
    HEX, // HEX color data
    ERROR, // everything else including syntax error
    ENDTOKEN   /// end of input token
}

class Token {

    private tokenType type;
    private int lineNumber;
    private Object data;

    // For tokens without data like "down"
    public Token(tokenType type, int lineNumber){
        this.type = type;
        this.lineNumber = lineNumber;
        this.data = null;
    }
    // For tokens with data associated with them like DECIMAL and HEX
    public Token(tokenType type, int lineNumber, Object data){
        this.type = type;
        this.lineNumber = lineNumber;
        this.data = data;
    }

    // for writing error messages (?)
    public String toString(){
        if (data == null){
            return type.toString();
        } else return type.toString() + "(" + data.toString() + ")";
    }

    public tokenType getType(){return type;}
    public int getLineNumber(){return lineNumber;}
    public Object getData(){return data;}
}
