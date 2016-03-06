/**
 * @author RYL
 * 这是一个停车场的类，设计时，每个车位相当于一个小的停车场，区别就是大小不同
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Random;
public class Park {
	private  int x = 200;
	private  int y = 300;
	private int WIDTH;//
	private int LENGTH = 250;
	private int width;
	private int length;
	private String flag = "park";
	Park(TheMain tm){
		WIDTH = (tm.totalParkNumber/2 + 2)*45;
//		System.out.println("WIDTH : "+WIDTH);
		width = WIDTH;
		length = LENGTH;
	}
	Park(int x,int y,int wid,int len,String flag){
		this.x = x;
		this.y = y;
		width = wid;
		length = len;
		this.flag = flag;
	}
	public void draw(Graphics g){
		Color c = g.getColor();
		if(flag.equals("park"))
			g.setColor(Color.green);
		else
			g.setColor(Color.ORANGE);
		g.fillRect(x, y, width, length);
		g.setColor(c);
	}

}
