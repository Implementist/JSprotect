package obfu.copy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.PropertyGet;
import org.mozilla.javascript.ast.VariableDeclaration;
import org.mozilla.javascript.ast.VariableInitializer;
import org.mozilla.javascript.ast.IfStatement;

class function{
	private Set<String> VarSet=new HashSet<String>();
	Graph graph=new Graph();//记录赋�?�的依赖关系;
	Graph graph2=new Graph();//记录函数调用的依赖关�?
	
	private void dealAsTree(AstNode node){
		for(AstNode it=node;it!=null;it=(AstNode)it.getNext()){
			//System.out.println(it.toSource()+it.getClass());
			switch(it.getType()){
			case Token.EXPR_RESULT:ProExpr(it);break;
			case Token.VAR:ProVarDec(it);break;
			case Token.IF:ProIf(it);break;
			}
		}
		graph.ShowGraph();
		graph2.ShowGraph();
		//Iterator ite=VarSet.iterator();
		//while(ite.hasNext()){
		//	System.out.println(((String)ite.next()));
		//}
	}
	
	
	private void ProIf(AstNode node){
		AstNode IfCondition=((IfStatement)node).getCondition();
		//System.out.println(IfCondition.toSource());
		AstNode ThenNode=(AstNode)((((IfStatement)node).getThenPart())).getFirstChild();
			dealAsTree(ThenNode);
		if(((IfStatement)node).getElsePart()!=null){
			AstNode ElseNode=(AstNode)((((IfStatement)node).getElsePart())).getFirstChild();
			dealAsTree(ElseNode);
		}
	}
	
	private void ProExpr(AstNode node){
		AstNode Expr=((ExpressionStatement)node).getExpression();
		//System.out.println(Expr.getClass());
		if(Expr instanceof Assignment)
			ProAss(Expr);
		if(Expr instanceof FunctionCall){
			ProCall(Expr);
		}
	}
	
	private void ProCall(AstNode node){
		
	}
	
	private void ProAss(AstNode node){
		AstNode LAss=((Assignment)node).getLeft();
		AstNode RAss=((Assignment)node).getRight();
		List<String> LLinkA=getLinkVar(LAss);
		List<String> RLinkA=getLinkVar(RAss);
		for(int i=0;i<LLinkA.size();i++)
			for(int j=0;j<RLinkA.size();j++){
				graph.AddEdge(LLinkA.get(i),RLinkA.get(j));
			}
		
	}
	//获取�?有声明的变量
	private void ProVarDec(AstNode node){
		List<VariableInitializer> tmpInit=new ArrayList<VariableInitializer>();
		List<String> LinkVar=new ArrayList<String>();
		tmpInit=((VariableDeclaration)node).getVariables();
		for(int i=0;i<tmpInit.size();i++){
			//创建依赖关系;
			AstNode tmpIn=tmpInit.get(i).getInitializer();
			if(tmpIn!=null)LinkVar=getLinkVar(tmpIn);
			AstNode tmp=tmpInit.get(i).getTarget();
			if(!VarSet.contains(tmp.toSource()))VarSet.add(tmp.toSource());
			Iterator it=LinkVar.iterator();
			while(it.hasNext()){
				String Stmp=(String)it.next();
				graph.AddEdge(Stmp, tmp.toSource());
			}
		}
		/*Iterator it=LinkVar.iterator();
		while(it.hasNext()){
			System.out.println((String)it.next());
		}*/
	}
	
	private boolean IsExist(String name,AstNode node){
		//System.out.println("exist:"+node.toSource());
		if(node instanceof PropertyGet){
			AstNode ProTarget=((PropertyGet)node).getTarget();
			return IsExist(name,ProTarget);
		}else if(node instanceof FunctionCall){
			List<AstNode> ArgList=new ArrayList<AstNode>();
			ArgList=((FunctionCall)node).getArguments();
			AstNode CallTarget=((FunctionCall)node).getTarget();
			boolean res=false;
			for(int i=0;i<ArgList.size();i++){
				//graph2.AddEdge(ArgList.get(i).toSource(), node.toSource());
				res=res||IsExist(name,ArgList.get(i));
			}
			return res||IsExist(name,CallTarget);
		}else if(node instanceof Name){
			if(((Name)node).getIdentifier().equals(name)){
				return true;
			}
		}
		return false;
	}
	
	private List<String>getLinkVar(AstNode node){
		List<String> tmpList=new ArrayList<String>();
		Iterator it=VarSet.iterator();
		while(it.hasNext()){
			String name=(String)it.next();
			if(IsExist(name,node)){
				tmpList.add(name);
			}
		}
		return tmpList;
	}
	
	
	public void GetVarNameMap(AstNode node){
		//System.out.println("ok");
		dealAsTree((AstNode)node.getFirstChild());
	}
}