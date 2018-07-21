package cs.cooble.cypher;

import cs.cooble.dictionary.Dictionary;

/**
 * Created by Matej on 20.7.2018.
 */
public class AtbashDecypher implements Decypher {
    private final Dictionary dictionary;
    private final Alphabet alphabet;
    private final AtbashCypher cypher;

    public AtbashDecypher(Dictionary dictionary, Alphabet alphabet, AtbashCypher cypher) {

        this.dictionary = dictionary;
        this.alphabet = alphabet;
        this.cypher = cypher;
    }

    @Override
    public String[] decypher(String cypher) {
        if(dictionary.getNumberOfWords(cypher.toUpperCase())>dictionary.getNumberOfWords(this.cypher.decypher(cypher))){
            return new String[]{cypher};
        }else return new String[]{this.cypher.cypher(cypher)};
    }
}
