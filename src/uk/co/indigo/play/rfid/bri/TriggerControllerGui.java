package uk.co.indigo.play.rfid.bri;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;

public class TriggerControllerGui {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private Label triggerNameLabel = null;
	private Label switchLabel = null;
	private Label maskLabel = null;
	private Label delayLabel = null;
	private Text triggerNameText = null;
	private Text switchesText = null;
	private Text maskText = null;
	private Text delayText = null;
	private Button addTriggerButton = null;
	private List triggerList = null;
	
	private TriggerControllerHelper tch = null;
	private Button listenButton = null;
	
	public TriggerControllerGui() {
		tch = new TriggerControllerHelper();
	}

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
		TriggerControllerGui thisClass = new TriggerControllerGui();
		thisClass.createSShell();
		thisClass.sShell.open();

		while (!thisClass.sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		sShell = new Shell();
		sShell.setText("Trigger Controller");
		sShell.setSize(new org.eclipse.swt.graphics.Point(293,254));
		triggerNameLabel = new Label(sShell, SWT.NONE);
		triggerNameLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(16,17,28,13));
		triggerNameLabel.setText("Name");
		switchLabel = new Label(sShell, SWT.NONE);
		switchLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(176,17,46,13));
		switchLabel.setText("Switches");
		maskLabel = new Label(sShell, SWT.NONE);
		maskLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(116,17,31,13));
		maskLabel.setText("Mask");
		delayLabel = new Label(sShell, SWT.NONE);
		delayLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(236,17,31,13));
		delayLabel.setText("Delay");
		triggerNameText = new Text(sShell, SWT.BORDER);
		triggerNameText.setBounds(new org.eclipse.swt.graphics.Rectangle(15,37,82,19));
		switchesText = new Text(sShell, SWT.BORDER);
		switchesText.setBounds(new org.eclipse.swt.graphics.Rectangle(176,37,41,19));
		maskText = new Text(sShell, SWT.BORDER);
		maskText.setBounds(new org.eclipse.swt.graphics.Rectangle(116,37,41,19));
		delayText = new Text(sShell, SWT.BORDER);
		delayText.setBounds(new org.eclipse.swt.graphics.Rectangle(235,38,32,19));
		addTriggerButton = new Button(sShell, SWT.NONE);
		addTriggerButton.setBounds(new org.eclipse.swt.graphics.Rectangle(16,68,81,23));
		addTriggerButton.setText("Set Trigger");
		addTriggerButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
						System.out.println("widgetSelected()"); // TODO Auto-generated Event stub widgetSelected()
						addTrigger();
					}
				});
		triggerList = new List(sShell, SWT.NONE);
		triggerList.setBounds(new org.eclipse.swt.graphics.Rectangle(16,108,251,64));
		listenButton = new Button(sShell, SWT.NONE);
		listenButton.setBounds(new org.eclipse.swt.graphics.Rectangle(15,187,52,23));
		listenButton.setText("Listen");
		listenButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
						System.out.println("widgetSelected()"); // TODO Auto-generated Event stub widgetSelected()
						listen();
					}
				});
	}
	
	private void addTrigger() {
		String name = triggerNameText.getText();
		int mask = Integer.parseInt(maskText.getText());
		int switches = Integer.parseInt(switchesText.getText());
		long delay = Long.parseLong(delayText.getText()); 
		tch.addTrigger(name, mask, switches, delay);
		triggerList.add(name + " " + mask + " " + switches + " " + delay);
		triggerNameText.setText("");
		switchesText.setText("");
		maskText.setText("");
		delayText.setText("");
	}
	
	private void listen() {
		triggerNameText.setEnabled(false);
		switchesText.setEnabled(false);
		maskText.setEnabled(false);
		delayText.setEnabled(false);
		addTriggerButton.setEnabled(false);
		listenButton.setEnabled(false);
		tch.listen();
		triggerNameText.setEnabled(true);
		switchesText.setEnabled(true);
		maskText.setEnabled(true);
		delayText.setEnabled(true);
		addTriggerButton.setEnabled(true);
		listenButton.setEnabled(true);
	}

}
