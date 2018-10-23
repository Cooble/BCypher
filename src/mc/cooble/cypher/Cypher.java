package mc.cooble.cypher;


public interface Cypher {

    String cypher(String input);

    String decypher(String input);

    String[] getAttributes();
    String[] getAttributesNames();
    void setAttributes(String[] attributes);
}
