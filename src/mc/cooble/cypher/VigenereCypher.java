package mc.cooble.cypher;


public class VigenereCypher implements Cypher, Keyable {
    private Alphabet abc;
    private String key = "KEY";

    /**
     * R0 -> 'a'->'a'
     * R1 -> 'a'->'b'
     */
    private boolean R1;
    private boolean decypher;

    private CaesarCypher caesar;

    public VigenereCypher(Alphabet abc) {
        this.abc = abc;
        caesar = new CaesarCypher(abc);
    }

    @Override
    public void setKey(String key) {
        if (key != null)
            this.key = key.toUpperCase();
    }

    @Override
    public String cypher(String input) {
        input = input.toUpperCase();
        int keyIndexfromi = 0;
        char[] output = new char[input.length()];
        for (int i = 0; i < input.length(); i++) {
            int symbol = input.charAt(i);
            if (abc.indexOf(symbol) != -1) {
                int keyIndex = keyIndexfromi % key.length();
                int shift = abc.indexOf(key.charAt(keyIndex));
                if (R1)
                    shift += 1;
                caesar.setShift(decypher?-shift:shift);
                output[i] = caesar.cypher((char) symbol + "").charAt(0);
                keyIndexfromi++;
            } else output[i] = (char) symbol;
        }

        return new String(output);
    }

    @Override
    public String decypher(String input) {
        input = input.toUpperCase();
        int keyIndexfromi = 0;
        char[] output = new char[input.length()];
        for (int i = 0; i < input.length(); i++) {
            int symbol = input.charAt(i);
            if (abc.indexOf(symbol) != -1) {
                int keyIndex = keyIndexfromi % key.length();
                int shift = abc.indexOf(key.charAt(keyIndex));
                if (R1)
                    shift += 1;
                caesar.setShift(-shift);
                output[i] = caesar.cypher((char) symbol + "").charAt(0);
                keyIndexfromi++;
            } else output[i] = (char) symbol;
        }

        return new String(output);
    }

    @Override
    public String[] getAttributesNames() {
        return new String[]{"key", "R-1","decypher"};
    }

    @Override
    public String[] getAttributes() {
        return new String[]{key, R1 + "",""+decypher};
    }

    @Override
    public void setAttributes(String[] attributes) {
        if(attributes[0].length()==1)
            attributes[0]+=attributes[0];
        setKey(attributes[0].toUpperCase());
        if(attributes[1].equals("1"))
            R1=true;
        else
            R1 = Boolean.parseBoolean(attributes[1]);

        if(attributes[2].equals("1"))
            decypher=true;
        else
            decypher = Boolean.parseBoolean(attributes[2]);
    }

    @Override
    public String toString() {
        return "Vigenere cypher";
    }
}
