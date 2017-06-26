package obfu.copy;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class compress {
    public void compress(String directory) {

        File compressedFile = new File(directory);
        File dir = compressedFile.getParentFile();

        String cmd = this.constructCommandLine(directory);
        System.out.println(cmd);
        Runtime runtime = Runtime.getRuntime();

        try {
            java.lang.Process process = runtime.exec(cmd, null, dir);
            System.err.print(loadStream(process.getErrorStream()));
            BufferedInputStream in = new BufferedInputStream(
                    process.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));

            File file = new File("." + File.separator + "lib" + File.separator + "yuicompressor-2.4.7.jar");

            int byteread = 0;
            int bytesum = 0;
            if (file.exists()) {
                InputStream inStream = new FileInputStream("yuicompressor-2.4.7.jar"); // 读入原文件
                FileOutputStream fs = new FileOutputStream(dir + File.separator + "yuicompressor-2.4.7.jar");
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }

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

            File newfile = new File(dir + File.separator + "yuicompressor-2.4.7.jar");
            if (newfile.exists()) {
                newfile.delete();
            }

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // this.changeOriginalFile(fileName+"-out.js");
    }

    static String loadStream(InputStream in) throws IOException {
        int ptr = 0;
        in = new BufferedInputStream(in);
        StringBuffer buffer = new StringBuffer();
        while ((ptr = in.read()) != -1) {
            buffer.append((char) ptr);
        }
        return buffer.toString();
    }

    /**
     * @param directory 使用YUICompressor待压缩的文件
     * @return 压缩后的命令行输出
     */
    public String constructCommandLine(String directory) {
        int index;
        String cutedDir = directory.substring(0, directory.lastIndexOf('\\'));
        System.out.println("This is the result: " + cutedDir);
        File file = new File(cutedDir);
        file.mkdirs();
        if ((index = directory.lastIndexOf('\\')) != -1) {
            directory = directory.substring(index + 1, directory.length());
        }
        System.out.println(directory);
        String stringbuffer = new String("java -jar " + "C:/JSprotectLib/yuicompressor-2.4.7.jar ");
        String minifiedFileName = directory.substring(0, directory.length() - 3) + "-min.js";
        stringbuffer += directory;
        stringbuffer += " --nomunge  --preserve-semi --disable-optimizations -o ";

        stringbuffer += cutedDir  + File.separator + minifiedFileName;
        return stringbuffer;
    }
}