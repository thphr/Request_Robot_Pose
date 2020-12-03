package com.thph.requestrobotpose.impl;

import com.thph.requestrobotpose.impl.script.ScriptCommand;
import com.thph.requestrobotpose.impl.script.ScriptSender;

public class RobotMotionRequester {
	
	private String acceleration = "0.5";
	private String stopAcceleration = "20";
	private String time = "0.5";
	private String tool_speed_x_positive = "[1.0,0.0,0.0,0.0,0.0,0.0]";
	private String tool_speed_x_negative = "[-1.0,0.0,0.0,0.0,0.0,0.0]";
	private String tool_speed_y_positive = "[0.0,1.0,0.0,0.0,0.0,0.0]";
	private String tool_speed_y_negative = "[0.0,-1.0,0.0,0.0,0.0,0.0]";
	private String tool_speed_z_positive = "[0.0,0.0,1.0,0.0,0.0,0.0]";
	private String tool_speed_z_negative = "[0.0,0.0,-1.0,0.0,0.0,0.0]";

	public enum Axis{
		Z_POSITIVE,
		Z_NEGATIVE,
		Y_POSITIVE,
		Y_NEGATIVE,
		X_POSITIVE,
		X_NEGATIVE
	}
	 
	
	public void requestRobotMove(Axis pose_direction) {
		
		switch(pose_direction) {
		case X_POSITIVE:
			moveRobot(pose_direction, tool_speed_x_positive, acceleration, time);
			break;
		case X_NEGATIVE:
			moveRobot(pose_direction, tool_speed_x_negative, acceleration, time);
			break;
		case Y_POSITIVE:
			moveRobot(pose_direction, tool_speed_y_positive, acceleration, time);
			break;
		case Y_NEGATIVE:
			moveRobot(pose_direction, tool_speed_y_negative, acceleration, time);
			break;
		case Z_POSITIVE:
			moveRobot(pose_direction, tool_speed_z_positive, acceleration, time);
			break;
		case Z_NEGATIVE:
			moveRobot(pose_direction, tool_speed_z_negative, acceleration, time);
			break;
		default:
			moveRobot(pose_direction, tool_speed_x_positive, acceleration, time);
			break;
		}
		
	}
	
	public void stopRobotMove() {
		ScriptSender sender = new ScriptSender();

		ScriptCommand senderCommand = new ScriptCommand("SenderCommand");
		senderCommand.setAsSecondaryProgram();
		senderCommand.appendLine("stopl("+ stopAcceleration+")");
		
		sender.sendScriptCommand(senderCommand);
	}
	
	private void moveRobot(Axis pose_direction, String tool_speed, String acceleration, String time) {
		
		ScriptSender sender = new ScriptSender();

		ScriptCommand senderCommand = new ScriptCommand("SenderCommand");
		senderCommand.setAsPrimaryProgram();
		senderCommand.appendLine("speedl("+ tool_speed +","+ acceleration+","+time+")");
		
		sender.sendScriptCommand(senderCommand);
	}
}
