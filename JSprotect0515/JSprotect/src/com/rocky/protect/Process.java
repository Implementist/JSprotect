package com.rocky.protect;

import org.mozilla.javascript.ast.*;
import org.mozilla.javascript.Token;

import java.util.ArrayList;
import java.util.List;

public class Process {
    private ArrayList<String> FunctionList = null;
    private ArrayList<String> FunctionCalls = null;
    private AstRoot root = null;

    public Process(){
        this.FunctionList = new ArrayList<String>();
        this.FunctionCalls = new ArrayList<String>();
    }


    /**
     * 遍历规则:
     * 只处理同一作用域中的各节点，先获取第一个节点访问，然后顺着next链表访问，直到为空
     * @param node
     */
    public void extractFunctionNames(AstNode node)
    {
        AstNode first = (AstNode)node.getFirstChild();
        AstNode temp = first;
        while(null != temp){
            processNode(temp);
            temp = (AstNode)temp.getNext();
        }
    }

    /**
     * 设置抽象语法树
     * @param root Ast的根
     */
    public void setRoot(AstRoot root)
    {
        this.root = root;
    }

    /**
     * 获取抽象语法树
     * @param root
     * @return 抽象语法树的根
     */
    public AstRoot getRoot(AstRoot root)
    {
        return this.root;
    }

    public ArrayList<String> getFunctionList()
    {
        return this.FunctionList;
    }

    public ArrayList<String> getFunctionCalls()
    {
        return this.FunctionCalls;
    }

    /**
     * 处理所有的node，实质为一个Dispatcher的作用，让不同的处理函数来处理不同类型的Node
     * @param node
     */
    private void processNode(AstNode node)
    {
        switch(node.getType())
        {
            case Token.FUNCTION : this.processFunctionNode(node);
                break;
            case Token.CALL : this.processFunctionCall(node);
                break;
            case Token.EXPR_RESULT : this.processExpressionStatementResult(node);
                break;
            case Token.EXPR_VOID: this.processExpressionStatementVoid(node);
                break;
            case Token.ASSIGN : this.processAssignment(node);
                break;
            case Token.COLON : this.processObjectProperty(node);
                break;
            case Token.IF : this.processIfStatement(node);
                break;
            case Token.BLOCK : this.processScope(node);
                break;
            case Token.SWITCH : this.processSwitchStatement(node);
                break;
            case Token.CASE : this.processSwitchCase(node);
                break;
            case Token.FOR : this.processForLoop(node);
                break;
            case Token.WHILE : this.processWhileLoop(node);
                break;
            case Token.VAR : this.processVariable(node);
                break;
            case Token.OBJECTLIT : this.processObjectLiteral(node);
                break;
        }
    }

    /**
     * 处理FunctionNode
     * @param node FunctionNode
     */
    private void processFunctionNode(AstNode node)
    {
        // 获取函数名, 并加入
        Name functionName = ((FunctionNode)(node)).getFunctionName();
        if(null != functionName)
            this.addFunction(functionName);

        // 获取FunctionBody，对函数内的语句继续进行遍历
        Block functionBody = (Block)((FunctionNode)(node)).getBody();
        this.extractFunctionNames(functionBody);
    }

