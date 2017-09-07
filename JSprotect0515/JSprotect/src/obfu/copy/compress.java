package obfu.copy;

import java.io.*;
public class compress {
    public void compress(String fileName){
        String cmd = this.constructCommandLine(fileName);
        Runtime runtime = Runtime.getRuntime();
        try {
            java.lang.Process process = runtime.exec(cmd);
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

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
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

    public String constructCommandLine(String fileName) {

        String stringbuffer = new String("");
        String compileFileName = fileName.substring(0, fileName.length() - 3)
                + "-min.js";

        stringbuffer += "cmd /c ";
        stringbuffer += (FileUtils.getWholeFileName("jsmin.exe",FileUtils.SERVER_ROOT_FOLDER,"lib") + " ");
        stringbuffer += "<" + fileName + "> ";
        stringbuffer += compileFileName;
        return stringbuffer.toString();
    }
}
