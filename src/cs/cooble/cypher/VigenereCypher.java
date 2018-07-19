package cs.cooble.cypher;


public class VigenereCypher implements Cypher {
    private Alphabet abc;
    private String key;

    /**
     * R0 -> 'a'->'a'
     * R1 -> 'a'->'b'
     */
    private boolean R1;

    private CaesarCypher caesar;

    public VigenereCypher(Alphabet abc) {
        this.abc = abc;
        caesar = new CaesarCypher(abc);
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String cypher(String input) {
        char[] output = new char[input.length()];
        for (int i = 0; i < input.length(); i++) {
            int symbol = input.charAt(i);
            if (abc.indexOf(symbol) != -1) {
                int keyIndex = i % key.length();
                int shift = abc.indexOf(abc.abc[keyIndex]);
                if (R1)
                    shift += 1;
                caesar.setShift(shift);
                output[i] = caesar.cypher((char) symbol + "").charAt(0);
            } else output[i] = (char) symbol;
        }

        return new String(output);
    }

    @Override
    public String decypher(String input) {
        char[] output = new char[input.length()];
        for (int i = 0; i < input.length(); i++) {
            int symbol = input.charAt(i);
            if (abc.indexOf(symbol) != -1) {
                int keyIndex = i % key.length();
                int shift = abc.indexOf(abc.abc[keyIndex]);
                caesar.setShift(-shift);
                output[i] = caesar.cypher((char) symbol + "").charAt(0);
            } else output[i] = (char) symbol;
        }

        return new String(output);
    }
}
