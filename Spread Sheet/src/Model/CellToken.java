package Model;

import java.util.Objects;

/**
 * A CellToken is a reference to a cell within the spreadsheet.
 * Contains a row value and a column value.
 * @author Dillon Crookshank
 * @author Halim Lee
 * @author Thinh Le
 * @version 1.0
 */
public class CellToken extends Token {

    /** Field that holds the row value. */
    private int myRow;

    /** Field that holds the column value. */
    private int myColumn;

    /** The default constructor. Creates an empty CellToken. */
    public CellToken() {}

    /**
     * Creates a cell token with the given coordinates
     * @param theRow The row of the cell.
     * @param theColumn The column of the cell.
     */
    public CellToken(final int theRow, final int theColumn) {
        myRow = theRow;
        myColumn = theColumn;
    }

    /** An accessor method for the row value. */
    public int getRow() {
        return myRow;
    }

    /** An accessor method for the column value. */
    public int getColumn() {
        return myColumn;
    }

    /** A mutator method for the row value. */
    public void setRow(int theRow) {
        myRow = theRow;
    }

    /** A mutator method for the column value. */
    public void setColumn(int theColumn) {
        myColumn = theColumn;
    }

    /**
     * Override of toString() that shows a CellToken in the format: {row, column}
     * @return A String representation of a CellToken
     */
    @Override
    public String toString() {
        return SheetUtility.getCellAddress(this);
    }

    @Override
    public boolean equals(final Object theToken) {
        if (!(theToken instanceof CellToken)) return false;
        return (this.getRow() == ((CellToken) theToken).getRow())
                && (this.getColumn() == ((CellToken) theToken).getColumn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getRow(), this.getColumn());
    }
}
