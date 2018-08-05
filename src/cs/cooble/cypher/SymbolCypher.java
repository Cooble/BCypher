package cs.cooble.cypher;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matej on 5.8.2018.
 */
public class SymbolCypher implements Cypher {


    private final Alphabet alphabet;
    private Map<Integer,Integer> key = new HashMap<>();
    public SymbolCypher(Alphabet alphabet) {
        this.alphabet = alphabet;
        for (int i = 0; i < alphabet.length; i++) {
            key.put(alphabet.abc[i],alphabet.abc[alphabet.length-1-i]);
        }
    }

    @Override
    public String cypher(String input) {
        return useKey(key,prepare(input));
    }

    @Override
    public String decypher(String input) {
        return "";
    }

    @Override
    public String[] getAttributes() {
        String[] out = new String[alphabet.length];
        for (int i = 0; i < alphabet.length; i++)
            out[i]=""+((char)(int)key.get(alphabet.abc[i]));
        return out;
    }

    @Override
    public String[] getAttributesNames() {
        String[] out = new String[alphabet.length];
        for (int i = 0; i < alphabet.length; i++) {
            out[i]=""+((char)((int)alphabet.abc[i]));
        }
        return out;
    }

    @Override
    public void setAttributes(String[] attributes) {
        key.clear();
        for (int i = 0; i < alphabet.length; i++)
            key.put(alphabet.abc[i],(int)attributes[i].charAt(0));
    }

    protected static String useKey(Map<Integer, Integer> key, String word) {
        StringBuilder builder = new StringBuilder(word.length());
        for (int i = 0; i < word.length(); i++) {
            if (key.keySet().contains((int) word.charAt(i)))
                builder.append((char)(int)key.get((int) word.charAt(i)));
        }
        return builder.toString();
    }

    protected static String prepare(String txt) {
        txt = txt.replace("'", " ");
        txt = txt.replace("  ", " ");
        txt = txt.replace("  ", " ");
        txt = txt.trim();
        txt = txt.toUpperCase();
        return txt;
    }

    protected static String[] toWords(String txt) {
        return prepare(txt).split(" ");
    }

    protected static String getLongest(String[] words) {
        int longest = 0;
        int indexik = 0;
        for (int i = 0; i < words.length; i++) {
            if (words[i].length() > longest) {
                longest = words[i].length();
                indexik = i;
            }
        }
        return words[indexik];

    }

    @Override
    public String toString() {
        return "Symbol cypher";
    }
}
