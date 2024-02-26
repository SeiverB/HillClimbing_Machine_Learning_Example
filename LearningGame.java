import javax.swing.*;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList; // For arbitrary size lists 
import java.util.Arrays;

public class LearningGame extends JPanel implements MouseListener, MouseMotionListener{
    
    static final int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 800;

    public int mousex = 0;
    public int mousey = 0;

    public int gameState = 0;

    public Timer frameTimer;

    public FlappySphere game;

    public int frameTime;

    public float stepSize;
    public ArrayList<Double> tweaks;
    public float temperature;
    public float tempDecrease;
    
    public ArrayList<Double> parameters;

    public ActionListener actionListener;

    public ChangeListener changeListener;

    public boolean playing;

    public LearningGame(){

        //listen for mouse events (clicks and movements) on this object
        addMouseMotionListener(this);
        addMouseListener(this);
        
        this.parameters = new ArrayList<Double>(Arrays.asList(0.0, 0.0, 0.0, 0.0));

        // Randomly initialize values. Since we have found some extremely good values now, we don't need to do this.
        /*
        for(int i = 0; i < this.parameters.size(); i++){
            this.parameters.set(i, (Math.random() - 0.5));
            System.out.println(i + ": " + this.parameters.get(i));
        }
        */

        // Using parameters that seem to allow for the game to run for >15 minutes
        this.parameters = new ArrayList<Double>(Arrays.asList(0.3232958746481672, 2.158564342918485, 0.007292778344468843, 0.22517543203339288));

        // Saved results from previous offline training ---------------------------------------------------------------

        // Very old results, kept only to show how algorithm was changed a few times.
        // [7.675500992278485, 3.11092479331988, -0.18916820267588516]
        // [-0.4182321518654068, 0.14627782837012107, -0.06175080969181241] (new *100 alpha)
        // [-1.8668900588823814, 3.39621720469573, -0.1737460490429775] (new *10 alpha)
        // [-0.3682735536945124, -0.5102236689632045, 0.025282257574220028]
        // [0.88897229639405, 1.0127685846733487, -0.06976049664301837]
        // [3.038641339664571, 2.4646595756815013, -0.10455630227627104]
        // [0.3949831819172218, -3.191738690610196, 0.13511793241523617] (back to *100 alpha)
        // [-0.8325660522523158, 0.18723935725850505, -0.01952986576116536, 0.7549062741603484] (added epsilon based on velocity)
        // [-0.035630570512558274, -0.17383168619768274, 0.009132204444919995, -0.03396733536533719] = '' 308 fitness 
        // [-4.19149190473331, 3.122986305033019, 0.8149943182366529, 1.19317378955825] = alpha now adds instead 308 fitness
        // [-0.41529688750534266, 0.7875840251993109, 1.491183637549114, 2.075452886001506] = 397
        
        /*
        
        plays v well
        0: 0.32617909481233565
        1: 0.7278474291932153
        2: 0.09081943749119263
        3: 0.826056316265939

        better (f=1163.0)
        0: 0.3238790948123359
        1: 0.8085474291932153
        2: 0.07221943749119239
        3: 0.8280563162659377

        WOOOOOOOOOOOOOO
        0: 0.32188587464816737
        1: 1.940454342918489
        2: 0.01127277834446882
        3: 0.08481543203339284

        WOOOOOOOOOOOOOO
        0: 0.3232958746481672
        1: 2.158564342918485
        2: 0.007292778344468843
        3: 0.22517543203339288


        */
        //this.tweaks = new ArrayList<Double>(Arrays.asList(-0.0001, -0.01, -0.1, 0.1, 0.01, 0.0001));

        // The different changes to the parameter weights that are tried.
        this.tweaks = new ArrayList<Double>(Arrays.asList(-0.0001, -0.01, 0.01, 0.0001, 0.000001, -0.000001));

        this.game = new FlappySphere(this.parameters, WINDOW_HEIGHT, false);

        // How large the physics steps are
        this.stepSize = 4f;

        // How often a new frame is played
        this.frameTime = 30;

        // Initial temperature value
        this.temperature = 1;

        // How much temperature decrease after each annealing step.
        this.tempDecrease = -0.0001f;


        // This timer runs the simulation at a reasonable speed for when we wish to view it.
        // This isn't used when we are training our parameters, so the simulation can move as fast as possible.
        this.frameTimer = new Timer(frameTime, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.doStep(stepSize);
                frameTimer.restart();      
            }
        });

        // Uncomment this in order to train the parameters a certain amount of iterations.
        /*
        for(int i = 0; i < 10000; i++){
            annealParameters(this.game);
        }
        */
        
        // After training parameters, this plays the simulation at a reasonable speed. 
        this.game = new FlappySphere(this.parameters, WINDOW_HEIGHT, true);
        System.out.println(this.parameters);
        runGame();
    }

    public void annealParameters(FlappySphere game){
        // best parameter change so far
        int bestParameterIndex = -1;
        double bestTweak = 0;
        
        while(this.game.gameOver == false){
            this.game.doStep(this.stepSize);
        }

        float bestValue = this.game.fitness;

        for(int i = 0; i < parameters.size(); i++){
            double currentParameter = parameters.get(i);
            for(int j = 0; j < tweaks.size(); j++){
                parameters.set(i, parameters.get(i) + tweaks.get(j));
                this.game = new FlappySphere(this.parameters, WINDOW_HEIGHT, false);
                
                while(this.game.gameOver == false){
                    this.game.doStep(this.stepSize);
                }
                
                float value = this.game.fitness + ((float)(Math.random() - Math.random()) * this.temperature);

                if(value > bestValue){
                    bestValue = value;
                    bestParameterIndex = i;
                    bestTweak = tweaks.get(j);
                }

                // Reset back to old value
                parameters.set(i, currentParameter);

            }

        }

        if(this.temperature > 0){
            this.temperature -= this.tempDecrease;
        }
        else{
            this.temperature = 0;
        }

        // Gone through all parameters, check if we have found a good set.
        if(bestParameterIndex >= 0){
            double newValue = bestTweak + parameters.get(bestParameterIndex);

            // Make param change permanent
            parameters.set(bestParameterIndex, newValue);
        }

    }

    // Redraws the graphics on the game window
    public void paintComponent(Graphics g){
        // Set background to black
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        this.game.draw(g);

    }

    public void runGame(){
        this.frameTimer.start();
    }

    
    // Capture mouse drag events
    @Override
    public void mouseDragged(MouseEvent e) {
        this.mousex = e.getX();
        this.mousey = e.getY();
    }

    // IF you want to play by clicking your mouse to flap the bird,
    // you must uncomment this line <this.game.flap();> below in this method.
    @Override
    public void mousePressed(MouseEvent e){
        mousex = e.getX();
        mousey = e.getY();
        //this.game.flap();
    }

    // Capture mouse move events
    @Override
    public void mouseMoved(MouseEvent e) {
        this.mousex = e.getX();
        this.mousey = e.getY();
    }

    @Override
    public void mouseExited(MouseEvent e){
    }

    @Override
    public void mouseEntered(MouseEvent e){
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

}