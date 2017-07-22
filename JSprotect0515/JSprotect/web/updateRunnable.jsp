<%--
  Created by IntelliJ IDEA.
  User: Implementist
  Date: 2017/6/16
  Time: 10:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.rocky.db.*" %>
<%
    // 获取输入的用户名和参数
    String username = request.getParameter("username");
    String projectId = request.getParameter("projectId");

    Boolean result = ProjectInfoDAO.updateRunnableByUsernameAndProjectId(username,Integer.parseInt(projectId));
    if (result) {
        out.print(1);
    } else {
        out.print(2);
    }
%>
