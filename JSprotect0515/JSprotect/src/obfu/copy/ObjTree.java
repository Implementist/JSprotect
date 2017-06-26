package obfu.copy;
import java.util.ArrayList;

import org.mozilla.javascript.ast.AstNode;


public class ObjTree {
	private ArrayList<ObjTree>Children;
	private String ObjName;
	private String NewName;
	private AstNode parent;
	
	ObjTree(){
		Children=new ArrayList<ObjTree>();
		ObjName=null;
		NewName=null;
		parent=null;
	}
	
	public ArrayList<ObjTree> getChildren(){
		if(Children!=null)return Children;
		else return null;
	}
	
	public void setName(String name){
		ObjName=name;
	}
	
	public void SetNewName(String name){
		NewName=name;
	}
	
	public ObjTree getChild(int n){
		if(n>=Children.size())return null;
		else return Children.get(n-1);
	}
	
	public void addChild(ObjTree child){
		for(int i=0;i<Children.size();i++){
			if(Children.get(i).getName()==child.getName())
				return;
		}
		Children.add(child);
	}
	
	public String getName(){
		return ObjName;
	}
	
	public String getNewName(){
		return NewName;
	}
	
	
	public void SetNewAndOld(String OldName,String NewName){
		this.ObjName=OldName;
		this.NewName=NewName;
	}
	
	public void Show(ObjTree obj){
		System.out.printf(obj.ObjName+" ");
		for(int i=0;i<obj.Children.size();i++){
			Show(obj.Children.get(i));
		}
	}
}
