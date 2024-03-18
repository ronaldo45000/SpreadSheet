package Controller;

import Model.CellToken;
import Model.SheetUtility;
import Model.Spreadsheet;
import View.CellPanel;
import View.FormulaPanel;
import View.MenuBar;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The controller to the GUI. initializes the window components and adds listeners.
 * @author Dillon Crookshank
 * @author Halim Lee
 * @author Thinh Le
 * @version 1.1
 */
public class SheetController {
    /** The name of the window. */
    private static final String DEFAULT_TITLE = "Spreadsheet";

    /** The default file directory that the file selection dialog opens up to. */
    private static final String DEFAULT_DIRECTORY = "C:\\";

    /** The pixel padding added to the minimum window width. */
    private static final int PADDING_X = 25;

    /** The pixel padding added to the minimum window height. */
    private static final int PADDING_Y = 25;

    private final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    /** The instance of the model. */
    private Spreadsheet mySheet;

    /** The main window that holds the GUI. */
    private final JFrame myWindow;

    /** The panel that holds every component. */
    private final JPanel myMainPanel;

    /** The top menu bar. */
    private final View.MenuBar myMenuBar;

    /** The panel that holds the formula editor. */
    private final FormulaPanel myFormulaPanel;

    /** The panel that holds the cells of the spreadsheet. */
    private CellPanel myCellPanel;

    /** The scroll pane that holds the cell panel. */
    private JScrollPane myScrollable;

    /** The instance of GridBagConstraints used throughout the class. */
    private final GridBagConstraints myGBC;

    /** Boolean Field that determines if formulas should be shown in cells or not. */
    private boolean myViewFormulaFlag;

    /** The lone constructor to the controller. */
    public SheetController() {
        myViewFormulaFlag = false;
        myGBC = new GridBagConstraints();

        //Initialize the model
        mySheet = new Spreadsheet();

        //Initialize the view components
        myMainPanel = new JPanel(new GridBagLayout());
        myMenuBar = new MenuBar();

        //Initialize formula panel
        myFormulaPanel = new FormulaPanel();
        myGBC.gridx = 0;
        myGBC.gridy = 0;
        myMainPanel.add(myFormulaPanel, myGBC);

        //Initialize the cell panel
        myCellPanel = new CellPanel();
        myScrollable = new JScrollPane(myCellPanel);
        myGBC.gridy = 1;
        myMainPanel.add(myScrollable, myGBC);

        //Initialize the window
        myWindow = new JFrame(DEFAULT_TITLE);
        myWindow.setContentPane(myMainPanel);
        myWindow.setJMenuBar(myMenuBar);
        myWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        resetWindow();

        addListeners();

        //Show the window.
        myWindow.setVisible(true);
    }

