<%--
  Created by IntelliJ IDEA.
  User: KKY
  Date: 2016/12/12
  Time: 11:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.rocky.db.*" %>
<%
    // 获取输入的用户名和参数
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    UserInfo userInfo = UserInfoDAO.queryUserInfoByUsername(username);
    if (null != userInfo && userInfo.getPassword().equals(password)) {
        session.setMaxInactiveInterval(1500);
        session.setAttribute("upLoadPath", username);
        session.setAttribute("login", "yes");
        session.setAttribute("user", username);

        switch (userInfo.getStatus()) {
            case 0:
                out.print("0");
                break;
            case 1:
                out.print("1");
                break;
        }
    } else {
        out.print("-1");
    }
%>
