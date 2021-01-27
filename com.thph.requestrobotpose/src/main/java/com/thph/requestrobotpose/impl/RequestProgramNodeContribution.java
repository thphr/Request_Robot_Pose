package com.thph.requestrobotpose.impl;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.xmlrpc.XmlRpcException;

import com.thph.requestrobotpose.impl.RobotMotionRequester.Axis;
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

		writer.assign("isEnabled", "True");
		writer.appendLine("while(isEnabled == True):");
		writer.assign("isEnabled", getInstallation().getXMLRPCVariable() + ".isEnabled()");

		writer.assign(Direction.ZNEGATIVE.label,
				getInstallation().getXMLRPCVariable() + ".getDirectionEnabled(\"" + Direction.ZNEGATIVE.label + "\")");
		writer.assign(Direction.ZPOSITIVE.label,
				getInstallation().getXMLRPCVariable() + ".getDirectionEnabled(\"" + Direction.ZPOSITIVE.label + "\")");
		writer.assign(Direction.YNEGATIVE.label,
				getInstallation().getXMLRPCVariable() + ".getDirectionEnabled(\"" + Direction.YNEGATIVE.label + "\")");
		writer.assign(Direction.YPOSITIVE.label,
				getInstallation().getXMLRPCVariable() + ".getDirectionEnabled(\"" + Direction.YPOSITIVE.label + "\")");
		writer.assign(Direction.XNEGATIVE.label,
				getInstallation().getXMLRPCVariable() + ".getDirectionEnabled(\"" + Direction.XNEGATIVE.label + "\")");
		writer.assign(Direction.XPOSITIVE.label,
				getInstallation().getXMLRPCVariable() + ".getDirectionEnabled(\"" + Direction.XPOSITIVE.label + "\")");

		
		
		setDirection(writer, Direction.ZNEGATIVE.label, Axis.Z_Axis, -0.3);
		setDirection(writer, Direction.ZPOSITIVE.label, Axis.Z_Axis, 0.3);
		setDirection(writer, Direction.YNEGATIVE.label, Axis.Y_Axis, -0.3);
		setDirection(writer, Direction.YPOSITIVE.label, Axis.Y_Axis, 0.3);
		setDirection(writer, Direction.XNEGATIVE.label, Axis.X_Axis, -0.3);
		setDirection(writer, Direction.XPOSITIVE.label, Axis.X_Axis, 0.3);
		 
		// ----------------Z
		// DIRECTION-------------------------------------------------------
//		writer.appendLine("if(" + Direction.ZNEGATIVE.label + " == True):");
//		writer.appendLine(robotMotionRequester.requestScriptRobotMove(Axis.Z_Axis, -0.3));
//		writer.appendLine("elif(" + Direction.ZNEGATIVE.label + " == False):");
//		writer.appendLine(robotMotionRequester.generateScriptStopCommand());
//		writer.appendLine("end");
//
//		writer.appendLine("if(" + Direction.ZPOSITIVE.label + " == True):");
//		writer.appendLine(robotMotionRequester.requestScriptRobotMove(Axis.Z_Axis, 0.3));
//		writer.appendLine("elif(" + Direction.ZPOSITIVE.label + " == False):");
//		writer.appendLine(robotMotionRequester.generateScriptStopCommand());
//		writer.appendLine("end");

		// ----------------Y
		// DIRECTION-------------------------------------------------------
//		writer.appendLine("if(" + Direction.YNEGATIVE.label + " == True):");
//		writer.appendLine(robotMotionRequester.requestScriptRobotMove(Axis.Y_Axis, -0.3));
//		writer.appendLine("elif(" + Direction.YNEGATIVE.label + " == False):");
//		writer.appendLine(robotMotionRequester.generateScriptStopCommand());
//		writer.appendLine("end");
//
//		writer.appendLine("if(" + Direction.YPOSITIVE.label + " == True):");
//		writer.appendLine(robotMotionRequester.requestScriptRobotMove(Axis.Y_Axis, 0.3));
//		writer.appendLine("elif(" + Direction.YPOSITIVE.label + " == False):");
//		writer.appendLine(robotMotionRequester.generateScriptStopCommand());
//		writer.appendLine("end");

		// ----------------X
		// DIRECTION-------------------------------------------------------
//		writer.appendLine("if(" + Direction.XNEGATIVE.label + " == True):");
//		writer.appendLine(robotMotionRequester.requestScriptRobotMove(Axis.X_Axis, -0.3));
//		writer.appendLine("elif(" + Direction.XNEGATIVE.label + " == False):");
//		writer.appendLine(robotMotionRequester.generateScriptStopCommand());
//		writer.appendLine("end");
//
//		writer.appendLine("if(" + Direction.XPOSITIVE.label + " == True):");
//		writer.appendLine(robotMotionRequester.requestScriptRobotMove(Axis.X_Axis, 0.3));
//		writer.appendLine("elif(" + Direction.XPOSITIVE.label + " == False):");
//		writer.appendLine(robotMotionRequester.generateScriptStopCommand());
//		writer.appendLine("end");

		writer.appendLine("sync()");
		writer.sleep(0.3);
		writer.appendLine("textmsg(\"test\")");
		writer.appendLine("end");

	}
	private void setDirection(ScriptWriter writer, String directionlabel, Axis axis, double speed) {
		writer.appendLine("if(" + directionlabel + " == True):");
		writer.appendLine(robotMotionRequester.requestScriptRobotMove(axis, speed));
		writer.appendLine("elif(" + directionlabel + " == False):");
		writer.appendLine(robotMotionRequester.generateScriptStopCommand());
		writer.appendLine("end");
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