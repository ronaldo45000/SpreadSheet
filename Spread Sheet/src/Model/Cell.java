package Model;

/**
 * A class that holds the data of a single cell of a spreadsheet.
 * @author Dillon Crookshank
 * @author Halim Lee
 * @author Thinh Le
 */
public class Cell {
    /** The expression tree representation of the formula. */
    private final ExpressionTree myFormulaTree;

    /** The raw formula of the cell. */
    private final String myFormula;

    /** The evaluated value of the formula. */
    private double myValue;

    /**
     * The lone constructor for a Cell.
     * @param theFormula The infix formula.
     * @param theTree The expression tree of the formula.
     */
    public Cell(String theFormula, ExpressionTree theTree) {
        myFormula = theFormula;
        myFormulaTree = theTree;

        myValue = 0;
    }

    /**
     * An accessor method for the value within the Cell.
     * @return The value of the formula of the cell.
     */
    public double getValue() {
        return myValue;
    }

    /**
     * An accessor method for the formula.
     * @return The infix formula.
     */
    public String getFormula() {
        return myFormula;
    }

    /**
     * Accessor method for the expression tree within the cell.
     * @return The expression tree.
     */
    public ExpressionTree getExpressionTree() {
        return myFormulaTree;
    }

    /**
     * Mutator method for the value of the cell.
     * @param theValue The new value for the cell.
     */
    public void setValue(double theValue) {
        myValue = theValue;
    }
}