    /** Helper method that adds listeners to every component. */
    private void addListeners() {
        //-=-=-=-=-=-=-=-=-=- Formula Bar Listeners -=-=-=-=-=-=-=-=-=-//
        myFormulaPanel.addActionListener(theEvent -> {
            switch (theEvent.getActionCommand()) {
                case (FormulaPanel.ADDRESS) -> { /* Do nothing. */}
                case (FormulaPanel.FORMULA), (FormulaPanel.SET) -> updateCellData();
                case (FormulaPanel.CLEAR) -> {
                    //Clear the textFields
                    myFormulaPanel.setFormula("");
                    myFormulaPanel.setAddress("");
                }
            }
        });

        //Cell address text field listener.
        myFormulaPanel.addAddressListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkAddress();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkAddress();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkAddress();
            }
        });

        //-=-=-=-=-=-=-=-=-=- Menu Bar Listeners -=-=-=-=-=-=-=-=-=-//
        myMenuBar.addListeners(theEvent ->{
            switch(theEvent.getActionCommand()) {
                case (View.MenuBar.QUIT) -> exitConfirmation();
                case (MenuBar.CLEAR) -> clearConfirmation();
                case (MenuBar.NEW) -> newSheetPrompt();
                case (MenuBar.OPEN) -> openSpreadsheet();
                case (MenuBar.SAVE) -> saveSpreadsheet();
                case (MenuBar.VIEW_VALUES) -> {
                    //Change formula flag field and update GUI
                    myViewFormulaFlag = false;
                    updateCellPanel();

                    //Activate and deactivate options as necessary
                    myMenuBar.setOptionEnabled(MenuBar.VIEW_VALUES, false);
                    myMenuBar.setOptionEnabled(MenuBar.VIEW_FORMULAS, true);

                }
                case (MenuBar.VIEW_FORMULAS) -> {
                    //Change formula flag field and update GUI
                    myViewFormulaFlag = true;
                    updateCellPanel();

                    //Activate and deactivate options as necessary
                    myMenuBar.setOptionEnabled(MenuBar.VIEW_VALUES, true);
                    myMenuBar.setOptionEnabled(MenuBar.VIEW_FORMULAS, false);

                }
                case (MenuBar.INCREASE_PRECISION) -> {
                    //Increment decimal precision and update the GUI
                    mySheet.displaceDecimalPrecision(1);
                    updateCellPanel();

                    //Activate and deactivate options when needed
                    if (mySheet.getDecimalPrecision() == 8) {
                        myMenuBar.setOptionEnabled(MenuBar.INCREASE_PRECISION, false);
                    }
                    myMenuBar.setOptionEnabled(MenuBar.DECREASE_PRECISION, true);

                }
                case (MenuBar.DECREASE_PRECISION) -> {
                    //Decrement decimal precision and update the GUI
                    mySheet.displaceDecimalPrecision(-1);
                    updateCellPanel();

                    //Activate and deactivate options when needed
                    if (mySheet.getDecimalPrecision() == 0) {
                        myMenuBar.setOptionEnabled(MenuBar.DECREASE_PRECISION, false);
                    }
                    myMenuBar.setOptionEnabled(MenuBar.INCREASE_PRECISION, true);

                }
            }
        });

        //-=-=-=-=-=-=-=-=-=- Window Listeners -=-=-=-=-=-=-=-=-=-//
        myWindow.addWindowListener(new WindowAdapter() {
            /** Listener method that calls whenever the window attempts to close */
            @Override
            public void windowClosing(WindowEvent theEvent) { exitConfirmation(); }
        });
    }

    /** Helper method that shows a jOptionPane to confirm that the user wants to exit the program. */
    private void exitConfirmation() {
        if (JOptionPane.showConfirmDialog(myWindow, "You sure that you want to exit?",
                "Exit Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            myWindow.dispose();
        }
    }

    /** Helper method that prompts the user to create a new spreadsheet. */
    private void newSheetPrompt() {
        //Initialize the text fields
        JTextField rowField = new JTextField(3);
        JTextField columnField = new JTextField(3);

        //Add listeners that consume key events that are not numerical keys, so that
        //the only characters that you are able to type are digits.
        rowField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                }
            }
        });
        columnField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                }
            }
        });

        //Create the panel that will be placed in an option pane
        JPanel optionPanel = new JPanel(new GridBagLayout());

        //Place the components on the panel
        myGBC.gridx = 0;
        myGBC.gridy = 0;
        myGBC.gridwidth = 2;
        optionPanel.add(new JLabel("Enter the dimensions of the new spreadsheet."), myGBC);

        myGBC.gridx = 0;
        myGBC.gridy = 1;
        myGBC.gridwidth = 1;
        optionPanel.add(new JLabel("Columns:"), myGBC);

        myGBC.gridx = 1;
        myGBC.gridy = 1;
        optionPanel.add(columnField, myGBC);

        myGBC.gridx = 0;
        myGBC.gridy = 2;
        optionPanel.add(Box.createHorizontalStrut(15), myGBC); // a spacer

        myGBC.gridx = 0;
        myGBC.gridy = 3;
        optionPanel.add(new JLabel("Rows:"), myGBC);

        myGBC.gridx = 1;
        myGBC.gridy = 3;
        optionPanel.add(rowField, myGBC);

        //Create the option pane
        int result = JOptionPane.showConfirmDialog(null, optionPanel,
                "New Spreadsheet", JOptionPane.OK_CANCEL_OPTION);

        //Create a new spreadsheet if "ok" was selected
        if (result == JOptionPane.OK_OPTION) {
            int newRows = 10;
            int newColumns = 10;

            //Check to make sure that the given string is a number
            if (!rowField.getText().trim().equals("")) {
                newRows = Integer.parseInt(rowField.getText());
            }
            if (!columnField.getText().trim().equals("")) {
                newColumns = Integer.parseInt(columnField.getText());
            }

            //Make sure that if the set dimensions are below the predefined minimum, set them to the minimum.
            if (newRows < Spreadsheet.MINIMUM_DIMENSION) {
                newRows = Spreadsheet.MINIMUM_DIMENSION;
            }
            if (newColumns < Spreadsheet.MINIMUM_DIMENSION) {
                newColumns = Spreadsheet.MINIMUM_DIMENSION;
            }

            createNewSheet(newRows, newColumns);

            myWindow.setTitle(DEFAULT_TITLE);
        }
    }

    /**
     * Helper method that re-initializes the CellPanel to the given dimensions, and resets the window.
     * @param theRows The # of rows in the new sheet.
     * @param theColumns The # of columns in the new sheet.
     */
    private void createNewSheet(final int theRows, final int theColumns) {
        myMainPanel.remove(myScrollable);
        myCellPanel = new CellPanel(theRows, theColumns);
        myScrollable = new JScrollPane(myCellPanel);
        mySheet = new Spreadsheet(theRows, theColumns);

        //Add the sheetPanel back to the GUI
        myGBC.gridx = 0;
        myGBC.gridy = 1;
        myMainPanel.add(myScrollable, myGBC);

        resetWindow();

        myMenuBar.setOptionEnabled(MenuBar.INCREASE_PRECISION, true);
        myMenuBar.setOptionEnabled(MenuBar.DECREASE_PRECISION, true);
    }

    /** Helper method that prompts the user to open an existing spreadsheet. */
    private void openSpreadsheet() {
        //Prompt the user to select a file.
        JFileChooser chooser = new JFileChooser(DEFAULT_DIRECTORY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files(.txt)", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);

        //Only open a new file if a file was properly selected
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            Scanner sheetFile = new Scanner("");
            try {
                sheetFile = new Scanner(chooser.getSelectedFile().getAbsoluteFile());
            } catch (FileNotFoundException theExc) {
                System.err.println("File not Found.");
            }

            //Dimensions of the sheet are always the first 2 pieces of data
            int rows = Integer.parseInt(sheetFile.next());
            int columns = Integer.parseInt(sheetFile.next());

            sheetFile.nextLine();

            //Change the sheet in the window
            createNewSheet(rows, columns);
            myMenuBar.setOptionEnabled(MenuBar.CLEAR, true);

            //Read each cell and fill the sheet
            while (sheetFile.hasNext()) {
                String dataLine = sheetFile.nextLine();

                CellToken nextCell = new CellToken();
                int index = 0;
                index = SheetUtility.getCellToken(dataLine, index, nextCell);

                mySheet.changeCellFormula(nextCell, dataLine.substring(index).trim());
            }
            //Tell the model to evaluate the entire spreadsheet and update the GUI
            mySheet.evaluateSheet();
            updateCellPanel();

            //Update the window title and close resources.
            myWindow.setTitle(chooser.getSelectedFile().getName());
            sheetFile.close();
        }
    }

    /** Helper method that prompts the user to save the current spreadsheet. */
    private void saveSpreadsheet() {
        //Prompt the user to select a file
        JFileChooser chooser = new JFileChooser(DEFAULT_DIRECTORY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files(.txt)", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showSaveDialog(null);

        //Only start saving a file if a file name was properly entered.
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            PrintStream fileOut = System.err;
            try {
                fileOut = new PrintStream(chooser.getSelectedFile().getAbsoluteFile());
            } catch (FileNotFoundException theExc) {
                System.err.println("File not Found.");
            }

            //Put the data from the model into the file.
            fileOut.print(mySheet.toString());

            //Update window title and close resources
            myWindow.setTitle(chooser.getSelectedFile().getName());
            fileOut.close();
        }
    }

    /** Helper method that prompts the user with a JOptionPane to confirm that they want to clear the spreadsheet. */
    private void clearConfirmation() {
        if (JOptionPane.showConfirmDialog(myWindow, "You sure that you want to clear your spreadsheet?",
                "Exit Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            mySheet.clear();
            updateCellPanel();

            myFormulaPanel.setAddress("");
            myFormulaPanel.setFormula("");
            myMenuBar.setOptionEnabled(MenuBar.CLEAR, false);
        }
    }

    /** Helper method that checks the address and enables the Formula text field whenever the address is valid. */
    private void checkAddress() {
        //First, check if the cell address is in the valid format
        boolean isValid = SheetUtility.isValidAddress(myFormulaPanel.getAddress());
        boolean isWithinBounds = false;

        CellToken tempCell = new CellToken();
        if (isValid) {
            //Then, check if the cell address points to a cell currently within the sheet.
            SheetUtility.getCellToken(myFormulaPanel.getAddress(), 0, tempCell);

            isWithinBounds = tempCell.getRow() >= 0 && tempCell.getRow() < mySheet.getNumRows()
                    && tempCell.getColumn() >= 0 && tempCell.getColumn() < mySheet.getNumColumns();

        }

        //Update the formula text field based on previous checks
        if (isValid && isWithinBounds) {
            myFormulaPanel.setComponentEnabled(FormulaPanel.FORMULA, true);
            myFormulaPanel.setFormula(mySheet.getCellFormula(tempCell.getRow(), tempCell.getColumn()));
            myFormulaPanel.setComponentEnabled(FormulaPanel.SET, true);
        } else {
            myFormulaPanel.setComponentEnabled(FormulaPanel.FORMULA, false);
            myFormulaPanel.setFormula("");
            myFormulaPanel.setComponentEnabled(FormulaPanel.SET, false);
        }
    }

    /**
     * Helper method that updates the data of a single cell.
     * Uses the cell address and formula currently within the text fields of the formula panel.
     */
    private void updateCellData() {
        //Use the current address field and change that cell
        CellToken tempCell = new CellToken();
        SheetUtility.getCellToken(myFormulaPanel.getAddress(), 0, tempCell);

        if (myFormulaPanel.getFormula().trim().equals("")) {
            mySheet.clearCell(tempCell);
            return;
        }
        //Keep the old formula just in case an error gets thrown, so that the program can revert the users changes.
        String oldFormula = mySheet.getCellFormula(tempCell);

        try {
            mySheet.changeCellFormulaAndRecalculate(tempCell, myFormulaPanel.getFormula());
        } catch (Exception theError) {
            JOptionPane.showMessageDialog(null, "An Error Has Occurred.");
            mySheet.changeCellFormulaAndRecalculate(tempCell, oldFormula);
            updateCellPanel();
            return;
        }

        myMenuBar.setOptionEnabled(MenuBar.CLEAR, true);
        updateCellPanel();
    }

    /**
     * Helper method that refreshes each cell value/formula text field.
     * This method should be called whenever a cell's underlying data is changed.
     **/
    private void updateCellPanel() {
        int columns = mySheet.getNumColumns();
        int rows = mySheet.getNumRows();

        //Update the entire sheet whenever any cell changes
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {

                if (myViewFormulaFlag) {
                    myCellPanel.setCell(mySheet.getCellFormula(y, x), x, y);
                } else {
                    myCellPanel.setCell(mySheet.getCellValue(y, x), x, y);
                }

            }
        }
    }

    /** A helper method that resets the window to its initial state. */
    private void resetWindow() {
        myWindow.pack();
        myWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        myWindow.setResizable(false);

        //Set the preferred size of the scroll pane
        myScrollable.setPreferredSize(new Dimension((int) SCREEN_SIZE.getWidth() - PADDING_X * 2,
                (int) SCREEN_SIZE.getHeight() - myFormulaPanel.getHeight() - PADDING_Y * 2));


        //change initial state of the GUI
        myFormulaPanel.setComponentEnabled(FormulaPanel.FORMULA, false);
        myFormulaPanel.setComponentEnabled(FormulaPanel.SET, false);
        myFormulaPanel.setFormula("");
        myFormulaPanel.setAddress("");

        myMenuBar.setOptionEnabled(MenuBar.CLEAR, false);
        myMenuBar.setOptionEnabled(MenuBar.VIEW_VALUES, false);
        myMenuBar.setOptionEnabled(MenuBar.VIEW_FORMULAS, true);

        myViewFormulaFlag = false;
    }
}
