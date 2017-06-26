<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.rocky.db.Error" %>
<%@ page import="com.rocky.db.ErrorDAO" %>
<%
    if (session.getAttribute("user") == null)
        response.sendRedirect("login.jsp");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset='utf-8'>
    <title>JSProtect</title>
    <link href="css/signup1.css" rel="stylesheet" type="text/css"/>
    <link href="css/signup2.css" rel="stylesheet" type="text/css"/>
    <link href="css/index.css" rel="stylesheet" type="text/css"/>
    <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link rel="icon" href="img/logo.ico" type="image/x-icon">
    <script src="js/jquery.js"></script>
    <script src="js/bootstrap.js"></script>
    <script src="js/ErrorUtils.js"></script>
</head>


<body class="logged_out  env-production windows  signup">
<a href="#start-of-content" tabindex="1" class="accessibility-aid js-skip-to-content">Skip to content</a>
<div class="wrapper">
    <div class="header header-logged-out" role="banner">
        <div class="container clearfix">
            <a class="header-logo-wordmark">
                <span class="mega-octicon octicon-logo-github">JSProtect</span>
            </a>

            <div class="header-actions" role="navigation">
                <!--<a class="button primary" href="/join" data-ga-click="(Logged out) Header, clicked Sign up, text:sign-up">Sign up</a>
                  <a class="button" href="/login?return_to=%2Fjoin" data-ga-click="(Logged out) Header, clicked Sign in, text:sign-in">Sign in</a>-->
                <!-- <a class="header-nav-link name" href="#"></a> -->
                <ul class="nav nav-pills">
                    <li class="dropdown">
                        <a class="dropdown-toggle" id="drop4" role="button" data-toggle="dropdown"
                           href="#"><%=session.getAttribute("user")%> <b class="caret"></b></a>
                        <ul id="menu1" class="dropdown-menu" role="menu" aria-labelledby="drop4">
                            <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Settings</a></li>
                            <li role="presentation"><a role="menuitem" tabindex="-1" href="UserManagement.jsp">Users</a>
                            </li>
                            <li role="presentation"><a role="menuitem" tabindex="-1"
                                                       href="ErrorManagement.jsp">Errors</a></li>
                            <li role="presentation"><a role="menuitem" tabindex="-1" href="logout.jsp">Logout</a></li>
                        </ul>
                    </li>
                </ul>
            </div>

            <ul class="header-nav left" role="navigation">

                <li class="header-nav-item">
                    <a class="header-nav-link" href="/features"
                       data-ga-click="(Logged out) Header, go to features, text:features">Features</a>
                </li>
            </ul>

        </div>
    </div>
    <div class="container">
        <div style="width:740px; margin: 0 auto; margin-top: 80px;">
            <div class="page-header">
                <h2>Errors</h2>
            </div>
            <div>
                <%
                    System.out.println("『User: " + (String) session.getAttribute("user"));
                    System.out.println("  Page: ErrorManagement』");

                    ArrayList<Error> errors = ErrorDAO.queryAll();

                    if (errors.size() != 0) {

                        //输出表头
                        out.print("<table class='table table-hover'><thead><tr><th>Id</th><th>User</th><th>Project</th><th>Type</th><th>Content</th></tr></thead><tbody>");

                        for (Error error : errors) {
                            out.print("<tr>");
                            out.print("<td>" + error.getErrorId() + "</td>");
                            out.print("<td>" + error.getUsername() + "</td>");
                            out.print("<td>" + error.getProjectId() + "</td>");
                            out.print("<td>" + error.getErrorType() + "</td>");
                            out.print("<td> <a href=\"javascript:void(0)\"; onclick=\"showContent('" + error.getErrorContent() + "')\">Show</a></td>");
                            out.print("</tr>");
                        }
                        out.print("</tbody></table>");
                    } else {
                        out.print("There is no error to show.");
                    }


                %>
            </div>
        </div>
    </div>
    <div class="container" id="aeaoofnhgocdbnbeljkmbjdmhbcokfdb-mousedown">
        <div class="site-footer" role="contentinfo" id="" style="text-align:center;">
            &copy NWU-IRDETO IoT & Infosec Joint Lab
        </div><!-- /.site-footer -->
    </div>
</div>


</body>
</html>
