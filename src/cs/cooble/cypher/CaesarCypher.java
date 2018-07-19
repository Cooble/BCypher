package cs.cooble.cypher;

public class CaesarCypher implements Cypher {

    private Alphabet abc;
    private int shift;

    public CaesarCypher(Alphabet abc) {
        this.abc = abc;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }

    @Override
    public String cypher(String input) {
        input = input.toUpperCase();
        char[] output = new char[input.length()];
        for (int i = 0; i < input.length(); i++) {
            int symbol = input.charAt(i);
            int index = abc.indexOf(symbol);
            if (index != -1) {
                index += shift;
                if (index > abc.length - 1)
                    index -= abc.length;
                else if (index < 0)
                    index += abc.length;
                output[i] = (char) abc.abc[index];
            } else
                output[i] = (char) symbol;

        }
        return new String(output);
    }

    @Override
    public String decypher(String input) {
        setShift(-shift);
        String out = cypher(input);
        setShift(-shift);
        return out;

    }
}
