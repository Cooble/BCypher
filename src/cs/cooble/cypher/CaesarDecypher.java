package cs.cooble.cypher;

import cs.cooble.dictionary.Dictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matej on 20.7.2018.
 */
public class CaesarDecypher implements Decypher {

    private Dictionary dictionary;
    private CaesarCypher caesar;
    private Alphabet alphabet;

    public CaesarDecypher(Dictionary dictionary, Alphabet alphabet) {
        this.dictionary = dictionary;
        this.alphabet = alphabet;
        caesar = new CaesarCypher(alphabet);
    }

    public CaesarDecypher(Dictionary dictionary, Alphabet alphabet, CaesarCypher caesar) {
        this.dictionary = dictionary;
        this.alphabet = alphabet;
        this.caesar = caesar;
    }

    @Override
    public String[] decypher(String cypher) {
        int shift=-1;
        cypher = cypher.toUpperCase();
        String[] out = new String[alphabet.length];
        int[] probability = new int[alphabet.length];

        for (int i = 0; i < alphabet.length; i++) {
            caesar.setShift(i);
            out[i] = caesar.decypher(cypher);
            probability[i] = dictionary.getNumberOfWords(out[i]);
        }
        int maxProbability = 0;
        List<String> possibles = new ArrayList<>();
        for (int aProbability : probability) {
            maxProbability = Math.max(maxProbability, aProbability);
        }
        for (int i = 0; i < probability.length; i++) {
            if (probability[i] == maxProbability) {
                if (shift == -1)
                    shift = i;
                possibles.add(out[i]);
            }

        }
        caesar.setShift(alphabet.length-shift);
        return possibles.toArray(new String[possibles.size()]);
    }
}
