package uk.co.indigo.play.rfid.bri;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;

public class OutputControllerGui {
	
	private TriggerController triggerController;

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="88,47"
	private Button checkBox_01 = null;
	private Button checkBox_02 = null;
	private Button checkBox_03 = null;
	private Button checkBox_04 = null;
	private Button setButton = null;
	private Button countButton = null;

	private Text countDelayText = null;

	private Label msLabel = null;

	private Text countText = null;

	private Label countLabel = null;

	private Label statusLabel = null;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/* Before this is run, be sure to set up the launch configuration (Arguments->VM Arguments)
		 * for the correct SWT library path in order to run with the SWT dlls. 
		 * The dlls are located in the SWT plugin jar.  
		 * For example, on Windows the Eclipse SWT 3.1 plugin jar is:
		 *       installation_directory\plugins\org.eclipse.swt.win32_3.1.0.jar
		 */
		Display display = Display.getDefault();
		OutputControllerGui thisClass = new OutputControllerGui();
		thisClass.createSShell();
		thisClass.sShell.open();

		while (!thisClass.sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
	
	public OutputControllerGui() {
		initTriggerController();
	}
	
	private void initTriggerController() {
		try {
			triggerController = TriggerController.getInstance();
		}
		catch (BRIWrapperException bwe) {
			System.err.println(bwe);
		}
	}

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		sShell = new Shell();
		sShell.setText("IF5 Output Line Controller");
		sShell.setSize(new org.eclipse.swt.graphics.Point(249,179));
		checkBox_01 = new Button(sShell, SWT.CHECK);
		checkBox_01.setBounds(new org.eclipse.swt.graphics.Rectangle(11,22,35,16));
		checkBox_01.setText("01");
		checkBox_02 = new Button(sShell, SWT.CHECK);
		checkBox_02.setBounds(new org.eclipse.swt.graphics.Rectangle(56,22,35,16));
		checkBox_02.setText("02");
		checkBox_03 = new Button(sShell, SWT.CHECK);
		checkBox_03.setBounds(new org.eclipse.swt.graphics.Rectangle(100,22,35,16));
		checkBox_03.setText("03");
		checkBox_04 = new Button(sShell, SWT.CHECK);
		checkBox_04.setBounds(new org.eclipse.swt.graphics.Rectangle(146,22,35,16));
		checkBox_04.setText("04");
		setButton = new Button(sShell, SWT.NONE);
		setButton.setBounds(new org.eclipse.swt.graphics.Rectangle(190,18,42,23));
		setButton.setText("Set");
		setButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				//System.out.println("widgetSelected()");
				setOutputValue();
			}
		});
		countButton = new Button(sShell, SWT.NONE);
		countButton.setText("Count");
		countButton.setLocation(new org.eclipse.swt.graphics.Point(191,82));
		countButton.setSize(new org.eclipse.swt.graphics.Point(41,23));
		countDelayText = new Text(sShell, SWT.BORDER);
		countDelayText.setBounds(new org.eclipse.swt.graphics.Rectangle(131,82,46,18));
		countDelayText.setText("1000");
		msLabel = new Label(sShell, SWT.RIGHT);
		msLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(36,82,88,16));
		msLabel.setText("Count Delay (ms)");
		countText = new Text(sShell, SWT.BORDER);
		countText.setBounds(new org.eclipse.swt.graphics.Rectangle(131,52,46,18));
		countText.setText("1");
		countLabel = new Label(sShell, SWT.RIGHT);
		countLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(36,52,88,16));
		countLabel.setText("Times to Count");
		statusLabel = new Label(sShell, SWT.BORDER);
		statusLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(11,127,221,16));
		statusLabel.setText("");
		countButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				//System.out.println("widgetSelected()");
				count();
			}
		});
	}
	
	private void setOutputValue() {
		int val = 0;
		val += checkBox_01.getSelection()?0:8;
		val += checkBox_02.getSelection()?0:4;
		val += checkBox_03.getSelection()?0:2;
		val += checkBox_04.getSelection()?0:1;
		try {
			triggerController.writePattern(val);
		}
		catch (BRIWrapperException bwe) {
			System.out.println(bwe);
		}
	}
	
	private void count() {
		checkBox_01.setEnabled(false);
		checkBox_02.setEnabled(false);
		checkBox_03.setEnabled(false);
		checkBox_04.setEnabled(false);
		setButton.setEnabled(false);
		countButton.setEnabled(false);
		statusLabel.setText("");
		int timesToCount = 0;
		long countDelay = 0L;
		try {
			timesToCount = Integer.parseInt(countText.getText());
			countDelay = Long.parseLong(countDelayText.getText());
			if (timesToCount < 1 || countDelay < 1)
				throw new NumberFormatException();
		}
		catch (NumberFormatException nfe) {
			statusLabel.setForeground(new Color(Display.getCurrent(),255,0,0));
			statusLabel.setText("PLEASE ENTER VALID VALUES!");
			checkBox_01.setEnabled(true);
			checkBox_02.setEnabled(true);
			checkBox_03.setEnabled(true);
			checkBox_04.setEnabled(true);
			setButton.setEnabled(true);
			countButton.setEnabled(true);
			return;
		}
		try {
			statusLabel.setForeground(new Color(Display.getCurrent(),0,0,255));
			statusLabel.setText("Counting...");
			triggerController.blinkenlights(timesToCount,countDelay);
		}
		catch (BRIWrapperException bwe) {
			System.err.println(bwe);
		}
		finally {
			checkBox_01.setEnabled(true);
			checkBox_02.setEnabled(true);
			checkBox_03.setEnabled(true);
			checkBox_04.setEnabled(true);
			setButton.setEnabled(true);
			countButton.setEnabled(true);
			statusLabel.setText("");
		}
	}

}
