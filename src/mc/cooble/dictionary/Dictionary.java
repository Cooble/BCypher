package mc.cooble.dictionary;

import com.sun.istack.internal.Nullable;
import mc.cooble.main.Main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Matej on 20.7.2018.
 */
public class Dictionary {
    private static final int DICKS_NUMBER = 22;
    private String[] dic = new String[0];
    private String[][] sizedDics;
    private int[][] sizedDicsOcuurences;
    private Map<Integer, Double> letterFrequency;

    public static Dictionary load() {
        InputStream stream = Main.class.getClassLoader().getResourceAsStream("word_frequency.txt");
        Dictionary dictionary = new Dictionary();
        dictionary.load(stream);
        dictionary.setNumberOfSizedDicks(DICKS_NUMBER + 1);
        for (int i = 1; i < DICKS_NUMBER + 1; i++) {
            InputStream stream2 = Main.class.getClassLoader().getResourceAsStream(i + ".txt");
            InputStream stream3 = Main.class.getClassLoader().getResourceAsStream(i + ".txt");
            if (stream2 != null)
                dictionary.loadSized(stream2, stream3);
        }
        Map<Integer, Double> freqMap = new HashMap<>();
        InputStream freq = Main.class.getClassLoader().getResourceAsStream("letter_frequency.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(freq))) {
            reader.lines().forEach((e) -> {
                String[] s = e.split(" ");
                int c = s[0].charAt(0);
                double d = Double.parseDouble(s[1]);
                freqMap.put(c, d);
            });
        } catch (Exception ignored) {
            System.err.println("cannot load dic ");
            ignored.printStackTrace();
        }
        dictionary.letterFrequency = freqMap;

        System.out.println("loaded dick with " + dictionary.length() + " words");
        /*System.out.println("some dics");
        for (int i = 0; i < 10; i++) {
            System.out.println(dictionary.dic[i]);
        }*/
        return dictionary;
    }

    private void load(InputStream input) {
        dic = toArray(readFile(input, 0, "\t"));
    }

    private void setNumberOfSizedDicks(int sizedDicksNumber) {
        sizedDics = new String[sizedDicksNumber][];
        sizedDicsOcuurences = new int[sizedDicksNumber][];
    }

    private void loadSized(InputStream input, InputStream input2) {
        List<String> strings = readFile(input, 0, "\t");
        List<String> occurrences = readFile(input2, 1, "\t");

        int[] oc = new int[occurrences.size()];
        for (int i = 0; i < occurrences.size(); i++) {
            oc[i] = (int)(Double.parseDouble(occurrences.get(i))/10d);//dividing each occurrence by 10 because Integer.Max_VALUE < occurrence
        }

        sizedDics[strings.get(0).length()] = toArray(strings);
        sizedDicsOcuurences[strings.get(0).length()] = oc;
    }

    private List<String> readFile(InputStream input, int wordIndex, @Nullable String separator) {
        ArrayList<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            reader.lines().forEach((e) -> {
                e = e.toUpperCase();
                if (separator != null) {
                    String[] split = e.split(separator);
                    if (!(split.length > wordIndex)) {
                        throw new IllegalArgumentException("invalid file!");
                    }
                    list.add(split[wordIndex]);
                } else
                    list.add(e);
            });
        } catch (Exception ignored) {
            System.err.println("cannot load dic ");
            ignored.printStackTrace();
        }
        return list;

    }

    private String[] toArray(List<String> list) {
        String[] a = new String[list.size()];
        for (int i = 0; i < a.length; i++) {
            a[i] = list.get(i);
        }
        return a;
    }

    public String get(int index) {
        if (index < 0 || index > dic.length - 1)
            return null;
        return dic[index];
    }

    public String[] getStringsMatching(String template) {
        ArrayList<String> out = new ArrayList<>();
        for (String word : dic) {
            if (isMatching(word, template))
                out.add(word);
        }
        return toArray(out);

    }

    public boolean isMatching(String value, String template) {
        if (value.length() != template.length())
            return false;
        for (int i = 0; i < value.length(); i++) {
            char c = template.charAt(i);
            if (c != '?')
                if (c != value.charAt(i))
                    return false;

        }
        return true;
    }

    public int length() {
        return dic.length;
    }

    /**
     * algorithm to check if value is in dic.
     * //todo optimize
     *
     * @param value
     * @return
     */
    public boolean contains(String value) {
        for (String word : dic) {
            if (word.equals(value))
                return true;
        }
        return false;
    }

    public int getNumberOfWords(String s) {
        String[] words = s.split(" ");
        int out = 0;
        for (String word : words) {
            if (contains(word))
                out++;
        }
        return out;

    }

    public String[] get() {
        return dic;
    }

    public String[] getSized(int size) {
        return sizedDics[size];
    }

    public int[] getSizedOccurences(int size) {
        return sizedDicsOcuurences[size];
    }

    public int getSizedOccurence(int size,String word){
        String[] sized = getSized(size);
        for (int i = 0; i < sized.length; i++) {
            if(sized[i].equals(word))
                return getSizedOccurences(size)[i];
        }
        return -1;
    }

    public boolean containsSized(String s) {
        String[] list = sizedDics[s.length()];
        if (list == null)
            return false;

        for (String word : list) {
            if (word.equals(s))
                return true;
        }
        return false;
    }

    public Map<Integer, Double> getLetterFrequency() {
        return letterFrequency;
    }
}
