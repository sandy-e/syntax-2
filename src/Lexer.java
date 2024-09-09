
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class Lexer {

    private List<Token> tokens;
    //private String input;
    private int currentToken;


    // Helper method for reading contents of input stream to a String
    private static String readInput(InputStream f) throws java.io.IOException {
        Reader stdin = new InputStreamReader(f);
        StringBuilder buf = new StringBuilder();
        char[] input = new char[1024];
        int read;
        while((read = stdin.read(input)) != -1){
            buf.append(input, 0 , read);
        }
        return buf.toString();
    }

    public Lexer(InputStream in) throws IOException{
        String input = Lexer.readInput(in);
        input = input.toUpperCase();
        // Regex for describing tokens with white space, which will be ignored


        Pattern tokenPattern = Pattern.compile(
                   //comment
                "%.*(\\n)" +         // comment
                "|(FORW|BACK|LEFT|RIGHT|COLOR|REP)(\\s|%.*\\n)" +  // all commands with space after
                "|DOWN|UP" +      //pen up and down
                "|\\." +   //period
                "|\"" +     // quote
                "|#[0-9A-F]{6}" +   // Hex value
                "|[1-9]\\d*([ \\t]|\\n|\\.|%.*\\n)" +  // positive integer with space/comment or period
                "|\\n" +     //newline to count lines
                "|[ \\t]+");  //rest of white space
        // replace \r cuz windows fucky
        input = input.replace("\r", "");

        Matcher m = tokenPattern.matcher(input);
        int inputPos = 0;
        tokens = new ArrayList<>();
        currentToken = 0;
        int currentLine = 1;
        // Find tokens/whitespace in input
        while(m.find()){    //find returns true as long as end of input file isn't reached
            //String matches = m.group().replace("\n", "n");
            //matches = matches.replace("\r", "r");
            //matches = matches.replace(" ", "s");
            //System.out.println(matches);

            // if we skipped something at the start of the input, mark that as invalid
            // add tokens to the list
            if(m.start() != inputPos){
                tokens.add(new Token(tokenType.ERROR, currentLine));
            }
            if(m.group().charAt(0) == '%'); // is a comment, do nothing
            else if (m.group().contains("FORW"))
                tokens.add(new Token(tokenType.FORW, currentLine));
            else if (m.group().contains("BACK"))
                tokens.add(new Token(tokenType.BACK, currentLine));
            else if (m.group().contains("LEFT"))
                tokens.add(new Token(tokenType.LEFT, currentLine));
            else if (m.group().contains("RIGHT"))
                tokens.add(new Token(tokenType.RIGHT, currentLine));
            else if (m.group().matches("DOWN"))
                tokens.add(new Token(tokenType.DOWN, currentLine));
            else if (m.group().matches("UP"))
                tokens.add(new Token(tokenType.UP, currentLine));
            else if (m.group().contains("REP"))
                tokens.add(new Token(tokenType.REP, currentLine));
            else if (m.group().contains("COLOR"))
                tokens.add(new Token(tokenType.COLOR, currentLine));
            else if(m.group().matches("\\."))
                tokens.add(new Token(tokenType.PERIOD, currentLine));
            else if(m.group().matches("\""))
                tokens.add(new Token(tokenType.QUOTE, currentLine));
            else if(m.group().matches("#[\\dA-F]{6}"))
                tokens.add(new Token(tokenType.HEX, currentLine, m.group()));

            else if(Character.isDigit(m.group().charAt(0))) {
                int i = 0;
                while (Character.isDigit(m.group().charAt(i)))
                    i++;
                tokens.add(new Token(tokenType.DECIMAL, currentLine, Integer.parseInt(m.group().substring(0,i))));
                if (m.group().contains("."))
                    tokens.add(new Token(tokenType.PERIOD, currentLine));}

            if (m.group().contains("\n"))
                currentLine++;
            inputPos = m.end();
        }

        // check if some of the input was not a token

        if (inputPos != input.length()){
            tokens.add(new Token(tokenType.ERROR, currentLine));
        }
        // ending token
        tokens.add(new Token(tokenType.ENDTOKEN, currentLine));

        // debugging code. prints out sequence of tokens
        //for (Token token : tokens)
        //   System.out.println(token.getType() + ((token.getData() != null ? " (" + token.getData() + ")" : "") + " on line: " + token.getLineNumber()));
}


    // look at next token without retrieving
    public Token peekToken() throws SyntaxError {
        return tokens.get(currentToken);
    }

    // retrieve the next token
    public Token nextToken() throws SyntaxError {
        Token result = peekToken();
        ++currentToken;
        return result;
    }

    public Token previousToken() throws SyntaxError{
        if(currentToken == 0)
            return tokens.get(currentToken);
        else return tokens.get(currentToken-1);
    }

    public boolean hasMoreTokens(){
        return currentToken < tokens.size();
    }

}
