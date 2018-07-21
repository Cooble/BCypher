package cs.cooble.main;

import cs.cooble.cypher.*;
import cs.cooble.dictionary.Dictionary;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
       // allCyphers();
      //  if (true)
      //      return;
        Scanner scanner = new Scanner(System.in);

        InputStream stream = Main.class.getClassLoader().getResourceAsStream("words_alpha.txt");
        Dictionary dictionary = new Dictionary();
        CaesarCypher caesar = new CaesarCypher(new Alphabet());
        CaesarDecypher decypher = new CaesarDecypher(dictionary, new Alphabet());

        dictionary.load(stream);
        System.out.println("loaded dick with " + dictionary.length() + " words");

        while (true) {
            System.out.println("========================================");
            System.out.println("enter value");
            String value = scanner.nextLine();
            System.out.println("running decypherification");
            String[] decyp = decypher.decypher(caesar.cypher(value));
            for (int i = 0; i <decyp.length; i++) {
                System.out.println(decyp[i]);

            }
        }


    }

    public static void allCyphers() {
        Scanner scanner = new Scanner(System.in);
        InputStream stream = Main.class.getClassLoader().getResourceAsStream("words_alpha.txt");
        Dictionary dictionary = new Dictionary();
        Alphabet alphabet = new Alphabet();

        List<Cypher> cyphers = new ArrayList<>();
        cyphers.add(new CaesarCypher(alphabet));
        cyphers.add(new AtbashCypher(alphabet));
        cyphers.add(new VigenereCypher(alphabet));
        System.out.println("Loaded cyphers:");
        while (true) {
            System.out.println("========================");
            for (Cypher cypher : cyphers) {
                System.out.println("* " + cypher);
            }
            System.out.println();
            System.out.println("enter cypher you want");
            int selectedIndex = 0;
            boolean con = true;
            while (con) {
                String s = scanner.nextLine();
                for (int i = 0; i < cyphers.size(); i++) {
                    if (cyphers.get(i).toString().toLowerCase().startsWith(s.toLowerCase())) {
                        selectedIndex = i;
                        System.out.println("You've selected " + cyphers.get(selectedIndex));
                        con = false;
                        break;
                    }
                }
            }
            Cypher c = cyphers.get(selectedIndex);
            System.out.println("Enter value");
            String value = scanner.nextLine();
            String[] attribs = c.getAttributes();
            for (int i = 0; i < attribs.length; i++) {
                System.out.println("Enter "+attribs[i]);
                attribs[i]=scanner.nextLine();
            }
            c.setAttributes(attribs);

            System.out.println("Cypher is: ");
            System.out.println();
            System.out.println(c.cypher(value));

        }
    }
}
