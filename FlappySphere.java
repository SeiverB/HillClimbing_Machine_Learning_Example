import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
// import java.util.Random;

public class FlappySphere {
    
    public Circle circle;
    public boolean gameOver;
    public float fitness;
    public float gapSize = 200;
    public float randomness = 350;
    public Pipe topPipe, bottomPipe;
    public Boolean debug = false;

    private int flapTimer;
    
    // Was used to generate the same set of pipes each time.
    // Useful for debugging at first, but ended up causing overfitting.
    // private Random generator = new Random(153548);

    public int windowHeight;
    public ArrayList<Double> parameters;

    // This is the class for running the actual game
    public FlappySphere(ArrayList<Double> parameters, int windowHeight, Boolean debug){
        this.windowHeight = windowHeight;
        this.circle = new Circle(new Vector2(16, windowHeight / 2), 0.1f, 4, 16);
        makeNewPipes();
        this.gameOver = false;
        this.fitness = 0;
        this.parameters = parameters;
        this.debug = debug;
    }

    // Let bird flap.
    public void flap(){
        this.circle.flap();
    }

    // Draws the game.
    public void draw(Graphics g){
        g.setColor(new Color(0.0f, 0.75f, 0.15f));
        g.fillRect(0, 0, 800, 900);
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, 800, 800);
        this.circle.draw(g);
        this.topPipe.draw(g);
        this.bottomPipe.draw(g);
    }

    // This is what the parameters control
    // The total sum of all the weighted values is the number of physics steps until the next flap occurs 
    public void handleFlapTimer(){
        if(this.flapTimer < 0){
            flap();
            // Alpha[0] is inversed, and multiplied with the distance to the next pipe.
            // Beta[1] is multiplied with the difference between the circle's y position, and the bottom pipe's y position
            // Delta[2] is the same as Beta, but the difference is squared.
            // Epsilon[3] is multiplied with the velocity  
            this.flapTimer = (int)Math.round( ((bottomPipe.position.x - circle.position.x)/(parameters.get(0)*100)) + (
            ((parameters.get(1)/100) * (bottomPipe.position.y - circle.position.y)) + 
            ((parameters.get(2)/1000) * Math.pow(bottomPipe.position.y - circle.position.y, 2)) +
            ((parameters.get(3) * circle.velocity.y))));
            if(this.flapTimer < 0){
                this.flapTimer = -this.flapTimer;
            }
            if(this.debug){
                // Print out the parameter weights each time we flap if debug mode is on
                System.out.println("AlphaVal: " + (bottomPipe.position.x - circle.position.x)/(parameters.get(0) * 100));
                System.out.println("BetaVal: " + (parameters.get(0)/100) * (bottomPipe.position.y - circle.position.y));
                System.out.println("DeltaVal: " +(parameters.get(1)/1000) * Math.pow(bottomPipe.position.y - circle.position.y, 2));
                System.out.println("EpsilonVal: " + ((parameters.get(3) * circle.velocity.y)));
                System.out.println(this.flapTimer);
                if(this.gameOver){
                    this.flapTimer = 1000;
                }
            }
            
        }
        this.flapTimer--;
    }
    // Do a single physics step
    public void doStep(float stepSize){
        handleFlapTimer();
        if(!this.gameOver){
            this.circle.doStep(stepSize);
            this.topPipe.doStep(stepSize);
            this.bottomPipe.doStep(stepSize);
            
            // If pipes reach left edge of screen, create new pipes.
            if( (topPipe.position.x + topPipe.width) < 0 ){
                makeNewPipes();
            }
            
            if(getCollisions()){
                this.gameOver = true;
                if(this.debug){
                    System.out.println("Fitness: " + this.fitness);
                    for(int i = 0; i < this.parameters.size(); i++){
                        System.out.println(i + ": " + this.parameters.get(i));
                    }
                }
            }
            this.fitness++;
            
            /* used in training for limiting the total amount of time that the simulation may run for.
            if(this.fitness >= 10000000){
                for(int i = 0; i < this.parameters.size(); i++){
                    System.out.println(i + ": " + this.parameters.get(i));
                }
                System.out.println("WOOOOOOOOOOOOOO");
                this.gameOver = true;
                this.fitness = 999999;
            }
            */
        }
    }

    // Create a new set of pipes at the right edge of the screen
    public void makeNewPipes(){
        //float rand = (float)((generator.nextFloat() - 0.5f) * this.randomness);
        float rand = (float)((Math.random() - 0.5f) * this.randomness);
        this.topPipe = new TopPipe(new Vector2(windowHeight, (windowHeight / 2) - (gapSize / 2) + rand), 90, 5);
        this.bottomPipe = new BottomPipe(new Vector2(windowHeight, (windowHeight / 2) + (gapSize / 2) + rand), 90, 5);
    }

    // Handles all the collisions between the circle, the ground, and the pipes.
    public boolean getCollisions(){

        if(topPipe.getCollision(circle.position, circle.width) || bottomPipe.getCollision(circle.position, circle.width)
        || (this.circle.position.y) > (this.windowHeight - circle.width/2)){
            return true;
        }
        return false;
    }

}
