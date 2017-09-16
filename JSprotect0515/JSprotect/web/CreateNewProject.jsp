<%--
  Created by IntelliJ IDEA.
  User: Implementist
  Date: 2017/06/21
  Time: 11:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
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
    <script type="text/javascript" src="js/bootstrap-filestyle.js"></script>
    <script src="js/uupLoadProject.js"></script>
    <script src="js/jquery.js"></script>
    <script src="js/bootstrap.js"></script>
    <script src="js/CreateNewProjectPageUtils.js"></script>
    <script type="text/javascript">
        function checkchange() {
            var btn1 = document.getElementById("analy");
            var btn2 = document.getElementById("btall1");
            var btn3 = document.getElementById("btno1");
            var btn4 = document.getElementById("btob1");
            var btn5 = document.getElementById("btall2");
            var btn6 = document.getElementById("btno2");
            var btn7 = document.getElementById("btob2");
            var tag = document.getElementById("chpro");
            if (tag.checked === true) {
                btn1.removeAttribute("disabled");
                btn2.removeAttribute("disabled");
                btn3.removeAttribute("disabled");
                btn4.removeAttribute("disabled");
                btn5.removeAttribute("disabled");
                btn6.removeAttribute("disabled");
                btn7.removeAttribute("disabled");
            }
            else {
                btn1.setAttribute("disabled", true);
                btn2.setAttribute("disabled", true);
                btn3.setAttribute("disabled", true);
                btn4.setAttribute("disabled", true);
                btn5.setAttribute("disabled", true);
                btn6.setAttribute("disabled", true);
                btn7.setAttribute("disabled", true);
            }
        }
    </script>
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

    <propertyNames style="display:none;">
        <%
            if (session.getAttribute("CurrentPropertyNameString") != null)
                out.println(((String) session.getAttribute("CurrentPropertyNameString")).trim());
        %>
    </propertyNames>

    <strings style="display:none;">
        <%
            if (session.getAttribute("CurrentStringString") != null)
                out.print(((String) session.getAttribute("CurrentStringString")).trim());
        %>
    </strings>

    <stringDetails style="display:none;">
        <%
            if (session.getAttribute("CurrentStringDetailString") != null)
                out.print(((String) session.getAttribute("CurrentStringDetailString")).trim());
        %>
    </stringDetails>

    <div class="ui container grid" style="position: relative;top:10px;left: 143px">
        <form id="form" action="UploadFile.jsp" method="post" enctype="multipart/form-data" style="height: 111px">
            <legend style="margin-bottom:50px;">
                Create New Project
                <img id="imgHelp" src="img/feedback.png"
                     style="width: 25px; height: 25px; margin-bottom: 5px; margin-left: 5px"
                     onmouseover="this.src='img/feedback_fill.png'; this.style.cursor='pointer'"
                     onmouseleave="this.src='img/feedback.png'"
                     onclick="confirm('操作说明:\n\n' +
                                              '*框选计算式后可设置强度，范围[1~10]\n\n' +
                                              '*框选控制流平展后可设置阈值和块大小\n' +
                                              '        阈值表示触发平展的最低函数量,范围[1~10]\n' +
                                              '        块大小表示平展分块的大小,范围[1~阈值-1]\n\n' +
                                              '下面举例说明( 假定设置阈值和块大小分别为 5 和 3 )：\n' +
                                              '平展前：\n' +
                                              'function(){\n' +
                                              '        节点一\n        节点二\n        节点三\n        节点四\n        节点五\n' +
                                              '}\n\n' +
                                              '平展后：\n' +
                                              'switch(blockNumber){\n' +
                                              '    case 0:\n        节点一\n        节点二\n        节点三\n    break;\n' +
                                              '    case 1:\n        节点四\n        节点五\n    break;\n' +
                                              '}\n' +
                                              '注意：块大小应小于阈值！\n\n' +
                                              '记得选择要上传的文件！')">
            </legend>
            <input type="file" name="selectFile" style="position: relative;top: -34px;left: 0px">

            <div class="field">

            </div>

            <input type="submit" name="Upload" value="Upload" style="position: relative;top:-61px;left: 547px">
        </form>
        <textarea cols=40 rows=5 wrap=virtual name=ipt id="in" style="width:609px;height:306px"><%
            if (session.getAttribute("context") != null) {
                out.println(((String) session.getAttribute("context")).trim());
                session.setAttribute("context", "");
            }
        %></textarea><br>
        <input name="chbPropertyName" type="checkbox" id="chpro" style="position: relative;top: 40px;left: 2px" onchange="checkchange();">
        <span style=" display: block; position: relative;top: 23px;left: 22px;font-weight: bold;">属性混淆</span>
        <div class="row" style="position: relative;top: 66px;left: 22px">
            <h5 style="position: absolute;top: -20px;">
                选择要进行混淆的属性&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp选择字符串</h5>
            <div id="content_property" style="width:690px;">
                <div id="content_left" name="content_left"
                     style="position:relative;top:21px;border:1px solid black;width:307px;height:264px;float:left;overflow:scroll;">

                </div>
                <div name="content_right"
                     style="position:relative;top:21px;width:300px;height:264px;float:left;overflow:scroll;border:1px solid black;">
                    <table border="1px" rules="rows" id="content_right" style="width:300px;overflow-x:scroll">
                        <tr>
                            <td></td>
                            <td>字符串</td>
                            <td>对字符串的解释</td>
                        </tr>

                    </table>
                </div>
                <input id="analy" type="button" value="Analyze" onclick="analyzeFile()"
                       style="position: relative;top: -15px;left: -68px" disabled="true">
                <div id="btnposition1" style="position: absolute;left: 0px;top: 301px">
                    <input disabled="true" type="button" id="btall1" name="btall1" value="全选" onclick="chooseall1();">
                    <input disabled="true" type="button" id="btno1" name="btno1" value="反选" onclick="obchoose1();">
                    <input disabled="true" type="button" id="btob1" name="btob1" value="全不选" onclick="not1();">
                </div>
                <div id="btnposition2" style="position: absolute;left: 312px;top: 301px">
                    <input disabled="true" type="button" id="btall2" name="btall2" value="全选" onclick="chooseall2();">
                    <input disabled="true" type="button" id="btno2" name="btno2" value="反选" onclick="obchoose2();">
                    <input disabled="true" type="button" id="btob2" name="btob2" value="全不选" onclick="not2();">
                </div>
            </div>
        </div>
    </div>
    <div class="ui container grid" style="width:740px; margin: 80px auto 0;">
        <form id="form1" style="border-top: 1px solid #b3abab;margin: 0 auto;position: relative;top: 81px;left:28px"
              onsubmit="submitForm()" method="post" enctype="multipart/form-data" target="hidden_frame">
            <div class="row">
                <div class="column">
                    <div class="" id="root">
                        <div data-reactroot="">
                            <div class="ui grid">
                                <div class="column">
                                    <iframe id="hidden_frame" name="hidden_frame" style="display:none"></iframe>
                                    <div class="ui relaxed four column grid">
                                        <div class="column">
                                            <div class="ui basic segment">
                                                <div class="field">
                                                    <div class="ui checked checkbox">
                                                        <label>
                                                            <input name="chbBigArray" type="checkbox" tabindex="0"
                                                                   value="unchecked"
                                                                   onchange="reverseValue(document.getElementsByName('chbBigArray')[0])">
                                                            <h4>大数组加壳</h4>
                                                        </label>
                                                    </div>
                                                </div>
                                                <div class="ui divider"></div>
                                                <div class="field">
                                                    <div class="ui checkbox" style="position: relative;top:50px">
                                                        <label>
                                                            <input name="chbComputation" type="checkbox" tabindex="0"
                                                                   value="unchecked"
                                                                   onchange="setDisabledOfComputationParam()"><h4
                                                                style="letter-spacing:17px">计算式</h4>
                                                        </label>
                                                    </div>
                                                </div>
                                                <div class="disabled field">
                                                    <label style="position: relative;top:64px;left:24px">强度</label>
                                                    <div class="ui input"
                                                         style="position: relative;top:45px;height: 10px">
                                                        <input name="txtComputationParam" type="number"
                                                               readonly="readonly" step="1" min="1" max="10" value="5"
                                                               style="width:51px;height: 10px;position: relative;top:-13px;left: 57px">
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
                                                                   onchange="setDisabledOfFlattenParams()"><h4>
                                                            控制流平展</h4>
                                                        </label>
                                                    </div>
                                                </div>
                                                <div class="disabled field">
                                                    <label style="position: relative;left: 23px;top:41px">阈值</label>
                                                    <div class="ui input">
                                                        <input name="txtThresholdValue" type="number" value="5"
                                                               step="1" min="1" max="10"
                                                               readonly="readonly"
                                                               style="width: 55px;position: relative;left: 55px;top: 10px"
                                                               onmouseout="changeMaxValue()">
                                                    </div>
                                                </div>
                                                <label style="position: relative;left: 23px;top:42px">块大小</label>
                                                <input name="txtBlockSize" type="number" value="4"
                                                       step="1" min="1" max="4"
                                                       readonly="readonly"
                                                       style="width: 43px;position: relative;top: 10px;left: 68px"><br>
                                                <div style="font-size: 12px;position: relative;left: 17px">
                                                    (块大小应小于阈值)
                                                </div>
                                            </div>
                                        </div>
                                        <div class="column">
                                            <div id="thirdColumn" class="ui basic segment"
                                                 style="position: relative;left: 104px">
                                                <div class="field">
                                                    <div class="ui checkbox">
                                                        <label>
                                                            <input name="chbStringAndNumber" type="checkbox"
                                                                   tabindex="0" value="unchecked"
                                                                   onchange="reverseValue(document.getElementsByName('chbStringAndNumber')[0])">
                                                            <h4>数字处理</h4>
                                                        </label>
                                                    </div>
                                                </div>

                                                <div class="field" style="position: relative;top:96px">
                                                    <div class="ui checked checkbox">
                                                        <label>
                                                            <input name="chbDeadCode" type="checkbox" tabindex="0"
                                                                   value="unchecked"
                                                                   onchange="reverseValue(document.getElementsByName('chbDeadCode')[0])">
                                                            <h4>废代码</h4>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="column">
                                            <div id="fourthColumn" class="ui basic segment"
                                                 style="position: relative;left: 104px">
                                                <div class="field" style="position: relative">
                                                    <h4>&nbsp;&nbsp;&nbsp;&nbsp;保留字</h4>
                                                    <textarea name="txtReserveName" type="text"
                                                              style="width: 100px;height: 140px;float: left"></textarea>
                                                    <span style="font-size: 12px;position: relative;left: -80px;top: 140px">(请用空格间隔)</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div style="text-align: center">
                    <input type="submit" class="btn" value="Submit" id="btnsubmit"
                           style="position: relative;top:48px;left: 212px">
                </div>
            </div>
        </form>
    </div><!-- end of container -->

    <div class="container" style="margin-top: 100px;" id="aeaoofnhgocdbnbeljkmbjdmhbcokfdb-mousedown">
        <div class="site-footer" role="contentinfo" id="" style="text-align:center;">
            &copy NWU-IRDETO IoT & Infosec Joint Lab <br> Updated on 7 July 2017
        </div><!-- /.site-footer -->
    </div>
</div>

</body>
</html>

