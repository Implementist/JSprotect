<%--
  Created by IntelliJ IDEA.
  User: KKY
  Date: 2016/12/12
  Time: 11:18
  To change this template use File | Settings | File Templates.
--%>
<%@page import="com.rocky.db.*" %>
<%

    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String emailAddress = request.getParameter("emailAddress");

    if (null != UserInfoDAO.queryUserInfoByUsername(username)) {
        //构造用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(password);
        userInfo.setEmailAddress(emailAddress);

        //向数据库中插入用户信息
        UserInfoDAO.insertUserInfo(userInfo);

        out.print("SUCC_CREATE");
    } else {
        out.print("EXIST");
    }
%>
