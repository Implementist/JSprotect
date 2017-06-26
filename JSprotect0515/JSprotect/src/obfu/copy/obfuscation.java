package obfu.copy;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.ArrayLiteral;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.Block;
import org.mozilla.javascript.ast.ConditionalExpression;
import org.mozilla.javascript.ast.ElementGet;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.ForLoop;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.IfStatement;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.LabeledStatement;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NewExpression;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.NumberLiteral;
import org.mozilla.javascript.ast.ObjectLiteral;
import org.mozilla.javascript.ast.ObjectProperty;
import org.mozilla.javascript.ast.ParenthesizedExpression;
import org.mozilla.javascript.ast.PropertyGet;
import org.mozilla.javascript.ast.ReturnStatement;
import org.mozilla.javascript.ast.StringLiteral;
import org.mozilla.javascript.ast.SwitchCase;
import org.mozilla.javascript.ast.SwitchStatement;
import org.mozilla.javascript.ast.ThrowStatement;
import org.mozilla.javascript.ast.UnaryExpression;
import org.mozilla.javascript.ast.VariableDeclaration;
import org.mozilla.javascript.ast.VariableInitializer;
import org.mozilla.javascript.ast.WhileLoop;

class obfuscation{
	private FunctionNode Func;
	private ArrayList<AstNode> ArrayNode=new ArrayList<AstNode>();
	private Set<String> window =new HashSet<String>(); 
	
	
	private List<String> StringValue=new ArrayList<String>();
	private List<AstNode> StringNode1=new ArrayList<AstNode>();
	private List<AstNode> StringNode2=new ArrayList<AstNode>();
	private Set RandomNum=new HashSet();
	private List<String>ArrayName=new ArrayList<String>();
	private Set NumberValue=new HashSet();
	private List<String> StringArr1=new ArrayList<String>();
	private List<String> StringArr2=new ArrayList<String>();
	
	
	
	private Map<String,String> MapNames =new HashMap<String,String>();
	private Set<String> SetNames=new HashSet<String>();
	private Set<String> NewSetNames=new HashSet<String>();
	private ArrayList<String>   NamesInFlattern;
    private ArrayList<String>   ones;
    private ArrayList<String>   twos;
    private ArrayList<String>   threes;
    private int total=19982;

    private Random random;
    private int StrNum;
    private int Numnum;

    obfuscation(){
        this.initVariablesPool();
        this.NamesInFlattern=new ArrayList<String>();
        this.random=new Random();
        this.StrNum=0;
        this.Numnum=0;
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

        twos.remove("as");
        twos.remove("is");
        twos.remove("do");
        twos.remove("if");
        twos.remove("in");
        threes.remove("for");
        threes.remove("int");
        threes.remove("new");
        threes.remove("try");
        threes.remove("use");
        threes.remove("var");
    }
    
    private void collectNames(AstNode node) {
		node.visit(new Visitor());
	}
    
    private void InitWindow(){
    	window.add("document");
    	window.add("clearInterval");
    	window.add("alert");
    	window.add("clearTimeout");
    	window.add("confirm");
    	window.add("prompt");
    	window.add("resizeBy");
    	window.add("resizeTo");
    	window.add("scrollBy");
    	window.add("scrollTo");
    	window.add("setInterval");
    	window.add("setTimeout");
    }
    
    class Visitor implements NodeVisitor{
    	public boolean visit(AstNode node){
    		if(node.getType()==Token.NAME)
    			NamesInFlattern.add(((Name)node).getIdentifier());
    		return true;
    	}
    }

    public String getWordFromThePool() {
        int index = this.random.nextInt(this.total);
        String word = this.threes.get(index);
        return word;
    }
    
    public  String getValidWord(AstNode node) {
        this.NamesInFlattern.clear();
        this.collectNames(node);

        String word = this.getWordFromThePool();

        while (this.NamesInFlattern.contains(word)) {
            word = this.getWordFromThePool();
        }
        return word;
    }

