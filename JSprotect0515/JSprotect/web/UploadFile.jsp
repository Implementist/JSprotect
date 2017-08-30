<%--
  Created by IntelliJ IDEA.
  User: Implementist
  Date: 2017/8/30
  Time: 8:59
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="obfu.copy.FileUtils" %>
<%@ page import="org.apache.commons.fileupload.FileItem" %>
<%@ page import="org.apache.commons.fileupload.disk.DiskFileItemFactory" %>
<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload" %>
<%@ page import="java.io.File" %>
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

        // 得到所有的文件
        List<FileItem> items = upload.parseRequest(request);
        for (FileItem fileItem : items) {
            fileName = fileItem.getName();
            if (fileName != null) {
                session.setAttribute("CurrentFile", fileName);
                File fullFile = new File(fileItem.getName());
                File savedFile = new File(uploadDirectory, fullFile.getName());
                fileItem.write(savedFile);
            }
        }
        response.sendRedirect("CreateNewProject.jsp");
    } catch (Exception e) {
        // 可以跳转出错页面
        e.printStackTrace();
    }
%>
