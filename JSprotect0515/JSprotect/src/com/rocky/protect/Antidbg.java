package com.rocky.protect;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ast.*;

import java.io.*;

//import org.eclipse.jdt.internal.compiler.ast.ThisReference;

public class Antidbg {

    private String antiDbgString; // 阻止调试的产生
    private String debuggerDetectionCode; // 用于检测各个浏览器中是否有调试器参与
    private int antiTamperCodeLength; // 反调试代码的长度
    private AstNode externalFuncCallNode; // 额外创造的函数结点
    private Obfuscation obfuscation; // 混淆对象实例
    private String filePath; // 文件路径
    private AstRoot astRoot; // 用于记录该文件所生成的抽象语法树的根结点
    private Ast ast; // ast对象实例
    private boolean functionInternal; // 是否选取内部函数，作为通过校验过程待生成的对象
    private FunctionNode internalfuncNode; // 若防篡改的过程是从内部函数提取，则用functionNode记录

    /**
     * 构造函数
     *
     * @param filePath 要保护JavaScript代码文件的路径
     */
    public Antidbg(String filePath) {
        this.antiDbgString = "(function(){try{function a(){(function(){}).constructor(\"(function(f){(function a(){try{function b(i){if((''+(i/i)).length !== 1 || i % 20 === 0){(function(){}).constructor('debugger')();}else{debugger;}b(++i);}b(0);} catch(e) {f.setTimeout(a, 50);}})()})(document.body.appendChild(document.createElement('frame')).contentWindow)\")();}if(!/arm|ipad|ipod|iphone/i.test(navigator.platform)){if(document.body){a();}else{window.onload=a;}}}catch(e){}})();";
        this.antiTamperCodeLength = 0;
        this.externalFuncCallNode = null;
        this.ast = new Ast();
        this.filePath = null;
        this.astRoot = null;
        this.filePath = filePath;
    }

    /**
     * 设置obfuscation对象
     *
     * @param obfuscation
     */
    public void setObfuscationObject(Obfuscation obfuscation) {
        this.obfuscation = obfuscation;
    }

    /**
     * 获取obfuscation对象
     *
     * @return obfuscation对象
     */
    public Obfuscation getObfuscationObject() {
        return this.obfuscation;
    }

    /**
     * 设置文件名
     *
     * @param filePath 文件名
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 获取当前文件的路径
     *
     * @return 当前文件的文件名
     */
    public String getFilePath() {
        return this.filePath;
    }

    /**
     * 生成抽象语法树的根结点
     */
    public void generateAstRoot() {
        if (this.filePath != null) {
            this.astRoot = this.ast.getAstRoot(this.filePath, 0);
            //System.out.println("这是提取ASTROOT后："+this.astRoot.toSource());
        }
    }

    /**
     * 获取反调试代码
     *
     * @return 反调试代码
     */
    /*public String getdebuggerDetectionCode() {
		if (this.debuggerDetectionCode != null)
			return this.debuggerDetectionCode;
		else
			return "";
	}*/

    /**
     * 返回反调试代码
     *
     * @return 反调试代码d
     */
    public String getAntiDebugString() {
        return this.antiDbgString;
    }

