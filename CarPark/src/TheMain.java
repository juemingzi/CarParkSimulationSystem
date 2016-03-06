
/**
 * @author RYL
 * 
 * �����������ڽ�����������������ʵ��ϵͳ���������
 */
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.Date;

public class TheMain extends Frame {

	public static final int GAME_WIDE = 800;
	public static final int GAME_HEIGHT = 600;
	public static int totalNumber = 0;// ͣ��λ����Ŀ
	public static int totalCar = 0;// �ܷ��泵��
	public static int[] carpark;// ͣ��λռ�����
	public static int[] worker;
	public ArrayList<Car> clist = new ArrayList<Car>();
	public ArrayList<Car> clist2 = new ArrayList<Car>();
	public ArrayList<Car> celist = new ArrayList<Car>();
	public ArrayList<Car> cTime = new ArrayList<Car>();
	public ArrayList<Car> cAtBarrier = new ArrayList<Car>();
	public ArrayList<Car> falseCar = new ArrayList<Car>();//��ΪĳЩԭ���ܳ�ȥ�ĳ�
	public long startTime;// ���濪ʼʱ��
	public long totalTime;// �������ʱ��
	public static int outCarNumber = 0;// ����������Ŀ
	public static int totalParkTime = 0;// ��ͣ��ʱ��
	public static int averageParkTime = 0;// ƽ��ͣ��ʱ��
	public static int e = 0;// 10;//Ա������
	public static int totalParkNumber = 0;// ͣ��λ��Ŀ
	private Report re = new Report(this);
	public String rpts = "";
	public static int computeNumber=0;

	// public Car c = new Car(this);
	Car c;
	TheMain tm = this;
	Park p;
	Barrier be = new BarrierEnter(this);
	Barrier bo = new BarrierOut(this);
	Sensor se,so;

	Image offScreenImage = null;// �������������ͼ��

	public void paint(Graphics g) {
		totalTime = (System.currentTimeMillis() - startTime) / 1000;
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		g.drawString("��ǰʱ��      : " + df.format(day), 10, 40);
		g.drawString("����ʱ��      : " + totalTime, 10, 55);
		g.drawString("���泵��Ŀ : " + totalCar + "", 10, 70);
		g.drawString("���г�λ      : " + totalNumber + "", 10, 85);
		g.drawString("clist     : " + clist.size() + "", 10, 100);
		g.drawString("cAtBarrier     : " + cAtBarrier.size() + "", 10, 115);
		g.drawString("falseCar     : " + falseCar.size() + "", 10, 130);
		
		p.draw(g);
		for (int i = 0; i < totalParkNumber/2; i++) {
			Park pl = new Park(200 + (i + 1) * 45, 300, 40, 100, "place");
			pl.draw(g);
		}
		for (int i = 0; i < totalParkNumber/2; i++) {
			Park pl = new Park(200 + (i + 1) * 45, 450, 40, 100, "place");
			pl.draw(g);
		}
		
		se = new Sensor(210,420,tm);
		so = new Sensor(200 + (tm.totalParkNumber/2+2)*45+10,420,tm);
		se.draw(g);
		so.draw(g);
		be.draw(g);
		bo.draw(g);

		if(cAtBarrier.size()>0 && totalNumber>0){
			Car cab = cAtBarrier.get(0);
			/**
			 * ��������ͻ�������ǰ������վʱ�жϣ������ʱ�������г�Ҫ��ȥ��ȴ����Ƚ����
			 */
			long currentTime = System.currentTimeMillis();
			int xxx = tm.cTime.size();
			if (tm.cTime.size()>0)
				for (int i = 0; i < tm.cTime.size(); i++) {
					Car ccuren = tm.cTime.get(i);
					if (ccuren.getJudgeTime() == currentTime) {
						ccuren.setJudgeTime(3000);
					}
				}
			be.sendMessage(cab);
//			totalNumber--;
			cAtBarrier.remove(cab);
		}
		if (clist.size() > 0) {
			for (int i1 = 0; i1 < clist.size(); i1++) {
				c = clist.get(i1);
				c.draw(g);
			}
		}
//		charge();
		// c.moveIn();
	}

	public void update(Graphics g) {// Ϊ��ʵ��˫���壨double buffer��
		if (offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDE, GAME_HEIGHT);
		}
		Graphics gOffiScreen = offScreenImage.getGraphics();// ����ͼƬ�Ļ���

		Color c = gOffiScreen.getColor();
		gOffiScreen.setColor(Color.LIGHT_GRAY);
		gOffiScreen.fillRect(0, 0, GAME_WIDE, GAME_HEIGHT);
		gOffiScreen.setColor(c);

