package com.thph.requestrobotpose.impl;


import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import javax.swing.JPanel;
import javax.swing.BoxLayout;

public class RequestProgramNodeView implements SwingProgramNodeView<RequestProgramNodeContribution> {

	public RequestProgramNodeView() {

	}

	@Override
	public void buildUI(JPanel panel, ContributionProvider<RequestProgramNodeContribution> provider) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

	}


}