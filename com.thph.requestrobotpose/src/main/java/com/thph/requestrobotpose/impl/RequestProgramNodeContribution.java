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
	
	private RobotMotionRequester robotMotionRequester;
	
	private static final String Z_NEGATIVE = "zNegative";
	private static final String Z_POSITIVE = "zPositive";
	
	private static final String Y_NEGATIVE = "yNegative";
	private static final String Y_POSITIVE = "yPositive";
	
	private static final String X_NEGATIVE = "xNegative";
	private static final String X_POSITIVE = "xPositive";
	
	private boolean isPopupStillEnabled;

	private Timer uiTimer;

	public RequestProgramNodeContribution(ProgramAPIProvider apiProvider, RequestProgramNodeView view, DataModel model,
			CreationContext context) {

		this.programAPI = apiProvider.getProgramAPI();
		this.apiProvider = apiProvider;
		this.view = view;
		this.model = model;
		this.isPopupStillEnabled = false;
		
		this.robotMotionRequester = new RobotMotionRequester();


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
						if (getInstallation().getXmlRpcDaemonInterface().isReachable()) {
							if (!isPopupStillEnabled()) {
								try {
									boolean result = getInstallation().getXmlRpcDaemonInterface().getDirectionEnabled("zNegative");
									System.out.println("ProgramnodeButton:" + result);
//									System.out.println("programnode: " + getInstallation().getXmlRpcDaemonInterface().isEnabled());
									if (getInstallation().getXmlRpcDaemonInterface().isEnabled()) {
										view.openPopopView(view.getProgramnnodeViewPanel());
										setPopupStillEnabled(true);
									}
								} catch (XmlRpcException e) {
									e.printStackTrace();
								} catch (UnknownResponseException e) {
									e.printStackTrace();
								}
							}
						}

					}
				});
			}
		}, 0, 1000);
	}

	@Override
	public void closeView() {
		if (uiTimer != null) {
			uiTimer.cancel();
		}
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
		writer.appendLine(getInstallation().getXMLRPCVariable() + ".showpopup()");
		
		//Needs a thread that runs a while-loop to check the response when popup should be cancel
		writer.assign("isEnabled", "True");
		writer.appendLine("while(isEnabled == True):");
		writer.assign("isEnabled", getInstallation().getXMLRPCVariable()+".isEnabled()");
		
		writer.assign("zNegativeEnabled", getInstallation().getXMLRPCVariable()+".getDirectionEnabled(\""+Z_NEGATIVE+"\")");
//		writer.assign("zPositiveEnabled", getInstallation().getXMLRPCVariable()+".getDirection("+Z_POSITIVE+")");
//		writer.assign("yNegativeEnabled", getInstallation().getXMLRPCVariable()+".getDirection("+Y_NEGATIVE+")");
//		writer.assign("yPositiveEnabled", getInstallation().getXMLRPCVariable()+".getDirection("+Y_POSITIVE+")");
//		writer.assign("xNegativeEnabled", getInstallation().getXMLRPCVariable()+".getDirection("+X_NEGATIVE+")");
//		writer.assign("xPositiveEnabled", getInstallation().getXMLRPCVariable()+".getDirection("+X_POSITIVE+")");
		
		writer.appendLine("if(zNegativeEnabled == True):");
		writer.appendLine("speedl(\"[0.0,0.0,-1.0,0.0,0.0,0.0]\",\""+ robotMotionRequester.getAcceleration()+ "\",\""+robotMotionRequester.getFuncionReturnTime()+"\")");
		writer.appendLine("elif(zNegativeEnabled == False):");
		writer.appendLine("stopl("+robotMotionRequester.getStopAcceleration()+")");
		writer.appendLine("end");
		
		writer.appendLine("sync()");
		writer.sleep(0.3);
		writer.appendLine("textmsg(\"test\")");
		writer.appendLine("end");
		
		
		
//		writer.appendLine("stopl(2)");

	}

	public MyDaemonInstallationNodeContribution getInstallation() {
		return apiProvider.getProgramAPI().getInstallationNode(MyDaemonInstallationNodeContribution.class);
	}

	public void setPopupStillEnabled(boolean isPopupStillEnabled) {
		this.isPopupStillEnabled = isPopupStillEnabled;
	}

	public boolean isPopupStillEnabled() {
		return isPopupStillEnabled;
	}



}