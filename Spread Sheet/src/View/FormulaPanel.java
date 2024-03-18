package View;

import java.awt.event.ActionListener;
import javax.swing.event.DocumentListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Panel that holds the formula editor.
 * @author Dillon Crookshank
 * @author Halim Lee
 * @author Thinh Le
 * @version 1.0
 */
public class FormulaPanel extends JPanel {

	/** The command associated with the Address Text Field. */
	public static final String ADDRESS = "Address";

	/** The command associated with the Formula Text Field. */
	public static final String FORMULA = "Formula";

	/** The command associated with the set button. */
	public static final String SET = "Set Cell";

	/** The command associated with the clear button. */
	public static final String CLEAR = "Clear";
	
	/** The text field for the cell address. */
	private final JTextField myAddressField;

	/** The text field for the formula. */
	private final JTextField myFormulaField;

	/** The set button. */
	private final JButton mySetButton;

	/** The Clear button. */
	private final JButton myClearButton;

	/** The lone constructor. Creates a new formula Panel. */
	public FormulaPanel() {
		//Initialize Address text field with a label.
		add(new JLabel("Cell: "));

		myAddressField = new JTextField(10);
		myAddressField.setActionCommand(ADDRESS);
		add(myAddressField);

		//Initialize Formula text field with a label.
		add(new JLabel("Formula: "));

		myFormulaField = new JTextField(20);
		myFormulaField.setActionCommand(FORMULA);
		add(myFormulaField);

		//Initialize both buttons.
		mySetButton = new JButton(SET);
		add(mySetButton);

		myClearButton = new JButton(CLEAR);
		add(myClearButton);
	}

	/**
	 * Accessor method for the address text field.
	 * @return The String currently in the address text field.
	 */
	public String getAddress() {
		return myAddressField.getText();
	}

	/**
	 * Mutator method for the address text field.
	 * @param theAddress The new String that should be in the address text field.
	 */
	public void setAddress(String theAddress) {
		myAddressField.setText(theAddress);
	}

	/**
	 * An accessor method for the formula text field.
	 * @return The String currently in the formula text field.
	 */
	public String getFormula() {
		return myFormulaField.getText();
	}

	/**
	 * A mutator method for the formula text field.
	 * @param theFormula The new String that should be in the formula text field.
	 */
	public void setFormula(String theFormula) {
		myFormulaField.setText(theFormula);
	}

	/**
	 * Lets you activate and deactivate specific components within the FormulaPanel.
	 * For TextFields, The field isEditable is mutated.
	 * For Buttons, The field isEnabled is mutated.
	 * @param theComponent Use one of the following static fields: ADDRESS, FORMULA, SET, CLEAR.
	 * @param isEnabled True if the given component should be enabled. False otherwise.
	 */
	public void setComponentEnabled(final String theComponent, final boolean isEnabled) {
		switch (theComponent) {
			case ADDRESS -> myAddressField.setEditable(isEnabled);
			case FORMULA -> myFormulaField.setEditable(isEnabled);
			case SET -> mySetButton.setEnabled(isEnabled);
			case CLEAR -> myClearButton.setEnabled(isEnabled);
			default -> { /*Shouldn't reach here if used properly.*/ }
		}
	}

	/**
	 * Attaches the given ActionListener to every component in the Formula Panel
	 * @param theListener The Listener to be attached to the components within the formula panel.
	 */
	public void addActionListener(final ActionListener theListener) {
		mySetButton.addActionListener(theListener);
		myClearButton.addActionListener(theListener);
		myAddressField.addActionListener(theListener);
		myFormulaField.addActionListener(theListener);

	}

	public void addAddressListener(final DocumentListener theListener) {
		myAddressField.getDocument().addDocumentListener(theListener);
	}
}
