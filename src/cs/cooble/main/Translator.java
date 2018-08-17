package cs.cooble.main;

import cs.cooble.cypher.*;
import cs.cooble.dictionary.Dictionary;

import javax.xml.transform.TransformerException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matej on 20.7.2018.
 */
public class Translator {

    private static List<Cypher> cyphers;
    private static List<Decypher> decyphers;
    private static Alphabet alphabet = new Alphabet();
    private static Dictionary dictionary = Dictionary.load();
    private static int currentCypher;
    private static boolean decoding;


    public static void setCurrentCypher(int currentCypher) {
        Translator.currentCypher = currentCypher;
    }

    public static List<Cypher> getCyphers() {
        if (cyphers == null) {
            decyphers = new ArrayList<>();
            cyphers = new ArrayList<>();
            cyphers.add(new CaesarCypher(alphabet));
            decyphers.add(new CaesarDecypher(dictionary, alphabet, (CaesarCypher) cyphers.get(cyphers.size() - 1)));
            cyphers.add(new AtbashCypher(alphabet));
            decyphers.add(new AtbashDecypher(dictionary, alphabet, (AtbashCypher) cyphers.get(cyphers.size() - 1)));
            cyphers.add(new VigenereCypher(alphabet));
            decyphers.add(new VigenereDecypher(dictionary, alphabet, (VigenereCypher) cyphers.get(cyphers.size() - 1)));
            cyphers.add(new SymbolCypher(alphabet));
            decyphers.add(new SymbolDecipher(dictionary, alphabet,(SymbolCypher)cyphers.get(cyphers.size() - 1)));
        }
        return cyphers;
    }

    public static List<Decypher> getDecyphers() {
        return decyphers;
    }

    public static String translate(String input) {
        if (decoding) {
            decoding = false;
            final String[][] s = {{}};
            Thread t = new Thread(()-> s[0] =getCurrentDecypher().decypher(input));
            t.start();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (getCurrentDecypher().busy()){
                System.out.println(getCurrentDecypher().currentState());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(getCurrentCypher().getAttributes()[0]);
            StringBuilder builder = new StringBuilder();
            for (String value : s[0]) {
                builder.append(value);
                builder.append("\n");
            }
            return builder.toString();
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

    public static void saveCypher(Cypher cypher,File file){
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try(PrintWriter writer = new PrintWriter(file)){
            writer.println("#"+cypher);
            for (int i = 0; i < cypher.getAttributes().length; i++) {
                writer.println(cypher.getAttributesNames()[i]+"\t"+cypher.getAttributes()[i]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void loadCypher(Cypher cypher,File file){

        List<String> names = new ArrayList<>();
        List<String > values = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            if(!reader.readLine().equals("#"+cypher))
                return;

            String line;
            while ((line=reader.readLine())!=null){
                String name = line.split("\t")[0];
                String val = line.split("\t")[1];
                names.add(name);
                values.add(val);


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] atribNames = cypher.getAttributesNames();
        for (int i = 0; i < atribNames.length; i++) {
            String atribName = atribNames[i];
            atribNames[i]=values.get(names.indexOf(atribName));
        }
        cypher.setAttributes(atribNames);
    }
}
