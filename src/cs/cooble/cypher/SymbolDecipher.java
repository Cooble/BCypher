package cs.cooble.cypher;

import cs.cooble.dictionary.Dictionary;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matej on 5.8.2018.
 */
public class SymbolDecipher implements Decypher {
    private final Dictionary dictionary;
    private final Alphabet alphabet;

    private Map<Integer, Integer> key = new HashMap<>();
    private Map<Integer, Boolean> usedKeys = new HashMap<>();
    private final long MAX_NUMBER;


    public SymbolDecipher(Dictionary dictionary, Alphabet alphabet) {
        this.dictionary = dictionary;
        this.alphabet = alphabet;
        tries=-1;
        MAX_NUMBER = Maths.factorial(23);
    }

    @Override
    public String[] decypher(String cypher) {
        tries=0;
        final String mainWord = SymbolCypher.getLongest(SymbolCypher.toWords(cypher));

        boolean success = decypherPlease(mainWord);
        if (success)
            return new String[]{SymbolCypher.useKey(key, cypher)};
        else return null;

    }

    private boolean doneDecy;
    private volatile long tries;

    private boolean decypherPlease(String cypher) {
        doneDecy = false;
        decy(cypher, 0);
        tries=-1;
        return doneDecy;
    }

    private void decy(String cypher, int currentIndex) {
        for (int i = 0; i < alphabet.length && !doneDecy; i++) {
            Boolean b = usedKeys.get(alphabet.abc[i]);
            if (b != null && b)
                continue;
            key.put(alphabet.abc[currentIndex], alphabet.abc[i]);
            usedKeys.put(alphabet.abc[i], true);
            if (currentIndex != alphabet.length - 1) {
                decy(cypher, currentIndex + 1);
            } else {
                testKey(key, cypher);
                tries++;
            }
            usedKeys.put(alphabet.abc[i], false);
        }
    }

    private void testKey(Map<Integer, Integer> key, String cypher) {
        if (dictionary.contains(SymbolCypher.useKey(key, cypher)))
            doneDecy = true;
    }

    @Override
    public String currentState() {
        return "("+tries+"/"+ MAX_NUMBER +")";
    }

    @Override
    public boolean busy() {
        return tries!=-1;
    }
}