	private void processNode(AstNode node){
    	switch(node.getType()){
    		//case Token.FUNCTION:this.processFunctionNode(node);
    		//	break;
    		case Token.CALL:this.processFunctionCall(node);
    			break;
    		case Token.EXPR_RESULT:this.processExpressionStatemetResult(node);
    			break;
    		case Token.EXPR_VOID:this.processExpressionStatementVoid(node);
    			break;
    		case Token.ASSIGN:this.processAssignment(node);
    			break;
    //		case Token.COLON:this.processObjectProperty(node);
    //			break;
    		case Token.IF:this.processIfStatement(node);
    			break;
    		//case Token.BLOCK:this.processScope(node);
    			//break;
    		case Token.SWITCH:this.processSwitchStatement(node);
    			break;
    		case Token.CASE:this.processSwitchCase(node);
    			break;
    		case Token.FOR:this.processForLoop(node);
    			break;
    		case Token.WHILE:this.processWhileLoop(node);
    			break;
    		case Token.VAR:this.processVariable(node);
    			break;
    		case Token.RETURN:this.processReturn(node);
				break;
    		case Token.OBJECTLIT:this.processObjectLiteral(node);
    			break;
    		case Token.THROW:this.processThrow(node);
				break;
    		case Token.NEW:this.processNew(node);
				break;
    	}
    }
	
	
	public AstNode dealdetail(AstNode node){
		if(node instanceof LabeledStatement){
			System.out.println("Find it!");
		}else
		if(node instanceof UnaryExpression){
			AstNode Unop=((UnaryExpression)node).getOperand();
			if(Unop!=null){
				Unop=dealdetail(Unop);
			}
			((UnaryExpression)node).setOperand(Unop);
		}else
		if(node instanceof ArrayLiteral){
			List<AstNode> ArrayEle=((ArrayLiteral)node).getElements();
			for(int i=0;i<ArrayEle.size();i++){
				ArrayEle.set(i,dealdetail(ArrayEle.get(i)));
			}
			
		}else
		if(node instanceof ParenthesizedExpression){
			AstNode ParNode=((ParenthesizedExpression)node).getExpression();
			((ParenthesizedExpression)node).setExpression(dealdetail(ParNode));
		}else
		if(node instanceof FunctionCall){
			AstNode Callnode=((FunctionCall)node).getTarget();
			((FunctionCall)node).setTarget(dealdetail(Callnode));
			List<AstNode>ArgNode=((FunctionCall)node).getArguments();
			for(int i=0;i<ArgNode.size();i++){
				ArgNode.set(i, dealdetail(ArgNode.get(i)));
			}
		}else
		if(node instanceof ConditionalExpression){
			AstNode TExpr=dealdetail(((ConditionalExpression)node).getTrueExpression());
			AstNode FExpr=dealdetail(((ConditionalExpression)node).getFalseExpression());
			AstNode TeExpr=dealdetail(((ConditionalExpression)node).getTestExpression());
			((ConditionalExpression)node).setTrueExpression(TExpr);
			((ConditionalExpression)node).setFalseExpression(FExpr);
			((ConditionalExpression)node).setTestExpression(TeExpr);
		}else 
			if(node instanceof PropertyGet){
				Stack<AstNode> tmpStack=new Stack<AstNode>();
				while(true){
					AstNode ProTar=((PropertyGet)node).getTarget();
					AstNode ProPro=((PropertyGet)node).getProperty();
					ProTar=dealdetail(ProTar);
					ProPro=dealdetail(ProPro);
					tmpStack.push(ProPro);
					node=ProTar;
					if(!(ProTar instanceof PropertyGet)){
						if(ProTar instanceof Name){
							String tmpname=((Name)ProTar).getIdentifier();
							if(window.contains(tmpname)){
		    					Name tmo=new Name();
		    					tmo.setIdentifier("window");
		    					if(!SetNames.contains("window")){
		    						SetNames.add("window");
		    						String TmName=getValidWord((AstNode)tmo);
		    						NewSetNames.add(TmName);
		    					}
		        				tmpStack.push(ProTar);
		        				ProTar=(AstNode)tmo;
		        				node=(AstNode)tmo;
		        			}
		    				
						}
						break;
					}
				}
				while(!tmpStack.isEmpty()){
					AstNode tmp=tmpStack.pop();
	    			ElementGet tmpElem=new ElementGet(node,tmp);
	    			node=tmpElem;
				}
			}else
		if(node instanceof InfixExpression){
			AstNode LInfix=((InfixExpression)node).getLeft();
			AstNode RInfix=((InfixExpression)node).getRight();
			((InfixExpression)node).setLeft(dealdetail(LInfix));
			((InfixExpression)node).setRight(dealdetail(RInfix));
		}else
		if(node.getType()==Token.GETELEM){
    		AstNode EleNode=((ElementGet)node).getElement();
    		EleNode=dealdetail(EleNode);
    		((ElementGet)node).setElement(EleNode);
    		AstNode tmp=((ElementGet)node).getTarget();
    		tmp=dealdetail(tmp);
    		((ElementGet)node).setTarget(tmp);
    	}
		return node;
	}
	
	
	private void processNew(AstNode node){
		ObjectLiteral ObjNew=((NewExpression)node).getInitializer();
		if(ObjNew!=null){
			((NewExpression)node).setInitializer((ObjectLiteral)dealdetail(ObjNew));
		}
	}
	
