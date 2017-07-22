<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.rocky.db.ProjectInfo" %>
<%@ page import="com.rocky.db.ProjectInfoDAO" %>
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
    <script src="js/ProjectUtils.js"></script>
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
        <div style="width:740px; margin: 80px auto 0;">
            <div class="page-header">
                <h2>Projects</h2>
            </div>
            <div class="createNewProject">
                <a href="CreateNewProject.jsp">Create New Project</a>
            </div>
            <div>
                <%
                    System.out.println("『User: " + (String) session.getAttribute("user"));
                    System.out.println("  Page: index』");

                    // 获取用户所有的project信息
                    String username = (String) session.getAttribute("user");
                    ArrayList<ProjectInfo> projectInfos = ProjectInfoDAO.queryProjectInfoByUsername(username);

                    // 获取下载工程要进入的路径
                    ServletContext context = pageContext.getServletContext();
                    String projectDownloadPath = context.getInitParameter("file-download") + session.getAttribute("MD5");

                    if (projectInfos.size() > 0) {
                        //输出表头
                        out.print("<table class='table table-hover'><thead><tr><th>Project ID</th><th>UpLoad Time</th><th>Download</th><th>Runnable</th><th>Operation</th></tr></thead><tbody>");

                        for (ProjectInfo projectInfo : projectInfos) {
                            //下载地址
                            String downloadURLItem = "\"" + "Projects/" + (String) session.getAttribute("user") + "/" + projectInfo.getProjectId() + "/" + projectInfo.getFileName().substring(0, projectInfo.getFileName().length() - 3) + "-min.js" + "\"";

                            out.print("<tr>");
                            out.print("<td>Project" + projectInfo.getProjectId() + "</td>");
                            out.print("<td>" + projectInfo.getDate() + "</td>");

                            out.print("<td> <a href=" + downloadURLItem + " download=" + "\"" + projectInfo.getFileName().substring(0, projectInfo.getFileName().length() - 3) + "-min.js" + "\"" + ">" + projectInfo.getFileName() + "</a></td>");
                            if (projectInfo.getRunnable())
                                out.print("<td> <input type=\"checkbox\" checked=\"checked\" onclick=\"updateRunnable('" + session.getAttribute("user") + "\',\'" + projectInfo.getProjectId() + "')\"></td>");
                            else
                                out.print("<td> <input type=\"checkbox\" onclick=\"updateRunnable('" + session.getAttribute("user") + "\',\'" + projectInfo.getProjectId() + "')\"></td>");
                            out.print("<td> <a href=\"javascript:void(0)\"; onclick=\"deleteProject('" + session.getAttribute("user") + "\',\'" + projectInfo.getProjectId() + "')\">Delete</a></td>");
                            out.print("</tr>");
                        }
                        out.print("</tbody></table>");
                    } else {
                        out.print("Please create a project to protect your applications.");
                    }


                %>
            </div>
        </div>
    </div>
    <div class="container" id="aeaoofnhgocdbnbeljkmbjdmhbcokfdb-mousedown">
        <div class="site-footer" role="contentinfo" id="" style="text-align:center;">
            &copy NWU-IRDETO IoT & Infosec Joint Lab <br> Updated on 7 July 2017
        </div><!-- /.site-footer -->
    </div>
</div>


</body>
</html>
