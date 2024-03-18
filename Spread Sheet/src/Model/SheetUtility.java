package Model;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * A utility class that contains useful methods for throughout the Spreadsheet project
 * @author Dillon Crookshank
 * @author Halim Lee
 * @author Thinh Le
 * @version 1.0
 */
public class SheetUtility {

    /**
     * getCellToken
     * <p>
     * Assuming that the next chars in a String (at the given startIndex)
     * is a cell reference, set cellToken's column and row to the
     * cell's column and row.
     * If the cell reference is invalid, the row and column of the return CellToken
     * are both set to BadCell (which should be a final int that equals -1).
     * Also, return the index of the position in the string after processing
     * the cell reference.
     * (Possible improvement: instead of returning a CellToken with row and
     * column equal to BadCell, throw an exception that indicates a parsing error.)
     * <p>
     * A cell reference is defined to be a sequence of CAPITAL letters,
     * followed by a sequence of digits (0-9).  The letters refer to
     * columns as follows: A = 0, B = 1, C = 2, ..., Z = 25, AA = 26,
     * AB = 27, ..., AZ = 51, BA = 52, ..., ZA = 676, ..., ZZ = 701,
     * AAA = 702.  The digits represent the row number.
     *
     * @param inputString the input string
     * @param startIndex  the index of the first char to process
     * @param cellToken   a cellToken (essentially a return value)
     * @return index corresponding to the position in the string just after the cell reference
     */
    public static int getCellToken(String inputString, int startIndex, CellToken cellToken) {
        char ch;
        int column;
        int row;
        int index = startIndex;

        // handle a bad startIndex
        if ((startIndex < 0) || (startIndex >= inputString.length())) {
            throw new IllegalArgumentException("Bad startIndex");
        }

        // get rid of leading whitespace characters
        while (index < inputString.length()) {
            ch = inputString.charAt(index);
            if (!Character.isWhitespace(ch)) {
                break;
            }
            index++;
        }

        if (index == inputString.length()) {
            // reached the end of the string before finding a capital letter
            throw new IllegalArgumentException("Incomplete Cell Reference");
        }

        // ASSERT: index now points to the first non-whitespace character

        ch = inputString.charAt(index);
        // process CAPITAL alphabetic characters to calculate the column
        if (!Character.isUpperCase(ch)) {
            throw new IllegalArgumentException("Invalid or Missing column value");
        } else {
            column = ch - 'A';
            index++;
        }

        while (index < inputString.length()) {
            ch = inputString.charAt(index);
            if (Character.isUpperCase(ch)) {
                column = ((column + 1) * 26) + (ch - 'A');
                index++;
            } else {
                break;
            }
        }
        if (index == inputString.length()) {
            // reached the end of the string before fully parsing the cell reference
            throw new IllegalArgumentException("Missing row value");
        }

        // ASSERT: We have processed leading whitespace and the
        // capital letters of the cell reference

        // read numeric characters to calculate the row
        if (Character.isDigit(ch)) {
            row = ch - '0';
            index++;
        } else {
            throw new IllegalArgumentException("Invalid row value");
        }

        while (index < inputString.length()) {
            ch = inputString.charAt(index);
            if (Character.isDigit(ch)) {
                row = (row * 10) + (ch - '0');
                index++;
            } else {
                break;
            }
        }

        // successfully parsed a cell reference
        cellToken.setColumn(column);
        cellToken.setRow(row);
        return index;
    }

    /**
     * Given a CellToken, print it out as it appears on the
     * spreadsheet (e.g., "A3")
     *
     * @param cellToken a CellToken
     * @return the cellToken's coordinates
     */
    public static String getCellAddress(CellToken cellToken) {
        char ch;
        String returnString = "";
        int col;
        int largest = 26;  // minimum col number with number_of_digits digits
        int number_of_digits = 2;

        col = cellToken.getColumn();

        // compute the biggest power of 26 that is less than or equal to col
        // We don't check for overflow of largest here.
        while (largest <= col) {
            largest = largest * 26;
            number_of_digits++;
        }
        largest = largest / 26;
        number_of_digits--;

        // append the column label, one character at a time
        while (number_of_digits > 1) {
            ch = (char) (((col / largest) - 1) + 'A');
            returnString += ch;
            col = col % largest;
            largest = largest / 26;
            number_of_digits--;
        }

        // handle last digit
        ch = (char) (col + 'A');
        returnString += ch;

        // append the row as an integer
        returnString += cellToken.getRow();

        return returnString;
    }

