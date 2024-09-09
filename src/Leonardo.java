
public class Leonardo {
    private int angle;
    private double xPos;
    private double yPos;
    private boolean penDown = false;
    private String color = "#0000FF";

    public void move(int steps){
        double oldXPos = xPos;
        double oldYPos = yPos;
        xPos = (xPos + steps*Math.cos((Math.PI*angle)/180));
        yPos = (yPos + steps*Math.sin((Math.PI*angle)/180));
        if (penDown){
            System.out.printf("%s %.4f %.4f %.4f %.4f%n", color, oldXPos, oldYPos, xPos, yPos);
        }
    }

    public void penUp(){penDown = false;}
    public void penDown(){penDown = true;}

    public void turn(int degrees){angle += degrees;}

    public void changeColor(String color){
        this.color = color;
    }
}

