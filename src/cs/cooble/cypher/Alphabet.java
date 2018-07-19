package cs.cooble.cypher;


public class Alphabet {
    public final int[] abc;
    public final int length;

    public Alphabet(int[] abc) {
        this.abc = abc;
        length=abc.length;
    }
    public Alphabet(){
        abc = new int[26];
        for (int i = 0; i < 26; i++) {
            abc[i] = (char) ((int) 'A' + i);
        }
        length=abc.length;
    }

    public int indexOf(int symbol){
        for (int i = 0; i < abc.length; i++) {
            if(symbol==abc[i])
                return i;
        }
        return -1;
    }

}
