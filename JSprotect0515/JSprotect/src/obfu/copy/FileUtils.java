package obfu.copy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Implementist on 2017/6/26.
 */
public abstract class FileUtils {
//    public static void main(String[] args) {
//        System.out.println(FileUtils.getWholeFileName("UserDAO.java",
//                SERVER_ROOT_FOLDER, "Projects"));
//    }

    /**
     * Tomcat服务器ROOT文件夹的绝对路径
     */
    public static final String SERVER_ROOT_FOLDER = "C:" +
            File.separator +
            "Program Files" +
            File.separator +
            "Apache Software Foundation" +
            File.separator +
            "Tomcat 7.0" +
            File.separator +
            "webapps" +
            File.separator +
            "ROOT";

    public static final String C_PROJECTS_FOLDER = "C:" +
            File.separator +
            "Projects";

    /**
     * 获取完整的路径
     *
     * @param folders 文件夹
     * @return 完整的路径
     */
    public static String getWholeDirectory(String... folders) {
        StringBuilder result = new StringBuilder();
        for (String folder : folders) {
            result.append(folder).append(File.separator);
        }
        return result.toString();
    }

    /**
     * 获取完整的文件名
     *
     * @param fileName 文件名
     * @param folder   根目录
     * @return 完整的文件名
     */
    public static String getWholeFileName(String fileName, String folder) {
        return folder +
                File.separator +
                fileName;

    }

    /**
     * 获取完整的文件名
     *
     * @param fileName 文件名
     * @param folders  每一级目录
     * @return 完整的文件名
     */
    public static String getWholeFileName(String fileName, String... folders) {
        StringBuilder result = new StringBuilder();
        for (String folder : folders) {
            result.append(folder).append(File.separator);
        }
        result.append(fileName);
        return result.toString();
    }

    /**
     * 移动文件
     *
     * @param source 原路径
     * @param target 目标路径
     */
    public static void moveFile(String source, String target) {
        copyFolder(source, target);
        deleteFolder(source);
    }

    /**
     * 复制整个文件夹内容
     *
     * @param source String  原文件路径
     * @param target String  复制后路径
     */
    public static void copyFolder(String source, String target) {

        try {
            (new File(target)).mkdirs();  //如果文件夹不存在  则建立新文件夹
            File files = new File(source);
            File temp;
            for (String file : files.list()) {
                if (source.endsWith(File.separator)) {
                    temp = new File(source + file);
                } else {
                    temp = new File(source + File.separator + file);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(target + "/" +
                            (temp.getName()));
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(source + File.separator + file, target + File.separator + file);
                }
            }
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 删除文件夹
     *
     * @param directory 路径
     */
    public static void deleteFolder(String directory) {
        try {
            deleteAllFiles(directory);  //删除完里面所有内容
            File file = new File(directory);
            file.delete();  //删除空文件夹

        } catch (Exception e) {
            System.out.println("删除文件夹操作出错");
            e.printStackTrace();

        }

    }

    /**
     * 删除文件夹里面的所有文件
     *
     * @param directory 路径
     */
    public static void deleteAllFiles(String directory) {
        File folder = new File(directory);
        if (!folder.exists()) {
            return;
        }
        if (!folder.isDirectory()) {
            return;
        }
        String[] files = folder.list();
        File temp;
        if (files != null) {
            for (String file : files) {
                if (directory.endsWith(File.separator)) {
                    temp = new File(directory + file);
                } else {
                    temp = new File(directory + File.separator + file);
                }
                if (temp.isFile()) {
                    temp.delete();
                }
                if (temp.isDirectory()) {
                    deleteAllFiles(directory + File.separator + file);//先删除文件夹里面的文件
                    deleteFolder(directory + File.separator + file);//再删除空文件夹
                }
            }
        }
    }
}
