import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by chengs on 17-4-13.
 */
public class CompileExp_2 {
    public static void main(String[] args) {
        HashMap<String,String> hashMap=new HashMap<String,String>();
        hashMap.put("=","eql");
        hashMap.put(";","semicolon");
        hashMap.put(",","comma");
        hashMap.put("(","lparen");
        hashMap.put(")","rparen");
        hashMap.put(":=","becomes");
        hashMap.put("+","plus");
        hashMap.put("-","minus");
        hashMap.put("/","divide");
        hashMap.put("*","multi");
        hashMap.put("#","uneql");
        hashMap.put("<","less");;
        hashMap.put("<=","lessOreql");
        hashMap.put(">","greater");
        hashMap.put(">=","greaterOreql");
        hashMap.put(".","period");
        String keywords[]={"const","var","procedure","begin","end","odd","if","then","call","while","do","read","write"};
        for(int i=0;i<keywords.length;i++){
            hashMap.put(keywords[i],keywords[i]+"sym");
        }
        ArrayList<String> list=new ArrayList<String>();
        try{
            File f=new File("PL_2.pl");
            Scanner sc=new Scanner(f);
            while(sc.hasNext()) {
                String str = sc.next();
                //System.out.println(str);
                int i=0;
                String buff="";
                int status=0;
                //0为开始,2为前一个字符为数字或者字符,1前一个数字为其他字符
                str=str.toLowerCase();
                while(i<str.length()){
                    if((str.charAt(i)>='0'&&str.charAt(i)<='9')||(str.charAt(i)>='a'&&str.charAt(i)<='z')){
                        if(status==0){
                            status=2;
                            buff=buff+str.charAt(i);
                        }else if(status==1){
                            status=2;
                            list.add(buff);
                            buff=String.valueOf(str.charAt(i));
                        }else if(status==2){
                            buff=buff+str.charAt(i);
                        }
                    }else{
                        if(status==0){
                            status=1;
                            buff=buff+str.charAt(i);
                        }else if(status==2){
                            status=1;
                            list.add(buff);
                            buff=String.valueOf(str.charAt(i));
                        }else if(status==1){
                            buff=buff+str.charAt(i);
                        }
                    }
                    i++;
                }
                if(buff!=null){
                    list.add(buff);
                }
            }
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                String key=iter.next().toString();
                String result=hashMap.get(key);
                if(result==null){
                    if(key.charAt(0)>='0'&&key.charAt(0)<='9'){
                        result="number";
                    }else{
                        result="ident";
                    }
                }
                System.out.printf("(\"%s\",%s)\n",key,result);
            }
        }catch (IOException e){
            System.out.println("File read Error:"+e.getMessage());
        }
    }
}
