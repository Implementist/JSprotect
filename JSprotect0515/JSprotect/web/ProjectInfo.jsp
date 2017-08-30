<%--
  Created by IntelliJ IDEA.
  User: KKY
  Date: 2016/12/12
  Time: 11:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page import="com.rocky.adbProject.*" %>
<%@ page import="com.rocky.db.ProjectInfo" %>
<%
    if (session.getAttribute("user") == null)
        response.sendRedirect("login.jsp");

    Project project = new Project();
    ProjectInfo projectInfo = project.getOneProjectInfo((String) session.getAttribute("user"), Integer.parseInt(request.getParameter("id")));
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset='utf-8'>
    <title>JSProtect(Create new project)</title>
    <link href="css/signup1.css" rel="stylesheet" type="text/css"/>
    <link href="css/signup2.css" rel="stylesheet" type="text/css"/>
    <link href="css/index.css" rel="stylesheet" type="text/css"/>
    <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="css/createNewProject.css" rel="stylesheet" type="text/css"/>
    <link rel="icon" href="img/logo.ico" type="image/x-icon">
    <script src="js/uupLoadProject.js"></script>
    <script src="js/jquery.js"></script>
</head>
<body class="logged_out  env-production windows  signup">
<a href="#start-of-content" tabindex="1" class="accessibility-aid js-skip-to-content">Skip to content</a>
<div class="wrapper" id="wrapper">
    <div class="header header-logged-out" role="banner">
        <div class="container clearfix">
            <a class="header-logo-wordmark">
                <span class="mega-octicon octicon-logo-github">JSProtect</span>
            </a>

            <div class="header-actions" role="navigation">
                <!--<a class="button primary" href="/join" data-ga-click="(Logged out) Header, clicked Sign up, text:sign-up">Sign up</a>
                  <a class="button" href="/login?return_to=%2Fjoin" data-ga-click="(Logged out) Header, clicked Sign in, text:sign-in">Sign in</a>-->
                <a class="header-nav-link name" href="#"><%=session.getAttribute("user")%>
                </a>
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
        <div style="width:740px; margin: 0 auto; margin-top: 80px; ">
            <legend style="margin-bottom:50px;">Project Information</legend>
            <div>
                <table class="table table-condensed" style="margin: 0 auto; width:95%; font-weight:bold;">
                    <tr>
                        <td>Poject ID</td>
                        <td><%=request.getParameter("id")%>
                        </td>
                    </tr>
                    <tr>
                        <td>UpLoad Time</td>
                        <td><%=projectInfo.getDate().toString()%>
                        </td>
                    </tr>
                    <tr>
                        <td>Protection Options</td>
                        <td>Antidbg, Anti-Tamper, Obfuscation</td>
                    </tr>
                    <tr>
                        <td>Obfuscation Strength</td>
                        <td><%=projectInfo.getObfuscationStrength()%>
                        </td>
                    </tr>
                    <tr>
                        <td>Flatten Control flow</td>
                        <td><%=projectInfo.getFlatternCount()%>
                        </td>
                    </tr>
                    <tr>
                        <td>Opaque Predicates</td>
                        <td><%=projectInfo.getOpaqueCount()%>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td></td>
                    </tr>

                </table>
            </div>
        </div>
    </div>
    <!--<div class="container">
        <div style="width:740px; margin: 0 auto; margin-top: 80px; ">
            <iframe id="hidden_frame" name="hidden_frame" style="display:none"></iframe>
          <form id="form1" style="width:700px; margin: 0 auto;" action="Obfuscation.jsp" method="post" enctype="multipart/form-data" target="hidden_frame">
          <legend style="margin-bottom:50px;">Project Information</legend>
          <label class="checkbox" style="padding-left:70px;margin-bottom:20px;"><input type="checkbox" value="1" id="adb">Anti debugging</label>
          <label class="checkbox" style="padding-left:70px;margin-bottom:20px;"><input type="checkbox" value="2" id="obf">Obfuscation</label>
          <label class="checkbox"style="padding-left:70px;margin-bottom:20px;"> <input type="checkbox" value="3" id="atp">Anti Tamper</label>
            <input style="padding-left:50px;margin-bottom:20px;" type="file" name = "upfile" size = "200" accept="application/zip" id="filesubmit"><br>
            <input style="margin-left:50px" type="submit" class="btn" value="Submit" id="btnsubmit">
            </form>
        </div> end of container -->


</div>
<div class="container" style="margin-top: 100px;" id="aeaoofnhgocdbnbeljkmbjdmhbcokfdb-mousedown">
    <div class="site-footer" role="contentinfo" id="" style="text-align:center;">
        &copy NWU-IRDETO IoT & Infosec Joint Lab <br> Updated on 7 July 2017
    </div><!-- /.site-footer -->
</div>
</div>
<div id="info">
    <div id="top">
        <h1 id="header">Information</h1>
    </div>
    <div id="bottom">
        <p>The projects has been protected successfully, please download!</p>
        <div id="confirmbtn">
            <button class="btn btn-info" onClick="confirmOperation()">Confirm</button>
        </div>
    </div>
</div><!-- end of info -->
<script>
    document.getElementById("info").style.display = "none";
</script>
</body>
</html>
