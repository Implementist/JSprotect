package obfu.copy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.mozilla.javascript.ast.AstNode;

public class Graph {
	private Map<String,Integer> NumMap=new HashMap<String,Integer>();
	private Vector<Vertex> head =new Vector<Vertex>();
	private Set<String> NodeSet=new HashSet<String>();
	private int vertexNum;
	
	public Graph(){
		int vertexNum=0;
	}
	
	
	public void ShowGraph(){
		for(int i=0;i<vertexNum;i++){
			System.out.printf("%s:",head.get(i).node);
			for(Edge tmp=head.get(i).adjacent;tmp!=null;tmp=tmp.link)
				System.out.printf("%s ",tmp.name);
			System.out.println();
		}
	}
	
	public boolean AddEdge(String start,String end){
		if(IsExist(start)){
			Edge begin;
			int local=NumMap.get(start);
			Vertex Fhead=head.get(local);
			for(begin=Fhead.adjacent;begin.link!=null;begin=begin.link){
				if(begin.name.equals(end))return false;
			}
			Edge newEdge=new Edge();
			newEdge.name=end;
			begin.link=newEdge;
			newEdge.link=null;
		}else{
			NodeSet.add(start);
			this.vertexNum++;
			NumMap.put(start,this.vertexNum-1);
			Vertex newhead=new Vertex();
			newhead.node=start;
			Edge FirstEdge=new Edge();
			FirstEdge.name=end;
			FirstEdge.link=null;
			newhead.adjacent=FirstEdge;
			head.add(newhead);
		}
		return true;
	}
	
	
	
	private boolean IsExist(String node){
		if(NodeSet.contains(node))return true;
		else return false;
	}
}
