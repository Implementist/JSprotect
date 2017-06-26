<%--
  Created by IntelliJ IDEA.
  User: Implementist
  Date: 2017/6/3
  Time: 10:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.rocky.db.*" %>
<%
    // 获取输入的用户名和参数
    String username = request.getParameter("username");
    String projectId = request.getParameter("projectId");

    Boolean result = ProjectInfoDAO.deleteProjectInfoByUsernameAndProjectId(username, projectId);
    if (result) {
        out.print(1);
    } else {
        out.print(2);
    }
%>
