package obfu.copy;


import org.mozilla.javascript.ast.*;

import java.util.*;


//函数形式声明对象会出错
public class FunNameAndParams {
	private Map<String,String>  thisMap=new HashMap<String,String>();//记录所有this的变量
	private Map<String,String> ObjMethod=new HashMap<String,String>();//记录以函数形式声明的方法。
	private Set<String>FunName=new HashSet<String>();//记录函数名
	private Set<String>ArguName=new HashSet<String>();
	private Set<AstNode>Params=new HashSet<AstNode>();
	private Set<AstNode> DealedNode=new HashSet<AstNode>();//记录处理过的结点
	//记录所有的全局变量
	private ArrayList<AstNode> FlagList=new ArrayList<AstNode>();
	private ArrayList<AstNode>VarNode=new ArrayList<AstNode>();
	private DataTrees scopeData=new DataTrees(null);
	//记录所有的父节点不为variableInitializer和Assignment的结点.
	private Map<AstNode,DataTrees> NameToScope=new HashMap<AstNode,DataTrees>();
	private Stack<Integer> scopeNum=new Stack<Integer>();
	private Set<String>NameSet=new HashSet<String>();//记录所有的变量名;
	private ArrayList<String>   ones;
	private ArrayList<String>   twos;
	private ArrayList<String>   threes;
	private ArrayList<String> Names=new ArrayList<String>();
	private int total=203000;
	private Random random;
	FunNameAndParams(){
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
		twos.remove("of");
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
				if(parent instanceof FunctionNode){
					List<AstNode>Params=((FunctionNode) parent).getParams();
					for(int j=0;j<Params.size();j++){
						scopeData.addData(Params.get(j).toSource());
						DealedNode.add(Params.get(j));
					}
				}
			}else if(node instanceof StringLiteral&&((StringLiteral) node).getValue().equals("end flag")){
				scopeData=scopeData.getFather();
			}else if(node instanceof Name){
				//if(!(node.getParent() instanceof PropertyGet))
				scopeData.AddOtherNames(node.toSource());
				if(node.getParent() instanceof FunctionNode){
					if(((FunctionNode)node.getParent()).getFunctionName()==node&&!ObjName.contains(node.toSource())){
						scopeData.addData(node.toSource());
						DealedNode.add(node);
					}
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
				ArrayList<AstNode> names=scopeData.getNames_f(name);
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
			if(ResverNames.contains(Str)){
				ArrayList<AstNode> Resver=new ArrayList<AstNode>();
				Name RName=new Name();
				RName.setIdentifier(Str);
				Resver.add(RName);
				Names=Resver;
			}else{
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


	class MultiVar implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof StringLiteral&&((StringLiteral) node).getValue().equals("start flag")){
				int oldScopeNum=scopeNum.pop();
				scopeNum.push(oldScopeNum+1);
				scopeNum.push(0);
				scopeData=scopeData.getChild(oldScopeNum);
				//6.27修改  函数名，参数名
				AstNode grandPa=node.getParent().getParent();
				if(grandPa instanceof FunctionNode){
					//AstNode FunName=((FunctionNode)grandPa).getFunctionName();
					//if(FunName!=null)
					//	NameToScope.put(FunName,(DataTrees)scopeData.clone());
					List<AstNode> Params=((FunctionNode)grandPa).getParams();
					for(int j=0;j<Params.size();j++){
						NameToScope.put(Params.get(j), (DataTrees)scopeData.clone());
					}
				}
			}else
			if(node instanceof StringLiteral&&((StringLiteral) node).getValue().equals("end flag")){
				scopeNum.pop();
				scopeData=scopeData.getFather();
			}else if(node instanceof Name){
				NameToScope.put(node,(DataTrees)scopeData.clone());
			}
			return true;
		}
	}


