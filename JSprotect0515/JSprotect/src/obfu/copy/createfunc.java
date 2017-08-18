package obfu.copy;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Random;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.ArrayLiteral;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.Block;
import org.mozilla.javascript.ast.ConditionalExpression;
import org.mozilla.javascript.ast.ElementGet;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.KeywordLiteral;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.NumberLiteral;
import org.mozilla.javascript.ast.ObjectProperty;
import org.mozilla.javascript.ast.ParenthesizedExpression;
import org.mozilla.javascript.ast.RegExpLiteral;
import org.mozilla.javascript.ast.ReturnStatement;
import org.mozilla.javascript.ast.StringLiteral;
import org.mozilla.javascript.ast.SwitchCase;
import org.mozilla.javascript.ast.UnaryExpression;
import org.mozilla.javascript.ast.VariableDeclaration;
import org.mozilla.javascript.ast.VariableInitializer;


public class createfunc {
	private Random random;
	//创建数组.
	
	createfunc(){
		random=new Random();
	}
	private AstNode createArray(List<AstNode> nodes){
		ArrayLiteral NewArray=new ArrayLiteral();
		for(int i=0;i<nodes.size();i++){
			NewArray.addElement(nodes.get(i));
		}
		return NewArray;
	}
	
	
	Map<String,ArrayList<String>> MuMap=new HashMap<String,ArrayList<String>>();
	private void MutiVar(){
		for(int i=0;i<4;i++){
			VariableDeclaration ArrVar=new VariableDeclaration();
			List<VariableInitializer> ArrInit=new ArrayList<VariableInitializer>();
			ArrayList<Name> NameLists=new ArrayList<Name>();
			ArrayList<String> NameStr=new ArrayList<String>();
			for(int j=0;j<3;j++){
				Name VarName=new Name();
				VarName.setIdentifier("nisl"+i+j);
				NameLists.add(VarName);
				NameStr.add(VarName.toSource());
				VariableInitializer Init=new VariableInitializer();
				Init.setTarget((AstNode)VarName.clone());
				ArrInit.add(Init);
			}
			ArrVar.setVariables(ArrInit);
			ArrVar.setIsStatement(true);
			Assignment NewAss=new Assignment();
			Name AName=new Name();
			//AName.setIdentifier((char)('a'+i)+"");
			AName.setIdentifier("NislArgu"+i);
			NameStr.add(AName.toSource());
			MuMap.put(AName.toSource(), NameStr);
			NewAss.setLeftAndRight(NameLists.get(0),AName);
			NewAss.setOperator(Token.ASSIGN);
			NameLists.get(0).setParent(NewAss);
			NameLists.get(0).setRelative(NewAss.getPosition());
			AName.setParent(NewAss);
			AName.setRelative(NewAss.getPosition());
			AstNode Right=(AstNode)NewAss.clone();
			Assignment Ass=null;
			for(int j=0;j<2;j++){
				if(j!=0)Right=(AstNode)Ass.clone();
				Ass=new Assignment();
				Ass.setLeftAndRight(NameLists.get(j+1), Right);
				Ass.setOperator(Token.ASSIGN);
				NameLists.get(j+1).setParent(Ass);
				NameLists.get(j+1).setRelative(Ass.getPosition());
				Right.setParent(Ass);
				Right.setRelative(Ass.getPosition());
				//System.out.println(Ass.toSource());
			}
			ExpressionStatement Expr=new ExpressionStatement();
			Expr.setExpression(Ass);
			AstNode parent=First.getParent();
			parent.addChildBefore(ArrVar, First);
			ArrVar.setParent(parent);
			ArrVar.setRelative(parent.getPosition());
			parent.addChildBefore(Expr, First);
			Expr.setParent(parent);
			Expr.setRelative(parent.getPosition());
		}
	}
	
	//创建函数.
	private AstNode CreateFun(ArrayList<AstNode>nodes,List<AstNode> Argu){
		FunctionNode FuncNode=new FunctionNode();
		FuncNode.setParams(Argu);
		Block FBlock=new Block();
		for(int i=0;i<nodes.size();i++){
			FBlock.addChild(nodes.get(i));
		}
		FuncNode.setBody(FBlock);
		return FuncNode;
	}
	
	private AstNode CreateFunCall(AstNode node,AstNode TarName,List<AstNode> Argu){
		FunctionCall FunCall=new FunctionCall();
		if(TarName==null){
			ParenthesizedExpression parenNode=new ParenthesizedExpression();
			parenNode.setExpression(node);
			FunCall.setTarget(parenNode);
		}else{
			FunCall.setTarget(TarName);
		}
		FunCall.setArguments(Argu);
		ExpressionStatement Expr=new ExpressionStatement();
		Expr.setExpression(FunCall);
		return Expr;
	}
	
