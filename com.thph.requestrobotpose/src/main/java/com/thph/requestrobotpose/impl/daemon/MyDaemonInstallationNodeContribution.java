package com.thph.requestrobotpose.impl.daemon;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;

public class MyDaemonInstallationNodeContribution implements InstallationNodeContribution {
	
	private MyDaemonService myDaemonService;
	private XmlRPCdaemonInterface xmlRPCdaemonInterface;

	public MyDaemonInstallationNodeContribution(InstallationAPIProvider apiProvider, MyDaemonInstallationNodeView view,
			DataModel model, CreationContext context,MyDaemonService myDaemonService) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void openView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void generateScript(ScriptWriter writer) {
		// TODO Auto-generated method stub

	}

}