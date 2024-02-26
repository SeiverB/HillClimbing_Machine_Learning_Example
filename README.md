## LOGIC / DESIGN

This program is a demonstration of an AI that learned to play a simplified re-creation of "Flappy Bird".
The AI was trained offline using a hill-climbing algorithm, along with the principles of annealing.

The AI was run through well over 1000000 iterations, with different learning methods being manually tuned 
along the way to produce an optimal result.

The AI was trained to a near-perfect state with the following 4 parameters:
- Alpha[0] is inversed, and multiplied with the distance to the next pipe.
- Beta[1] is multiplied with the difference between the circle's y position, and the bottom pipe's y position
- Delta[2] is the same as Beta, but the difference is squared.
- Epsilon[3] is multiplied with the velocity  

The parameters are also multiplied or divided by some factor of ten so that their final values end up
being in a similar range to eachother

Once the parameters are multiplied by the weights, the resultant integer is the number of frames the circle waits before performing another "flap"

## HOW TO COMPILE / RUN

To run game, simply execute the included .jar file.

To compile game from source into .jar file, ensure you have java jdk installed, 
then open up your command-line interface.
navigate to the "source" folder included in this zip file, and run the following 2 commands:


javac *.java

jar cvfm game.jar MANIFEST.MF *.class


the .jar file may then be executed from the command line by: java -jar game.jar

#### If you wish to actually play the game yourself, or modify the algorithm/weights, use the following instructions, along with the comments in the source code.

To play the game yourself, you will need to disable handleFlapTimer(); in the dostep method of the FlappySphere game class
as well as enable //this.game.flap(); in the mousePressed method in LearningGame.java

To train the ai, uncomment the specified lines within LearningGame.java, and within flappysphere.java
Some code merely adds some functionality that makes it easier to train in certain cases, 
and not all of it will need to be uncommented.


## BUGS

The AI was found to run for at least 10 minutes in most cases, though through the training we know it is 
not perfect, and will inevitably fail eventually. Whether this is due to an unfortunate sequence of random
numbers, or a fault of the AI itself, i don't know.
