package obfu.copy;
import java.io.*;
//import java.io.File;


/**
 * Created by KKY on 2017/5/10.
 */
public class TryYUI {
    public static void main(String[] args) {
        //System.out.println("Hello World! Why so serious, I just wanna run a YUI test _(:蟹銆嶁垹)_!");
       // TryYUI();
    }

    public static void TryYUI(){
        //System.out.println("Surprise!");
        String targetFilename = "E:\\JSP\\project\\kky\\tdc.js";
         compress(targetFilename);
        //System.out.println("Happy ending! _(:蟹銆嶁垹)_!");
    }

    public static void compress(String filename){

         System.out.println(filename);

        System.out.println("Do something for "+filename);

        File compressFile = new File(filename);
        File dir = compressFile.getParentFile();

        String cmd = constructCommandLine(filename);
        System.out.println("I got the cmd "+cmd);
        Runtime runtime = Runtime.getRuntime();

        try{
            java.lang.Process process = runtime.exec(cmd, null, dir);
            System.out.println("___+++++++");
            BufferedInputStream in = new BufferedInputStream(process.getInputStream());
            System.out.println("___+++++++");
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
            System.out.println("___+++++++");
//C:\Program Files\Apache Software Foundation\Tomcat 7.0\webapps\JSprotect\WEB-INF\lib
            File file = new File("D:\\Ideaworkspace\\JSprotect0515\\JSprotect\\web\\WEB-INF\\lib\\yuicompressor-2.4.8.jar");
            //File file = new File("D:\\workspace\\java\\JSprotect\\WebContent\\WEB-INF\\lib\\yuicompressor-2.4.8.jar");
            //D:\self\jsconcern\JSprotect0515\JSprotect\web\WEB-INF\lib
            int byteread = 0;
            int bytesum = 0;
            if (file.exists()) {
                //C:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\webapps\\JSprotect\\WebContent\\WEB-INF\\lib\\yuicompressor-2.4.8.jar
                InputStream inStream = new FileInputStream("D:\\Ideaworkspace\\JSprotect0515\\JSprotect\\web\\WEB-INF\\lib\\yuicompressor-2.4.8.jar");

                FileOutputStream fs = new FileOutputStream(dir + File.separator + "yuicompressor-2.4.8.jar");
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 瀛楄妭鏁� 鏂囦欢澶у皬
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }  else{System.out.println("xxx");}

            String lineStr;

            while ((lineStr = inBr.readLine()) != null)// 鑾峰緱鍛戒护鎵ц鍚庡湪鎺у埗鍙扮殑杈撳嚭淇℃伅
                System.out.println(lineStr);// 鎵撳嵃杈撳嚭淇℃伅
            // 妫�鏌ュ懡浠ゆ槸鍚︽墽琛屽け璐�
            if (process.waitFor() != 0) {
                if (process.exitValue() == 1)// p.exitValue()==0琛ㄧず姝ｅ父缁撴潫锛�1锛氶潪姝ｅ父缁撴潫
                    System.err.println("error!");
            }
            inBr.close();
            in.close();

            File newfile = new File(dir+File.separator + "yuicompressor-2.4.8.jar");
            if(newfile.exists()){
                newfile.delete();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String constructCommandLine(String fileName) {
        //System.out.println(x);
        String temp[]=fileName.split("\\\\");
        System.out.println(temp.length);
        //String usert=temp[temp.length-1];
        String usert=temp[4];
        System.out.println(usert+"++++++");
        int index;
        if((index = fileName.lastIndexOf('\\')) != -1){
            //fileName = fileName.substring(index + 1, fileName.length());
        }
        String stringbuffer = new String("java -jar yuicompressor-2.4.8.jar ");
        System.out.println(fileName+"===========");
        String minifiedFileName =  "t.js";
        stringbuffer += fileName;
        //stringbuffer += " --nomunge  --preserve-semi --disable-optimizations -o ";//娌℃湁鍙橀噺骞剁簿绠�鏇挎崲娣锋穯
        stringbuffer += " --preserve-semi --disable-optimizations -o ";
        stringbuffer += minifiedFileName;
        return stringbuffer.toString();
    }
}