	class DealObjMethod implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof Name){
				if(ObjMethod.containsKey(node.toSource())){
					((Name) node).setIdentifier(ObjMethod.get(node.toSource()));
					DealedNode.add(node);
				}
				if(thisMap.containsKey(node.toSource())){
					((Name) node).setIdentifier(thisMap.get(node.toSource()));
					DealedNode.add(node);
				}
			}else if(node instanceof StringLiteral){
				if(ObjMethod.containsKey(((StringLiteral) node).getValue()))
					((StringLiteral) node).setValue(ObjMethod.get(((StringLiteral) node).getValue()));
				if(thisMap.containsKey(((StringLiteral) node).getValue()))
					((StringLiteral) node).setValue(thisMap.get(((StringLiteral) node).getValue()));
			}else if(node instanceof PropertyGet){
				AstNode Target=((PropertyGet) node).getTarget();
				if(Target.toSource().equals("this")&&!(node.getParent() instanceof FunctionCall)){

				}
			}
			return true;
		}
	}

	public Set getParams(){
		return Params;
	}

	private ArrayList<AstNode> SuP=new ArrayList<AstNode>();

	private void DealNameToScope(){
		Iterator it=NameToScope.keySet().iterator();
		while(it.hasNext()){
			AstNode Name=(AstNode)it.next();
			if(!Name.toSource().equals("$")){
				DataTrees DT=NameToScope.get(Name);
				AstNode parent=Name.getParent();
				if(parent instanceof FunctionNode){
					AstNode FunName=((FunctionNode) parent).getFunctionName();
					//System.out.println(FunName.toSource()+"  "+Name.toSource());
					if(FunName!=null&&FunName==Name){
						AstNode Funtmp=DT.getRandomName_f(FunName.toSource());
						if(Funtmp!=null){
							((Name)FunName).setIdentifier(Funtmp.toSource());
							DealedNode.add(FunName);
						}
					}else{
						List<AstNode> Params=((FunctionNode) parent).getParams();
						for(int j=0;j<Params.size();j++){
							AstNode tmp=DT.getRandomName_f(Params.get(j).toSource());
							if(tmp!=null&&Params.get(j)==Name){
								((Name)Params.get(j)).setIdentifier(tmp.toSource());
								DealedNode.add(Params.get(j));
							}
						}
					}
				}else{
					AstNode tmpName=DT.getRandomName_f(Name.toSource());
					if(tmpName!=null){
						if(parent instanceof PropertyGet){
							AstNode Property=((PropertyGet) parent).getProperty();
							AstNode Target=((PropertyGet) parent).getTarget();
							if(Property.toSource().equals(Name.toSource())&&FunName.contains(Property.toSource())){
								//ObjMethod.put(Name.toSource(), tmpName.toSource());
							}
							if(Target.toSource().equals("this")){
								//thisMap.put(Name.toSource(),tmpName.toSource());
							}
							if(Target==Name){
								((Name)Name).setIdentifier(tmpName.toSource());
								DealedNode.add(Name);
							}
						}else{
							if(!(parent instanceof ObjectProperty&&((ObjectProperty)parent).getLeft()==Name)){
								((Name)Name).setIdentifier(tmpName.toSource());
								DealedNode.add(Name);
							}
						}
					}
				}
			}
		}
	}

	public Set<AstNode> GetDealedNode(){
		return DealedNode;
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
	public ArrayList<String> getEncryptFunctionName(){
		ArrayList<String> Names=new ArrayList<String>();
		AstNode nisl4=scopeData.getRandomName_f("nisl4");
		AstNode nisl2=scopeData.getRandomName_f("nisl2");
		Names.add(nisl2.toSource());
		Names.add(nisl4.toSource());
		return Names;
	}


	//functioncall 参数提取父节点设置有问题。
	ToElement ob =null;
	AstNode Root=null;
	private Set<String> ObjName;
	private Set<String>ResverNames;
	public void testt(AstNode node,Set<String> ObjName,Set<String> ResverName){
		this.ob=ob;
		this.ObjName=ObjName;
		ResverNames=ResverName;
		Root=(AstNode)node.clone();
		scopeNum.push(0);
		node.visit(new InsertFlag());
		node.visit(new BuildFirstTree());
		ArrageFirstTree(scopeData);
		DealFirstTree(scopeData);//给声明结点赋予三个新变量
		node.visit(new MultiVar());
		DealNameToScope();
		DeleteFlag();
		node.visit(new DealObjMethod());
	}
}
//匿名函数中不适用，会出错