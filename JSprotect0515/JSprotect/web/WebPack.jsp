<%--
  Created by IntelliJ IDEA.
  User: Implementist
  Date: 2017/9/1
  Time: 9:36
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="obfu.copy.FileUtils" %>
<%@ page import="java.io.File" %>
<%@ page import="com.rocky.adbProject.Project" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%
    String userName = (String) session.getAttribute("user");
    String projectId = String.valueOf(new Project().getProjectId(userName));
    String fileName = (String) session.getAttribute("CurrentFile");
    fileName = fileName.substring(0, fileName.length() - 3)
            + "cpl.js";

    String elements = FileUtils.SERVER_ROOT_FOLDER + File.separator + "Projects";
    elements += "!@#";
    elements += userName;
    elements += "!@#";
    elements += projectId;
    elements += "!@#";
    elements += fileName;

    out.print(elements);
%>
