import java.util.*;

public class Parser {
    private Lexer lexer;
    //private Leonardo L;

    Parser(Lexer lexer){
        this.lexer = lexer;
    }

    public ParseTree parse() throws SyntaxError{
        ParseTree operations = expr(); //operations();
        //if (lexer.nextToken().getType() != tokenType.ENDTOKEN){
            //throw new SyntaxError(lexer.peekToken().getLineNumber());
        //}
        return operations;
    }

    private ParseTree expr()  throws SyntaxError {
        ParseTree operations;
        //tokenType type = lexer.peekToken().getType();
        //while(lexer.hasMoreTokens()) {
               operations = operations();
        //    }
        return operations;
    }


    private static final Set<tokenType> commands = new HashSet<>
            (Arrays.asList(tokenType.FORW, tokenType.BACK,
                    tokenType.LEFT, tokenType.RIGHT, tokenType.DOWN, tokenType.UP
                    , tokenType.COLOR, tokenType.REP));


    // parse tokens to create ParseTrees
    private ParseTree operations() throws SyntaxError{
        Token t;

        if(lexer.peekToken().getType() == tokenType.ENDTOKEN){
            t = lexer.nextToken();}
        else t = lexer.peekToken();

        switch (t.getType()){
            case FORW:{
                        return new Operations(tokenType.FORW, moveOp(), operations());}
            case BACK:{
                        return new Operations(tokenType.BACK, moveOp(), operations());}
            case LEFT:{
                        return new Operations(tokenType.LEFT, moveOp(), operations());} // can  use moveop, same syntax
            case RIGHT:{
                        return new Operations(tokenType.RIGHT, moveOp(), operations());}
            case DOWN:  penOp();
                        return new Operations(tokenType.DOWN, null, operations());
            case UP:    penOp();
                        return new Operations(tokenType.UP, null, operations());
            case COLOR: return new Operations(tokenType.COLOR, colorOp(), operations());


            case REP:
                        Token rep = lexer.nextToken(); // consume
                        Token decimal = lexer.nextToken();
                        if (decimal.getType() != tokenType.DECIMAL)
                            throw new SyntaxError(decimal.getLineNumber());
                        if (decimal.getType() == tokenType.ENDTOKEN) // unexpected end
                            throw new SyntaxError(decimal.getLineNumber());
                        if (lexer.peekToken().getType() == tokenType.QUOTE){
                            return new RepTree((int) decimal.getData(), Quotes(), operations());
                        }
                        else{
                            // call  noquoteRepNodes?
                            return new RepTree((int) decimal.getData(), noQuotes(), operations());
                        }
                        //return new RepTree((int)decimal.getData(), repNodes(), operations());

            case ENDTOKEN:
                        return new Operations(tokenType.ENDTOKEN, null, null);

            default:
                throw new SyntaxError(t.getLineNumber());
            }

    }

    // this function assumes we just started rep quotes
    private ParseTree Quotes() throws SyntaxError{

        Token next = lexer.nextToken(); // consume first quote

        next = lexer.peekToken();
        if (next.getType() == tokenType.QUOTE) { // no empty quotes
            throw new SyntaxError(next.getLineNumber());
        }

        switch(next.getType()){
            case FORW:
                return new Operations(tokenType.FORW, moveOp(), inQuotes());
            case BACK:
                return new Operations(tokenType.BACK, moveOp(), inQuotes());
            case LEFT:
                return new Operations(tokenType.LEFT, moveOp(), inQuotes());
            case RIGHT:
                return new Operations(tokenType.RIGHT, moveOp(), inQuotes());
            case DOWN:
                penOp();
                return new Operations(tokenType.DOWN, null, inQuotes());
            case UP:
                penOp();
                return new Operations(tokenType.UP, null, inQuotes());
            case COLOR:
                return new Operations(tokenType.COLOR, colorOp(), inQuotes());
            case REP:
                Token rep = lexer.nextToken(); // consume
                Token decimal = lexer.nextToken();

                if (decimal.getType() != tokenType.DECIMAL)
                    throw new SyntaxError(decimal.getLineNumber());
                if (decimal.getType() == tokenType.ENDTOKEN) // unexpected end
                    throw new SyntaxError(decimal.getLineNumber());
                if (lexer.peekToken().getType() == tokenType.QUOTE){
                    return new RepTree((int) decimal.getData(), Quotes(), inQuotes());
                }
                else{
                    return new RepTree((int) decimal.getData(), noQuotes(), inQuotes());
                }
            case QUOTE: // should not happen
                lexer.nextToken();  // consume end-quote
                return null;
            case ENDTOKEN: // unexpected end
                throw new SyntaxError(lexer.previousToken().getLineNumber());
            default:
                throw new SyntaxError(next.getLineNumber());
        }

    }


