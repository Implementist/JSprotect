package com.rocky.protect;

import jdk.nashorn.internal.ir.ForNode;
import org.mozilla.javascript.ast.*;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.Token;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.io.File;

public class Obfuscation {

    private ArrayList<String> ones;               //
    private ArrayList<String> twos;               //
    private ArrayList<String> threes;             // three为包括三个字符的变量名集合
    private int total = 19982;                    // 三个字符的所有变量名数，但应该从中间去掉JavaScript关键字
    private ArrayList<String> NamesInFlattern;    //
    private Random random;                        // 随机数对象实例
    private ArrayList<AstNode> opaques;           // 非透明谓词
    private int flatternStrength;                 // 平展控制流混淆强度
    private int opaqueStrength;                   // 非透明谓词混淆强度
    private int flatternCount;                    // 平展控制流数
    private int opaqueCount;                      // 非透明谓词数
    private AstRoot root;                         //
    private ArrayList<AstNode> astNodelist;       // 用于进行混淆

    /**
     * 构造函数
     */
    public Obfuscation() {
        this.NamesInFlattern = new ArrayList<String>();
        this.initVariablesPool();
        this.random = new Random();
        this.opaques = new ArrayList<AstNode>();
        this.initOpaques();
        this.flatternCount = 0;
        this.opaqueCount = 0;
        this.astNodelist = new ArrayList<AstNode>();
    }

    /**
     * 获取平展控制流数
     *
     * @return 平展控制流数
     */
    public int getFlatternCount() {
        return flatternCount;
    }

    /**
     * 获取非透明谓词数
     *
     * @return
     */
    public int getOpaqueCount() {
        return opaqueCount;
    }

    /**
     * 获取平展控制流混淆强度
     *
     * @return 平展控制流混淆强度
     */
    public int getFlatternStrength() {
        return flatternStrength;
    }

    /**
     * 设置平展控制流混淆强度
     *
     * @param flatternStrength 平展控制流混淆强度
     */
    public void setFlatternStrength(int flatternStrength) {
        this.flatternStrength = flatternStrength;
    }

    /**
     * 获取非透明谓词混淆强度
     *
     * @return 非透明谓词混淆强度
     */
    public int getOpaqueStrength() {
        return opaqueStrength;
    }

    /**
     * 设置非透明谓词混淆强度
     *
     * @param opaqueStrength 非透明谓词混淆强度
     */
    public void setOpaqueStrength(int opaqueStrength) {
        this.opaqueStrength = opaqueStrength;
    }

