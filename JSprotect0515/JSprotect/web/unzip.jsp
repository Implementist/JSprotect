<%--
  Created by IntelliJ IDEA.
  User: KKY
  Date: 2016/12/12
  Time: 11:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ page import="java.util.zip.ZipEntry"%>
<%@ page import="java.util.zip.ZipFile"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.Enumeration"%>
<%
    try {
        // filePath代表要解压的文件, 而outPath代表要解压的路径
        String filePath = "C:\\apache-tomcat-7.0.55\\webapps\\data\\temp.zip";
        String outPath = "C:\\apache-tomcat-7.0.55\\webapps\\data";

        // 缓冲区设置为2048
        int bufferSize = 2048;

        // 创建ZipFile对象, 修饰该Zip文件, 进行解压操作; 并获取该Zip文件中所有的项至names中
        ZipFile zipFile = new ZipFile(filePath);
        Enumeration names = zipFile.entries();

        while (names.hasMoreElements())
        {

            ZipEntry entry = (ZipEntry) names.nextElement();

            // 对目录项不进行处理, 只需在目录中文件创建时判断其父路径是否存在, 若不存在, 则创建该父路径即可
            if (!entry.isDirectory())
            {

                InputStream readStream = zipFile.getInputStream(entry);

                if (readStream != null)
                {
                    // 创建解压后的文件对象
                    File file = new File(outPath + File.separator
                            + entry.getName());

                    // 若文件所在目录不存在, 则创建该目录
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdir();
                    }

                    // 同时利用解压后的文件对象, 构造文件输出流
                    FileOutputStream outStream = new FileOutputStream(
                            file);

                    // 利用 count来判断文件是否读取到了尾部, 用data开辟一段空间, 来当做
                    // 缓冲池, 暂存每次读出的数据, 并写入到输出流
                    int count;
                    byte[] data = new byte[bufferSize];

                    // read the data from the entry and output to the unzip file stream
                    while ((count = readStream.read(data, 0, bufferSize)) != -1)
                        outStream.write(data, 0, count);

                    // close the input and output stream
                    outStream.close();
                    readStream.close();
                }
            }
        }
        zipFile.close();
    } catch (Exception ex) {
        System.out.println(ex);
    }
%>
