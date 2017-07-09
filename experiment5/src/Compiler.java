import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.lang.System.exit;

/**
 * Created by chengs on 17-4-28.
 */
public class Compiler {

    public ArrayList<Res> resultList;
    public int nowLocal;
    public int tmpNum;
    class Res{
        String value;
        String type;
        Res(String value,String type){
            this.value=value;
            this.type=type;
        }
    }

    public static void main(String[] args) {
            String programPath="test_expression.pl";
            Compiler compiler=new Compiler(programPath);
            compiler.startCompile();
        //由于本次实验是在上次词法分析的基础上改编的,所以暂不支持自定义变量计算问题
    }
    public void startCompile(){
        tmpNum=0;
        expression();
        if(nowLocal+1!=resultList.size()){
            error(33);
        }
        System.out.println("编译正确!");
    }

    public  Compiler(String programPath) {

        HashMap<String,String> hashMap = new HashMap<String, String>();
        hashMap.put("=", "eql");
        hashMap.put(";", "semicolon");
        hashMap.put(",", "comma");
        hashMap.put("(", "lparen");
        hashMap.put(")", "rparen");
        hashMap.put(":=", "becomes");
        hashMap.put("+", "plus");
        hashMap.put("-", "minus");
        hashMap.put("/", "slash");
        hashMap.put("*", "times");
        hashMap.put("#", "uneql");
        hashMap.put("<", "less");
        ;
        hashMap.put("<=", "lessOreql");
        hashMap.put(">", "greater");
        hashMap.put(">=", "greaterOreql");
        hashMap.put(".", "period");
        String keywords[] = {"const", "var", "procedure", "begin", "end", "odd", "if", "then", "call", "while", "do", "read", "write"};
        for (int i = 0; i < keywords.length; i++) {
            hashMap.put(keywords[i], keywords[i] + "sym");
        }
        ArrayList<String> list = new ArrayList<String>();
        try {
            File f = new File(programPath);
            Scanner sc = new Scanner(f);
            while (sc.hasNext()) {
                String str = sc.next();
                System.out.println(str);
                int i = 0;
                String buff = "";
                int status = 0;
                //状态标识符:0代表开始状态,2为前一个字符为数字或者字母的状态,1前一个字符为其他字符的状态
                str = str.toLowerCase();

                while (i < str.length()) {
                    if ((str.charAt(i) >= '0' && str.charAt(i) <= '9') || (str.charAt(i) >= 'a' && str.charAt(i) <= 'z')) {
                        if (status == 0) {
                            status = 2;
                            buff = buff + str.charAt(i);
                        } else if (status == 1) {
                            status = 2;
                            list.add(buff);
                            buff = String.valueOf(str.charAt(i));
                        } else if (status == 2) {
                            buff = buff + str.charAt(i);
                        }
                    } else {
                        if (status == 0) {
                            status = 1;
                            buff = buff + str.charAt(i);
                        } else if (status == 2) {
                            status = 1;
                            list.add(buff);
                            buff = String.valueOf(str.charAt(i));
                        } else if (status == 1) {
                            buff = buff + str.charAt(i);
                        }
                    }
                    i++;
                }
                if (buff != null) {
                    list.add(buff);
                }
            }
            Iterator iter = list.iterator();
            resultList=new ArrayList<Res>();
            while (iter.hasNext()) {
                String key = iter.next().toString();
                String result = hashMap.get(key);
                if (result == null) {
                    if (key.charAt(0) >= '0' && key.charAt(0) <= '9') {
                        resultList.add(new Res(key,"number"));
                    } else if(key.charAt(0)>='a'&&key.charAt(0)<='z'){
                        resultList.add(new Res(key,"ident"));
                    }else{
                        for(int i=0;i<key.length();i++){
                            result=hashMap.get(key.substring(i,i+1));
                            //System.out.println(result);
                            resultList.add(new Res(key.substring(i,i+1),result));
                        }
                    }
                }else{
                    resultList.add(new Res(key,result));
                }
            }
            resultList.add(new Res("END","END"));
            nowLocal=0;
        } catch (IOException e) {
            System.out.println("File read Error:" + e.getMessage());
        }
        System.out.println();

        for(int i=0;i<resultList.size();i++){
            System.out.println(resultList.get(i).type+" "+resultList.get(i).value);
        }
        System.out.println();
    }
    public String expression(){
        //System.out.println("expression:"+nowLocal);
        String temp="";
        if(resultList.get(nowLocal).type.equals("minus")) {
           nextSymbol();
           temp+="-"+term();
        }else if(resultList.get(nowLocal).type.equals("plus")) {
            nextSymbol();
            temp+="+"+term();
        }else{
            temp+=term();
        }
        while(resultList.get(nowLocal).type.equals("plus")||resultList.get(nowLocal).type.equals("minus")){
            if(resultList.get(nowLocal).type.equals("plus")){
                nextSymbol();
                System.out.println("(+,"+temp+","+term()+",t"+(++tmpNum)+")");
                temp="t"+tmpNum;
            }else{
                nextSymbol();
                System.out.println("(-,"+temp+","+term()+",t"+(++tmpNum)+")");
                temp="t"+tmpNum;
            }
        }
        return temp;
    }
    public String term(){
        //System.out.println("term:"+nowLocal);
        String temp=factor();
        while(resultList.get(nowLocal).type.equals("times")||resultList.get(nowLocal).type.equals("slash")){
            if(resultList.get(nowLocal).type.equals("times")){
                nextSymbol();
                System.out.println("(*,"+temp+","+factor()+",t"+(++tmpNum)+")");
                temp="t"+tmpNum;
            }else{
                nextSymbol();
                System.out.println("(/,"+temp+","+factor()+",t"+(++tmpNum)+")");
                temp="t"+tmpNum;
            }
        }
        return temp;
    }
    public String factor(){
        //System.out.println("factor:"+nowLocal);
        String temp="";
        if(resultList.get(nowLocal).type.equals("ident")){
            temp=resultList.get(nowLocal).value;
            nextSymbol();
        }else if(resultList.get(nowLocal).type.equals("number")){
            temp=resultList.get(nowLocal).value;
            nextSymbol();
        }else if(resultList.get(nowLocal).type.equals("lparen")){
            nextSymbol();
            temp= expression();
            if(resultList.get(nowLocal).type.equals("rparen")){
                nextSymbol();
            }else{
                error(22);
            }
        }else{
            error(11);
        }
        return temp;
    }
    public void error(int index){
        System.out.println("编译错误!");
        if(index==11) {
            System.out.println("    没有找到需要的因子结构");
        }else if(index==22){
            System.out.println("    因子缺少右括号");
        }else if(index==33){
            System.out.println("    程序有多余部分");
        }else if(index==44){
            System.out.println("    表达式不完整");
        }else{
            System.out.println("    为定义名称错误");
        }
        exit(index);
    }
    public void nextSymbol(){
        if(nowLocal+1<resultList.size()) {
            nowLocal++;
        }else {
            error(44);
        }
    }
}

