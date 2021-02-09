package com.thph.requestrobotpose.impl;

import java.util.Locale;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.data.DataModel;


public class RequestProgramNodeService implements SwingProgramNodeService<RequestProgramNodeContribution, RequestProgramNodeView> {

	@Override
	public String getId() {

		return "Request_Pose";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {

		configuration.setChildrenAllowed(false);
	
	} 

	@Override
	public String getTitle(Locale locale) {
	
		return "Request Pose";
	}

	@Override
	public RequestProgramNodeView createView(ViewAPIProvider apiProvider) {
		return new RequestProgramNodeView();

	}

	@Override
	public RequestProgramNodeContribution createNode(ProgramAPIProvider apiProvider, RequestProgramNodeView view,
			DataModel model, CreationContext context) {

		return new RequestProgramNodeContribution(apiProvider, view, model, context);
	}

}