package cs.cooble.cypher;

/**
 * Created by Matej on 20.7.2018.
 */
public interface Decypher {

    String[] decypher(String cypher);

    default boolean busy() {return false;}

    default String currentState(){return null;}
}
