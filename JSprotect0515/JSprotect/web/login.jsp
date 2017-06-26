<%--
  Created by IntelliJ IDEA.
  User: KKY
  Date: 2016/12/12
  Time: 11:22
  To change this template use File | Settings | File Templates.
--%>
<%
    if (session.getAttribute("user") != null)
        response.sendRedirect("index.jsp");
%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <script src="js/login.js"></script>
    <link href="css/login.css" rel="stylesheet">
    <link rel="icon" href="img/logo.ico" type="image/x-icon">
</head>
<body class="logged_out  env-production windows">
<div class="wrapper">
    <div class="header header-logged-out" role="banner">
        <div class="container clearfix">
            <a class="header-logo-wordmark">
                <span class="mega-octicon octicon-logo-github">JSProtect</span>
            </a>
            <div class="header-actions" role="navigation">
                <a class="button primary" href="signUp.jsp"
                   data-ga-click="(Logged out) Header, clicked Sign up, text:sign-up" id="">Sign up</a>
                <a class="button" href="#" data-ga-click="(Logged out) Header, clicked Sign in, text:sign-in">Sign
                    in</a>
            </div>

            <ul class="header-nav left" role="navigation">

                <li class="header-nav-item">
                    <a class="header-nav-link" href="/features"
                       data-ga-click="(Logged out) Header, go to features, text:features">Features</a>
                </li>

            </ul>
        </div>
    </div>
    <div id="start-of-content" class="accessibility-aid"></div>
    <div class="site clearfix" role="main">
        <div id="site-container" class="context-loader-container" data-pjax-container="">
            <div class="auth-form" id="log">
                <form accept-charset="UTF-8">
                    <div style="margin:0;padding:0;display:inline">
                        <input name="utf8" type="hidden" value="&#x2713;"/>
                        <input name="authenticity_token" type="hidden"
                               value="KrjEACb0B4hZ4kybQU7qEhPxJYHu8trtcYaI/Z9XjZYqzgCEnFuiK+usTH5mI4IrSZJG1tFx6pJ95egScbLYdw=="/>
                    </div>
                    <div class="auth-form-header">
                        <h1>Sign in</h1>
                    </div>

                    <div class="auth-form-body">
                        <label for="login_field">Username or Email</label>
                        <input autocapitalize="off" autocorrect="off" autofocus="autofocus" class="input-block"
                               id="login_field" name="login" tabindex="1" type="text"/>
                        <label for="password">Password <a href="/password_reset">(forgot password)</a></label>
                        <input class="input-block" id="password" name="password" tabindex="2" type="password"/>
                        <input class="button" data-disable-with="Signing in…" name="commit" tabindex="3" type="button"
                               value="Sign in" onclick="query()"/>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<div class="container" id="aeaoofnhgocdbnbeljkmbjdmhbcokfdb-mousedown">
    <div class="site-footer" role="contentinfo">
        &copy NWU-IRDETO IoT & Infosec Joint Lab
    </div><!-- /.site-footer -->
</div>
</body>
</html>
