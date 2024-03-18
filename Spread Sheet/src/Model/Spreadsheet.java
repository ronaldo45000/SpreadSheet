package Model;

import java.util.*;

/**
 * TODO:
 * Implement topological sort.
 * @author Dillon Crookshank
 * @author Halim Lee
 * @author Thinh Le
 * @version 0.1
 */
public class Spreadsheet {
    /** The default # of rows in a spreadsheet. */
    public static final int DEFAULT_ROWS = 10;

    /** The default # of columns in a spreadsheet. */
    public static final int DEFAULT_COLUMNS = 10;

    /** The smallest any dimension of the spreadsheet can be. */
    public static final int MINIMUM_DIMENSION = 2;

    /** The array that holds all the cells of the spreadsheet. */
    private Cell[][] myCells;

    /** Holds the # of rows in the spreadsheet currently. */
    private final int myNumRows;

    /** Holds the # of columns in the spreadsheet currently. */
    private final int myNumColumns;

    /** The # of decimal places that are used when a cell value is polled. */
    private int myDecimalPrecision;

    /** Creates an empty 10x10 spreadsheet. */
    public Spreadsheet() {
        this(DEFAULT_ROWS, DEFAULT_COLUMNS);
    }

    /**
     * Creates an empty spreadsheet using the given parameters.
     * @param theNumRows The # of rows the spreadsheet will have.
     * @param theNumColumns The # of columns the spreadsheet will have.
     * @throws IllegalArgumentException When given dimensions smaller than the specified minimum(2);
     */
    public Spreadsheet(final int theNumRows, final int theNumColumns) {
        if (theNumRows < MINIMUM_DIMENSION || theNumColumns < MINIMUM_DIMENSION) {
            throw new IllegalArgumentException("Spreadsheet too small.");
        }

        myCells = new Cell[theNumRows][theNumColumns];

        myNumRows = theNumRows;
        myNumColumns = theNumColumns;

        myDecimalPrecision = 1;
    }

    /**
     * @return The # of rows in the spreadsheet.
     */
    public int getNumRows() {
        return myNumRows;
    }

    /**
     * @return The # of columns in the spreadsheet.
     */
    public int getNumColumns() {
        return myNumColumns;
    }

    /**
     * An accessor method for getting the value of a specific cell from the sheet.
     * Used in the GUI.
     * @param theRow The row of the cell.
     * @param theColumn The column of the cell.
     * @return The value of the cell at the given coordinates, returns 0 if the cell is empty.
     */
    public String getCellValue(final int theRow, final int theColumn) {
        // Check if the given cell coordinates are valid.
        if (theRow > getNumRows() || theRow < 0
                || theColumn > getNumColumns() || theColumn < 0) {
            throw new IllegalArgumentException("Bad Cell");
        }

        return myCells[theRow][theColumn] != null ? String.format("%." + myDecimalPrecision +"f", myCells[theRow][theColumn].getValue()) : "";
    }

    /**
     * An accessor method for getting the value of a specific cell from the sheet.
     * @param theCell The Token reference to a cell.
     * @return The value of the cell at the given coordinates, returns 0 if the cell is empty.
     */
    public double getCellValue(final CellToken theCell) {
        // Check if the given cell token contains valid cell coordinates.
        if (theCell.getRow() > getNumRows() || theCell.getRow() < 0
                || theCell.getColumn() > getNumColumns() || theCell.getColumn() < 0) {
            throw new IllegalArgumentException("Bad Cell");
        }

        // Create temp variables to simplify the return statement.
        int row = theCell.getRow();
        int column = theCell.getColumn();

        return myCells[row][column] != null ? myCells[row][column].getValue() : 0;
    }

    /**
     * An accessor method for getting the formula of a specific cell from the sheet.
     * Used in the GUI.
     * @param theRow The row of the cell.
     * @param theColumn The column of the cell.
     * @return The formula of the cell at the given coordinates, returns an empty String if the cell is empty.
     */
    public String getCellFormula(final int theRow, final int theColumn) {
        // Check if the given cell coordinates are valid.
        if (theRow > getNumRows() || theRow < 0
                || theColumn > getNumColumns() || theColumn < 0) {
            throw new IllegalArgumentException("Bad Cell");
        }

        return myCells[theRow][theColumn] != null ? myCells[theRow][theColumn].getFormula() : "";
    }

    /**
     * An accessor method for getting the formula of a specific cell from the sheet.
     * Used in the GUI.
     * @param theCell The reference to the cell.
     * @return The formula of the cell at the given coordinates, returns an empty String if the cell is empty.
     */
    public String getCellFormula(final CellToken theCell) {
        return getCellFormula(theCell.getRow(), theCell.getColumn());
    }

