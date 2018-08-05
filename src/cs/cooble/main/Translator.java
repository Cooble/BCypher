package cs.cooble.main;

import cs.cooble.cypher.*;
import cs.cooble.dictionary.Dictionary;

import javax.xml.transform.TransformerException;
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
            cyphers.add(new SymbolCypher(alphabet));
            decyphers.add(new SymbolDecipher(dictionary, alphabet));
        }
        return cyphers;
    }

    public static List<Decypher> getDecyphers() {
        return decyphers;
    }

    public static String translate(String input) {
        if (decoding) {
            decoding = false;
            final String[] s = {null};
            Thread t = new Thread(()-> s[0] =getCurrentDecypher().decypher(input)[0]);
            t.start();
            while (getCurrentDecypher().busy()){
                System.out.println(getCurrentDecypher().currentState());
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return s[0];
        }
        return getCurrentCypher().cypher(input);
    }

    public static Cypher getCurrentCypher() {
        return cyphers.get(currentCypher);
    }
    public static Decypher getCurrentDecypher() {
        return decyphers.get(currentCypher);
    }

    public static void decode() {
        decoding = true;
    }

    public static boolean busy() {
        return decyphers.get(currentCypher).busy();
    }

    public static String currentState() {
        return decyphers.get(currentCypher).currentState();
    }
}