//下面是实验3语法分析的代码,读者可以对照参考一下

//    public int expression(){
//        //System.out.println("expression:"+nowLocal);
//        if(resultList.get(nowLocal).type.equals("plus")||resultList.get(nowLocal).type.equals("minus")) {
//            nextSymbol();
//            term();
//        }else{
//            term();
//        }
//        while(resultList.get(nowLocal).type.equals("plus")||resultList.get(nowLocal).type.equals("minus")){
//            nextSymbol();
//            term();
//        }
//        return 0;
//    }
//    public int term(){
//        //System.out.println("term:"+nowLocal);
//        factor();
//        while(resultList.get(nowLocal).type.equals("times")||resultList.get(nowLocal).type.equals("slash")){
//            nextSymbol();
//            factor();
//        }
//        return 0;
//    }
//    public int factor(){
//        //System.out.println("factor:"+nowLocal);
//        if(resultList.get(nowLocal).type.equals("ident")){
//            nextSymbol();
//        }else if(resultList.get(nowLocal).type.equals("number")){
//            nextSymbol();
//        }else if(resultList.get(nowLocal).type.equals("lparen")){
//            nextSymbol();
//            expression();
//            if(resultList.get(nowLocal).type.equals("rparen")){
//                nextSymbol();
//            }else{
//                error(22);
//            }
//        }else{
//            error(11);
//        }
//        return 0;
//    }
//    public void error(int index){
//        System.out.println("编译错误!");
//        if(index==11) {
//            System.out.println("    没有找到需要的因子结构");
//        }else if(index==22){
//            System.out.println("    因子缺少右括号");
//        }else if(index==33){
//            System.out.println("    程序有多余部分");
//        }else if(index==44){
//            System.out.println("    表达式不完整");
//        }else{
//            System.out.println("    为定义名称错误");
//        }
//        exit(index);
//    }
//    public void nextSymbol(){
//        if(nowLocal+1<resultList.size()) {
//            nowLocal++;
//        }else {
//            error(44);
//        }
//    }
//}