	public static String stringToAscii(String value)
	{
		StringBuffer sbu = new StringBuffer("\\x");
		char[] chars = value.toCharArray(); 
		for (int i = 0; i < chars.length; i++) {
			if(i != chars.length - 1)
			{	
				String Str=Integer.toHexString((int)chars[i]);	
				sbu.append(Str).append("\\x");
			}
			else {
				String Str=Integer.toHexString((int)chars[i]);
				sbu.append(Integer.toHexString((int)chars[i]));
			}
		}
		return sbu.toString();
	}
	
	class StrNum implements NodeVisitor{
		public boolean visit(AstNode node){			
			if(node instanceof NumberLiteral&&IsNum==1){
				String StrNum=((NumberLiteral)node).getValue();
				if(!StrNum.contains(".")&&!StrNum.contains("0x")&&!StrNum.contains("e")&&!StrNum.contains("E")){
					long in=Long.valueOf(StrNum).longValue();
					((NumberLiteral)node).setValue("0x"+Long.toHexString(in));
				}
			}
			return true;
		}
	}
	

	
	
	
	Map<AstNode,List<AstNode>> ParaMap=new HashMap<AstNode,List<AstNode>>();
	private AstNode First=null;
	private void InitFirst(AstNode node){
		First=node;
	}
	
	
	Set<AstNode> Params=new HashSet<AstNode>();
	Set<String> StrParams=new HashSet<String>();//记录数字
	Set<String> StrParam=new HashSet<String>();//记录字符串
	Set<Integer> KeyLiteral=new HashSet<Integer>();//记录keywordliteral;
	Map<String,ArrayList<AstNode>> NumMap=new HashMap<String,ArrayList<AstNode>>();
	Map<Integer,ArrayList<AstNode>> KeyMap=new HashMap<Integer,ArrayList<AstNode>>();
	Map<String,ArrayList<AstNode>> StrMap=new HashMap<String,ArrayList<AstNode>>();
	class getParam2 implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof StringLiteral){
				AstNode parent=node.getParent();
				if(!(parent instanceof ObjectProperty&&((ObjectProperty)parent).getLeft()==node)){
					StrParam.add(((StringLiteral) node).getValue());
					if(StrMap.containsKey(((StringLiteral) node).getValue())){
						ArrayList<AstNode>StrLists=StrMap.get(((StringLiteral)node).getValue());
						StrLists.add(node);
					}else{
						ArrayList<AstNode> StrLists=new ArrayList<AstNode>();
						StrLists.add(node);
						StrMap.put(((StringLiteral)node).getValue(), StrLists);
					}
				}
			}else if(node instanceof NumberLiteral){
				AstNode parent =node.getParent();
				if(parent instanceof ObjectProperty&&((ObjectProperty)parent).getLeft()==node){

				}else{
					//Params.add(node);
					if(NumMap.containsKey(node.toSource())){
						ArrayList<AstNode>NumLists=NumMap.get(node.toSource());
						NumLists.add(node);
					}else{
						ArrayList<AstNode> NumLists=new ArrayList<AstNode>();
						NumLists.add(node);
						NumMap.put(node.toSource(),NumLists);
					}
					StrParams.add(node.toSource());
				}
			}else if(node instanceof RegExpLiteral){
				Params.add(node);
			}else if(node instanceof VariableInitializer){
				if(((VariableInitializer)node).getInitializer()!=null&&KeyWord.contains(((VariableInitializer)node).getInitializer().toSource())){
					//System.out.println(node.toSource());
					Params.add(((VariableInitializer)node).getInitializer());
				}
			}else if(node instanceof KeywordLiteral){
				if(!node.toSource().equals("this")){
					KeyLiteral.add(node.getType());
					if(KeyMap.containsKey(node.getType())){
						ArrayList<AstNode>KeyLists=KeyMap.get(node.getType());
						KeyLists.add(node);
					}else{
						ArrayList<AstNode> KeyLists=new ArrayList<AstNode>();
						KeyLists.add(node);
						KeyMap.put(node.getType(), KeyLists);
					}
				}
			}
			return true;
		}
	}

	private ArrayList<AstNode> CreateArrays2(){
		Iterator itt=StrParams.iterator();
		while(itt.hasNext()){
			NumberLiteral ParaNum=new NumberLiteral();
			ParaNum.setValue((String)itt.next());
			Params.add(ParaNum);
		}
		Iterator strit=StrParam.iterator();
		while(strit.hasNext()){
			StringLiteral ParaStr=new StringLiteral();
			ParaStr.setValue((String)strit.next());
			ParaStr.setQuoteCharacter('"');
			Params.add(ParaStr);
		}
		Iterator keyIt=KeyLiteral.iterator();
		while(keyIt.hasNext()){
			KeywordLiteral Key=new KeywordLiteral();
			Key.setType((int)keyIt.next());
			Params.add(Key);
		}
		ArrayList<List<AstNode>> SArrays=new ArrayList<List<AstNode>>();
		int size=Params.size();
		size=(size+4)/4;
		Iterator it=Params.iterator();
		for(int i=0,j=0;i<4;i++){
			List<AstNode>Array=new ArrayList<AstNode>();
			while(it.hasNext()){
				if(j==size)break;
				AstNode Ass=(AstNode)it.next();
				AstNode elem=(AstNode)Ass.clone();
				Array.add(elem);
				List<AstNode> EleNodes=new ArrayList<AstNode>();
				ArrayList<String> NameStr=MuMap.get("NislArgu"+i);
				for(int k=0;k<NameStr.size();k++){
					Name eleName=new Name();
					eleName.setIdentifier(NameStr.get(k));
					NumberLiteral num=new NumberLiteral();
					num.setValue(""+j);
					ElementGet EleNode=new ElementGet();
					EleNode.setTarget(eleName);
					EleNode.setElement(num);
					EleNodes.add(EleNode);
				}
				ParaMap.put(Ass, EleNodes);
				j++;
			}
			SArrays.add(Array);
			j=0;
		}
		ArrayList<AstNode> Arrays=new ArrayList<AstNode>();
		for(int i=0;i<4;i++){
			ArrayLiteral arr=new ArrayLiteral();
			arr.setElements(SArrays.get(i));
			Arrays.add(arr);
		}
		return Arrays;
	}

	private void DealParams2(){
		Iterator it=ParaMap.keySet().iterator();
		while(it.hasNext()){
			AstNode Node=(AstNode)it.next();
			AstNode parent=Node.getParent();
			List<AstNode> NewNodes=ParaMap.get(Node);
			//AstNode NewNode=ParaMap.get(Node);
			if(parent==null){
				if(Node instanceof KeywordLiteral){
					ArrayList<AstNode>KeyLists=KeyMap.get(Node.getType());
					if(KeyLists!=null){
						for(int j=0;j<KeyLists.size();j++)
							LocalNode(KeyLists.get(j),KeyLists.get(j).getParent(),NewNodes);
					}
				}else{
					ArrayList<AstNode> NumLists=NumMap.get(Node.toSource());
					if(NumLists!=null){
						for(int j=0;j<NumLists.size();j++){
							LocalNode(NumLists.get(j),NumLists.get(j).getParent(),NewNodes);
						}
					}else if(StrMap.get(((StringLiteral)Node).getValue())!=null){
						ArrayList<AstNode> StrLists=StrMap.get(((StringLiteral)Node).getValue());
						for(int j=0;j<StrLists.size();j++){
							LocalNode(StrLists.get(j),StrLists.get(j).getParent(),NewNodes);
						}
					}
				}
			}else{
				LocalNode(Node,parent,NewNodes);
			}
		}
	}

	private void LocalNode(AstNode Node,AstNode parent,List<AstNode>NewNodes){
		//System.out.println("ook");
				AstNode NewNode=NewNodes.get((random.nextInt(NewNodes.size())+random.nextInt(NewNodes.size()))%NewNodes.size());
				if(parent instanceof Assignment){
					if(((Assignment) parent).getRight().toSource().equals(Node.toSource())){
						((Assignment) parent).setRight(NewNode);
						NewNode.setParent(parent);
						NewNode.setRelative(parent.getPosition());
					}else if(((Assignment) parent).getLeft().toSource().equals(Node.toSource())){
						((Assignment) parent).setLeft(NewNode);
						NewNode.setParent(parent);
						NewNode.setRelative(parent.getPosition());
					}
				}else if(parent instanceof FunctionCall){
					AstNode Target=((FunctionCall) parent).getTarget();
					List<AstNode> Statements=((FunctionCall) parent).getArguments();
					if(Target.toSource().equals(Node.toSource())){
						((FunctionCall) parent).setTarget(Node);
						Node.setParent(parent);
						Node.setRelative(parent.getPosition());
					}else{
						for(int j=0;j<Statements.size();j++){
							if(Statements.get(j)==Node){
								Statements.set(j, NewNode);
								NewNode.setParent(parent);
								NewNode.setRelative(parent.getPosition());
								break;
							}
						}
					}
				}else if(parent instanceof VariableInitializer){
					if(((VariableInitializer) parent).getInitializer()==Node){
						((VariableInitializer) parent).setInitializer(NewNode);
						NewNode.setParent(parent);
						NewNode.setRelative(parent.getPosition());
					}
				}else if(parent instanceof ElementGet){
					if(((ElementGet) parent).getTarget()==Node){
						((ElementGet) parent).setTarget(NewNode);
						NewNode.setParent(parent);
						NewNode.setRelative(parent.getPosition());
					}else if(((ElementGet) parent).getElement()==Node){
						NewNode.setParent(parent);
						((ElementGet) parent).setElement(NewNode);
						NewNode.setRelative(parent.getPosition());
					}
				}else if(parent instanceof InfixExpression){
					if(((InfixExpression) parent).getLeft()==Node){
						((InfixExpression) parent).setLeft(NewNode);
						NewNode.setParent(parent);
						NewNode.setRelative(parent.getPosition());
					}else if(((InfixExpression) parent).getRight()==Node){
						((InfixExpression) parent).setRight(NewNode);
						NewNode.setParent(parent);
						NewNode.setRelative(parent.getPosition());
					}
				}else if(parent instanceof ArrayLiteral){
					List<AstNode>Statements=((ArrayLiteral) parent).getElements();
					for(int j=0;j<Statements.size();j++){
						if(Statements.get(j)==Node){
							Statements.set(j, NewNode);
							NewNode.setParent(parent);
							NewNode.setRelative(parent.getPosition());
							break;
						}
					}
				}else if(parent instanceof ConditionalExpression){
					if(((ConditionalExpression) parent).getTrueExpression()==Node){
						((ConditionalExpression) parent).setTrueExpression(NewNode);
						NewNode.setParent(parent);
						NewNode.setRelative(parent.getPosition());
					}else if(((ConditionalExpression) parent).getFalseExpression()==Node){
						((ConditionalExpression) parent).setFalseExpression(NewNode);
						NewNode.setParent(parent);
						NewNode.setRelative(parent.getPosition());
					}
				}else if(parent instanceof ReturnStatement){
					if(((ReturnStatement) parent).getReturnValue()==Node){
						((ReturnStatement) parent).setReturnValue(NewNode);
						NewNode.setParent(parent);
						NewNode.setRelative(parent.getPosition());
					}
				}else if(parent instanceof UnaryExpression){
					if(((UnaryExpression) parent).getOperand()==Node){
						NewNode.setParent(parent);
						NewNode.setRelative(parent.getPosition());
						((UnaryExpression) parent).setOperand(NewNode);
					}
				}else if(parent instanceof SwitchCase){
					if(((SwitchCase) parent).getExpression()==Node){
						((SwitchCase) parent).setExpression(NewNode);
						NewNode.setParent(parent);
						NewNode.setRelative(parent.getPosition());
					}
				}else if(parent instanceof ParenthesizedExpression){
					AstNode Expr=((ParenthesizedExpression) parent).getExpression();
					if(Expr==Node){
						((ParenthesizedExpression) parent).setExpression(NewNode);
						NewNode.setParent(parent);
						NewNode.setRelative(parent.getPosition());
					}
				}else{
				}
			}

	
	
	private Set<String>KeyWord=null;
	private int IsNum;
	private int IsString;
	public AstNode createfunction(AstNode node,Set KeyWord,int Shell,int IIS,int IsNum,int IsString){
		this.IsNum=IsNum;
		this.IsString=IsString;
		AstNode OutFunCall=node;
		if(Shell==1){
			InitFirst((AstNode)node.getFirstChild());
			MutiVar();
			this.KeyWord=KeyWord;
			node.visit(new getParam2());
			ArrayList<AstNode> arrays=CreateArrays2();
			DealParams2();
			List<AstNode>Argu1=new ArrayList<AstNode>();
			for(int i=0;i<4;i++){
				Name arguName=new Name();
				arguName.setIdentifier("NislArgu"+i);
				Argu1.add(arguName);
			}
			ArrayList<AstNode> Body=new ArrayList<AstNode>();
			Body.add(node);
			AstNode OutFun=CreateFun(Body,Argu1);	
			OutFunCall=CreateFunCall(OutFun,null,arrays);
		}
		if(IIS==1){
		OutFunCall.visit(new StrNum());
		}
		return OutFunCall;
	}
}
