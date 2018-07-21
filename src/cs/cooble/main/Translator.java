package cs.cooble.main;

import cs.cooble.cypher.*;
import cs.cooble.dictionary.Dictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matej on 20.7.2018.
 */
public class Translator {

    private static List<Cypher> cyphers;
    private static List<Decypher> decyphers;
    private static Alphabet alphabet = new Alphabet();
    private static Dictionary dictionary = new Dictionary();
    private static int currentCypher;
    private static String input;
    private static boolean decoding;


    public static void setCurrentCypher(int currentCypher) {
        Translator.currentCypher = currentCypher;
    }

    public static List<Cypher> getCyphers() {
        if (cyphers == null) {
            decyphers = new ArrayList<>();
            cyphers = new ArrayList<>();
            dictionary.load(Main.class.getClassLoader().getResourceAsStream("words_alpha.txt"));

            cyphers.add(new CaesarCypher(alphabet));
            decyphers.add(new CaesarDecypher(dictionary, alphabet, (CaesarCypher) cyphers.get(cyphers.size() - 1)));
            cyphers.add(new AtbashCypher(alphabet));
            decyphers.add(new AtbashDecypher(dictionary, alphabet, (AtbashCypher) cyphers.get(cyphers.size() - 1)));
            cyphers.add(new VigenereCypher(alphabet));
            decyphers.add(new VigenereDecypher(dictionary, alphabet, (VigenereCypher) cyphers.get(cyphers.size() - 1)));
        }
        return cyphers;
    }

    public static List<Decypher> getDecyphers() {
        return decyphers;
    }

    public static String translate(String input) {
        Translator.input = input;
        if (decoding) {
            decoding = false;
            return decyphers.get(currentCypher).decypher(input)[0];
        }
        return getCurrentCypher().cypher(input);
    }

    public static Cypher getCurrentCypher() {
        return cyphers.get(currentCypher);
    }

    public static void decode() {
        decoding = true;
    }
}
