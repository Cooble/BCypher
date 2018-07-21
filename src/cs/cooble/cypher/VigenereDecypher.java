package cs.cooble.cypher;

import cs.cooble.dictionary.Dictionary;

/**
 * Created by Matej on 20.7.2018.
 */
public class VigenereDecypher implements Decypher {
    private final Dictionary dictionary;
    private final Alphabet alphabet;
    private final VigenereCypher cypher;

    public VigenereDecypher(Dictionary dictionary, Alphabet alphabet, VigenereCypher cypher) {
        this.dictionary = dictionary;
        this.alphabet = alphabet;
        this.cypher = cypher;
    }

    @Override
    public String[] decypher(String cypher) {
        return new String[]{cypher};
    }
}