    // this function is called if we are in quotes
    private ParseTree inQuotes() throws SyntaxError{

        Token next = lexer.peekToken();
        if(next.getType() == tokenType.QUOTE){
            lexer.nextToken(); // consume quote
            return null; // end of quote
        }

        switch(next.getType()){
            case FORW:
                return new Operations(tokenType.FORW, moveOp(), inQuotes());
            case BACK:
                return new Operations(tokenType.BACK, moveOp(), inQuotes());
            case LEFT:
                return new Operations(tokenType.LEFT, moveOp(), inQuotes());
            case RIGHT:
                return new Operations(tokenType.RIGHT, moveOp(), inQuotes());
            case DOWN:
                penOp();
                return new Operations(tokenType.DOWN, null, inQuotes());
            case UP:
                penOp();
                return new Operations(tokenType.UP, null, inQuotes());
            case COLOR:
                return new Operations(tokenType.COLOR, colorOp(), inQuotes());
            case REP:
                Token rep = lexer.nextToken(); // consume
                Token decimal = lexer.nextToken();

                if (decimal.getType() != tokenType.DECIMAL)
                    throw new SyntaxError(decimal.getLineNumber());
                if (decimal.getType() == tokenType.ENDTOKEN) // unexpected end
                    throw new SyntaxError(decimal.getLineNumber());
                if (lexer.peekToken().getType() == tokenType.QUOTE){
                    return new RepTree((int) decimal.getData(), Quotes(), inQuotes());
                }
                else{
                    return new RepTree((int) decimal.getData(), noQuotes(), inQuotes());
                }
            case QUOTE:
                lexer.nextToken(); // consume quote
                return null;
            case ENDTOKEN: //unexpected end
                throw new SyntaxError(lexer.previousToken().getLineNumber());
            default:
                throw new SyntaxError(next.getLineNumber());
        }

    }

    private ParseTree noQuotes() throws SyntaxError{
        Token next = lexer.peekToken();

        switch(next.getType()){
            case FORW:
                return new Operations(tokenType.FORW, moveOp(), null);
            case BACK:
                return new Operations(tokenType.BACK, moveOp(), null);
            case LEFT:
                return new Operations(tokenType.LEFT, moveOp(), null);
            case RIGHT:
                return new Operations(tokenType.RIGHT, moveOp(), null);
            case DOWN:
                return new Operations(tokenType.DOWN, null, null);
            case UP:
                return new Operations(tokenType.UP, null, null);
            case COLOR:
                return new Operations(tokenType.COLOR, colorOp(), null);
            case REP:
                Token rep = lexer.nextToken(); // consume
                Token decimal = lexer.nextToken();

                if (decimal.getType() != tokenType.DECIMAL)
                    throw new SyntaxError(decimal.getLineNumber());
                if (decimal.getType() == tokenType.ENDTOKEN) // unexpected end
                    throw new SyntaxError(decimal.getLineNumber());
                if (lexer.peekToken().getType() == tokenType.QUOTE){
                    return new RepTree((int) decimal.getData(), Quotes(), null);
                }
                else{
                    return new RepTree((int) decimal.getData(), noQuotes(), null);
                }
            default:
                throw new SyntaxError(next.getLineNumber());
        }

    }


    private String colorOp() throws SyntaxError{
        Token color = lexer.nextToken();
        Token hex = lexer.nextToken();
        if(hex.getType() == tokenType.ENDTOKEN) // unexpected end, error where last code line was.
            throw new SyntaxError(color.getLineNumber());
        if(hex.getType() != tokenType.HEX){
            throw new SyntaxError(hex.getLineNumber());}
        Token period = lexer.nextToken();
        if(period.getType() == tokenType.ENDTOKEN)
            throw new SyntaxError(hex.getLineNumber());
        if(period.getType() != tokenType.PERIOD){
            throw new SyntaxError(period.getLineNumber());}
        return (String)hex.getData();
    }



