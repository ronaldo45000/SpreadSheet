package View;

import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * The menu bar component. Creates the following menu structure:
 * File Menu -> Clear, Quit, New, Save.
 * View Menu -> View Values, View Formulas, Increase Precision, Decrease Precision.
 * @author Dillon Crookshank
 * @author Halim Lee
 * @author Thinh Le
 * @version 1.0
 */
public class MenuBar extends JMenuBar {

	/** The command associated with the Clear option. */
	public static final String CLEAR = "Clear";

	/** The command associated with the Quit option. */
	public static final String QUIT = "Quit";

	/** The command associated with the New option. */
	public static final String NEW = "New";

	/** The command associated with the Open option. */
	public static final String OPEN = "Open";

	/** The command associated with the Save option. */
	public static final String SAVE = "Save";

	/** The command associated with the View Values option. */
	public static final String VIEW_VALUES = "View Values";

	/** The command associated with the View Formulas option. */
	public static final String VIEW_FORMULAS = "View Formulas";

	/** The command associated with the Increase Precision option. */
	public static final String INCREASE_PRECISION = "Increase Precision";

	/** The command associated with the Decrease Precision option. */
	public static final String DECREASE_PRECISION = "Decrease Precision";

	/** The Clear option. */
	private final JMenuItem myClearOption;

	/** The Quit option. */
	private final JMenuItem myQuitOption;

	/** The new option. */
	private final JMenuItem myNewOption;

	/** The Open option. */
	private final JMenuItem myOpenOption;

	/** The Save option. */
	private final JMenuItem mySaveOption;

	/** The View Values option. */
	private final JMenuItem myValueOption;

	/** The View formulas option. */
	private final JMenuItem myFormulaOption;

	/** The Increase Precision option. */
	private final JMenuItem myIncrementOption;

	/** The Decrease Precision option. */
	private final JMenuItem myDecrementOption;

	/** The lone constructor. Creates a new Menu Bar. */
	public MenuBar(){
		super();

		//Initialize the File menu.
		JMenu fileMenu = new JMenu("File");

		//Initialize the New option
		myNewOption = new JMenuItem(NEW);
		fileMenu.add(myNewOption);

		//Initialize the Open option.
		myOpenOption = new JMenuItem(OPEN);
		fileMenu.add(myOpenOption);

		//Initialize the Save option.
		mySaveOption = new JMenuItem(SAVE);
		mySaveOption.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		fileMenu.add(mySaveOption);
		fileMenu.addSeparator();

		//Initialize the Clear option.
		myClearOption = new JMenuItem(CLEAR);
		fileMenu.add(myClearOption);
		fileMenu.addSeparator();

		//Initialize the Quit option.
		myQuitOption = new JMenuItem(QUIT);
		myQuitOption.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
		fileMenu.add(myQuitOption);

		//Finish initializing the File menu.
		this.add(fileMenu);

		//Initialize the View menu.
		JMenu viewMenu = new JMenu("View");

		//Initialize the View Values option
		myValueOption = new JMenuItem(VIEW_VALUES);
		viewMenu.add(myValueOption);

		//Initialize the View Formulas option
		myFormulaOption = new JMenuItem(VIEW_FORMULAS);
		viewMenu.add(myFormulaOption);
		viewMenu.addSeparator();

		myIncrementOption = new JMenuItem(INCREASE_PRECISION);
		myIncrementOption.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, InputEvent.CTRL_DOWN_MASK));
		viewMenu.add(myIncrementOption);

		myDecrementOption = new JMenuItem(DECREASE_PRECISION);
		myDecrementOption.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, InputEvent.CTRL_DOWN_MASK));
		viewMenu.add(myDecrementOption);

		//Finish initializing the View menu.
		this.add(viewMenu);
	}

	/**
	 * Lets you activate and deactivate specific menu options within the Menu Bar.
	 * @param theOption Use one of the following static fields: CLEAR, QUIT, NEW, OPEN, SAVE,
	 *                     VIEW_VALUES, VIEW_FORMULAS, INCREASE_PRECISION, DECREASE_PRECISION.
	 * @param isEnabled True if the given menu option should be enabled. False otherwise.
	 */
	public void setOptionEnabled(final String theOption, final boolean isEnabled) {
		switch(theOption) {
			case CLEAR -> myClearOption.setEnabled(isEnabled);
			case QUIT -> myQuitOption.setEnabled(isEnabled);
			case NEW -> myNewOption.setEnabled(isEnabled);
			case OPEN -> myOpenOption.setEnabled(isEnabled);
			case SAVE -> mySaveOption.setEnabled(isEnabled);
			case VIEW_VALUES -> myValueOption.setEnabled(isEnabled);
			case VIEW_FORMULAS -> myFormulaOption.setEnabled(isEnabled);
			case INCREASE_PRECISION -> myIncrementOption.setEnabled(isEnabled);
			case DECREASE_PRECISION -> myDecrementOption.setEnabled(isEnabled);
			default -> { /*This shouldn't happen if used properly. */ }
		}
	}

	public void addListeners(ActionListener theListener) {
		myClearOption.addActionListener(theListener);
		myNewOption.addActionListener(theListener);
		myOpenOption.addActionListener(theListener);
		mySaveOption.addActionListener(theListener);
		myQuitOption.addActionListener(theListener);
		myValueOption.addActionListener(theListener);
		myFormulaOption.addActionListener(theListener);
		myIncrementOption.addActionListener(theListener);
		myDecrementOption.addActionListener(theListener);
	}
}
