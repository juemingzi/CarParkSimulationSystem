
/**
 * @author RYL
 * 
 * 每个汽车有属于自己的员工ID号，跟停车位，车辆生成时，自动分配ID
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class Car {
	public TheMain tm;
	private static final int WIDTH = 30;//车的大小
	private static final int LENGTH = 80;//车的大小
	private static final int ENUMBER = 30;// 员工ID取值范围
	private static Random r = new Random();//随机数，用于生成员工ID
	private int idCard;//员工ID
	private int parkNumber;//车辆停车位
	private int stayTime = 0;//随机生成的车辆停车时间
	private long judgeTime = Long.MAX_VALUE;//车辆的出停车场的时间，初始设为无穷大
	private String sflag = "";//若为yes说明该车合法可以出场
	private boolean flag = false;//一个标志
	private boolean inPark = false;//判断该车是否已经进停车场

	// Calendar data=Calendar.getInstance();
	// int year = data.get(Calendar.YEAR);
	// int month = data.get(Calendar.MONTH)+1;
	// int day = data.get(Calendar.DATE);
	// int hour = data.get(Calendar.HOUR);
	// int minute = data.get(Calendar.MINUTE);
	// int second = data.get(Calendar.SECOND);

	private int x = 50;
	private int y = 150;
	private int width;
	private int length;
	private boolean isGood = false;//看该车是否合法，若为true则允许进停车场

	Car(TheMain tm) {
		// idCard = r.nextInt(tm.e);
		int count = 0;
		idCard = r.nextInt(tm.e);
		while (true) {
			 if (count == tm.e + 20) {
			 idCard = r.nextInt(ENUMBER)+ tm.e;
			 int i;
			 for(i=0;i<tm.clist.size();i++){
				 if(tm.clist.get(i).idCard == idCard)
					 break;
			 }
			 if(i == tm.clist.size()){
				 isGood = true;
			 }
			 break;
			 } else
			if (tm.worker[idCard] == 0) {
				tm.worker[idCard] = 1;
				isGood = true;
				break;
			} else {
				idCard = r.nextInt(tm.e);
				count++;
			}
		}
		width = WIDTH;
		length = LENGTH;
		this.tm = tm;
	}
	public String getSflag() {
		return sflag;
	}

	public boolean isGood() {
		return isGood;
	}
	public int getStayTime() {
		return stayTime;
	}
	public void setStayTime(int stayTime) {
		this.stayTime = stayTime;
	}
	public long getJudgeTime() {
		return judgeTime;
	}
	public void setJudgeTime(int time) {
		judgeTime += time;
	}

	// 获取画笔画图
	public void draw(Graphics g) {
		long currentTime;
		// 在draw的时候判断该车是否已经到时间该出去了，如果时间到了将flag设为true，车驶向出站口。
		currentTime = System.currentTimeMillis();
		charge();
		if (currentTime / 1000 == judgeTime / 1000 && flag == false) {
			// System.out.println("currentTime == judgeTime ");
			moveOut();
			//员工ID超出停车位的员工不为违反规定的
			if (idCard >= 0 && idCard < tm.totalParkNumber) {
				sflag = "yes";
			}
			// if(tm != null)
			tm.bo.sendMessage(this);
			flag = true;
		}

		Color c = g.getColor();
		g.setColor(Color.BLUE);
		// g.fillOval(X, Y, WIDTH, LENDTH);
		g.fillRect(x, y, length, width);
		g.setColor(c);
		g.drawString("ID:" + idCard, x, y);

	}

	public void moveIn() {
		this.x = 100;
		this.y = 410;

	}

	// 车辆走出停车场
	public void moveOut() {
		// this.x = 400;
		this.x = 200 + (tm.totalParkNumber / 2 + 2) * 45 - 100;
		this.y = 410;
		this.length = LENGTH;
		this.width = WIDTH;
		// 车辆离开停车位后，停车位变量设为0
		tm.carpark[parkNumber] = 0;
	}

	public void outPark() {
		int n = tm.bo.x1 - 10;
		while (n <= tm.so.getX() + 30) {
			if (this.x == tm.so.getX()) {
				tm.be.x = 200 + (tm.totalParkNumber / 2 + 2) * 45;
				tm.be.y = 450;
			}
			this.x = n;
			n += 10;
		}
		this.x = 550;
		this.y = 410;

	}

	// 车辆进入停车场选择离入口最近的地方开始停车
	public void parking() {
		// parkNumber = r.nextInt(10);
		int csp;
		// while (tm.carpark[parkNumber] != 0) {
		// parkNumber = r.nextInt(10);
		// }

		// System.out.println("sp number is : " + parkNumber);
		int n = 0;
		while (n <= tm.totalParkNumber / 2 - 1) {
			if (tm.carpark[n] == 0) {
				tm.carpark[n] = 1;
				this.x = 200 + (n + 1) * 45 + 5;
				this.y = 300 + 10;
				this.length = WIDTH;
				this.width = LENGTH;
				this.parkNumber = n;
				break;
			} else if (tm.carpark[n + tm.totalParkNumber / 2] == 0) {
				tm.carpark[n + tm.totalParkNumber / 2] = 1;
				csp = n + 1;
				this.x = 200 + csp * 45 + 5;
				this.y = 450 + 10;
				this.length = WIDTH;
				this.width = LENGTH;
				this.parkNumber = n + tm.totalParkNumber / 2;
				break;
			}
			n++;
		}
		// if (parkNumber <= 4) {
		// tm.carpark[parkNumber] = 1;
		// this.x = 200 + (parkNumber + 1) * 45 + 5;
		// this.y = 300 + 10;
		// this.length = WIDTH;
		// this.width = LENGTH;
		// } else {
		// tm.carpark[parkNumber] = 1;
		// csp = parkNumber - 4;
		// this.x = 200 + csp * 45 + 5;
		// this.y = 450 + 10;
		// this.length = WIDTH;
		// this.width = LENGTH;
		// }
		// 对每一辆进入停车场的车随机生成停车时间
		int time = r.nextInt(5) + 1;
		stayTime = time * 5 * 1000;
		judgeTime = System.currentTimeMillis();
		judgeTime += stayTime;
		inPark = true;
		// System.out.println("judgeTime is : " + judgeTime);
		tm.cTime.add(this);

	}

	// 车辆在停车道停留
	public void atStreet() {
		int n = 100;
		while (n <= tm.se.getX() + 10) {
			if (this.x == tm.se.getX()) {
				tm.be.x = 200;
				tm.be.y = 450;
			}
			this.x = n;
			n += 10;
		}
		// this.x = 220;
		this.y = 410;
	}

	public String keyPressed(KeyEvent e) {
		if (inPark == false) {
			moveIn();// 将车停放到入口
			if (tm.totalNumber == 0) {
				tm.cAtBarrier.add(this);
				return "停车场已满！！！";
			} else {
				/**
				 * 处理车道冲突情况，当前车辆进站时判断，如果此时车场中有车要出去则等待，先进后出
				 */
				// 获取当前时间看此时是否有车出场,若有则停车时间加时让进站车辆先行走
				long currentTime = System.currentTimeMillis();
				int xxx = tm.cTime.size();
				// System.out.println("tm.cTime.size() "+tm.cTime.size());
				if (tm.cTime.size() > 0)
					for (int i = 0; i < tm.cTime.size(); i++) {
						Car ccuren = tm.cTime.get(i);
						if (ccuren.judgeTime / 1000 == currentTime / 1000) {
							ccuren.judgeTime += 1000;
						}
					}
				// if(tm != null)
				tm.be.sendMessage(this);
				// parking();
				// tm.totalNumber--;
				return "OK!";
			}
		} else {
			System.out.println(" not OK～～～");
			return "OK!";
		}

	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getIdCard() {
		return idCard;
	}

	/**
	 * 该函数用来实现当车辆不符合要求不允许出停车场时，回到原来位置或者离出口最近的位置
	 */
	public void getback() {
		int n = tm.totalParkNumber / 2 - 1;// 跟停车位数目有关
		int m = n;
		int csp;
		if (tm.carpark[this.parkNumber] == 0) {
			if (parkNumber <= m) {
				tm.carpark[parkNumber] = 1;
				this.x = 200 + (parkNumber + 1) * 45 + 5;
				this.y = 300 + 10;
				this.length = WIDTH;
				this.width = LENGTH;
			} else {
				tm.carpark[parkNumber] = 1;
				csp = parkNumber - m;
				this.x = 200 + csp * 45 + 5;
				this.y = 450 + 10;
				this.length = WIDTH;
				this.width = LENGTH;
			}
		} else {
			while (n >= 0) {
				if (tm.carpark[n] == 0) {
					tm.carpark[n] = 1;
					this.x = 200 + (n + 1) * 45 + 5;
					this.y = 300 + 10;
					this.length = WIDTH;
					this.width = LENGTH;
					this.parkNumber = n;
					return;
				} else if (tm.carpark[n + m + 1] == 0) {
					tm.carpark[n + m + 1] = 1;
					csp = n + 1;
					this.x = 200 + csp * 45 + 5;
					this.y = 450 + 10;
					this.length = WIDTH;
					this.width = LENGTH;
					this.parkNumber = n + m + 1;
					return;
				}
				n--;
			}
		}
	}

	public void charge() {
		int num = this.parkNumber % (tm.totalParkNumber / 2);
		for (int j = 0; j < tm.cTime.size(); j++) {
			Car ctest = tm.cTime.get(j);
			if (this.judgeTime == ctest.judgeTime && this.idCard != ctest.idCard) {
				int tnum = ctest.parkNumber % (tm.totalParkNumber / 2);
				if (num <= tnum) {
					this.judgeTime += 2000;
				} else {
					ctest.judgeTime += 2000;
				}
			}
		}
	}
	
	public void falseCarOutPark(){
		moveOut();
		sflag = "yes";
		stayTime =(int)(System.currentTimeMillis()-judgeTime);
		tm.bo.sendMessage(this);
	}
}
