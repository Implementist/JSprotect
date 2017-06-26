package obfu.copy;
//Ŀǰ������
//1.sum++���ֻ������⡣
//2.InfixExpression��Ƕ�׻������⡣

import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;

import java.io.*;
import java.util.ArrayList;
import java.util.Set;

public class test {
    public test() {
        //System.out.println("ooooasdasda");
    }

    public static AstNode InsertCrypt(AstNode Decrypt, AstNode Source) {
        AstNode parent = Source.getParent();
        parent.addChildBefore(Decrypt, Source);
        Decrypt.setParent(parent);
        return parent;
    }

    public static String DealStr(String Str) {
        //System.out.println(Str);
        String[] strArray = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
        StringBuffer str = new StringBuffer(Str);
        ArrayList<Integer> IndexList = new ArrayList<Integer>();
        int index = -1;
        int sum = 0;
        while (true) {
            Str = Str.substring(index + 1);
            //  System.out.println(Str);
            index = Str.indexOf("\\x");
            if (index == -1) break;
            if (Str.charAt(index + 3) == '\\' || Str.charAt(index + 3) == '\'' || Str.charAt(index + 3) == '"')
                IndexList.add(sum + index);
            sum += index + 1;
            //System.out.println(index);
        }
        // System.out.println(IndexList.size());
        for (int i = IndexList.size() - 1; i >= 0; i--) {
            str.insert(IndexList.get(i) + 2, "0");
            //System.out.println(str.substring(IndexList.get(i)));
        }
        //System.out.println(str);
        return new String(str);
    }


    //是否控制流平展，阈值，case大小，大数组加壳，计算式混淆，计算式混淆参数，属性名，处理数字，处理字符串
    public void protect(int controlflow, int stand, int eachcase, int Shell, int caculate, int ratecalculate, int Prop, int IIS, int IsNum, int IsString, String path, String FilePath, String fileName, String projectId, String user) {
        //public static void main(String[] args)throws IOException{
        try {
            //D:\个人\js前端相关\JSprotect0515\JSprotect\src\obfu\copy
            //4.13
            //String decryptfile="D:\\Ideaworkspace\\JSprotect0515\\JSprotect\\src\\obfu\\copy\\decrypt.js";
            String decryptfile = "C:\\Program Files\\decrypt.js";
            //String decryptfile="D:\\workspace\\java\\JSprotect\\src\\obfu\\copy\\decrypt.js";
            Reader decryptreader = new FileReader(decryptfile);
            CompilerEnvirons decryptenv = new CompilerEnvirons();
            decryptenv.setRecordingLocalJsDocComments(true);    //C:\Program Files\Apache Software Foundation\Tomcat 7.0\webapps\JSprotect\WEB-INF\classes\obfu\copy
            decryptenv.setAllowSharpComments(true);
            decryptenv.setRecordingComments(true);
            AstRoot decryptnode = new Parser(decryptenv).parse(decryptreader, decryptfile, 1);
            //System.out.println(decryptnode.toSource());


            String file = path;
            Reader reader = new FileReader(file);
            CompilerEnvirons env = new CompilerEnvirons();
            env.setRecordingLocalJsDocComments(true);
            env.setAllowSharpComments(true);
            env.setRecordingComments(true);
            AstRoot node = new Parser(env).parse(reader, file, 1);
            System.out.println("finish");
            AstNode Nnode = InsertCrypt(decryptnode, (AstNode) node.getFirstChild());
            function fu = new function();
            ToElement ob = new ToElement(Nnode);
            ob.GetVarNameMap(Nnode, Prop, caculate, Shell, ratecalculate);
            Set<String> KeyWord = ob.getKeyWord();
            ArrayList<AstNode> NodeList = ob.getNodeList();
            VarComp Varp = new VarComp();
            Varp.ChAttr(Nnode);
            if (controlflow == 1) {
                if (stand <= eachcase || eachcase == 0) {
                    stand = 5;
                    eachcase = 4;
                }
                flatten fla = new flatten(Nnode);
                fla.flattencontrol(Nnode, stand, eachcase);
            }
            createfunc cfunc = new createfunc();
            Nnode = cfunc.createfunction(Nnode, KeyWord, Shell, IIS, IsNum, IsString);
            testpage test = new testpage();
            test.testt(Nnode, NodeList);
            File protectedProjectPath = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\webapps\\ROOT\\Projects\\" + user);
            if (!protectedProjectPath.exists())
                protectedProjectPath.mkdir();
            System.out.println("C:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\webapps\\ROOT\\Projects\\" + user + "\\" + projectId + "\\" + projectId + fileName + "====----====");
            File protectedProjectPath2 = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\webapps\\ROOT\\Projects\\" + user + "\\" + projectId);
            if (!protectedProjectPath2.exists())
                protectedProjectPath2.mkdirs();

            File dir = new File("C:" + File.separator + "Projects" + File.separator + user + File.separator + projectId);
            dir.mkdirs();

            FileWriter fw = new FileWriter("C:" + File.separator + "Projects" + File.separator + user + File.separator + projectId + File.separator + projectId + fileName);
            String str = Nnode.toSource();
            if (IIS == 1) {
                str = str.replace("\\\\x", "\\x");
                str = DealStr(str);
            }
            //System.out.println(str);
            fw.write(str);
            fw.flush();
            fw.close();
            compress comp = new compress();
            comp.compress("C:" + File.separator + "Projects" + File.separator + user + File.separator + projectId + File.separator + projectId + fileName);
            moveFile("C:" + File.separator + "Projects" + File.separator + user + File.separator + projectId,
                    "C:" + File.separator + "Program Files" + File.separator + "Apache Software Foundation" + File.separator + "Tomcat 7.0" + File.separator + "webapps" + File.separator + "ROOT" + File.separator + "Projects" + File.separator + user + File.separator + projectId);
        } catch (IOException ee) {
            System.out.println(ee.toString());
        }
    }