	private void processThrow(AstNode node){
		AstNode ThExpr=((ThrowStatement)node).getExpression();
		if(ThExpr!=null){
			((ThrowStatement)node).setExpression(dealdetail(ThExpr));
		}
	}
	
	private void processSwitchCase(AstNode node){
		AstNode Expr=((SwitchCase)node).getExpression();
		if(Expr!=null){
			((SwitchCase)node).setExpression(dealdetail(Expr));
		}
		List<AstNode>statements=((SwitchCase)node).getStatements();
		if(statements!=null)
		for(int i=0;i<statements.size();i++){
			if(statements.get(i)!=null){
				System.out.println(statements.get(i).toSource());
			}
		}
	}
	
	private void processSwitchStatement(AstNode node){
		AstNode SwExpr=((SwitchStatement)node).getExpression();
		if(SwExpr!=null){
			((SwitchStatement)node).setExpression(dealdetail(SwExpr));
		}
	}
	
	private void processFunctionCall(AstNode node){
		AstNode Callnode=((FunctionCall)node).getTarget();
		((FunctionCall)node).setTarget(dealdetail(Callnode));
		List<AstNode>ArgNode=((FunctionCall)node).getArguments();
		for(int i=0;i<ArgNode.size();i++){
			ArgNode.set(i, dealdetail(ArgNode.get(i)));
		}
	}
	
	private void processWhileLoop(AstNode node){
		AstNode WhileCon=((WhileLoop)node).getCondition();
		if(WhileCon!=null){
			((WhileLoop)node).setCondition(dealdetail(WhileCon));
		}
	}
	
	private void processReturn(AstNode node){
		AstNode ReturnV=((ReturnStatement)node).getReturnValue();
		if(ReturnV!=null){
			((ReturnStatement)node).setReturnValue(dealdetail(ReturnV));
		}
	}
	
    private void processForLoop(AstNode node){
    	if(node instanceof ForLoop){
    		if(((ForLoop)node).getInitializer()!=null){
    			((ForLoop)node).setInitializer(dealdetail(((ForLoop)node).getInitializer()));
    		}
    		if(((ForLoop)node).getCondition()!=null){
    			((ForLoop)node).setCondition(dealdetail(((ForLoop)node).getCondition()));
    		}
    		if(((ForLoop)node).getIncrement()!=null){
    			((ForLoop)node).setIncrement(dealdetail(((ForLoop)node).getIncrement()));
    		}
    	}
    }
	
	private void processAssignment(AstNode node){
    	AstNode LNode=((Assignment)node).getLeft();
    	AstNode RNode=((Assignment)node).getRight();
    	if(LNode!=null){
    		AstNode Ln=dealdetail(LNode);
    		((Assignment)node).setLeft(Ln);
    	}
    	if(RNode!=null){
    		AstNode Rn=dealdetail(RNode);
    		((Assignment)node).setRight(Rn);
    	}
    }
	
	 private void processIfStatement(AstNode node){
	    	AstNode Condition=((IfStatement)node).getCondition();
	    	((IfStatement)node).setCondition(dealdetail(Condition));
	    }
	
	 private void processObjectLiteral(AstNode node){
		 List<ObjectProperty> tmpList=((ObjectLiteral)node).getElements();
		 for(int i=0;i<tmpList.size();i++)
			tmpList.set(i, (ObjectProperty)dealdetail(tmpList.get(i)));
	 }
	
	 private void processVariable(AstNode node){
	    	if(node instanceof VariableInitializer){
	    		AstNode nnode=((VariableInitializer)node).getTarget();
	    		if(nnode!=null)
	    			((VariableInitializer)node).setTarget(dealdetail(nnode));
	    		AstNode InitNode=((VariableInitializer)node).getInitializer();
	    		if(InitNode!=null)
	    			((VariableInitializer)node).setInitializer(dealdetail(InitNode));
	    	}
	    }
	
	
	
	public void processExpressionStatemetResult(AstNode node){
		AstNode Expr_res=((ExpressionStatement)node).getExpression();
		((ExpressionStatement)node).setExpression(dealdetail(Expr_res));
	}
	
