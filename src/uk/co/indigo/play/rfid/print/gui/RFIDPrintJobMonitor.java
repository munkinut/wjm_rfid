package uk.co.indigo.play.rfid.print.gui;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.co.indigo.play.rfid.queue.DirQueueMonitor;

public class RFIDPrintJobMonitor {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private Button startButton = null;
	private Button stopButton = null;
	private Label queueLabel = null;
	private Label archiveLabel = null;
	private Text queueText = null;
	private Text archiveText = null;
	private Label dutyCycleLabel = null;
	private Text dutyCycleText = null;

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
		RFIDPrintJobMonitor thisClass = new RFIDPrintJobMonitor();
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
		sShell.setText("Print Job Monitor");
		sShell.setSize(new org.eclipse.swt.graphics.Point(471,159));
		startButton = new Button(sShell, SWT.NONE);
		startButton.setBounds(new org.eclipse.swt.graphics.Rectangle(11,97,55,23));
		startButton.setText("Start");
		startButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				try {
					RFIDPrintHelper.startQueue(queueText.getText(), archiveText.getText(), Long.parseLong(dutyCycleText.getText()));
					queueText.setEnabled(false);
					archiveText.setEnabled(false);
					dutyCycleText.setEnabled(false);
				}
				catch (IllegalArgumentException iae) {
					// do nothing!
				}
			}
		});
		stopButton = new Button(sShell, SWT.NONE);
		stopButton.setBounds(new org.eclipse.swt.graphics.Rectangle(76,97,55,23));
		stopButton.setText("Stop");
		queueLabel = new Label(sShell, SWT.NONE);
		queueLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(11,7,46,21));
		queueLabel.setText("Queue");
		archiveLabel = new Label(sShell, SWT.NONE);
		archiveLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(11,37,46,21));
		archiveLabel.setText("Archive");
		queueText = new Text(sShell, SWT.BORDER);
		queueText.setBounds(new org.eclipse.swt.graphics.Rectangle(76,7,376,21));
		queueText.setText(DirQueueMonitor.DEFAULT_QUEUE);
		archiveText = new Text(sShell, SWT.BORDER);
		archiveText.setBounds(new org.eclipse.swt.graphics.Rectangle(76,37,376,21));
		archiveText.setText(DirQueueMonitor.DEFAULT_ARCHIVE);
		dutyCycleLabel = new Label(sShell, SWT.NONE);
		dutyCycleLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(11,67,61,21));
		dutyCycleLabel.setText("Duty Cycle");
		dutyCycleText = new Text(sShell, SWT.BORDER);
		dutyCycleText.setBounds(new org.eclipse.swt.graphics.Rectangle(76,67,94,21));
		dutyCycleText.setText("" + DirQueueMonitor.DEFAULT_DUTY_CYCLE);
		stopButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				RFIDPrintHelper.stopQueue();
				queueText.setEnabled(true);
				archiveText.setEnabled(true);
				dutyCycleText.setEnabled(true);
			}
		});
	}

}
