import java.io.FileInputStream;
import java.io.InputStream;

public class Main {

    public static void main(String[] args) throws java.io.IOException{

        // fileread test
        InputStream input = new FileInputStream("sanherib-S2\\syntax\\src\\textsyntax.txt");
        //Lexer lexer = new Lexer(System.in); // kattis
        Lexer lexer = new Lexer(input);     // Split input in to tokens, and add each token to a list



        Leonardo L = new Leonardo();
        Parser parser = new Parser(lexer);
        try{
            ParseTree results = parser.parse();
            results.evaluate(L);
        } catch (SyntaxError error){
            System.out.println(error.getMessage());
        }
    }
}
