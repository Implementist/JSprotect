package obfu.copy;

import java.io.*;

/**
 * Created by Administrator on 2017/8/23 0023.
 */
public class compile {
   public void compile(String userName, String fileName){
       File compiledFile = new File(FileUtils.getWholeDirectory(FileUtils.SERVER_ROOT_FOLDER, "node_modules",".bin"));
       File dir = compiledFile;
       System.out.println(dir);
       String cmd = this.constructCommandLine(userName, fileName);
       Runtime runtime = Runtime.getRuntime();
       try {
           Process process = runtime.exec(cmd, null, dir);
           System.err.print(loadStream(process.getErrorStream()));
           BufferedInputStream in = new BufferedInputStream(
                   process.getInputStream());
           BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
           String lineStr;

           while ((lineStr = inBr.readLine()) != null)
               // 获得命令执行后在控制台的输出信息
               System.out.println(lineStr);// 打印输出信息
           // 检查命令是否执行失败
           if (process.waitFor() != 0) {
               if (process.exitValue() == 1)// p.exitValue()==0表示正常结束，1：非正常结束
                   System.err.println("命令执行失败!");
           }
           inBr.close();
           in.close();

       } catch (InterruptedException | IOException ex) {
           ex.printStackTrace();
       }
   }

    static String loadStream(InputStream in) throws IOException {
        int ptr = 0;
        in = new BufferedInputStream(in);
        StringBuffer buffer = new StringBuffer();
        while( (ptr = in.read()) != -1 ) {
            buffer.append((char)ptr);
        }
        return buffer.toString();
    }

    public String constructCommandLine(String userName, String fileName) {
        int index;
        if((index = fileName.lastIndexOf('\\')) != -1){
            fileName = fileName.substring(index + 1, fileName.length());
        }

        String outputDirectory = FileUtils.getWholeDirectory(FileUtils.SERVER_ROOT_FOLDER, "Temp");
        File outputFolder = new File(outputDirectory);
        if (!outputFolder.exists())
            outputFolder.mkdirs();

        String stringbuffer = new String("");
        stringbuffer +="babel.cmd ";
        stringbuffer += FileUtils.getWholeFileName(fileName, FileUtils.SERVER_ROOT_UPLOAD_FOLDER, userName);
        stringbuffer += " -o ";
        stringbuffer += FileUtils.getWholeFileName("app-compile.js", FileUtils.SERVER_ROOT_FOLDER, "Temp");
        return stringbuffer;
    }
}
