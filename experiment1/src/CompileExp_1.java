import java.io.*;
import java.util.Scanner;

/**
 * Created by chengs on 17-3-30.
 */
public class CompileExp_1 {
    public static void main(String[] args) throws FileNotFoundException {
        String keywords[]={"const","var","procedure","begin","end","odd","if","then","call","while","do","read","write"};
        String UserWords[][]=new String[1000][2];
        int UserWordsLength=0;
        try{
            File f=new File("PL_5.pl");
            Scanner sc=new Scanner(f);
            sc.useDelimiter("[^a-zA-Z0-9]");
            while(sc.hasNext()){
                String str=sc.next();
                //System.out.println(str);
                int i;
                for(i=0;i<keywords.length;i++){
                    if(str.equalsIgnoreCase(keywords[i])) break;
                }
                if(i==keywords.length) {
                    if (!(str.isEmpty()||str.charAt(0) >= '0' && str.charAt(0) <= '9')) {
                        int j;
                        for(j=0;j<UserWordsLength;j++){
                            if(str.equalsIgnoreCase(UserWords[j][0])){
                                UserWords[j][1]=Integer.toString(Integer.parseInt(UserWords[j][1])+1);
                                break;
                            }
                        }
                        if(j==UserWordsLength) {
                            //System.out.println(str.toLowerCase());
                            UserWords[UserWordsLength][0]=str.toLowerCase();
                            UserWords[UserWordsLength][1]="1";
                            UserWordsLength++;
                        }
                    }
                }
            }
            for(int i=0;i<UserWordsLength;i++){
                System.out.printf("( %s : %s )\n",UserWords[i][0],UserWords[i][1]);
            }
        }catch (IOException e){
            System.out.println("File read Error:"+e.getMessage());
        }
    }
}
