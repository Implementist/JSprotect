<%--
  Created by IntelliJ IDEA.
  User: Implementist
  Date: 2017/8/30
  Time: 8:59
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="com.rocky.adbProject.Project" %>
<%@ page import="obfu.copy.DealProperty_NEW" %>
<%@ page import="obfu.copy.FileUtils" %>
<%@ page import="obfu.copy.compile" %>
<%@ page import="obfu.copy.webpack" %>
<%@ page import="org.apache.commons.fileupload.FileItem" %>
<%@ page import="org.apache.commons.fileupload.disk.DiskFileItemFactory" %>
<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%
    String userName = (String) session.getAttribute("user");

    // 上传文件的目录
    String uploadDirectory = FileUtils.getWholeDirectory(FileUtils.SERVER_ROOT_UPLOAD_FOLDER, userName);

    // 临时文件目录
    String tempDirectory = FileUtils.getWholeDirectory(FileUtils.SERVER_ROOT_FOLDER, "Temp");

    try {
        File uploadFile = new File(uploadDirectory);
        if (!uploadFile.exists()) {
            uploadFile.mkdirs();
        }
        File tempFile = new File(tempDirectory);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }

        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // 设置缓冲区大小，这里是4KB
        factory.setSizeThreshold(4096);

        // 设置缓冲区目录
        factory.setRepository(tempFile);

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        // 设置最大文件尺寸，这里是4MB
        upload.setSizeMax(4194304);

        String fileName = "";

        //transcode值指示的是是否将JS代码转码（高版本转到版本5）
        int transcode = 0;
        session.setAttribute("Transcode", "0");

        // 得到所有的文件
        List<FileItem> items = upload.parseRequest(request);
        for (FileItem fileItem : items) {

            if (fileItem.isFormField() && fileItem.getFieldName().equals("chbTranscode")) {
                transcode = fileItem.getString().equals("checked") ? 1 : 0;
                session.setAttribute("Transcode", String.valueOf(transcode));
            }

            fileName = fileItem.getName();
            if (fileName != null) {
                session.setAttribute("CurrentFile", fileName);
                File fullFile = new File(fileItem.getName());
                File savedFile = new File(uploadDirectory, fullFile.getName());
                fileItem.write(savedFile);
            }
        }

        byte buf[] = new byte[10000];
        try {
            String path = FileUtils.getWholeFileName((String) session.getAttribute("CurrentFile"), FileUtils.SERVER_ROOT_UPLOAD_FOLDER, userName);
            File fp = new File(path);
            FileInputStream fistream = new FileInputStream(fp);
            int bytesum = fistream.read(buf, 0, 10000);
            String str_file = new String(buf, 0, bytesum);
            fistream.close();
            session.setAttribute("context", str_file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 生成该用户的ProjectId
        Project project = new Project();
        session.setAttribute("CurrentProjectId", project.getProjectId(userName) + "");

        if (transcode == 1) {
            compile com = new compile();
            com.compile(userName, (String) session.getAttribute("CurrentFile"));
            webpack web = new webpack();
            web.webpack();
        }

        DealProperty_NEW dealProperty_new = new DealProperty_NEW();

        String fullFileName;
        if (((String) session.getAttribute("Transcode")).equals("1"))
            fullFileName = FileUtils.getWholeFileName("app-webpack.js", FileUtils.SERVER_ROOT_FOLDER, "Temp");
        else
            fullFileName = FileUtils.getWholeFileName((String) session.getAttribute("CurrentFile"),
                    FileUtils.SERVER_ROOT_UPLOAD_FOLDER, (String) session.getAttribute("user"));

        dealProperty_new.DealPropertyName(fullFileName);

        //属性名
        ArrayList<String> propertyNames = dealProperty_new.getPropertyNameList();
        StringBuilder propertyNameString = new StringBuilder();
        for (String s : propertyNames)
            propertyNameString.append(s).append(" ");
        session.setAttribute("CurrentPropertyNameString", propertyNameString);

        //字符串
        ArrayList<String> strings = dealProperty_new.getStringList();
        StringBuilder stringString = new StringBuilder();
        for (String s : strings)
            stringString.append(s).append(" ");
        session.setAttribute("CurrentStringString", stringString);

        //字符串详情
        ArrayList<String> stringDetails = dealProperty_new.getStringEn();
        StringBuilder stringDetailString = new StringBuilder();
        for (String s : stringDetails) {
            stringDetailString.append(s).append("!@#");
        }
        session.setAttribute("CurrentStringDetailString", stringDetailString);

        response.sendRedirect("CreateNewProject.jsp");
    } catch (Exception e) {
        e.printStackTrace();
        //TODO:跳转错误页面或者弹框报错
    }
%>
