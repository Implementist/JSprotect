package obfu.copy;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.*;

import java.io.IOException;
import java.util.*;

//计算式分解错误：jQuery.isPlainObject(copy) || (copyIsArray = jQuery.isArray(copy))
//for循环的初始化不提取导致后面出错。
class ToElement {
    private int SplitString = 0;//是否拆分字符串
    private Set<String> LeftName = new HashSet<String>();
    //记录所有属性类型的变量
    private Set<String> SpeName = new HashSet<String>();//非参数，非定义变量
    private Set<String> NewFunctionCallSet = new HashSet<String>();
    private Set<String> VarKeySet = new HashSet<String>();
    private ArrayList<AstNode> NodeList = new ArrayList<AstNode>();//记录所有提取的结点
    private Set<String> LabelName = new HashSet<String>();//记录LabelName
    private Set<String> ObjKeyWord = new HashSet<String>();//记录公有对象
    private ArrayList<AstNode> Function = new ArrayList<AstNode>();
    private ArrayList<AstNode> StrNode = new ArrayList<AstNode>();
    private ArrayList<String> FuncName = new ArrayList<String>();
    private Set<String> Param = new HashSet<String>();//作为参数的结点
    private Set<AstNode> KeyWord = new HashSet<AstNode>();//获取Keyword
    private ArrayList<AstNode> moduleNode = new ArrayList<AstNode>();//获取模块函数
    private ArrayList<AstNode> ObjNode = new ArrayList<AstNode>();//保存所有的声明的对象
    private ArrayList<String> StrObjNode = new ArrayList<String>();//保存所有声明的对象的字符串形式名字
    private Set<String> FirstEle = new HashSet<String>();//首元素
    private ArrayList<String> FunNames = new ArrayList<String>();//记录所有的函数名,参数名
    private Set<String> SetNames = new HashSet<String>();//记录所有的变量名
    private Set<String> SetVarNames = new HashSet<String>();//粗略记录所有的声明的变量名
    private Set<String> NewSetNames = new HashSet<String>();//记录所有的新产生的变量名
    private Map<String, String> MapNames = new HashMap<String, String>();//新旧变量名的匹配
    private Stack<AstNode> FunctionCallStack = new Stack<AstNode>();//保存所有的FunctionCall结点
    private ArrayList<AstNode> InfixExpressionArrayList = new ArrayList<AstNode>();//保存所有的InfixExpression类型的结点.
    private Stack<AstNode> PropertyList = new Stack<AstNode>();//记录所有的PropertyGet结构
    private ArrayList<AstNode> InfixComma = new ArrayList<AstNode>();//记录拆分后的InfixExpression;
    private ArrayList<AstNode> ReturnList = new ArrayList<AstNode>();//记录所有return中的语句。
    private ArrayList<AstNode> InfixCommaFather = new ArrayList<AstNode>();//记录拆分后的InfixExpression的父节点。
    private ArrayList<AstNode> VariableList = new ArrayList<AstNode>();//记录所有的声明结点，之后进行拆分 var a=b,c=4+5;=>var a=b;var c=5+4;
    private ArrayList<AstNode> ExpressionList = new ArrayList<AstNode>();//记录所有的Expression，拆分所有的 c=a,b=d这种类型.
    private ArrayList<AstNode> ElementList = new ArrayList<AstNode>();//记录所有的ElementGet结点，保证Element都是变量.
    private ArrayList<AstNode> UnaryExprList = new ArrayList<AstNode>();//记录所有的i++类型。
    private ArrayList<AstNode> SpeAssList = new ArrayList<AstNode>();//记录所有的+=类型。
    private ArrayList<AstNode> ObjectList = new ArrayList<AstNode>();//记录所有的objectliteral结点，将属性值提取.
    private ArrayList<AstNode> NumDataList = new ArrayList<AstNode>();//记录所有的数字.
    private ArrayList<AstNode> StrDataList = new ArrayList<AstNode>();//记录所有的字符串;
    private ArrayList<AstNode> RegDataList = new ArrayList<AstNode>();//记录所有的正则表达式
    private ArrayList<AstNode> ListList = new ArrayList<AstNode>();//记录所有的数组.
    private ArrayList<AstNode> VarName = new ArrayList<AstNode>();//记录所有的声明变量名
    private Set<String> ObjProName = new HashSet<String>();//获取所有的属性名，方法名
    private Map<String, String> NewObjNameMap = new HashMap<String, String>();//新旧属性名，方法名对
    private Set<String> ObjName = new HashSet<String>();//记录所有公有属性名，方法名


    private Set<String> UseObjName = new HashSet<String>();//记录所有使用过的属性名，方法名
    //这部分记录所有的FOR和If结点，目的在于给作用域中单一结点加上大括号。
    private ArrayList<AstNode> ScopeList = new ArrayList<AstNode>();
    private ArrayList<String> NamesInFlattern;
    private ArrayList<String> ones;
    private ArrayList<String> twos;
    private ArrayList<String> threes;
    private ArrayList<String> Names = new ArrayList<String>();
    private ArrayList<String> NamesObj = new ArrayList<String>();
    private int total = 203000;
    private Random random;
    private AstNode First = null;//保存首结点
    private int StrNum;//统计所有的字符串的数量。
    private int Numnum;
    private ArrayList<String> Comm = new ArrayList<String>();
    private int Namenum;//记录使用到第几个名字
    private int ObjNamenum;
    private int ObjNameNum;

    ToElement(AstNode node) {
        InitObjKeyWord();
        Namenum = 1;
        ObjNameNum = 1;
        ObjNamenum = 1;
        this.NamesInFlattern = new ArrayList<String>();
        this.initVariablesPool();
        this.random = new Random();
        InitComm();
    }


    private void InitObjKeyWord() {
        ObjKeyWord.add("undefined");
        ObjKeyWord.add("Function");
        ObjKeyWord.add("Array");
        ObjKeyWord.add("Boolean");
        ObjKeyWord.add("Date");
        ObjKeyWord.add("Math");
        ObjKeyWord.add("Number");
        ObjKeyWord.add("String");
        ObjKeyWord.add("RegExp");
        ObjKeyWord.add("decodeURI");
        ObjKeyWord.add("decodeURIComponent");
        ObjKeyWord.add("encodeURI");
        ObjKeyWord.add("encodeURIComponent");
        ObjKeyWord.add("escape");
        ObjKeyWord.add("eval");
        ObjKeyWord.add("getClass");
        ObjKeyWord.add("isFinite");
        ObjKeyWord.add("isNaN");
        ObjKeyWord.add("Number");
        ObjKeyWord.add("parseFloat");
        ObjKeyWord.add("parseInt");
        ObjKeyWord.add("String");
        ObjKeyWord.add("unescape");
        ObjKeyWord.add("Window");
        ObjKeyWord.add("navigator");
        ObjKeyWord.add("screen");
        ObjKeyWord.add("history");
        ObjKeyWord.add("location");
        ObjKeyWord.add("document");
        ObjKeyWord.add("Element");
        ObjKeyWord.add("Attr");
        ObjKeyWord.add("window");
        ObjKeyWord.add("Object");
        ObjKeyWord.add("console");
        ObjKeyWord.add("alert");
        ObjKeyWord.add("blur");
        ObjKeyWord.add("clearInterval");
        ObjKeyWord.add("clearTimeout");
        ObjKeyWord.add("close");
        ObjKeyWord.add("confirm");
        ObjKeyWord.add("createPopup");
        ObjKeyWord.add("focus");
        ObjKeyWord.add("moveBy");
        ObjKeyWord.add("moveTo");
        ObjKeyWord.add("open");
        ObjKeyWord.add("print");
        ObjKeyWord.add("prompt");
        ObjKeyWord.add("resizeBy");
        ObjKeyWord.add("resizeTo");
        ObjKeyWord.add("scrollBy");
        ObjKeyWord.add("scrollTo");
        ObjKeyWord.add("Image");
        ObjKeyWord.add("setInterval");
        ObjKeyWord.add("setTimeout");
        ObjKeyWord.add("Float32Array");
        ObjKeyWord.add("XMLHttpRequest");
        ObjKeyWord.add("Infinity");
        ObjKeyWord.add("Location");
        ObjKeyWord.add("History");
        ObjKeyWord.add("Screen");
        ObjKeyWord.add("Navigator");
        ObjKeyWord.add("Window");
        ObjKeyWord.add("ArrayBuffer");
        ObjKeyWord.add("Int8Array");
        ObjKeyWord.add("Uint8Array");
        ObjKeyWord.add("Int16Array");
        ObjKeyWord.add("Uint16Array");
        ObjKeyWord.add("Int32Array");
        ObjKeyWord.add("Uint32Array");
        ObjKeyWord.add("Float32Array");
        ObjKeyWord.add("Float64Array");
        ObjKeyWord.add("JSON");
        ObjKeyWord.add("Error");
    }

    public Set<String> getKeyWord() {
        return ObjKeyWord;
    }

    private void InitComm() {
        Comm.add("toString");
        Comm.add("parse");
        Comm.add("concat");
        Comm.add("random");
        Comm.add("Init");
        Comm.add("mixIn");
        Comm.add("hasOwnProperty");
        Comm.add("clone");
        Comm.add("extend");
        Comm.add("create");
        Comm.add("stringify");
    }

