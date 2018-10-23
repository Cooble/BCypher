package mc.cooble.main;

import mc.cooble.cypher.*;
import mc.cooble.dictionary.Dictionary;

import java.io.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) {
      /*  generateSizedDictionaries(new File("D:\\Dev\\Java\\NORMAL\\BCypher\\res\\word_frequency.txt"),new File("D:\\Dev\\Java\\NORMAL\\BCypher\\res"));
        if(true)
            return;*/
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

    /**
     * sorts word_frequency.txt words to files by its length
     *
     * @param srcFile
     * @param folder
     */
    public static void generateSizedDictionaries(File srcFile,File folder){
        if(!folder.exists())
            folder.mkdirs();
        Map<Integer,List<String>> sorted = new HashMap<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(srcFile))) {
            reader.lines().forEach(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    String word = s.split("\t")[0];//remove occurrences
                    int length = word.length();
                    if(!sorted.containsKey(length))
                        sorted.put(length,new ArrayList<>());
                    sorted.get(length).add(s);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        sorted.forEach(new BiConsumer<Integer, List<String>>() {
            @Override
            public void accept(Integer integer, List<String> strings) {
                File target  = new File(folder.getAbsolutePath()+"/"+integer+".txt");
                try (PrintWriter writer = new PrintWriter(target)){
                    for(String s:strings) {
                        double d = Double.parseDouble(s.split("\t")[1]);
                        if(d>=484886)//ignore not so popular words
                            writer.println(s.toUpperCase());
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("done copying");

    }
}