	public void processExpressionStatementVoid(AstNode node){
		AstNode Expr_res=((ExpressionStatement)node).getExpression();
		((ExpressionStatement)node).setExpression(dealdetail(Expr_res));
	}
	
	
	class testvisit implements NodeVisitor{
		public boolean visit(AstNode node){
			 processNode(node);
			return true;
		}
	}


	
	
	
	//设置多个变量指向部分，将elementget类型进行拆分�?
	private Set<String> EleSet=new HashSet<String>();
	private Stack<AstNode> EleStack=new Stack<AstNode>();
	private Map<String,ArrayList<AstNode>> EleMap=new HashMap<String,ArrayList<AstNode>>();
	class test implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node.getType()== Token.FUNCTION){
				AstNode Fstart=(AstNode)((FunctionNode)node).getBody().getFirstChild();
				while(Fstart!=null){
					dealdivide(Fstart);
					Fstart=(AstNode)Fstart.getNext();
				}
			}
			return true;
		}
	}
	

	private void dealdivide(AstNode node){
		System.out.println(node.getClass());
		if(node.getType()== Token.EXPR_VOID){
			ProceResult(node);	
		}
	}
	
	private void ProceCall(AstNode node){
		AstNode Target=((FunctionCall)node).getTarget();
		System.out.println(Target.getClass());
	}
	
	private void ProceResult(AstNode node){
		AstNode Expr=((ExpressionStatement)node).getExpression();
		AstNode ResNode=dealEle(Expr,3,node,0);
		System.out.println(ResNode.toSource());
	}
	
	private AstNode dealKinds(AstNode node){
		
		return node;
	}
	//处理elementget结构
	private AstNode dealEle(AstNode node,int ParaNum,AstNode First,int flag){
		ArrayList<AstNode>ElemDec=new ArrayList<AstNode>();
		ArrayList<AstNode>ElemIni=new ArrayList<AstNode>();
		ArrayList<AstNode>SingleInfix=new ArrayList<AstNode>();
		AstNode Element,Target;
		Name TarName;
		if(!EleSet.contains(node.toSource())){
			EleSet.add(node.toSource());
		}else{
			System.out.println(node.toSource());
			int num=random.nextInt(3);
			ArrayList<AstNode> bfList=EleMap.get(node.toSource());
			return (Name)bfList.get(num);
		}
		while(true){
			AstNode NTmpTar=null;
			Element=((ElementGet)node).getElement();
			if(!EleSet.contains(Element.toSource())){
				EleSet.add(Element.toSource());
			}
			EleStack.push(Element);
			Target=((ElementGet)node).getTarget();
			if(!EleSet.contains(Target.toSource()))
				EleSet.add(Target.toSource());
			if(!(Target instanceof ElementGet)){	
				break;
			}
			node=Target;
		}
		node=(AstNode)Target.clone();
		if(!EleSet.contains(node)){
			TarName=BNVariable(node,ParaNum,Target,ElemDec,ElemIni);
		}else{//当之前出现过该结点时，获取之前声明的名字�?
			System.out.println("ea");
			ArrayList<AstNode>TmpList=EleMap.get(node.toSource());
			int len=TmpList.size();
			TarName=(Name)TmpList.get(random.nextInt(len));
		}
		node=TarName;
		while(!EleStack.isEmpty()){
			AstNode OldEle=(AstNode)EleStack.pop();
			AstNode NewEle=(AstNode)OldEle.clone();
			if(!EleSet.contains(NewEle)){
				NewEle=BNVariable(NewEle,ParaNum,OldEle,ElemDec,ElemIni);
			}else{//当之前出现过该结点时，获取之前声明的名字�?
				ArrayList<AstNode>TmpList=EleMap.get(NewEle.toSource());
				int len=TmpList.size();
				((Name)NewEle).setIdentifier(((Name)TmpList.get(random.nextInt(len))).getIdentifier());
			}
			ElementGet OtmpEle=new ElementGet(Target,OldEle);
			ElementGet tmpEle=new ElementGet(node,NewEle);
			if(node instanceof ElementGet){
				ElementGet NtmpEle=new ElementGet(TarName,NewEle);
				TarName=BNVariable(NtmpEle,ParaNum,OtmpEle,ElemDec,ElemIni);
				//System.out.println(TarName.toSource()+":"+NtmpEle.toSource());
			}else{
				TarName=BNVariable(tmpEle,ParaNum,OtmpEle,ElemDec,ElemIni);
			}
			Target=OtmpEle;
			VariableInitializer SingleVar=new VariableInitializer();
			SingleVar.setTarget(TarName);
			SingleVar.setInitializer(tmpEle);
			SingleInfix.add(SingleVar);
			//System.out.println(SingleVar.toSource());
			//System.out.println(OtmpEle.toSource()+";"+tmpEle.toSource()+";"+TarName.toSource());
			node=TarName;
		}
		AstNode Pare=First.getParent();
		makeInfix(SingleInfix);
		//AstNode First=(AstNode)Pare.getFirstChild();
		for(int i=ElemDec.size()-1;i>=0;i--){
			Pare.addChildBefore(ElemIni.get(i),First);
			Pare.addChildBefore(ElemDec.get(i),ElemIni.get(i));
			First=ElemDec.get(i);
		}
		//System.out.println(node.toSource());
		return node;
	}
	
	private ArrayList<AstNode> GetThreeName(int num){
		ArrayList<AstNode> VarNameList=new ArrayList<AstNode>();
		for(int i=0;i<num;i++){
			Name Nname=new Name();
			String Sname=getValidWord(Nname);
			Nname.setIdentifier(Sname);
			VarNameList.add(Nname);
		}
		return VarNameList;
	}
	
	
	//IsMulti设置是否进行多个变量指向;
	private Name BNVariable(AstNode node,int IsMulti,AstNode OldNode,ArrayList<AstNode> ElemDec,ArrayList<AstNode> ElemInit){
		System.out.println("aa:"+OldNode.toSource());
		Name resName=null;
		int num= this.random.nextInt(IsMulti);
		VariableDeclaration variableDeclaration=new VariableDeclaration();
		ArrayList<AstNode> names=GetThreeName(IsMulti);
		if(!EleMap.keySet().contains(OldNode.toSource())){
			EleMap.put(OldNode.toSource(), names);
			for(int i=0;i<IsMulti;i++){
				VariableInitializer variableInit=new VariableInitializer();
				VariableInitializer DvariableInit=new VariableInitializer();
				variableInit.setTarget(names.get(i));
				DvariableInit.setTarget(names.get(IsMulti-i-1));
				variableDeclaration.addVariable(DvariableInit);
				/*if(node instanceof Name){
					StringLiteral tmpName=new StringLiteral();
					tmpName.setValue("test");
					node=(AstNode)tmpName;
				}*/
				variableInit.setInitializer(node);
				node=(AstNode)variableInit;//数据赋�??;
				//System.out.println("res:"+node.toSource());
			}
			ExpressionStatement ExpS=new ExpressionStatement();
			ExpS.setExpression(node);
			variableDeclaration.setIsStatement(true);//数据声明;
			resName=(Name)names.get(num);
			ElemDec.add(variableDeclaration);
			ElemInit.add(ExpS);
		}else{
			ArrayList<AstNode> bfList=EleMap.get(OldNode.toSource());
			resName=(Name)bfList.get(num);
		}
		return resName;
	}
	
	private void makeInfix(ArrayList<AstNode> ArrayInit){
		AstNode before=null;
		InfixExpression tmpInfix=null;
		for(int i=0;i<ArrayInit.size();i++){
			AstNode tmp=((VariableInitializer)(ArrayInit.get(i))).getInitializer();
			AstNode tmpEp=ArrayInit.get(i);
			if(before!=null){
				tmpInfix=new InfixExpression();
				tmpInfix.setOperator(89);
				tmpInfix.setLeft(before);
				tmpInfix.setRight(tmpEp);
				before=tmpInfix;
					// System.out.println(before.toSource());
			}else{
				before=(AstNode)tmpEp.clone();
			}
		}
		System.out.println("res:"+before.toSource());
	}
	
	class test2 implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof InfixExpression){
				//System.out.println(((InfixExpression)node).getOperator().toSource());
			}
			return true;
		}
	}
	
	
	public void GetVarNameMap(AstNode node) throws IOException {
		try{
			InitWindow();
			//node.visit(new test2());
			node.visit(new testvisit());
			node.visit(new test());
			//Iterator it=EleSet.iterator();
			//while(it.hasNext()){
				//String test=((AstNode)it.next()).toSource();
			//	System.out.println(":"+it.next());
			//}
			FileWriter fw = new FileWriter("output.js");
			fw.write(node.toSource());
			fw.flush();
			fw.close();
		}catch(IOException ee) {
			System.out.println(ee.toString());
		}
	}
}