    /**
     * Changes the given cell's formula.
     * @param theCell The reference to the cell.
     * @param theFormula The infix formula as a String.
     * @throws IllegalArgumentException When the given cell reference does not exist on the spreadsheet.
     */
    public void changeCellFormula(final CellToken theCell, final String theFormula) {
        // Check if the given cell token contains valid cell coordinates.
        if (theCell.getRow() > getNumRows() || theCell.getRow() < 0
                || theCell.getColumn() > getNumColumns() || theCell.getColumn() < 0) {
            throw new IllegalArgumentException("Cell Does not exist.");
        }

        if (theFormula.trim().equals("")) {
            myCells[theCell.getRow()][theCell.getColumn()] = null;
            return;
        }

        //Create a new expression tree based on the given formula String.
        Stack postFixFormula = SheetUtility.getPostFixStack(theFormula);
        ExpressionTree formulaTree = new ExpressionTree(postFixFormula);

        //Add the cell to the spreadsheet matrix.
        myCells[theCell.getRow()][theCell.getColumn()] = new Cell(theFormula, formulaTree);
    }

    /**
     * Sets the given cell reference to the given formula.
     * @param theCell The reference to the cell.
     * @param theFormula The infix formula as a String.
     */
    public void changeCellFormulaAndRecalculate(final CellToken theCell, final String theFormula) {
        changeCellFormula(theCell, theFormula);
        evaluateSheet();

    }

    /**
     * Resets an individual cell.
     * @param theCell The reference to a cell.
     */
    public void clearCell(final CellToken theCell) {
        myCells[theCell.getRow()][theCell.getColumn()] = null;
    }

    /** Resets all the cells within the spreadsheet. */
    public void clear() {
        myCells = new Cell[myNumRows][myNumColumns];
    }

    /**
     * An accessor method for the Decimal precision(# of decimal places used).
     * @return The current decimal precision.
     */
    public int getDecimalPrecision() {
        return myDecimalPrecision;
    }

    /**
     * A displacement based mutator method for the Decimal precision(# of decimal places used).
     * @param theDisplacement How much the displacement should change.
     */
    public void displaceDecimalPrecision(final int theDisplacement) {
        myDecimalPrecision += theDisplacement;
    }

    /**
     * An override of the toString() method.
     * Creates a multiline String that holds the dimensions and data from every non-null cell.
     * Format:
     * [rows] [columns]
     * [Cell Address] [Cell Formula]
     * ...
     * @return A String representation of the spreadsheet.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(myNumRows)
                .append(" ")
                .append(myNumColumns)
                .append("\n");

        for (int y = 0; y < myNumRows; y++) {
            for (int x = 0; x < myNumColumns; x++) {
                if (myCells[y][x] != null) {
                    result.append(SheetUtility.getCellAddress(new CellToken(y, x)))
                            .append(" ")
                            .append(myCells[y][x].getFormula())
                            .append("\n");
                }
            }
        }

        return result.toString();
    }

    /**
     * Preforms a topological sort on the cells of the Spreadsheet, and returns the result as a Queue.
     * @return A Queue of CellTokens in proper evaluation order.
     * @throws IllegalStateException When a dependency loop is found.
     */
    public Queue<CellToken> topologicalSort(final Map<CellToken, Set<CellToken>> theAdjacencyMap) {
        Queue<CellToken> evalQueue = new LinkedList<>();
        Map<CellToken, Integer> indegree = new HashMap<>();

        // Calculate indegree for each node and initialize the adjacencyMap
        for (CellToken currentCell : theAdjacencyMap.keySet()) {
            indegree.put(currentCell, 0);
        }
        for (Set<CellToken> currentCell : theAdjacencyMap.values()) {
            for (CellToken neighbor : currentCell) {
                indegree.put(neighbor, indegree.getOrDefault(neighbor, 0) + 1);
            }
        }

        // Add nodes with indegree 0 to queue
        Queue<CellToken> tempQueue = new LinkedList<>();
        for (CellToken currentCell : indegree.keySet()) {
            if (indegree.get(currentCell) == 0) {
                tempQueue.add(currentCell);
            }
        }

        // Perform topological sort
        while (!tempQueue.isEmpty()) {
            CellToken currentCell = tempQueue.poll();
            evalQueue.add(currentCell);
            for (CellToken neighbor : theAdjacencyMap.getOrDefault(currentCell, new HashSet<>())) {
                indegree.put(neighbor, indegree.get(neighbor) - 1);
                if (indegree.get(neighbor) == 0) {
                    tempQueue.add(neighbor);
                }
            }
        }

        // Check for cycle
        if (evalQueue.size() < theAdjacencyMap.size()) {
            throw new IllegalStateException("There is a cycle");
        }

        return evalQueue;
    }

