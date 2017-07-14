package obfu.copy;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.*;

import java.util.*;


public class flatten {
	private ArrayList<AstNode> FunctionList=new ArrayList<AstNode>();
	private ArrayList<AstNode> ScopeList=new ArrayList<AstNode>();
	private ArrayList<String> Names=new ArrayList<String>();
	private ArrayList<String>NamesInFlattern;
	private ArrayList<String>   ones;
	private ArrayList<String>   twos;
	private ArrayList<String>   threes;
	private ArrayList<String>   four;
	private int total=19982;
	private Random random;
	private int Namenum;//记录使用到第几个名字

	flatten(AstNode node){
		Namenum=0;
		this.NamesInFlattern=new ArrayList<String>();
		this.collectNames(node);//获取目前所有的变量名。
		this.initVariablesPool();
		this.random=new Random();
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
		for(int i=0;i<twos.size();i++)
			Names.add(twos.get(i)+"xc");
	}
	private void collectNames(AstNode node) {
		node.visit(new Visitor());
	}
	class Visitor implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node.getType()==Token.NAME){
				if(((Name)node).getIdentifier().length()<4)
					NamesInFlattern.add(((Name)node).getIdentifier());
			}
			return true;
		}
	}
	public String getWordFromThePool() {
		String word = this.Names.get(Namenum++);
		return word;
	}

	public  String getValidWord(AstNode node) {
		//this.NamesInFlattern.clear();
		//this.collectNames(node);
		String word = this.getWordFromThePool();
		while (this.NamesInFlattern.contains(word)) {
			word = this.getWordFromThePool();
		}
		this.NamesInFlattern.add(word);
		return word;
	}


	private int FunFlag=0;
	class TestFun implements NodeVisitor{
		public boolean visit(AstNode node){
			//System.out.println(node.getClass());
			if(node instanceof FunctionNode){
				AstNode parent =node.getParent();
				if(parent instanceof Assignment){
					if(((Assignment) parent).getLeft() instanceof PropertyGet
							||((Assignment)parent).getLeft() instanceof ElementGet)
						FunFlag=1;
				}
				/*AstNode parent =node.getParent();
				AstNode Name =((FunctionNode) node).getFunctionName();
				if(!(parent instanceof ObjectProperty)||!(parent instanceof ReturnStatement)&&!(Name==null))
					FunFlag=1;*/
			}
			return true;
		}
	}

	class getFunction implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof FunctionNode){
				//获取所有的函数结点，以函数体为单位进行平展
				AstNode Body=((FunctionNode) node).getBody();
				Body.visit(new TestFun());
				if(FunFlag==0)FunctionList.add(node);
				else FunFlag=0;
			}
			return true;
		}
	}

	class getOption implements NodeVisitor{
		public boolean visit(AstNode node){
			//获取除了等号，冒号，逗号，点的运算符
			if(node instanceof InfixExpression&&((InfixExpression) node).getOperator()!=Token.ASSIGN&&((InfixExpression) node).getOperator()!=Token.COMMA
					&&((InfixExpression)node).getOperator()!=Token.GETPROP&&((InfixExpression)node).getOperator()!=Token.COLON&&((InfixExpression)node).getOperator()!=Token.AND){
				AstNode parent=node.getParent();
				while(!(parent instanceof FunctionNode)){
					if(parent==null)break;
					parent=parent.getParent();
				}
				if(parent==FunctionNode)
					OpList.add(node);
			}
			return true;
		}
	}

	private AstNode CreateFunction(int op){
		//创建调用函数
		FunctionNode NewFun=new FunctionNode();
		Name Argu1=new Name();
		Name Argu2=new Name();
		Argu1.setIdentifier(getValidWord(Argu1));
		Argu2.setIdentifier(getValidWord(Argu2));
		NewFun.addParam(Argu1);
		NewFun.addParam(Argu2);
		InfixExpression newInfix=new InfixExpression();
		newInfix.setLeftAndRight((AstNode)Argu1.clone(), (AstNode)Argu2.clone());
		newInfix.setOperator(op);
		ReturnStatement Return=new ReturnStatement();
		Return.setReturnValue(newInfix);
		newInfix.setParent(Return);
		newInfix.setRelative(Return.getPosition());
		Block FBlock=new Block();
		FBlock.addChild(Return);
		NewFun.setBody(FBlock);
		return NewFun;
	}

	public int[] getSequence(int no){
		int[] sequence=new int[no];
		for(int i=0;i<no;i++){
			sequence[i]=i;
		}
		Random random=new Random();
		for(int i=0;i<no;i++){
			int p=random.nextInt(no);
			int tmp=sequence[i];
			sequence[i]=sequence[p];
			sequence[p]=tmp;
		}
		random=null;
		return sequence;
	}


	private void DealFunctionFlatten(){
		for(int i=0;i<FunctionList.size();i++){
			ArrayList<AstNode>NodeList=new ArrayList<AstNode>();
			AstNode Body=((FunctionNode)FunctionList.get(i)).getBody();
			for(AstNode Start=(AstNode)Body.getFirstChild();Start!=null;Start=(AstNode)Start.getNext()){
				NodeList.add(Start);
			}
			ArrayList<List<AstNode>> Statements=new ArrayList<List<AstNode>>();
			//把函数体结点进行分组，准备放到case中
			if(NodeList.size()>=stand){
				int size=NodeList.size();
				List<AstNode> Statement=new ArrayList<AstNode>();
				for(int j=0;j<NodeList.size();j++){
					Statement.add(NodeList.get(j));
					if(j!=0&&(j%eachcase==0||j==NodeList.size()-1)){
						Statements.add(Statement);
					}
					if(j!=0&&j%eachcase==0)Statement=new ArrayList<AstNode>();
				}
				int[] casesequence=getSequence(Statements.size());
				int[] NewSequ=new int[casesequence.length];
				Name ForLoop=new Name();
				ForLoop.setIdentifier(getValidWord(ForLoop));
				Name Switch=new Name();
				Switch.setIdentifier(getValidWord(Switch));
				VariableDeclaration ForVar=new VariableDeclaration();
				List<VariableInitializer> ForInits=new ArrayList<VariableInitializer>();
				VariableInitializer ForInit=new VariableInitializer();
				ForInit.setTarget(ForLoop);
				ForLoop.setParent(ForInit);
				ForLoop.setRelative(ForInit.getPosition());
				KeywordLiteral KeyWord=new KeywordLiteral();
				KeyWord.setType(Token.TRUE);
				ForInit.setInitializer(KeyWord);
				ForInits.add(ForInit);
				ForVar.setVariables(ForInits);

				VariableDeclaration SwitchVar=new VariableDeclaration();
				List<VariableInitializer> SwitchInits=new ArrayList<VariableInitializer>();
				VariableInitializer SwitchInit=new VariableInitializer();
				AstNode SwitchInitName=(AstNode)Switch.clone();
				SwitchInit.setTarget(SwitchInitName);
				SwitchInitName.setParent(SwitchInit);
				SwitchInitName.setRelative(SwitchInit.getPosition());
				NumberLiteral SwitchNum=new NumberLiteral();
				int SNum=0;
				SwitchNum.setValue(SNum+"");
				SwitchInit.setInitializer(SwitchNum);
				SwitchInits.add(SwitchInit);
				SwitchVar.setVariables(SwitchInits);
				ForLoop forLoop=new ForLoop();
				Scope scopeFor=new Scope();
				forLoop.setBody(scopeFor);
				forLoop.setIncrement(new EmptyExpression());
				forLoop.setInitializer(new EmptyExpression());
				AstNode Condition=(AstNode)ForLoop.clone();
				forLoop.setCondition(Condition);
				Condition.setParent(forLoop);
				Condition.setRelative(forLoop.getPosition());
				SwitchStatement switchStatement=new SwitchStatement();
				AstNode SwitchStatementName=(AstNode)Switch.clone();
				switchStatement.setExpression(SwitchStatementName);
				SwitchStatementName.setParent(switchStatement);
				SwitchStatementName.setRelative(switchStatement.getPosition());
				ArrayList<SwitchCase>cases=new ArrayList<SwitchCase>();
				//System.out.println(Statements.size());
				//System.out.println();
				//for(int j=0;j<casesequence.length;j++)
				//	System.out.print(" "+casesequence[j]);

				for(int j=0;j<Statements.size();j++){
					int num=casesequence[j];
					//System.out.println(num);
					SwitchCase Case=new SwitchCase();
					for(int k=0;k<Statements.get(num).size();k++){
						Case.addStatement(Statements.get(num).get(k));
						ExpressionStatement Expr=new ExpressionStatement();
						if(k==Statements.get(num).size()-1){
							Assignment CaseAss=new Assignment();
							if(num!=Statements.size()-1){
								NumberLiteral Num=new NumberLiteral();
								Num.setValue((casesequence[j]+1)+"");
								AstNode CaseAssSwitchName=(AstNode)Switch.clone();
								CaseAss.setLeftAndRight(CaseAssSwitchName, Num);
								CaseAssSwitchName.setParent(CaseAss);
								CaseAssSwitchName.setRelative(CaseAss.getPosition());
								CaseAss.setOperator(Token.ASSIGN);
								Expr.setExpression(CaseAss);
								Case.addStatement(Expr);
							}else{
								KeywordLiteral KeyW=new KeywordLiteral();
								KeyW.setType(Token.FALSE);
								AstNode CaseAssName=(AstNode) ForLoop.clone();
								CaseAss.setLeftAndRight(CaseAssName, KeyW);
								CaseAssName.setParent(CaseAss);
								CaseAssName.setRelative(CaseAss.getPosition());
								CaseAss.setOperator(Token.ASSIGN);
								Expr.setExpression(CaseAss);
								Case.addStatement(Expr);
							}
							Case.addStatement(new BreakStatement());
						}
					}
					NumberLiteral Num=new NumberLiteral();
					Num.setValue(num+"");
					Case.setExpression(Num);
					cases.add(Case);
				}
				switchStatement.setCases(cases);
				scopeFor.addChild(switchStatement);
				//System.out.println(forLoop.toSource());

				Scope FunScope=new Scope();
				SwitchVar.setIsStatement(true);
				ForVar.setIsStatement(true);
				FunScope.addChild(ForVar);
				FunScope.addChild(SwitchVar);
				FunScope.addChild(forLoop);
				ForVar.setParent(FunScope);
				ForVar.setRelative(FunScope.getPosition());
				SwitchVar.setParent(FunScope);
				SwitchVar.setRelative(FunScope.getPosition());
				forLoop.setParent(FunScope);
				forLoop.setRelative(FunScope.getPosition());
				((FunctionNode)FunctionList.get(i)).setBody(FunScope);;
			}
		}
	}

	//这部分处理函数将操作符提取的规则
	private AstNode FunctionNode=null;
	private ArrayList<AstNode>OpList=new ArrayList<AstNode>();
	private void DealFunction(){
		for(int i=0;i<FunctionList.size();i++){
			AstNode First=(AstNode)((FunctionNode)FunctionList.get(i)).getBody().getFirstChild();
			ObjectLiteral Obj=new ObjectLiteral();
			Set<Integer> Ops=new HashSet<Integer>();
			FunctionNode=FunctionList.get(i);
			//System.out.println(FunctionList.get(i).toSource());
			FunctionList.get(i).visit(new getOption());
			FunctionNode=null;
			for(int j=0;j<OpList.size();j++){
				Ops.add(((InfixExpression)OpList.get(j)).getOperator());
			}
			Iterator it=Ops.iterator();
			Map<Integer,AstNode> OpNode=new HashMap<Integer,AstNode>();
			while(it.hasNext()){
				Name objName=new Name();
				int op=(int)it.next();
				objName.setIdentifier(getValidWord(objName));
				AstNode Fun=CreateFunction(op);
				OpNode.put(op, objName);
				ObjectProperty newPro=new ObjectProperty();
				newPro.setLeftAndRight(objName,Fun);
				Obj.addElement(newPro);
			}
			Name ObjectName=new Name();
			ObjectName.setIdentifier(getValidWord(ObjectName));
			for(int j=0;j<OpList.size();j++){
				//System.out.println(OpList.get(j).toSource());
				AstNode parent=OpList.get(j).getParent();
				FunctionCall NewFun=new FunctionCall();
				ElementGet NewEle=new ElementGet();
				NewEle.setTarget((Name)ObjectName.clone());
				int op=((InfixExpression)OpList.get(j)).getOperator();
				AstNode FunName=OpNode.get(op);
				StringLiteral Ele=new StringLiteral();
				Ele.setValue(FunName.toSource());
				Ele.setQuoteCharacter('"');
				NewEle.setElement(Ele);
				NewFun.setTarget(NewEle);
				AstNode InfixLeft=(AstNode)((InfixExpression)OpList.get(j)).getLeft().clone();
				AstNode InfixRight=(AstNode)((InfixExpression)OpList.get(j)).getRight().clone();
				NewFun.addArgument(InfixLeft);
				NewFun.addArgument(InfixRight);
				InfixLeft.setParent(NewFun);
				InfixLeft.setRelative(NewFun.getPosition());
				InfixRight.setParent(NewFun);
				InfixRight.setRelative(NewFun.getPosition());
				//System.out.println(parent.toSource()+"  "+parent.getClass());
				if(parent instanceof InfixExpression){
					//System.out.println(parent.toSource());
					AstNode Left=((InfixExpression) parent).getLeft();
					//System.out.println("LL:  "+Left.toSource()+" "+OpList.get(j).toSource());
					if(Left.toSource().equals(OpList.get(j).toSource())){
						((InfixExpression) parent).setLeft(NewFun);
					}
					AstNode Right=((InfixExpression)parent).getRight();
					//System.out.println("RR:  "+Right.toSource()+" "+OpList.get(j).toSource());
					if(Right.toSource().equals(OpList.get(j).toSource())){
						((InfixExpression) parent).setRight(NewFun);
					}
					//System.out.println("PP   "+parent.toSource());
					NewFun.setParent(parent);
					NewFun.setRelative(parent.getPosition());
				}else if(parent instanceof Assignment){
					AstNode Left=((Assignment) parent).getLeft();
					if(Left.toSource().equals(OpList.get(j).toSource())){
						((Assignment) parent).setLeft(NewFun);
					}
					AstNode Right=((Assignment) parent).getRight();
					if(Left.toSource().equals(OpList.get(j).toSource())){
						((Assignment) parent).setRight(NewFun);
					}
					NewFun.setParent(parent);
					NewFun.setRelative(parent.getPosition());
				}else if(parent instanceof ElementGet){
					AstNode EleNode=((ElementGet) parent).getElement();
					if(EleNode==OpList.get(j)){
						((ElementGet) parent).setElement(NewFun);
						NewFun.setParent(parent);
						NewFun.setRelative(parent.getPosition());
					}
				}else if(parent instanceof FunctionCall){
					//System.out.println(parent.toSource());
					List<AstNode> Argus=((FunctionCall) parent).getArguments();
					for(int k=0;k<Argus.size();k++){
						if(Argus.get(k)==OpList.get(j)){
							Argus.set(k, NewFun);
							break;
						}
					}
					NewFun.setParent(parent);
					NewFun.setRelative(parent.getPosition());

				}else if(parent instanceof IfStatement){
					AstNode Condition=((IfStatement) parent).getCondition();
					if(Condition==OpList.get(j)){
						((IfStatement) parent).setCondition(NewFun);
						NewFun.setParent(parent);
						NewFun.setRelative(parent.getPosition());
					}
				}else if(parent instanceof VariableInitializer){
					AstNode Init=((VariableInitializer) parent).getInitializer();
					if(Init==OpList.get(j)){
						((VariableInitializer) parent).setInitializer(NewFun);
						NewFun.setParent(parent);
						NewFun.setRelative(parent.getPosition());
					}
				}else if(parent instanceof ConditionalExpression){
					AstNode truestatement=((ConditionalExpression) parent).getTrueExpression();
					AstNode falsestatement=((ConditionalExpression) parent).getFalseExpression();
					AstNode teststatement=((ConditionalExpression) parent).getTestExpression();
					if(truestatement==OpList.get(j)){
						((ConditionalExpression) parent).setTrueExpression(NewFun);
					}else if(falsestatement==OpList.get(j)){
						((ConditionalExpression) parent).setFalseExpression(NewFun);
					}else if(teststatement==OpList.get(j)){
						((ConditionalExpression) parent).setTestExpression(NewFun);
					}
					NewFun.setParent(parent);
					NewFun.setRelative(parent.getPosition());
				}else if(parent instanceof ReturnStatement){
					AstNode ReturnValue=((ReturnStatement) parent).getReturnValue();
					if(ReturnValue==OpList.get(j)){
						((ReturnStatement) parent).setReturnValue(NewFun);
					}
					NewFun.setParent(parent);
					NewFun.setRelative(parent.getPosition());
				}else if(parent instanceof WhileLoop){
					AstNode Condition=((WhileLoop) parent).getCondition();
					if(Condition==OpList.get(j)){
						((WhileLoop) parent).setCondition(NewFun);
					}
					NewFun.setParent(parent);
					NewFun.setRelative(parent.getPosition());
				}else if(parent instanceof ParenthesizedExpression){
					AstNode Expr=((ParenthesizedExpression) parent).getExpression();
					if(Expr==OpList.get(j)){
						((ParenthesizedExpression) parent).setExpression(NewFun);
						NewFun.setParent(parent);
						NewFun.setRelative(parent.getPosition());
					}
				}else if(parent instanceof ExpressionStatement){
					System.out.println(parent.toSource());
				}else if(parent instanceof ForLoop){

				}else{
					//System.out.println("??"+parent.toSource()+" "+parent.getClass());
				}
			}
			if(!Obj.getElements().isEmpty()){
				VariableDeclaration ObjVar=new VariableDeclaration();
				VariableInitializer ObjInit=new VariableInitializer();
				ObjInit.setTarget(ObjectName);
				ObjInit.setInitializer(Obj);
				ObjVar.addVariable(ObjInit);
				ObjVar.setIsStatement(true);
				((FunctionNode)FunctionList.get(i)).getBody().addChildBefore(ObjVar,First);
				ObjVar.setParent(((FunctionNode)FunctionList.get(i)).getBody());
				ObjVar.setRelative(((FunctionNode)FunctionList.get(i)).getBody().getPosition());
			}
			OpList.clear();
			//System.out.println(ObjVar.toSource());
		}
	}
	private int stand;
	private int eachcase;
	public void flattencontrol(AstNode node,int stand,int eachcase){
		this.stand=stand;
		this.eachcase=eachcase;
		node.visit(new getFunction());
		DealFunctionFlatten();
	}
}
