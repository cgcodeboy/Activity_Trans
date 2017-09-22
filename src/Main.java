import java.util.Random;
public class Main {

    public static void main(String[] args)throws Exception {
        int n=10,which=2;
        int v[][] = new int[n][2];
        Random rand = new Random();
        for(int i=0;i<n;i++)
            for(int j=0;j<2;j++) {
                v[i][j] = rand.nextInt(300) + 50;
            }
        ga out=new ga(n,which,v);
        System.out.println(out.value);

    }
}
