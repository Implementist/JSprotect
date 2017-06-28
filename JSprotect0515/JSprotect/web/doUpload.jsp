<%--
  Created by IntelliJ IDEA.
  User: KKY
  Date: 2016/12/12
  Time: 11:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.io.*,java.util.*,javax.servlet.*" %>
<%@ page import="org.apache.commons.fileupload.*" %>
<%@ page import="org.apache.commons.fileupload.disk.*" %>
<%@ page import="org.apache.commons.fileupload.servlet.*" %>
<%@ page import="com.rocky.adbProject.Project" %>
<%@ page import="org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException" %>
<%@ page import="com.rocky.zip.*" %>
<%@ page import="obfu.copy.*" %>
<%@ page import="com.rocky.db.ProjectInfo" %>
<%@ page import="com.rocky.db.ProjectInfoDAO" %>
<%@ page import="java.util.List" %>
<%
    File file;
    int maxFileSize = 50000 * 1024;
    int maxMemSize = 100000 * 1024;

    // 获取文件要上传的路径
    ServletContext context = pageContext.getServletContext();
    String filePath = context.getInitParameter("file-upload-path");

    //System.out.println(filePath);

    // 验证上传内容的类型
    String contentType = request.getContentType();
    String obfuscationStrength = "";

    //System.out.println(obfuscationStrength);

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
        String md5 = (String) session.getAttribute("upLoadPath");
        //System.out.println(md5);
        filePath = filePath + md5 + "\\";
        //上传
        System.out.println("/////" + filePath);
        try {

            File upLoadDir = new File(filePath);
            if (!upLoadDir.exists()) {
                upLoadDir.mkdir();
            }

            // 解析获取的文件
            List fileItems = upload.parseRequest(request);

            // 处理上传的文件
            Iterator i = fileItems.iterator();

            out.println("<html>");
            out.println("<head>");
            out.println("<title>JSP File upload</title>");
            out.println("</head>");
            out.println("<body>");
            int bigArray = 0, calculate = 0, strength = 0, cff = 0, thresholdValue = 0, blockSize = 0, stringAndNumber = 0, string = 0, number = 0, paramName = 0;
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
                        stringAndNumber = fi.getString().equals("checked") ? 1 : 0;
                        number = stringAndNumber;
                        string = 0;
                        System.out.println(stringAndNumber);
                    } else if (fi.getFieldName().equals("chbString")) {
                        string = fi.getString().equals("checked") ? 1 : 0;
                        System.out.println(string);
                    } else if (fi.getFieldName().equals("chbNumber")) {
                        number = fi.getString().equals("checked") ? 1 : 0;
                        System.out.println(number);
                    } else if (fi.getFieldName().equals("chbParamName")) {
                        paramName = fi.getString().equals("checked") ? 1 : 0;
                        System.out.println(paramName);
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
                    String projectId = project
                            .getProjectId((String) session
                                    .getAttribute("user"));

                    //System.out.println(projectId);

					/* if(new File(filePath+projectId).exists())
					{
						project.deleteFiles(filePath+projectId);
					}*/


                    // 对每一个zip将其解压到md5+projectID的路径中
                    //System.out.println("Unzip Filename:" + filePath + projectId);
                    // Unzip unzip = new Unzip(filePath + fileName,
                    //  filePath + projectId);
                    // ArrayList<String> zipfileEntryName = unzip
                    // .unzipFiles();

                    // 删除zip文件
             /*       File upLoadFile = new File(filePath + fileName);
                    if (upLoadFile.exists())
                        upLoadFile.delete();*/

                    // 之后对每一个js进行保护

                    int flatternStrength = 0, opaqueStrength = 0;
//                    int shell, multy, caculate, Prop;
//                    System.out.println("::::::" + obfuscation1 + obfuscation2 + obfuscation3);
//                    if (obfuscation1.equals("1")) {
//                        shell = 1;
//                    } else {
//                        shell = 0;
//                    }
//                    if (obfuscation2.equals("1")) {
//                        multy = 1;
//                    } else {
//                        multy = 0;
//                    }
//                    if (obfuscation3.equals("1")) {
//                        caculate = 1;
//                    } else {
//                        caculate = 0;
//                    }
//                    if (obfuscation4.equals("1")) {
//                        Prop = 1;
//                    } else {
//                        Prop = 0;
//                    }
                    test ntest = new test();
                    //System.out.println("/////////"+"fp"+filePath+RName);
                    System.out.println(filePath + "KKKLLLL" + fileName);
                    ntest.protect(cff, thresholdValue, blockSize, bigArray, calculate, strength, paramName, stringAndNumber, number, string, filePath + fileName, filePath, fileName, projectId, (String) session.getAttribute("user"));

                    //TryYUI.compress(filePath+fileName);
                    //此部分为混淆强度设置
                   /* if(obfuscationStrength.equals("Low")){
                        flatternStrength = 40;
                        opaqueStrength = 30;
                    } else if (obfuscationStrength.equals("Middle")) {
                        flatternStrength = 60;
                        opaqueStrength = 50;
                    } else {
                        flatternStrength = 80;
                        opaqueStrength = 70;
                    }
                    opaqueStrength = 0;//不透明谓词混淆部分存在问题(问题已解决，缺少不透明谓词库)，设置为零绕过这一保护
                    //flatternStrength = 0;//设置为零绕过平展保护

                    ArrayList<Integer> strength = project.ProcessProject(zipfileEntryName, flatternStrength, opaqueStrength);

                    System.out.println("Parameter:" + zipfileEntryName + "  FlattenStrength:" + flatternStrength + "  OpaqueStrength:" +opaqueStrength + "  FlattenCount:" + strength.get(0)+ "  OpaqueCount:" + strength.get(1));


                    unzip.zip.close();  */
                    // 压缩后的zip文件，存储到web目录下的Projects目录下，ProjectID.Zip
                    Zip zip = new Zip();
                    // zip.setZipFileDirectory("D:\\Tools\\Apache Software Foundation\\Tomcat 7.0\\webapps\\Project\\"+(String) session.getAttribute("user")+"\\"+projectId);
                    zip.setZipFileDirectory("C:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\webapps\\ROOT\\Projects\\" + (String) session.getAttribute("user") + "\\" + projectId);
                    System.out.println(filePath + projectId);

                    // upLoadPath实际就是md5值
                    File protectedProjectPath = new File(request
                            .getSession().getServletContext()
                            .getRealPath("")
                            + "\\Projects\\"
                            + session.getAttribute("upLoadPath"));

                    // 若md5目录不存在，则创建
                    if (!protectedProjectPath.exists())
                        protectedProjectPath.mkdir();

                   /* String protectedProjectSavePath = request
                            .getSession().getServletContext()
                            .getRealPath("")
                            + "\\Projects\\"
                            + session.getAttribute("upLoadPath")
                            + "\\"
                            + projectId + ".js";*/
                    String protectedProjectSavePath = "C:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\webapps\\ROOT\\Projects\\" + (String) session.getAttribute("user") + "\\" + projectId + "\\" + projectId + fileName.substring(0, fileName.length() - 3) + ".zip";

                    System.out.println("DownlodaPath:" + protectedProjectSavePath);

                    File temp = new File(protectedProjectSavePath);
                    if (temp.exists())
                        temp.delete();


                    // zip.setZipFileName(protectedProjectSavePath);
                    // zip.compress();

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
            out.println("</body>");
            out.println("</html>");
        } catch (SizeLimitExceededException ex) {
            System.out.println("File size exceeded!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            //out.print("<script>alert(\"Oops, it seems an error occurred here!\")</script>");
        }
    } else {
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet upload</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<p>No file uploaded</p>");
        out.println("</body>");
        out.println("</html>");
    }
%>