		paint(gOffiScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	public void launchFrame() {
		/**
		 * ���ļ��л�ȡ��Ϣ��Ȼ����������ʼ��
		 */
		getNumber();
		p = new Park(this);
		carpark = new int[totalNumber];
		worker = new int[e];
		for (int i = 0; i < totalNumber; i++) {
			carpark[i] = 0;
		}
		for (int i = 0; i < e; i++) {
			worker[i] = 0;
		}
		startTime = System.currentTimeMillis();
		
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s = df.format(day);
		s = s.replace("-", "");
		s = s.replace(":", "");
		rpts = s.replace(" ", "-");
//		re.writeReport(ds);
		
		
		/**
		 * ҳ������
		 */
		this.setLocation(300, 100);
		this.setSize(GAME_WIDE, GAME_HEIGHT);
		this.setTitle("CarPark");
		
		/**
		 * ������ر�ʱ���б����¼
		 */
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				
				re.writeReport(rpts);
				System.exit(0);
			}

		});

		this.setResizable(false);
		this.setBackground(Color.DARK_GRAY);

		this.addKeyListener(new KeyMonitor());

		setVisible(true);
		new Thread(new PaintThread()).start();
	}

	public void getNumber(){
		try {
			String encoding = "GBK";
			File file = new File("E:/CodePlace/Eclipse/CarPark/parkdata.txt");
			if (file.isFile() && file.exists()) { // �ж��ļ��Ƿ����
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// ���ǵ������ʽ
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				
				while ((lineTxt = bufferedReader.readLine()) != null) {
					System.out.println(lineTxt);
					String[] s = lineTxt.split("=");
					if(s[0].equals("e")){
						e = Integer.parseInt(s[1]);
					}else if(s[0].equals("n")){
						totalNumber = Integer.parseInt(s[1]) * 2;
						totalParkNumber = Integer.parseInt(s[1]) * 2;
					}
				}
				read.close();
			} else {
				System.out.println("�Ҳ���ָ�����ļ�");
			}
		} catch (Exception e) {
			System.out.println("��ȡ�ļ����ݳ���");
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {

		TheMain tc = new TheMain();
		tc.launchFrame();
	}

	private class PaintThread implements Runnable {
		public void run() {
			while (true) {
				/**
				 * ÿ��10���ӱ���һ��
				 */
				if(computeNumber == 10){
					System.out.println("ͣ�������� ��");
					System.out.println("��ǰ���ڳ��� �� "+ clist.size());
					System.out.println("�����ۼ��볡���� �� "+totalCar );
					System.out.println("�����ۼƳ������� �� "+ outCarNumber);
					System.out.println("���η���ƽ��ͣ��ʱ�� �� "+averageParkTime);
					computeNumber = 0;
				}
				repaint();// ���õ������װ���repaint����
				try {
					Thread.sleep(1000);
					computeNumber ++;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private class KeyMonitor extends KeyAdapter {
		//
		// public void keyReleased(KeyEvent e) {
		// myTank.keyRelesed(e);
		// }
		//
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_A) {// ��ӷ��泵��
				Car c = new Car(tm);
				totalCar++;
				clist.add(c);
				clist2.add(c);
			} else if (e.getKeyCode() == KeyEvent.VK_E ) {// ������ͣ����
				if(clist2.size()>0){
					celist.add(clist2.get(0));
//					clist2.remove(clist2.get(0));
				}
				
				String s = "û�г���Ҫ��վ";
//				s = c.keyPressed(e);
				if(celist.size()>0){
					s = celist.get(0).keyPressed(e);
					celist.remove(clist2.get(0));
					clist2.remove(clist2.get(0));
				}
					
				if (!s.equals("OK!"))
					JOptionPane.showMessageDialog(null, s);
			}else if(e.getKeyCode() == KeyEvent.VK_O){
				/**
				 * ��ģ������ʵ�ֵ������������վʱ��������Ա�Գ������в�����ʹ���������վ
				 */
				if(falseCar.size()>0){
					falseCar.get(0).falseCarOutPark();
					falseCar.remove(falseCar.get(0));
				}
				
			}
		}
	}
	/**
	 * ���ڳ���������ͻ�Ĵ���Զ���ȳ�������������
	 */
//	public void charge() {
//		for (int i = 0; i < cTime.size(); i++) {
//			Car charge = cTime.get(i);
//			int num = charge.parkNumber % 5;
//			for (int j = i + 1; j < cTime.size(); j++) {
//				Car ctest = cTime.get(j);
//				if (charge.judgeTime == ctest.judgeTime) {
//					int tnum = ctest.parkNumber % 5;
//					if (num <= tnum) {
//						charge.judgeTime += 2000;
//					} else {
//						ctest.judgeTime += 2000;
//					}
//				}
//			}
//		}
//	}
}
