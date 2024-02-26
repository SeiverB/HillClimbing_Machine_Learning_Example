import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

	//declare and initialize the frame
    static JFrame f = new JFrame("Hill-Climbing Learning Game");
    
    public static int DELAY = 17; // Frame time in milliseconds

    private static Timer frameTimer;

    // Game Object
    private static LearningGame game;

    private static JPanel mainpanel;

    public static void main(String[] args) {

		//make it so program exits on close button click
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Create game area
        Main.game = new LearningGame();

        // Create frame for main menu
        Main.mainpanel = new JPanel(new BorderLayout());
        mainpanel.setOpaque(true);

        //add a drawing timer object
        frameTimer = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //repaint the screen
                game.repaint();
                
            }
        });

        // Start frame timer once it is initialized
        Main.frameTimer.start();


        //the size of the game will be 810x810, the size of the JFrame needs to be slightly larger
        f.setSize(816,900);

        f.setLocationRelativeTo(null);

		//show the window
        f.setVisible(true);

        Main.game.setVisible(true);

        // Add game object to frame
        mainpanel.add(game, BorderLayout.CENTER);

        f.setContentPane(mainpanel);

	}

}