<%--
  Created by IntelliJ IDEA.
  User: Implementist
  Date: 2017/08/28
  Time: 9:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.rocky.adbProject.Project,com.rocky.db.ProjectInfo,com.rocky.db.ProjectInfoDAO" %>
<%@ page import="obfu.copy.FileUtils" %>
<%@ page import="obfu.copy.test" %>
<%@ page import="java.io.File" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.Arrays" %>
<%
    int bigArray = Integer.parseInt(request.getParameter("bigArray"));
    int calculate = Integer.parseInt(request.getParameter("calculate"));
    int strength = Integer.parseInt(request.getParameter("strength"));
    int controlFlowFlatten = Integer.parseInt(request.getParameter("controlFlowFlatten"));
    int thresholdValue = Integer.parseInt(request.getParameter("thresholdValue"));
    int blockSize = Integer.parseInt(request.getParameter("blockSize"));
    int numberHandling = Integer.parseInt(request.getParameter("numberHandling"));
    int paramName = Integer.parseInt(request.getParameter("paramName"));
    int deadCode = Integer.parseInt(request.getParameter("deadCode"));

    //保留字
    String reservedNames = request.getParameter("reserveNames");
    String[] reservedNameArray = reservedNames.trim().split(",");
    HashSet<String> reservedNameSet = new HashSet<String>(Arrays.asList(reservedNameArray));

    //属性名
    String propertyNames = request.getParameter("propertyNames");

    //字符串
    String strings = request.getParameter("strings");

    // 获取文件要上传的路径
    String filePath = FileUtils.SERVER_ROOT_UPLOAD_FOLDER;

    String obfuscationStrength = "";

    // 获取用户名
    String userName = (String) session.getAttribute("user");

    filePath = filePath + userName + File.separator;
    try {
        String fileName = (String) session.getAttribute("CurrentFile");

        // 生成该用户的ProjectId
        Project project = new Project();
        int projectId = project.getProjectId(userName);

        test ntest = new test();
        ntest.protect(propertyNames, strings, deadCode, reservedNameSet, controlFlowFlatten, thresholdValue, blockSize, bigArray, calculate, strength, paramName, numberHandling, filePath + fileName, filePath, fileName, projectId, (String) session.getAttribute("user"));

        // upLoadPath实际就是md5值
        File protectedProjectPath = new File(request
                .getSession().getServletContext()
                .getRealPath("")
                + "\\Projects\\"
                + userName);

        if (!protectedProjectPath.exists())
            protectedProjectPath.mkdirs();

        // 压缩完成后，删除该Project提交的文件
        project.deleteDirectory(filePath + projectId);

        //构造工程信息
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setUsername((String) session.getAttribute("user"));
        projectInfo.setProjectId(projectId);
        projectInfo.setAntidbg(1);
        projectInfo.setObfuscation(1);
        projectInfo.setAntiTamper(1);
        projectInfo.setObfuscationStrength(obfuscationStrength);
        projectInfo.setFlatternCount(0);
        projectInfo.setOpaqueCount(0);
        projectInfo.setFileName(fileName);

        // 将该工程信息插入数据库
        ProjectInfoDAO.insertProjectInfo(projectInfo);

        // 构造返回成功的字符串
        String returnValue = "<script>parent.callback('success')</script>";
        out.print(returnValue);

    } catch (Exception ex) {
        //System.out.println(ex.getMessage());
        ex.printStackTrace();
    }
%>
