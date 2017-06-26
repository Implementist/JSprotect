package com.rocky.protect;

import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.AstRoot;


import java.io.*;
import java.util.ArrayList;

public class Ast {

    private CompilerEnvirons env;

    /**
     * Ast的构造函数
     */
    public Ast()
    {
        env = new CompilerEnvirons();
        //env.ideEnvirons();
        env.setRecoverFromErrors(true);
        env.setGenerateDebugInfo(true);
        env.setRecordingComments(true);
    }

    /**
     * 根据提供的文件路径，获取该文件抽象语法树的根
     * @param filePath
     * @param startLineNo
     * @return
     */
    @SuppressWarnings("finally")
    public AstRoot getAstRoot(String filePath, int startLineNo)
    {
        AstRoot root = null;
        Reader reader = null;
        /*BufferedReader reader = null;
        String tempString =null;
        ArrayList<String> arrArray = new ArrayList<String> ();*/
        try
        {
            /*reader= new BufferedReader(new FileReader(filePath));
            Parser parser = new Parser(env);
            while((tempString= reader.readLine())!=null){
                System.out.println("行: " + tempString);
                arrArray.add(tempString);
            }
            int strLen =arrArray.size();
            int i=0;
            tempString = "";
            while(i<strLen) {
                tempString= tempString + arrArray.get(i);
                i++;
            }
            System.out.println("读到的字符串："+tempString);
            root = parser.parse(tempString, filePath, startLineNo);*/

            reader= new FileReader(filePath);
            Parser parser = new Parser(env);
            root = parser.parse(reader, filePath, startLineNo);
            reader.close();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally {

            return root;
        }
    }
}
