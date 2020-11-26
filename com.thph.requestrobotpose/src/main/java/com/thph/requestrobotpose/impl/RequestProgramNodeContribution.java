package com.thph.requestrobotpose.impl;


import com.thph.requestrobotpose.impl.daemon.MyDaemonInstallationNodeContribution;
import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.domain.ProgramAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;


public class RequestProgramNodeContribution implements ProgramNodeContribution {

	private final ProgramAPI programAPI;
	private final ProgramAPIProvider apiProvider;
	private final RequestProgramNodeView view;
	private final DataModel model;
	

	public RequestProgramNodeContribution(ProgramAPIProvider apiProvider, RequestProgramNodeView view, DataModel model, CreationContext context) {
	
		this.programAPI = apiProvider.getProgramAPI();
		this.apiProvider = apiProvider;
		this.view = view;
		this.model = model;
		
	}

	@Override
	public void openView() {
	} 


	@Override
	public void closeView() {
		// TODO Auto-generated method stub
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Request Robot Pose";
	}

	@Override
	public boolean isDefined() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void generateScript(ScriptWriter writer) {
//		writer.assign("mydaemon_showpopup", getInstallation().getXMLRPCVariable() + ".showpopup");
//		writer.assign("mydaemon_cancel", getInstallation().getXMLRPCVariable() + ".cancelpopup()");
		writer.appendLine("# Connect to XMLRPC  server" + "mydaemon_showpopup");
		
	}
	
	private MyDaemonInstallationNodeContribution getInstallation(){
		return apiProvider.getProgramAPI().getInstallationNode(MyDaemonInstallationNodeContribution.class);
	}

} 