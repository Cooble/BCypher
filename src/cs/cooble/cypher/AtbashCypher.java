package cs.cooble.cypher;


public class AtbashCypher implements Cypher {

    private Alphabet abc;

    public AtbashCypher(Alphabet abc) {
        this.abc = abc;
    }


    @Override
    public String cypher(String input) {
        input=input.toUpperCase();
        char[] out =new char[input.length()];
        for (int i = 0; i < input.length(); i++) {
            int symbol = input.charAt(i);
            int index = abc.indexOf(symbol);
            if(index!=-1) {
                index = abc.length - 1 - index;
                out[i] = (char) abc.abc[index];
            }else
            out[i]= (char) symbol;
        }
        return new String(out);
    }


    @Override
    public String decypher(String input) {
        return cypher(input);
    }
}