    /**
     * 初始化谓词库
     */
    private void initOpaques() {
        Ast ast = new Ast();
        AstRoot root;
        try {
            String currentPathString = this.getClass().getClassLoader().getResource("/").getPath();
            currentPathString = currentPathString.substring(1, currentPathString.length());
            int index = currentPathString.lastIndexOf("out");

            //System.out.println(currentPathString);

            String opaquePathString = currentPathString.substring(0, index + 3);
            opaquePathString.replace("/", "\\");

            File file = new File(opaquePathString + File.separator + "Opaques");

            //System.out.println(opaquePathString);
            System.out.println("OpaquesSets: " + file);

            if (file.isDirectory()) {
                File[] files = file.listFiles();

                for (File fileItem : files) {
                    // System.out.println(fileItem.getAbsolutePath());
                    root = ast.getAstRoot(fileItem.getAbsolutePath(), 0);
                    if (root == null)
                        System.out.println("Null Root");

                    this.opaques.add((AstNode) root.getFirstChild());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 初始化变量池，以后的变量名均可从该变量池中取
     */
    private void initVariablesPool() {

        ones = new ArrayList<String>();
        for (char c = 'a'; c <= 'z'; c++)
            ones.add(Character.toString(c));
        for (char c = 'A'; c <= 'Z'; c++)
            ones.add(Character.toString(c));

        twos = new ArrayList<String>();
        for (int i = 0; i < ones.size(); i++) {
            String one = ones.get(i);
            for (char c = 'a'; c <= 'z'; c++)
                twos.add(one + Character.toString(c));
            for (char c = 'A'; c <= 'Z'; c++)
                twos.add(one + Character.toString(c));
            for (char c = '0'; c <= '9'; c++)
                twos.add(one + Character.toString(c));
        }

        threes = new ArrayList<String>();
        for (int i = 0; i < twos.size(); i++) {
            String two = twos.get(i);
            for (char c = 'a'; c <= 'z'; c++)
                threes.add(two + Character.toString(c));
            for (char c = 'A'; c <= 'Z'; c++)
                threes.add(two + Character.toString(c));
            for (char c = '0'; c <= '9'; c++)
                threes.add(two + Character.toString(c));
        }

        // Remove two-letter JavaScript reserved words and built-in globals...
        twos.remove("as");
        twos.remove("is");
        twos.remove("do");
        twos.remove("if");
        twos.remove("in");
        // twos.removeAll(builtin);

        // Remove three-letter JavaScript reserved words and built-in globals...
        threes.remove("for");
        threes.remove("int");
        threes.remove("new");
        threes.remove("try");
        threes.remove("use");
        threes.remove("var");
        // threes.removeAll(builtin);

        // That's up to ((26+26)*(1+(26+26+10)))*(1+(26+26+10))-8
    }

    /**
     * 获取有效的变量名，不能与文件中其他函数重名
     *
     * @param node
     * @return 变量名
     */
    public String getValidWord(AstNode node) {
        this.NamesInFlattern.clear();
        this.collectNames(node);

        String word = this.getWordFromThePool();

        while (this.NamesInFlattern.contains(word)) {
            word = this.getWordFromThePool();
        }
        return word;
    }

    /**
     * 从变量池中选取变量名
     *
     * @return 从变量池中选取的变量名
     */
    public String getWordFromThePool() {
        int index = this.random.nextInt(this.total);
        String word = this.threes.get(index);
        return word;
    }

    /**
     * 获取该文件中所有的函数名
     *
     * @param node 语法树遍历的起始位置
     */
    private void collectNames(AstNode node) {
        node.visit(new Visitor());
    }

    /**
     * 实现NodeVisitor接口，以方便提取所有的函数名
     *
     * @author Marion
     */
    class Visitor implements NodeVisitor {

        public boolean visit(AstNode node) {
            if (node.getType() == Token.NAME)
                NamesInFlattern.add(((Name) node).getIdentifier());
            return true;
        }
    }

    /**
     * 平展控制流的实现
     *
     * @param node 要平展语句链表的第一个结点
     */
    public void flattenControlFlow(AstNode node) {

        if (this.random.nextInt(110) < this.flatternStrength) {
            //if (this.random.nextInt(90) < 60) {
            // tmp用于遍历过程，word1用于switch，word2用于for条件
            AstNode tmp = node;
            String word1, word2;

            //Node parent=node.getParent();
            //if(parent==null|| parent.getFirstChild() == null) return;

            ArrayList<AstNode> statements = new ArrayList<AstNode>();

            while (tmp != null) {
                statements.add(tmp);
                tmp = (AstNode) (tmp.getNext());
            }

            // 如果语句块太少，则不进行平展控制流
            if (statements.size() <= 5)
                return;

            // 下面代码，集中进行控制流压扁
            word1 = this.getValidWord(node);
            word2 = this.getValidWord(node);

            while (word1.equals(word2))
                word2 = this.getValidWord(node);

            // 记住前后引用，把它变成平展控制
            this.constructSwitchCase(word1, word2, statements, node);
            ++this.flatternCount;
        }
    }

    /**
     * FOR结构平展
     *
     * @param node
     */
    public void flattenControlFlow_FOR(AstNode node) {
        if (this.random.nextInt(90) < this.flatternStrength) {
            Node parent = node.getParent();
            if (parent == null || parent.getFirstChild() == null) {
                return;
            }
            System.out.println("进入FOR节点处理函数！");
            AstNode NewNode = (AstNode) (((Loop) node).getBody()).getFirstChild();
            AstNode tmp = NewNode;
            String word1, word2;
            word1 = this.getValidWord(node);
            word2 = this.getValidWord(node);
            while (word1.equals(word2))
                word2 = this.getValidWord(node);
            ArrayList<AstNode> statements = new ArrayList<AstNode>();
            if (NewNode == null) {
                NewNode = ((Loop) node).getBody();
                if (Searchbreak(NewNode)) return;
                statements.add(NewNode);
            } else {
                while (tmp != null) {
                    if (Searchbreak(tmp)) return;
                    statements.add(tmp);
                    tmp = (AstNode) tmp.getNext();
                }
            }
            this.constructSwitchCase_FOR(word1, word2, statements, node);
        }
    }

    /**
     * IF结构平展
     *
     * @param node
     */
    public void flattenControlFlow_IF(AstNode node) {

        if (this.random.nextInt(90) < this.flatternStrength) {
            String word1, word2;
            Node parent = node.getParent();
            if (parent == null || parent.getFirstChild() == null) return;

            ArrayList<AstNode> statementsThen = new ArrayList<AstNode>();
            ArrayList<AstNode> statementsElse = new ArrayList<AstNode>();

            System.out.println("进入IF节点处理函数！");

            AstNode ThenNode = (AstNode) ((((IfStatement) (node)).getThenPart())).getFirstChild();
            if (ThenNode == null) {
                ThenNode = (((IfStatement) (node)).getThenPart());
                if (Searchbreak(ThenNode)) return;
                statementsThen.add(ThenNode);
            } else {
                while (ThenNode != null) {
                    if (Searchbreak(ThenNode)) return;
                    statementsThen.add(ThenNode);
                    ThenNode = (AstNode) (ThenNode.getNext());
                }
            }
            //System.out.println("Then分支： "+statementsThen.get(0).toSource());

            if (((IfStatement) (node)).getElsePart() != null) {
                AstNode ElseNode = (AstNode) ((((IfStatement) (node)).getElsePart())).getFirstChild();
                if (ElseNode == null) {
                    ElseNode = (((IfStatement) (node)).getElsePart());
                    if (Searchbreak(ElseNode)) return;
                    statementsElse.add(ElseNode);
                } else {
                    while (ElseNode != null) {
                        if (Searchbreak(ElseNode)) return;
                        statementsElse.add(ElseNode);
                        ElseNode = (AstNode) (ElseNode.getNext());
                    }
                }
                //System.out.println("Else分支： "+statementsElse.get(0).toSource());
            }
            word1 = this.getValidWord(node);
            word2 = this.getValidWord(node);
            while (word1.equals(word2))
                word2 = this.getValidWord(node);
            this.constructSwitchCase_IF(word1, word2, statementsThen, statementsElse, node);
        }
    }

    /**
     * 构造switch语句
     *
     * @param wordForLoop   Loop循环使用的变量名
     * @param wordForSwitch Switch语句使用的变量名
     * @param statements    Switch语句待填充的各语句
     * @param node          要平展的东西
     *                      ，一般为一个functionBody里第一个
     */
    private AstNode constructSwitchCase(String wordForLoop,
                                        String wordForSwitch, ArrayList<AstNode> statements, AstNode node) {
        ArrayList<AstNode> statementsAfterReorder = new ArrayList<AstNode>();

        int len = statements.size();
        int sequence[] = new int[len];
        Name identifierForLoop = new Name(0, wordForLoop);
        Name identifierForSwitch = new Name(0, wordForSwitch);

        // 将statements复制
        for (int i = 0; i < len; ++i) {
            statementsAfterReorder.add(statements.get(i));
        }

        // 对这些语句打乱顺序
        Collections.shuffle(statementsAfterReorder);

        // 获取sequence数组
        for (int i = 0; i < len; ++i)
            sequence[i] = statementsAfterReorder.indexOf(statements.get(i));

        // 构建switch语句的变量的声明和初始化，即 "var IXG = i"
        VariableDeclaration variableDeclaration = new VariableDeclaration();
        ArrayList<VariableInitializer> variables = new ArrayList<VariableInitializer>();
        VariableInitializer variableInit = new VariableInitializer();
        variableInit.setTarget(identifierForSwitch);
        NumberLiteral value = new NumberLiteral();
        value.setValue("" + sequence[0]);
        variableInit.setInitializer(value);
        variables.add(variableInit);
        variableDeclaration.setVariables(variables);
        variableDeclaration.setIsStatement(true); // 不设置这句，var声明语句后无分号

        // 构建FoorLoop
        ForLoop forLoop = new ForLoop();

        VariableDeclaration initializer = new VariableDeclaration();
        ArrayList<VariableInitializer> vars = new ArrayList<VariableInitializer>();

        // 设置for循环第一句
        VariableInitializer varinit = new VariableInitializer();
        varinit.setTarget(identifierForLoop);
        KeywordLiteral keywordTrue = new KeywordLiteral();
        keywordTrue.setType(Token.TRUE);
        varinit.setInitializer(keywordTrue);
        vars.add(varinit);
        initializer.setVariables(vars);
        forLoop.setInitializer(initializer);

        // 设置for循环第二句和第三句
        forLoop.setIncrement(new EmptyExpression());
        forLoop.setCondition(identifierForLoop);

        // 设置for循环的body
        Scope scope = new Scope();
        forLoop.setBody(scope);

        AstNode parent = node.getParent();
        parent.removeChildren();
        parent.addChild(variableDeclaration);
        parent.addChild(forLoop);

        // 构建Switch语句
        SwitchStatement switchStatement = new SwitchStatement();
        scope.addChild(switchStatement);
        switchStatement.setExpression(identifierForSwitch);

        // 用于存储Switch语句的每个Cas
        ArrayList<SwitchCase> cases = new ArrayList<SwitchCase>(len);
        for (int i = 0; i < len; ++i)
            cases.add(new SwitchCase());

        NumberLiteral number;

        // 创建每一个case语句，添加到Switch中
        for (int i = 0; i < len; ++i) {
            // 设置case item, item为case匹配的条件
            SwitchCase eachCase = new SwitchCase();
            ArrayList<AstNode> statementsOfCase = new ArrayList<AstNode>();
            number = new NumberLiteral();
            number.setValue("" + sequence[i]);
            eachCase.setExpression(number);
            statementsOfCase.add(statements.get(i));

            if (i != (len - 1)) {
                // 设置类型为赋值表达式
                ExpressionStatement expression = new ExpressionStatement();
                expression.setType(Token.EXPR_RESULT);

                Assignment assign = new Assignment();
                assign.setType(Token.ASSIGN);
                assign.setLeft(identifierForSwitch);
                number = new NumberLiteral();
                number.setValue("" + sequence[i + 1]);
                assign.setRight(number);
                expression.setExpression(assign);
                statementsOfCase.add(expression);
            } else // 最后一句单独处理
            {
                // 设置类型为赋值表达式
                ExpressionStatement expression = new ExpressionStatement();
                expression.setType(Token.EXPR_RESULT);

                // 设置为基础赋值类型
                Assignment assign = new Assignment();
                assign.setType(Token.ASSIGN);
                assign.setLeft(identifierForLoop);
                KeywordLiteral keywordFalse = new KeywordLiteral();
                keywordFalse.setType(Token.FALSE);
                assign.setRight(keywordFalse);
                expression.setExpression(assign);
                statementsOfCase.add(expression);
            }
            // 在每一个case语句最后添加break语句，并将case语句添加到cases集合中
            statementsOfCase.add(new BreakStatement());
            eachCase.setStatements(statementsOfCase);
            cases.set(sequence[i], eachCase);
        }
        // 设置所有switch语句的case集合
        switchStatement.setCases(cases);
        return forLoop;
    }

    /**
     * case 乱序方法
     *
     * @param sequence
     * @param len
     * @return
     */
    public int[] Reorder(int sequence[], int len) {
        int[] temp = new int[len];
        int RandCount;
        int Position = 0;
        do {
            Random rand = new Random();
            int r = len - Position;
            RandCount = rand.nextInt(r);
            temp[Position] = sequence[RandCount];
            Position++;
            sequence[RandCount] = sequence[r - 1];// 将最后一位数值赋值给已经被使用的位置
        } while (Position < len);
        return temp;
    }

    /**
     * 查找节点中是否包含break以及continue语句
     *
     * @param node
     * @return
     */
    public boolean Searchbreak(AstNode node) {
        if (node.getType() == Token.BREAK || node.getType() == Token.CONTINUE) {
            return true;
        }
        if (node.getType() == Token.IF) {
            AstNode ThenNode = (AstNode) ((((IfStatement) (node)).getThenPart())).getFirstChild();
            if (ThenNode == null) {
                ThenNode = ((IfStatement) (node)).getThenPart();
                if (ThenNode.getType() == Token.BREAK || ThenNode.getType() == Token.CONTINUE) {
                    return true;
                }
            } else {
                while (ThenNode != null) {
                    if (ThenNode.getType() == Token.BREAK || ThenNode.getType() == Token.CONTINUE) {
                        return true;
                    }
                    ThenNode = (AstNode) (ThenNode.getNext());
                }
            }
            if (((IfStatement) (node)).getElsePart() != null) {
                AstNode ElseNode = (AstNode) ((((IfStatement) (node)).getElsePart())).getFirstChild();
                if (ElseNode == null) {
                    ElseNode = ((IfStatement) (node)).getElsePart();
                    if (ElseNode.getType() == Token.BREAK || ElseNode.getType() == Token.CONTINUE) {
                        return true;
                    }
                } else {
                    while (ElseNode != null) {
                        if (ElseNode.getType() == Token.BREAK || ElseNode.getType() == Token.CONTINUE) {
                            return true;
                        }
                        ElseNode = (AstNode) (ElseNode.getNext());
                    }
                }
            }
        }
        return false;
    }

    /**
     * 构造FOR平展后的Switch结构
     *
     * @param wordForLoop
     * @param wordForSwitch
     * @param statements
     * @param node
     * @return
     */
    private AstNode constructSwitchCase_FOR(String wordForLoop, String wordForSwitch, ArrayList<AstNode> statements, AstNode node) {
        int j, size;

        size = statements.size();
        int CaseSize = size / 4;
        int len = CaseSize + 1;
        if ((size % 4) != 0)
            len = len + 1;
        //求取总的case数量
        int sequence[] = new int[len];
        for (int i = 0; i < len; i++)
            sequence[i] = i;
        if (len > 2) {
            sequence = Reorder(sequence, len);
        }

        System.out.println("构造IF节点的SWITCH结构！");
        Name identifierForLoop = new Name(0, wordForLoop);
        Name identifierForSwitch = new Name(0, wordForSwitch);

        //	定义完成switch的跳转变量的声明;
        VariableDeclaration variableDeclaration = new VariableDeclaration();
        ArrayList<VariableInitializer> variables = new ArrayList<VariableInitializer>();
        VariableInitializer variableInit = new VariableInitializer();
        variableInit.setTarget(identifierForSwitch);
        NumberLiteral value = new NumberLiteral();
        value.setValue("" + sequence[0]);
        variableInit.setInitializer(value);
        variables.add(variableInit);
        variableDeclaration.setVariables(variables);
        variableDeclaration.setIsStatement(true);

        //添加FORLOOP
        ForLoop forLoop = new ForLoop();
        VariableDeclaration initializer = new VariableDeclaration();
        ArrayList<VariableInitializer> vars = new ArrayList<VariableInitializer>();
        VariableInitializer variableInit1 = new VariableInitializer();
        variableInit1.setTarget(identifierForLoop);
        KeywordLiteral keyWord = new KeywordLiteral();
        keyWord.setType(Token.TRUE);
        variableInit1.setInitializer(keyWord);
        vars.add(variableInit1);
        initializer.setVariables(vars);
        forLoop.setInitializer(initializer);
        forLoop.setIncrement(new EmptyExpression());
        forLoop.setCondition(identifierForLoop);
        Scope scopeloop = new Scope();
        forLoop.setBody(scopeloop);
        SwitchStatement switchstatement = new SwitchStatement();
        scopeloop.addChild(switchstatement);
        switchstatement.setExpression(identifierForSwitch);
        ArrayList<SwitchCase> cases = new ArrayList<SwitchCase>(len);
        for (int i = 0; i < len; i++)
            cases.add(new SwitchCase());

        Node parent = node.getParent();
        if (parent == null) {
            //System.out.println("node is null");
            return null;
        }

        //将生成的Forloop插入父节点
        parent.addChildBefore(variableDeclaration, node);
        AstNode IniNode = ((ForLoop) node).getInitializer();
        if (!(IniNode instanceof EmptyExpression)) {
            ExpressionStatement IniExpre = new ExpressionStatement();
            IniExpre.setType(Token.EXPR_RESULT);
            IniExpre.setExpression(IniNode);
            parent.addChildBefore(IniExpre, node);
        }

        IfStatement ifstatement = new IfStatement();
        //InfixExpression condition=(InfixExpression)((ForLoop)node).getCondition();
        ifstatement.setCondition(((ForLoop) node).getCondition());

        //设置for成立部分跳转;
        ExpressionStatement expressionThen = new ExpressionStatement();
        expressionThen.setType(Token.EXPR_RESULT);
        Scope scopethen = new Scope();
        Assignment assign = new Assignment();
        assign.setType(Token.ASSIGN);
        assign.setLeft(identifierForSwitch);
        //System.out.println(sequence[1]);
        NumberLiteral number = new NumberLiteral();
        number.setValue("" + sequence[1]);
        assign.setRight(number);
        expressionThen.setExpression(assign);
        scopethen.addChild(expressionThen);
        ifstatement.setThenPart(scopethen);
        //设立for不成立部分跳转
        ExpressionStatement expressionElse = new ExpressionStatement();
        expressionElse.setType(Token.EXPR_RESULT);
        Assignment assignElse = new Assignment();
        assignElse.setType(Token.ASSIGN);
        assignElse.setLeft(identifierForLoop);
        KeywordLiteral keywordLoop = new KeywordLiteral();
        keywordLoop.setType(Token.FALSE);
        assignElse.setRight(keywordLoop);
        expressionElse.setExpression(assignElse);
        Scope scopeElse = new Scope();
        scopeElse.addChild(expressionElse);
        ifstatement.setElsePart(scopeElse);
        //把if块加入到case中
        SwitchCase eachCaseFor = new SwitchCase();
        ArrayList<AstNode> statementCaseFor = new ArrayList<AstNode>();
        NumberLiteral number1 = new NumberLiteral();
        number1.setValue("" + sequence[0]);
        eachCaseFor.setExpression(number1);
        //System.out.println(ifstatement.toSource());
        statementCaseFor.add(ifstatement);
        statementCaseFor.add(new BreakStatement());
        eachCaseFor.setStatements(statementCaseFor);
        cases.set(sequence[0], eachCaseFor);

        for (int i = 0, k = 1; size != 0; ) {
            SwitchCase eachCase1 = new SwitchCase();
            ArrayList<AstNode> statementsOfCase = new ArrayList<AstNode>();
            NumberLiteral number2 = new NumberLiteral();
            number2.setValue("" + sequence[k]);
            eachCase1.setExpression(number2);
            ExpressionStatement expression = new ExpressionStatement();
            expression.setType(Token.EXPR_RESULT);
            //System.out.println(size);
            if (size > 4) {
                //System.out.println("size>4");
                for (j = 0; j < 4 && i < statements.size() - j; j++)
                    statementsOfCase.add(statements.get(i + j));
                size -= 4;
                i = i + 4;
                Assignment assignCase = new Assignment();
                assignCase.setType(Token.ASSIGN);
                assignCase.setLeft(identifierForSwitch);
                NumberLiteral numberCase = new NumberLiteral();
                numberCase.setValue("" + sequence[k + 1]);
                assignCase.setRight(numberCase);
                expression.setExpression(assignCase);
                statementsOfCase.add(expression);
            } else {
                for (int n = 0; n < size; n++)
                    statementsOfCase.add(statements.get(i + n));
                Assignment assignLast = new Assignment();
                assignLast.setType(Token.ASSIGN);
                assignLast.setLeft(identifierForLoop);
                NumberLiteral numberLast = new NumberLiteral();
                numberLast.setValue("" + sequence[0]);
                assignLast.setLeft(identifierForSwitch);
                assignLast.setRight(numberLast);
                expression.setExpression(assignLast);
                statementsOfCase.add(expression);
                AstNode NIncrement = (AstNode) ((ForLoop) node).getIncrement();
                if (!(NIncrement instanceof EmptyExpression)) {
                    ExpressionStatement expressionIn = new ExpressionStatement();
                    expressionIn.setType(Token.EXPR_RESULT);
                    expressionIn.setExpression(NIncrement);
                    statementsOfCase.add(expressionIn);
                }
                size = 0;
            }
            statementsOfCase.add(new BreakStatement());
            eachCase1.setStatements(statementsOfCase);
            //System.out.println("k:"+k);
            cases.set(sequence[k], eachCase1);
            k++;
        }
        switchstatement.setCases(cases);
        parent.replaceChild(node, forLoop);
        System.out.println("-----------------FOR平展结束----------------------");
        return forLoop;
    }

    /**
     * 构造IF平展后的Switch结构的方法
     *
     * @param wordForLoop
     * @param wordForSwitch
     * @param statementsThen
     * @param statementsElse
     * @param node
     * @return
     */

    private AstNode constructSwitchCase_IF(String wordForLoop, String wordForSwitch, ArrayList<AstNode> statementsThen, ArrayList<AstNode> statementsElse, AstNode node) {
        int lenElse = statementsElse.size();
        int len = lenElse == 0 ? 2 : 3; //switch 的case数判定
        //int len = 3;
        //int sequence[]=new int[len];
        int sequence[] = {0, 1, 2};

        if (len > 2) {//乱序IF平展的3个case
            sequence = Reorder(sequence, len);
        } else {
            int temp[] = {0, 1};
            temp = Reorder(temp, len);
            sequence[0] = temp[0];
            sequence[1] = temp[1];
        }

        Name identifierForLoop = new Name(0, wordForLoop);
        Name identifierForSwitch = new Name(0, wordForSwitch);

        System.out.println("构造IF节点的SWITCH结构！");

        //	定义完成switch的跳转变量的声明;
        VariableDeclaration variableDeclaration = new VariableDeclaration();
        ArrayList<VariableInitializer> variables = new ArrayList<VariableInitializer>();
        VariableInitializer variableInit = new VariableInitializer();
        variableInit.setTarget(identifierForSwitch);
        NumberLiteral value = new NumberLiteral();
        //value.setValue(""+0);
        value.setValue("" + sequence[0]);
        variableInit.setInitializer(value);
        variables.add(variableInit);
        variableDeclaration.setVariables(variables);
        variableDeclaration.setIsStatement(true);

        //构建for循环
        ForLoop forLoop = new ForLoop();
        VariableDeclaration initializer = new VariableDeclaration();
        ArrayList<VariableInitializer> vars = new ArrayList<VariableInitializer>();
        VariableInitializer varInit = new VariableInitializer();
        varInit.setTarget(identifierForLoop);
        KeywordLiteral keyWord = new KeywordLiteral();
        keyWord.setType(Token.TRUE);
        varInit.setInitializer(keyWord);
        vars.add(varInit);
        initializer.setVariables(vars);
        forLoop.setInitializer(initializer);
        //forLoop.setInitializer(varInit);

        forLoop.setIncrement(new EmptyExpression());
        forLoop.setCondition(identifierForLoop);

        //利用forLoop来代替原本的if节点。
        Scope scopeloop = new Scope();
        forLoop.setBody(scopeloop);
        //设置for循环的作用域
        //AstNode parent=node.getParent();
        Node parent = node.getParent();
        //parent.removeChildren();
        //parent.addChild(variableDeclaration);
        //parent.addChild(forLoop);
        //parent.addChildBefore(variableDeclaration);
        //将声明的switch变量插入到原来if语句之前。
        parent.addChildBefore(variableDeclaration, node);
        parent.replaceChild(node, forLoop);


        //构造Switch三个模块，condition为0，then为1，else为2；
        SwitchStatement switchstatement = new SwitchStatement();
        scopeloop.addChild(switchstatement);
        switchstatement.setExpression(identifierForSwitch);

        ArrayList<SwitchCase> cases = new ArrayList<SwitchCase>();
        for (int i = 0; i < len; i++)
            cases.add(new SwitchCase());
        NumberLiteral num;
        //设置condition部分的模块
        SwitchCase IfCase = new SwitchCase();
        ArrayList<AstNode> statementsofIfCase = new ArrayList<AstNode>();
        //重新设计if-condition的两个scope；

        IfStatement ifstatement = new IfStatement();
        //InfixExpression condition=(InfixExpression)(((IfStatement)(node)).getCondition());
        AstNode condition = ((IfStatement) (node)).getCondition();
        //System.out.println("条件： "+condition.toSource());
        ifstatement.setCondition(condition);
        num = new NumberLiteral();
        num.setValue("" + sequence[0]);
        IfCase.setExpression(num);
        statementsofIfCase.add(ifstatement);
        //把if AstNode加入到case中;
        ExpressionStatement expressionThen = new ExpressionStatement();
        expressionThen.setType(Token.EXPR_RESULT);

        //把then部分的switchvar=1加入scopethen;
        Assignment assign = new Assignment();
        assign.setType(Token.ASSIGN);
        assign.setLeft(identifierForSwitch);
        num = new NumberLiteral();
        num.setValue("" + sequence[1]);//把then-part保存在case:1中;
        assign.setRight(num);
        expressionThen.setExpression(assign);

        //System.out.println("ThenCase设定：" + expressionThen.toSource());
        Scope scopethen = new Scope();
        ifstatement.setThenPart(scopethen);
        scopethen.addChild(expressionThen);

        //设置loop结束语句。
        ExpressionStatement expression = new ExpressionStatement();
        expression.setType(Token.EXPR_RESULT);
        Assignment assignloop = new Assignment();
        assignloop.setType(Token.ASSIGN);
        assignloop.setLeft(identifierForLoop);
        KeywordLiteral keywordFalse = new KeywordLiteral();
        keywordFalse.setType(Token.FALSE);
        assignloop.setRight(keywordFalse);
        expression.setExpression(assignloop);

        //如果存在else部分则申请scope存入switchvar=2；
        ExpressionStatement expressionElse = new ExpressionStatement();
        expressionElse.setType(Token.EXPR_RESULT);
        if (statementsElse.size() != 0) {
            Assignment assignelse = new Assignment();
            assignelse.setType(Token.ASSIGN);
            assignelse.setLeft(identifierForSwitch);
            num = new NumberLiteral();
            num.setValue("" + sequence[2]);
            assignelse.setRight(num);
            expressionElse.setExpression(assignelse);
        } else {
            expressionElse.setExpression(assignloop);
        }

        //System.out.println("ElseCase设定：" + expressionElse.toSource());
        Scope scopeelse = new Scope();
        ifstatement.setElsePart(scopeelse);
        scopeelse.addChild(expressionElse);

        //添加break语句，关联case序号和case
        statementsofIfCase.add(new BreakStatement());
        IfCase.setStatements(statementsofIfCase);
        cases.set(sequence[0], IfCase);

        //设置then_case
        SwitchCase thenCase = new SwitchCase();
        num = new NumberLiteral();
        num.setValue("" + sequence[1]);
        thenCase.setExpression(num);
        ArrayList<AstNode> statementsofThenCase = new ArrayList<AstNode>();
        for (int i = 0; i < statementsThen.size(); i++) {
            statementsofThenCase.add(statementsThen.get(i));
        }
        statementsofThenCase.add(expression);
        statementsofThenCase.add(new BreakStatement());
        thenCase.setStatements(statementsofThenCase);
        cases.set(sequence[1], thenCase);

        //设置else_case
        if (statementsElse.size() != 0) {
            SwitchCase elseCase = new SwitchCase();
            num = new NumberLiteral();
            num.setValue("" + sequence[2]);
            elseCase.setExpression(num);
            ArrayList<AstNode> statementsofElseCase = new ArrayList<AstNode>();
            for (int i = 0; i < statementsElse.size(); i++) {
                statementsofElseCase.add(statementsElse.get(i));
            }
            statementsofElseCase.add(expression);
            statementsofElseCase.add(new BreakStatement());
            elseCase.setStatements(statementsofElseCase);
            cases.set(sequence[2], elseCase);
        }

        switchstatement.setCases(cases);
        System.out.println("-----------------IF平展结束----------------------");
        return forLoop;
    }

    /**
     * 非透明谓词混淆
     */
    public void opaqueObfuscation() {

        int len = this.astNodelist.size();
        for (int i = 0; i < len; ++i) {
            AstNode node = this.astNodelist.get(i);

            // 随机数小于设置的强度，则进行混淆
            if (this.random.nextInt(100) < this.opaqueStrength && node != this.root) {
                // 随机选取一个非透明谓词
                int index = this.random.nextInt(this.opaques.size());

                IfStatement ifStatement = new IfStatement();
                InfixExpression condition = (InfixExpression) (((IfStatement) (this.opaques.get(index))).getCondition());

                ifStatement.setCondition(condition);

                Scope scope = new Scope();
                ifStatement.setThenPart(scope);

                // 添加谓词
                AstNode parent = node.getParent();
                if (parent.getType() == Token.IF || parent.getType() == Token.IFEQ || parent.getType() == Token.IFNE || parent.getType() == Token.SWITCH || parent.getType() == Token.FOR || parent.getType() == Token.CASE)
                    continue;
                //System.out.println("node " + node.getClass().toString());
                //System.out.println("parent " + parent.getClass().toString());
                parent.addChildBefore(ifStatement, node);
                parent.removeChild(node);
                //parent.replaceChild(node, ifStatement);

                // 将原来的node添加到IfStatement结点的ThenPart域
                scope.addChild(node);
                ifStatement.setParent(parent);
                ++this.opaqueCount;
                //System.out.println("opaque obfuscation:" + ++this.opaqueCount);
            }
        }
        //System.out.println(this.opaqueCount);
    }

    /**
     * 混淆
     *
     * @param root 抽象语法树的根
     */
    public void obfuscate(AstRoot root) {

        // 设置混淆强度
        this.setFlatternStrength(flatternStrength);
        this.setOpaqueStrength(opaqueStrength);

        // 进入混淆过程
        //AstNode node = (AstNode) root.getFirstChild();
        this.root = root;
        this.obfuscationProcess(root);
    }


    /**
     * 实现NodeVisitor接口，以方便提取所有的函数名--*--$--#--%--&--@--*----*--$--#--%--&--@--*----*--$--#--%--&--@--*----*--$--#--%--&--@--*----*--$--#--%--&--@--*--s
     *
     * @author Marion
     */
    class obfuscationvisitor implements NodeVisitor {
        public boolean visit(AstNode node) {
            if (node.getType() == Token.FUNCTION) {
                // 对函数结点进行平展控制流混淆
                Block block = (Block) ((FunctionNode) (node)).getBody();
                flattenControlFlow((AstNode) (block.getFirstChild()));
            } else if (node.getType() == Token.FOR && !(node instanceof ForInLoop)) {
                // 对IF结点进行平展控制流混淆
                System.out.println("选择FOR节点处理！");
                flattenControlFlow_FOR(node);//这种直接使用node做参数是否合理？
            } else if (node.getType() == Token.IF && node.getParent() != null) {
                // 对IF结点进行平展控制流混淆
                System.out.println("选择IF节点处理！");
                flattenControlFlow_IF(node);//这种直接使用node做参数是否合理？
            }
            return true;
        }
    }

    class ExprVisitor implements NodeVisitor {

        public boolean visit(AstNode node) {
            if (node.getType() == Token.EXPR_RESULT || node.getType() == Token.EXPR_VOID)
                astNodelist.add(node);
            return true;
        }
    }

    /**
     * 混淆过程
     *
     * @param node 语法树的根
     */
    public void obfuscationProcess(AstNode node) {
        // 平展控制流
        node.visit(new ExprVisitor());

        opaqueObfuscation();
        node.visit(new obfuscationvisitor());
    }
}
