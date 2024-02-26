import java.awt.Color;
import java.awt.Graphics;

// Pipe that extends from top of screen
public class TopPipe extends Pipe{

    public TopPipe(Vector2 position, float width, float speed) {
        super(position, width, speed);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect((int)this.position.x, 0, (int)this.width, (int)this.position.y);
    }

    @Override
    public boolean getCollision(Vector2 position, int width) {
        float width2 = width / 2;
        if( ((position.y - width2) < this.position.y) && 
        ((position.x + width2) > this.position.x ) && 
        ((position.x + width2) < (this.position.x + this.width))){
            return true;
        }
        return false;
    }
    




}