    private void moveFile(String source, String target) {
        copyFolder(source,  target);
        delFolder(source);
    }

    /**
     *  复制整个文件夹内容
     *  @param  oldPath  String  原文件路径  如：c:/fqf
     *  @param  newPath  String  复制后路径  如：f:/fqf/ff
     *  @return  boolean
     */
    public  void  copyFolder(String  oldPath,  String  newPath)  {

        try  {
            (new  File(newPath)).mkdirs();  //如果文件夹不存在  则建立新文件夹
            File  a=new  File(oldPath);
            String[]  file=a.list();
            File  temp=null;
            for  (int  i  =  0;  i  <  file.length;  i++)  {
                if(oldPath.endsWith(File.separator)){
                    temp=new  File(oldPath+file[i]);
                }
                else{
                    temp=new  File(oldPath+File.separator+file[i]);
                }

                if(temp.isFile()){
                    FileInputStream  input  =  new  FileInputStream(temp);
                    FileOutputStream  output  =  new  FileOutputStream(newPath  +  "/"  +
                            (temp.getName()).toString());
                    byte[]  b  =  new  byte[1024  *  5];
                    int  len;
                    while  (  (len  =  input.read(b))  !=  -1)  {
                        output.write(b,  0,  len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if(temp.isDirectory()){//如果是子文件夹
                    copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);
                }
            }
        }
        catch  (Exception  e)  {
            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace();

        }

    }

    /**
     *  删除文件夹
     *  @param folderPath 文件夹路径
     *  @return  是否删除成功
     */
    public  void  delFolder(String  folderPath)  {
        try  {
            delAllFile(folderPath);  //删除完里面所有内容
            String  filePath  =  folderPath;
            filePath  =  filePath.toString();
            java.io.File  myFilePath  =  new  java.io.File(filePath);
            myFilePath.delete();  //删除空文件夹

        }
        catch  (Exception  e)  {
            System.out.println("删除文件夹操作出错");
            e.printStackTrace();

        }

    }

    /**
     *  删除文件夹里面的所有文件
     *  @param  path  String  文件夹路径  如  c:/fqf
     */
    public  void  delAllFile(String  path)  {
        File  file  =  new  File(path);
        if  (!file.exists())  {
            return;
        }
        if  (!file.isDirectory())  {
            return;
        }
        String[]  tempList  =  file.list();
        File  temp  =  null;
        for  (int  i  =  0;  i  <  tempList.length;  i++)  {
            if  (path.endsWith(File.separator))  {
                temp  =  new  File(path  +  tempList[i]);
            }
            else  {
                temp  =  new  File(path  +  File.separator  +  tempList[i]);
            }
            if  (temp.isFile())  {
                temp.delete();
            }
            if  (temp.isDirectory())  {
                delAllFile(path+"/"+  tempList[i]);//先删除文件夹里面的文件
                delFolder(path+"/"+  tempList[i]);//再删除空文件夹
            }
        }
    }
}