package Model;

/**
 * A Linked List implementation of a Stack, used for storing expression tokens.
 * @author Dillon Crookshank
 * @author Halim Lee
 * @author Thinh Le
 * @version 1.0
 */
public class Stack {
    /** The top node of the stack. */
    private Node myTop;

    /** A Linked List node that holds a Token. */
    private static class Node {
        /** The token of the node. */
        Token myToken;

        /** The reference to the next node in the list. */
        Node myNext;

        /** The default constructor for a node. */
        public Node(Token theToken, Node theNext) {
            myToken = theToken;
            myNext = theNext;
        }
    }

    /** Creates a new empty stack. */
    public Stack() {
        myTop = null;
    }

    /**
     * A method that checks if the stack is empty or not.
     * @return True if the stack is empty.
     */
    public boolean isEmpty() {
        return myTop == null;
    }

    /**
     * @return The Token from the top of the stack.
     */
    public Token top() {
        if (isEmpty()) return null;
        return myTop.myToken;
    }

    /**
     * Adds a new Token to the top of the stack.
     * If the given Token is null, it will not be added to the stack.
     * @param theToken The token to be added to the stack.
     */
    public void push(Token theToken) {
        if (theToken == null) return;
        myTop = new Node(theToken, myTop);
    }

    /**
     * Returns the Token from the top of the stack, then pops the stack.
     * If the stack is already empty, nothing will happen and the method will return null.
     * @return The Token from the top of the stack.
     */
    public Token topAndPop() {
        if (isEmpty()) return null;

        Token temp = top();
        pop();
        return temp;
    }

    /**
     * Pops the top element from the stack.
     * If the stack is already empty, nothing will happen.
     */
    public void pop() {
        if (isEmpty()) return;

        myTop = myTop.myNext;
    }

    /** Deletes every token from the stack. */
    public void makeEmpty() {
        myTop = null;
    }

    /** Creates a String representation of the entire stack. */
    public String toString() {
        if (isEmpty()) return "{}";

        StringBuilder sb = new StringBuilder();
        Node current = myTop;
        sb.append("{");
        sb.append(current.myToken);
        current = current.myNext;
        while (current != null) {
            sb.append(", ").append(current.myToken);
            current = current.myNext;
        }
        sb.append("}");

        return sb.toString();
    }
}