    /**
     * 处理FunctionCall类型的结点
     * @param node 为FunctionCall类型的结点
     */
    private void processFunctionCall(AstNode node)
    {
        // FunctionCall有两种类型，一种是直接调用函数名(target为Name), 另一种是通过对象名调用(target为PropertyGet)
        AstNode tmp = ((FunctionCall)(node)).getTarget();

        if(tmp.getType() == Token.NAME)     // 处理target为Name
        {
            Name functionCallName = (Name)(((FunctionCall)(node)).getTarget());
            this.addFunctionCalls(functionCallName);
        }

        if(tmp.getType() == Token.GETPROP)  // 处理target为PropertyGet
        {
            // 处理 a.apply(object)这种调用
            if(((Name)(((PropertyGet)(tmp)).getRight())).getIdentifier().equals("apply") || ((Name)(((PropertyGet)(tmp)).getRight())).getIdentifier().equals("call"))
            {
                // 函数的调用作用域在参数列表的第一个
                // this.addFunctionCalls((Name)(((PropertyGet)(tmp)).getLeft()));
                String functionName="";


                if(((PropertyGet)(tmp)).getLeft().getType() == Token.NAME)
                {
                    functionName = ((Name)(((PropertyGet)(tmp)).getLeft())).getIdentifier();
                }
                else
                {
                    // tmp为PropertyGet类型
                    tmp = (((PropertyGet)(tmp)).getLeft());
                    while((tmp != null) && (tmp.getType() == Token.GETPROP))
                    {
                        // this.a.apply
                        if(((PropertyGet)(tmp)).getLeft().getType() != Token.THIS)
                            functionName = "." + ((Name)(((PropertyGet)(tmp)).getLeft())).getIdentifier() + "." + ((Name)(((PropertyGet)(tmp)).getRight())).getIdentifier();
                        else
                            functionName = ((Name)(((PropertyGet)(tmp)).getRight())).getIdentifier() + functionName;

                        tmp = ((PropertyGet)(tmp)).getLeft();
                    }
                    if('.' == functionName.charAt(0) )
                        functionName = functionName.substring(1);
                }

                this.addFunctionCalls(new Name(0, functionName));
            }
            else if(((PropertyGet)(tmp)).getLeft().getType() == Token.CALL)
            {
                this.processNode(((PropertyGet)(tmp)).getLeft());
                this.addFunctionCalls((Name)(((PropertyGet)(tmp)).getRight()));
            }
            else
            {
                String functionName = "";

                while(tmp.getType() == Token.GETPROP)
                {
                    functionName = "." + ((Name)(((PropertyGet)(tmp)).getRight())).getIdentifier() + functionName;
                    tmp = ((PropertyGet)(tmp)).getLeft();
                }

                // 跳出循环，处理PropertyGet的left和right均是Name的情况
                // 跳出循环，说明该Left已经为Name了，所以直接获取left，调用parent的getRight获取right部分
                if(tmp.getType() == Token.THIS)
                    functionName = functionName.substring(1);
                else
                    functionName = ((Name)(tmp)).getIdentifier() + functionName;
                this.addFunctionCalls(new Name(0, functionName));
            }
        }

        // 处理参数列表, 因为函数的参数列表中，可能也包含了函数定义，函数是可以作为参数进行传递的
        List<AstNode> arguments = ((FunctionCall)(node)).getArguments();

        int len = arguments.size();

        AstNode argument;

        for(int i = 0; i < len; ++i)
        {
            argument = arguments.get(i);
            if(argument.getType() == Token.FUNCTION){
                this.processFunctionNode(argument);
            }
        }
    }

    /**
     * 处理带返回值的赋值表达式
     * @param node 代表带返回值的赋值表达式结点
     */
    private void processExpressionStatementResult(AstNode node)
    {
        this.processNode(((ExpressionStatement)(node)).getExpression());
    }

    /**
     * 处理不带返回值的赋值表达式结点
     * @param node
     */
    private void processExpressionStatementVoid(AstNode node)
    {
        this.processNode(((ExpressionStatement)(node)).getExpression());
    }


    private void processAssignment(AstNode node)
    {
        if((((Assignment)(node)).getLeft()).getType() == Token.GETPROP && (((Assignment)(node)).getRight()).getType() == Token.OBJECTLIT)
        {
            // 获取属性操作的所有元素
            List<ObjectProperty> elements = ((ObjectLiteral)(((Assignment)(node)).getRight())).getElements();

            int len = elements.size();

            for(int i = 0; i < len; ++i)
            {
                System.out.println(i);
                processNode(elements.get(i));
            }

        }
        else if((((Assignment)(node)).getLeft()).getType() == Token.GETPROP && (((Assignment)(node)).getRight()).getType() == Token.CALL)
        {
            this.processNode(((Assignment)node).getRight());
        }
    }

