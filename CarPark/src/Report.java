import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author RYL
 * 
 *         该类是用来进行报告记录的，每次退出仿真则进行报告记录
 */
public class Report {
	private int endTime;
	
	TheMain tm;

	public Report(TheMain tm) {
		this.tm = tm;
	}

	public int getEndTime() {
		return endTime;
	}

	public void writeReport(String s) {
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String data = df.format(day);
		String ds[] = data.split(" ");
		String fileName = s + "-" + ds[1].replace(":", "") + ".rpt";
		String filePath = "E:/CodePlace/Eclipse/CarPark/" + fileName;
		File file = new File(filePath);
		// tm.averageParkTime = tm.totalParkTime / tm.outCarNumber;
		FileOutputStream fis = null;
		BufferedWriter bw = null;
		try {

			fis = new FileOutputStream(filePath);
			bw = new BufferedWriter(new OutputStreamWriter(fis));
			bw.write("The totalCar number is " + tm.totalCar);
			bw.newLine();
			bw.flush();
			bw.write("The outCarNumber number is " + tm.outCarNumber);
			bw.newLine();
			bw.flush();
			bw.write("The averageParkTime number is " + tm.averageParkTime);
			bw.newLine();
			bw.flush();
			System.out.println("写入文件完成!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
				if (bw != null)
					bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
