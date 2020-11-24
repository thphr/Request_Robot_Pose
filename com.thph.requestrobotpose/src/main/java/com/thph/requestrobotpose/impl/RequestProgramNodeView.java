package com.thph.requestrobotpose.impl;


import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import java.awt.Component;
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
	 final JButton button = new JButton();

	public RequestProgramNodeView() {
		
	}

	@Override
	public void buildUI(JPanel panel, ContributionProvider<RequestProgramNodeContribution> provider) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		panel.add(this.createPopup());

	}
	 

	private Box createPopup() {
		// build poup menu
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.CENTER_ALIGNMENT);
		
        // New project menu item
        JMenuItem menuItem = new JMenuItem("New Project...",
                new ImageIcon("images/newproject.png"));
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription(
                "New Project");
        popup.add(menuItem);
        
        
        button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				popup.show(button, button.getHeight(), button.getWidth());
			}
		});
        
        box.add(popup);
        box.add(button);
        
        return box;
	}
	
	
	
	
	
    private void showPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            popup.show(e.getComponent(),
                    e.getX(), e.getY());
        }
    }


}