    // Ensures requested move is correct
    private int moveOp() throws SyntaxError{
        Token move = lexer.nextToken(); // nextToken to recieve forw token, and point to next token
        Token decimal = lexer.nextToken();
        if(decimal.getType() == tokenType.ENDTOKEN)
            throw new SyntaxError(move.getLineNumber());
        if(decimal.getType() != tokenType.DECIMAL){
            throw new SyntaxError(decimal.getLineNumber());}

        Token period = lexer.nextToken();
        if(period.getType() == tokenType.ENDTOKEN)
            throw new SyntaxError(decimal.getLineNumber());
        if(period.getType() != tokenType.PERIOD){
            throw new SyntaxError(period.getLineNumber());}
        return (int)decimal.getData();
    }

    // Ensures requested pen operation is given in a correct order/manner
    private void penOp() throws SyntaxError{
        Token pen = lexer.nextToken();
        Token period = lexer.nextToken();
        if(period.getType() == tokenType.ENDTOKEN)
            throw new SyntaxError(pen.getLineNumber());
        if(period.getType() != tokenType.PERIOD)
            throw new SyntaxError(period.getLineNumber());
    }


    /**
     // preliminary plan:
     // - check in operations() if quotes, if yes
     //      -call repNodes assuming quotes
     // - otherwise check tokentype and call moveOp/colorOp accordingly as usual

     private ParseTree repNodes() throws SyntaxError{

     Token next = lexer.peekToken();
     ArrayList<ParseTree> repCommands = new ArrayList<>(); // yeet this l8r

     if (next.getType() == tokenType.QUOTE){
     lexer.nextToken(); // consume start-quote
     next = lexer.peekToken();
     if(next.getType() == tokenType.QUOTE)   // no empty quotes
     throw new SyntaxError(next.getLineNumber());

     //quoteloop: while(commands.contains(next.getType())){    // label while loop to be able to break it
     next = lexer.peekToken();
     switch(next.getType()){
     case FORW:
     return new Operations(tokenType.FORW, moveOp(), operations());
     //repCommands.add(new Operations(tokenType.FORW, moveOp(), null));
     break;
     case BACK:
     repCommands.add(new Operations(tokenType.BACK, moveOp(), null));
     break;
     case LEFT:
     repCommands.add(new Operations(tokenType.LEFT, moveOp(), null)); // can  use moveop, same syntax order
     break;
     case RIGHT:
     repCommands.add(new Operations(tokenType.RIGHT, moveOp(), null));
     break;
     case DOWN: penOp();
     repCommands.add(new Operations(tokenType.DOWN, null, null));
     break;
     case UP: penOp();
     repCommands.add(new Operations(tokenType.UP, null, null));
     break;
     case COLOR:
     repCommands.add(new Operations(tokenType.COLOR, colorOp(), null));
     break;

     case REP: Token rep = lexer.nextToken();
     Token decimal = lexer.nextToken();
     if (decimal.getType() != tokenType.DECIMAL) {
     throw new SyntaxError(decimal.getLineNumber());}
     repCommands.add(new RepTree((int)decimal.getData(), repNodes(), null));
     break;
     case QUOTE:
     break;
     //break quoteloop;
     case ENDTOKEN:
     throw new SyntaxError(lexer.previousToken().getLineNumber());
     default:
     throw new SyntaxError(next.getLineNumber());
     }

     //    }
     lexer.nextToken();  // consume end-quote
     //after while--


     if(next.getType() != tokenType.QUOTE){  // must start and end with quote if multiple commands in rep.
     throw new SyntaxError(next.getLineNumber());
     }
     }
     else if (commands.contains(next.getType())){    // this is for the case of a one-line rep
     switch(next.getType()){     // check if one command without quotes instead
     case FORW:
     repCommands.add(new Operations(tokenType.FORW, moveOp(), null));
     break;
     case BACK:
     repCommands.add(new Operations(tokenType.BACK, moveOp(), null));
     break;
     case LEFT:
     repCommands.add(new Operations(tokenType.LEFT, moveOp(), null));
     break; // can  use moveop, same syntax order
     case RIGHT:
     repCommands.add(new Operations(tokenType.RIGHT, moveOp(), null));
     break;
     case DOWN:  penOp();
     repCommands.add(new Operations(tokenType.DOWN, null, null));
     break;
     case UP:    penOp();
     repCommands.add(new Operations(tokenType.UP, null, null));
     break;
     case COLOR: repCommands.add(new Operations(tokenType.COLOR, colorOp(), null));
     break;
     case REP:
     Token rep = lexer.nextToken();
     Token decimal = lexer.nextToken();
     if(decimal.getType() != tokenType.DECIMAL)
     throw new SyntaxError(decimal.getLineNumber());
     repCommands.add(new RepTree((int)decimal.getData(), repNodes(), null));
     break;

     }
     }

     return repCommands;
     }
     */
}

