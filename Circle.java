import java.awt.Color;
import java.awt.Graphics;

public class Circle {
    
    public Vector2 velocity;
    public Vector2 position;
    public float gravity, flapStrength;
    public int width;

    private boolean doFlap = false;

    public Circle(Vector2 startingPos, float gravity, float flapStrength, int width){
        this.width = width;
        this.position = startingPos;
        this.velocity = new Vector2(0, 0);
        this.gravity = gravity;
        this.flapStrength = flapStrength;
    }

    public void doStep(float stepSize){
        this.position = this.position.add(this.velocity.scalar(stepSize));
        if(doFlap){
            this.velocity = new Vector2(0, -this.flapStrength);
            this.doFlap = false;
        }
        else{
            this.velocity = new Vector2(0, this.velocity.y + (this.gravity * stepSize));
        }
    }

    public void flap(){
        this.doFlap = true;
    }

    public void draw(Graphics g){
        g.setColor(Color.YELLOW);
        g.fillOval((int)Math.round(this.position.x - width / 2), (int)Math.round(this.position.y - width/2), width, width);
    }

}
