package obfu.copy;
import org.mozilla.javascript.ast.AstNode;

import java.util.*;

//设置作用域树，管理所有变量的作用域;


public class DataTrees implements Cloneable{
	private Set<String> FuName;// 记录函数名（函数形式的对象）
	private Set<String> SumNames;//在一个函数内所有的变量名
	private ArrayList<String> Params;
	private Set<String> OtherNames;//在一个函数内所有非声明的变量名
	private Map<String,ArrayList<AstNode>> VariablesNames;//声明变量和新名字的匹配
	private ArrayList<DataTrees> Children;//记录函数内部新的函数
	private DataTrees father;//记录父节点
	private Random random;
	private int NameNum;
	
	DataTrees(DataTrees father){
		this.NameNum=0;
		this.Params=new ArrayList<String>();
		this.FuName=new HashSet<String>();
		this.SumNames=new HashSet<String>();
		this.VariablesNames=new HashMap<String,ArrayList<AstNode>>();
		this.OtherNames=new HashSet<String>();
		this.Children=new ArrayList<DataTrees>();
		this.father=father;
		this.random=new Random();
	}
	
	public void addFuName(String FuName){
		this.FuName.add(FuName);
	}
	
	public int getNameNum(){
		return NameNum;
	}
	
	public void SetNameNum(){
		NameNum++;
	}
	public void addParams(String name){
		Params.add(name);
	}
	
	
	public void AddOtherNames(String name){
		//System.out.println(name);
		//if(SumNames.contains(name))System.out.println(name);
		 SumNames.add(name);
	}
	
	
	public DataTrees getFather(){
		if(this.father==null)
			return null;
		return this.father;
	}
	
	public ArrayList<DataTrees> getChildren(){
		return this.Children;
	}
	
	public DataTrees getChild(int n){
		if(Children==null)
			return null;
		return this.Children.get(n);
	}
	
	public void addChild(DataTrees child){
		this.Children.add(child);
	}
	
	public boolean CheckData(String name){
		if(this.VariablesNames.keySet().contains(name))
			return true;
		return false;
	}
	
	public void addThreeNames(String name,ArrayList<AstNode>names){//更新结点，插入新的名字
		this.VariablesNames.put(name,names);
		//for(int i=0;i<names.size();i++)
			//SumNames.add(names.get(i).toSource());
	}
	
	
	public void addData(String name){//先插入声明的结点
		this.VariablesNames.put(name, null);
	}
	
	public void showPara(){
		Iterator it=Params.iterator();
		System.out.println("(");
		while(it.hasNext()){
			System.out.print(it.next());
		}
		System.out.println(")");
	}
	
	public ArrayList<AstNode> getNames(String name){
		if(FuName.contains(name))return null;
		if(Params.contains(name))return null;
		if(CheckData(name)){
			ArrayList<AstNode> tmpList=VariablesNames.get(name);
			return tmpList;
		}
		DataTrees father=this.father;
		if(father==null)return null;
		while(true){
			while(!father.CheckData(name)){
				father=father.getFather();
				if(father==null)
					return null;
			}
			ArrayList tmpNames=father.getNames(name);
			return tmpNames;
		}
	}
	
	public AstNode getRandomName(String name){
		if(FuName.contains(name))return null;
		if(Params.contains(name))return null;
		if(CheckData(name)){
			ArrayList<AstNode> tmpList=this.VariablesNames.get(name);
			return tmpList.get(this.random.nextInt(tmpList.size()));
		}
		DataTrees father=this.father;
		if(father==null)return null;
		while(true){
			while(!father.CheckData(name)){
				father=father.getFather();
				if(father==null)
					return null;
			}
			AstNode ResName=father.getRandomName(name);
			return ResName;
		}
	}
	
	public Set GetSumNames(){
		return this.SumNames;
	}
	
	public Set GetVarKeySet(){
		return this.VariablesNames.keySet();
	}
	
	public Object clone(){
		DataTrees NewTree=null;
		try{
			NewTree=(DataTrees)super.clone();
			/*if(this.father!=null)NewTree.father=(DataTrees)this.father.clone();
			else NewTree.father=null;
			NewTree.Children=new ArrayList<DataTrees>();
			for(int i=0;i<Children.size();i++){
				DataTrees DT=(DataTrees)Children.get(i).clone();
				NewTree.Children.add(DT);
			}
			NewTree.VariablesNames=new HashMap<String,ArrayList<AstNode>>();
			Iterator it=VariablesNames.keySet().iterator();
			while(it.hasNext()){
				String str=(String)it.next();
				ArrayList<AstNode>Names=new ArrayList<AstNode>();
				for(int i=0;i<3;i++){
					Names.add((AstNode)VariablesNames.get(str).get(i).clone());
				}
				NewTree.VariablesNames.put(str, Names);
			}*/
		}catch(CloneNotSupportedException e){
			e.printStackTrace();
		}
		return NewTree;
	}
	
	public void ShowAllName(DataTrees Node){
		System.out.printf("(");
		Iterator it=Node.GetSumNames().iterator();
		while(it.hasNext()){
			System.out.printf("%s ",it.next());
		}
		ArrayList<DataTrees> tmpChildren=Node.getChildren();
		for(int i=0;i<tmpChildren.size();i++)
			ShowAllName(tmpChildren.get(i));
		System.out.printf(")  ");
	}
	
	
	
	public void ShowTree(DataTrees Node){
		System.out.printf("(");
		Iterator it=Node.GetVarKeySet().iterator();
		while(it.hasNext()){
			String tmp=(String)it.next();
			ArrayList<AstNode> tmpList=Node.getNames(tmp);
			System.out.printf("%s ",tmp);
			//System.out.printf("[");
			//for(int i=0;i<tmpList.size();i++)
			//	System.out.printf("%s ",tmpList.get(i).toSource());
			//System.out.printf("] ");
		}
		ArrayList<DataTrees> tmpChildren=Node.getChildren();
		for(int i=0;i<tmpChildren.size();i++)
			ShowTree(tmpChildren.get(i));
		System.out.printf(")  ");
	}
}
