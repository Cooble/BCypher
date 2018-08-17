package cs.cooble.cypher;

import com.sun.istack.internal.Nullable;
import cs.cooble.dictionary.Dictionary;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static cs.cooble.cypher.SymbolCypher.*;

/**
 * Created by Matej on 5.8.2018.
 * Is able to decypher any cypher with each letter replaced with some other letter.
 * i.e. "abcd" could mean "heart"
 * uses whole english dictionary
 * the longer the cypher the better and faster guesses are (at certain point is better not to add more words as this can contrary slow process down)
 * if word from cypher is not contained ín dictionary, Decypher won't probably ever finish decoding
 *
 * to decypher such a cypher one must replace each strange character with any letter from normal alphabet (better to write this translation down before giving it to the program)
 */
public class SymbolDec {

    private final Dictionary dictionary;
    private final Alphabet alphabet;
    private final Alphabet symbolAlphabet;

    /**
     * @param dictionary
     * @param alphabet       default english alphabet
     * @param symbolAlphabet characters of foreign alphabet (no need to be sorted to match {@code alphabet})
     */
    public SymbolDec(Dictionary dictionary, Alphabet alphabet, Alphabet symbolAlphabet) {
        this.dictionary = dictionary;
        this.alphabet = alphabet;
        this.symbolAlphabet = symbolAlphabet;
    }

    private String cypher;

    private Map<Integer, Integer> bestKey = new HashMap<>();

