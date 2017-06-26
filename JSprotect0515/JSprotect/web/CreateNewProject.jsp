<%--
  Created by IntelliJ IDEA.
  User: Implementist
  Date: 2017/06/21
  Time: 11:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
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
    <script src="js/bootstrap.js"></script>
    <script src="js/CreateNewProjectPageUtils.js"></script>
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

    <div class="ui container grid" style="width:740px; margin: 80px auto 0;">
        <div class="row">
            <div class="column">
                <div class="" id="root">
                    <div data-reactroot="">
                        <div class="ui grid">
                            <div class="column">
                                <iframe id="hidden_frame" name="hidden_frame" style="display:none"></iframe>
                                <form id="form1" style="margin: 0 auto;" action="doUpload.jsp" method="post" enctype="multipart/form-data" target="hidden_frame">
                                    <legend style="margin-bottom:50px;">Create New Project</legend>
                                    <div class="ui relaxed four column grid">
                                        <div class="column">
                                            <div class="ui basic segment">
                                                <div class="field">
                                                    <div class="ui checked checkbox">
                                                        <label>
                                                            <input name="chbBigArray" type="checkbox" tabindex="0"
                                                                   value="unchecked" onchange="reverseValue(document.getElementsByName('chbBigArray')[0])">大数组加壳
                                                        </label>
                                                    </div>
                                                </div>
                                                <div class="ui divider"></div>
                                                <div class="field">
                                                    <div class="ui checkbox" style="position: relative;top:50px">
                                                        <label>
                                                            <input name="chbComputation" type="checkbox" tabindex="0"
                                                                   value="unchecked"
                                                                   onchange="setDisabledOfComputationParam()">计算式
                                                        </label>
                                                    </div>
                                                </div>
                                                <div class="disabled field">
                                                    <label style="position: relative;top:45px;left:21px">强度</label>
                                                    <div class="ui input"
                                                         style="position: relative;top:45px;height: 10px">
                                                        <input name="txtComputationParam" type="number"
                                                               readonly="readonly" step="1" min="1" max="10" value="5"
                                                               style="width:64px;height: 10px">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="column">
                                            <div class="ui basic segment" style="position: relative;left: 61px">
                                                <div class="field">
                                                    <div class="ui checked checkbox">
                                                        <label>
                                                            <input name="chbControlFlowFlatten" type="checkbox"
                                                                   tabindex="0" value="unchecked"
                                                                   onchange="setDisabledOfFlattenParams()">控制流平展
                                                        </label>
                                                    </div>
                                                </div>
                                                <div class="disabled field">
                                                    <label style="position: relative;left: 20px">阈值</label>
                                                    <div class="ui input">
                                                        <input name="txtThresholdValue" type="text" value="0"
                                                               readonly="readonly" style="width: 91px">
                                                    </div>
                                                </div>
                                                <div class="disabled field">
                                                    <label style="position: relative;left: 20px">块大小</label>
                                                    <div class="ui input">
                                                        <input name="txtBlockSize" type="text" value="0"
                                                               readonly="readonly" style="width: 91px"><br>
                                                    </div>
                                                    <div style="font-size: 12px">
                                                        块大小应小于阈值
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="column">
                                            <div class="ui basic segment" style="position: relative;left: 104px">
                                                <div class="field">
                                                    <div class="ui checkbox">
                                                        <label>
                                                            <input name="chbStringAndNumber" type="checkbox"
                                                                   tabindex="0" value="unchecked"
                                                                   onchange="setDisabledOfStringAndNumber()">字符串、数字处理
                                                        </label>
                                                    </div>
                                                </div>
                                                <div class="disabled field">
                                                    <div class="ui checkbox">
                                                        <label>
                                                            <input name="chbString" type="checkbox" tabindex="0" value="unchecked"
                                                                   disabled="disabled" onchange="reverseValue(document.getElementsByName('chbString')[0])">字符串
                                                        </label>
                                                    </div>
                                                    <div class="ui checkbox">
                                                        <label>
                                                            <input name="chbNumber" type="checkbox" tabindex="0" value="unchecked"
                                                                   disabled="disabled" onchange="reverseValue(document.getElementsByName('chbNumber')[0])">数字
                                                        </label>
                                                    </div>
                                                </div>

                                                <div class="field">
                                                    <div class="ui checked checkbox">
                                                        <label>
                                                            <input name="chbParamName" type="checkbox" tabindex="0"
                                                                   value="unchecked" onchange="reverseValue(document.getElementsByName('chbParamName')[0])">属性名
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div style="text-align: center">
                                            <input type="file" name="upfile" size="200" accept="application" id="filesubmit">&nbsp;&nbsp;
                                            <input type="submit" class="btn" value="Submit" id="btnsubmit">
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div><!-- end of container -->

    <div class="container" style="margin-top: 100px;" id="aeaoofnhgocdbnbeljkmbjdmhbcokfdb-mousedown">
        <div class="site-footer" role="contentinfo" id="" style="text-align:center;">
            &copy NWU-IRDETO IoT & Infosec Joint Lab
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