    /**
     * 向JavaScript文件中注入调试器检测代码
     */
    public void injectAntiDbg() {

		/*
		 * step 1: 生成该JavaScript文件的抽象语法树
		 * step 2: 获取顶级作用域中的函数，判断从该文件中获取函数还是生成额外的函数作为防篡改部分要生成的对象
		 * step 3: 构造防篡改部分的代码
		 * step 4: 处理抽象语法树
		 * step 5: 添加混淆
		 * step 6: 压缩并写入文件
		 */
        this.generateAstRoot();
        this.getFunction();
        String antiTmaperPart = this.constructAntiTamperPart(this.consturctFunctionToBeGenerated());
        //this.processAstTree();//测试,不添加新的函数在末尾！
        this.obfuscation.obfuscate(this.astRoot);

        // 以下工作为实现第六步的操作
        try {
            // 先把防篡改代码写入orginal.js
            FileWriter writer = new FileWriter(this.filePath, false);
            //writer.write(antiTmaperPart);//测试不添加反调试结构，屏蔽！！！！！！！！！！！

            //writer.write(this.astRoot.toSource());

            // 再把混淆后的代码写入original1.js, 并压缩为original1-out.js
            String obfuscatedFileName = this.filePath.substring(0,
                    this.filePath.length() - 3) + "1.js";
            FileWriter writer2 = new FileWriter(obfuscatedFileName, false);
            //System.out.println(this.astRoot.toSource());
            //writer2.write(antiTmaperPart);
            writer2.write(this.astRoot.toSource());
            writer2.close();

            // demo1.js  to demo1-out.js
            this.compress(obfuscatedFileName);

            // this.filePath.substring(0, this.filePath.length() - 3) +
            // "-out.js"

            // 读取original1-out.js并写入original.js中
            String compressedFileName = obfuscatedFileName.substring(0,
                    obfuscatedFileName.length() - 3) + "-out.js";
            FileReader reader = new FileReader(compressedFileName);
            //@SuppressWarnings("resource")
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line = null;
            while ((line = bufferedReader.readLine()) != null)
                writer.write(line);

            writer.close();
            bufferedReader.close();
            reader.close();

            File file = new File(obfuscatedFileName);
            File file1 = new File(compressedFileName);
            if (file.exists())
                file.delete();
            if (file1.exists())
                file1.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * IE中的检测方法
     *
     * @return IE中的检测方法
     */
	/*private String detectIE() {
		return "if(window.__IE_DEVTOOLBAR_CONSOLE_COMMAND_LINE)return function(){}";
	}

	*//**
     * Firefox中的检测方法
     *
     * @return Firefox中的检测方法
     *//*
	private String detectFirefox() {
		return "if(window.console&&window.console.exception&&window.console.log&& window.console.log.toString().indexOf(\"apply\") !== -1";
	}

	*//**
     * Opera, Chrome和Safari中的检测方法是相同的
     *
     * @return Opera, Chrome和Safari中的检测方法是相同的
     *//*
	private String detectChrome() {
		return "if(console.profiles){console.profile('a');console.profileEnd('a');if(console.profiles.length!=0)return function(){}";
	}*/

    /**
     * 生成检测调试器的代码
     */
	/*public void genDebuggerDetectionCode() {
		if (debuggerDetectionCode == null) {
			this.debuggerDetectionCode += this.detectIE();
			this.debuggerDetectionCode += this.detectFirefox();
			this.debuggerDetectionCode += this.detectChrome();
		}
	}*/

    /**
     * 压缩JavaScript文件
     *
     * @param fileName 要压缩的文件名
     */
    public void compress(String fileName) {

        File compressedFile = new File(fileName);
        File dir = compressedFile.getParentFile();

        String cmd = this.constructCommandLine(fileName);
        Runtime runtime = Runtime.getRuntime();

        try {
            java.lang.Process process = runtime.exec(cmd, null, dir);
            BufferedInputStream in = new BufferedInputStream(
                    process.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));

            File file = new File("c:\\Tools\\Apache Software Foundation\\Tomcat 7.0\\lib\\yuicompressor-2.4.8.jar");

            int byteread = 0;
            int bytesum = 0;
            if (file.exists()) {
                InputStream inStream = new FileInputStream("c:\\Tools\\Apache Software Foundation\\Tomcat 7.0\\lib\\yuicompressor-2.4.8.jar"); // 读入原文件
                FileOutputStream fs = new FileOutputStream(dir + File.separator
                        + "yuicompressor-2.4.8.jar");
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }

            String lineStr;

            while ((lineStr = inBr.readLine()) != null)
                // 获得命令执行后在控制台的输出信息
                System.out.println(lineStr);// 打印输出信息
            // 检查命令是否执行失败
            if (process.waitFor() != 0) {
                if (process.exitValue() == 1)// p.exitValue()==0表示正常结束，1：非正常结束
                    System.err.println("命令执行失败!");
            }
            inBr.close();
            in.close();

            File newfile = new File(dir + File.separator
                    + "yuicompressor-2.4.8.jar");
            if (newfile.exists()) {
                newfile.delete();
            }

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // this.changeOriginalFile(fileName+"-out.js");
    }

    /**
     * @param fileName 使用YUICompressor待压缩的文件
     * @return 压缩后的命令行输出
     */
    public String constructCommandLine(String fileName) {
        int index;
        if ((index = fileName.lastIndexOf('\\')) != -1) {
            fileName = fileName.substring(index + 1, fileName.length());
        }
        String stringbuffer = new String("java -jar yuicompressor-2.4.8.jar ");
        String minifiedFileName = fileName.substring(0, fileName.length() - 3)
                + "-out.js";
        stringbuffer += fileName;
        //stringbuffer += " --nomunge  --preserve-semi --disable-optimizations -o ";
        stringbuffer += " --preserve-semi --disable-optimizations -o ";
        stringbuffer += minifiedFileName;
        return stringbuffer.toString();
    }

    /**
     * 重命名，将a-out.js命名为a.js，即删除保护前的文件
     *
     * @param fileName 待重命名的文件名
     */
    public void changeOriginalFile(String fileName) {
        String minifiedFileName = fileName.substring(0, fileName.length() - 3)
                + "-out.js";
        File minifiedFile = new File(minifiedFileName);
        // 如果文件存在，则用简化后的代码填充源文件
        if (minifiedFile.exists()) {
            File originalFile = new File(fileName);
            if (originalFile.exists()) {
                originalFile.delete();
                if (!minifiedFile.renameTo(new File(fileName)))
                    ;
                System.out.println("文件重命名失败");
            }

        }
    }

    /**
     * 首先需要检测顶级作用域中是否有函数定义，如果没有，则自己构造
     */
    public void getFunction() {

		/*if (this.astRoot != null) {
			int functions = 0;
			ArrayList<FunctionNode> functionNodes = new ArrayList<FunctionNode>();

			if (astRoot != null)
				functions = getfunctionsOfGlobal(astRoot, functionNodes);
			else
				System.out.println("生成抽象语法树出错");

			// 我们首先做的时候，每次都生成新的函数
			functions = 0;

			if (functions != 0) {
				// 随机选取一个函数，作为要生成的函数
				Random random = new Random();
				this.internalfuncNode = functionNodes.get(random
						.nextInt(functions));
				this.functionInternal = true;
			} else {
				// 构造一个额外的函数
				this.functionInternal = false;
			}
		}*/
    }

    /**
     * 构造一个外部函数的函数体
     *
     * @return 外部函数的函数体
     */
    private String constructExternalFunctionBody() {
        String funcName = obfuscation.getValidWord(this.astRoot);
        this.constructExternalFunctionCallNode(funcName);
        return funcName + " = function(){}";
    }

    /**
     * 构造一个外部函数的函数调用结点
     *
     * @param funcName 函数名
     */
    private void constructExternalFunctionCallNode(String funcName) {
		/* FunctionNode funcNode = new FunctionNode(); */
        FunctionCall functionCall = new FunctionCall();
        Name name = new Name(0, funcName);

        ExpressionStatement statement = new ExpressionStatement();
        statement.setExpression(functionCall);
        functionCall.setArguments(null);
        functionCall.setTarget(name);
        this.externalFuncCallNode = statement;
    }

    /**
     * 获取全局作用域中函数定义的个数
     *
     * @param astRoot
     *            抽象语法树的根
     * @param functionNodes
     *            所有函数定义的集合
     * @return 全局作用域中函数定义数
     */
	/*public int getfunctionsOfGlobal(AstRoot astRoot,
									ArrayList<FunctionNode> functionNodes) {
		int funcNums = 0;
		AstNode node = (AstNode) astRoot.getFirstChild();
		while (node != null) {
			if (node.getType() == Token.FUNCTION) {
				funcNums++;
				functionNodes.add((FunctionNode) node);
			}

			node = (AstNode) node.getNext();
		}
		return funcNums;
	}*/

    /**
     * 获取防篡改函数头部的代码 Part1
     *
     * @return 防篡改函数头部代码
     */
    private String getFunctionHeader() {
        return "function x(p,q){";
    }

    /**
     * 获取解码部分的代码 Part2
     *
     * @return 解码部分的代码
     */
    private String getTheDecodePart() {
        // 这些代码是用于在防篡改函数中解密要使用的函数的
        return "var V,QQ=window,c='';var t=unescape('%0C%3A9%7B%17L%5D%7C%220L%7D%5C_I%5D%7D%7C%3DQ%01O%113%1B%7DL%3BPY_Y*61PL%1F%120%19%7DL*GiN%5B719%1DLU%10%3B%1B%26%208%0B%19%5BY.3%27%1DLL%0C0%187%03%3B%5B%19%19Y%2C08W%03Y%5D%7C%0E%2C%008AV_l0%3B%7D%1D%03Y%108%0A6L%7D%5DT_Z%3D%3E.%5BL%1F%21%007%1B0%1Amlnf%11%13%1C%7F%3Dc%3D%100%0D%20%12meyf%13%12%1Fp+c2%160%1BL%7DNOTJ*61PO%14W%24%03%7DL%3DGTI%5D%2C*%3DJ%00N%5D%7C%1A%3B%0D+O%5D_%5B');for(var xk=0,kx=0;xk<t.length;xk++,kx++){if(kx==16){kx=0}c+=String.fromCharCode(t.charCodeAt(xk)^q.charCodeAt(kx))}Q=c.split(unescape('%23%23'));";
    }

    /**
     * 获取查看代码完整性和是否有调试器部分的代码 Part3
     *
     * @return 返回查看代码完整性和是否有调试器部分的代码
     */
    private String getTheTamperAndDebuggedPart() {
        return "if(QQ[Q[0]](Q[1])[Q[2]](x)){V=Q[15]}if(QQ[Q[3]]&&QQ[Q[3]][Q[4]]&&QQ[Q[3]][Q[5]]&&QQ[Q[3]][Q[5]][Q[6]]()[Q[7]]([Q[8]])!==-1){V=Q[15]}if(QQ[Q[3]]&&QQ[Q[3]][Q[9]]){QQ[Q[3]][Q[10]]('a');QQ[Q[3]][Q[11]]('a');if(QQ[Q[3]][Q[9]][Q[12]]!=0){V=Q[15]}}";
    }

    /**
     * 获取反调试部分 Part4
     *
     * @return 反调试部分的代码
     */
    private String getAntiDebugPart() {
        return "function QQQ(QO){if(QO%20===0){(function(){})[Q[16]](Q[17])()}else{}QQQ(++QO)}try{QQQ(0)}catch(QO){}";
    }

    /**
     * 获取解码函数部分的代码 Part5
     *
     * @return 解码函数部分的代码
     */
    private String getTheDecodeFunctionPart() {
        return "(function(){V='';if(V==Q[15]){}else{X=x.toString();p=unescape(p);if(X.length>p.length){X=X.substr(0,p.length);}else{var round=Math.floor(p.length/X.length);var addition=p.length-round*X.length;var t=X;for(var i=1;i<round;++i){X+=t}X+=t.substr(0,addition)}for(var z=0;z<p.length;z++){V+=String.fromCharCode(X.charCodeAt(z)^p.charCodeAt(z))}}(function(){}).constructor(V)();})();}";
    }

    /**
     * 获取防篡改函数尾部的代码 Part6
     *
     * @return 防篡改函数尾部的代码，只用于生成代码
     */
    private String getFunctionTailPart(String checksum) {
        return ")('" + checksum + "', '^_^>o<~_~^o^(::)');";
    }

    /**
     * 获取添加入的防篡改部分的代码长度
     *
     * @return 防篡改代码长度
     */
    private int getAntidbgCodeLength() {
        // 将所有构造防篡改函数的部分相加，为最后生成校验和做准备
		/*
		 * int length = this.getFunctionHeader().length() +
		 * this.getTheDecodePart().length() +
		 * this.getTheTamperAndDebuggedPart().length() +
		 * this.getAntiDebugPart().length() +
		 * this.getTheDecodeFunctionPart().length() +
		 * this.getFunctionTailPart().length(); return length;
		 */
        return this.antiTamperCodeLength;
    }

    /**
     * 计算校验和
     *
     * @param functionGenerated
     * @return 返回计算得出的校验和
     */
    public String caculateCheckSum(String functionGenerated) {

        // 存储生成的校验和
        String checksumString = "";

        // antiTamperSource为防篡改函数的文本
        // generatedFunctionString为要生成的函数（其中包括了反调试代码）
        String antiTamperSource = this.getAntiTamperFunctionSource();
        String generatedFunctionString = functionGenerated;

        // lengthOfAntiTamper 防篡改函数文本的长度
        // lengthOfGennerateFunction
        // 要生成函数的文本长度，因为处理两个双引号，所以需要额外字符进行转义，一个双引号需要4个额外字符
        int lengthOfAntiTamper = this.getAntidbgCodeLength();
        int lengthOfGeneratedFunction = generatedFunctionString.length() - 2 * 4;

        //System.out.println(lengthOfGeneratedFunction);

        // 搭建Rhino的执行环境，利用Rhino来执行escape函数，
        Context ctx = Context.enter();
        Scriptable scope = ctx.initStandardObjects();

        // jsStr用于构造需要再Rhino中执行的字符串
        String jsStr = "";
        String antiTamperSourceRhino = "var antiTamperSourceRhino = \""
                + antiTamperSource + "\";";
        String generatedFunctionStringRhino = "var generatedFunctionStringRhino = \""
                + generatedFunctionString + "\";";
        String temp = "var temp = '';";

        if (lengthOfAntiTamper >= lengthOfGeneratedFunction) {
            jsStr = antiTamperSourceRhino
                    + generatedFunctionStringRhino
                    + temp
                    + "for(var i = 0; i < "
                    + lengthOfGeneratedFunction
                    + "; ++i)"
                    + "temp += String.fromCharCode(antiTamperSourceRhino.charCodeAt(i) ^ generatedFunctionStringRhino.charCodeAt(i));"
                    + "escape(temp);";
            Object result = ctx.evaluateString(scope, jsStr, null, 0, null);
            checksumString = result.toString();
        } else {
            // 将x的长度扩充至p
            int round = (int) Math.floor(lengthOfGeneratedFunction
                    / lengthOfAntiTamper);
            int addition = lengthOfGeneratedFunction - round
                    * lengthOfAntiTamper;
            String tempString = antiTamperSource;
            for (int i = 1; i < round; ++i)
                antiTamperSource += tempString;
            antiTamperSource += tempString.substring(0, addition);

            // 构造字符串，用于Rhino解释escape函数得到escape后的checksumString
            antiTamperSourceRhino = "var antiTamperSourceRhino = \""
                    + antiTamperSource + "\";";
            jsStr = antiTamperSourceRhino
                    + generatedFunctionStringRhino
                    + temp
                    + "for(var i = 0; i < "
                    + lengthOfGeneratedFunction
                    + "; ++i)"
                    + "temp += String.fromCharCode(antiTamperSourceRhino.charCodeAt(i) ^ generatedFunctionStringRhino.charCodeAt(i));"
                    + "escape(temp);";
            Object result = ctx.evaluateString(scope, jsStr, null, 0, null);
            checksumString = result.toString();
        }
        // System.out.println(checksumString);
        return checksumString;
    }

    /**
     * 获取防篡改函数的函数体
     *
     * @return 防篡改函数的函数体
     */
    private String getAntiTamperFunctionSource() {
        StringBuffer buffer = new StringBuffer("");

        // 将各个部分的代码合并起来
        buffer.append(this.getFunctionHeader());
        buffer.append(this.getTheDecodePart());
        buffer.append(this.getTheTamperAndDebuggedPart());
        buffer.append(this.getAntiDebugPart());
        buffer.append(this.getTheDecodeFunctionPart());

        this.antiTamperCodeLength = buffer.toString().length();
        //System.out.println(buffer.toString());
        return buffer.toString();
    }

    /**
     * 构造完整的防篡改部分
     *
     * @param functionBody
     * @return 结合待生成的functionBody，构造完整的防篡改部分
     */
    public String constructAntiTamperPart(String functionBody) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("(");
        buffer.append(this.getFunctionHeader());
        buffer.append(this.getTheDecodePart());
        buffer.append(this.getTheTamperAndDebuggedPart());
        buffer.append(this.getAntiDebugPart());
        buffer.append(this.getTheDecodeFunctionPart());
        buffer.append(this.getFunctionTailPart(this.caculateCheckSum(functionBody)));
        //System.out.println(buffer.toString());
        return buffer.toString();
    }

    /**
     * 构造待生成的函数
     *
     * @return 与反调试结合在一起的函数体
     */
    private String consturctFunctionToBeGenerated() {
        // 添加反调试代码
        String antidbgCode = "(function(){try{function a(){(function(){}).constructor(\"+\"\\\"(function(f){(function a(){try{function b(i){if((''+(i/i)).length!==1||i%20===0){(function(){}).constructor('debugger')();}else{debugger;}b(++i);}b(0);} catch(e) {f.setTimeout(a, 50);}})()})(document.body.appendChild(document.createElement('frame')).contentWindow)\\\"\"+\")();}if(!/arm|ipad|ipod|iphone/i.test(navigator.platform)){if(document.body){a();}else{window.onload=a;}}}catch(e){}})();";

        // 使用generatedCode构造需要通过防篡改过程生成的函数代码
        String generatedCode = "";
		/*if (this.functionInternal) {
			//
			generatedCode = antidbgCode
					+ this.internalfuncNode.getFunctionName().getIdentifier()
					+ " = " + this.internalfuncNode.getBody().toSource();
		} else {*/
        // 用构造的外部函数作为待生成的对象
        generatedCode = antidbgCode + this.constructExternalFunctionBody();
        //}
        //System.out.println(generatedCode);
        return generatedCode;
    }

    /**
     * 处理语法树
     */
	/*private void processAstTree() {
		// 若使用内部函数，将内部函数从语法树中删除
		if (this.functionInternal) {
			astRoot.removeChild(this.internalfuncNode);
		} else {
			// 若定义额外的函数，将其添加到抽象语法树中
			astRoot.addChild(this.externalFuncCallNode);
		}
	}*/
}
