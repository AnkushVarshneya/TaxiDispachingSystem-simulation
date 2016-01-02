/**
 * @(#)TaxiServiceDispatchSystem.java
 * Assignment#4
 * @author Ankush Varshneya
 * @student# 100853074
 * @version 1.00 2012/3/16
 */

import java.awt.*; //Needed for Component.
import java.awt.event.*; // Needed for ActionListener.
import javax.swing.*; // Needed for JFrame.
import javax.swing.event.*; // Needed for ListSelectionListener, DocumentListener.
import java.util.*; // Needed for arraylist, Math.random

public class TaxiServiceDispatchSystem extends JFrame implements ActionListener {
	private javax.swing.Timer			aTimer;
	private DispatchCenter				model;
	private	ArrayList<ClientRequest>	requests;

	// JComponents
	private JList						incomingList;
	private JList[]						areaList;
	private JLabel						aLabel;
	private JScrollPane					aScrollPane;
	private JButton						dispachButton;

	// Store menu items for access from event handlers
	private JMenuItem		startClientsComing, stopClientsFromComing, simulationRate, statistics;

    public TaxiServiceDispatchSystem(String title) {
    	super(title); // Sets the title of the window

    	//creates a timer to simulate customer calls
    	this.aTimer = new javax.swing.Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleTimerTick();
			}});
    	this.model = new DispatchCenter(); // create a new model
		this.requests = new ArrayList<ClientRequest>(); // Initilize the requests list array
		this.areaList = new JList[this.model.getAreaNames().size()]; // Initilize the area list array

 		// Choose gridbag layout
		GridBagLayout layout = new GridBagLayout();
		this.getContentPane().setLayout(layout);
		GridBagConstraints constraints = new GridBagConstraints();

		// Create and add the main menu bar
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		// Create simulation menu and add it to the menu bar
		JMenu simulationMenu = new JMenu("Simulation");
		simulationMenu.setMnemonic('t');
		simulationMenu.add(this.startClientsComing = new JMenuItem("Start Clients Coming"));
		this.startClientsComing.addActionListener(this);
		simulationMenu.add(this.stopClientsFromComing = new JMenuItem("Stop Clients From Coming"));
		this.stopClientsFromComing.addActionListener(this);
		menuBar.add(simulationMenu);

		// Create Settings menu and add it to the menu bar
		JMenu settingsMenu = new JMenu("Settings");
		settingsMenu.setMnemonic('S');
		settingsMenu.add(this.simulationRate = new JMenuItem("Simulation Rate..."));
		this.simulationRate.addActionListener(this);
		menuBar.add(settingsMenu);

		// Create admin menu and add it to the menu bar
		JMenu adminMenu = new JMenu("Admin");
		adminMenu.setMnemonic('A');
		adminMenu.add(this.statistics = new JMenuItem("Statistics"));
		this.statistics.addActionListener(this);
		menuBar.add(adminMenu);

		// Set up Dispatch button
		this.dispachButton = new JButton("Dispatch");
		constraints = this.makeConstraints(0, 2, 1, 1, 1, 0, new Insets(4, 4, 4, 4), GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTH);
		layout.setConstraints(this.dispachButton, constraints);
		this.dispachButton.addActionListener(this);
		this.getContentPane().add(this.dispachButton);

		// Set up incoming label
		this.aLabel = new JLabel("Incoming");
		this.aLabel.setForeground(new Color(0, 110, 0)); // Set the the text green
		constraints = this.makeConstraints(0, 0, 1, 1, 0, 0, new Insets(4, 4, 4, 4), GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTH);
		layout.setConstraints(this.aLabel, constraints);
		this.getContentPane().add(this.aLabel);
		// Set up incoming list
		this.incomingList = new JList();
		this.incomingList.setForeground(new Color(0, 110, 0)); // Set the the text green
		this.incomingList.setSelectionForeground(new Color(0, 110, 0)); // Set the the text green
		constraints = this.makeConstraints(0, 1, 1, 1, 1, 1, new Insets(4, 4, 4, 4), GridBagConstraints.BOTH, GridBagConstraints.CENTER);
		this.aScrollPane = new JScrollPane(this.incomingList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		layout.setConstraints(this.aScrollPane , constraints);
		this.getContentPane().add(this.aScrollPane);

		int counter = 0;
		for(String S: model.getAreaNames()) {
			// Set up all area labels
    		this.aLabel = new JLabel(S);
    		constraints = this.makeConstraints(counter+1, 0, 1, 1, 0, 0, new Insets(4, 4, 4, 4), GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTH);
    		layout.setConstraints(this.aLabel, constraints);
			this.getContentPane().add(this.aLabel);
			// Set up all area lists
			this.areaList[counter] = new JList();
			constraints = this.makeConstraints(counter+1, 1, 1, 1, 1, 1, new Insets(4, 4, 4, 4), GridBagConstraints.BOTH, GridBagConstraints.CENTER);
			this.aScrollPane = new JScrollPane(this.areaList[counter], ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			layout.setConstraints(this.aScrollPane , constraints);
			this.getContentPane().add(this.aScrollPane);
			counter++;
    	}

		this.update();

		this.setSize(1000, 400); // Manually computed sizes.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void update() {
    	// Update incoming list
    	this.incomingList.setListData(this.requests.toArray());

    	// Update all area lists
    	int counter = 0;
		for(String S: model.getAreaNames()) {
			//System.out.println(S);
			this.areaList[counter].setListData(this.model.taxisInArea(S).toArray());
			counter++;
		}
    }

    // Used make constraints and to shorten code.
	private GridBagConstraints makeConstraints(int gX, int gY, int gW, int gH, int wX, int wY, Insets insets, int fill, int anchor) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = gX;
		c.gridy = gY;
		c.gridwidth = gW;
		c.gridheight = gH;
		c.weightx = wX;
		c.weighty = wY;
		c.insets = insets;
		c.fill = fill;
		c.anchor = anchor;
		return c;
	}

	// This is the single event handler for all the buttons.
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == startClientsComing) { this.handleStartClientsComing(); }
		else if(e.getSource() == stopClientsFromComing) { this.handleStopClientsFromComing(); }
		else if(e.getSource() == simulationRate) { this.handleSimulationRate(); }
		else if(e.getSource() == statistics) { this.handleStatistics(); }
		else if(e.getSource() == dispachButton) { this.handleDispachButton(); }
		this.update(); // Update the view.
	}

	// Event handlers functions
	private void handleTimerTick() {
		// Random number generator generates a 0 or 1, if 0 then requestGranted = true else false
		int randomNumber = (int)(Math.random()*2);
		boolean requestGranted = (randomNumber == 0)? true:false;

		System.out.println("request granted: " + requestGranted + " " + randomNumber );

		// Requests are only awnsered 1 out of 2 times.
		if(requestGranted) {
			// Ads one of 6 areas randomly as pickupLocation an dropOffLocation
			requests.add(new ClientRequest(model.getAreaNames().get((int)(Math.random()*6)), model.getAreaNames().get((int)(Math.random()*6))));
		}

		for(int i=0; i<model.getBusyTaxis().size(); i++) {
			Taxi T = model.getBusyTaxis().get(i);
			if(T.getEstimatedTimeToDestination()==0) T.setAvailable(true); // Once the time to destination  is zero the car is available agaian
			else T.decreaseEstimatedTimeToDest(); // Decrease the time per iteration
		}

		this.update(); // Update the view.
	}

	private void handleStartClientsComing() { aTimer.start(); } //Start timer.

	private void handleStopClientsFromComing() { aTimer.stop(); } // Stop timer.

	private void handleSimulationRate() {
		JOptionPane inputPane = new JOptionPane();
		String input = inputPane.showInputDialog("Iterations Per Second (1 to 20)");

		// If the cancel or close button was pressed a null string is returned we was that as a blank string to avoid null pointer exception
		if (input==null) input = "";

		// If the value entered by user is out of the range of 1 to 20 or not a number then display error message and then ask user to input again.
		if(!inRangeOf(input, 1, 20)) {
			JOptionPane.showMessageDialog(null, "Error", "Please choose a number from 1 to 20", JOptionPane.ERROR_MESSAGE);
			this.handleSimulationRate();
		}
		else this.aTimer.setDelay((int)(1000/Integer.parseInt(input)));
	}

	private void handleStatistics() {
		// Now bring up the dialog box
		StatisticsDialog dialog = new StatisticsDialog(this, "Dispach Statistics", true, this.model);
		dialog.setVisible(true);
	}

	private void handleDispachButton() {
		if (!this.requests.isEmpty())
			if(this.model.sendTaxiForRequest(this.requests.get(0))!=null) // Only allow requests if a taxi is avaliable
				this.requests.remove(0); // Remove the request after its done.
	}

	// Returns if a string s is a number between start to end
	private boolean inRangeOf(String s, int start, int end){
		for(int i = start; i<=end; i++ )
			if(s.equals(i+""))
				return true;
		return false;
	}

	// This is where the program begins.
    public static void main(String[] args) {
		new TaxiServiceDispatchSystem("Taxi Service Dispatching System").setVisible(true);
	}
}