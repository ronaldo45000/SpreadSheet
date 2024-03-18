package Model;

/**
 * An OperatorToken stores a single operator and can tell you its own priority.
 * @author Dillon Crookshank
 * @author Halim Lee
 * @author Thinh Le
 * @version 1.0
 */
public class OperatorToken extends Token {
    /** The addition operator. */
    public static final char PLUS = '+';

    /** The priority of the addition operator. */
    public static final int PLUS_PRIORITY = 0;

    /** The subtraction operator. */
    public static final char MINUS = '-';

    /** The priority of the subtraction operator. */
    public static final int MINUS_PRIORITY = 0;

    /** The multiplication operator. */
    public static final char MULTI = '*';

    /** The priority of the multiplication operator. */
    public static final int MULTI_PRIORITY = 1;

    /** The division operator. */
    public static final char DIV = '/';

    /** The priority of the division operator. */
    public static final int DIV_PRIORITY = 1;

    /** The left parentheses' operator. */
    public static final char LEFT_PAREN = '(';

    /** The right parentheses' operator. */
    public static final char RIGHT_PAREN = ')';

    /** The priority of both the parentheses operators. */
    public static final int PAREN_PRIORITY = -1;

    /** The exponent operator. */
    public static final char EXP = '^';

    /** The priority of the exponent operator. */
    public static final int EXP_PRIORITY = 2;

    /** The operator stored in the token. */
    private char myOperator;

    /** The default constructor. Creates an empty OperatorToken. */
    public OperatorToken() {}

    /**
     * The parameterized constructor
     * @param theOperator The operator to be stored in the token.
     */
    public OperatorToken(char theOperator) {
        setOperator(theOperator);
    }

    /**
     * Accessor method for the operator stored in the token.
     * @return The operator within the token.
     */
    public char getOperator()
    {
        return myOperator;
    }

    /**
     * Mutator method for the operator within the token.
     * @param theOperatorToken What the operator should be set to.
     */
    public void setOperator(char theOperatorToken) {
        myOperator = theOperatorToken;
    }

    /**
     * Return the priority of this OperatorToken.
     * priorities:
     *   +, - : 0
     *   *, / : 1
     *   ^    : 2
     *   (, ) : -1
     *
     * @return  the priority of operatorToken
     */
    int priority() {
        switch (myOperator) {
            case PLUS -> { return PLUS_PRIORITY; }

            case MINUS -> { return MINUS_PRIORITY; }

            case MULTI -> { return MULTI_PRIORITY; }

            case DIV -> { return DIV_PRIORITY; }

            case EXP -> { return EXP_PRIORITY; }

            case LEFT_PAREN, RIGHT_PAREN -> { return PAREN_PRIORITY; }

            default -> throw new IllegalArgumentException("Error Case In OperatorToken.priority()");

        }
    }

    /**
     * An override of toString() that makes a String in the format: {operator}.
     * @return A String representation of an OperatorToken.
     */
    public String toString() {
        return String.format("{%c}", myOperator);
    }
}
