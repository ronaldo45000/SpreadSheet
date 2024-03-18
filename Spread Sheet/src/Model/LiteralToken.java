package Model;

/**
 * A token that holds a Literal (a number constant).
 * @author Dillon Crookshank
 * @author Halim Lee
 * @author Thinh Le
 * @version 1.0
 */
public class LiteralToken extends Token {

    /** Private field for holding the literal value. */
    private int myLiteral;

    /** The default constructor. Creates an empty LiteralToken. */
    public LiteralToken() {}

    /**
     * The lone constructor for a LiteralToken
     * @param theLiteral What the literal value is set to.
     */
    public LiteralToken(int theLiteral) {
        myLiteral = theLiteral;
    }

    /**
     * An accessor for the value field.
     * @return The value of the literal.
     */
    public int getLiteral() {
        return myLiteral;
    }

    /**
     * A mutator for the value field.
     * @param theLiteral The new value for myValue.
     */
    public void setLiteral(int theLiteral) {
        myLiteral = theLiteral;
    }

    /**
     * An override of toString() that makes a String in the format: {literal}
     * @return A String representation of a LiteralToken
     */
    @Override
    public String toString() {
        return String.format("{%d}", myLiteral);
    }
}
