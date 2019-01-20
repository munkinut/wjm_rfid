

import com.intermec.datacollection.rfid.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.datatransfer.*;
import javax.swing.event.*;
import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.PrintWriter;

public class RFIDTest extends    JPanel
implements ActionListener,
ListSelectionListener,
MessageDispatchEventListener
{
	private Reader m_rfid;
  	private JFrame jFrame;

	private JPopupMenu   xmlMenu;
	private JMenuItem    copy;
	private JMenuItem    execute;
	private JMenuItem    save;
	private Component    selectedComponent; 
	private MouseAdapter adapter; 
	private Clipboard    clipbd = getToolkit().getSystemClipboard();

	private JCheckBox  autoModeCheckBox;

	private JButton exitButton;
	private JButton sendCmdButton;
	private JButton resultXMLParserButton;
	private JButton executeXMLButton;
	private JButton writeXMLButton;
	private JButton setSWTTButton;
	private JButton unloadSWTTButton;
	private JButton loadSWTTButton;
	private JButton openButton;
	private JButton closeButton;

	private JLabel  msgDispatchLabel;
	private JLabel  selectedReaderLabel;
	private JList   readerList;
	private DefaultListModel readerListModel;

	private JTextArea msgDispatchTextArea;

	private JTextArea cmdResultTextArea;
	private JTextArea xmlTextArea;
	private JTextField swttFileName;

	private JComboBox cmdList;

	private JLabel versionLabel;

	private JScrollPane readerListScrollPane;
	private JScrollPane msgDispatchListScrollPane;
	private JScrollPane cmdResultScrollPane;
	private JScrollPane xmlScrollPane;



	String [] commands = { 
		"Identify",
		"Identify where SWTT=0000000000AA",
		"Identify * where SWTT=0000000000AF",
		"Read *",
		"Read * where SWTT=0000000000AA",
		"Read * where TagId=0300000000000000",
		"Read Field(4)",
		"Read Field(1) Field(2)",
		"Read Field(1,3) where SWTT=\"0000\"",
		"Read Field(1)  Mem(JustSay,80,5,s)"
	};

	/**
	 * 
	 */
	public RFIDTest()
	{

		super();

			m_rfid = new Reader ();
			m_rfid.addMessageDispatchListener (this);

		autoModeCheckBox = new JCheckBox ("Autonomous Mode");
		autoModeCheckBox.setMnemonic('A');
		autoModeCheckBox.setSelected (false);
		autoModeCheckBox.setEnabled (false);

		msgDispatchLabel = new JLabel ("Message Dispatcher");
		selectedReaderLabel = new JLabel ("No Reader Selected");

		sendCmdButton = new JButton ("Send command");
		sendCmdButton.addActionListener(this);
		sendCmdButton.setActionCommand("SendCommand");
		sendCmdButton.setMnemonic('S');

		resultXMLParserButton = new JButton ("Result XML Parser");
		resultXMLParserButton.addActionListener(this);
		resultXMLParserButton.setActionCommand("ResultXMLParser");
		resultXMLParserButton.setMnemonic('R');

		executeXMLButton = new JButton ("Execute XML document");
		executeXMLButton.addActionListener(this);
		executeXMLButton.setActionCommand("ExecuteXML");
		executeXMLButton.setMnemonic('E');

		unloadSWTTButton = new JButton ("Unload SWTT");
		unloadSWTTButton.addActionListener(this);
		unloadSWTTButton.setActionCommand("UnloadSWTT");
		unloadSWTTButton.setMnemonic('U');

		loadSWTTButton = new JButton ("Load SWTT");
		loadSWTTButton.addActionListener(this);
		loadSWTTButton.setActionCommand("LoadSWTT");
		loadSWTTButton.setMnemonic('L');

		writeXMLButton = new JButton ("Write");
		writeXMLButton.addActionListener(this);
		writeXMLButton.setActionCommand("WriteXML");
		writeXMLButton.setMnemonic('W');

		setSWTTButton = new JButton ("Set SWTT");
		setSWTTButton.addActionListener(this);
		setSWTTButton.setActionCommand("SetSWTT");
		setSWTTButton.setMnemonic('e');

		openButton = new JButton ("Open");
		openButton.addActionListener(this);
		openButton.setActionCommand("OpenReader");
		openButton.setMnemonic('O');

		closeButton = new JButton ("Close");
		closeButton.addActionListener(this);
		closeButton.setActionCommand("CloseReader");
		closeButton.setMnemonic('C');
		closeButton.setEnabled (false);

		cmdList = new JComboBox (commands);
		cmdList.setEditable (true);
		cmdList.setPreferredSize (new Dimension (10, 14));
		cmdList.addActionListener (this);

		readerListModel = new DefaultListModel();
		String reader = this.m_rfid.findFirstReader();
		while (reader.length() > 0)
		{
			System.out.println("Reader = [" + reader + "]");
			readerListModel.addElement (reader);
			reader = this.m_rfid.findNextReader();
		}

		readerList = new JList (readerListModel);
		readerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		readerList.setVisibleRowCount(6);
		readerList.addListSelectionListener (this);
		readerList.setSelectedIndex(0);
		readerListScrollPane = new JScrollPane(readerList);
		readerListScrollPane.setPreferredSize(new Dimension(260, 10));
		//XXX: Must do the following, too, or else the scroller thinks
		//XXX: it's taller than it is:
		readerListScrollPane.setMinimumSize(new Dimension(260, 10));
		readerListScrollPane.setAlignmentX(LEFT_ALIGNMENT);

		msgDispatchTextArea = new JTextArea (20, 32);
		msgDispatchTextArea.setEditable (false);
		msgDispatchListScrollPane = new JScrollPane(msgDispatchTextArea);
		msgDispatchListScrollPane.setPreferredSize(new Dimension(260, 80));
		//XXX: Must do the following, too, or else the scroller thinks
		//XXX: it's taller than it is:
		msgDispatchListScrollPane.setMinimumSize(new Dimension(260, 80));
		msgDispatchListScrollPane.setAlignmentX(LEFT_ALIGNMENT);


		cmdResultTextArea = new JTextArea (15, 60);
		cmdResultTextArea.setEditable (false);
		cmdResultScrollPane = new JScrollPane(cmdResultTextArea,
											  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
											  JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		xmlTextArea = new JTextArea (15, 60);
		xmlTextArea.setEditable (true);
		xmlScrollPane = new JScrollPane(xmlTextArea,
										JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
										JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		//Create the popup menu to be used in this text area
		makePopup(); 
    	xmlTextArea.addMouseListener( adapter ); 


		swttFileName = new JTextField(20);

		//Initialize the state of the push buttons
		setButtons (false);

		//Get and Display the version of the ITCRFID dll we are using
		versionLabel = new JLabel ("ITCRFID.DLL v0.0");
		String versionStr = m_rfid.getITCRFIDversion ();
		versionLabel.setText ("ITCRFID.DLL v" + versionStr);
	}
	private void setButtons (boolean bVal)
	{
		closeButton.setEnabled (bVal);
		sendCmdButton.setEnabled (bVal);
		resultXMLParserButton.setEnabled (bVal);
		loadSWTTButton.setEnabled (bVal);
		unloadSWTTButton.setEnabled (bVal);
		executeXMLButton.setEnabled (bVal);
		writeXMLButton.setEnabled (bVal);
		setSWTTButton.setEnabled (bVal);
	}

	public void actionPerformed (ActionEvent e)
	{
		if ("OpenReader".equals(e.getActionCommand()))
		{
			System.out.println ("Open Button");
			openButton.setEnabled (false);
			if (m_rfid.open(selectedReaderLabel.getText ()))
			{
				String myswtFileName = m_rfid.ReaderProperty.getSWTTfilename();
				System.out.println ("SWTT Filename = [" + myswtFileName + "]");
				swttFileName.setText (myswtFileName);
   			    m_rfid.setUseIP3ScannerHandle(true);
				m_rfid.setCommunicationTimeout (10);
				setButtons (true);
			}
			else
			{
				JOptionPane.showMessageDialog (this, "Error opening reader");
				setButtons (false);
				openButton.setEnabled (true);
			}
		}
		else if ("CloseReader".equals(e.getActionCommand()))
		{
			System.out.println ("Close Button");
			m_rfid.close ();
			setButtons (false);
			openButton.setEnabled (true);
   	    }
		else if ("SendCommand".equals(e.getActionCommand()))
		{
			System.out.println ("Send Command, " + cmdList.getSelectedItem().toString());
			sendCmdButton.setEnabled (false);
			cmdResultTextArea.setText ("");
			String s = m_rfid.command (cmdList.getSelectedItem().toString());
			if (s != null)
			{
				cmdResultTextArea.setText (s);
			}
			sendCmdButton.setEnabled (true);
		}
		else if ("ResultXMLParser".equals(e.getActionCommand()))
		{
			System.out.println ("Result XML Parser");

			String s = cmdResultTextArea.getText ();
			if (s.length() > 0)
			{
				XMLResultDialog myXMLDialog = new XMLResultDialog(jFrame, "XML Data", cmdResultTextArea.getText ());
				myXMLDialog.setLocationRelativeTo(null);   	
				myXMLDialog.setVisible (true);
			}
		}
		else if ("LoadSWTT".equals(e.getActionCommand()))
		{
			System.out.println ("Load SWTT");
			if (!m_rfid.loadSWTT())
			{
				JOptionPane.showMessageDialog (this, "Error in loadSWTT()");
			}
		}
		else if ("UnloadSWTT".equals(e.getActionCommand()))
		{
			System.out.println ("UnloadSWTT");
			m_rfid.unloadSWTT ();
		}
		else if ("SetSWTT".equals(e.getActionCommand()))
		{
			System.out.println ("SetSWTT");
			setSWTTButton.setEnabled (false);
			if (!m_rfid.write("@setswtt.xml"))
			{
				JOptionPane.showMessageDialog (this, "Error loading setswtt.xml");
			}
			setSWTTButton.setEnabled (true);
		}
		else if ("WriteXML".equals(e.getActionCommand()))
		{
			System.out.println ("WriteXML");
			writeXMLButton.setEnabled (false);
			if (!m_rfid.write("@writeTags.xml"))
			{
				JOptionPane.showMessageDialog (this, "Error writing writeTags.xml");
			}


			writeXMLButton.setEnabled (true);
		}
		else if (("ExecuteXML".equals(e.getActionCommand())) ||
				 ("Execute XML document".equals(e.getActionCommand())))
		{
			System.out.println ("ExecuteXML");
			String s = xmlTextArea.getText ();
			if (s.length() > 0)
			{
				if (!m_rfid.write (s))
				{
					JOptionPane.showMessageDialog (this, "Error executing xml");
				}
			}
			else
			{
				JOptionPane.showMessageDialog (this, "No XML to execute. You must enter valid XML.");		
			}
		}
		else if ("New XMLdoc template".equals(e.getActionCommand()))
		{
			System.out.println ("New XMLdoc template");
			String  MyTab   = "    ";
			String  MyNL    = "\r\n";
			StringBuffer sb = new StringBuffer ();
			sb.append ("<?xml version=\"1.0\" encoding=\"utf-8\" ?>" + MyNL);
			sb.append ("<WriteTags>" + MyNL);
			sb.append (MyNL);

			sb.append (MyTab +  "<Tag>"+MyTab + "<!-- This template applies to all Tags in range -->" + MyNL);
			sb.append (MyTab +  "</Tag>" +MyNL);

			sb.append ( MyNL);


			sb.append (MyTab +  "<Tag ID=\"0123456789ABCDEF\">" + MyTab + "<!-- This template applies to ONE Tag with ID \"0123456789ABCDEF\"  -->" + MyNL );
			sb.append ( MyTab +  "</Tag>" +MyNL);

			sb.append ( MyNL);
			sb.append ( MyTab +  "<Tag SWTT=\"0123456789AB\">" + MyTab + "<!-- This template applies to all Tags with SWTT \"0123456789AB\"  -->" + MyNL );
			sb.append ( MyTab +  "</Tag>" +MyNL);
			sb.append (MyNL);
			sb.append ("</WriteTags>" +MyNL);
			xmlTextArea.setText (sb.toString ());
		}
		else if ("Clear All".equals(e.getActionCommand()))
		{
			xmlTextArea.setText ("");
		}
		else if ("Copy".equals(e.getActionCommand()))
		{
			String selection = xmlTextArea.getSelectedText ();
			StringSelection clipString = new StringSelection (selection);
			clipbd.setContents (clipString, clipString);
		}
		else if ("Paste".equals(e.getActionCommand()))
		{
			pasteIntoTextArea ();
		}
		else if ("Insert SWTTField template".equals(e.getActionCommand()))
		{
			String selection = "\r\n    <SWTTField EntryNr=\"x\" >Insert Field value here</SWTTField>";
			StringSelection clipString = new StringSelection (selection);
			clipbd.setContents (clipString, clipString);
			pasteIntoTextArea ();

		}
		else if ("Insert MEMField template".equals(e.getActionCommand()))
		{
			String selection = "\r\n    <MEMField MemStart=\"x\" MemLen=\"x\" MemType=\"string | hex | int\">Insert Field value here</MEMField>";
			StringSelection clipString = new StringSelection (selection);
			clipbd.setContents (clipString, clipString);
			pasteIntoTextArea ();
		}
		else if ("Insert SWTT template".equals(e.getActionCommand()))
		{
			String selection = "\r\n    <SWTT>0123456789AB</SWTT>";
			StringSelection clipString = new StringSelection (selection);
			clipbd.setContents (clipString, clipString);
			pasteIntoTextArea ();
		}
		else if ("Load XML Document".equals(e.getActionCommand()))
		{
			String fileName = File.separator + "xml";
			JFileChooser fc = new JFileChooser (new File(fileName));
			fc.showOpenDialog (this);
			File setFile = fc.getSelectedFile ();

			try
			{
				StringBuffer sb = new StringBuffer ();
				String lineSep = System.getProperty ("line.separator");
				String aLine = "";
				BufferedReader br = new BufferedReader(new FileReader (setFile));
				while (null != (aLine = br.readLine()))
				{
					sb.append (aLine);
					sb.append (lineSep);
				}
				br.close();
				xmlTextArea.setText (sb.toString());
			}
			catch (Exception ee)
			{
				ee.printStackTrace();
			}
		}
		else if ("Save XML Document".equals(e.getActionCommand()))
		{
			String fileName = File.separator + "xml";
			JFileChooser fc = new JFileChooser (new File(fileName));
			fc.showSaveDialog (this);
			File setFile = fc.getSelectedFile ();
			try
			{
				FileWriter fw = new FileWriter (setFile);
				PrintWriter pw = new PrintWriter (new BufferedWriter (fw));
				pw.println (xmlTextArea.getText());
				pw.close ();
			}
			catch (Exception ee)
			{
				ee.printStackTrace ();
			}
		}
	}

	void pasteIntoTextArea ()
	{

		Transferable clipData = clipbd.getContents (this);
		try
		{
			String clipString = (String)clipData.getTransferData(DataFlavor.stringFlavor);
			xmlTextArea.replaceRange(clipString, xmlTextArea.getSelectionStart(), xmlTextArea.getSelectionEnd());
		}
		catch (Exception ex)
		{
			System.out.println("not String flavor");
		}
	}

	void makePopup()
	{
		xmlMenu = new JPopupMenu("");
		xmlMenu.add (makeMenuItem("New XMLdoc template") );
		xmlMenu.add (makeMenuItem("Insert SWTTField template") );
		xmlMenu.add (makeMenuItem("Insert MEMField template") );
		xmlMenu.add (makeMenuItem("Insert SWTT template") );
		execute = makeMenuItem("Execute XML document");
		xmlMenu.add (execute);
		xmlMenu.add (makeMenuItem("Load XML Document") );
		save = makeMenuItem ("Save XML Document");
		xmlMenu.add (save);
		copy = makeMenuItem ("Copy");
		xmlMenu.add (copy );
		xmlMenu.add (makeMenuItem("Paste") );
		xmlMenu.add (makeMenuItem("Clear All") );

		// Create a MouseAdapter that creates a Popup menu
		// when the right mouse or equivalent button clicked.
		adapter = new MouseAdapter()
		{
			// On some machines, mouseReleased sets
			// PopupTrigger.
			public void mouseReleased(MouseEvent e)
			{
				if (e.isPopupTrigger())
				{
					showPopupMenu(e);
				}
			}
			// But on other machines, mousePressed sets
			// PopupTrigger.
			public void mousePressed(MouseEvent e)
			{
				if (e.isPopupTrigger())
				{
					showPopupMenu(e);
				}
			}

			public void showPopupMenu(MouseEvent e)
			{
				selectedComponent = e.getComponent();
				if (xmlTextArea.getText().length () <= 0)
				{
					copy.setEnabled (false);
					execute.setEnabled (false);
					save.setEnabled (false);
				}
				else
				{
					copy.setEnabled (true);
					execute.setEnabled (true);
					save.setEnabled (true);
				}
				// Get the component on which the button click 
				// occurred and show the menu there.
				xmlMenu.show( selectedComponent, 
							  e.getX(), e.getY());

			}
		};

	}

	// A utility method for making menu items.
	private JMenuItem makeMenuItem(String label)
	{
		JMenuItem item = new JMenuItem(label);
		item.addActionListener( this );
		return item;
	}

	public void valueChanged(ListSelectionEvent e) 
	{
		try
		{

			if (e.getSource() == readerList)
			{
				if (e.getValueIsAdjusting() == false)
				{

					if (readerList.getSelectedIndex() == -1)
					{
						//No selection
						selectedReaderLabel.setText ("No Reader Selected");
					}
					else
					{
						//Selection
						int idx = readerList.getSelectedIndex ();
						System.out.println ("Index = " + idx);
						String reader = (String)readerList.getSelectedValue ().toString();
						selectedReaderLabel.setText (reader); 
					}
				}
			}
		}
		catch (Exception ee)
		{
			System.out.println (ee.toString());
		}
	}


	private void addComponentsToPane (Container pane)
	{
		setLayout (new BorderLayout ());

		//Command combo box and top buttons
		JPanel topPanel = new JPanel ();
		topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10)); //top, left, bottom, right
		topPanel.setLayout (new BoxLayout (topPanel, BoxLayout.LINE_AXIS));
		topPanel.add (cmdList);
		topPanel.add (Box.createRigidArea (new Dimension (8, 0)));
		topPanel.add (sendCmdButton);
		topPanel.add (Box.createRigidArea (new Dimension (40, 0)));
		topPanel.add (resultXMLParserButton);
		topPanel.add (Box.createRigidArea (new Dimension (185, 0)));
		topPanel.add (msgDispatchLabel);
		topPanel.add (Box.createRigidArea (new Dimension (68, 0)));
		add (topPanel, BorderLayout.PAGE_START);

		//Command Result and XML
		JPanel leftPanel = new JPanel();
		leftPanel.setBorder(BorderFactory.createEmptyBorder(0,10,0,5));
		leftPanel.setLayout (new BoxLayout (leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add (cmdResultScrollPane);
		leftPanel.add (Box.createRigidArea (new Dimension (0, 10)));
		leftPanel.add (xmlScrollPane);
		leftPanel.add (Box.createRigidArea (new Dimension (0, 4)));

		JPanel bottomLeftPanel = new JPanel ();
		bottomLeftPanel.setLayout (new BoxLayout (bottomLeftPanel, BoxLayout.LINE_AXIS));
		bottomLeftPanel.add (executeXMLButton);
		bottomLeftPanel.add (Box.createRigidArea (new Dimension (360, 0)));
		bottomLeftPanel.add (writeXMLButton);
		bottomLeftPanel.add (Box.createRigidArea (new Dimension (10, 0)));
		bottomLeftPanel.add (setSWTTButton);
		leftPanel.add (bottomLeftPanel);
		add (leftPanel, BorderLayout.LINE_START);

		//Dispatch Messages and Reader List
		JPanel rightPanel = new JPanel ();
		rightPanel.setBorder(BorderFactory.createEmptyBorder(0,5,0,10));
		rightPanel.setLayout (new BoxLayout (rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add (msgDispatchListScrollPane);
		rightPanel.add (Box.createRigidArea (new Dimension (0, 10)));
		rightPanel.add (readerListScrollPane);
		rightPanel.add (Box.createRigidArea (new Dimension (0, 10)));
		rightPanel.add (selectedReaderLabel);
		add (rightPanel, BorderLayout.LINE_END);

		//Bottom row of buttons
		JPanel bottomPanel = new JPanel ();
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		bottomPanel.setLayout (new BoxLayout (bottomPanel, BoxLayout.LINE_AXIS));
		bottomPanel.add (autoModeCheckBox);
		bottomPanel.add (Box.createRigidArea (new Dimension (40, 0)));
		bottomPanel.add (unloadSWTTButton);
		bottomPanel.add (Box.createRigidArea (new Dimension (5, 0)));
		bottomPanel.add (loadSWTTButton);
		bottomPanel.add (Box.createRigidArea (new Dimension (5, 0)));
		bottomPanel.add (swttFileName);
		bottomPanel.add (Box.createRigidArea (new Dimension (20, 0)));
		bottomPanel.add (versionLabel);
		bottomPanel.add (Box.createRigidArea (new Dimension (30, 0)));
		bottomPanel.add (openButton);
		bottomPanel.add (Box.createRigidArea (new Dimension (10, 0)));
		bottomPanel.add (closeButton);
		bottomPanel.add (Box.createRigidArea (new Dimension (60, 0)));
		add (bottomPanel, BorderLayout.PAGE_END);
	}
	/**
	 * Create the GUI and show it.  For thread safety, 
	 * this method should be invoked from the 
	 * event-dispatching thread.
	 */
	private void createAndShowGUI()
	{
		//Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);

		//Create and set up the window.
		jFrame = new JFrame("RFID Test");
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Create and set up the content pane.
		RFIDTest rfidTest = new RFIDTest();

		rfidTest.setOpaque(true); //content panes must be opaque
		jFrame.setContentPane(rfidTest);

		//Set up the content pane.
		rfidTest.addComponentsToPane(jFrame.getContentPane());


		//Display the window.
		jFrame.pack();
		jFrame.setLocationRelativeTo(null);   	
		jFrame.setVisible(true);



	}

	public void rfidEventRead (MessageDispatchEvent e)
	{
		try
		{

			System.out.println ("rfidEventRead");
			StringBuffer evSb = new StringBuffer ();
			evSb.append (e.getMessageDispatchType() + ", ");
			evSb.append (e.getMessageSubSystem() + ", ");
			evSb.append (e.toString ());
			StringBuffer sb = new StringBuffer ();
			sb.append ("DISPATCH EVENT - " + e.toString());
			if (e.getMessageErrorValue() != 0)
			{
				System.out.println ("DISPATCH EVENT - " + e.toString() + ", " + e.getMessageErrorValue());
				sb.append (", " + m_rfid.getLastErrorMessage(e.getMessageErrorValue()));
				evSb.append (", " + m_rfid.getLastErrorMessage(e.getMessageErrorValue()));
			}
			else
			{
				System.out.println ("DISPATCH EVENT - " + e.toString());
			}
			evSb.append ("\r\n");
			msgDispatchTextArea.insert (evSb.toString(), 0);
		}
		catch (Exception ee)
		{
			System.out.println (ee.toString());
		}
	}

	public static void main(String[] args) 
	{
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.

		System.out.println ("RFIDTest.main");
		final RFIDTest rfidTest = new RFIDTest ();
		System.out.println ("RFIDTest.main.addMessageDispatcher");

		javax.swing.SwingUtilities.invokeLater(new Runnable()
		   {
			   public void run()
			   {
				   try
				   {
					   rfidTest.createAndShowGUI(); 
				   }
				   catch (Exception e)
				   {
					   System.out.println (e.toString());
				   }
			   }
		   });
	}


}
