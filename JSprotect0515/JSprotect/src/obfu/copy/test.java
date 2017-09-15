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
import java.util.Map;
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
    public void protect(String propertyNames, String strings, int DeadCode, Set ReserverName, int controlflow, int stand, int eachcase, int Shell, int caculate, int ratecalculate, int Prop, int IIS, String path, String FilePath, String fileName, int projectId, String user) {
        System.out.println("Prop: " + Prop);

        try {
            String decryptfile = FileUtils.getWholeFileName("decrypt.js", FileUtils.SERVER_ROOT_LIB_FOLDER);

            Reader decryptreader = new FileReader(decryptfile);
            CompilerEnvirons decryptenv = new CompilerEnvirons();
            decryptenv.setRecordingLocalJsDocComments(true);
            decryptenv.setAllowSharpComments(true);
            decryptenv.setRecordingComments(true);
            AstRoot decryptnode = new Parser(decryptenv).parse(decryptreader, decryptfile, 1);

            AstRoot deadnode = null;
            AstRoot deadnodeIf = null;
            if (DeadCode == 1) {
                String deadfile = FileUtils.getWholeFileName("deadcode.js", FileUtils.SERVER_ROOT_LIB_FOLDER);
                Reader deadreader = new FileReader(deadfile);
                CompilerEnvirons deadenv = new CompilerEnvirons();
                deadenv.setRecordingLocalJsDocComments(true);
                deadenv.setAllowSharpComments(true);
                deadenv.setRecordingComments(true);
                deadnode = new Parser(deadenv).parse(deadreader, deadfile, 1);

                String deadfileIf = FileUtils.getWholeFileName("deadcodeIf.js", FileUtils.SERVER_ROOT_LIB_FOLDER);
                Reader deadreaderIf = new FileReader(deadfileIf);
                CompilerEnvirons deadenvIf = new CompilerEnvirons();
                deadenvIf.setRecordingLocalJsDocComments(true);
                deadenvIf.setAllowSharpComments(true);
                deadenvIf.setRecordingComments(true);
                deadnodeIf = new Parser(deadenvIf).parse(deadreaderIf, deadfileIf, 1);
            }

            Reader reader = new FileReader(path);
            CompilerEnvirons env = new CompilerEnvirons();
            env.setRecordingLocalJsDocComments(true);
            env.setAllowSharpComments(true);
            env.setRecordingComments(true);
            AstNode Nnode = new Parser(env).parse(reader, path, 1);
            System.out.println("finish");
            // AstNode Nnode = InsertCrypt(decryptnode, (AstNode) node.getFirstChild());

            ToElement ob = new ToElement(Nnode);
            System.out.println("test in"+propertyNames);
            ob.GetVarNameMap(propertyNames, strings, decryptnode, Nnode, deadnode, deadnodeIf, Prop, caculate, Shell, ratecalculate, ReserverName);
            Map<String, String> VarThisMap = ob.getVarThisSet();
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
            Nnode = cfunc.createfunction(Nnode, KeyWord, Shell, IIS, IIS, 0);
            testpage test = new testpage();
            test.testt(Shell, Prop, Nnode, NodeList, ReserverName, VarThisMap);
            File protectedProjectPath = new File(FileUtils.getWholeDirectory(FileUtils.SERVER_ROOT_FOLDER, "Projects", user));
            if (!protectedProjectPath.exists())
                protectedProjectPath.mkdir();

            System.out.println("Full File Name: " + FileUtils.getWholeFileName(fileName, FileUtils.SERVER_ROOT_FOLDER, "Projects", user, projectId + ""));
            File protectedProjectPath2 = new File(FileUtils.getWholeDirectory(FileUtils.SERVER_ROOT_FOLDER, "Projects", user, projectId + ""));
            if (!protectedProjectPath2.exists())
                protectedProjectPath2.mkdirs();

            File dir = new File(FileUtils.getWholeDirectory(FileUtils.SERVER_ROOT_FOLDER, "Projects", user, projectId + ""));
            dir.mkdirs();

            FileWriter fw = new FileWriter(FileUtils.getWholeFileName(fileName, FileUtils.SERVER_ROOT_FOLDER, "Projects", user, projectId + ""));
            String str = Nnode.toSource();
            //System.out.println(str);
            fw.write(str);
            fw.flush();
            fw.close();

            compress comp = new compress();
            comp.compress(FileUtils.getWholeFileName(fileName, FileUtils.SERVER_ROOT_FOLDER, "Projects", user, projectId + ""));
        } catch (IOException ee) {
            ee.printStackTrace();
        }
    }
}