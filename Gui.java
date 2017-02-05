package app;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;

import javax.swing.*;


/**
 * This is the GUI class of the Spreadsheet program that generates a spreadsheet
 * that has the same functionality as the menu-driven spreadsheet. *
 */
public class Gui {
	JTable table;
	private final JFrame myFrame;
	private final int DEFAULT_ROWS = 50;
	private final int DEFAULT_COLS = 16;
	private Spreadsheet theSpreadsheet;
	JMenuBar menuBar;
	JMenu fileMenu, editMenu, helpMenu;
	JMenuItem saveItem, closeItem, saveAsItem, 
		aboutItem, userManualItem, techManualItem, openItem;
    JTextField formulaBar;
    
	/**
	 * Default constructor
	 */
	public Gui() {
		myFrame = new JFrame("TCSS342 Spreadsheet");
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	/**
	 * This method's purpose is so that our GUI constructor doesn't get too
	 * full of initializations and method calls.
	 */
	public void start() {
		myFrame.setLayout(new BorderLayout());
		theSpreadsheet = new Spreadsheet(DEFAULT_ROWS, DEFAULT_COLS);		
		myFrame.setVisible(true);
		createMenu();
        createFormulaBox();
		createGrid();
	}
	
	/**
	 * This method creates the menu bar of the JFrame with helpful
	 * submenus and menu items to use as tools for the spreadsheet.
	 */
	public void createMenu() {
		//create the menu bar.
		menuBar = new JMenuBar();
		
		//menus on the bar
		fileMenu = new JMenu("File");
		helpMenu = new JMenu("Help");
		
		//menu items in file
		closeItem = new JMenuItem("Close");
		saveAsItem = new JMenuItem("Save As...");
		aboutItem = new JMenuItem("About...");
		userManualItem = new JMenuItem("User Manual");
		techManualItem = new JMenuItem("Technical Manual");
				
		//Save As button action
		saveAsItem.addActionListener(new ActionListener() {
			/**
			 * The event that the 'Save As...' menu item is clicked.
			 */
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                saveSpreadsheet(); 
            }
        });
		fileMenu.add(saveAsItem);
		//adding action
        closeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                //exits the program.
                System.exit(0);
            }
        });
		fileMenu.add(closeItem);
		
		
		
		//menu items in help
		aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                helpMsg(); 
            }
        });
		helpMenu.add(aboutItem);
		userManualItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                MsgForUser(); 
            }
        });
		helpMenu.add(userManualItem);
		techManualItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                techManual(); 
            }
        });
		helpMenu.add(techManualItem);
		
		
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		
		myFrame.setJMenuBar(menuBar);
	}
    
    public void createFormulaBox() {
		formulaBar = new JTextField(50);
		formulaBar.addActionListener(new ActionListener() {
			
			/**
			 * The event that 'Enter' i hit while in the formula box.
			 */
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String newFormula = formulaBar.getText();
				int row = table.getSelectedRow();
				int col = table.getSelectedColumn();
				Cell tempCell = theSpreadsheet.getCellFromCellToken(new CellToken(col, row + 1));
				tempCell.setFormula(newFormula);				
				table.setValueAt(newFormula, row, col);				
			}
		});
		
		myFrame.add(formulaBar, BorderLayout.NORTH);
	}
    
	/**
     * a private helper method.
     * save the selected file.
     */
    private void saveSpreadsheet() {        
        JFrame parentFrame = new JFrame();
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");   
         
        int userSelection = fileChooser.showSaveDialog(parentFrame);
         
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            File file = new File(fileToSave.getAbsolutePath());
            try{ 
                if (!file.exists()) {
    				file.createNewFile();
    			}
            
	            FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
	            String [][] s = theSpreadsheet.getValArray();
	            for(int i= 0; i < DEFAULT_ROWS; i++){
	            	
	            	for(int j = 0; j < DEFAULT_COLS; j++){
	            		if(s[i][j].equals("")) {
	            			bw.write("0");
	            		}
	            		else {
	            			bw.write(s[i][j]);	
	            		}
	    		        
	    		        bw.write("|");
	    			}
	            	
	            	bw.newLine();
	            }
	            
	            bw.close();
	    		System.out.println("Save as file: " + fileToSave.getAbsolutePath());  
            } catch (IOException e) {
            	e.printStackTrace();
            }
        }
    }
    
    /**
    * a helper method that helps to pop up a message.
    */
    private void MsgForUser() {
        JOptionPane.showMessageDialog(new JFrame(), 
        		" * Enter the formula directly in the cell or enter the formula in the formula bar."
                + System.lineSeparator() 
                + " * Entering a valid formula will change the value of the cell. " 
                + System.lineSeparator()
                + " * Invalid formula does not replace the current formula." 
                + System.lineSeparator() 
                + " * If formula shows circular dependency, an error message will be shown." );    
    }
		
    /**
     * a helper method that helps to pop up a message.
     */
     private void helpMsg() {
         JOptionPane.showMessageDialog(new JFrame (), "TCSS 342 Spreadsheet, Winter 2016");
     }
     
     /**
      * a helper method that helps to pop up a message.
      */
      private void techManual() {
          JOptionPane.showMessageDialog(new JFrame(), 
                  "This Spreadsheet Application is a java application."
                  + System.lineSeparator() 
                  + "It mainly has two larger components."
                  + System.lineSeparator()
                  + " The GUI or graphical user interface and the"
                  + System.lineSeparator() 
                  + "back-end supporting the actual logic of Spreadsheet application.");
                   
      }
          
    
	/**
	 * This method creates the excel-like grid that we can treat like
	 * cells on a spreadsheet.  
	 */
	public void createGrid() {	
		//initializes the table.
		table = new JTable(new MyDataModel(theSpreadsheet));		
				
		//Creates the Scroll pane that allows us to scroll through the spreadsheet.
		table.setPreferredScrollableViewportSize(new Dimension(1000, 500));
		table.setFillsViewportHeight(true);
		
		
		//the table/spreadsheet part is put inside of the scroll pane
		JScrollPane scrollPane = new JScrollPane(table);
		
		JTable rowTable = new RowNumberTable(table);
		scrollPane.setRowHeaderView(rowTable);
		scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, rowTable.getTableHeader());

        table.setCellSelectionEnabled(true);
		table.addMouseListener(new MouseListener() {

			/**
			 * The event that the mouse is clicked.
			 */
			@Override
			public void mouseClicked(MouseEvent theEvent) {
				int row = table.rowAtPoint(theEvent.getPoint()) + 1;
				int col = table.columnAtPoint(theEvent.getPoint());
				
				CellToken tempCellToken = new CellToken(col, row);
				Cell tempCell = theSpreadsheet.getCellFromCellToken(tempCellToken);
				
				String cellFormula = tempCell.getFormula();
				formulaBar.setText(cellFormula);				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				//mouse event overrides that are required but
				//not needed in our case
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				//mouse event overrides that are required but
				//not needed in our case
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				//mouse event overrides that are required but
				//not needed in our case
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				//mouse event overrides that are required but
				//not needed in our case
			}
		});
        
//		//the scrollpane (and the table inside of it) is added to the frame.
		myFrame.add(scrollPane, BorderLayout.CENTER);
		myFrame.pack();
	}
}
