package com.thph.requestrobotpose.impl.servicedaemon;

import java.util.Locale;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.ContributionConfiguration;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.domain.data.DataModel;

public class MyDaemonInstallationNodeService
		implements SwingInstallationNodeService<MyDaemonInstallationNodeContribution, MyDaemonInstallationNodeView> {
	
	private final MyDaemonService myDaemonService;

	public MyDaemonInstallationNodeService(MyDaemonService myDaemonService) {

		this.myDaemonService = myDaemonService;
	}
 
	@Override
	public void configureContribution(ContributionConfiguration configuration) {

	}

	@Override
	public String getTitle(Locale locale) {
		return "My Daemon Service";
	}

	@Override
	public MyDaemonInstallationNodeView createView(ViewAPIProvider apiProvider) {
		return new MyDaemonInstallationNodeView(apiProvider);
	}

	@Override
	public MyDaemonInstallationNodeContribution createInstallationNode(InstallationAPIProvider apiProvider,
			MyDaemonInstallationNodeView view, DataModel model, CreationContext context) {
		return new MyDaemonInstallationNodeContribution(apiProvider, view, model, context,this.myDaemonService);
	}

}