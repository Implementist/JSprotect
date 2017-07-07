package obfu.copy;

import org.mozilla.javascript.ast.*;

import java.util.*;


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
	private int total=203140;
	private Random random;
	testpage(){
		this.initVariablesPool();
        this.random=new Random();
	}
	
	
	private void initVariablesPool() {
        ones = new ArrayList<String>();
        for (char c = 'a'; c <= 'z'; c++)
            ones.add(Character.toString(c));
        for (char c = 'A'; c <= 'Z'; c++)
            ones.add(Character.toString(c));
        ones.add("_");
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
						if(!Params.get(j).toSource().contains("NislArgu"))
							scopeData.addParams(Params.get(j).toSource());
						scopeData.addData(Params.get(j).toSource());
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
					AstNode tmp=node.getParent();
					AstNode tmpP=tmp.getParent();
					if(((VariableInitializer)tmp).getInitializer()!=node){
								scopeData.addData(node.toSource());
					}
				}else if(node.getParent() instanceof ElementGet){
					if(((ElementGet)node.getParent()).getTarget().toSource().equals("window")&&!node.toSource().equals("window"))
						VarNode.add(node);
				}else if(node.getParent() instanceof FunctionNode){
					AstNode FunctionName=((FunctionNode)node.getParent()).getFunctionName();
					if(FunctionName!=null&&FunctionName==node){
						scopeData.addData(node.toSource());
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
		/*Iterator it=scopeData.GetVarKeySet().iterator();
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
		}*/
		ArrayList<String> VariableNameList=scopeData.getVariablesNamesList();
		for(int k=0;k<VariableNameList.size();k++){
			String Str=VariableNameList.get(k);
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
		System.out.println(Children.size());
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
				AstNode parent=node.getParent().getParent();
				if(parent instanceof FunctionNode){
					List<AstNode> Params=((FunctionNode) parent).getParams();
					if(Params.size()==4){
						for(int j=0;j<Params.size();j++)
							if(Params.get(j).toSource().equals("NislArgu"+j))
								((Name)Params.get(j)).setIdentifier(NislArgu.get(j).toSource());
					}
				}
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
						VarToScope.put(node, (DataTrees)scopeData.clone());
					}else{
						VarRToScope.put(node, (DataTrees)scopeData.clone());
					}
				}else if(node.getParent() instanceof Assignment){
					if(((Assignment)node.getParent()).getRight()==node)
						AssRToScope.put(node,(DataTrees)scopeData.clone());
					else
						AssToScope.put(node,(DataTrees)scopeData.clone());
				}else {
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
				for(int j=0;j<Params.size();j++){
					AstNode tmpName=DT.getRandomName(Params.get(j).toSource());
					if(tmpName!=null)((Name)Params.get(j)).setIdentifier(tmpName.toSource());
				}
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
	
	
	ArrayList<AstNode> NislArgu=new ArrayList<AstNode>();
	public void testt(AstNode node,ArrayList<AstNode> NodeList){
		this.ob=ob;
		Root=(AstNode)node.clone();
		scopeNum.push(0);
		node.visit(new InsertFlag());
		/*Iterator it=ArguName.iterator();
		while(it.hasNext()){
			String name=(String)it.next();
			//Names.remove(name);
		}*/
		node.visit(new BuildFirstTree());
		ArrageFirstTree(scopeData);
		scopeData.ShowTree(scopeData);
		DealFirstTree(scopeData);//给声明结点赋予三个新变量
		//scopeData.ShowAllName(scopeData);
		//DealVarName();
		for(int i=0;i<4;i++){
			AstNode tmp=scopeData.getChild(0).getRandomName("NislArgu"+i);
			if(tmp!=null)NislArgu.add(tmp);
		}
		node.visit(new MultiVar());
		DealVarRToScope();
		DealVarToScope2();
		DealAssToScope();
		DealAssRToScope();
		DealNameToScope();
		DeleteFlag();
		scopeData.ShowTree(scopeData);
	}
}
//匿名函数中不适用，会出错