package bat.ke.qq.com;

/***
 * ascii码
 */
public class AsciiCode {

    public static void main(String[] args) {
        char c[]="lies".toCharArray();
        for (int i=0;i<c.length;i++){

            System.out.println((c[i])+":"+(int)c[i]);
        }


    }
}
