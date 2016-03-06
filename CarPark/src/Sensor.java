import java.awt.Color;
import java.awt.Graphics;

/**
 * @author RYL
 * 
 * 该类是实现传感器的功能
 */
public class Sensor {
	private int x;
	private int y;
	private static final int WIDTH = 5;
	private static final int LENGTH = 5;
//	public TheMain tm;
	public Sensor(int x,int y,TheMain tm){
		this.x = x;
		this.y = y;
//		this.tm = tm;
//		this.width = width;
//		this.length = length;
	}
	public void draw(Graphics g){
		Color c = g.getColor();
		g.setColor(Color.RED);
		g.fillOval(x, y, WIDTH, LENGTH);
		g.setColor(c);
	}
	public int getX() {
		return x;
	}
}
