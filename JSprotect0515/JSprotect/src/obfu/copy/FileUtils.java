package obfu.copy;

import java.io.File;

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
            "XAMPP" +
            File.separator +
            "tomcat" +
            File.separator +
            "webapps" +
            File.separator +
            "ROOT";

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
}