    /**
     * 处理ObjectProperty结点
     * @param node 为ObjectProperty结点
     */
    private void processObjectProperty(AstNode node)
    {
        // 若该ObjectProperty的类型为FunctionNode
        if(((ObjectProperty)(node)).getRight().getType() == Token.FUNCTION)
        {
            // ObjectProperty的left为Name，right为FunctionNode
            this.addFunction((Name)(((ObjectProperty)(node)).getLeft()));

            // 继续遍历FunctionNode，找FunctionCall
            this.processFunctionNode(((ObjectProperty)(node)).getRight());
        }
    }

    /**
     * 处理IfStatement语句
     * @param node 为IfStatement结点
     */
    private void processIfStatement(AstNode node)
    {
        IfStatement ifStatement = (IfStatement)(node);

        // 获取Then Part
        if(null != ifStatement.getThenPart())
            this.processNode(ifStatement.getThenPart());

        // 获取Else Part
        if(null != ifStatement.getElsePart())
            this.processNode(ifStatement.getElsePart());
    }

    /**
     * 处理Scope结点
     * @param node 为Scope结点
     */
    private void processScope(AstNode node)
    {
        // 顺着Scope结点的FirstChild访问即可
        Scope scope = (Scope)(node);
        this.extractFunctionNames(scope);
    }

    /**
     * 处理SwitchStatement结点
     * @param node 为SwitchStatement结点
     */
    private void processSwitchStatement(AstNode node)
    {
        List<SwitchCase> cases = ((SwitchStatement)(node)).getCases();

        int len = cases.size();

        for(int i = 0; i < len; ++i)
        {
            this.processSwitchCase(cases.get(i));
        }
    }

    /**
     * 处理SwitchCase结点
     * @param node 为SwitchCase结点
     */
    private void processSwitchCase(AstNode node)
    {
        SwitchCase switchCase = (SwitchCase)(node);

        List<AstNode> statements = switchCase.getStatements();

        int len = statements.size();

        for(int i = 0; i < len; ++i)
        {
            this.processNode(statements.get(i));
        }
    }

    /**
     * 处理ForLoop结点
     * @param node 为ForLoop结点
     */
    private void processForLoop(AstNode node)
    {
        ForLoop forLoopNode = (ForLoop)(node);
        this.processNode(forLoopNode.getBody());
    }

    /**
     * 处理WhileLoop结点
     * @param node 为WhileLoop结点
     */
    private void processWhileLoop(AstNode node)
    {
        WhileLoop whileLoop = (WhileLoop)(node);
        this.processNode(whileLoop.getBody());
    }

    private void processVariable(AstNode node)
    {
        VariableDeclaration variableDeclaration = (VariableDeclaration)node;

        List<VariableInitializer> variables = variableDeclaration.getVariables();

        int len = variables.size();

        for(int i = 0; i < len; ++i)
        {
            if(null != variables.get(i).getInitializer() && variables.get(i).getInitializer().getType() == Token.OBJECTLIT)
                this.processNode(variables.get(i).getInitializer());
        }
    }

    private void processObjectLiteral(AstNode node)
    {
        ObjectLiteral objectLiteral = (ObjectLiteral)node;

        List<ObjectProperty> elements = objectLiteral.getElements();

        int len = elements.size();
        for(int i = 0; i < len; ++i)
        {
            this.processNode(elements.get(i));
        }
    }
    /**
     * 向FunctionList中加入该函数名
     * @param functionName 要加入的函数名
     */
    private void addFunction(Name functionName)
    {
        this.FunctionList.add(functionName.getIdentifier());
    }

    /**
     * 向FunctionCalls中加入该函数调用
     * @param functionCallName 是要加入的FunctionCall
     */
    private void addFunctionCalls(Name functionCallName)
    {
        this.FunctionCalls.add(functionCallName.getIdentifier());
    }
}