    private void InitFirst(AstNode node) {
        First = node;
    }

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
        //ones.remove("m");
        twos.remove("as");
        twos.remove("is");
        twos.remove("do");
        twos.remove("if");
        twos.remove("in");
        twos.remove("of");
        threes.remove("for");
        threes.remove("int");
        threes.remove("new");
        threes.remove("try");
        threes.remove("use");
        threes.remove("var");
        threes.remove("cos");
        threes.remove("sin");
        for (int i = 0; i < threes.size(); i++)
            Names.add(threes.get(i) + "asd");
        for (int i = 0; i < ones.size(); i++)
            Names.add(ones.get(i) + "asd");
        for (int i = 0; i < twos.size(); i++)
            NamesObj.add(twos.get(i));
        for (int i = 0; i < threes.size(); i++)
            NamesObj.add(threes.get(i));
    }

    private void collectNames(AstNode node) {
        node.visit(new Visitor());
    }

    class ChangeDeadName implements NodeVisitor {
        public boolean visit(AstNode node) {
            if (node instanceof Name) {
                ((Name) node).setIdentifier(FunNameDead);
            }
            return true;
        }
    }

    private String FunNameDead = null;

    class Visitor implements NodeVisitor {
        public boolean visit(AstNode node) {
            if (node.getType() == Token.NAME) {
                if (((Name) node).getIdentifier().length() < 6)
                    NamesInFlattern.add(((Name) node).getIdentifier());
            }
            if (node instanceof FunctionNode) {
                AstNode FunName = ((FunctionNode) node).getFunctionName();
                if (DeadCodesIf != null && DeadCodesIf.size() > 0 && FunName != null) {
                    FunNameDead = FunName.toSource();
                    AstNode body = ((FunctionNode) node).getBody();
                    int ran = random.nextInt(DeadCodesIf.size());
                    AstNode DeadCodeIfs = (AstNode) ((AstNode) DeadCodesIf.get(ran).getFirstChild()).clone();
                    DeadCodesIf.remove(ran);
                    DeadCodeIfs.visit(new ChangeDeadName());
                    for (AstNode First = (AstNode) body.getFirstChild(); First != null && First.getNext() != null; First = (AstNode) First.getNext()) {
                        int num = random.nextInt(10);
                        if (num < 4) {
                            AstNode NewNode = (AstNode) DeadCodeIfs.clone();
                            body.addChildAfter(NewNode, First);
                            NewNode.setParent(body);
                            NewNode.setRelative(body.getPosition());
                            break;
                        }
                    }
                }
            }
            return true;
        }
    }

    public String getWordFromThePool(int flag) {
        String word;
        word = this.Names.get(Namenum++);
        return word;
    }

    public String getValidWord(AstNode node, int flag) {
        //this.NamesInFlattern.clear();
        //this.collectNames(node);
        String word = this.getWordFromThePool(1);
        while (this.NamesInFlattern.contains(word)) {
            word = this.getWordFromThePool(1);
        }
        this.NamesInFlattern.add(word);
        return word;
    }

    private String getWordFromTheObjPool() {
        String word = this.NamesObj.get(ObjNameNum++);
        return word;
    }


    private String getNewObjName() {
        String Name = this.getWordFromTheObjPool();
        while (this.UseObjName.contains(Name) || ObjName.contains(Name)) {
            Name = this.getWordFromTheObjPool();
        }
        this.UseObjName.add(Name);
        return Name;
    }

    //上面是获取新的变量名
    private void DealUnaryExprList() {
        for (int i = 0; i < UnaryExprList.size(); i++) {
            AstNode op = ((UnaryExpression) UnaryExprList.get(i)).getOperand();
            int opp = ((UnaryExpression) UnaryExprList.get(i)).getOperator();
            if (opp == 107 || opp == 106) {
                if (opp == 107) opp = 22;
                if (opp == 106) opp = 21;
                InfixExpression NewInfix = new InfixExpression();
                NumberLiteral Num = new NumberLiteral();
                Num.setValue("1");
                Num.setParent(NewInfix);
                Num.setRelative(NewInfix.getPosition());
                NewInfix.setOperator(opp);
                AstNode Nop = (AstNode) op.clone();
                NewInfix.setLeftAndRight(Nop, Num);
                Nop.setParent(NewInfix);
                Nop.setRelative(NewInfix.getPosition());
                if (Nop instanceof ElementGet) {
                    AstNode Ele = (AstNode) ((ElementGet) Nop).getElement().clone();
                    Ele.setParent(Nop);
                    Ele.setRelative(Nop.getPosition());
                    ((ElementGet) Nop).setElement(Ele);
                    AstNode Tar = (AstNode) ((ElementGet) Nop).getTarget().clone();
                    if (Tar instanceof ElementGet) {
                        AstNode Elenode = (AstNode) ((ElementGet) Tar).getElement().clone();
                        Elenode.setParent(Tar);
                        Elenode.setRelative(Tar.getPosition());
                        ((ElementGet) Tar).setElement(Elenode);
                        Tar.setParent(Nop);
                        Tar.setRelative(Nop.getPosition());
                        ((ElementGet) Nop).setTarget(Tar);
                    }
                }
                Assignment InfixExp = new Assignment();
                InfixExp.setOperator(Token.ASSIGN);
                op = (AstNode) op.clone();
                InfixExp.setLeftAndRight(op, NewInfix);
                op.setParent(InfixExp);
                op.setRelative(InfixExp.getPosition());
                NewInfix.setParent(InfixExp);
                NewInfix.setRelative(InfixExp.getPosition());
                if (op instanceof ElementGet) {
                    AstNode Ele = (AstNode) ((ElementGet) op).getElement().clone();
                    Ele.setParent(op);
                    Ele.setRelative(op.getPosition());
                    ((ElementGet) op).setElement(Ele);
                    AstNode Tar = (AstNode) ((ElementGet) Nop).getTarget().clone();
                    if (Tar instanceof ElementGet) {
                        AstNode Elenode = (AstNode) ((ElementGet) Tar).getElement().clone();
                        Elenode.setParent(Tar);
                        Elenode.setRelative(Tar.getPosition());
                        ((ElementGet) Tar).setElement(Elenode);
                        Tar.setParent(Nop);
                        Tar.setRelative(Nop.getPosition());
                        ((ElementGet) Nop).setTarget(Tar);
                    }
                }
                if (UnaryExprList.get(i).getParent() instanceof ExpressionStatement) {
                    ((ExpressionStatement) UnaryExprList.get(i).getParent()).setExpression(InfixExp);
                    InfixExp.setParent(UnaryExprList.get(i).getParent());
                    InfixExp.setRelative(((ExpressionStatement) UnaryExprList.get(i).getParent()).getPosition());
                } else if (UnaryExprList.get(i).getParent() instanceof ForLoop) {
                    AstNode Increment = ((ForLoop) UnaryExprList.get(i).getParent()).getIncrement();
                    if (Increment.toSource().equals(UnaryExprList.get(i).toSource())) {
                        ((ForLoop) UnaryExprList.get(i).getParent()).setIncrement(InfixExp);
                    }
                } else {
                    System.out.println("UnaryExprList:" + UnaryExprList.get(i).getParent().getClass());
                }
            }
        }
    }

    private void DealSpeAssList() {
        for (int i = 0; i < SpeAssList.size() && SpeAssList.get(i) instanceof Assignment; i++) {
            AstNode Left = ((Assignment) SpeAssList.get(i)).getLeft();
            AstNode Right = ((Assignment) SpeAssList.get(i)).getRight();
            int op = ((Assignment) SpeAssList.get(i)).getOperator();
            if (Right instanceof InfixExpression) {
                ParenthesizedExpression PareRight = new ParenthesizedExpression();
                PareRight.setExpression(Right);
                Right = PareRight;
            }
            AstNode NewRightNode = (AstNode) Left.clone();
            if (Left instanceof ElementGet) {
                AstNode str = (AstNode) ((ElementGet) Left).getElement().clone();
                ((ElementGet) NewRightNode).setElement(str);
                str.setParent(NewRightNode);
                str.setRelative(NewRightNode.getPosition());
                AstNode Tar = (AstNode) ((ElementGet) Left).getTarget().clone();
                ((ElementGet) NewRightNode).setTarget(Tar);
                Tar.setParent(NewRightNode);
                Tar.setRelative(NewRightNode.getPosition());
            }
            InfixExpression NewRight = new InfixExpression();
            if (op > 90 && op < 94) {
                NewRight.setOperator(op - 82);
            } else {
                NewRight.setOperator(op - 76);
            }
            if (Right instanceof Name) Right = (AstNode) Right.clone();
            Right.setParent(NewRight);
            Right.setRelative(NewRight.getPosition());
            NewRightNode.setParent(NewRight);
            NewRightNode.setRelative(NewRight.getPosition());
            NewRight.setLeftAndRight(NewRightNode, Right);
            ((Assignment) SpeAssList.get(i)).setOperator(90);
            ((Assignment) SpeAssList.get(i)).setRight(NewRight);
            if (SpeAssList.get(i).getParent() instanceof ExpressionStatement) {
                ((ExpressionStatement) SpeAssList.get(i).getParent()).setExpression(SpeAssList.get(i));
            } else if (SpeAssList.get(i).getParent() instanceof ForLoop) {
                AstNode Increment = ((ForLoop) SpeAssList.get(i).getParent()).getIncrement();
                if (Increment.toSource().equals(SpeAssList.get(i).toSource())) {
                    ((ForLoop) SpeAssList.get(i).getParent()).setIncrement(SpeAssList.get(i));
                }
            } else {
                //System.out.println("SpeAssList:"+SpeAssList.get(i).getParent().toSource());
            }
        }
    }


    class testvisit1 implements NodeVisitor {
        public boolean visit(AstNode node) {
            if (node instanceof StringLiteral) {
                StrNum++;
                String str = ((StringLiteral) node).getValue();
                if (str.contains("\'")) {
                    ((StringLiteral) node).setQuoteCharacter('"');
                }
            } else if (node.getType() == Token.NAME) {
                AstNode Parent = node.getParent();
                if (Parent instanceof Assignment && ((Assignment) Parent).getLeft().toSource().equals(node.toSource()))
                    LeftName.add(node.toSource());
            } else if (node instanceof FunctionNode) {

                Name FunName = ((FunctionNode) node).getFunctionName();
                if (FunName != null) {
                    SpeName.add(FunName.toSource());
                    VarKeySet.add(FunName.toSource());
                    FunNames.add(FunName.toSource());
                    FuncName.add(FunName.toSource());
                    Function.add(node);
                }
                List<AstNode> Argus = ((FunctionNode) node).getParams();
                for (int i = 0; i < Argus.size(); i++) {
                    if (Argus.get(i) instanceof Name) {
                        FunNames.add(Argus.get(i).toSource());
                        VarKeySet.add(Argus.get(i).toSource());
                        SpeName.add(Argus.get(i).toSource());
                    }
                }
            } else if (node instanceof FunctionCall) {
                AstNode Target = ((FunctionCall) node).getTarget();
                if (Target instanceof Name) {
                    NewFunctionCallSet.add(Target.toSource());
                }
                if (!(Target instanceof ParenthesizedExpression)) {
                    FunctionCallStack.push((AstNode) node);
                }
                AstNode tmp = node;
                while (tmp instanceof FunctionCall || tmp instanceof ElementGet || tmp instanceof PropertyGet) {
                    if (tmp instanceof FunctionCall)
                        tmp = ((FunctionCall) tmp).getTarget();
                    else if (tmp instanceof ElementGet)
                        tmp = ((ElementGet) tmp).getTarget();
                    else if (tmp instanceof PropertyGet)
                        tmp = ((PropertyGet) tmp).getTarget();
                }
                if (tmp instanceof Name)
                    FirstEle.add(tmp.toSource());
            } else if (node instanceof PropertyGet) {
                PropertyList.add(node);
                AstNode tmp = node;
                while (tmp instanceof FunctionCall || tmp instanceof ElementGet || tmp instanceof PropertyGet) {
                    if (tmp instanceof FunctionCall)
                        tmp = ((FunctionCall) tmp).getTarget();
                    else if (tmp instanceof ElementGet)
                        tmp = ((ElementGet) tmp).getTarget();
                    else if (tmp instanceof PropertyGet)
                        tmp = ((PropertyGet) tmp).getTarget();
                }
                if (tmp instanceof Name)
                    FirstEle.add(tmp.toSource());
            } else if (node instanceof IfStatement) {
                ScopeList.add(node);
            } else if (node instanceof Loop) {
                ScopeList.add(node);
            } else if (node instanceof VariableDeclaration) {
                List<VariableInitializer> InitList = ((VariableDeclaration) node).getVariables();
                if (InitList.size() > 1) VariableList.add(node);
                for (int i = 0; i < InitList.size(); i++) {
                    AstNode Target = InitList.get(i).getTarget();
                    SetVarNames.add(Target.toSource());
                    VarKeySet.add(Target.toSource());
                }
            } else if (node instanceof ExpressionStatement) {
                AstNode Expr = ((ExpressionStatement) node).getExpression();
                if (Expr instanceof InfixExpression && ((InfixExpression) Expr).getOperator() == Token.COMMA) {
                    ExpressionList.add(node);
                }
                if (Expr instanceof UnaryExpression) {
                    //将 a++的形式转换成 a=a+1;
                    UnaryExprList.add(Expr);
                }
            } else if (node instanceof ReturnStatement) {
                AstNode ReturnValue = ((ReturnStatement) node).getReturnValue();
                if ((ReturnValue instanceof InfixExpression) || (ReturnValue instanceof ObjectLiteral)) {
                    ReturnList.add(node);
                }
            } else if (node instanceof ArrayLiteral) {
                ListList.add(node);
                List<AstNode> Lists = ((ArrayLiteral) node).getElements();
                for (int i = 0; i < Lists.size(); i++) {
                    if (Lists.get(i) instanceof FunctionNode) {
                        moduleNode.add(Lists.get(i));
                    }
                }
            } else if (node instanceof ObjectProperty) {
                AstNode Left = ((ObjectProperty) node).getLeft();
                ObjProName.add(Left.toSource());
                VarKeySet.add(Left.toSource());
            }
            if (node instanceof ObjectLiteral) {
                ObjectList.add(node);
            }
            if (node instanceof VariableInitializer && ((VariableInitializer) node).getInitializer() instanceof ObjectLiteral) {
                ObjNode.add(node);
                StrObjNode.add(((VariableInitializer) node).getTarget().toSource());
            } else if (node instanceof Label) {
                LabelName.add(((Label) node).getName());
            } else if (node instanceof ObjectProperty) {
                LeftName.add(((ObjectProperty) node).getLeft().toSource());
            } else if (node instanceof Assignment) {
                int op = ((Assignment) node).getOperator();
                if (op != Token.COMMA) {
                    if (op > 90 && op < 102) {
                        SpeAssList.add(node);
                    }
                }

            } else if (node instanceof NewExpression) {
                AstNode Target = ((NewExpression) node).getTarget();
                if (Target instanceof Name) {
                    NewFunctionCallSet.add(Target.toSource());
                }
            }
            return true;
        }
    }

    class findp implements NodeVisitor {
        public boolean visit(AstNode node) {
            if (node instanceof VariableDeclaration)
                if (node.getParent() == null)
                    System.out.println("??" + node.toSource());
            return true;
        }
    }


    class testvisit2 implements NodeVisitor {
        public boolean visit(AstNode node) {
            if (node instanceof Name) {
                AstNode parent = node.getParent();
                if (!(parent instanceof VariableInitializer && ((VariableInitializer) parent).getTarget() == node) && ObjKeyWord.contains(node.toSource()) && Shell == 1) {
                    if (!SetNames.contains(node.toSource())) {
                        SetNames.add(node.toSource());
                        Name tmpName = new Name();
                        tmpName.setIdentifier(getValidWord(tmpName, 1));
                        MapNames.put(node.toSource(), tmpName.toSource());
                        ((Name) node).setIdentifier(tmpName.toSource());
                    } else {
                        ((Name) node).setIdentifier(MapNames.get(node.toSource()));
                    }
                }
            } else if (node instanceof VariableDeclaration) {
                List<VariableInitializer> InitList = ((VariableDeclaration) node).getVariables();
                for (int i = 0; i < InitList.size(); i++) {
                    AstNode Target = InitList.get(i).getTarget();
                    if (InitList.get(i).getInitializer() instanceof InfixExpression && !(InitList.get(i).getInitializer() instanceof PropertyGet) && !(InitList.get(i).getInitializer() instanceof NumberLiteral)) {
                        if (((InfixExpression) InitList.get(i).getInitializer()).getOperator() != 90)
                            InfixExpressionArrayList.add(InitList.get(i).getInitializer());
                    }
                }
            } else if (node instanceof ElementGet) {
                if (((ElementGet) node).getElement() instanceof InfixExpression)
                    ElementList.add(node);
            } else if (node instanceof ExpressionStatement) {
                AstNode Expr = ((ExpressionStatement) node).getExpression();
                if (Expr instanceof Assignment) {
                    if (((Assignment) Expr).getRight() instanceof InfixExpression)
                        InfixExpressionArrayList.add(((Assignment) Expr).getRight());
                }
            } else if (node instanceof StringLiteral) {
                AstNode parent = node.getParent();//字符串处理有问题
                if (parent instanceof ObjectProperty && ((ObjectProperty) parent).getLeft() == node) {

                } else if (((StringLiteral) node).getValue().length() > 4) {
                    StrDataList.add(node);
                }
            } else if (node instanceof RegExpLiteral) {
                RegDataList.add(node);
            } else if (node instanceof PropertyGet) {
                PropertyList.add(node);
            } else if (node instanceof NumberLiteral) {
                NumDataList.add(node);
            } else if (node instanceof KeywordLiteral) {
                KeyWord.add(node);
            }
            return true;
        }
    }

    private AstNode SearchNode(AstNode parent, AstNode LocalNode, ArrayList<AstNode> NodeList) {
        AstNode Result = null, SubResult = null;
        for (int i = 0; i < NodeList.size(); i++) {
            SubResult = null;
            if (!(parent instanceof SwitchCase)) {
                for (AstNode start = (AstNode) parent.getFirstChild(); start != LocalNode; start = (AstNode) start.getNext()) {
                    if (start instanceof ExpressionStatement) {
                        AstNode Expr = ((ExpressionStatement) start).getExpression();
                        if (Expr instanceof Assignment) {
                            AstNode Left = ((Assignment) Expr).getLeft();
                            if (Left.toSource().contains(NodeList.get(i).toSource()) && NodeList.get(i).getType() == Left.getType())
                                SubResult = start;//可能出错
                        }
                    } else if (start instanceof VariableDeclaration) {
                        List<VariableInitializer> VarInitList = ((VariableDeclaration) start).getVariables();
                        for (int j = 0; j < VarInitList.size(); j++) {
                            if (VarInitList.get(j).getTarget().toSource().contains(NodeList.get(i).toSource()))
                                SubResult = start;
                        }
                    }
                    if (SubResult == Result)
                        Result = null;
                }
            } else {
                List<AstNode> Statements = ((SwitchCase) parent).getStatements();
                for (int j = 0; j < Statements.size(); j++) {
                    if (Statements.get(j) instanceof ExpressionStatement) {
                        AstNode Expr = ((ExpressionStatement) Statements.get(j)).getExpression();
                        if (Expr instanceof Assignment) {
                            AstNode Left = ((Assignment) Expr).getLeft();
                            if (Left.toSource().contains(NodeList.get(i).toSource()) && NodeList.get(i).getType() == Left.getType())
                                SubResult = Statements.get(j);//可能出错
                        }
                    } else if (Statements.get(j) instanceof VariableDeclaration) {
                        List<VariableInitializer> VarInitList = ((VariableDeclaration) Statements.get(j)).getVariables();
                        for (int k = 0; k < VarInitList.size(); k++) {
                            if (VarInitList.get(k).getTarget().toSource().contains(NodeList.get(i).toSource()))
                                SubResult = Statements.get(j);
                        }
                    }
                    if (SubResult == Result)
                        Result = null;
                }
            }
        }
        //if(Result==null)Result=SubResult;
        if (Result == null) {
            Result = SubResult;
            if (parent.getParent() instanceof Loop) return null;
            if (!(parent.getParent().getParent() instanceof Block)) return null;
            return SearchNode(parent.getParent().getParent(), parent.getParent(), NodeList);
        } else {
            return Result;
        }
    }


    public static String De(String str) {
        String n_str = str.replace(" ", "");
        n_str = n_str.replace("\n", "");
        return n_str;
    }

    private void DealElementList() {
        //记录Element结点和声明的变量名。
        Map<AstNode, AstNode> InsertNode = new HashMap<AstNode, AstNode>();
        //避免重复声明。
        ArrayList<String> InsertCondition = new ArrayList<String>();
        for (int i = 0; i < ElementList.size(); i++) {
            AstNode parent = ElementList.get(i).getParent();
            AstNode Target = ((ElementGet) ElementList.get(i)).getTarget();
            AstNode Element = ((ElementGet) ElementList.get(i)).getElement();
            ArrayList<AstNode> NodeList = new ArrayList<AstNode>();
            while (Element instanceof InfixExpression) {
                AstNode Left = ((InfixExpression) Element).getLeft();
                if (!(Left instanceof InfixExpression)) {
                    Element = ((InfixExpression) Element).getRight();
                } else {
                    Element = Left;
                    Left = ((InfixExpression) Element).getRight();
                }
                if (!(Left instanceof NumberLiteral))
                    NodeList.add(Left);
            }
            while (Target instanceof ElementGet) {
                Target = ((ElementGet) Target).getTarget();
            }
            while (!(parent instanceof ExpressionStatement) && !(parent instanceof VariableDeclaration)) {
                parent = parent.getParent();
            }
            Element = ((ElementGet) ElementList.get(i)).getElement();
            AstNode Result = SearchNode(parent.getParent(), parent, NodeList);
            if (!InsertCondition.contains(parent.toSource() + ElementList.get(i).toSource())) {
                VariableDeclaration Variable = new VariableDeclaration();
                List<VariableInitializer> variables = new ArrayList<VariableInitializer>();
                VariableInitializer Init = new VariableInitializer();
                Init.setInitializer(Element);
                InfixExpressionArrayList.add(Element);
                Element.setParent(Init);
                Element.setRelative(Init.getPosition());
                Name NewTarget = new Name();
                NewTarget.setIdentifier(getValidWord(NewTarget, 1));
                InsertNode.put(ElementList.get(i), (AstNode) NewTarget);
                Init.setTarget(NewTarget);
                NewTarget.setParent(Init);
                NewTarget.setRelative(Init.getPosition());
                variables.add(Init);
                Variable.setVariables(variables);
                Variable.setIsStatement(true);
                Variable.setParent(parent.getParent());
                Variable.setRelative(parent.getParent().getPosition());
                if (!(parent.getParent() instanceof SwitchCase)) {
                    if (Result != null)
                        parent.getParent().addChildAfter(Variable, Result);
                    else
                        parent.getParent().addChildBefore(Variable, parent.getParent().getFirstChild());
                } else {
                    List<AstNode> Statements = ((SwitchCase) parent.getParent()).getStatements();
                    if (Result != null)
                        for (int k = 0; k < Statements.size(); k++) {
                            if (Statements.get(k) == Result) {
                                Statements.add(k + 1, Variable);
                            }
                        }
                    else
                        Statements.add(0, Variable);
                }
                InsertCondition.add(parent.toSource() + ElementList.get(i).toSource());
            } else {
                InsertNode.put(ElementList.get(i), null);
            }
        }
        Iterator it = InsertNode.keySet().iterator();
        while (it.hasNext()) {
            AstNode EleNode = (AstNode) it.next();
            if (InsertNode.get(EleNode) == null) {
                Iterator itt = InsertNode.keySet().iterator();
                while (itt.hasNext()) {
                    AstNode Node = (AstNode) itt.next();
                    AstNode parent = Node.getParent();
                    while (true) {
                        if ((parent instanceof ExpressionStatement) || parent instanceof VariableDeclaration) {
                            break;
                        }
                        parent = parent.getParent();
                    }
                    parent = parent.getParent();
                    AstNode parent2 = EleNode.getParent();
                    while (true) {
                        if ((parent2 instanceof ExpressionStatement) || parent2 instanceof VariableDeclaration) {
                            break;
                        }
                        parent2 = parent2.getParent();
                    }
                    parent2 = parent2.getParent();
                    if (InsertNode.get(Node) != null && De(((ElementGet) Node).getElement().toSource()).contains(De(((ElementGet) EleNode).getElement().toSource()))
                            && De(parent.toSource()).contains(De(parent2.toSource()))) {
                        AstNode name = InsertNode.get(Node);
                        InsertNode.put(EleNode, name);
                    }
                }
            }
        }
        Iterator it2 = InsertNode.keySet().iterator();
        while (it2.hasNext()) {
            AstNode EleNode = (AstNode) it2.next();
            if (InsertNode.get(EleNode) != null) {
                ((ElementGet) EleNode).setElement(InsertNode.get(EleNode));
            }
        }
    }


    private void DealReturnList() {
        for (int i = 0; i < ReturnList.size(); i++) {
            AstNode ReturnValue = ((ReturnStatement) ReturnList.get(i)).getReturnValue();
            if (ReturnValue instanceof InfixExpression) {
                if (!(((InfixExpression) ReturnValue).getOperator() == 33)) {
                    VariableDeclaration VarNode = new VariableDeclaration();
                    List<VariableInitializer> InitList = new ArrayList<VariableInitializer>();
                    VariableInitializer Init = new VariableInitializer();
                    Name NewName = new Name();
                    NewName.setIdentifier(getValidWord(NewName, 1));
                    NewName.setParent(Init);
                    NewName.setRelative(Init.getPosition());
                    Init.setTarget(NewName);
                    Init.setInitializer(ReturnValue);
                    ReturnValue.setParent(Init);
                    ReturnValue.setRelative(Init.getPosition());
                    InitList.add(Init);
                    VarNode.setVariables(InitList);
                    VarNode.setIsStatement(true);
                    AstNode parent = ReturnList.get(i).getParent();
                    VarNode.setParent(parent);
                    VarNode.setRelative(parent.getPosition());
                    parent.addChildBefore(VarNode, ReturnList.get(i));
                    NewName = (Name) NewName.clone();
                    NewName.setParent(ReturnList.get(i));
                    NewName.setRelative(ReturnList.get(i).getPosition());
                    ((ReturnStatement) ReturnList.get(i)).setReturnValue(NewName);
                }
            }
        }
    }


    private void DealBlankList() {
        for (int i = 0; i < ScopeList.size(); i++) {
            AstNode Parent = ScopeList.get(i).getParent();
            if (Parent instanceof DoLoop) continue;
            if (Parent instanceof LabeledStatement) continue;
            if (ScopeList.get(i) instanceof Loop) {
                //如果结点属于for(;;)类型的结点
                if (ScopeList.get(i) instanceof ForLoop) {
                    AstNode Condition = ((ForLoop) ScopeList.get(i)).getCondition();
                    AstNode InitNode = ((ForLoop) ScopeList.get(i)).getInitializer();
                    AstNode Increment = ((ForLoop) ScopeList.get(i)).getIncrement();
                    AstNode ForBody = ((ForLoop) ScopeList.get(i)).getBody();
                    if (ForBody.getFirstChild() == null) {
                        //如果没有大括号
                        if (ForBody != null && !(ForBody instanceof Block)) {
                            Scope LoopScope = new Scope();
                            LoopScope.addChild(ForBody);
                            ForBody.setParent(LoopScope);
                            ForBody.setRelative(LoopScope.getPosition());
                            ((ForLoop) ScopeList.get(i)).setBody(LoopScope);
                        }
                    }
                    if (Condition instanceof EmptyExpression) continue;
                    //将原本的判断结构变为if语句和break的组合
                    ExpressionStatement ExprCondition = new ExpressionStatement();
                    VariableInitializer VarInit = new VariableInitializer();
                    List<VariableInitializer> VarInits = new ArrayList<VariableInitializer>();
                    VariableDeclaration VarCon = new VariableDeclaration();
                    Name ConditionName = new Name();
                    ConditionName.setIdentifier(getValidWord(ConditionName, 1));
                    ParenthesizedExpression Pare = new ParenthesizedExpression();
                    Pare.setExpression(Condition);
                    Condition.setParent(Pare);
                    Condition.setRelative(Pare.getPosition());
                    VarInit.setInitializer(Pare);
                    Pare.setParent(VarInit);
                    Pare.setRelative(VarInit.getPosition());
                    VarInit.setTarget(ConditionName);
                    ConditionName.setParent(VarInit);
                    ConditionName.setRelative(VarInit.getPosition());
                    VarInits.add(VarInit);
                    VarCon.setVariables(VarInits);
                    VarCon.setIsStatement(true);

                    EmptyExpression NewInit = new EmptyExpression();
                    ((ForLoop) ScopeList.get(i)).setCondition(NewInit);
                    UnaryExpression Unary = new UnaryExpression();
                    Unary.setOperator(26);
                    ConditionName = (Name) ConditionName.clone();
                    ConditionName.setParent(Unary);
                    ConditionName.setRelative(Unary.getPosition());
                    Unary.setOperand(ConditionName);
                    IfStatement IfExpr = new IfStatement();
                    IfExpr.setCondition(Unary);
                    //Scope scopeThen=new Scope();
                    //scopeThen.addChild(new BreakStatement());
                    IfExpr.setThenPart(new BreakStatement());
                    AstNode Body = ((ForLoop) ScopeList.get(i)).getBody();
                    Body.addChildrenToFront(IfExpr);
                    IfExpr.setParent(Body);
                    IfExpr.setRelative(Body.getPosition());
                    Body.addChildrenToFront(VarCon);
                    VarCon.setParent(Body);
                    VarCon.setRelative(Body.getPosition());
                } else if (ScopeList.get(i) instanceof ForInLoop) {
                    //给for( a in ass)添加括号
                    AstNode ForBody = ((ForInLoop) ScopeList.get(i)).getBody();
                    if (ForBody.getFirstChild() == null) {
                        if (ForBody != null && !(ForBody instanceof Block)) {
                            Scope LoopScope = new Scope();
                            LoopScope.addChild(ForBody);
                            ((ForInLoop) ScopeList.get(i)).setBody(LoopScope);
                        }
                    }
                } else if (ScopeList.get(i) instanceof WhileLoop) {
                    //给while循环添加括号
                    AstNode WhileBody = ((Loop) ScopeList.get(i)).getBody();
                    if (WhileBody.getFirstChild() == null) {
                        Scope LoopScope = new Scope();
                        LoopScope.addChild(WhileBody);
                        ((WhileLoop) ScopeList.get(i)).setBody(LoopScope);
                    }
                }
            }
            if (ScopeList.get(i) instanceof IfStatement) {
                //给if的then和else部分添加括号
                AstNode ThenPart = ((IfStatement) ScopeList.get(i)).getThenPart();
                AstNode ThenPartClone = (AstNode) ThenPart.clone();
                if (ThenPart.getFirstChild() == null) {
                    Scope Thenscope = new Scope();
                    Thenscope.addChild(ThenPart);
                    ((IfStatement) ScopeList.get(i)).setThenPart(Thenscope);
                }
                AstNode ElsePart = ((IfStatement) ScopeList.get(i)).getElsePart();
                if (ElsePart != null) {
                    if (ElsePart.getFirstChild() == null) {
                        Scope Elsescope = new Scope();
                        Elsescope.addChild(ElsePart);
                        ((IfStatement) ScopeList.get(i)).setElsePart(Elsescope);
                    }
                } else if (ThenPartClone instanceof Scope) {
                    /*Scope ElseScope=new Scope();
					if(DeadCodes.size()>0){
						int ran=random.nextInt(DeadCodes.size());
						AstNode DeadCode=(AstNode)DeadCodes.get(ran).clone();
						DeadCodes.remove(ran);
						((IfStatement)ScopeList.get(i)).setElsePart(DeadCode);
						DeadCode.setParent(ScopeList.get(i));
						DeadCode.setRelative(ScopeList.get(i).getPosition());
					/*	List<AstNode> DeadList=new ArrayList<AstNode>();
						AstNode firstNode=(AstNode)((AstNode)DeadCode.getFirstChild()).clone();
						ElseScope.addChild(firstNode);
						for(AstNode first=(AstNode)DeadCode.getFirstChild();first!=null;first=(AstNode)first.getNext()){
							AstNode InsertNode=(AstNode)first.clone();
							ElseScope.addChildAfter(InsertNode, firstNode);
							firstNode=InsertNode;
							InsertNode.setParent(ElseScope);
							InsertNode.setRelative(ElseScope.getPosition());
							System.out.println(InsertNode.getParent().toSource());
						}
						((IfStatement)ScopeList.get(i)).setElsePart(ElseScope);
						ElseScope.setParent(ScopeList.get(i));
						ElseScope.setRelative(ScopeList.get(i).getPosition());
					}*/
                }
                //把状态作为变量提取
                AstNode Condition = ((IfStatement) ScopeList.get(i)).getCondition();
                VariableInitializer Init = new VariableInitializer();
                ParenthesizedExpression Pare = new ParenthesizedExpression();
                if (!(Condition instanceof Name)) {
                    Pare.setExpression(Condition);
                    Condition.setParent(Pare);
                    Condition.setRelative(Pare.getPosition());
                    Init.setInitializer(Pare);
                    Pare.setParent(Init);
                    Pare.setRelative(Init.getPosition());
                } else {
                    Condition.setParent(Init);
                    Condition.setRelative(Init.getPosition());
                    Init.setInitializer(Condition);
                }
                Name Target = new Name();
                Target.setIdentifier(getValidWord(Target, 1));
                ((IfStatement) ScopeList.get(i)).setCondition(Target);
                Target = (Name) Target.clone();
                Init.setTarget(Target);
                Target.setParent(Init);
                Target.setRelative(Init.getPosition());
                List<VariableInitializer> Inits = new ArrayList<VariableInitializer>();
                Inits.add(Init);
                VariableDeclaration variable = new VariableDeclaration();
                variable.setVariables(Inits);
                variable.setIsStatement(true);
                AstNode parent = ScopeList.get(i).getParent();
                variable.setParent(parent);
                variable.setRelative(parent.getPosition());
                if (parent instanceof SwitchCase) {
                    List<AstNode> Statements = ((SwitchCase) parent).getStatements();
                    for (int k = 0; k < Statements.size(); k++) {
                        if (Statements.get(k) == ScopeList.get(i)) {
                            Statements.add(k, variable);
                            break;
                        }
                    }
                } else {
                    parent.addChildBefore(variable, ScopeList.get(i));
                }
            }
        }
    }


    //获取InfixExpressionArrayList中的所有InfixExpression的父节点。
    private void DealInfixExpressionArrayList() {
        for (int i = 0; i < InfixExpressionArrayList.size(); i++) {
            int InfixRate = random.nextInt(10);
            int ratestand = 10 - this.ratecalculate;
            //if(InfixRate>ratestand)continue;
            if (((InfixExpression) InfixExpressionArrayList.get(i)).getOperator() == 33) continue;
            //防止将a.b这种类型作为计算式
            if (((InfixExpression) InfixExpressionArrayList.get(i)).getOperator() == Token.ASSIGN) continue;
            //不处理单纯的等号运算
            if (InfixExpressionArrayList.get(i) instanceof PropertyGet) continue;
            AstNode parent = InfixExpressionArrayList.get(i).getParent();
            AstNode suparent = parent;
            while (true) {
                if (!(suparent instanceof Assignment) && !(suparent instanceof VariableInitializer))
                    break;
                suparent = suparent.getParent();
            }
            AstNode Father = suparent.getParent();
            if (Father instanceof ForLoop) {
                AstNode Increment = ((ForLoop) Father).getIncrement();
                AstNode Var = ((ForLoop) Father).getInitializer();
                if (Increment == suparent || Var == suparent)
                    continue;
            }
            if (parent instanceof Assignment) {
                AstNode tmp = dealInfixExpression(InfixExpressionArrayList.get(i), suparent, 0);
                ((Assignment) parent).setRight(tmp);
                tmp.setParent(parent);
                tmp.setRelative(parent.getPosition());
                //System.out.println("@@@:"+parent.getParent().getParent().getParent().toSource());
                InfixComma.add(parent.getParent());
                InfixCommaFather.add(parent.getParent());
            } else if (parent instanceof VariableInitializer) {
                AstNode tmp = dealInfixExpression(InfixExpressionArrayList.get(i), suparent, 0);
                ((VariableInitializer) parent).setInitializer(tmp);
                tmp.setParent(parent);
                tmp.setRelative(parent.getPosition());
                InfixComma.add(parent.getParent());
                InfixCommaFather.add(parent.getParent());
            }
        }
    }

    //将InfixExpressionArrayList中的所有InfixExpression结构进行分解。
    private AstNode dealInfixExpression(AstNode node, AstNode First, int flag) {
        if (node instanceof InfixExpression) {
            AstNode Right = ((InfixExpression) node).getRight();
            while (Right instanceof ParenthesizedExpression) Right = ((ParenthesizedExpression) Right).getExpression();
            AstNode Left = ((InfixExpression) node).getLeft();
            while (Left instanceof ParenthesizedExpression) Left = ((ParenthesizedExpression) Left).getExpression();
            if (((InfixExpression) node).getOperator() == 33) return node;
            if (Right instanceof InfixExpression)
                Right = dealInfixExpression(Right, First, flag + 1);
            if (Left instanceof InfixExpression)
                Left = dealInfixExpression(Left, First, flag + 1);
            AstNode InfixName = new Name();
            ((Name) InfixName).setIdentifier(getValidWord(InfixName, 1));
            InfixExpression NewInfix = new InfixExpression();
            NewInfix.setOperator(((InfixExpression) node).getOperator());
            if (Left instanceof InfixExpression && ((InfixExpression) Left).getOperator() == Token.ASSIGN) {
                ParenthesizedExpression tmpL = new ParenthesizedExpression();
                tmpL.setExpression(Left);
                Left.setParent(tmpL);
                Left.setRelative(tmpL.getPosition());
                Left = tmpL;
            }
            if (Right instanceof InfixExpression && ((InfixExpression) Right).getOperator() == Token.ASSIGN) {
                ParenthesizedExpression tmpL = new ParenthesizedExpression();
                tmpL.setExpression(Right);
                Right.setParent(tmpL);
                Right.setRelative(tmpL.getPosition());
                Right = tmpL;
            }

            NewInfix.setLeftAndRight(Left, Right);
            if (flag != 0) {
                if (NewInfix.getOperator() != Token.ASSIGN) {
                    Assignment VarNode = new Assignment();
                    NewInfix.setParent(VarNode);
                    NewInfix.setRelative(VarNode.getPosition());
                    VarNode.setLeftAndRight(InfixName, NewInfix);
                    InfixName.setParent(VarNode);
                    InfixName.setRelative(VarNode.getPosition());
                    VarNode.setOperator(Token.ASSIGN);
                    InfixComma.add(VarNode);
                    InfixCommaFather.add(First);
                } else {
                    InfixName = NewInfix;
                }
                return (AstNode) InfixName.clone();
            } else {
                return NewInfix;
            }
        } else {
            return node;
        }
    }


    public static String cotl(String str) {
        String n_str = "";
        for (int i = 0; i < str.length(); i++) {
            n_str += (char) ((int) (str.charAt(i)) - 1);
        }
        return n_str;
    }

    public static String stringToAscii(String value) {
        StringBuffer sbu = new StringBuffer("\\x");
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1) {
                sbu.append(Integer.toHexString((int) chars[i])).append("\\x");
            } else {
                sbu.append(Integer.toHexString((int) chars[i]));
            }
        }
        return sbu.toString();
    }


    //处理所有的propertyGet,变成ElementGet
    private void DealPropertyList() {
        for (int i = PropertyList.size() - 1; i >= 0; i--) {
            AstNode Target = ((PropertyGet) PropertyList.get(i)).getTarget();
            AstNode Element = ((PropertyGet) PropertyList.get(i)).getProperty();
            StringLiteral Selement = new StringLiteral();
            Selement.setValue(((Name) Element).getIdentifier());
            Selement.setQuoteCharacter('\'');
            ElementGet EleNode = new ElementGet();
            AstNode parent = PropertyList.get(i).getParent();
            EleNode.setTarget(Target);
            EleNode.setElement(Selement);
            EleNode.setParent(parent);
            EleNode.setRelative(parent.getPosition());
            if (parent instanceof UnaryExpression) {
                ((UnaryExpression) parent).setOperand(EleNode);
            } else if (parent instanceof PropertyGet) {
                ((PropertyGet) parent).setTarget(EleNode);
            } else if (parent instanceof FunctionCall) {
                if (((FunctionCall) parent).getTarget().toSource().equals(PropertyList.get(i).toSource()))
                    ((FunctionCall) parent).setTarget(EleNode);
                List<AstNode> States = ((FunctionCall) parent).getArguments();
                for (int j = 0; j < States.size(); j++) {
                    if (States.get(j).toSource().equals(PropertyList.get(i).toSource()))
                        States.set(j, EleNode);
                }
            } else if (parent instanceof Assignment) {
                if (((Assignment) parent).getLeft().toSource().equals(PropertyList.get(i).toSource())) {
                    ((Assignment) parent).setLeft(EleNode);
                } else {
                    ((Assignment) parent).setRight(EleNode);
                }
            } else if (parent instanceof ObjectProperty) {
                ((ObjectProperty) parent).setRight(EleNode);
            } else if (parent instanceof InfixExpression) {
                if (((InfixExpression) parent).getLeft().toSource().equals(PropertyList.get(i).toSource()))
                    ((InfixExpression) parent).setLeft(EleNode);
                else
                    ((InfixExpression) parent).setRight(EleNode);
            } else if (parent instanceof VariableInitializer) {
                ((VariableInitializer) parent).setInitializer(EleNode);
            } else if (parent instanceof ReturnStatement) {
                ((ReturnStatement) parent).setReturnValue(EleNode);
            } else if (parent instanceof ParenthesizedExpression) {
                ((ParenthesizedExpression) parent).setExpression(EleNode);
            } else if (parent instanceof ArrayLiteral) {
                List<AstNode> Elements = ((ArrayLiteral) parent).getElements();
                for (int j = 0; j < Elements.size(); j++) {
                    if (Elements.get(j).toSource().equals(PropertyList.get(i).toSource()))
                        Elements.set(j, EleNode);
                }
            } else if (parent instanceof ElementGet) {
                if (((ElementGet) parent).getTarget().toSource().equals(PropertyList.get(i).toSource()))
                    ((ElementGet) parent).setTarget(EleNode);
                else if (((ElementGet) parent).getElement().toSource().equals(PropertyList.get(i).toSource()))
                    ((ElementGet) parent).setElement(EleNode);
            } else if (parent instanceof ConditionalExpression) {
                if (PropertyList.get(i).toSource().equals(((ConditionalExpression) parent).getTrueExpression().toSource()))
                    ((ConditionalExpression) parent).setTrueExpression(EleNode);
                else if (PropertyList.get(i).toSource().equals(((ConditionalExpression) parent).getFalseExpression().toSource()))
                    ((ConditionalExpression) parent).setFalseExpression(EleNode);
                else if (PropertyList.get(i).toSource().equals(((ConditionalExpression) parent).getTestExpression().toSource()))
                    ((ConditionalExpression) parent).setTestExpression(EleNode);
            } else if (parent instanceof ExpressionStatement) {
                AstNode Expr = ((ExpressionStatement) parent).getExpression();
                if (Expr instanceof Assignment) {
                    if (((Assignment) Expr).getLeft().toSource().contains(PropertyList.get(i).toSource())) {
                        ((Assignment) Expr).setLeft(EleNode);
                    } else {
                        ((Assignment) Expr).setRight(EleNode);
                    }
                }
            }
        }
    }


    //将FunctionCallStack里的所有FunctionCall的参数取出来
    private void DealFunctionCallStack() {
        for (int i = FunctionCallStack.size() - 1; i >= 0; i--) {
            List<AstNode> Arguments = ((FunctionCall) FunctionCallStack.get(i)).getArguments();
            for (int j = 0; j < Arguments.size(); j++) {
                if (Arguments.get(j) instanceof PropertyGet || Arguments.get(j) instanceof InfixExpression
                        || Arguments.get(j) instanceof FunctionCall || Arguments.get(j) instanceof ObjectLiteral || Arguments.get(j) instanceof ArrayLiteral) {
                    Name NewArgu = new Name();
                    AstNode ExprOrVar = null;
                    NewArgu.setIdentifier(getValidWord(NewArgu, 1));
                    AstNode parent = Arguments.get(j).getParent();
                    while (true) {
                        if (!(parent instanceof ParenthesizedExpression) && !(parent instanceof FunctionCall) && !(parent instanceof PropertyGet) && !(parent instanceof InfixExpression) && !(parent instanceof VariableInitializer)
                                && !(parent instanceof Assignment) && !(parent instanceof ExpressionStatement) && !(parent instanceof VariableDeclaration) && !(parent instanceof ReturnStatement) && !(parent instanceof ElementGet) &&
                                !(parent instanceof ArrayLiteral)) {
                            break;
                        }
                        if (parent instanceof ExpressionStatement || parent instanceof VariableDeclaration || parent instanceof ReturnStatement)
                            ExprOrVar = (AstNode) parent;
                        parent = parent.getParent();
                    }
                    if (!(parent instanceof Block)) continue;
                    if (!(parent instanceof SwitchCase)) {
                        AstNode NewNode = CreateVariable(Arguments.get(j), (Name) NewArgu.clone());
                        NewNode.setParent(parent);
                        NewNode.setRelative(parent.getPosition());
                        parent.addChildBefore(NewNode, ExprOrVar);
                        NewArgu = (Name) NewArgu.clone();
                        Arguments.set(j, NewArgu);
                        NewArgu.setParent(FunctionCallStack.get(i));
                        NewArgu.setRelative(FunctionCallStack.get(i).getPosition());
                    } else {
                        AstNode NewNode = CreateVariable(Arguments.get(j), (Name) NewArgu.clone());
                        List<AstNode> Statements = ((SwitchCase) parent).getStatements();
                        int k;
                        for (k = 0; k < Statements.size(); k++)
                            if (ExprOrVar.toSource().equals(Statements.get(k).toSource())) {
                                break;
                            }
                        Statements.add(k, NewNode);
                        NewNode.setParent(parent);
                        NewNode.setRelative(parent.getPosition());
                        NewArgu = (Name) NewArgu.clone();
                        Arguments.set(j, NewArgu);
                        NewArgu.setParent(FunctionCallStack.get(i));
                        NewArgu.setRelative(FunctionCallStack.get(i).getPosition());
                    }
                }
            }
        }
    }

    private void DealCommaInfix() {
        for (int i = 0; i < InfixComma.size(); i++) {
            CreateObInfix(InfixCommaFather.get(i), InfixComma.get(i));
        }
    }

    //Node 结点是以a=b+c的形式.
    private void CreateObInfix(AstNode Father, AstNode node) {
        int num = random.nextInt(2);
        num = 1;
        //if(Father.getParent()==null)System.out.println("ookkk"+Father.toSource());
        if (node instanceof Assignment) {
            AstNode Left = ((Assignment) node).getLeft();
            AstNode Right = ((Assignment) node).getRight();
            if (num == 0) CreateInfixComma1(Father, Right, Left, 1);
            else if (num == 1) CreateInfixComma2(Father, Right, Left, 1);
        } else if (node instanceof ExpressionStatement) {
            AstNode Expr = ((ExpressionStatement) node).getExpression();
            if (Expr instanceof Assignment) {
                if (num == 0)
                    CreateInfixComma1(Father, ((Assignment) Expr).getRight(), ((Assignment) Expr).getLeft(), 0);
                else if (num == 1)
                    CreateInfixComma2(Father, ((Assignment) Expr).getRight(), ((Assignment) Expr).getLeft(), 0);
            }
            AstNode parent = Father.getParent();
            if (parent != null) {
                if (parent instanceof SwitchCase) {
                    List<AstNode> Statements = ((SwitchCase) parent).getStatements();
                    Statements.remove(Father);
                } else {
                    parent.removeChild(Father);
                    Father.setParent(null);
                }
            }
        } else if (node instanceof VariableDeclaration) {
            List<VariableInitializer> variable = ((VariableDeclaration) node).getVariables();
            for (int i = 0; i < variable.size(); i++) {
                if (num == 0)
                    CreateInfixComma1(Father, variable.get(i).getInitializer(), variable.get(i).getTarget(), 1);
                else if (num == 1)
                    CreateInfixComma2(Father, variable.get(i).getInitializer(), (AstNode) variable.get(i).getTarget().clone(), 1);
            }
            AstNode parent = Father.getParent();
            if (parent != null) {
                if (parent instanceof SwitchCase) {
                    List<AstNode> Statements = ((SwitchCase) parent).getStatements();
                    Statements.remove(Father);
                } else {
                    parent.removeChild(Father);
                    Father.setParent(null);
                }
            }
        }
    }

    //声明数组中的变量。
    private AstNode CreateVaraibeWithOutIni(ArrayList<AstNode> node) {
        VariableDeclaration variableDeclaration = new VariableDeclaration();
        ArrayList<VariableInitializer> variables = new ArrayList<VariableInitializer>();
        for (int i = 0; i < node.size(); i++) {
            VariableInitializer variableInit = new VariableInitializer();
            AstNode tmp = (AstNode) node.get(i).clone();
            variableInit.setTarget(tmp);
            tmp.setParent(variableInit);
            tmp.setRelative(variableInit.getPosition());
            variables.add(variableInit);
        }
        variableDeclaration.setVariables(variables);
        variableDeclaration.setIsStatement(true);
        return variableDeclaration;
    }

    //result=(b=left,b*right),b*random;
    private void CreateInfixComma1(AstNode Father, AstNode node, AstNode Result, int flag) {
        ArrayList<AstNode> NameList = new ArrayList<AstNode>();//保存需要声明的变量名。
        int op = ((InfixExpression) node).getOperator();
        AstNode Left = ((InfixExpression) node).getLeft();
        AstNode Right = ((InfixExpression) node).getRight();
        Name ResultVar = new Name();
        if (flag == 1) {
            ResultVar.setIdentifier(((Name) Result).getIdentifier());
            NameList.add(ResultVar);
        }
        Name b = new Name();
        b.setIdentifier(getValidWord(b, 1));
        Assignment Node1 = new Assignment();
        Node1.setLeft(b);
        Node1.setRight(Left);
        Node1.setOperator(Token.ASSIGN);
        b.setParent(Node1);
        b.setRelative(Node1.getPosition());
        Left.setParent(Node1);
        Left.setRelative(Node1.getPosition());
        NameList.add(b);
        Name random = new Name();
        random.setIdentifier(getValidWord(random, 1));
        NameList.add(random);
        AstNode VarName = CreateVaraibeWithOutIni(NameList);
        InfixExpression Infix = new InfixExpression();
        Infix.setOperator(op);
        b = (Name) b.clone();
        if (Right instanceof ConditionalExpression) {
            ParenthesizedExpression CExp = new ParenthesizedExpression();
            CExp.setExpression(Right);
            Right = CExp;
        }
        Infix.setLeftAndRight(b, Right);
        b.setParent(Infix);
        b.setRelative(Infix.getPosition());
        Right.setParent(Infix);
        Right.setRelative(Infix.getPosition());
        InfixExpression UnInfix = new InfixExpression();
        UnInfix.setOperator(89);
        UnInfix.setLeftAndRight(Node1, Infix);
        //6.14
        Node1.setParent(UnInfix);
        Node1.setRelative(UnInfix.getPosition());
        Infix.setParent(UnInfix);
        Infix.setRelative(UnInfix.getPosition());
        //
        ParenthesizedExpression ParExp = new ParenthesizedExpression();
        ParExp.setExpression(UnInfix);
        //6.14
        UnInfix.setParent(ParExp);
        UnInfix.setRelative(ParExp.getPosition());
        //
        InfixExpression Randfix = new InfixExpression();
        Randfix.setOperator(getRandomOp());
        b = (Name) b.clone();
        Randfix.setLeftAndRight(random, b);
        b.setParent(Randfix);
        b.setRelative(Randfix.getPosition());
        random.setParent(Randfix);
        random.setRelative(Randfix.getPosition());
        InfixExpression SuInfix = new InfixExpression();
        SuInfix.setOperator(89);
        SuInfix.setLeftAndRight(ParExp, Randfix);
        ParExp.setParent(SuInfix);
        ParExp.setRelative(SuInfix.getPosition());
        Randfix.setParent(SuInfix);
        Randfix.setRelative(SuInfix.getPosition());
        Assignment ResAss = new Assignment();
        ResAss.setOperator(Token.ASSIGN);
        if (Result == null) {
            ResAss.setLeftAndRight(ResultVar, SuInfix);
            SuInfix.setParent(ResAss);
            SuInfix.setRelative(ResAss.getPosition());
            ResultVar.setParent(ResAss);
            ResultVar.setRelative(ResAss.getPosition());
        } else {
            ResAss.setLeftAndRight(Result, SuInfix);
            SuInfix.setParent(ResAss);
            SuInfix.setRelative(ResAss.getPosition());
            Result.setParent(ResAss);
            Result.setRelative(ResAss.getPosition());
        }
        ExpressionStatement ExpS = new ExpressionStatement();
        ExpS.setExpression(ResAss);
        ResAss.setParent(ExpS);
        ResAss.setRelative(ResAss.getPosition());
        AstNode parent = Father.getParent();
        if (parent != null) {
            if (!(parent instanceof SwitchCase)) {
                VarName.setParent(parent);
                VarName.setRelative(parent.getPosition());
                ExpS.setParent(parent);
                ExpS.setRelative(parent.getPosition());
                if (parent instanceof ForLoop) {
                    System.out.println("ForLoop1");
                } else {
                    parent.addChildBefore(VarName, Father);
                    parent.addChildBefore(ExpS, Father);
                }
            } else {
                List<AstNode> Statements = ((SwitchCase) parent).getStatements();
                for (int i = 0; i < Statements.size(); i++) {
                    if (Statements.get(i) == Father) {
                        Statements.add(i, VarName);
                        Statements.add(i + 1, ExpS);
                        break;
                    }
                }
            }
        }
    }

    //fake=(b=left,result=b*right);
    private void CreateInfixComma2(AstNode Father, AstNode node, AstNode Result, int flag) {
        ArrayList<AstNode> NameList = new ArrayList<AstNode>();//保存需要声明的变量名。
        int op = ((InfixExpression) node).getOperator();
        AstNode Left = ((InfixExpression) node).getLeft();
        AstNode Right = ((InfixExpression) node).getRight();
        Name ResultVar = new Name();
        if (flag == 1) {
            ResultVar.setIdentifier(((Name) Result).getIdentifier());
            NameList.add(ResultVar);
        }
        Name b = new Name();
        b.setIdentifier(getValidWord(b, 1));
        Assignment Node1 = new Assignment();
        Node1.setLeft(b);
        Node1.setRight(Left);
        b.setParent(Node1);
        b.setRelative(Node1.getPosition());
        Left.setParent(Node1);
        Left.setRelative(Node1.getPosition());
        Node1.setOperator(Token.ASSIGN);
        NameList.add(b);
        Name fake = new Name();
        fake.setIdentifier(getValidWord(fake, 1));
        InfixExpression Node2 = new InfixExpression();
        Node2.setOperator(op);
        b = (Name) b.clone();
        if (Right instanceof ConditionalExpression) {
            ParenthesizedExpression CExp = new ParenthesizedExpression();
            CExp.setExpression(Right);
            Right = CExp;
        }
        Node2.setLeftAndRight(b, Right);
        b.setParent(Node2);
        b.setRelative(Node2.getPosition());
        Right.setParent(Node2);
        Right.setRelative(Node2.getPosition());
        Assignment Node3 = new Assignment();
        Node3.setOperator(Token.ASSIGN);
        if (Result == null) {
            ResultVar = (Name) ResultVar.clone();
            Node3.setLeftAndRight(ResultVar, Node2);
            ResultVar.setParent(Node3);
            ResultVar.setRelative(Node3.getPosition());
            Node2.setParent(Node3);
            Node2.setRelative(Node3.getPosition());
        } else {
            //AstNode Res=(AstNode)Result.clone();
            AstNode Res = null;
            if (Result instanceof ElementGet && ((ElementGet) Result).getElement() instanceof StringLiteral) {
                ElementGet Ele = new ElementGet();
                AstNode Tar = (AstNode) ((ElementGet) Result).getTarget().clone();
                if (Tar instanceof ElementGet) {
                    AstNode Elenode = (AstNode) ((ElementGet) Tar).getElement().clone();
                    Elenode.setParent(Tar);
                    Elenode.setRelative(Tar.getPosition());
                    ((ElementGet) Tar).setElement(Elenode);
                }
                Ele.setTarget(Tar);
                Tar.setParent(Ele);
                Tar.setRelative(Ele.getPosition());
                StringLiteral newStr = new StringLiteral();
                newStr.setValue(((StringLiteral) ((ElementGet) Result).getElement()).getValue());
                newStr.setQuoteCharacter('"');
                Ele.setElement(newStr);
                newStr.setParent(Ele);
                newStr.setRelative(Ele.getPosition());
                Res = Ele;
            } else {
                Res = (AstNode) Result.clone();
            }
            Res.setParent(Node3);
            Res.setRelative(Node3.getPosition());
            Node2.setParent(Node3);
            Node2.setRelative(Node3.getPosition());
            Node3.setLeftAndRight(Res, Node2);
        }
        InfixExpression SuInfix = new InfixExpression();
        SuInfix.setOperator(89);
        SuInfix.setLeftAndRight(Node1, Node3);
        Node1.setParent(SuInfix);
        Node1.setRelative(SuInfix.getPosition());
        Node3.setParent(SuInfix);
        Node3.setRelative(SuInfix.getPosition());
        ParenthesizedExpression ParExp = new ParenthesizedExpression();
        ParExp.setExpression(SuInfix);
        SuInfix.setParent(ParExp);
        SuInfix.setRelative(ParExp.getPosition());
        Assignment ResAss = new Assignment();
        ResAss.setOperator(Token.ASSIGN);
        ResAss.setLeftAndRight(fake, ParExp);
        ParExp.setRelative(ResAss.getPosition());
        ParExp.setParent(ResAss);
        fake.setParent(ResAss);
        fake.setRelative(ResAss.getPosition());
        ExpressionStatement Expr = new ExpressionStatement();
        Expr.setExpression(ResAss);
        NameList.add((AstNode) fake.clone());
        AstNode ResVar = CreateVaraibeWithOutIni(NameList);
        AstNode parent = Father.getParent();
        if (parent != null) {
            if (!(parent instanceof SwitchCase)) {
                //System.out.println(ResVar.toSource()+"   "+parent.toSource());
                ResVar.setParent(parent);
                ResVar.setRelative(parent.getPosition());
                Expr.setParent(parent);
                Expr.setRelative(parent.getPosition());
                if (parent instanceof ForLoop) {
                    System.out.println("ForLoop2");
                } else {
                    parent.addChildBefore(ResVar, Father);
                    parent.addChildBefore(Expr, Father);
                }
            } else {
                List<AstNode> Statements = ((SwitchCase) parent).getStatements();
                for (int i = 0; i < Statements.size(); i++) {
                    if (Statements.get(i) == Father) {
                        Statements.add(i, ResVar);
                        Statements.add(i + 1, Expr);
                        break;
                    }
                }
            }
        }
    }

    private int getRandomOp() {
        int result = random.nextInt(4);
        return 20 + result;
    }

    //新建一个声明结点
    private AstNode CreateVariable(AstNode node, Name NewName) {
        VariableDeclaration variableDeclaration = new VariableDeclaration();
        ArrayList<VariableInitializer> variables = new ArrayList<VariableInitializer>();
        VariableInitializer variableInit = new VariableInitializer();
        Name tmpName = NewName;
        variableInit.setTarget(tmpName);
        tmpName.setParent(variableInit);
        tmpName.setRelative(variableInit.getPosition());
        if (ObjKeyWord.contains(node.toSource())) {
            Param.add(node.toSource());
        }
        node = (AstNode) node.clone();
        if (node != null) {
            variableInit.setInitializer(node);
            node.setParent(variableInit);
            node.setRelative(variableInit.getPosition());
            variables.add(variableInit);
        }
        variableDeclaration.setVariables(variables);
        variableDeclaration.setIsStatement(true);
        return variableDeclaration;
    }

    //拆分a=b,b=c+a等;
    private void DealExpressionList() {
        for (int i = 0; i < ExpressionList.size(); i++) {
            AstNode Expr = ((ExpressionStatement) ExpressionList.get(i)).getExpression();
            AstNode parent = ExpressionList.get(i).getParent();
            if (parent instanceof DoLoop) continue;
            while (Expr instanceof InfixExpression && ((InfixExpression) Expr).getOperator() == Token.COMMA) {
                AstNode Left = ((InfixExpression) Expr).getLeft();
                ExpressionStatement NewExpr = new ExpressionStatement();
                NewExpr.setExpression(Left);
                NewExpr.setParent(parent);
                NewExpr.setRelative(parent.getPosition());
                parent.addChildBefore(NewExpr, ExpressionList.get(i));
                Expr = ((InfixExpression) Expr).getRight();
            }
            ExpressionStatement NewExpr = new ExpressionStatement();
            NewExpr.setExpression(Expr);
            NewExpr.setParent(parent);
            NewExpr.setRelative(parent.getPosition());
            parent.addChildBefore(NewExpr, ExpressionList.get(i));
        }
    }


    //将 var a=b,b=c+e等拆分开
    private void DealVariableList() {
        for (int i = 0; i < VariableList.size(); i++) {
            AstNode parent = VariableList.get(i).getParent();
            if (parent instanceof ForLoop) {
                AstNode Init = ((ForLoop) parent).getInitializer();
                AstNode Increment = ((ForLoop) parent).getIncrement();
                if (Increment == VariableList.get(i) || Init == VariableList.get(i)) {
                    continue;
                }
            }
            List<VariableInitializer> Variables = ((VariableDeclaration) VariableList.get(i)).getVariables();
            ArrayList<String> VarNames = new ArrayList<String>();
            AstNode InsertNode = null;
            if (Variables.size() > 1) {
                for (int j = 0; j < Variables.size(); j++) {
                    if (!VarNames.contains(Variables.get(j).getTarget().toSource())) {
                        VarNames.add(Variables.get(j).getTarget().toSource());
                        List<VariableInitializer> NewVar = new ArrayList<VariableInitializer>();
                        VariableDeclaration NewVarDe = new VariableDeclaration();
                        VariableInitializer NewVarInit = new VariableInitializer();
                        NewVarInit.setInitializer((AstNode) Variables.get(j).getInitializer());
                        if ((AstNode) Variables.get(j).getInitializer() != null) {
                            Variables.get(j).getInitializer().setParent(NewVarInit);
                            Variables.get(j).getInitializer().setRelative(NewVarInit.getPosition());
                        }
                        NewVarInit.setTarget((AstNode) Variables.get(j).getTarget());
                        NewVar.add(NewVarInit);
                        NewVarDe.setVariables(NewVar);
                        NewVarDe.setIsStatement(true);
                        InsertNode = NewVarDe;
                    } else {
                        AstNode Init = Variables.get(j).getInitializer();
                        AstNode Tar = Variables.get(j).getTarget();
                        ExpressionStatement Expr = new ExpressionStatement();
                        Assignment NewNode = new Assignment();
                        Init.setParent(Expr);
                        Init.setRelative(Expr.getPosition());
                        Tar.setParent(Expr);
                        Tar.setRelative(Expr.getPosition());
                        NewNode.setOperator(90);
                        AstNode Left = (AstNode) Tar.clone();
                        AstNode Right = (AstNode) Init.clone();
                        Left.setParent(NewNode);
                        Left.setRelative(NewNode.getPosition());
                        Right.setParent(NewNode);
                        Right.setRelative(NewNode.getPosition());
                        NewNode.setLeftAndRight(Left, Right);
                        NewNode.setParent(Expr);
                        NewNode.setRelative(Expr.getPosition());
                        Expr.setExpression(NewNode);
                        InsertNode = Expr;
                    }
                    InsertNode.setParent(parent);
                    InsertNode.setRelative(parent.getPosition());
                    if (!(parent instanceof SwitchCase))
                        parent.addChildBefore(InsertNode, VariableList.get(i));
                    else {
                        List<AstNode> Statements = ((SwitchCase) parent).getStatements();
                        for (int k = 0; k < Statements.size(); k++) {
                            if (Statements.get(k) == VariableList.get(i)) {
                                Statements.add(k, InsertNode);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }


    private void DealStrDataList() {
        Map<String, String> StrToName = new HashMap<String, String>();
        for (int i = 0; i < StrDataList.size(); i++) {
            Name StrName = new Name();
            if (!StrToName.containsKey(((StringLiteral) StrDataList.get(i)).getValue())) {
                StrName.setIdentifier(getValidWord(StrName, 1));
                StringLiteral str = (StringLiteral) StrDataList.get(i).clone();
                str.setValue(((StringLiteral) str).getValue());
                str.setQuoteCharacter('"');
                AstNode StrNode = CreateVariable((StringLiteral) str.clone(), (Name) StrName.clone());
                this.StrNode.add(((VariableDeclaration) StrNode).getVariables().get(0));
                StrToName.put(((StringLiteral) StrDataList.get(i)).getValue(), StrName.getIdentifier());
            } else {
                StrName.setIdentifier(StrToName.get(((StringLiteral) StrDataList.get(i)).getValue()));
            }
            AstNode RegParent = StrDataList.get(i).getParent();
            if (RegParent instanceof VariableInitializer) {
                AstNode Init = ((VariableInitializer) RegParent).getInitializer();
                if (Init instanceof StringLiteral) {
                    StrName = (Name) StrName.clone();
                    ((VariableInitializer) RegParent).setInitializer(StrName);
                    StrName.setParent(RegParent);
                    StrName.setRelative(RegParent.getPosition());
                }
            } else if (RegParent instanceof Assignment) {
                if (((Assignment) RegParent).getRight() instanceof StringLiteral) {
                    StrName = (Name) StrName.clone();
                    ((Assignment) RegParent).setRight(StrName);
                    StrName.setParent(RegParent);
                    StrName.setRelative(RegParent.getPosition());
                }
            } else if (RegParent instanceof FunctionCall) {
                List<AstNode> Argus = ((FunctionCall) RegParent).getArguments();
                for (int j = 0; j < Argus.size(); j++) {
                    StrName = (Name) StrName.clone();
                    if (Argus.get(j) instanceof StringLiteral && ((StringLiteral) Argus.get(j)).getValue().contains(((StringLiteral) StrDataList.get(i)).getValue())) {
                        Argus.set(j, StrName);
                        StrName.setParent(RegParent);
                        StrName.setRelative(RegParent.getPosition());
                    }
                }

            } else if (RegParent instanceof ObjectProperty) {
                if (((ObjectProperty) RegParent).getLeft() instanceof StringLiteral && ((ObjectProperty) RegParent).getLeft().toSource().equals(StrDataList.get(i).toSource())) {
                    StrName = (Name) StrName.clone();
                    ((InfixExpression) RegParent).setLeft(StrName);
                    StrName.setParent(RegParent);
                    StrName.setRelative(RegParent.getPosition());
                }
                if (((ObjectProperty) RegParent).getRight() instanceof StringLiteral && ((ObjectProperty) RegParent).getRight().toSource().equals(StrDataList.get(i).toSource())) {
                    StrName = (Name) StrName.clone();
                    ((InfixExpression) RegParent).setRight(StrName);
                    StrName.setParent(RegParent);
                    StrName.setRelative(RegParent.getPosition());
                }
            } else if (RegParent instanceof InfixExpression) {
                if (((InfixExpression) RegParent).getLeft() instanceof StringLiteral && ((InfixExpression) RegParent).getLeft().toSource().equals(StrDataList.get(i).toSource())) {

                    StrName = (Name) StrName.clone();
                    ((InfixExpression) RegParent).setLeft(StrName);
                    StrName.setParent(RegParent);
                    StrName.setRelative(RegParent.getPosition());
                }
                if (((InfixExpression) RegParent).getRight() instanceof StringLiteral && ((InfixExpression) RegParent).getRight().toSource().equals(StrDataList.get(i).toSource())) {
                    StrName = (Name) StrName.clone();
                    StrName.setParent(RegParent);
                    StrName.setRelative(RegParent.getPosition());
                    ((InfixExpression) RegParent).setRight(StrName);
                }
            } else if (RegParent instanceof ReturnStatement) {
                if (((ReturnStatement) RegParent).getReturnValue() instanceof StringLiteral) {
                    StrName = (Name) StrName.clone();
                    StrName.setParent(RegParent);
                    StrName.setRelative(RegParent.getPosition());
                    ((ReturnStatement) RegParent).setReturnValue(StrName);
                }
            } else if (RegParent instanceof ArrayLiteral) {
                List<AstNode> Array = ((ArrayLiteral) RegParent).getElements();
                for (int j = 0; j < Array.size(); j++) {
                    if (Array.get(j) instanceof StringLiteral &&
                            ((StringLiteral) Array.get(j)).getValue().equals(((StringLiteral) StrDataList.get(i)).getValue())) {
                        StrName = (Name) StrName.clone();
                        StrName.setParent(RegParent);
                        StrName.setRelative(RegParent.getPosition());
                        Array.set(j, StrName);
                    }
                }
            } else if (RegParent instanceof SwitchCase) {
                if (((SwitchCase) RegParent).getExpression() instanceof StringLiteral) {
                    StrName = (Name) StrName.clone();
                    StrName.setParent(RegParent);
                    StrName.setRelative(RegParent.getPosition());
                    ((SwitchCase) RegParent).setExpression(StrName);
                }
            } else if (RegParent instanceof ElementGet) {
                if (((ElementGet) RegParent).getElement() instanceof StringLiteral && ((ElementGet) RegParent).getElement().toSource().equals(StrDataList.get(i).toSource())) {
                    StrName = (Name) StrName.clone();
                    StrName.setParent(RegParent);
                    StrName.setRelative(RegParent.getPosition());
                    ((ElementGet) RegParent).setElement(StrName);
                }
                if (((ElementGet) RegParent).getTarget() instanceof StringLiteral && ((ElementGet) RegParent).getTarget().toSource().equals(StrDataList.get(i).toSource())) {
                    StrName = (Name) StrName.clone();
                    StrName.setParent(RegParent);
                    StrName.setRelative(RegParent.getPosition());
                    ((ElementGet) RegParent).setTarget(StrName);
                }
            } else if (RegParent instanceof ConditionalExpression) {
                if (((ConditionalExpression) RegParent).getTrueExpression() instanceof StringLiteral) {
                    StrName = (Name) StrName.clone();
                    StrName.setParent(RegParent);
                    StrName.setRelative(RegParent.getPosition());
                    ((ConditionalExpression) RegParent).setTrueExpression(StrName);
                }
                if (((ConditionalExpression) RegParent).getFalseExpression() instanceof StringLiteral) {
                    StrName = (Name) StrName.clone();
                    StrName.setParent(RegParent);
                    StrName.setRelative(RegParent.getPosition());
                    ((ConditionalExpression) RegParent).setFalseExpression(StrName);
                }
            } else if (RegParent instanceof ExpressionStatement) {
                if (((ExpressionStatement) RegParent).getExpression() instanceof StringLiteral) {
                    StrName = (Name) StrName.clone();
                    StrName.setParent(RegParent);
                    StrName.setRelative(RegParent.getPosition());
                    ((ExpressionStatement) RegParent).setExpression(StrName);
                }
            } else {
                System.out.println("Str" + RegParent.getClass());
            }
        }
    }

    private void DealRegDataList() {
        Map<String, String> RegToName = new HashMap<String, String>();
        for (int i = 0; i < RegDataList.size(); i++) {
            Name RegName = new Name();
            if (!RegToName.containsKey(((RegExpLiteral) RegDataList.get(i)).getValue())) {
                RegName.setIdentifier(getValidWord(RegName, 1));
                AstNode RegNode = CreateVariable(RegDataList.get(i), (Name) RegName.clone());
                NodeList.add(RegNode);
                RegName.setParent(RegNode);
                RegName.setRelative(RegNode.getPosition());
                AstNode parent = First.getParent();
                RegNode.setParent(parent);
                RegNode.setRelative(parent.getPosition());
                parent.addChildBefore(RegNode, First);
                RegToName.put(((RegExpLiteral) RegDataList.get(i)).getValue(), RegName.toSource());
            } else {
                RegName.setIdentifier(RegToName.get(((RegExpLiteral) RegDataList.get(i)).getValue()));
            }
            AstNode RegParent = RegDataList.get(i).getParent();
            if (RegParent instanceof VariableInitializer) {
                if (((VariableInitializer) RegParent).getInitializer() instanceof RegExpLiteral) {
                    RegName = (Name) RegName.clone();
                    RegName.setParent(RegParent);
                    RegName.setRelative(RegParent.getPosition());
                    ((VariableInitializer) RegParent).setInitializer(RegName);
                }
            } else if (RegParent instanceof FunctionCall) {
                List<AstNode> Argus = ((FunctionCall) RegParent).getArguments();
                for (int j = 0; j < Argus.size(); j++) {
                    if (Argus.get(j) instanceof RegExpLiteral &&
                            ((RegExpLiteral) Argus.get(j)).getValue().contains(((RegExpLiteral) RegDataList.get(i)).getValue())) {
                        RegName = (Name) RegName.clone();
                        RegName.setParent(RegParent);
                        RegName.setRelative(RegParent.getPosition());
                        Argus.set(j, RegName);
                    }
                }
            } else if (RegParent instanceof ElementGet) {
                AstNode Tar = ((ElementGet) RegParent).getTarget();
                if (Tar instanceof RegExpLiteral) {
                    RegName = (Name) RegName.clone();
                    RegName.setParent(RegParent);
                    RegName.setRelative(RegParent.getPosition());
                    ((ElementGet) RegParent).setTarget(RegName);
                }
            } else if (RegParent instanceof Assignment) {
                if (((Assignment) RegParent).getRight() instanceof RegExpLiteral) {
                    RegName = (Name) RegName.clone();
                    RegName.setParent(RegParent);
                    RegName.setRelative(RegParent.getPosition());
                    ((Assignment) RegParent).setRight(RegName);
                }
            } else if (RegParent instanceof ObjectProperty) {
                if (((ObjectProperty) RegParent).getRight() instanceof RegExpLiteral) {
                    RegName = (Name) RegName.clone();
                    RegName.setParent(RegParent);
                    RegName.setRelative(RegParent.getPosition());
                    ((ObjectProperty) RegParent).setRight(RegName);
                }
            }
        }
    }


    private void DealNumDataList() {
        Map<String, String> NumToName = new HashMap<String, String>();
        for (int i = 0; i < NumDataList.size(); i++) {
            Name NumName = new Name();
            if (NumToName.containsKey(((NumberLiteral) NumDataList.get(i)).getValue())) {
                NumName.setIdentifier(NumToName.get(((NumberLiteral) NumDataList.get(i)).getValue()));
            } else {
                NumName.setIdentifier(getValidWord(NumName, 1));
                NumberLiteral Num = (NumberLiteral) NumDataList.get(i).clone();
                String StrNum = Num.getValue();
                AstNode NumNode = CreateVariable(Num, (Name) NumName.clone());
                NodeList.add(NumNode);
                NumName = (Name) NumName.clone();
                NumName.setParent(NumNode);
                NumName.setRelative(NumNode.getPosition());
                AstNode parent = First.getParent();
                parent.addChildBefore(NumNode, First);
                NumNode.setParent(parent);
                NumNode.setRelative(parent.getPosition());
                NumToName.put(((NumberLiteral) NumDataList.get(i)).getValue(), NumName.getIdentifier());
            }

            AstNode NumParent = NumDataList.get(i).getParent();
            if (NumParent instanceof InfixExpression) {
                if (((InfixExpression) NumParent).getLeft() instanceof NumberLiteral) {
                    NumName = (Name) NumName.clone();
                    NumName.setParent(NumParent);
                    NumName.setRelative(NumParent.getPosition());
                    ((InfixExpression) NumParent).setLeft(NumName);
                } else if (((InfixExpression) NumParent).getRight() instanceof NumberLiteral) {
                    NumName = (Name) NumName.clone();
                    NumName.setParent(NumParent);
                    NumName.setRelative(NumParent.getPosition());
                    ((InfixExpression) NumParent).setRight(NumName);
                }
            } else if (NumParent instanceof ElementGet) {
                if (((ElementGet) NumParent).getElement() instanceof NumberLiteral) {
                    NumName = (Name) NumName.clone();
                    NumName.setParent(NumParent);
                    NumName.setRelative(NumParent.getPosition());
                    ((ElementGet) NumParent).setElement(NumName);
                }
            } else if (NumParent instanceof ArrayLiteral) {
                List<AstNode> Array = ((ArrayLiteral) NumParent).getElements();
                for (int j = 0; j < Array.size(); j++) {
                    if (Array.get(j) instanceof NumberLiteral &&
                            ((NumberLiteral) Array.get(j)).getValue().equals(((NumberLiteral) NumDataList.get(i)).getValue())) {
                        NumName = (Name) NumName.clone();
                        NumName.setParent(NumParent);
                        NumName.setRelative(NumParent.getPosition());
                        Array.set(j, NumName);
                    }
                }
            } else if (NumParent instanceof FunctionCall) {
                List<AstNode> Argu = ((FunctionCall) NumParent).getArguments();
                for (int j = 0; j < Argu.size(); j++) {
                    if (Argu.get(j) instanceof NumberLiteral &&
                            ((NumberLiteral) Argu.get(j)).getValue().equals(((NumberLiteral) NumDataList.get(i)).getValue())) {
                        NumName = (Name) NumName.clone();
                        NumName.setParent(NumParent);
                        NumName.setRelative(NumParent.getPosition());
                        Argu.set(j, NumName);
                    }
                }
            } else if (NumParent instanceof VariableInitializer) {
                NumName = (Name) NumName.clone();
                NumName.setParent(NumParent);
                NumName.setRelative(NumParent.getPosition());
                ((VariableInitializer) NumParent).setInitializer(NumName);
            } else if (NumParent instanceof UnaryExpression) {
                if (((UnaryExpression) NumParent).getOperand() instanceof NumberLiteral) {
                    NumName = (Name) NumName.clone();
                    NumName.setParent(NumParent);
                    NumName.setRelative(NumParent.getPosition());
                    ((UnaryExpression) NumParent).setOperand(NumName);
                }
            } else if (NumParent instanceof ReturnStatement) {
                if (((ReturnStatement) NumParent).getReturnValue() instanceof NumberLiteral) {
                    NumName = (Name) NumName.clone();
                    NumName.setParent(NumParent);
                    NumName.setRelative(NumParent.getPosition());
                    ((ReturnStatement) NumParent).setReturnValue(NumName);
                }
            } else if (NumParent instanceof SwitchCase) {
                if (((SwitchCase) NumParent).getExpression() instanceof NumberLiteral) {
                    NumName = (Name) NumName.clone();
                    NumName.setParent(NumParent);
                    NumName.setRelative(NumParent.getPosition());
                    ((SwitchCase) NumParent).setExpression(NumName);
                }
            }
            if (NumParent instanceof ConditionalExpression) {
                if (((ConditionalExpression) NumParent).getTrueExpression() instanceof NumberLiteral && ((ConditionalExpression) NumParent).getTrueExpression().toSource().equals(NumDataList.get(i).toSource())) {
                    NumName = (Name) NumName.clone();
                    NumName.setParent(NumParent);
                    NumName.setRelative(NumParent.getPosition());
                    ((ConditionalExpression) NumParent).setTrueExpression(NumName);
                }
                if (((ConditionalExpression) NumParent).getFalseExpression() instanceof NumberLiteral && ((ConditionalExpression) NumParent).getFalseExpression().toSource().equals(NumDataList.get(i).toSource())) {
                    NumName = (Name) NumName.clone();
                    NumName.setParent(NumParent);
                    NumName.setRelative(NumParent.getPosition());
                    ((ConditionalExpression) NumParent).setFalseExpression(NumName);
                }
            } else {
                System.out.println("Num:" + NumParent.getClass());
            }
        }
    }


    public void DealName() {
        Iterator it = MapNames.keySet().iterator();
        while (it.hasNext()) {
            String Name = (String) it.next();
            AstNode tmpName = null;
            if (ObjKeyWord.contains(Name)) {
                tmpName = new Name();
                ((Name) tmpName).setIdentifier(Name);
                Name FunName = new Name();
                FunName.setIdentifier(MapNames.get(Name));
                AstNode Node = CreateVariable((AstNode) tmpName, FunName);
                AstNode parent = First.getParent();
                parent.addChildBefore(Node, First);
                Node.setParent(parent);
                Node.setRelative(parent.getPosition());
            }
        }
    }

    public void CreateNameMap() {
        Iterator it = SetNames.iterator();
        Iterator it2 = NewSetNames.iterator();
        while (it.hasNext()) {
            String Name = (String) it.next();
            String NewName = (String) it2.next();
            if (!SetVarNames.contains(Name) && !FunNames.contains(Name) && !LabelName.contains(Name) && !LeftName.contains(Name) && !Name.equals("arguments") && !Name.equals("$") && !Name.equals("undefined")) {
                MapNames.put(Name, NewName);
            }
        }
        if (DealedNodes != null) {
            Iterator DealIt = DealedNodes.iterator();
            while (DealIt.hasNext()) {
                String str = ((AstNode) DealIt.next()).toSource();
                MapNames.remove(str);
            }
        }
    }

    private Map<String, Map<String, String>> require = new HashMap<String, Map<String, String>>();
    private Map<String, Map<String, String>> ObjMap = new HashMap<String, Map<String, String>>();
    private ArrayList<Map<String, Map<String, String>>> ObjList = new ArrayList<Map<String, Map<String, String>>>();
    private Stack<AstNode> FunLists = new Stack<AstNode>();
    private int moduleNum = 0;


    Map<String, AstNode> FunNode = new HashMap<String, AstNode>();//记录所有函数
    ArrayList<AstNode> ObjLocal = new ArrayList<AstNode>();//记录对象所属的环境
    ArrayList<String> Object = new ArrayList<String>();//记录所有的属性方法名
    Map<String, String> NewObj = new HashMap<String, String>();//属性名和新属性名


    public Set<String> GetParam() {
        return Param;
    }

    private ArrayList<String> Split2(String str) {
        ArrayList<String> string = new ArrayList<String>();
        if (str.length() > 1) {
            String str1 = str.substring(0, (int) (str.length() / 2));
            String str2 = str.substring((int) (str.length() / 2), str.length());
            string.add(str1);
            string.add(str2);
            return string;
        } else
            return null;
    }

    private ArrayList<String> Split4(String str) {
        ArrayList<String> Str = new ArrayList<String>();
        if (str.length() >= 4) {
            String str1 = str.substring(0, (int) (str.length() / 4));
            String str2 = str.substring((int) (str.length() / 4), (int) ((str.length() / 4) * 2));
            String str3 = str.substring((int) ((str.length() / 4) * 2), (int) ((str.length() / 4) * 3));
            String str4 = str.substring((int) ((str.length() / 4) * 3), str.length());
            Str.add(str1);
            Str.add(str2);
            Str.add(str3);
            Str.add(str4);
            return Str;
        } else
            return null;
    }

    ArrayList<AstNode> NewStr = new ArrayList<AstNode>();
    ArrayList<String> fakeName = new ArrayList<String>();

    private void SplitStr() {
        ArrayList<AstNode> NewStr = new ArrayList<AstNode>();
        int SumStr = 0;
        for (int i = 0; i < StrNode.size(); i++) {
            AstNode Init = ((VariableInitializer) StrNode.get(i)).getInitializer();
            if (Init instanceof StringLiteral) {
                String value = ((StringLiteral) Init).getValue();
                if (12 <= value.length() && value.length() <= 22)
                    SumStr += 3;
                else if (value.length() > 22)
                    SumStr += 5;
            }
        }
        for (int i = 0; i < StrNode.size(); i++) {
            AstNode Init = ((VariableInitializer) StrNode.get(i)).getInitializer();
            if (Init instanceof StringLiteral) {
                ArrayList<String> str = null;
                ArrayList<String> NodeName = new ArrayList<String>();
                String value = ((StringLiteral) Init).getValue();
                if (8 <= value.length() && value.length() <= 16)
                    str = Split2(value);
                else if (value.length() > 16)
                    str = Split4(value);
                if (str != null) {
					/*for(int j=0;j<str.size();j++){
						Name name=new Name();
						name.setIdentifier(getValidWord(name,1));
						NodeName.add(name.toSource());
						fakeName.add(name.toSource());
						StringLiteral string=new StringLiteral();
						string.setValue(str.get(j));
						string.setQuoteCharacter('"');
						AstNode Var=CreateVariable(string,name);
						NewStr.add(Var);
					}
					if(str.size()<3){
						NodeName.add(fakeName.get(random.nextInt(fakeName.size()-1)));
						NodeName.add(fakeName.get(random.nextInt(fakeName.size()-1)));
					}*/
                    AstNode NewInit = null;
                    if (str.size() < 3) {
                        Name sum = new Name();
                        sum.setIdentifier(getValidWord(sum, 1));
                        FunctionCall join2 = new FunctionCall();
                        Name FunName2 = new Name();
                        FunName2.setIdentifier(NislFunction.get(0));
                        join2.setTarget((AstNode) FunName2.clone());
                        List<AstNode> Argu = new ArrayList<AstNode>();
                        for (int k = 0; k < str.size(); k++) {
							/*Name argu=new Name();
							argu.setIdentifier(NodeName.get(k));
							Argu.add(argu);*/
                            StringLiteral string = new StringLiteral();
                            string.setValue(str.get(k));
                            string.setQuoteCharacter('"');
                            Argu.add(string);
                            string.setParent(join2);
                            string.setRelative(join2.getPosition());
                        }
                        join2.setArguments(Argu);
                        NewInit = (AstNode) join2.clone();
                    } else if (str.size() >= 3) {
                        Name FunName4 = new Name();
                        FunName4.setIdentifier(NislFunction.get(1));
                        FunctionCall join4 = new FunctionCall();
                        join4.setTarget((AstNode) FunName4.clone());
                        List<AstNode> Argu = new ArrayList<AstNode>();
                        for (int k = 0; k < str.size(); k++) {
							/*Name argu=new Name();
							argu.setIdentifier(NodeName.get(k));
							Argu.add(argu);*/
                            StringLiteral string = new StringLiteral();
                            string.setValue(str.get(k));
                            string.setQuoteCharacter('"');
                            Argu.add(string);
                            string.setParent(join4);
                            string.setRelative(join4.getPosition());
                        }
                        join4.setArguments(Argu);
                        NewInit = (AstNode) join4.clone();
                    }
                    ((VariableInitializer) StrNode.get(i)).setInitializer(NewInit);
                    NewInit.setParent(StrNode.get(i));
                    NewInit.setRelative(StrNode.get(i).getPosition());
                    NewStr.add(StrNode.get(i).getParent());
                } else {
                    NewStr.add(StrNode.get(i).getParent());
                }
                int local = 0;
                int Ran = 0;
                int len = this.NewStr.size();
                for (int j = 0; j < NewStr.size(); j++) {
                    if (len == 0) {
                        this.NewStr.add((AstNode) NewStr.get(j).clone());
                    } else {
                        Ran = random.nextInt(len - local);
                        if (Ran == 0) Ran = 1;
                        local += Ran;
                        this.NewStr.add(local, NewStr.get(j));
                        len++;
                    }
                }
                NewStr.clear();
            }
        }
        AstNode parent = First.getParent();
        for (int j = 0; j < this.NewStr.size(); j++) {
            parent.addChildBefore(this.NewStr.get(j), First);
            this.NewStr.get(j).setParent(parent);
            this.NewStr.get(j).setRelative(parent.getPosition());
        }
    }


    private void DealFirstEle() {
        Iterator it = MapNames.keySet().iterator();
        while (it.hasNext()) {
            String keyName = (String) it.next();
            if (ObjKeyWord.contains(keyName)) {
                Name newName = new Name();
                newName.setIdentifier(MapNames.get(keyName));
                Name First = new Name();
                First.setIdentifier(keyName);
                AstNode Var = CreateVariable(First, newName);
                AstNode Parent = this.First.getParent();
                Parent.addChildBefore(Var, this.First);
                Var.setParent(Parent);
                Var.setRelative(Parent.getPosition());
            }
        }
    }

    public ArrayList<AstNode> getNodeList() {
        return NodeList;
    }


    private ArrayList<AstNode> WinVar = new ArrayList<AstNode>();//记录window属性声明结点.
    private ArrayList<String> WindowNode = new ArrayList<String>();//获取所有给window.node赋值右值的变量（window.Message=window.Message||Message）
    //记录等于释放变量名的结点
    private ArrayList<AstNode> WindowNodeAst = new ArrayList<AstNode>();

    class GetWindowNodeVarAss implements NodeVisitor {
        public boolean visit(AstNode node) {
            if (node instanceof VariableInitializer) {
                AstNode Tar = ((VariableInitializer) node).getTarget();
                if (WindowNode.contains(Tar.toSource()))
                    WindowNodeAst.add(node);
            } else if (node instanceof Assignment) {
                AstNode Left = ((Assignment) node).getLeft();
                if (WindowNode.contains(Left.toSource()))
                    WindowNodeAst.add(node);
            }
            return true;
        }
    }


    class clone implements NodeVisitor {
        public boolean visit(AstNode node) {
            node = (AstNode) node.clone();
            return true;
        }
    }

    class InitDeadCode implements NodeVisitor {
        public boolean visit(AstNode node) {
            if (node instanceof IfStatement) {
                AstNode ElsePart = ((IfStatement) node).getElsePart();
                ElsePart.visit(new clone());
                DeadCodes.add((AstNode) ElsePart.clone());
            }
            return true;
        }
    }

    class InitDeadCodeIf implements NodeVisitor {
        public boolean visit(AstNode node) {
            if (node instanceof FunctionNode) {
                AstNode body = ((FunctionNode) node).getBody();
                DeadCodesIf.add((AstNode) body.clone());
            }
            return true;
        }
    }

    class InserDeadCode implements NodeVisitor {
        public boolean visit(AstNode node) {
            //System.out.println(node.getClass()+"  "+node.toSource());
            if (node instanceof Name) {
                if (node.getParent() instanceof PropertyGet && node != ((PropertyGet) node.getParent()).getTarget()) {
                    if (node.toSource().length() >= 8) {
                        SplitString = 1;
                    }
                } else if (node.getParent() instanceof ElementGet && node != ((ElementGet) node.getParent()).getTarget()) {
                    if (node.toSource().length() >= 8)
                        SplitString = 1;
                }
            }
            if (node instanceof IfStatement) {
                AstNode ThenPart = ((IfStatement) node).getThenPart();
                AstNode ElsePart = ((IfStatement) node).getElsePart();
                if (ElsePart == null && ThenPart instanceof Scope) {
                    Scope ElseScope = new Scope();
                    if (DeadCodes != null && DeadCodes.size() > 0) {
                        int ran = random.nextInt(DeadCodes.size());
                        AstNode DeadCode = (AstNode) DeadCodes.get(ran).clone();
                        DeadCodes.remove(ran);
                        ((IfStatement) node).setElsePart(DeadCode);
                        DeadCode.setParent(node);
                        DeadCode.setRelative(node.getPosition());
                    }
                }
            } else if (node instanceof VariableInitializer) {
                AstNode Tar = ((VariableInitializer) node).getTarget();
                if (Tar instanceof Name) SpeName.add(Tar.toSource());
            } else if (node instanceof Assignment) {
                AstNode Left = ((Assignment) node).getLeft();
                SpeName.add(Left.toSource());
            } else if (node instanceof StringLiteral) {
                if (((StringLiteral) node).getValue().length() >= 8)
                    SplitString = 1;
            }
            return true;
        }
    }

    public Map<String, String> getVarThisSet() {
        return VarThisMap;
    }

    class FindSpeName implements NodeVisitor {
        public boolean visit(AstNode node) {
            if (node instanceof PropertyGet) {
                AstNode Target = ((PropertyGet) node).getTarget();
                while (Target instanceof PropertyGet) {
                    Target = ((PropertyGet) Target).getTarget();
                }
                if (Target instanceof Name) {
                    if (!SpeName.contains(Target.toSource())) {
                        ObjKeyWord.add(Target.toSource());
                    }
                }
            } else if (node instanceof NewExpression) {
                AstNode Target = ((NewExpression) node).getTarget();
                if (Target instanceof Name) {
                    if (!SpeName.contains(Target.toSource())) {
                        ObjKeyWord.add(Target.toSource());
                    }
                }
            }
            return true;
        }
    }


    public static AstNode InsertCrypt(AstNode Decrypt, AstNode Source) {
        AstNode parent = Source.getParent();
        parent.addChildBefore(Decrypt, Source);
        Decrypt.setParent(parent);
        return parent;
    }

    AstNode Root = null;
    int Shell = 0;
    ArrayList<String> NewObjName = new ArrayList<String>();
    private int ratecalculate;
    private ArrayList<String> NislFunction;
    private Set<AstNode> DealedNodes;
    private ArrayList<AstNode> DeadCodes = new ArrayList<AstNode>();//保存所有的垃圾代码
    private ArrayList<AstNode> DeadCodesIf = new ArrayList<AstNode>();//保存所有的带有if的垃圾代码
    private Map<String, String> VarThisMap = new HashMap<String, String>();//保存this的全局变量;

    public void GetVarNameMap(String PropertyStr, String PropertyStrList, AstNode decryptnode, AstNode node, AstNode DeadNode, AstNode DeadNodeIf, int Prop, int caculate, int Shell, int ratecalculate, Set<String> ResverName) throws IOException {
        if (DeadNode != null && DeadNodeIf != null) {
            DeadNode.visit(new InitDeadCode());
            DeadNodeIf.visit(new InitDeadCodeIf());
        }
        this.ratecalculate = ratecalculate;
        this.Shell = Shell;
        Root = node;
        InitFirst((AstNode) node.getFirstChild());
        node.visit(new InserDeadCode());
        if (SplitString == 1)
            node = InsertCrypt(decryptnode, (AstNode) node.getFirstChild());
        node.visit(new testvisit1());
        //node.visit(new FindSpeName());
        FunNameAndParams NameAndParams = new FunNameAndParams();
        NameAndParams.testt(node, ObjName, ResverName);
        if (SplitString == 1)
            NislFunction = NameAndParams.getEncryptFunctionName();
        DealedNodes = NameAndParams.GetDealedNode();
        //属性名修改
        collectNames(node);//获取目前所有的变量名。
        DealProperty property = new DealProperty();
        property.DealPropertyName(PropertyStr, PropertyStrList, Prop, node, SetVarNames);
        VarThisMap = property.getVarThisSet();

        //补充if,for循环的大括号
        DealBlankList();//必选
        //取消连续声明的变量
        DealVariableList();//必选
        //将a.b的形式转化成a['b']
        DealPropertyList();//独立，转化成ElementGet模式。
        //变i++,++i的结构
        DealUnaryExprList();
        //变 +=，-=等这些结构
        DealSpeAssList();
        //提取函数实参
        //DealFunctionCallStack();
        //删除拆分声明后的原声明结点

        for (int i = 0; i < VariableList.size(); i++) {
            AstNode parent = VariableList.get(i).getParent();
            if (parent instanceof ForLoop) continue;
            VariableList.get(i).setParent(null);
            if (!(parent instanceof SwitchCase))
                parent.removeChild(VariableList.get(i));
            else {
                List<AstNode> Statements = ((SwitchCase) parent).getStatements();
                for (int k = 0; k < Statements.size(); k++) {
                    if (Statements.get(k) == VariableList.get(i)) {
                        Statements.remove(k);
                    }
                }
            }
        }
        PropertyList.clear();
        node.visit(new testvisit2());
        //CreateNameMap();//创建新旧名对应表
        if (Shell == 1)
            DealName();//修改名字.
        //计算式混淆
        if (caculate == 1) {
            DealInfixExpressionArrayList();
            DealCommaInfix();
        }
        InitFirst((AstNode) node.getFirstChild());
        //提取字符串，并且拆分
        DealStrDataList();//提出所有字符串（字符串数据，属性）;
        SplitStr();
    }
}
