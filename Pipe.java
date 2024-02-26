import java.awt.Graphics;

// Abstract class for both types of pipe
abstract class Pipe {
    
    // Where x is lateral position on screen, and y is distance mouth of pipe has from center of screen.
    public Vector2 position;

    // How fast pipe moves. Should be same as other pipe.
    public float speed;

    public float width;

    public Pipe(Vector2 position, float width, float speed){
        this.position = position;
        this.speed = speed;
        this.width = width;
    }

    // Move pipe. Suprise! the bird is actually stationary. 
    public void doStep(float stepSize){
        this.position = new Vector2(this.position.x - stepSize * this.speed, this.position.y); 
    }

    public abstract void draw(Graphics g);

    public abstract boolean getCollision(Vector2 position, int width); 

}
