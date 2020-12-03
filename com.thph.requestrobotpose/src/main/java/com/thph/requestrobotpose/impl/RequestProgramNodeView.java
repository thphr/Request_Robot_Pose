package com.thph.requestrobotpose.impl;


import com.thph.requestrobotpose.impl.RobotMotionRequester.Axis;
import com.ur.style.URSpacingSize;
import com.ur.style.components.URButtons;
import com.ur.style.components.URSpacing;
import com.ur.test.PreviewUI;
import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.JButton;

public class RequestProgramNodeView implements SwingProgramNodeView<RequestProgramNodeContribution> {
	
	 final JPopupMenu popup = new JPopupMenu();
	 
	 //Initiate command sender.
	 final RobotMotionRequester robotMotionRequester = new RobotMotionRequester();
	 
	 //Tempoary button for triggering the popup.
	 final JButton buttontest = new JButton();
	 
	 //Style guide library
	 URButtons urButtons = new URButtons();
	 PreviewUI previewUI = new PreviewUI();
	 URSpacing urSpacing = new URSpacing();
	 URSpacingSize urSpacingSize = new URSpacingSize();
	 
	 //Create buttons
	 JButton buttonZNegative = urButtons.getSmallButtonEnabled("Z-", 100);
	 JButton buttonZPositive = urButtons.getSmallButtonEnabled("Z+", 100);
	 JButton buttonXNegative = urButtons.getSmallButtonEnabled("X-", 100);
	 JButton buttonXPositive = urButtons.getSmallButtonEnabled("X+", 100);
	 JButton buttonYPositive = urButtons.getSmallButtonEnabled("Y+", 100);
	 JButton buttonYNegative = urButtons.getSmallButtonEnabled("Y-", 100);
	 JButton buttonOK= urButtons.getSmallButtonEnabled("OK", 100);
	 

	@Override
	public void buildUI(JPanel panel, ContributionProvider<RequestProgramNodeContribution> provider) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(this.createPopup(panel));

	}
	
	 

	/**
	 * create a popup on the programnode JPanel x,y(0,0).
	 * @param panel
	 * @return
	 */
	private Box createPopup(final JPanel panel) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.CENTER_ALIGNMENT);

		//Create a jpanel with a title.
        JPanel jpanel = previewUI.AddComponentsToUI("Popup");
        jpanel.add(createButtons());
        
        //Calls the button handler.
        this.handleButtonEvents();
        
        //add the panel with buttons to the popup.
        popup.add(jpanel);
        
        buttontest.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				popup.show(panel,panel.getWidth()/4,0);
			}
		});
        
        box.add(popup);
        box.add(buttontest);
        
        return box;
	}
	
	
	/**
	 * Creates a box with five buttons.
	 * @return
	 */
	private Box createButtons() {
		Box box = Box.createVerticalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		Box boxXNegative = Box.createHorizontalBox();
		
		Box boxXPostive = Box.createHorizontalBox();
		
		Box boxZ = Box.createHorizontalBox();
		
		Box boxY = Box.createHorizontalBox();
		
		Box boxSTOP = Box.createHorizontalBox();
		
		boxZ.add(buttonZPositive);
		boxZ.add(createCustomizedHorizontalSpacing(100));
		boxZ.add(buttonZNegative);
		
		boxXNegative.add(createCustomizedHorizontalSpacing(1));
		boxXNegative.add(buttonXNegative);
		
		boxXPostive.add(createCustomizedHorizontalSpacing(1));
		boxXPostive.add(buttonXPositive);
		
		boxSTOP.add(createCustomizedHorizontalSpacing(200));
		boxSTOP.add(buttonOK);
		
		boxY.add(buttonYNegative);
		boxY.add(createCustomizedHorizontalSpacing(100));
		boxY.add(buttonYPositive);
		
		box.add(boxZ);
		box.add(urSpacing.createVerticalSpacing(50));
		box.add(boxXNegative);
		box.add(urSpacing.createVerticalSpacing(50));
		box.add(boxY);
		box.add(urSpacing.createVerticalSpacing(50));
		box.add(boxXPostive);
		box.add(urSpacing.createVerticalSpacing(50));
		box.add(boxSTOP);
		
		return box;
	}
	
	
	/**
	 * Create handlers for buttons.
	 */
	private void handleButtonEvents() {
		
		this.createChangeListener(buttonZNegative, Axis.Z_NEGATIVE);
		this.createChangeListener(buttonZPositive, Axis.Z_POSITIVE);
		this.createChangeListener(buttonYNegative, Axis.Y_NEGATIVE);
		this.createChangeListener(buttonYPositive, Axis.Y_POSITIVE);
		this.createChangeListener(buttonXNegative, Axis.X_NEGATIVE);
		this.createChangeListener(buttonXPositive, Axis.X_POSITIVE);
		
		this.buttonOK.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				robotMotionRequester.stopRobotMove();
			}
		});
		
	}
	
	/**
	 * Method for creating change listener for buttons.
	 * @param button
	 * @param IONumber
	 */
	private void createChangeListener(final JButton button, final Axis axis) {
		button.getModel().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				ButtonModel model = (ButtonModel) e.getSource();

				if (model.isEnabled()) {
					robotMotionRequester.requestRobotMove(axis);
				}

			}
		});
	}

    /**
     * Create a horizontal spacing.
     * @param spacesize
     * @return
     */
	private Component createCustomizedHorizontalSpacing(int spacesize) {
		return Box.createRigidArea(new Dimension(spacesize, 0));
	}


}