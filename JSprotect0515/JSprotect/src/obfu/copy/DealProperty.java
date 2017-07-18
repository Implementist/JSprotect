package obfu.copy;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.*;

import java.util.*;



public class DealProperty {
	private Map<AstNode,ArrayList<String>> ObjLinksMap;//保存一条obj传输链;
	private Map<AstNode,ArrayList<String>> ObjProMap;//保存对象和属性名;
	private Set<String> WindowSet;//保存window关键字相关的属性名
	private Set<String> thisProSet;//保存由this声明的方法和属性名
	private Set<String> thisSet;//保存值为set的结点
	private Map<String,String> thisProMap;//保存this声明方法和属性名的映射;
	private Map<String,String>ObjNameMap;//保存方法属性名新旧名字的映射
	private Set<String> FunctionNames;//保存所有的函数名;
	private Set<String> windowVar;//保存所有被window赋值的变量;
	private Set<String> JsonArguObj;//保存所有和json相关的对象名;
	private Set<AstNode> JsonArguObjAst;//保存所有和json相关的对象结点
	private Set<String> objectNames;//保存所有的属性名;
	private Set<String> ObjNode;//保存所有的保存对象类型的结点名;
	private Map<String,Set<String>> ObjMap;//保存新结点和原结点的映射
	private Set<AstNode> ObjNodeInFunctionCall;//保存所有的对象类型为参数的函数调用
	private Set<AstNode> FunctionNodeAst;//保存所有函数定义结点
	private Set<AstNode> TargetFunNodeAst;//保存所有的目标函数结点;保存函数名或者记录函数的变量
	private Set<String> TargetFunNodeStr;//保存所有目标函数结点字符串
	private ArrayList<String>NamesInFlattern;
	private ArrayList<String>   ones;
	private ArrayList<String>   twos;
	private ArrayList<String>   threes;
	private ArrayList<String> Names;
	private Set<String> ReserveKeyWord;
	private int Namenum;
	DealProperty(){
		Namenum=0;
		ObjMap=new HashMap<String,Set<String>>();
		thisSet=new HashSet<String>();
		thisProMap=new HashMap<String,String>();
		WindowSet=new HashSet<String>();
		windowVar=new HashSet<String>();
		JsonArguObj=new HashSet<String>();
		JsonArguObjAst=new HashSet<AstNode>();
		ObjNode=new HashSet<String>();
		ObjNodeInFunctionCall=new HashSet<AstNode>();
		TargetFunNodeAst=new HashSet<AstNode>();
		TargetFunNodeStr=new HashSet<String>();
		FunctionNodeAst=new HashSet<AstNode>();
		FunctionNames=new HashSet<String>();
		objectNames=new HashSet<String>();
		thisProSet=new HashSet<String>();
		NamesInFlattern=new ArrayList<String>();
		ones=new ArrayList<String>();
		twos=new ArrayList<String>();
		threes=new ArrayList<String>();
		Names=new ArrayList<String>();
		ObjNameMap=new HashMap<String,String>();
		ReserveKeyWord=new HashSet<String>();
		InitReserveKeyWord();
		initVariablesPool();
		WindowSet.add("window");
		thisSet.add("this");
	}

