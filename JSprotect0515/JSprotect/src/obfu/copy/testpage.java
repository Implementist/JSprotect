package obfu.copy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Random;

import org.mozilla.javascript.Node;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.ArrayLiteral;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.ConditionalExpression;
import org.mozilla.javascript.ast.ContinueStatement;
import org.mozilla.javascript.ast.ElementGet;
import org.mozilla.javascript.ast.ForInLoop;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.KeywordLiteral;
import org.mozilla.javascript.ast.Loop;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.NewExpression;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.NumberLiteral;
import org.mozilla.javascript.ast.ObjectLiteral;
import org.mozilla.javascript.ast.ObjectProperty;
import org.mozilla.javascript.ast.ParenthesizedExpression;
import org.mozilla.javascript.ast.PropertyGet;
import org.mozilla.javascript.ast.RegExpLiteral;
import org.mozilla.javascript.ast.StringLiteral;
import org.mozilla.javascript.ast.SwitchCase;
import org.mozilla.javascript.ast.UnaryExpression;
import org.mozilla.javascript.ast.VariableDeclaration;
import org.mozilla.javascript.ast.VariableInitializer;
import org.mozilla.javascript.ast.IfStatement;
import org.mozilla.javascript.ast.ForLoop;
import org.mozilla.javascript.ast.ReturnStatement;
import org.mozilla.javascript.ast.WhileLoop;
import org.mozilla.javascript.ast.SwitchStatement;


//函数形式声明对象会出错

public class testpage {
	private Set<String>FunName=new HashSet<String>();//记录函数名
	private Set<String>ArguName=new HashSet<String>();
	private Set<AstNode>Params=new HashSet<AstNode>();
	//记录所有的全局变量
	private ArrayList<AstNode> FlagList=new ArrayList<AstNode>();
	private ArrayList<AstNode>VarNode=new ArrayList<AstNode>();
	private DataTrees scopeData=new DataTrees(null);
	//记录所有声明变量的结点（变量名结点）
	private Map<AstNode,DataTrees> VarToScope=new HashMap<AstNode,DataTrees>();
	//记录所有赋值变量的结点（变量名）
	private Map<AstNode,DataTrees> AssToScope=new HashMap<AstNode,DataTrees>();
	private Map<AstNode,DataTrees> AssRToScope=new HashMap<AstNode,DataTrees>();
	//记录所有初始化的右值
	private Map<AstNode,DataTrees> VarRToScope=new HashMap<AstNode,DataTrees>();
	//记录所有的父节点不为variableInitializer和Assignment的结点.
	private Map<AstNode,DataTrees> NameToScope=new HashMap<AstNode,DataTrees>();
	private Stack<Integer> scopeNum=new Stack<Integer>();
	private Set<String>NameSet=new HashSet<String>();//记录所有的变量名;
	private ArrayList<String>NamesInFlattern=new ArrayList<String>();
	private ArrayList<String>   ones;
	private ArrayList<String>   twos;
	private ArrayList<String>   threes;
	private ArrayList<String> Names=new ArrayList<String>();
	private int total=203000;
	private Random random;
	testpage(){
		this.initVariablesPool();
		//for(int i=0;i<15;i++)
		//	System.out.printf(" "+Names.get(i));
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
        ones.remove("a");
        ones.remove("b");
        ones.remove("c");
        ones.remove("d");
        ones.remove("m");
        ones.remove("n");
        twos.remove("as");
        twos.remove("is");
        twos.remove("do");
        twos.remove("if");
        twos.remove("in");
        twos.remove("of");
        twos.remove("a1");
        twos.remove("a2");
        twos.remove("a3");
        twos.remove("a0");
        threes.remove("for");
        threes.remove("int");
        threes.remove("new");
        threes.remove("try");
        threes.remove("use");
        threes.remove("var");
        threes.remove("cos");
        threes.remove("sin");
        for(int i=0;i<ones.size();i++)
        	Names.add(ones.get(i));
        for(int i=0;i<twos.size();i++)
        	Names.add(twos.get(i));
        for(int i=0;i<threes.size();i++)
        	Names.add(threes.get(i));
    }
	
