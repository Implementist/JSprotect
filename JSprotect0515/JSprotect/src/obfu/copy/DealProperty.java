package obfu.copy;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.*;

import java.util.*;



public class DealProperty {
	private Map<String,String> PropertyMap;
	private Set<String> PropertyName;
	private ArrayList<String>PropertyNameList;
	private ArrayList<String> StringEn;
	private ArrayList<AstNode> StringList;
	private Map<String,String> VarThisMap;
	private Set<String> ReserveNames;
	private Set<String> SetVarNames;
	private ArrayList<String>NamesInFlattern;
	private ArrayList<String>   ones;
	private ArrayList<String>   twos;
	private ArrayList<String>   threes;
	private ArrayList<String> Names;
	private Set<String> ReserveKeyWord;
	private int Namenum;
	DealProperty(){
		Namenum=0;
		PropertyMap=new HashMap<String,String>();
		StringList=new ArrayList<AstNode>();
		StringEn=new ArrayList<String>();
		NamesInFlattern=new ArrayList<String>();
		PropertyName=new HashSet<String>();
		PropertyNameList=new ArrayList<String>();
		ReserveNames=new HashSet<String>();
		SetVarNames=new HashSet<String>();
		VarThisMap=new HashMap<String,String>();
		ReserveKeyWord=new HashSet<String>();
		Names=new ArrayList<String>();
		InitReserveKeyWord();
		initVariablesPool();
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
		ReserveKeyWord.add("message");
		ReserveKeyWord.add("iceServers");
		ReserveKeyWord.add("urls");
		ReserveKeyWord.add("type");
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

	class getProperty implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof ObjectProperty){
				AstNode Left=((ObjectProperty) node).getLeft();
				if(Left instanceof Name&&!ReserveKeyWord.contains(Left.toSource())){
					String name=getValidWord();
					((Name)Left).setIdentifier(name);
					PropertyMap.put(Left.toSource(), name);
				}
			}else if(node instanceof PropertyGet){
				AstNode property=((PropertyGet) node).getProperty();
				if(property instanceof Name&&!ReserveKeyWord.contains(property.toSource()) ){
					String name=getValidWord();
					PropertyMap.put(property.toSource(), name);
					if(SetVarNames.contains(property.toSource())){
						VarThisMap.put(property.toSource(),name);
					}
					((Name)property).setIdentifier(name);
				}
			}
			return true;
		}
	}


	class visit implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof Assignment){
				AstNode Left=((Assignment) node).getLeft();
				while(Left instanceof PropertyGet){
					AstNode Property=((PropertyGet)Left).getProperty();
					if(!PropertyName.contains(Property.toSource())){
						PropertyName.add(Property.toSource());
						PropertyNameList.add(Property.toSource());
					}
					Left=((PropertyGet)Left).getTarget();
					if(Left instanceof FunctionCall)Left=((FunctionCall) Left).getTarget();
				}
			}
			else if(node instanceof ObjectProperty){
				AstNode Left=((ObjectProperty) node).getLeft();
				if(Left instanceof Name){
					if(!PropertyName.contains(Left.toSource())){
						PropertyName.add(Left.toSource());
						PropertyNameList.add(Left.toSource());
					}
				}else if(Left instanceof StringLiteral){
					ReserveKeyWord.add(((StringLiteral) Left).getValue());
				}
			}
			return true;
		}
	}
	class visit2 implements NodeVisitor{
		public boolean visit(AstNode node){
			if(node instanceof StringLiteral){
				AstNode parent=node.getParent();
				if(PropertyName.contains(((StringLiteral) node).getValue())&&!(parent instanceof ObjectProperty&&((ObjectProperty)parent).getLeft()==node)){
					StringList.add(node);
				}
			}
			return true;
		}
	}



	public Map<String,String> getVarThisSet(){
		return VarThisMap;
	}

	public void DealPropertyName(String PropertyStr,String PropertyStrList,int Prop,AstNode node,Set<String> SetVarNames){
		if(Prop==1){
			node.visit(new visit());
			for(int i=0,j=0;i<PropertyNameList.size();i++){
				if(ReserveKeyWord.contains(PropertyNameList.get(i))){
					PropertyName.remove(PropertyNameList.get(i));
					PropertyNameList.remove(i);
					//System.out.println("delete:"+PropertyNameList.get(i));
				}
			}
			node.visit(new visit2());
			for(int i=0;i<PropertyStr.length();i++){
				if(PropertyStr.charAt(i)=='0')
					PropertyNameList.remove(i);
			}
			node.visit(new getProperty());
			Iterator it=PropertyMap.keySet().iterator();
			while(it.hasNext()){
				System.out.println(it.next());
			}
			for(int i=0;i<PropertyStrList.length();i++){
				if(PropertyStrList.charAt(i)=='1'){
					System.out.println(((StringLiteral)StringList.get(i)).getValue());
					((StringLiteral)StringList.get(i)).setValue(PropertyMap.get(((StringLiteral)StringList.get(i)).getValue()));
				}
			}
		}
	}
}
