package com.thph.requestrobotpose.impl;


import com.ur.style.URSpacingSize;
import com.ur.style.components.URButtons;
import com.ur.style.components.URSpacing;
import com.ur.test.PreviewUI;
import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;

public class RequestProgramNodeView implements SwingProgramNodeView<RequestProgramNodeContribution> {
	
	 final JPopupMenu popup = new JPopupMenu();
	 
	 //Tempoary button for triggering the popup.
	 final JButton buttontest = new JButton();
	 
	 //Style guide library
	 URButtons urButtons = new URButtons();
	 PreviewUI previewUI = new PreviewUI();
	 URSpacing urSpacing = new URSpacing();
	 URSpacingSize urSpacingSize = new URSpacingSize();
	 
	 //Create buttons
	 JButton buttonUP = urButtons.getSmallButtonEnabled("UP", 100);
	 JButton buttonDOWN = urButtons.getSmallButtonEnabled("DOWN", 100);
	 JButton buttonLEFT = urButtons.getSmallButtonEnabled("LEFT", 100);
	 JButton buttonRIGHT = urButtons.getSmallButtonEnabled("RIGHT", 100);
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

		//Create a jpanel with a titel.
        JPanel jpanel = previewUI.AddComponentsToUI("Popup");
        jpanel.add(createButtons());
        popup.add(jpanel);
        
        buttontest.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				popup.show(panel,0,0);
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
		
		Box boxTOP = Box.createHorizontalBox();
		
		Box boxBottom = Box.createHorizontalBox();
		
		Box boxHorizontal = Box.createHorizontalBox();
		
		Box boxEnd = Box.createHorizontalBox();
		
		boxTOP.add(createCustomizedHorizontalSpacing(1));
		boxTOP.add(buttonUP);
		
		boxBottom.add(createCustomizedHorizontalSpacing(1));
		boxBottom.add(buttonDOWN);
		
		boxEnd.add(createCustomizedHorizontalSpacing(200));
		boxEnd.add(buttonOK);
		
		boxHorizontal.add(buttonLEFT);
		boxHorizontal.add(createCustomizedHorizontalSpacing(100));
		boxHorizontal.add(buttonRIGHT);
		
		box.add(boxTOP);
		box.add(urSpacing.createVerticalSpacing(50));
		box.add(boxHorizontal);
		box.add(urSpacing.createVerticalSpacing(50));
		box.add(boxBottom);
		box.add(urSpacing.createVerticalSpacing(100));
		box.add(boxEnd);
		
		return box;
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