import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class test{
	 public static void main(String[] args) {

		  FileOutputStream fis = null;
		    BufferedWriter bw = null;
		    String str="\r\n";
		    try {
		    
		     fis = new FileOutputStream("a.txt");
		     bw = new BufferedWriter(new OutputStreamWriter(fis));
		     for(int i=0;i<30;i++)
		     {
		      bw.write("看看这是第"+i+str);
		     }
		     bw.flush();
		     System.out.println("写入文件完成!");
		    } catch (FileNotFoundException e) {
		     e.printStackTrace();
		    } catch (IOException e) {
		     e.printStackTrace();
		    } finally{
		     try {
		      if(fis!=null)fis.close();    
		      if(bw!=null) bw.close();
		     } catch (IOException e) {
		      e.printStackTrace();
		     }
		    }
		 }
}