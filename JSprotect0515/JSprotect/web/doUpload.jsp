<%--
  Created by IntelliJ IDEA.
  User: KKY
  Date: 2016/12/12
  Time: 11:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.rocky.adbProject.Project,com.rocky.db.ProjectInfo,com.rocky.db.ProjectInfoDAO" %>
<%@ page import="obfu.copy.FileUtils" %>
<%@ page import="obfu.copy.test" %>
<%@ page import="org.apache.commons.fileupload.FileItem" %>
<%@ page import="org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException" %>
<%@ page import="org.apache.commons.fileupload.disk.DiskFileItemFactory" %>
<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload" %>
<%@ page import="java.io.File" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%
    File file;
    int maxFileSize = 50000 * 1024;
    int maxMemSize = 100000 * 1024;

    // 获取文件要上传的路径
    String filePath = FileUtils.SERVER_ROOT_UPLOAD_FOLDER;

    // 验证上传内容的类型
    String contentType = request.getContentType();
    String obfuscationStrength = "";

    // 内容类型验证通过
    if ((contentType.contains("multipart/form-data"))) {
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // 设置内存中存储文件的最大值
        factory.setSizeThreshold(maxMemSize);

        // 本地存储的数据大于maxMemSize
        factory.setRepository(new File("file-upload-temp"));

        // 创建一个新的文件上传处理程序
        ServletFileUpload upload = new ServletFileUpload(factory);

        // 设置最大上传的文件大小
        upload.setSizeMax(maxFileSize);

        // 获取用户的md5
        String userName = (String) session.getAttribute("user");
        //System.out.println(md5);
        filePath = filePath + userName + File.separator;
        //上传
        System.out.println("Upload Path: " + filePath);
        try {
            File upLoadDir = new File(filePath);
            if (!upLoadDir.exists())
                upLoadDir.mkdirs();

            // 解析获取的文件
            List fileItems = upload.parseRequest(request);

            // 处理上传的文件
            Iterator i = fileItems.iterator();

            int bigArray = 0, calculate = 0, strength = 0, cff = 0, thresholdValue = 0, blockSize = 0, numberHandling = 0, paramName = 0, deadCode = 0;
            HashSet<String> reserveName = new HashSet<String>();
            while (i.hasNext()) {
                // 获取上传文件的参数
                FileItem fi = (FileItem) i.next();
                if (fi.isFormField()) {
                    System.out.println(fi.getFieldName());

                    if (fi.getFieldName().equals("chbBigArray")) {
                        bigArray = fi.getString().equals("checked") ? 1 : 0;
                        System.out.println(bigArray);
                    } else if (fi.getFieldName().equals("chbComputation")) {
                        calculate = fi.getString().equals("checked") ? 1 : 0;
                        System.out.println(calculate);
                    } else if (fi.getFieldName().equals("txtComputationParam")) {
                        strength = Integer.parseInt(fi.getString());
                        System.out.println(strength);
                    } else if (fi.getFieldName().equals("chbControlFlowFlatten")) {
                        cff = fi.getString().equals("checked") ? 1 : 0;
                        System.out.println(cff);
                    } else if (fi.getFieldName().equals("txtThresholdValue")) {
                        thresholdValue = Integer.parseInt(fi.getString());
                        System.out.println(thresholdValue);
                    } else if (fi.getFieldName().equals("txtBlockSize")) {
                        blockSize = Integer.parseInt(fi.getString());
                        System.out.println(blockSize);
                    }
                    //TODO: The following two cases should be deleted later.
                    else if (fi.getFieldName().equals("chbStringAndNumber")) {
                        numberHandling = fi.getString().equals("checked") ? 1 : 0;
                        System.out.println(numberHandling);
                    } else if (fi.getFieldName().equals("chbParamName")) {
                        paramName = fi.getString().equals("checked") ? 1 : 0;
                        System.out.println(paramName);
                    } else if (fi.getFieldName().equals("txtReserveName")) {
                        String[] reserveNames = fi.getString().trim().split(" ");
                        reserveName.addAll(Arrays.asList(reserveNames));
                        System.out.println(fi.getString());
                    } else if (fi.getFieldName().equals("chbDeadCode")) {
                        deadCode = fi.getString().equals("checked") ? 1 : 0;
                        System.out.println(deadCode);
                    }
                } else {

                    String fileName = fi.getName();
                    // 写入文件
                    if (fileName.lastIndexOf("\\") >= 0) {
                        file = new File(filePath,
                                fileName.substring(fileName
                                        .lastIndexOf("\\")));
                    } else {
                        file = new File(filePath,
                                fileName.substring(fileName
                                        .lastIndexOf("\\") + 1));
                    }
                    fi.write(file);
                    System.out.println("Uploaded Filename:" + filePath + fileName);

                    // 生成该用户的ProjectId
                    Project project = new Project();
                    int projectId = project
                            .getProjectId((String) session
                                    .getAttribute("user"));

                    test ntest = new test();
                    ntest.protect(deadCode, reserveName, cff, thresholdValue, blockSize, bigArray, calculate, strength, paramName, numberHandling, filePath + fileName, filePath, fileName, projectId, (String) session.getAttribute("user"));

                    System.out.println(filePath + projectId);

                    // upLoadPath实际就是md5值
                    File protectedProjectPath = new File(request
                            .getSession().getServletContext()
                            .getRealPath("")
                            + "\\Projects\\"
                            + session.getAttribute("user"));

                    // 若md5目录不存在，则创建
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
                }
            }
        } catch (SizeLimitExceededException ex) {
            System.out.println("File size exceeded!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
%>
