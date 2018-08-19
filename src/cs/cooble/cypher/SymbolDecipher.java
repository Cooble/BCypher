package cs.cooble.cypher;

import cs.cooble.dictionary.Dictionary;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matej on 5.8.2018.
 */
public class SymbolDecipher implements Decypher {
    private final SymbolDec symbolDec;
    private final SymbolCypher symbolCypher;
    private volatile boolean busy;


    public SymbolDecipher(Dictionary dictionary, Alphabet alphabet, SymbolCypher symbolCypher) {
        symbolDec = new SymbolDec(dictionary, alphabet, alphabet);
        this.symbolCypher = symbolCypher;
    }

    @Override
    public String[] decypher(String cypher) {
        cypher =cypher.toUpperCase();
        busy=true;
        symbolDec.setUseKey(symbolCypher.isUseKey()?symbolCypher.getKey():null);
        symbolDec.decypher(cypher);
        Map<Integer, Integer> key = symbolDec.getBestKey();
        symbolCypher.setKey(key);
        busy=false;
        return new String[]{symbolCypher.decypher(cypher)};
    }

    @Override
    public String currentState() {
        return "";
    }

    @Override
    public boolean busy() {
        return busy;
    }
}
