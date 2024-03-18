package Model;

/**
 * This class stores an immutable expression tree, so that a formula can be easily evaluated.
 * @author Dillon Crookshank
 * @author Halim Lee
 * @author Thinh Le
 * @version 1.0
 */
class ExpressionTree {
    /** The root node of the tree. */
    private final ExpressionTreeNode myRoot;

    /**
     * This constructor generates an expression tree based on the given infix formula.
     * @param theFormula The postFix stack generated from SheetUtility.getPostFixStack(...);
     * */
    public ExpressionTree(Stack theFormula) {
        myRoot = createExpressionTree(theFormula);
    }

    /**
     * Accessor method for the root of the tree.
     * @return The root of the tree.
     */
    public ExpressionTreeNode getRoot() {
        return myRoot;
    }

    /**
     * A helper method that creates an expression tree based on the given postFix formula Stack.
     * @param theFormula The postfix formula Stack.
     * @return the Root of the new ExpressionTree.
     */
    private ExpressionTreeNode createExpressionTree(Stack theFormula) {
        ExpressionTreeNode returnTree;
        Token token;

        if (theFormula.isEmpty())
            return null;

        token = theFormula.topAndPop();  // need to handle stack underflow
        if ((token instanceof LiteralToken) ||
                (token instanceof CellToken) ) {

            // Literals and Cells are leaves in the expression tree
            returnTree = new ExpressionTreeNode(token, null, null);
            return returnTree;

        } else if (token instanceof OperatorToken) {
            // Continue finding tokens that will form the
            // right subtree and left subtree.
            ExpressionTreeNode rightSubtree = createExpressionTree(theFormula);
            ExpressionTreeNode leftSubtree  = createExpressionTree(theFormula);
            returnTree = new ExpressionTreeNode(token, leftSubtree, rightSubtree);
            return returnTree;
        }

        //The method shouldn't reach this point.
        return null;
    }
}