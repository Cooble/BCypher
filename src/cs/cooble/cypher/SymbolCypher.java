package cs.cooble.cypher;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Replace each character with some other character specified by key: {@code Map<strange_symbol,possible_real_letter>}
 */
public class SymbolCypher implements Cypher {

    private boolean useKey;
    private final Alphabet alphabet;
    private Map<Integer, Integer> key = new HashMap<>();

    public SymbolCypher(Alphabet alphabet) {
        this.alphabet = alphabet;
        for (int i = 0; i < alphabet.length; i++) {
            key.put(alphabet.abc[i], alphabet.abc[alphabet.length - 1 - i]);
        }
    }

    @Override
    public String cypher(String input) {
        return useKey(key, input.toUpperCase());
    }

    @Override
    public String decypher(String input) {
        Map<Integer,Integer> newKey  =new HashMap<>();
        key.forEach(new BiConsumer<Integer, Integer>() {
            @Override
            public void accept(Integer integer, Integer integer2) {
                newKey.put(integer2,integer);
            }
        });
        return useKey(newKey,input);
    }

    @Override
    public String[] getAttributes() {
        String[] out = new String[alphabet.length+1];
        for (int i = 0; i < alphabet.length; i++) {
            Integer in = key.get(alphabet.abc[i]);
            String s = in==null?"?":((char)(int)in)+"";
            out[i] = "" + s;
        }
        out[out.length-1]=""+useKey;

        return out;
    }

    @Override
    public String[] getAttributesNames() {
        String[] out = new String[alphabet.length+1];
        for (int i = 0; i < alphabet.length; i++) {
            out[i] = "" + ((char) ((int) alphabet.abc[i]));
        }
        out[out.length-1]="use key";
        return out;
    }

    @Override
    public void setAttributes(String[] attributes) {
        key.clear();
        for (int i = 0; i < alphabet.length; i++) {
            char symbol;
            String in = attributes[i];
            if(in==null||in.length()==0)
                symbol='?';
            else symbol=in.toUpperCase().charAt(0);

            key.put(alphabet.abc[i], (int) symbol);
        }
        useKey=Boolean.parseBoolean(attributes[attributes.length-1]);
    }

    public void setKey(Map<Integer, Integer> key) {
        this.key = key;
        for (int i = 0; i < alphabet.length; i++) {
            Integer in = key.get(alphabet.abc[i]);
            key.put(alphabet.abc[i],in==null?'?':in);
        }
    }

    protected static String useKey(Map<Integer, Integer> key, String word) {
        StringBuilder builder = new StringBuilder(word.length());
        for (int i = 0; i < word.length(); i++) {
            if (key.keySet().contains((int) word.charAt(i))) {
                if (key.get((int) word.charAt(i)) == -1)//ignore -1
                    builder.append(word.charAt(i));
                else
                    builder.append((char) (int) key.get((int) word.charAt(i)));
            } else builder.append(word.charAt(i));
        }
        return builder.toString();
    }

    protected static String prepare(String txt) {
        txt = txt.replace("'", " ");
        txt = txt.replace("!", " ");
        txt = txt.replace("?", " ");
        txt = txt.replace(".", " ");
        txt = txt.replace("\n", " ");
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

    /**
     * mod in which decoder uses the key and guesses only symbols which are left with question mark
     * @return
     */
    public boolean isUseKey() {
        return useKey;
    }

    public Map<Integer, Integer> getKey() {
        return key;
    }
}
