package Model;

/**
 * The node class that makes up an ExpressionTree.
 * We make this class package visibility instead of private so that the Spreadsheet class can traverse a tree.
 * @author Dillon Crookshank
 * @author Halim Lee
 * @author Thinh Le
 */
class ExpressionTreeNode {
    /** The element of each node. */
    private final Token myToken;

    /** The left subtree reference. */
    private final ExpressionTreeNode myLeft;

    /** The right subtree reference. */
    private final ExpressionTreeNode myRight;

    /**
     * The default constructor to a node.
     */
    public ExpressionTreeNode(Token theToken, ExpressionTreeNode theLeft, ExpressionTreeNode theRight) {
        myToken = theToken;
        myLeft = theLeft;
        myRight = theRight;
    }

    /** Accessor method for the Token of the node. */
    public Token getToken() {
        return myToken;
    }

    /** Accessor method for the left subtree of the node. */
    public ExpressionTreeNode getLeft() {
        return myLeft;
    }

    /** Accessor method for the right subtree of the node. */
    public ExpressionTreeNode getRight() {
        return myRight;
    }
}
