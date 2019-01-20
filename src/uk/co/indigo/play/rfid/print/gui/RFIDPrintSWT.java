package uk.co.indigo.play.rfid.print.gui;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Spinner;

import uk.co.indigo.play.rfid.print.PrintException;


/**
 * View for the SSCC Encode and Print application.
 *
 * @author milbuw
 */
public class RFIDPrintSWT {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="12,12"
	private Menu mainMenuBar = null;
	private Menu fileMenu = null;
	private Label headerLabel = null;
	private Label filterLabel = null;
	private Label partitionLabel = null;
	private Label cpiLabel = null;
	private Label serialRefLabel = null;
	private Label extDigitLabel = null;
	private Label checkDigitLabel = null;
	private Text hvText = null;
	private Text fvText = null;
	private Text pvText = null;
	private Text cpiText = null;
	private Text serialRefext = null;
	private Text extDigitText = null;
	private Text checkDigitText = null;
	private Combo printerCombo = null;
	private Label printerLabel = null;
	private Button encodeButton = null;
	private Text encodedText = null;
	private Button button = null;
	private Spinner qtySpinner = null;
	private Label qtyLabel = null;

	/**
	 * This method initializes printerCombo	
	 *
	 */
	private void createPrinterCombo() {
		printerCombo = new Combo(sShell, SWT.NONE);
		printerCombo.setBounds(new org.eclipse.swt.graphics.Rectangle(274,6,100,15));
		printerCombo.add("PM4i");
		printerCombo.select(0);
		printerCombo.setText(printerCombo.getItem(0));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		RFIDPrintSWT thisClass = new RFIDPrintSWT();
		thisClass.createSShell();
		thisClass.sShell.open();

		while (!thisClass.sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	/**
	 * This method initializes view and makes calls into the controller.
	 */
	private void createSShell() {
		sShell = new Shell();
		sShell.setText("RFID SSCC Encode and Print");
		sShell.setSize(new org.eclipse.swt.graphics.Point(399,281));
		headerLabel = new Label(sShell, SWT.NONE);
		headerLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(5,5,80,20));
		headerLabel.setText("Header Value:");
		filterLabel = new Label(sShell, SWT.NONE);
		filterLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(5,30,80,20));
		filterLabel.setText("Filter Value:");
		partitionLabel = new Label(sShell, SWT.NONE);
		partitionLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(5,55,80,20));
		partitionLabel.setText("Partition Value:");
		cpiLabel = new Label(sShell, SWT.NONE);
		cpiLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(5,80,80,20));
		cpiLabel.setText("CPI:");
		serialRefLabel = new Label(sShell, SWT.NONE);
		serialRefLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(5,105,80,20));
		serialRefLabel.setText("Serial Ref:");
		extDigitLabel = new Label(sShell, SWT.NONE);
		extDigitLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(4,130,80,20));
		extDigitLabel.setText("Ext. Digit:");
		checkDigitLabel = new Label(sShell, SWT.NONE);
		checkDigitLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(5,155,80,20));
		checkDigitLabel.setText("Check Digit:");
		hvText = new Text(sShell, SWT.BORDER);
		hvText.setBounds(new org.eclipse.swt.graphics.Rectangle(90,5,50,20));
		hvText.setText("49");
		hvText.setEditable(false);
		fvText = new Text(sShell, SWT.BORDER);
		fvText.setBounds(new org.eclipse.swt.graphics.Rectangle(90,30,50,20));
		fvText.setEditable(false);
		fvText.setText("3");
		pvText = new Text(sShell, SWT.BORDER);
		pvText.setBounds(new org.eclipse.swt.graphics.Rectangle(90,55,50,20));
		pvText.setText("0");
		cpiText = new Text(sShell, SWT.BORDER);
		cpiText.setBounds(new org.eclipse.swt.graphics.Rectangle(90,80,100,20));
		cpiText.setText("061414119283");
		serialRefext = new Text(sShell, SWT.BORDER);
		serialRefext.setBounds(new org.eclipse.swt.graphics.Rectangle(90,105,100,20));
		serialRefext.setText("7465");
		extDigitText = new Text(sShell, SWT.BORDER);
		extDigitText.setBounds(new org.eclipse.swt.graphics.Rectangle(90,130,50,20));
		extDigitText.setText("1");
		checkDigitText = new Text(sShell, SWT.BORDER);
		checkDigitText.setBounds(new org.eclipse.swt.graphics.Rectangle(90,155,50,20));
		checkDigitText.setText("7");
		mainMenuBar = new Menu(sShell, SWT.BAR);
		MenuItem fileMenuItem = new MenuItem(mainMenuBar, SWT.CASCADE);
		fileMenuItem.setText("File");
		fileMenu = new Menu(fileMenuItem);
		MenuItem push = new MenuItem(fileMenu, SWT.PUSH);
		push.setText("Close");
		push.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				sShell.close();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		fileMenuItem.setMenu(fileMenu);
		sShell.setMenuBar(mainMenuBar);
		createPrinterCombo();
		printerLabel = new Label(sShell, SWT.NONE);
		printerLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(220,5,40,15));
		printerLabel.setText("Printer:");
		encodeButton = new Button(sShell, SWT.NONE);
		encodeButton.setBounds(new org.eclipse.swt.graphics.Rectangle(5,180,50,20));
		encodeButton.setText("Encode");
		encodeButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
						encodedText.setText(RFIDPrintHelper.encode(
								hvText.getText(),
								fvText.getText(),
								pvText.getText(),
								cpiText.getText(),
								serialRefext.getText(),
								extDigitText.getText(),
								checkDigitText.getText()
							)
						);
					}
				});
		encodedText = new Text(sShell, SWT.BORDER);
		encodedText.setBounds(new org.eclipse.swt.graphics.Rectangle(90,180,250,20));
		button = new Button(sShell, SWT.NONE);
		button.setBounds(new org.eclipse.swt.graphics.Rectangle(5,205,50,20));
		button.setText("Print");
		button.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				try {
					RFIDPrintHelper.print(
						printerCombo.getItem(printerCombo.getSelectionIndex()),
						encodedText.getText(),
						qtySpinner.getSelection()
					);
				}
				catch (PrintException pe) {
					System.err.println(pe);
				}
			}
		});
		qtySpinner = new Spinner(sShell, SWT.NONE);
		qtySpinner.setBounds(new org.eclipse.swt.graphics.Rectangle(276,33,40,16));
		qtySpinner.setMinimum(1);
		qtyLabel = new Label(sShell, SWT.NONE);
		qtyLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(221,33,49,13));
		qtyLabel.setText("Quantity:");
	}

}