    public String[] decypher(String cypher) {
        String oldCypher = cypher;
        cypher = prepare(cypher);
        this.cypher = cypher;
        String[] wordsArray = toWords(cypher);
        Map<Integer, Double> mostUsed = getUseFrequency(cypher);
        Map<Integer, Double> englishUsed = dictionary.getLetterFrequency();

        List<Integer> sortedMostUsed = sort(mostUsed);
        List<Integer> sortedEnglishUsed = sort(englishUsed);

        Map<Integer, Integer> key = new HashMap<>();
        prepareKey(key, sortedMostUsed, sortedEnglishUsed);
        List<Word> words = buildWords(wordsArray, key);
        sortByGuesses(words);

        proccesWord(words, 0, key);
        List<String> wordProbabilities = new ArrayList<>();
        for (int i = 0; i < keys.size(); i++) {
            Map<Integer, Integer> key2 = keys.get(i);
            long prob = 0;
            for (Word w : words) {
                prob += dictionary.getSizedOccurence(w.word.length(), w.decode(key2));
            }
            wordProbabilities.add(prob + " " + i);
        }
        wordProbabilities.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                long prob1 = Long.parseLong(o1.split(" ")[0]);
                long prob2 = Long.parseLong(o2.split(" ")[0]);
                if (prob1 > prob2)
                    return 1;
                else if (prob1 == prob2)
                    return 0;
                else return -1;
            }
        });
        List<Integer> indexes = new ArrayList<>();
        final long[] maxVal = {0};
        final int[] maxIndex = {0};
        wordProbabilities.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                indexes.add(Integer.parseInt(s.split(" ")[1]));
                long max = Long.parseLong(s.split(" ")[0]);
                if (max > maxVal[0]) {
                    maxIndex[0] = Integer.parseInt(s.split(" ")[1]);
                    maxVal[0] = max;
                }

            }
        });
        String[] out = new String[keys.size()];

        System.out.println("Possible solutions");
        for (int i = 0; i < indexes.size(); i++) {
            Map<Integer, Integer> key1 = keys.get(indexes.get(i));
            System.out.println("* " + useKey(key1, cypher) + " " + wordProbabilities.get(i));
            out[i] = useKey(key1, oldCypher);
        }
        if (keys.size() != 0)
            bestKey = keys.get(maxIndex[0]);
        return out;
    }

    public Map<Integer, Integer> getBestKey() {
        return bestKey;
    }

    private static final int MAX_GUESS_SIZE = 5000;

    private void proccesWord(List<Word> words, int wordIndex, Map<Integer, Integer> key) {
        Word word = words.get(wordIndex);
        word.recalculateAllGuesses(key);
        Map<Integer, Integer> bufferKey = new HashMap<>();
        for (int guessNumber = 0; guessNumber < Math.min(MAX_GUESS_SIZE, word.guessSize()); guessNumber++) {
            bufferKey = copyKey(key, bufferKey);
            word.alterKeyToNextWord(guessNumber, bufferKey);

            //sorting words to the right
            List<Word> sortedSubList = words.subList(wordIndex + 1, words.size());
            for (Word w : sortedSubList)
                w.recalculateAllGuesses(bufferKey);
            Collections.sort(sortedSubList);

            for (int i = 0; i < sortedSubList.size(); i++) {//sort words on the right side
                words.set(i + wordIndex + 1, sortedSubList.get(i));
            }

            if (wordIndex != words.size() - 1)
                proccesWord(words, wordIndex + 1, bufferKey);
            else {//we are at the end of the time itself (maybe just at the end of words but whatever)
                registerPossibleKey(bufferKey);
            }
        }

    }

    private List<Map<Integer, Integer>> keys = new ArrayList<>();
    private long out = 0;
    private long size = 0;

    private void registerPossibleKey(Map<Integer, Integer> key) {
        out++;
        if (out % 100 == 0)
            System.out.println(useKey(key, cypher) + " " + (size + 1));
        keys.add(key);
        size++;
    }

    private Map<Integer, Integer> copyKey(Map<Integer, Integer> key, @Nullable Map<Integer, Integer> keyToWaste) {
        Map<Integer, Integer> out = keyToWaste == null ? new HashMap<>() : keyToWaste;
        out.clear();
        key.forEach(new BiConsumer<Integer, Integer>() {
            @Override
            public void accept(Integer integer, Integer integer2) {
                out.put(integer, integer2);
            }
        });
        return out;
    }

    private void sortByGuesses(List<Word> words) {
        Collections.sort(words);
    }

    private void prepareKey(Map<Integer, Integer> key, List<Integer> mostUsed, List<Integer> englishUsed) {
       /* for (int i = 0; i < englishUsed.size(); i++) {
            key.put(mostUsed.get(i), englishUsed.get(i));
        }*/
        for (Integer aMostUsed : mostUsed) {//means it can be any character
            key.put(aMostUsed, -1);
        }
    }


    private List<Word> buildWords(String[] words, Map<Integer, Integer> key) {
        List<Word> out = new ArrayList<>();
        for (String word : words) {
            out.add(new Word(word, dictionary, alphabet, key));
            out.get(out.size() - 1).recalculateAllGuesses(key);
        }
        return out;
    }


    private static List<Integer> sort(Map<Integer, Double> mostUsed) {
        List<Double> sortedMostUsedVal = makeList(mostUsed);
        Collections.sort(sortedMostUsedVal);
        List<Double> flipped = new ArrayList<>();
        for (int i = 0; i < sortedMostUsedVal.size(); i++) {
            flipped.add(sortedMostUsedVal.get(sortedMostUsedVal.size() - 1 - i));
        }
        sortedMostUsedVal = flipped;

        List<Integer> out = new ArrayList<>();
        for (Double sortedVal : sortedMostUsedVal) {
            final double val = sortedVal;
            mostUsed.forEach((integer, aDouble) -> {
                if (aDouble == val)
                    out.add(integer);
            });
        }

        return out;

    }

    private static List<Double> makeList(Map<Integer, Double> map) {
        List<Double> out = new ArrayList<>();
        map.forEach(new BiConsumer<Integer, Double>() {
            @Override
            public void accept(Integer integer, Double aDouble) {
                out.add(aDouble);
            }
        });
        return out;
    }

    /**
     * @param cypher
     * @return normalized frequency for each of character used
     */
    private Map<Integer, Double> getUseFrequency(String cypher) {
        Map<Integer, Double> out = new HashMap<>();
        for (int i = 0; i < cypher.length(); i++) {
            char c = cypher.charAt(i);
            if (!out.containsKey((int) c))
                out.put((int) c, (double) 1);
            else out.put((int) c, out.get((int) c) + 1);
        }
        final double[] sum = {0};
        out.forEach(new BiConsumer<Integer, Double>() {
            @Override
            public void accept(Integer integer, Double aDouble) {
                sum[0] += aDouble;
            }
        });
        out.replaceAll(new BiFunction<Integer, Double, Double>() {
            @Override
            public Double apply(Integer integer, Double aDouble) {
                return aDouble / sum[0];
            }
        });
        return out;

    }


    private static class Word implements Comparable<Word> {
        final String word;
        final Map<Integer, Integer> key;
        String[] guesses;
        Map<String, Double> probability;
        Alphabet alphabet;

        int currentWordIndex;

        int guessSize;
        List<String> extractedGuesses = new ArrayList<>();

        Word(String word, Dictionary dictionary, Alphabet alphabet, Map<Integer, Integer> key) {
            this.word = word;
            this.key = key;
            this.alphabet = alphabet;
            guesses = filterDoubles(dictionary.getSized(word.length()), word);
            probability = new HashMap<>();
        }

        int guessSize() {
            return guessSize;
        }

        int currentWordIndex() {
            return currentWordIndex;
        }

        /**
         * alter key by adding translations from word in list specified by index
         *
         * @param nextWord  index from 0 to {@code guessSize()}
         * @param bufferKey
         */
        void alterKeyToNextWord(int nextWord, Map<Integer, Integer> bufferKey) {
            currentWordIndex = nextWord;
            String guess = extractedGuesses.get(nextWord);
            for (int i = 0; i < guess.length(); i++) {
                int guessChar = guess.charAt(i);
                int wordChar = word.charAt(i);
                bufferKey.put(wordChar, guessChar);
            }
        }

        /**
         * creates new list containing all possible words for the key
         */
        void recalculateAllGuesses(Map<Integer, Integer> key) {
            extractedGuesses.clear();
            guessSize = 0;
            for (String guess : guesses) {
                boolean matches = true;
                for (int charIndex = 0; charIndex < guess.length(); charIndex++) {//check if word from dictionary matches the partial key (if val is -1 then its ignored)
                    int guessChar = guess.charAt(charIndex);
                    int symbolChar = word.charAt(charIndex);
                    int keyChar = key.get(symbolChar);
                    if (keyChar != -1) {
                        if (keyChar != guessChar) {
                            matches = false;
                            break;
                        }
                    }
                }
                if (matches) {
                    guessSize++;
                    extractedGuesses.add(guess);
                }
            }
        }


        String decode(Map<Integer, Integer> key) {
            return useKey(key, word);
        }

        @Override
        public String toString() {
            return word;
        }

        @Override
        public int compareTo(Word o) {
            return guessSize - o.guessSize;
        }
    }

    /**
     * filters words which have or not have more letters in it respectively
     * example word "cool" can have template "xppw" but definitely not "afds"
     *
     * @param src
     * @return
     */
    private static String[] filterDoubles(String[] src, String template) {
        List<String> out = new ArrayList<>();
        for (String s : src) {
            if (matchTemplate(s, template))
                out.add(s);
        }
        return out.toArray(new String[out.size()]);
    }

    /**
     * filters words which have or not have more letters in it respectively
     * example word "cool" can have template "xppw" but definitely not "afds"
     *
     * @param src
     * @return probabilities of those words occuring in the text
     */
    private static Integer[] filterDoublesProbabilites(String[] src, int[] probabilites, String template) {
        assert src.length == probabilites.length;
        List<Integer> out = new ArrayList<>();
        for (int i = 0; i < src.length; i++) {
            String s = src[i];
            if (matchTemplate(s, template))
                out.add(probabilites[i]);
        }
        return out.toArray(new Integer[out.size()]);
    }

    /**
     * filters words which have or not have more letters in it respectively
     * example word "cool" can have template "xppw" but definitely not "afds"
     */
    public static boolean matchTemplate(String src, String template) {
        if (src.length() != template.length())
            return false;
        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);
            char templateC = template.charAt(i);

            int index = 0;
            int nextOccurIndex;
            while ((nextOccurIndex = template.indexOf(templateC, index)) != -1) {
                if (c != src.charAt(nextOccurIndex))
                    return false;
                index = nextOccurIndex + 1;
            }
        }
        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);
            char templateC = template.charAt(i);

            int index = 0;
            int nextOccurIndex;
            while ((nextOccurIndex = src.indexOf(c, index)) != -1) {
                if (templateC != template.charAt(nextOccurIndex))
                    return false;
                index = nextOccurIndex + 1;
            }
        }
        return true;
    }

}
