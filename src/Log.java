/**
 * Created by hd on 2017/6/29 AD.
 */
public class Log {
    public static void printtt(String... string){
        StringBuffer str = new StringBuffer();
        str.append("*****  t= "+ DVAlgorithm.time+ "     ");
        for (int i = 0; i <string.length ; i++) {
            str.append("%15s");
        }
        str.append("   *****");
        str.append("\n");

        System.out.format(str.toString(), string);


    }
}
