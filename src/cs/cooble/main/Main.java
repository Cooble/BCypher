package cs.cooble.main;

import cs.cooble.cypher.Alphabet;
import cs.cooble.cypher.AtbashCypher;
import cs.cooble.cypher.Cypher;
import cs.cooble.cypher.VigenereCypher;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Alphabet alphabet = new Alphabet();
        while (true) {
            System.out.println("Type string to cypher:");
            String s = scanner.nextLine();
            System.out.println("Type key:");
            String key = scanner.nextLine();

            System.out.println("coded cipher is:");
            VigenereCypher cypher = new VigenereCypher(alphabet);
            cypher.setKey(key);
            System.out.println(cypher.cypher(s));
            System.out.println("decyphred");
            System.out.println(cypher.decypher(cypher.cypher(s)));
            System.out.println("type anything");
            scanner.nextLine();
        }
    }
}
