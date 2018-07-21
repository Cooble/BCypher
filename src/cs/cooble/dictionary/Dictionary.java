package cs.cooble.dictionary;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Matej on 20.7.2018.
 */
public class Dictionary {
    private String[] dic = new String[0];

    public void load(InputStream input) {
        ArrayList<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            reader.lines().forEach((e) -> list.add(e.toUpperCase()));
            reader.close();
        } catch (Exception ignored) {

        }
        dic = toArray(list);
    }

    private String[] toArray(List<String> list) {
        String[] a = new String[list.size()];
        for (int i = 0; i < a.length; i++) {
            a[i] = list.get(i);
        }
        return a;
    }

    public String get(int index) {
        if (index < 0 ||index > dic.length - 1)
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
            if (c != '*') {
                if (c != value.charAt(i))
                    return false;
            }
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
    public boolean contains(String value){
        for (String word : dic) {
            if (word.equals(value))
                return true;
        }
        return false;
    }

    public int getNumberOfWords(String s) {
        String[] words = s.split(" ");
        int out = 0;
        for(String word:words){
            if(contains(word))
                out++;
        }
        return out;

    }
}
