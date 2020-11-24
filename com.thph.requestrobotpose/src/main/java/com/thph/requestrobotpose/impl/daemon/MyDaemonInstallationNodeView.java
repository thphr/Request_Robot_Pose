package com.thph.requestrobotpose.impl.daemon;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ur.style.URSpacingSize;
import com.ur.style.components.URButtons;
import com.ur.style.components.URSpacing;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeView;

public class MyDaemonInstallationNodeView implements SwingInstallationNodeView<MyDaemonInstallationNodeContribution> {
	
	private JButton startButton;
	private JButton stopButton;
	private JLabel statusLabel; 
	
	private URButtons urButtons = new URButtons();
	private URSpacing urSpacing = new URSpacing();
	private URSpacingSize urSpacingSize = new URSpacingSize();

	public MyDaemonInstallationNodeView(ViewAPIProvider apiProvider) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void buildUI(JPanel panel, MyDaemonInstallationNodeContribution contribution) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(createStatusLabel());
	} 
	
	
	private Box createStatusLabel() {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		this.startButton = urButtons.getLargeButtonEnabled("Start Daemon", 200);
		this.stopButton = urButtons.getLargeButtonEnabled("Stop Daemon", 200);
		this.statusLabel = new JLabel("My Daemon status");
		
		box.add(startButton);
		box.add(urSpacing.createHorizontalSpacing());
		box.add(stopButton);
		box.add(urSpacing.createHorizontalSpacing());
		box.add(statusLabel);
		
		return box;
		
	}
	
	
	
	public void setStartButtonEnabled(boolean enabled) {
		startButton.setEnabled(enabled);
	}

	public void setStopButtonEnabled(boolean enabled) {
		stopButton.setEnabled(enabled);
	}

	public void setStatusLabel(String text) {
		statusLabel.setText(text);
	}

}