	private void InitReserveKeyWord(){
		ReserveKeyWord.add("constructor");
		ReserveKeyWord.add("length");
		ReserveKeyWord.add("prototype");
		ReserveKeyWord.add("concat");
		ReserveKeyWord.add("copyWithin");
		ReserveKeyWord.add("every");
		ReserveKeyWord.add("fill");
		ReserveKeyWord.add("filter");
		ReserveKeyWord.add("find");
		ReserveKeyWord.add("findIndex");
		ReserveKeyWord.add("forEach");
		ReserveKeyWord.add("indexOf");
		ReserveKeyWord.add("join");
		ReserveKeyWord.add("lastIndexOf");
		ReserveKeyWord.add("map");
		ReserveKeyWord.add("pop");
		ReserveKeyWord.add("push");
		ReserveKeyWord.add("reduce");
		ReserveKeyWord.add("reduceRight");
		ReserveKeyWord.add("reverse");
		ReserveKeyWord.add("shift");
		ReserveKeyWord.add("slice");
		ReserveKeyWord.add("some");
		ReserveKeyWord.add("sort");
		ReserveKeyWord.add("splice");
		ReserveKeyWord.add("toString");
		ReserveKeyWord.add("unshift");
		ReserveKeyWord.add("valueOf");
		ReserveKeyWord.add("getDate");
		ReserveKeyWord.add("getDay");
		ReserveKeyWord.add("getFullYear");
		ReserveKeyWord.add("getHours");
		ReserveKeyWord.add("getMilliseconds");
		ReserveKeyWord.add("getMinutes");
		ReserveKeyWord.add("getMonth");
		ReserveKeyWord.add("getSeconds");
		ReserveKeyWord.add("getTime");
		ReserveKeyWord.add("getTimezoneOffset");
		ReserveKeyWord.add("getUTCDate");
		ReserveKeyWord.add("getUTCDay");
		ReserveKeyWord.add("getUTCFullYear");
		ReserveKeyWord.add("getUTCHours");
		ReserveKeyWord.add("getUTCMilliseconds");
		ReserveKeyWord.add("getUTCMinutes");
		ReserveKeyWord.add("getUTCMonth");
		ReserveKeyWord.add("getUTCSeconds");
		ReserveKeyWord.add("parse");
		ReserveKeyWord.add("setDate");
		ReserveKeyWord.add("setFullYear");
		ReserveKeyWord.add("setHours");
		ReserveKeyWord.add("setMilliseconds");
		ReserveKeyWord.add("setMinutes");
		ReserveKeyWord.add("setSeconds");
		ReserveKeyWord.add("setTime");
		ReserveKeyWord.add("setUTCDate");
		ReserveKeyWord.add("setUTCFullYear");
		ReserveKeyWord.add("setUTCHours");
		ReserveKeyWord.add("setUTCMilliseconds");
		ReserveKeyWord.add("setUTCMinutes");
		ReserveKeyWord.add("setUTCMonth");
		ReserveKeyWord.add("setUTCSeconds");
		ReserveKeyWord.add("toDateString");
		ReserveKeyWord.add("toUTCString");
		ReserveKeyWord.add("toJSON");
		ReserveKeyWord.add("toLocaleDateString");
		ReserveKeyWord.add("toLocaleTimeString");
		ReserveKeyWord.add("toTimeString");
		ReserveKeyWord.add("toUTCString");
		ReserveKeyWord.add("UTC");
		ReserveKeyWord.add("valueOf");
		ReserveKeyWord.add("E");
		ReserveKeyWord.add("LN2");
		ReserveKeyWord.add("LN10");
		ReserveKeyWord.add("LOG2E");
		ReserveKeyWord.add("LOG10E");
		ReserveKeyWord.add("PI");
		ReserveKeyWord.add("SQRT1_2");
		ReserveKeyWord.add("SQRT2");
		ReserveKeyWord.add("abs");
		ReserveKeyWord.add("acos");
		ReserveKeyWord.add("asin");
		ReserveKeyWord.add("atan");
		ReserveKeyWord.add("atan2");
		ReserveKeyWord.add("ceil");
		ReserveKeyWord.add("cos");
		ReserveKeyWord.add("exp");
		ReserveKeyWord.add("floor");
		ReserveKeyWord.add("log");
		ReserveKeyWord.add("max");
		ReserveKeyWord.add("min");
		ReserveKeyWord.add("pow");
		ReserveKeyWord.add("random");
		ReserveKeyWord.add("round");
		ReserveKeyWord.add("sin");
		ReserveKeyWord.add("sqrt");
		ReserveKeyWord.add("tan");
		ReserveKeyWord.add("MAX_VALUE");
		ReserveKeyWord.add("MIN_VALUE");
		ReserveKeyWord.add("NEGATIVE_INFINITY");
		ReserveKeyWord.add("NaN");
		ReserveKeyWord.add("POSITIVE_INFINITY");
		ReserveKeyWord.add("toExponential");
		ReserveKeyWord.add("toFixed");
		ReserveKeyWord.add("toPrecision");
		ReserveKeyWord.add("charAt");
		ReserveKeyWord.add("charCodeAt");
		ReserveKeyWord.add("concat");
		ReserveKeyWord.add("fromCharCode");
		ReserveKeyWord.add("indexOf");
		ReserveKeyWord.add("lastIndexOf");
		ReserveKeyWord.add("match");
		ReserveKeyWord.add("replace");
		ReserveKeyWord.add("search");
		ReserveKeyWord.add("slice");
		ReserveKeyWord.add("split");
		ReserveKeyWord.add("substr");
		ReserveKeyWord.add("substring");
		ReserveKeyWord.add("toLowerCase");
		ReserveKeyWord.add("toUpperCase");
		ReserveKeyWord.add("trim");
		ReserveKeyWord.add("anchor");
		ReserveKeyWord.add("big");
		ReserveKeyWord.add("blink");
		ReserveKeyWord.add("bold");
		ReserveKeyWord.add("fixed");
		ReserveKeyWord.add("fontsize");
		ReserveKeyWord.add("fontSize");
		ReserveKeyWord.add("italics");
		ReserveKeyWord.add("link");
		ReserveKeyWord.add("small");
		ReserveKeyWord.add("strike");
		ReserveKeyWord.add("sub");
		ReserveKeyWord.add("sup");
		ReserveKeyWord.add("compile");
		ReserveKeyWord.add("exec");
		ReserveKeyWord.add("test");
		ReserveKeyWord.add("search");
		ReserveKeyWord.add("match");
		ReserveKeyWord.add("replace");
		ReserveKeyWord.add("split");
		ReserveKeyWord.add("Infinity");
		ReserveKeyWord.add("undefined");
		ReserveKeyWord.add("decodeURI");
		ReserveKeyWord.add("decodeURIComponent");
		ReserveKeyWord.add("encodeURI");
		ReserveKeyWord.add("encodeURIComponent");
		ReserveKeyWord.add("escape");
		ReserveKeyWord.add("eval");
		ReserveKeyWord.add("isFinite");
		ReserveKeyWord.add("isNaN");
		ReserveKeyWord.add("Number");
		ReserveKeyWord.add("parseFloat");
		ReserveKeyWord.add("parseInt");
		ReserveKeyWord.add("String");
		ReserveKeyWord.add("unescape");
		ReserveKeyWord.add("closed");
		ReserveKeyWord.add("document");
		ReserveKeyWord.add("frames");
		ReserveKeyWord.add("history");
		ReserveKeyWord.add("innerHeight");
		ReserveKeyWord.add("innerWidth");
		ReserveKeyWord.add("location");
		ReserveKeyWord.add("name");
		ReserveKeyWord.add("navigator");
		ReserveKeyWord.add("opener");
		ReserveKeyWord.add("outerHeight");
		ReserveKeyWord.add("outerWidth");
		ReserveKeyWord.add("pageXOffset");
		ReserveKeyWord.add("pageYOffset");
		ReserveKeyWord.add("parent");
		ReserveKeyWord.add("screen");
		ReserveKeyWord.add("screenLeft");
		ReserveKeyWord.add("screenTop");
		ReserveKeyWord.add("screenX");
		ReserveKeyWord.add("screenY");
		ReserveKeyWord.add("self");
		ReserveKeyWord.add("status");
		ReserveKeyWord.add("top");
		ReserveKeyWord.add("alert");
		ReserveKeyWord.add("blur");
		ReserveKeyWord.add("clearInterval");
		ReserveKeyWord.add("clearTimeout");
		ReserveKeyWord.add("close");
		ReserveKeyWord.add("confirm");
		ReserveKeyWord.add("createPopup");
		ReserveKeyWord.add("focus");
		ReserveKeyWord.add("moveBy");
		ReserveKeyWord.add("moveTo");
		ReserveKeyWord.add("open");
		ReserveKeyWord.add("print");
		ReserveKeyWord.add("prompt");
		ReserveKeyWord.add("resizeBy");
		ReserveKeyWord.add("resizeTo");
		ReserveKeyWord.add("scrollBy");
		ReserveKeyWord.add("scroll");
		ReserveKeyWord.add("scrollBy");
		ReserveKeyWord.add("scrollTo");
		ReserveKeyWord.add("setInterval");
		ReserveKeyWord.add("setTimeout");
		ReserveKeyWord.add("appCodeName");
		ReserveKeyWord.add("appName");
		ReserveKeyWord.add("appVersion");
		ReserveKeyWord.add("cookieEnabled");
		ReserveKeyWord.add("platform");
		ReserveKeyWord.add("javaEnabled");
		ReserveKeyWord.add("taintEnabled");
		ReserveKeyWord.add("availHeight");
		ReserveKeyWord.add("availWidth");
		ReserveKeyWord.add("colorDepth");
		ReserveKeyWord.add("height");
		ReserveKeyWord.add("pixelDepth");
		ReserveKeyWord.add("width");
		ReserveKeyWord.add("back");
		ReserveKeyWord.add("forward");
		ReserveKeyWord.add("go");
		ReserveKeyWord.add("hash");
		ReserveKeyWord.add("host");
		ReserveKeyWord.add("hostname");
		ReserveKeyWord.add("href");
		ReserveKeyWord.add("pathname");
		ReserveKeyWord.add("port");
		ReserveKeyWord.add("protocol");
		ReserveKeyWord.add("search");
		ReserveKeyWord.add("assign");
		ReserveKeyWord.add("reload");
		ReserveKeyWord.add("replace");
		ReserveKeyWord.add("activeElement");
		ReserveKeyWord.add("addEventListener");
		ReserveKeyWord.add("anchors");
		ReserveKeyWord.add("applets");
		ReserveKeyWord.add("baseURI");
		ReserveKeyWord.add("body");
		ReserveKeyWord.add("close");
		ReserveKeyWord.add("cookie");
		ReserveKeyWord.add("createAttribute");
		ReserveKeyWord.add("createComment");
		ReserveKeyWord.add("createDocumentFragment");
		ReserveKeyWord.add("createElement");
		ReserveKeyWord.add("createTextNode");
		ReserveKeyWord.add("doctype");
		ReserveKeyWord.add("documentElement");
		ReserveKeyWord.add("documentMode");
		ReserveKeyWord.add("documentURI");
		ReserveKeyWord.add("domain");
		ReserveKeyWord.add("domConfig");
		ReserveKeyWord.add("embeds");
		ReserveKeyWord.add("forms");
		ReserveKeyWord.add("getElementsByClassName");
		ReserveKeyWord.add("getElementById");
		ReserveKeyWord.add("getElementsByName");
		ReserveKeyWord.add("getElementsByTagName");
		ReserveKeyWord.add("images");
		ReserveKeyWord.add("implementation");
		ReserveKeyWord.add("importNode");
		ReserveKeyWord.add("inputEncoding");
		ReserveKeyWord.add("lastModified");
		ReserveKeyWord.add("links");
		ReserveKeyWord.add("normalize");
		ReserveKeyWord.add("normalizeDocument");
		ReserveKeyWord.add("querySelector");
		ReserveKeyWord.add("querySelectorAll");
		ReserveKeyWord.add("readyState");
		ReserveKeyWord.add("referrer");
		ReserveKeyWord.add("removeEventListener");
		ReserveKeyWord.add("renameNode");
		ReserveKeyWord.add("scripts");
		ReserveKeyWord.add("strictErrorChecking");
		ReserveKeyWord.add("title");
		ReserveKeyWord.add("URL");
		ReserveKeyWord.add("write");
		ReserveKeyWord.add("writeln");
		ReserveKeyWord.add("writeln");
		ReserveKeyWord.add("message");
		ReserveKeyWord.add("val");
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
	private void collectNames(AstNode node) {
		node.visit(new Visitor());
	}
	class Visitor implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node.getType()==Token.NAME){
				if(((Name)node).getIdentifier().length()<6)
					NamesInFlattern.add(((Name)node).getIdentifier());
			}
			return true;
		}
	}
	public String getWordFromThePool() {
		String word = this.Names.get(Namenum++);
		return word;
	}

	public  String getValidWord() {
		String word = this.getWordFromThePool();
		while (this.NamesInFlattern.contains(word)) {
			word = this.getWordFromThePool();
		}
		this.NamesInFlattern.add(word);
		return word;
	}

	private Map<AstNode,AstNode> NameFunction=new HashMap<AstNode,AstNode>();
	private Map<AstNode,AstNode> ObjFunction=new HashMap<AstNode,AstNode>();
	class CollectFunctionNode implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof VariableInitializer){
				AstNode Name=((VariableInitializer) node).getTarget();
				AstNode FunctionNode=((VariableInitializer) node).getInitializer();
				if(Name instanceof Name){
					NameFunction.put(Name, FunctionNode);
				}
			}else if(node instanceof FunctionNode){
				AstNode FunName=((FunctionNode) node).getFunctionName();
				if(FunName!=null)NameFunction.put(FunName, node);
			}
			return true;
		}
	}


	class FindWinNode implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof PropertyGet){//获取由this引导的属性和方法
				AstNode parent=node.getParent();
				while(true){
					if(!(parent instanceof PropertyGet||parent instanceof FunctionCall||parent instanceof ElementGet||parent instanceof InfixExpression))
						break;
					parent=parent.getParent();
				}
				if(parent instanceof ExpressionStatement)parent=((ExpressionStatement) parent).getExpression();
				AstNode Target=getTarget(node);
				if(Target instanceof Name&&Target.toSource().equals("window")){
					if(parent instanceof VariableInitializer){
						AstNode Tar=((VariableInitializer) parent).getTarget();
						if(Tar instanceof Name)windowVar.add(Tar.toSource());
					}
				}
			}else if(node instanceof VariableInitializer){
				AstNode Init=((VariableInitializer) node).getInitializer();
				if(Init!=null&&WindowSet.contains(Init.toSource())){
					WindowSet.add(((VariableInitializer) node).getTarget().toSource());
				}
			}else if(node instanceof Assignment){
				AstNode Right=((Assignment) node).getRight();
				AstNode Left =((Assignment) node).getLeft();
				if(WindowSet.contains(Right.toSource())){
					WindowSet.add(Left.toSource());
				}
			}else if(node instanceof FunctionCall){
				List<AstNode> Argus=((FunctionCall) node).getArguments();
				for(int i=0;i<Argus.size();i++){
					if(WindowSet.contains(Argus.get(i).toSource())){
						ObjNodeInFunctionCall.add(node);
						break;
					}
				}
			}
			return true;
		}
	}



	class FindWindowLists implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof Assignment){
				AstNode Left=((Assignment) node).getLeft();
				if(Left instanceof PropertyGet&&((PropertyGet) Left).getTarget() instanceof Name&&WindowSet.contains(((PropertyGet) Left).getTarget().toSource())){
					((Assignment) node).getRight().visit(new FindName());
				}
			}
			return true;
		}
	}



	private Map<String,Set<String>> ReturnObjFunction=new HashMap<String,Set<String>>();
	private Set<String> ObjNodes=new HashSet<String>();
	class CreateObjMap implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof VariableInitializer){
				AstNode Init=((VariableInitializer) node).getInitializer();
				if(Init!=null&&(Init instanceof ObjectLiteral||ObjNodes.contains(Init.toSource()))){
					//记录声明一个对象
					AstNode Tar=((VariableInitializer) node).getTarget();
					//if(!WindowLists.contains(Tar.toSource())){
					ObjNodes.add(Tar.toSource());
					if(Init instanceof ObjectLiteral){
						if(ObjMap.containsKey(Tar.toSource())){
							Set<String> ObjSet=ObjMap.get(Tar.toSource());
							if(ObjSet!=null){
								ObjSet.add(Tar.toSource());
							}
						}else{
							HashSet<String> ObjSet=new HashSet<String>();
							ObjSet.add(Tar.toSource());
							ObjMap.put(Tar.toSource(), ObjSet);
						}
					}else if(ObjNodes.contains(Init.toSource())){
						Set<String> ObjSet=ObjMap.get(Init.toSource());
						if(ObjSet!=null){
							if(ObjMap.containsKey(Tar.toSource())){
								Set<String> TarObjSet=ObjMap.get(Tar.toSource());
								if(ObjSet!=null){
									Iterator it=ObjSet.iterator();
									while(it.hasNext()){
										TarObjSet.add((String)it.next());
									}
								}
							}else{
								HashSet<String> TarObjSet=new HashSet<String>();
								Iterator it=ObjSet.iterator();
								while(it.hasNext()){
									TarObjSet.add((String)it.next());
								}
								ObjMap.put(Tar.toSource(), TarObjSet);
							}
						}
					}
					//}
				}
				if(Init!=null&&WindowSet.contains(Init.toSource())){
					WindowSet.add(((VariableInitializer) node).getTarget().toSource());
				}else if(Init!=null&&thisSet.contains(Init.toSource())){
					thisSet.add(((VariableInitializer) node).getTarget().toSource());
				}
			}else if(node instanceof Assignment){
				AstNode Right=((Assignment) node).getRight();
				AstNode Left =((Assignment) node).getLeft();
				if(ObjNodes.contains(Right.toSource())){
					ObjNodes.add(Left.toSource());
					if(ObjMap.containsKey(Left.toSource())){
						Set<String>LeftObjSet=ObjMap.get(Left.toSource());
						Set<String> RightObjSet=ObjMap.get(Right.toSource());
						if(RightObjSet!=null){
							Iterator it=RightObjSet.iterator();
							while(it.hasNext()){
								LeftObjSet.add((String)it.next());
							}
						}
					}else{
						Set<String>LeftObjSet=new HashSet<String>();
						Set<String> RightObjSet=ObjMap.get(Right.toSource());
						if(RightObjSet!=null){
							Iterator it=RightObjSet.iterator();
							while(it.hasNext()){
								LeftObjSet.add((String)it.next());
							}
						}
						ObjMap.put(Left.toSource(), LeftObjSet);
					}
				}
				if(WindowSet.contains(Right.toSource())){
					WindowSet.add(Left.toSource());
				}

			}if(node instanceof ReturnStatement){
				if(((ReturnStatement) node).getReturnValue() instanceof Name&&ObjNodes.contains(((ReturnStatement) node).getReturnValue().toSource())){
					AstNode parent=((ReturnStatement) node).getParent();
					while(!(parent instanceof FunctionNode)){
						parent=parent.getParent();
					}
					if(parent instanceof FunctionNode&&((FunctionNode)parent).getFunctionName()!=null){
						Set<String>ObjSet=ObjMap.get(((ReturnStatement) node).getReturnValue().toSource());
						if(ObjSet!=null){
							ReturnObjFunction.put(((FunctionNode)parent).getFunctionName().toSource(),ObjSet);
						}
					}else if(parent instanceof FunctionNode&&((FunctionNode)parent).getFunctionName()==null){
						parent=parent.getParent().getParent().getParent();
						if(parent instanceof Assignment){
							AstNode Left=((Assignment) parent).getLeft();
							Set<String>ObjSet=ObjMap.get(((ReturnStatement) node).getReturnValue().toSource());
							if(ObjSet!=null){
								Set<String> ObjSetLeft=new HashSet<String>();
								Iterator it=ObjSet.iterator();
								ObjSetLeft.add((String)it.next());
								ObjNodes.add(Left.toSource());
								ObjMap.put(Left.toSource(), ObjSetLeft);
							}
						}else if(parent instanceof VariableInitializer){
							AstNode Tar=((VariableInitializer) parent).getTarget();
							Set<String>ObjSet=ObjMap.get(((ReturnStatement) node).getReturnValue().toSource());
							if(ObjSet!=null){
								Set<String> ObjSetLeft=new HashSet<String>();
								Iterator it=ObjSet.iterator();
								ObjSetLeft.add((String)it.next());
								ObjNodes.add(Tar.toSource());
								ObjMap.put(Tar.toSource(), ObjSetLeft);
							}
						}
					}
				}else if(((ReturnStatement) node).getReturnValue() instanceof ObjectLiteral){
					AstNode parent=node.getParent();
					while(!(parent instanceof FunctionNode)){
						parent=parent.getParent();
					}
					if(parent instanceof FunctionNode&&((FunctionNode)parent).getFunctionName()!=null){
						Set<String> ObjSet=new HashSet<String>();
						ObjSet.add(((FunctionNode)parent).getFunctionName().toSource());
						ReturnObjFunction.put(((FunctionNode)parent).getFunctionName().toSource(),ObjSet);
					}else if(parent instanceof FunctionNode&&((FunctionNode)parent).getFunctionName()==null){
						parent=parent.getParent().getParent().getParent();
						if(parent instanceof Assignment){
							AstNode Left=((Assignment) parent).getLeft();
							Set<String> ObjSetLeft=new HashSet<String>();
							ObjSetLeft.add(Left.toSource());
							ObjNodes.add(Left.toSource());
							ObjMap.put(Left.toSource(), ObjSetLeft);
						}else if(parent instanceof VariableInitializer){
							AstNode Tar=((VariableInitializer) parent).getTarget();
							Set<String> ObjSetLeft=new HashSet<String>();
							ObjSetLeft.add(Tar.toSource());
							ObjNodes.add(Tar.toSource());
							ObjMap.put(Tar.toSource(), ObjSetLeft);
						}
					}
				}
			}else if(node instanceof FunctionCall){
				AstNode Tar=((FunctionCall) node).getTarget();
				if(Tar!=null&&Tar instanceof Name){
					AstNode parent=node.getParent();
					if(parent instanceof VariableInitializer){
						AstNode target=((VariableInitializer) parent).getTarget();
						Set<String>ObjSet=ReturnObjFunction.get(Tar.toSource());
						if(ObjSet!=null){
							Set<String> ObjSetLeft=new HashSet<String>();
							Iterator it=ObjSet.iterator();
							ObjSetLeft.add((String)it.next());
							ObjNodes.add(target.toSource());
							ObjMap.put(target.toSource(), ObjSetLeft);
						}
					}else if(parent instanceof Assignment){
						AstNode Left=((Assignment)parent).getLeft();
						Set<String>ObjSet=ReturnObjFunction.get(Left.toSource());
						if(ObjSet!=null){
							Set<String> ObjSetLeft=new HashSet<String>();
							Iterator it=ObjSet.iterator();
							ObjSetLeft.add((String)it.next());
							ObjNodes.add(Left.toSource());
							ObjMap.put(Left.toSource(), ObjSetLeft);
						}
					}
				}
			}
			return true;
		}
	}




	class FindObjNode implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof PropertyGet){//获取由this引导的属性和方法
				AstNode parent=node.getParent();
				while(true){
					if(!(parent instanceof PropertyGet||parent instanceof FunctionCall||parent instanceof ElementGet||parent instanceof InfixExpression))
						break;
					parent=parent.getParent();
				}
				if(parent instanceof ExpressionStatement)parent=((ExpressionStatement) parent).getExpression();
				if(((PropertyGet) node).getTarget() instanceof KeywordLiteral){
					if(parent instanceof Assignment&&((Assignment) parent).getLeft()==node)
						if(!ReserveKeyWord.contains(((PropertyGet) node).getProperty().toSource())){
							thisProSet.add(((PropertyGet) node).getProperty().toSource());
							ObjNameMap.put(((PropertyGet) node).getProperty().toSource(), getValidWord());
						}
				}
				AstNode Target=getTarget(node);
				if(Target instanceof Name&&Target.toSource().equals("window")){
					if(parent instanceof VariableInitializer){
						AstNode Tar=((VariableInitializer) parent).getTarget();
						if(Tar instanceof Name)windowVar.add(Tar.toSource());
					}
				}
			}else if(node instanceof VariableInitializer){
				AstNode Init=((VariableInitializer) node).getInitializer();
				if(Init!=null&&(Init instanceof ObjectLiteral||ObjNode.contains(Init.toSource()))){
					//记录声明一个对象
					AstNode Tar=((VariableInitializer) node).getTarget();
					if(!WindowLists.contains(Tar.toSource())&&!WinSourceObj.contains(Tar.toSource()))
						ObjNode.add(Tar.toSource());
				}
				if(Init!=null&&WindowSet.contains(Init.toSource())){
					WindowSet.add(((VariableInitializer) node).getTarget().toSource());
				}else if(Init!=null&&thisSet.contains(Init.toSource())){
					thisSet.add(((VariableInitializer) node).getTarget().toSource());
				}
			}else if(node instanceof Assignment){
				AstNode Right=((Assignment) node).getRight();
				AstNode Left =((Assignment) node).getLeft();
				if(ObjNode.contains(Right.toSource())&&!WindowLists.contains(Left.toSource())&&!WinSourceObj.contains(Left.toSource())){
					ObjNode.add(Left.toSource());
				}
				if(WindowSet.contains(Right.toSource())){
					WindowSet.add(Left.toSource());
				}
				if(Left instanceof PropertyGet){
					if(((PropertyGet) Left).getTarget() instanceof Name&&ObjNode.contains(((PropertyGet) Left).getTarget().toSource())&&!WinSourceObj.contains(((PropertyGet) Left).getTarget().toSource())){
						ObjNameMap.put(((PropertyGet) Left).getProperty().toSource(), getValidWord());
					}
				}
			}else if(node instanceof FunctionCall){
				List<AstNode> Argus=((FunctionCall) node).getArguments();
				for(int i=0;i<Argus.size();i++){
					if(ObjNode.contains(Argus.get(i).toSource())){
						ObjNodeInFunctionCall.add(node);
						break;
					}else if(Argus.get(i) instanceof ObjectLiteral){
						ObjNodeInFunctionCall.add(node);
						break;
					}else if(WindowSet.contains(Argus.get(i).toSource())){
						ObjNodeInFunctionCall.add(node);
						break;
					}
				}
				if(((FunctionCall) node).getTarget() instanceof PropertyGet){
					AstNode Nnode=((FunctionCall) node).getTarget();
					AstNode Target=getTarget(((FunctionCall)node).getTarget());
					AstNode Property=((PropertyGet)Nnode).getProperty();
					if(Target instanceof Name&&Target.toSource().equals("JSON")){
						for(int i=0;i<Argus.size();i++){
							//System.out.println(Argus.get(i).toSource());
							//JsonArguObj.add(Argus.get(i).toSource());
							//ObjNode.remove(Argus.get(i).toSource());
						}
					}
				}
				if(node instanceof NewExpression){
					List<AstNode>NewArgus=((NewExpression) node).getArguments();
					for(int i=0;NewArgus!=null&&i<NewArgus.size();i++)
						JsonArguObj.add(NewArgus.get(i).toSource());
				}
				if(node instanceof FunctionCall){
					AstNode Tar=((FunctionCall) node).getTarget();
					if(!FunctionNames.contains(Tar.toSource())){
						//node.visit(new getArguLists());
					}
				}
			}else if(node instanceof FunctionNode){
				FunctionNodeAst.add(node);
				AstNode FunctionName=((FunctionNode) node).getFunctionName();
				if(FunctionName!=null)FunctionNames.add(FunctionName.toSource());
			}else if(node instanceof ObjectProperty){
				AstNode LeftName=((ObjectProperty) node).getLeft();
				if(LeftName instanceof Name){
					objectNames.add(LeftName.toSource());
					if(!ReserveKeyWord.contains(LeftName.toSource()))
						ObjNameMap.put(LeftName.toSource(), getValidWord());
				}
			}else if(node instanceof VariableInitializer&&((VariableInitializer)node).getInitializer() instanceof Name){
				AstNode Target=((VariableInitializer)node).getTarget();
				AstNode Init=((VariableInitializer)node).getInitializer();
				if(JsonArguObj.contains(Init.toSource())&&Target instanceof Name){
					JsonArguObj.add(Target.toSource());
				}
			}
			return true;
		}
	}

	class addObjName implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof Assignment){
				AstNode Left=((Assignment) node).getLeft();
				if(Left instanceof PropertyGet&&Params.contains(((PropertyGet) Left).getTarget().toSource())&&!ReserveKeyWord.contains(((PropertyGet) Left).getProperty().toSource())){
					ObjNameMap.put(((PropertyGet) Left).getProperty().toSource(), getValidWord());
				}
			}
			return true;
		}
	}

	private Set<String> Params=new HashSet<String>();
	private void DealObjNodeInFunctionCall(){
		Iterator it=ObjNodeInFunctionCall.iterator();
		while(it.hasNext()){
			FunctionCall FuncNode=(FunctionCall)it.next();
			List<AstNode> Argus=FuncNode.getArguments();
			AstNode Target=FuncNode.getTarget();
			if(Target instanceof ParenthesizedExpression)Target=((ParenthesizedExpression) Target).getExpression();
			if(Target instanceof FunctionNode){
				List<AstNode> FuncParams=((FunctionNode) Target).getParams();
				if(FuncParams.size()<=Argus.size()){
					for(int i=0;i<Argus.size();i++){
						if(ObjNode.contains(Argus.get(i).toSource()))
							Params.add(FuncParams.get(i).toSource());
						if(WindowSet.contains(Argus.get(i).toSource())){
							WindowSet.add(FuncParams.get(i).toSource());
						}
					}
					Target.visit(new addObjName());
					Params.clear();
				}else{
					continue;
				}
			}else if(Target instanceof Name){
				TargetFunNodeStr.add(Target.toSource());
			}
		}
	}

	class FindName implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof Name){
				WindowLists.add(node.toSource());
			}
			return true;
		}
	}

	private Set<String> WindowLists=new HashSet<String>();
	class FindObjFunction implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof FunctionNode){
				//获取有函数名的函数定义
				AstNode FunctionName=((FunctionNode) node).getFunctionName();
				if(FunctionName!=null&&TargetFunNodeStr.contains(FunctionName.toSource())){
					TargetFunNodeAst.add(FunctionName);
				}
			}else if(node instanceof VariableInitializer&&((VariableInitializer)node).getInitializer() instanceof FunctionNode){
				//获取将函数付给变量的函数定义
				AstNode TargetName=((VariableInitializer)node).getTarget();
				if(TargetFunNodeStr.contains(TargetName.toSource())){
					TargetFunNodeAst.add(TargetName);
				}
			}else if(node instanceof Assignment){
				AstNode Left=((Assignment) node).getLeft();
				if(Left instanceof PropertyGet&&((PropertyGet) Left).getTarget() instanceof Name&&JsonArguObj.contains(((PropertyGet) Left).getTarget().toSource())){
					ObjNameMap.remove(((PropertyGet) Left).getProperty().toSource());
				}
				if(Left instanceof PropertyGet&&((PropertyGet) Left).getTarget() instanceof Name&&WindowSet.contains(((PropertyGet) Left).getTarget().toSource())){
					((Assignment) node).getRight().visit(new FindName());
				}
			}else if(node instanceof Assignment&&((Assignment)node).getRight() instanceof ObjectLiteral){
				AstNode Left=((Assignment)node).getLeft();
				AstNode Right=((Assignment)node).getRight();
				if(JsonArguObj.contains(Left.toSource())){
					node.visit(new RemoveJsonArguObj());
				}
			}else if(node instanceof VariableInitializer&&((VariableInitializer)node).getInitializer() instanceof ObjectLiteral){
				AstNode Target=((VariableInitializer)node).getTarget();
				AstNode Init=((VariableInitializer)node).getInitializer();
				if(JsonArguObj.contains(Target.toSource())){
					node.visit(new RemoveJsonArguObj());
				}
			}else if(node instanceof FunctionCall){
				AstNode Tar=((FunctionCall) node).getTarget();
				//System.out.println(Tar.toSource());
				if(!FunctionNames.contains(Tar.toSource())){
					//System.out.println(Tar.toSource());
					//node.visit(new getArguLists());
				}
			}
			return true;
		}
	}

	private Set<String> ArguLists=new HashSet<String>();
	class getArguLists implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof FunctionCall){
				List<AstNode> ArgusLists=((FunctionCall) node).getArguments();
				for(int i=0;i<ArgusLists.size();i++){
					if(ArgusLists.get(i) instanceof Name&&ObjNode.contains(ArgusLists.get(i).toSource())){
						ArguLists.add(ArgusLists.get(i).toSource());
						JsonArguObj.add(ArgusLists.get(i).toSource());
					}
				}
			}
			return true;
		}
	}

	class RemoveReturn implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof ReturnStatement){
				node.visit(new RemoveJsonArguObj());
			}
			return true;
		}
	}


	class FindTargetFunctionName implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof FunctionNode&&((FunctionNode)node).getFunctionName()!=null&&((FunctionNode)node).getFunctionName().toSource().equals(TargetFunctionName)){
				node.visit(new RemoveReturn());
			}
			return true;
		}
	}

	private String TargetFunctionName=null;
	class DealWindowLists implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof VariableInitializer){
				AstNode Target=((VariableInitializer)node).getTarget();
				AstNode Init=((VariableInitializer)node).getInitializer();
				boolean just1=Init instanceof FunctionCall&&!(((FunctionCall)Init).getTarget() instanceof Name);
				boolean just2=Init instanceof FunctionCall&&((FunctionCall)Init).getTarget() instanceof Name;
				if(WindowLists.contains(Target.toSource())&&just1){
					node.visit(new RemoveReturn());
				}else if(WindowLists.contains(Target.toSource())&&just2){
					TargetFunctionName=((FunctionCall)Init).getTarget().toSource();
					nodeclone.visit(new FindTargetFunctionName());
				}
			}else if(node instanceof Assignment){
				AstNode Left=((Assignment) node).getLeft();
				AstNode Right=((Assignment) node).getRight();
				boolean just1=Right instanceof FunctionCall&&!(((FunctionCall)Right).getTarget() instanceof Name);
				boolean just2=Right instanceof FunctionCall&&((FunctionCall)Right).getTarget() instanceof Name;
				if(WindowLists.contains(Left.toSource())){
					node.visit(new RemoveReturn());
				} if(WindowLists.contains(Left.toSource())&&just2){
					TargetFunctionName=((FunctionCall)Right).getTarget().toSource();
					nodeclone.visit(new FindTargetFunctionName());
				}
			}
			return true;
		}
	}

	class RemoveObjName implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof ObjectProperty){
				AstNode Left=((ObjectProperty) node).getLeft();
				if(Left instanceof Name)
					ObjNameMap.remove(Left.toSource());
			}
			return true;
		}
	}

	class RemoveJsonArguObj implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof ObjectProperty){
				AstNode Left=((ObjectProperty) node).getLeft();
				if(Left instanceof Name)
					ObjNameMap.remove(Left.toSource());
			}
			return true;
		}
	}


	private AstNode getTarget(AstNode node){
		if(node instanceof PropertyGet||node instanceof ElementGet){
			AstNode target=null;
			if(node instanceof PropertyGet)target=((PropertyGet) node).getTarget();
			else if(node instanceof ElementGet)target=((ElementGet) node).getTarget();
			while(true){
				if(!(target instanceof PropertyGet||target instanceof FunctionCall||target instanceof ElementGet))break;
				if(target instanceof FunctionCall)target=((FunctionCall) target).getTarget();
				if(target instanceof PropertyGet)target=((PropertyGet) target).getTarget();
				if(target instanceof ElementGet)target=((ElementGet) target).getTarget();
			}
			return target;
		}
		return null;
	}


	class ChangeObjName implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof Name){
				if(!ReserveKeyWord.contains(node.toSource())){
					AstNode parent=node.getParent();
					boolean just1=parent instanceof PropertyGet&&getTarget((PropertyGet)parent)!=node;
					//boolean just2=parent instanceof ElementGet&&getTarget((ElementGet)parent)!=node;
					boolean just2=parent instanceof PropertyGet&&!WinSourceObj.contains(getTarget((PropertyGet)parent).toSource());
					boolean just3=!objectNames.contains(node.toSource());
					boolean just4=parent instanceof ObjectProperty&&((ObjectProperty)parent).getLeft()==node;
					if(just2&&just1&&just3){
						String NewName=ObjNameMap.get(node.toSource());
						if(NewName!=null)((Name) node).setIdentifier(NewName);
					}else if((just2&&just1)||just4){
						String NewName=ObjNameMap.get(node.toSource());
						if(NewName!=null)((Name)node).setIdentifier(NewName);
					}
				}
			}else if(node instanceof StringLiteral){
				String Str=ObjNameMap.get(((StringLiteral) node).getValue());
				if(Str!=null){
					((StringLiteral)node).setValue(Str);
				}
			}
			return true;
		}
	}

	private void DealTargetFunNodeAst(){
		Iterator it=ObjNodeInFunctionCall.iterator();
		while(it.hasNext()){
			FunctionCall FuncNode=(FunctionCall)it.next();
			List<AstNode> Argus=FuncNode.getArguments();
			AstNode Target=FuncNode.getTarget();
			if(Target instanceof Name){
				Iterator Tarit=TargetFunNodeAst.iterator();
				while(Tarit.hasNext()){
					AstNode Name=(AstNode)Tarit.next();
					if(Name.toSource().equals(Target.toSource())){
						AstNode parent=Name.getParent();
						if(parent instanceof FunctionNode){
							List<AstNode> NameParams=((FunctionNode) parent).getParams();
							if(NameParams.size()==Argus.size()){
								for(int i=0;i<Argus.size();i++){
									if(ObjNode.contains(Argus.get(i).toSource()))
										Params.add(NameParams.get(i).toSource());
								}
								parent.visit(new addObjName());
								Params.clear();
							}
						}else if(parent instanceof VariableInitializer){
							AstNode Init=((VariableInitializer) parent).getInitializer();
							if(Init!=null&&Init instanceof FunctionNode){
								List<AstNode> NameParams=((FunctionNode) Init).getParams();
								if(NameParams.size()==Argus.size()){
									for(int i=0;i<Argus.size();i++){
										if(ObjNode.contains(Argus.get(i).toSource()))
											Params.add(NameParams.get(i).toSource());
									}
									Init.visit(new addObjName());
									Params.clear();
								}
							}
						}
					}
				}
			}
		}
	}


	private Set<String>WinSourceObj=new HashSet<String>();//记录释放的初始结点;
	private AstNode nodeclone=null;
	public void DealPropertyName(AstNode node,Set<String> ReserveNames){
		nodeclone=node;
		Iterator it=ReserveNames.iterator();
		while(it.hasNext()){
			ReserveKeyWord.add((String)it.next());
		}
		collectNames(node);
		node.visit(new CollectFunctionNode());
		node.visit(new FindWinNode());
		DealObjNodeInFunctionCall();
		node.visit(new FindWindowLists());
		node.visit(new CreateObjMap());
		Iterator itt=WindowLists.iterator();
		while(itt.hasNext()){
			String str=(String)itt.next();
			Set<String> source=ObjMap.get(str);
			if(source!=null){
				Iterator sit=source.iterator();
				WinSourceObj.add((String)sit.next());
			}
		}
		itt=WinSourceObj.iterator();
		while(itt.hasNext()){
			String Str=(String)itt.next();
			WindowLists.add(Str);
			System.out.println(Str);
		}
		node.visit(new FindObjNode());
		node.visit(new FindObjFunction());
		node.visit(new DealWindowLists());
		DealTargetFunNodeAst();
		node.visit(new ChangeObjName());
	}
}