	 public String getWordFromThePool(DataTrees scopeData) {
	        String word = this.Names.get(scopeData.getNameNum());
	        scopeData.SetNameNum();
	        return word;
	    }
	 
	public  String getValidWord(DataTrees scopeData,Set Names,AstNode node) {
		String word = this.getWordFromThePool(scopeData);
		while (Names.contains(word)) {
			word = this.getWordFromThePool(scopeData);
		}
		return word;
    }
	
	class InsertFlag implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof FunctionNode){
				List<AstNode> Argu=((FunctionNode) node).getParams();
				for(int i=0;i<Argu.size();i++){
					ArguName.add(Argu.get(i).toSource());
				}
				AstNode Name=((FunctionNode) node).getFunctionName();
				if(Name!=null)FunName.add(Name.toSource());
				StringLiteral start=new StringLiteral();
				start.setValue("start flag");
				start.setQuoteCharacter('"');
				FlagList.add(start);
				AstNode parent=((FunctionNode) node).getBody();
				start.setParent(parent);
				start.setRelative(parent.getPosition());
				parent.addChildrenToFront(start);
				StringLiteral end=new StringLiteral();
				end.setValue("end flag");
				end.setQuoteCharacter('"');
				end.setParent(parent);
				end.setRelative(parent.getPosition());
				FlagList.add(end);
				parent.addChildrenToBack(end);
			}else if(node instanceof VariableInitializer){
				//if(((VariableInitializer) node).getInitializer() instanceof ObjectLiteral)
					//System.out.println(node.toSource());
			}else if(node instanceof Name){
				NameSet.add(node.toSource());
			}
			return true;
		}
	}
	
	
	
	class BuildFirstTree implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof StringLiteral &&((StringLiteral)node).getValue().equals("start flag")){
				DataTrees ChildScope=new DataTrees(scopeData);
				scopeData.addChild(ChildScope);
				scopeData=ChildScope;
				AstNode parent=node.getParent().getParent();
				//System.out.println(node.getParent().getClass());
				if(parent instanceof FunctionNode){
					List<AstNode>Params=((FunctionNode) parent).getParams();
					for(int j=0;j<Params.size();j++){
						scopeData.addParams(Params.get(j).toSource());
					}
					//AstNode FunName=((FunctionNode) parent).getFunctionName();
					//if(FunName!=null)scopeData.addParams(((FunctionNode) parent).getFunctionName().toSource());
				}
			}else
			if(node instanceof StringLiteral&&((StringLiteral) node).getValue().equals("end flag")){
				scopeData=scopeData.getFather();
			}else if(node instanceof FunctionNode){
				AstNode FuName=((FunctionNode) node).getFunctionName();
				List<AstNode> Params=((FunctionNode) node).getParams();
				if(FuName!=null)
					scopeData.addFuName(FuName.toSource());
			}else if(node instanceof Name){
				scopeData.AddOtherNames(node.toSource());
				if(node.getParent() instanceof VariableInitializer){
					//System.out.println(node.getParent().toSource());
					AstNode tmp=node.getParent();
					AstNode tmpP=tmp.getParent();
					//System.out.println(tmpP.toSource()+" "+tmpP.getClass());
					if(((VariableInitializer)tmp).getInitializer()!=node){//&&((VariableDeclaration)tmpP).isStatement()
							//if(!node.toSource().equals("$"))
								scopeData.addData(node.toSource());
					}
				}else if(node.getParent() instanceof ElementGet){
					if(((ElementGet)node.getParent()).getTarget().toSource().equals("window")&&!node.toSource().equals("window"))
						VarNode.add(node);
				}
			}
			return true;
		}
	}
	
	
	private void ArrageFirstTree(DataTrees scopeData){
		ArrayList<DataTrees> Children=scopeData.getChildren();
		if(Children!=null){
			for(int i=0;i<Children.size();i++){
				ArrageFirstTree(Children.get(i));
				DataTrees Father=Children.get(i).getFather();
				Iterator it=Children.get(i).GetSumNames().iterator();
				while(it.hasNext()){
					Father.AddOtherNames((String)it.next());
				}
			}
		}
		
	}
	
	
	private void DealFirstTree(DataTrees scopeData){
		if(scopeData.getFather()!=null){
			ArrayList<String> NewNames=new ArrayList<String>();
			Iterator it=scopeData.GetSumNames().iterator();
			while(it.hasNext()){
				String name=(String)it.next();
				//System.out.println(name);
				ArrayList<AstNode> names=scopeData.getNames(name);
				if(names!=null){
					for(int j=0;j<names.size();j++)
						NewNames.add(names.get(j).toSource());
				}
			}
			for(int i=0;i<NewNames.size();i++)
				scopeData.AddOtherNames(NewNames.get(i));
		}
		Iterator it=scopeData.GetVarKeySet().iterator();
		while(it.hasNext()){
			String Str=(String)it.next();
			ArrayList<AstNode>Names=null;
				if(Str.equals("$")){
					
					System.out.println(Str+"adadada");
					Name SpeName=new Name();
					SpeName.setIdentifier("$");
					ArrayList<AstNode> Namess=new ArrayList<AstNode>();
					Namess.add(SpeName);
					Names=Namess;
				}else{
					if(FunName.contains(Str)){
						Name tmp=new Name();
						tmp.setIdentifier(Str);
						ArrayList<AstNode>tmps=new ArrayList<AstNode>();
						tmps.add(tmp);
						Names=tmps;
					}else
						Names=GetThreeName(scopeData,scopeData.GetSumNames(),1);
				}
			scopeData.addThreeNames(Str, Names);
			for(int i=0;i<Names.size();i++){
				scopeData.AddOtherNames(Names.get(i).toSource());
			}
		}
		ArrayList<DataTrees> Children=scopeData.getChildren();
		for(int i=0;i<Children.size();i++)
			DealFirstTree(Children.get(i));
	}
	
	private void DealVarName(){
		for(int i=0;i<VarNode.size();i++){
			ArrayList<AstNode> names=GetThreeName(scopeData,scopeData.GetSumNames(),1);
			scopeData.addThreeNames(VarNode.get(i).toSource(), names);
		}
	}
	
	
	class BuildTree implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof StringLiteral&&((StringLiteral) node).getValue().equals("start flag")){
				DataTrees ChildScope=new DataTrees(scopeData);
				scopeData.addChild(ChildScope);
				scopeData=ChildScope;
			}
			if(node instanceof StringLiteral&&((StringLiteral) node).getValue().equals("end flag")){
				scopeData=scopeData.getFather();
			}
			if(node instanceof Name){
				if(node.getParent() instanceof VariableInitializer){
					AstNode tmp=node.getParent();
					if(((VariableInitializer)tmp).getInitializer()!=node){
						//ArrayList<AstNode>tmpNames=GetThreeName(3);
						//scopeData.addData(node.toSource(), tmpNames);
					}
				}
			}
			
			return true;
		}
	}
	
	class MultiVar implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof StringLiteral&&((StringLiteral) node).getValue().equals("start flag")){
				int oldScopeNum=scopeNum.pop();
				scopeNum.push(oldScopeNum+1);
				scopeNum.push(0);
				scopeData=scopeData.getChild(oldScopeNum);
			}
			if(node instanceof StringLiteral&&((StringLiteral) node).getValue().equals("end flag")){
				scopeNum.pop();
				scopeData=scopeData.getFather();
			}
			if(node instanceof Name){
				if(node.getParent() instanceof VariableInitializer){
					AstNode tmp=node.getParent();
					if(((VariableInitializer)tmp).getInitializer()!=node){
						//System.out.println(node.toSource()+" "+node.getParent().toSource());
						VarToScope.put(node, (DataTrees)scopeData.clone());
					}else{
						//System.out.println(node.toSource()+" "+node.getParent().toSource());
						VarRToScope.put(node, (DataTrees)scopeData.clone());
					}
				}else if(node.getParent() instanceof Assignment){
					if(((Assignment)node.getParent()).getRight()==node)
						AssRToScope.put(node,(DataTrees)scopeData.clone());
					else
						AssToScope.put(node,(DataTrees)scopeData.clone());
				}else {
					//System.out.println(node.getParent().getClass());
					NameToScope.put(node,(DataTrees)scopeData.clone());
				}
			}
			return true;
		}
	}
	
	private void DealVarRToScope(){
		Iterator it=VarRToScope.keySet().iterator();
		while(it.hasNext()){
			AstNode Var=(AstNode)it.next();
			AstNode parent=Var.getParent();
			DataTrees DT=VarRToScope.get(Var);
			Name RName=new Name();
			AstNode tmpName=DT.getRandomName(Var.toSource());
			if(tmpName!=null){
				RName.setIdentifier(tmpName.toSource());
				((VariableInitializer)parent).setInitializer(RName);
				RName.setParent(parent);
				RName.setRelative(parent.getPosition());
			}
		}
	}
	
	private void DealAssRToScope(){
		Iterator it=AssRToScope.keySet().iterator();
		while(it.hasNext()){
			AstNode Name=(AstNode)it.next();
			AstNode tmpName=AssRToScope.get(Name).getRandomName(Name.toSource());
			if(tmpName!=null)((Name)Name).setIdentifier(tmpName.toSource());
		}
	}
	
	private void DealAssToScope(){
		Iterator it=AssToScope.keySet().iterator();
		while(it.hasNext()){
			AstNode Assign=(AstNode)it.next();
			DataTrees Dt=AssToScope.get(Assign);
			ArrayList<AstNode> Names=Dt.getNames(Assign.toSource());
			if(Names!=null){
				if(Names.size()>1)System.out.println("11errro!!!");
				((Name)Assign).setIdentifier(Names.get(0).toSource());
			}
		}
	}
	
	
	private void DealAssToScope2(){
		Iterator it=AssToScope.keySet().iterator();
		while(it.hasNext()){
			AstNode Assign=(AstNode)it.next();
			//System.out.println(Assign.toSource());
			DataTrees DT=AssToScope.get(Assign);
			ArrayList<AstNode> Names=DT.getNames(Assign.toSource());
			AstNode parent=Assign.getParent();
			//System.out.println(Assign.toSource()+" "+parent.toSource());
			AstNode Suparent=parent.getParent();
			AstNode Left=((Assignment)parent).getLeft();
			AstNode Right=((Assignment)parent).getRight();
			if(Names!=null&&Left.toSource().equals(Assign.toSource())){
				((Assignment)parent).setLeft(Names.get(0));
				Names.get(0).setParent(parent);
				Names.get(0).setRelative(parent.getPosition());
				for(int i=1;i<Names.size();i++){
					Assignment NewRight=(Assignment)parent.clone();
					((Assignment)parent).setLeftAndRight(Names.get(i), NewRight);
					NewRight.setParent(parent);
					NewRight.setRelative(parent.getPosition());
				}	
			}else if(Names!=null&&Right.toSource().equals(Assign.toSource())){
				((Name)Right).setIdentifier(DT.getRandomName(Right.toSource()).toSource());
			}
			//System.out.println(Assign.toSource());
		}
	}
	
	public Set getParams(){
		return Params;
	}
	
	private void DealVarToScope2(){
		Iterator it=VarToScope.keySet().iterator();
		while(it.hasNext()){
			AstNode Name=(AstNode)it.next();
			//System.out.println(Name.toSource());
			DataTrees Dt=VarToScope.get(Name);
			VariableInitializer parent=(VariableInitializer)Name.getParent();
			VariableDeclaration Suparent=(VariableDeclaration)parent.getParent();
		//	System.out.println(Suparent.toSource());
			//if(Suparent.isStatement()){
				ArrayList<AstNode> Names=Dt.getNames(Name.toSource());
				//if(Names==null)
				//System.out.println(parent.toSource());
				//System.out.println(Name.toSource());
				if(Names!=null){
				if(Names.size()!=1){
					System.out.println(Names.size()+"error!!!");
				}
				parent.setTarget(Names.get(0));
				}
			//}
		}
	}
	
	
	private ArrayList<AstNode> SuP=new ArrayList<AstNode>();
	private void DealVarToScope3(){
		Iterator it=VarToScope.keySet().iterator();
		while(it.hasNext()){
			AstNode Name=(AstNode)it.next();
			//System.out.println(Name.toSource());
			DataTrees DT=VarToScope.get(Name);
			VariableInitializer parent=(VariableInitializer)Name.getParent();
			VariableDeclaration Suparent=(VariableDeclaration)parent.getParent();
			boolean IsStatement=Suparent.isStatement();
			if(IsStatement){//判断是否是独立语句
				//System.out.println(Suparent.toSource());
				List<VariableInitializer> InitList=Suparent.getVariables();
				ArrayList<AstNode> Names=DT.getNames(Name.toSource());
				List<VariableInitializer> NewInitList=new ArrayList<VariableInitializer>();
				List<VariableInitializer> NewInitList2=new ArrayList<VariableInitializer>();
				ArrayList<AstNode> NewExprList=new ArrayList<AstNode>();
				for(int i=0;i<InitList.size();i++){
					int flag=0;
					if(InitList.get(i).getInitializer()==null&&InitList.get(i).getTarget().toSource().equals(Name.toSource())){
						InitList.get(i).setTarget(Names.get(0));
						Names.get(0).setParent(InitList.get(i));
						Names.get(0).setRelative(InitList.get(i).getPosition());
						for(int j=1;j<Names.size();j++){
							VariableInitializer tmpInit=new VariableInitializer();
							tmpInit.setTarget(Names.get(j));
							Names.get(j).setParent(tmpInit);
							Names.get(j).setRelative(tmpInit.getPosition());
							NewInitList.add((VariableInitializer)tmpInit.clone());
						}
						for(int j=0;j<NewInitList.size();j++)
							InitList.add(NewInitList.get(j));
					}else if(InitList.get(i).getInitializer()!=null&&InitList.get(i).getTarget().toSource().equals(Name.toSource())){
						AstNode Initc=(AstNode)InitList.get(i).getInitializer().clone();
						if(Initc instanceof StringLiteral||Initc instanceof NumberLiteral||Initc instanceof RegExpLiteral||Initc instanceof KeywordLiteral||Initc instanceof Name)
							flag=1;
						Assignment tmpAss=null;
						//System.out.println(Name.toSource());
						if(Names==null)System.out.println("ook");
						for(int j=0;j<Names.size();j++){
							VariableInitializer tmpInit=new VariableInitializer();
							//if(Names==null)System.out.println(Suparent.toSource());
							tmpInit.setTarget((AstNode)Names.get(j).clone());
							NewInitList2.add(tmpInit);
							tmpAss=new Assignment();
							AstNode tmpc=(AstNode)Names.get(j).clone();
							tmpAss.setLeftAndRight(tmpc,Initc);
							tmpAss.setOperator(Token.ASSIGN);
							//System.out.println(tmpAss.toSource());
							if(j==0&&flag==1)Params.add(tmpAss);
							tmpc.setParent(tmpAss);
							tmpc.setRelative(tmpAss.getPosition());
							Initc.setParent(tmpAss);
							Initc.setRelative(tmpAss.getPosition());
							Initc=(AstNode)tmpAss;
						}
						ExpressionStatement Expr=new ExpressionStatement();
						Expr.setExpression(tmpAss);
						NewExprList.add(Expr);
						VariableDeclaration NewVar=new VariableDeclaration();
						NewVar.setVariables(NewInitList2);
						NewVar.setIsStatement(true);
						AstNode Parent=Suparent.getParent();
						//if(Parent instanceof AstRoot)System.out.println(Suparent.toSource());
						if(Parent==null)System.out.println("::"+Suparent.toSource());
						//System.out.println(Suparent.toSource());
						if(!(Parent instanceof SwitchCase)){
							//if(Parent==null)System.out.println("::"+Suparent.toSource());
							Parent.addChildBefore(NewVar, Suparent);
							NewVar.setParent(Parent);
							NewVar.setRelative(Parent.getPosition());
							for(int k=0;k<NewExprList.size();k++){
								Parent.addChildBefore(NewExprList.get(k), Suparent);
								NewExprList.get(k).setParent(Parent);
								NewExprList.get(k).setRelative(Parent.getPosition());
							}
							Parent.removeChild(Suparent);
						}else{
							List<AstNode> Statements=((SwitchCase)Parent).getStatements();
							int j=0;
							for(j=0;j<Statements.size();j++){
								if(Statements.get(j).toSource().equals(Suparent.toSource())){
									break;
								}
							}//插入顺序有问题 
							for(int k=NewExprList.size()-1;k>=0;k--){
								Statements.add(j,NewExprList.get(k));
								NewExprList.get(k).setParent(Parent);
								NewExprList.get(k).setRelative(Parent.getPosition());
							}
							Statements.add(j, NewVar);
							NewVar.setParent(Parent);
							NewVar.setRelative(Parent.getPosition());
	
	
						}
											//if(Parent==null)System.out.println(Suparent.toSource());
						//else System.out.println(Parent.toSource()+" "+Parent.getClass());
						//Parent.addChildAfter(NewVar, Suparent);
					}
				}
			}
		}
	}
	
	private void DealVarToScope(){
		Iterator it=VarToScope.keySet().iterator();
		while(it.hasNext()){
			AstNode Name=(AstNode)it.next();
			DataTrees DT=VarToScope.get(Name);
			VariableInitializer parent=(VariableInitializer)Name.getParent();
				VariableDeclaration Suparent=(VariableDeclaration)parent.getParent();
				//System.out.println(Suparent.toSource());
				List<VariableInitializer> InitList=Suparent.getVariables();
				ArrayList<AstNode> Names=DT.getNames(Name.toSource());
				List<VariableInitializer> NewInitList=new ArrayList<VariableInitializer>();
				for(int i=0;i<InitList.size();i++){
					if(InitList.get(i).getInitializer()==null&&InitList.get(i).getTarget().toSource().equals(Name.toSource())){
						InitList.get(i).setTarget(Names.get(0));
						Names.get(0).setParent(InitList.get(i));
						Names.get(0).setRelative(InitList.get(i).getPosition());
						for(int j=1;j<3;j++){
							VariableInitializer tmpInit=new VariableInitializer();
							tmpInit.setTarget(Names.get(j));
							Names.get(j).setParent(tmpInit);
							Names.get(j).setRelative(tmpInit.getPosition());
							NewInitList.add((VariableInitializer)tmpInit.clone());
						}
					}else if(InitList.get(i).getInitializer()!=null&&InitList.get(i).getTarget().toSource().equals(Name.toSource())){
						VariableInitializer tmp=InitList.get(i);
						tmp.setTarget(Names.get(0));
						Names.get(0).setParent(tmp);
						Names.get(0).setRelative(tmp.getPosition());
						for(int j=1;j<3;j++){
							VariableInitializer tmpVar=new VariableInitializer();
							tmpVar.setInitializer(tmp);
							tmp.setParent(tmpVar);
							tmp.setRelative(tmpVar.getPosition());
							tmpVar.setTarget(Names.get(j));
							Names.get(j).setParent(tmpVar);
							Names.get(j).setRelative(tmpVar.getPosition());
							tmp=tmpVar;
						}
						InitList.set(i, tmp);
						AstNode Init=tmp.getInitializer();
						if(Init.toSource().equals(Name.toSource())){
							//System.out.println("ok");
							((Name)Init).setIdentifier(DT.getRandomName(Name.toSource()).toSource());
						}
					}
				}
				for(int j=0;j<NewInitList.size();j++)
					InitList.add(NewInitList.get(j));
				
			}
	}
	
	private void DealNameToScope(){
		Iterator it=NameToScope.keySet().iterator();
		while(it.hasNext()){
			AstNode Name=(AstNode)it.next();
			//System.out.println(Name.toSource());
			//System.out.println(Name.toSource()+"  "+Name.getParent().toSource());
			if(!Name.toSource().equals("$")){
			DataTrees DT=NameToScope.get(Name);
			AstNode parent=Name.getParent();
			if(parent instanceof FunctionNode){
				List<AstNode> Params=((FunctionNode) parent).getParams();
				if(Params.contains(Name))
					continue;
				//System.out.println("baozha");
			}else if(parent instanceof FunctionCall){
				AstNode Tar=((FunctionCall) parent).getTarget();
				//if(Tar instanceof Name&&Tar.toSource().equals(Name.toSource()))
					//continue;
			}
			if(parent instanceof ObjectProperty&&Name==((ObjectProperty) parent).getLeft()){
				//System.out.println("ok");
			}else{
				AstNode tmpName=DT.getRandomName(Name.toSource());
				if(tmpName!=null)
				//System.out.println(Name.toSource()+" "+tmpName.toSource());
				if(tmpName!=null){
					((Name)Name).setIdentifier(tmpName.toSource());
					//System.out.println("res:"+Name.toSource()+"  "+Name.getParent().getParent().toSource());
				}
			}
			}
		}
	}
	
	private ArrayList<AstNode> GetThreeName(DataTrees scopeData,Set<String> Names,int num){
		ArrayList<AstNode> VarNameList=new ArrayList<AstNode>();
		for(int i=0;i<num;i++){
			Name Nname=new Name();
			String Sname=getValidWord(scopeData,Names,Nname);
			NameSet.add(Sname);
			Nname.setIdentifier(Sname);
			VarNameList.add(Nname);
		}
		return VarNameList;
	}
	

	
	private void DeleteFlag(){
		for(int i=0;i<FlagList.size();i++){
			AstNode parent=FlagList.get(i).getParent();
			if(parent==null)System.out.println("ok");
			else parent.removeChild(FlagList.get(i));
			FlagList.get(i).setParent(null);
		}
	}
	
	//functioncall 参数提取父节点设置有问题。
	ToElement ob =null;
	AstNode Root=null;
	public void testt(AstNode node,ArrayList<AstNode> NodeList){
		this.ob=ob;
		Root=(AstNode)node.clone();
		scopeNum.push(0);
		node.visit(new InsertFlag());
		Iterator it=ArguName.iterator();
		while(it.hasNext()){
			String name=(String)it.next();
			//System.out.println(name);
			Names.remove(name);
		}
		node.visit(new BuildFirstTree());
		ArrageFirstTree(scopeData);
		//scopeData.ShowTree(scopeData);
		DealFirstTree(scopeData);//给声明结点赋予三个新变量
		//scopeData.ShowAllName(scopeData);
		//DealVarName();
		node.visit(new MultiVar());
		DealVarRToScope();
		DealVarToScope2();
		for(int i=0;i<SuP.size();i++){
			AstNode parent=SuP.get(i).getParent();
			parent.removeChild(SuP.get(i));
		}
		DealAssToScope();
		DealAssRToScope();
		DealNameToScope();
		DeleteFlag();
		//scopeData.ShowTree(scopeData);
	}
}
//匿名函数中不适用，会出错