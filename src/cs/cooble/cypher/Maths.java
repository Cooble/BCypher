package cs.cooble.cypher;

/**
 * Created by Matej on 5.8.2018.
 */
public class Maths {

    public static long factorial(long in) {
        long out = 1;
        while (in > 0) {
            out *= in;
            in--;
        }
        return out;
    }
}
