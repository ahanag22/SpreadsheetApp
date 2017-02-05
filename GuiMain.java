package app;

import java.awt.EventQueue;

/**
 * This class is the driver class for the GUI of the spreadsheet.
 */
public class GuiMain {	
	/**
	 * The default constructor.
	 */
	private GuiMain() {
		throw new IllegalStateException();
	}
	
	/**
	 * The Main method for driving the GUI
	 * @param args Command line arguments.
	 */
	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				final Gui theGUI = new Gui();
				theGUI.start();
			}
		});
	}
}


