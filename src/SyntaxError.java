public class SyntaxError extends Exception {
    public SyntaxError(int line){
        super("Syntaxfel på rad " + line);
    }
}
