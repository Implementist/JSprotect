package obfu.copy;
import org.mozilla.javascript.ast.*;

import java.util.ArrayList;
import java.util.List;


public class VarComp {

	private ArrayList<VariableDeclaration> VarNodes=new ArrayList<VariableDeclaration>(); 
	class Visit implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof VariableDeclaration){
				VarNodes.add((VariableDeclaration) node);
			}
			return true;
		}
	}
	
	private ArrayList<VariableDeclaration> RemoveLists=new ArrayList<VariableDeclaration>();
	private void DealVarNodes(){
		ArrayList<VariableDeclaration> VarLists=new ArrayList<VariableDeclaration>();
		List<VariableInitializer> Variables=new ArrayList<VariableInitializer>();
		for(int i=0;i<VarNodes.size()-1;i++){
			if(!(VarNodes.get(i).getParent() instanceof SwitchCase)){
				if(VarNodes.get(i).getNext()==VarNodes.get(i+1)){
					VarLists.add(VarNodes.get(i));
				}else{
					if(VarLists.size()>0){
						VarLists.add(VarNodes.get(i));
						RemoveLists.addAll(VarLists);
						for(int j=0;j<VarLists.size();j++){
							List<VariableInitializer>tmps=VarLists.get(j).getVariables();
							Variables.addAll(tmps);
						}
						VariableDeclaration resVar=new VariableDeclaration();
						resVar.setVariables(Variables);
						resVar.setIsStatement(true);
						AstNode parent=VarLists.get(0).getParent();
						parent.addChildAfter(resVar, VarLists.get(0));
						resVar.setParent(parent);
						resVar.setRelative(parent.getPosition());
						VarLists.clear();
						Variables.clear();
					}
				}
			}
		}
	}
	
	
	public void ChAttr(AstNode node){
		node.visit(new Visit());
		DealVarNodes();
		for(int i=0;i<RemoveLists.size();i++){
			AstNode parent=RemoveLists.get(i).getParent();
			if(parent instanceof SwitchCase){
				
			}else{
				parent.removeChild(RemoveLists.get(i));
			}
		}
	}
}
