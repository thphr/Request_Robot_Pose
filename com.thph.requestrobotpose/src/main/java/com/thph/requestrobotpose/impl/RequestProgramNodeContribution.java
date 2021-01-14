package com.thph.requestrobotpose.impl;

import java.awt.EventQueue;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.xmlrpc.XmlRpcException;

import com.thph.requestrobotpose.impl.servicedaemon.MyDaemonInstallationNodeContribution;
import com.thph.requestrobotpose.impl.servicedaemon.UnknownResponseException;
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
	
	private Timer uiTimer;

	public RequestProgramNodeContribution(ProgramAPIProvider apiProvider, RequestProgramNodeView view, DataModel model,
			CreationContext context) {

		this.programAPI = apiProvider.getProgramAPI();
		this.apiProvider = apiProvider;
		this.view = view;
		this.model = model;

	}

	@Override
	public void openView() {
		uiTimer = new Timer(true);
		uiTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						if(getInstallation().getXmlRpcDaemonInterface().isReachable()) {
							System.out.println("Programnode: It's reachable");
							try {
								System.out.println("programnode: " + getInstallation().getXmlRpcDaemonInterface().isEnabled());
								if(getInstallation().getXmlRpcDaemonInterface().isEnabled()) {
									view.openPopopView(view.getProgramnnodeViewPanel());
								}
							} catch (XmlRpcException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (UnknownResponseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					
					}
				});
			}
		}, 0, 1000);
	}

	@Override
	public void closeView() {
	}

	@Override
	public String getTitle() {
		return "Request Robot Pose";
	}

	@Override
	public boolean isDefined() {
		return true;
	}

	@Override
	public void generateScript(ScriptWriter writer) {
//		writer.appendLine("def myprogram():");
//		writer.appendLine(getInstallation().getXMLRPCVariable() + ".showpopup()");
//		writer.assign("isEnabled", "True");
//		writer.appendLine("while(isEnabled == True):");
//		writer.assign("isEnabled", getInstallation().getXMLRPCVariable()+".isEnabled()");
//		writer.appendLine("end");
//		writer.appendLine("end");
		
	}

	public MyDaemonInstallationNodeContribution getInstallation() {
		return apiProvider.getProgramAPI().getInstallationNode(MyDaemonInstallationNodeContribution.class);
	}

}