    /**
     * Lets you check an address to a cell and returns true if it is valid.
     * @param theCell The String address that will be checked.
     * @return True if the given address is a valid cell address.
     */
    public static boolean isValidAddress(String theCell) {
        int index = 0;
        while(theCell.length() > index && theCell.charAt(index) == ' ') {
            //iterate through whitespace of address
            index++;
        }

        boolean containsLetter = false;
        while(theCell.length() > index && Character.isUpperCase(theCell.charAt(index))) {
            //iterate through first part of address
            containsLetter = true;
            index++;
        }

        boolean containsNumber = false;
        while(theCell.length() > index && Character.isDigit(theCell.charAt(index))) {
            //iterate through second part of address
            containsNumber = true;
            index++;
        }

        return containsLetter && containsNumber;

    }

    public static Stack getPostFixStack(final String theInfixString) {
        //scan through the string, and convert the formula String into an infix Queue of Tokens.
        Queue<Token> infixQueue = new ArrayDeque<>();

        int index = 0;
        //iterate through the String
        while (index < theInfixString.length()) {
            //Iterate through whitespace characters
            while (theInfixString.charAt(index) == ' ') {
                index++;
            }

            //Next character is either a Letter(CellToken), a number(LiteralToken), or a symbol(OperatorToken)
            if (Character.isUpperCase(theInfixString.charAt(index))) {
                //Get the cell token
                CellToken cell = new CellToken();
                index = getCellToken(theInfixString, index, cell);

                //Add the newly generated CellToken to the queue.
                infixQueue.add(cell);

            } else if (Character.isDigit(theInfixString.charAt(index))) {
                //Scan one character at a time and form an integer.
                int literal = 0;
                while(index < theInfixString.length() && Character.isDigit(theInfixString.charAt(index))) {
                    literal *= 10;
                    literal += theInfixString.charAt(index) - '0';
                    index++;
                }
                //Add the resulting integer to the queue as a LiteralToken.
                infixQueue.add(new LiteralToken(literal));

            } else if (isOperator(theInfixString.charAt(index))) {
                //Operator can only be a single character; Add its token to the queue.
                infixQueue.add(new OperatorToken(theInfixString.charAt(index)));
                index++;

            } else {
                throw new IllegalArgumentException("Invalid Formula Composition");
            }


        }

        //We now have the formula as a Queue of tokens, this will make the following steps simpler.
        Stack returnStack = new Stack();
        Stack operatorStack = new Stack();

        while (!infixQueue.isEmpty()) {
            //If the token is an Operand(LiteralToken/CellToken), pop and push it to the return stack
            if (infixQueue.peek() instanceof LiteralToken || infixQueue.peek() instanceof CellToken) {
                returnStack.push(infixQueue.poll());
            }

            //If the next token is a left parentheses, push it to the operator stack
            else if (((OperatorToken) infixQueue.peek()).getOperator() == '(') {
                operatorStack.push(infixQueue.poll());
            }

            //If the next token is a right parentheses, pop the operator stack into the return stack until you
            // find a left parentheses, then pop the left parentheses
            else if (((OperatorToken) infixQueue.peek()).getOperator() == ')') {
                while (((OperatorToken) operatorStack.top()).getOperator() != '(') {
                    returnStack.push(operatorStack.topAndPop());
                }
                operatorStack.pop();
                infixQueue.poll();
            }

            //If the next token is an operator(OperatorToken)...
            //While the operator stack isn't empty, and the priority of the next token is less than or equal to the
            // priority of the top of the operator stack, pop and push the operator stack into the return stack.
            else if (infixQueue.peek() instanceof OperatorToken) {
                while (!operatorStack.isEmpty() && ((OperatorToken) infixQueue.peek()).priority() <= ((OperatorToken) operatorStack.top()).priority()) {
                    returnStack.push(operatorStack.topAndPop());
                }

                operatorStack.push(infixQueue.poll());
            }
        }

        //pop and push all the remaining tokens from the operator stack into the return stack
        while (!operatorStack.isEmpty()) {
            returnStack.push(operatorStack.topAndPop());
        }

        return returnStack;
    }

    /**
     * Return true if the char ch is an operator of a formula.
     * Current operators are: +, -, *, /, ^, (, ).
     *
     * @param theChar A character.
     * @return True if theChar is an operator.
     */
    private static boolean isOperator(char theChar) {
        return ((theChar == OperatorToken.PLUS) ||
                (theChar == OperatorToken.MINUS) ||
                (theChar == OperatorToken.MULTI) ||
                (theChar == OperatorToken.DIV) ||
                (theChar == OperatorToken.LEFT_PAREN) ||
                (theChar == OperatorToken.RIGHT_PAREN) ||
                (theChar == OperatorToken.EXP));
    }
}
