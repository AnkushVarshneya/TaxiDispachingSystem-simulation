/**
 * @(#)StatisticsDialog.java
 * Assignment#4
 * @author Ankush Varshneya
 * @student# 100853074
 * @version 1.00 2012/3/16
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class StatisticsDialog extends JDialog {

	// This is a pointer to the DispatchCenter that is being edited
	private DispatchCenter	model;
	// JComponents
	private JLabel			aLabel;
	private JTextField[][]	stats;

    public StatisticsDialog(JFrame owner, String title, boolean modal, DispatchCenter aModel) {
    	super(owner, title, modal);
    	this.model = aModel;

    	// Choose gridbag layout
		GridBagLayout layout = new GridBagLayout();
		this.getContentPane().setLayout(layout);
		GridBagConstraints constraints = new GridBagConstraints();

		//set up from/to lable
		this.aLabel = new JLabel("FROM \\ TO");
    	constraints = this.makeConstraints(0, 0, 1, 1, 0, 0, new Insets(4, 4, 4, 4), GridBagConstraints.HORIZONTAL, GridBagConstraints.SOUTHEAST);
    	layout.setConstraints(this.aLabel, constraints);
		this.getContentPane().add(this.aLabel);

		int counter = 0;
    	for(String S: model.getAreaNames()) {
			// Set up all area labels
    		this.aLabel = new JLabel(S);
    		this.aLabel.setHorizontalAlignment(JLabel.CENTER);
    		constraints = this.makeConstraints(counter+1, 0, 1, 1, 0, 0, new Insets(4, 4, 4, 4), GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
    		layout.setConstraints(this.aLabel, constraints);
			this.getContentPane().add(this.aLabel);

			this.aLabel = new JLabel(S);
			this.aLabel.setHorizontalAlignment(JLabel.RIGHT);
    		constraints = this.makeConstraints(0, counter+1, 1, 1, 0, 0, new Insets(4, 4, 4, 4), GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
    		layout.setConstraints(this.aLabel, constraints);
			this.getContentPane().add(this.aLabel);
			counter++;
    	}
		this.stats = new JTextField[model.getAreaNames().size()][model.getAreaNames().size()];
    	for(int col=0; col<model.getAreaNames().size(); col++) {
    		for(int row=0; row<model.getAreaNames().size(); row++) {
				this.stats[col][row] = new JTextField(this.model.getAreaNames().get(row) + " to " + this.model.getAreaNames().get(col));
				this.stats[col][row].setEditable(false);
				this.stats[col][row].setHorizontalAlignment(JTextField.RIGHT);
				constraints = this.makeConstraints(col + 1, row + 1, 1, 1, 1, 1, new Insets(4, 4, 4, 4), GridBagConstraints.BOTH, GridBagConstraints.CENTER);
    			layout.setConstraints(this.stats[col][row], constraints);
    			this.getContentPane().add(this.stats[col][row]);
			}
    	}

    	this.update();

		// Ensure that the dialog box appears close to the main window and initiate a size
    	setLocationRelativeTo(owner);
		setSize(800, 400);
    }

    public void update() {
    	JTextField lowest, highest, current;
    	lowest = highest = this.stats[0][0];

    	for(int col=0; col<model.getAreaNames().size(); col++) {
    		for(int row=0; row<model.getAreaNames().size(); row++) {
    			 current = this.stats[col][row];
    			 current.setText(this.model.getFromTOCounterFor(this.model.getAreaNames().get(row), this.model.getAreaNames().get(col))+"");
    			 if(Integer.parseInt(current.getText())>Integer.parseInt(highest.getText()))
    			 	highest = current;
    			  if(Integer.parseInt(current.getText())<Integer.parseInt(lowest.getText()))
    			 	lowest = current;
    		}
    	}
    	highest.setBackground(Color.green);
    	lowest.setBackground(Color.red);
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



}