    /**
     * Finds the dependencies of each cell.
     * @return A map of cell dependencies.
     */
    private Map<CellToken, Set<CellToken>> getAdjacencyMap() {
        Map<CellToken, Set<CellToken>> result = new HashMap<>();

        //Initialize the adjacencyMap
        for (int y = 0; y < myNumRows; y++) {
            for (int x = 0; x < myNumColumns; x++) {
                result.put(new CellToken(y, x), new HashSet<>());
            }
        }

        //Fill the adjacencyMap
        for (int y = 0; y < myNumRows; y++) {
            for (int x = 0; x < myNumColumns; x++) {
                if (myCells[y][x] != null) {
                    HashSet<CellToken> dependencies = new HashSet<>();
                    findDependencies(myCells[y][x].getExpressionTree().getRoot(), dependencies);
                    CellToken cell = new CellToken(y, x);

                    for (CellToken neighbor : dependencies) {
                        result.get(neighbor).add(cell);
                    }
                }
            }
        }

        return result;
    }

    /**
     * Helper method that recursively traverses an expression tree to look for cell dependencies.
     * @param root The root of the expression tree
     * @param theDependencies The set that the dependencies should be placed in.
     */
    private void findDependencies(final ExpressionTreeNode root, Set<CellToken> theDependencies) {
        if (root.getToken() instanceof CellToken) {
            theDependencies.add((CellToken) root.getToken());
        } else if (root.getToken() instanceof OperatorToken) {
            findDependencies(root.getLeft(), theDependencies);
            findDependencies(root.getRight(), theDependencies);
        }
    }

    /**
     * Calls getEvalQueue() and uses the resulting Queue to evaluate every cell in proper order.
     */
    public void evaluateSheet() {
        Queue<CellToken> evalQueue = topologicalSort(getAdjacencyMap());

        while(!evalQueue.isEmpty()) {
            evaluateCell(evalQueue.poll());
        }
    }

    /**
     * Changes the value field of a cell based on the result of evaluating its expressionTree.
     * @param theCell The cell to be evaluated.
     * @throws IllegalArgumentException When the given cell reference does not exist on the spreadsheet.
     */
    private void evaluateCell(final CellToken theCell) {
        // Check if the given cell token contains valid cell coordinates.
        if (theCell.getRow() > getNumRows() || theCell.getRow() < 0
                || theCell.getColumn() > getNumColumns() || theCell.getColumn() < 0) {
            throw new IllegalArgumentException("Cell Does not exist.");
        }

        if (myCells[theCell.getRow()][theCell.getColumn()] == null) {
            return;
        }

        //Get the expression tree from the cell and put its evaluated value back into the cell.
        Cell cell = myCells[theCell.getRow()][theCell.getColumn()];
        ExpressionTree formulaTree = cell.getExpressionTree();

        cell.setValue(evaluateTree(formulaTree.getRoot()));
    }

    /**
     * Recursively traverses an expression tree and evaluates it.
     * this evaluation must be done in the Spreadsheet class, since we need access to the values of other cells.
     * @param theRoot The root node of the expression tree.
     * @return The value of the expression tree.
     */
    private double evaluateTree(final ExpressionTreeNode theRoot) {
        //First 2 if-statements are base cases.
        //If the node is a cell or a literal, then it must be a leaf.
        if (theRoot.getToken() instanceof CellToken) {
            return getCellValue((CellToken) theRoot.getToken());

        } else if (theRoot.getToken() instanceof LiteralToken) {
            return ((LiteralToken) theRoot.getToken()).getLiteral();

        } else { //apply the operator to the values of its subtrees
            switch (((OperatorToken) theRoot.getToken()).getOperator()) {
                case OperatorToken.PLUS -> { return evaluateTree(theRoot.getLeft()) + evaluateTree(theRoot.getRight()); }
                case OperatorToken.MINUS -> { return evaluateTree(theRoot.getLeft()) - evaluateTree(theRoot.getRight()); }
                case OperatorToken.MULTI -> { return evaluateTree(theRoot.getLeft()) * evaluateTree(theRoot.getRight()); }
                case OperatorToken.DIV -> { return evaluateTree(theRoot.getLeft()) / evaluateTree(theRoot.getRight()); }
                case OperatorToken.EXP -> { return Math.pow(evaluateTree(theRoot.getLeft()), evaluateTree(theRoot.getRight())); }
            }
        }

        //Should not reach this point
        throw new IllegalArgumentException("Bad Expression Tree");
    }
}
