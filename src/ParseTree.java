import java.util.ArrayList;

abstract class ParseTree {
    abstract public void evaluate(Leonardo L);
}

class RepTree extends ParseTree{

    private int reps;
    private ParseTree quoteNodes; // for commands to be repeated
    private ParseTree nextNode; // next node in syntax tree, outside rep

    RepTree(int reps, ParseTree quoteNodes, ParseTree nextNode){
        this.reps = reps;
        this.quoteNodes = quoteNodes;
        this.nextNode = nextNode;
    }

    public void evaluate(Leonardo L){

        for(int i = 0; i<reps; i++){
                quoteNodes.evaluate(L);
        }


        // recursive call to neighbour node when rep is finished
        if(nextNode == null)
            return;
        else nextNode.evaluate(L);
    }

}

class Operations extends ParseTree{

    private tokenType operation;
    private ParseTree nextNode;
    private Object data;

    Operations(tokenType operation, Object data, ParseTree nextNode){
        this.operation = operation;
        this.nextNode = nextNode;
        this.data = data;
    }

    public void evaluate(Leonardo L){
        if (operation == null)
            return;
        switch(operation){
            case FORW: L.move((int)data);
                        //nextNode.evaluate(L);
                        break;
            case BACK: L.move(-(int)data);
                        //nextNode.evaluate(L);
                        break;
            case LEFT: L.turn((int)data);
                        //nextNode.evaluate(L);
                        break;
            case RIGHT: L.turn(-(int)data);
                        //nextNode.evaluate(L);
                        break;
            case COLOR: L.changeColor((String)data);
                        //nextNode.evaluate(L);
                        break;
            case UP:    L.penUp();
                        //nextNode.evaluate(L);
                        break;
            case DOWN:  L.penDown();
                        //nextNode.evaluate(L);
                        break;
            case ENDTOKEN: System.exit(0); // exit i think or nothing idk we will see
            default:
        }
        if (nextNode == null)
            return;
        else nextNode.evaluate(L);
    }
}
