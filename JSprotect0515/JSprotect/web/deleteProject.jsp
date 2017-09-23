<%--
  Created by IntelliJ IDEA.
  User: Implementist
  Date: 2017/6/3
  Time: 10:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.rocky.db.*" %>
<%@ page import="obfu.copy.FileUtils" %>
<%@ page import="java.io.File" %>
<%
    // 获取输入的用户名和参数
    String username = request.getParameter("username");
    String projectId = request.getParameter("projectId");

    //删除服务器上的该文件夹
    String folderPath = FileUtils.getWholeDirectory(FileUtils.SERVER_ROOT_FOLDER, "Projects", username,projectId);

    File file = new File(folderPath);
    FileUtils.deleteFile(file);

    Boolean result = ProjectInfoDAO.deleteProjectInfoByUsernameAndProjectId(username, Integer.parseInt(projectId));
    if (result) {
        out.print(1);
    } else {
        out.print(2);
    }
%>
