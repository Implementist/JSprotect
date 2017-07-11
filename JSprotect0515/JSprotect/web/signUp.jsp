<%--
  Created by IntelliJ IDEA.
  User: KKY
  Date: 2016/12/12
  Time: 11:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset='utf-8'>
    <title>Join JSProtect</title>
    <link href="css/signup1.css" rel="stylesheet" type="text/css" />
    <link href="css/signup2.css" rel="stylesheet" type="text/css" />
    <script src="js/createAccount.js"> </script>
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
                <a class="button primary" href="/join" data-ga-click="(Logged out) Header, clicked Sign up, text:sign-up">Sign up</a>
                <a class="button" href="login.jsp" data-ga-click="(Logged out) Header, clicked Sign in, text:sign-in">Sign in</a>
            </div>

            <ul class="header-nav left" role="navigation">
                <li class="header-nav-item">
                    <a class="header-nav-link" href="/explore" data-ga-click="(Logged out) Header, go to explore, text:explore">Explore</a>
                </li>
                <li class="header-nav-item">
                    <a class="header-nav-link" href="/features" data-ga-click="(Logged out) Header, go to features, text:features">Features</a>
                </li>
                <li class="header-nav-item">
                    <a class="header-nav-link" href="https://enterprise.github.com/" data-ga-click="(Logged out) Header, go to enterprise, text:enterprise">Enterprise</a>
                </li>
                <li class="header-nav-item">
                    <a class="header-nav-link" href="/blog" data-ga-click="(Logged out) Header, go to blog, text:blog">Blog</a>
                </li>
            </ul>

        </div>
    </div>



    <div id="start-of-content" class="accessibility-aid"></div>
    <div class="site clearfix" role="main">
        <div id="site-container" class="context-loader-container" data-pjax-container>



            <div class="setup-wrapper">

                <div class="setup-main ">
                    <div class="setup-form-container">
                        <noscript>

                        </noscript>

                        <form accept-charset="UTF-8" action="/" autocomplete="off" class="setup-form js-form-signup-detail" id="signup-form" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="2fNR1gsZ4UbgR54jSC1A966ysLUCUeYgfjMDttQf8doQKZKrg5S6ccLvAzVyUQEnexFLRD+Y0L0MTm8QnnmYQw==" /></div>

                            <h2 class="setup-form-title">
                                Create your personal account
                            </h2>

                            <dl class="form errored"><dt class="input-label"><div class="field-with-errors"><label autocapitalize="off" autofocus="autofocus" data-autocheck-url="/signup_check/username" for="user_login" name="user[login]">Username</label></div></dt><dd><div class="field-with-errors"><input autocapitalize="off" autofocus="autofocus" data-autocheck-url="/signup_check/username" id="user_login" name="user[login]" size="30" type="text" value="" /></div></dd></dl>

                            <dl class="form"><dt class="input-label"><div class="field-with-errors"><label autocapitalize="off" data-autocheck-url="/signup_check/email" for="user_email" name="user[email]">Email Address</label></div></dt><dd><div class="field-with-errors"><input autocapitalize="off" data-autocheck-url="/signup_check/email" id="user_email" name="user[email]" size="30" type="text" /></div><p class="note">You will occasionally receive account related emails. We promise not to share your email with anyone.</p></dd></dl>

                            <dl class="form errored"><dt class="input-label"><div class="field-with-errors"><label data-autocheck-url="/signup_check/password" for="user_password" name="user[password]">Password</label></div></dt><dd><div class="field-with-errors"><input data-autocheck-url="/signup_check/password" id="user_password" name="user[password]" size="30" type="password" /></div></dd></dl>

                            <dl class="form"><dt class="input-label"><label for="user_password_confirmation">Confirm your password</label></dt><dd><input id="user_password_confirmation" name="user[password_confirmation]" size="30" type="password" /></dd></dl>

                            <div class="form-actions">
                                <input type="button" class="button primary" id="signup_button" onclick="check()" value="Create an account">
                            </div>

                        </form>
                    </div> <!-- /.setup-form-container -->
                </div> <!-- /.setup-main -->

                <!--  <div class="setup-secondary">
                  <div class="setup-info-module">
                    <h2>You’ll love GitHub</h2>
                    <ul class="features-list">
                      <li><strong>Unlimited</strong> collaborators</li>
                      <li><strong>Unlimited</strong> public repositories</li>

                      <li class="list-divider"></li>

                      <li><span class="octicon octicon-check"></span> Great communication</li>
                      <li><span class="octicon octicon-check"></span> Friction-less development</li>
                      <li><span class="octicon octicon-check"></span> Open source community</li>
                    </ul>
                  </div>
                </div>-->
            </div>

        </div>
        <div class="modal-backdrop"></div>
    </div>
</div><!-- /.wrapper -->

<div class="container">

</div><!-- /.container -->


<div class="fullscreen-overlay js-fullscreen-overlay" id="fullscreen_overlay">
    <div class="fullscreen-container js-suggester-container">
        <div class="textarea-wrap">
            <textarea name="fullscreen-contents" id="fullscreen-contents" class="fullscreen-contents js-fullscreen-contents" placeholder=""></textarea>
            <div class="suggester-container">
                <div class="suggester fullscreen-suggester js-suggester js-navigation-container"></div>
            </div>
        </div>
    </div>
    <div class="fullscreen-sidebar">
        <a href="#" class="exit-fullscreen js-exit-fullscreen tooltipped tooltipped-w" aria-label="Exit Zen Mode">
            <span class="mega-octicon octicon-screen-normal"></span>
        </a>
        <a href="#" class="theme-switcher js-theme-switcher tooltipped tooltipped-w"
           aria-label="Switch themes">
            <span class="octicon octicon-color-mode"></span>
        </a>
    </div>
</div>



<div id="ajax-error-message" class="flash flash-error">
    <span class="octicon octicon-alert"></span>
    <a href="#" class="octicon octicon-x flash-close js-ajax-error-dismiss" aria-label="Dismiss error"></a>
    Something went wrong with that request. Please try again.
</div>
<div class="container" id="aeaoofnhgocdbnbeljkmbjdmhbcokfdb-mousedown">
    <div class="site-footer" role="contentinfo" id="" style="text-align:center;">
        &copy NWU-IRDETO IoT & Infosec Joint Lab <br> Updated on 7 July 2017
    </div><!-- /.site-footer -->
</div>


</body>
</html>
