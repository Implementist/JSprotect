<%--
  Created by IntelliJ IDEA.
  User: Implementist
  Date: 2017/6/7
  Time: 9:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.rocky.db.UserInfoDAO" %>
<%@ page import="com.rocky.db.UserInfo" %>
<%
    // 获取输入的用户名和参数
    String username = request.getParameter("username");

    UserInfo userInfo = UserInfoDAO.queryUserInfoByUsername(username);

    Boolean result = false;

    if (null != userInfo) {
        if (userInfo.getStatus() == 0) {
            result = UserInfoDAO.updateStatusByUsername(username, 1);
        } else {
            result = UserInfoDAO.updateStatusByUsername(username, 0);
        }
    }

    if (result) {
        out.print(1);
    } else {
        out.print(2);
    }
%>
