/**
 * @author RYL
 * 
 * 该类是一个栏杆类，用于实现栏杆的抬起跟落下，并进行判断
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
public class Barrier {
	public TheMain tm;
//	public Car c = null;
	public ArrayList<Car> cl = new ArrayList<Car>();
	public int x = 200;
	public int y = 400;
	public int x1 = 200;
	public int y1 = 400;
//	public boolean flag = false;
	public Barrier(TheMain tm){
		this.tm = tm;
	}
	public void draw(Graphics g){
		Color c = g.getColor();
		g.setColor(Color.black);
		g.drawLine(x1, y1, x, y);
		g.setColor(c);
	}
	public void sendMessage(Car c){
//		this.c = c;
		cl.add(c);
	}
}

class BarrierEnter extends Barrier{
	
	public BarrierEnter(TheMain tm) {
		super(tm);
		// TODO Auto-generated constructor stub
	}
	public void draw(Graphics g){
		Car c = null;
		if(cl.size()>0){
			c = cl.get(0);
			cl.remove(cl.get(0));
		}
		if(c != null && c.getX() == 100 && tm.totalNumber > 0 && c.isGood()){
			move();
			super.draw(g);
//			c.atStreet();//车辆在车道停留
			c.parking();//车辆进入停车位
			tm.totalNumber--;
		}else if(c != null && c.getX() == 100 && tm.totalNumber > 0 && !c.isGood()){
			System.out.println("不属于合法车辆，禁止进入！");
			tm.clist.remove(c);
		}else{
			x = 200;
			y = 450;
			super.draw(g);
		}
	}
	public void move(){
		x = 200+30;
		y = 420;
	}
}

class BarrierOut extends Barrier{
	public BarrierOut(TheMain tm) {
		super(tm);
		// TODO Auto-generated constructor stub
	}
	public void draw(Graphics g){
		Car c = null;
		if(cl.size()>0){
			c = cl.get(0);
			cl.remove(cl.get(0));
		}
			
		x1 = 200 + (tm.totalParkNumber/2+2)*45;
		//x = 200+300;////////
		x = 200 + (tm.totalParkNumber/2+2)*45;
		y = 450;
		int bound = 200 + (tm.totalParkNumber/2+2)*45 - 100;
		if(c != null && c.getX() == bound && c.getSflag().equals("yes")){
			move();
			tm.worker[c.getIdCard()] = 0;
			super.draw(g);
			c.outPark();
			tm.clist.remove(c);
			tm.totalNumber ++;//车出场车位数加1
			tm.totalParkTime += c.getStayTime() / 1000;//换算为s
			tm.outCarNumber ++;//车出场后开始累加出场车辆数目
			tm.averageParkTime = tm.totalParkTime / tm.outCarNumber;
			///////////////
		}else if(c != null && !c.getSflag().equals("yes") && c.getX() == bound){
			super.draw(g);
			c.getback();
			tm.falseCar.add(c);
		}
		else{
			super.draw(g);
		}
	}
	public void move(){
		x += 30;
		y = 420;
	}
	
}
