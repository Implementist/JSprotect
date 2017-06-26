<%--
  Created by IntelliJ IDEA.
  User: Implementist
  Date: 2017/6/6
  Time: 15:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.rocky.db.*" %>
<%
    // 获取输入的用户名和参数
    String username = request.getParameter("username");

    Boolean result = UserInfoDAO.deleteUserInfoByUsername(username);
    if (result) {
        out.print(1);
    } else {
        out.print(2);
    }
%>
