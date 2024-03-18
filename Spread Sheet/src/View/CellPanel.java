package View;

import Model.CellToken;
import Model.SheetUtility;
import Model.Spreadsheet;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;
/**
 * The Cell Panel. Can generate a grid of cells of any positive dimensions.
 * @author Dillon Crookshank
 * @author Halim Lee
 * @author Thinh Le
 * @version 1.0
 */
public class CellPanel extends JPanel {
	/** The GridBagConstraints used to add every component. */
	private final GridBagConstraints myGBC;

	/** A matrix of references to text fields. */
	private final JTextField[][] myCells;

	/** The default constructor. Creates a sheet based on the default dimensions from the Spreadsheet class. */
	public CellPanel() {
		this(Spreadsheet.DEFAULT_ROWS, Spreadsheet.DEFAULT_COLUMNS);
	}

	/**
	 * Creates a spreadsheet panel with custom dimensions.
	 * @param theRows The # of rows in the spreadsheet.
	 * @param theColumns The # of columns in the spreadsheet.
	 * @throws IllegalArgumentException When the given dimensions are below the minimum defined in Model/Spreadsheet.java.
	 */
	public CellPanel(final int theRows, final int theColumns) {
		if (theRows < Spreadsheet.MINIMUM_DIMENSION || theColumns < Spreadsheet.MINIMUM_DIMENSION) {
			throw new IllegalArgumentException("Dimensions too small.");
		}

		//Initialize the private fields.
		myCells = new JTextField[theRows][theColumns];
		myGBC = new GridBagConstraints();

		//Initialize the layout
		setLayout(new GridBagLayout());

		//Create the column labels.
		for (int j = 0; j < theColumns; j++) {
			myGBC.gridx = j + 1;
			myGBC.gridy = 0;

			//Use existing code to generate column labels.
			String columnAddress = SheetUtility.getCellAddress(new CellToken(0, j));
			columnAddress = columnAddress.substring(0, columnAddress.length() - 1);

			JLabel label = new JLabel(columnAddress);
			label.setHorizontalAlignment(JLabel.CENTER);
			this.add(label, myGBC);
		}

		//Create the row labels.
		for (int j = 0; j < theRows; j++) {
			myGBC.gridx = 0;
			myGBC.gridy = j + 1;
			JLabel label = new JLabel(j + "  ");
			label.setHorizontalAlignment(JLabel.CENTER);
			this.add(label, myGBC);
		}

		//Create the grid of cells and place it on the panel.
		myGBC.gridx = 1;
		myGBC.gridy = 1;
		this.add(innerGrid(theRows, theColumns), myGBC);
	}

	/**
	 * Lets you change the text of a specific cell's text field.
	 * @param theText The new String to be in the cell.
	 * @param theX The x coordinate of the cell.
	 * @param theY The y coordinate of the cell.
	 * @throws IllegalArgumentException If the given coordinates do not fall within the grid.
	 */
	public void setCell(final String theText, final int theX, final int theY) {
		if (theX < 0 || theX >= myCells[0].length || theY < 0 || theY >= myCells.length) {
			throw new IllegalArgumentException("Cell does not exist. ");
		}

		myCells[theY][theX].setText(theText);
	}

	/**
	 * Helper method that creates the cells of the spreadsheet Panel.
	 * @param theRows The # of rows in the spreadsheet.
	 * @param theColumns The # of columns in the spreadsheet.
	 * @return The newly created JPanel.
	 */
	private JPanel innerGrid(final int theRows, final int theColumns) {
		JPanel grid = new JPanel();
		for (int y = 0; y < theRows; y++) {
			for (int x = 0; x < theColumns; x++) {
				myGBC.gridx = x + 1;
				myGBC.gridy = y + 1;
				myCells[y][x] = new JTextField(10);
				myCells[y][x].setEditable(false);
				this.add(myCells[y][x], myGBC);
			}
		}
		return grid;
	}
}

