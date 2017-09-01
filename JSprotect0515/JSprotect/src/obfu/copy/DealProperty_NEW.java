package obfu.copy;

import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class DealProperty_NEW {
    private Set<String> PropertyName;
    private ArrayList<String>PropertyNameList;
    private ArrayList<String> StringEn;
    private ArrayList<String> StringList;
    private Set<String> ReserveKeyWord;
    public DealProperty_NEW(){
        StringList=new ArrayList<String>();
        StringEn=new ArrayList<String>();
        PropertyName=new HashSet<String>();
        PropertyNameList=new ArrayList<String>();
        ReserveKeyWord=new HashSet<String>();
        InitReserveName();
    }
    private void InitReserveName(){
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
                if(PropertyName.contains(((StringLiteral) node).getValue())&&!(parent instanceof PropertyGet&&((PropertyGet)parent).getLeft()==node)){
                    while(true){
                        if(parent instanceof ExpressionStatement||parent instanceof FunctionCall||parent instanceof PropertyGet||parent instanceof ElementGet||parent instanceof VariableInitializer||parent instanceof Assignment)
                            break;
                        parent=parent.getParent();
                    }
                    StringEn.add(parent.toSource());
                    System.out.println(":"+node.toSource());
                    StringList.add(node.toSource());
                }
            }
            return true;
        }
    }

    public AstNode getAstNode(String filePath) throws IOException{
        try{
            Reader reader=new FileReader(filePath);
            CompilerEnvirons env=new CompilerEnvirons();
            env.setRecordingLocalJsDocComments(true);
            env.setAllowSharpComments(true);
            env.setRecordingComments(true);
            AstRoot node=new Parser(env).parse(reader,filePath,1);
            return node;
        }catch(IOException ee){
            System.out.println(ee.toString());
            return null;
        }
    }

    public ArrayList<String> getPropertyNameList(){
        return PropertyNameList;
        //获取属性名
    }

    public ArrayList<String> getStringEn(){
        return StringEn;
        //获取字符串详情
    }

    public ArrayList<String> getStringList(){
        return StringList;
        //获取属性字符串
    }

    public String getCode(){
        return StringCode;
        //获取所有代码
    }

    private String StringCode=null;
    public void DealPropertyName(String filePath) throws IOException{
        AstNode node=getAstNode(filePath);
        node.visit(new visit());
        for(int i=0,j=0;i<PropertyNameList.size();i++){
            if(ReserveKeyWord.contains(PropertyNameList.get(i))){
                PropertyName.remove(PropertyNameList.get(i));
                PropertyNameList.remove(i);
            }
        }
        node.visit(new visit2());
        for(int i=0;i<PropertyNameList.size();i++){
            System.out.println(PropertyNameList.get(i));
        }
        for(int i=0;i<StringEn.size();i++){
            System.out.println("Test in DP_NEW: " + StringEn.get(i));
        }
        StringCode=node.toSource();
    }
}
