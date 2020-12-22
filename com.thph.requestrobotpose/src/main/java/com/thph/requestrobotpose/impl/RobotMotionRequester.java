package com.thph.requestrobotpose.impl;

import java.util.Arrays;
import java.util.HashMap;

import com.thph.requestrobotpose.impl.script.ScriptCommand;
import com.thph.requestrobotpose.impl.script.ScriptSender;

public class RobotMotionRequester {
	
	private double acceleration;
	private double stopAcceleration;
	private double funcionReturnTime;

	private double[] tool_speed_map;
	private HashMap<Axis, Integer> AxisToToolMap = new HashMap<Axis, Integer>();
	
	
	public enum Axis{
		Z_Axis,
		Y_Axis,
		X_Axis
	}
	
	
	public RobotMotionRequester() {
		this.resetToolmap();
		setupAxisToToolMap();
		setAcceleration(0.5);
		setStopAcceleration(20.0);
		setFuncionReturnTime(0.5);
	}
	
	private void setupAxisToToolMap() {
		AxisToToolMap.put(Axis.Z_Axis, 2);
	
		AxisToToolMap.put(Axis.X_Axis, 0);
	
		AxisToToolMap.put(Axis.Y_Axis, 1);		
	}
	
	private void resetToolmap() {
		tool_speed_map = new double[] {0.0,0.0,0.0,0.0,0.0,0.0};
	}
	 
	private void buildRobotMoveRequest(Axis axis, double speed) {
		tool_speed_map[AxisToToolMap.get(axis)] = speed;
	}
	
	private String ConvertToolMapToString() {
		return Arrays.toString(tool_speed_map);
	}
	
	
	public void requestRobotMove(Axis axis, double speed) {
		this.resetToolmap();
		buildRobotMoveRequest(axis, speed);
		moveRobot(ConvertToolMapToString(), getAcceleration(), getFuncionReturnTime());
		
		
	}
	
	public void stopRobotMove() {
		ScriptSender sender = new ScriptSender();

		ScriptCommand senderCommand = new ScriptCommand("SenderCommand");
		senderCommand.setAsPrimaryProgram();
		senderCommand.appendLine("stopl("+ getStopAcceleration()+")");
		
		sender.sendScriptCommand(senderCommand);
	}
	
	private void moveRobot(String tool_speed, double acceleration, double time) {
		
		ScriptSender sender = new ScriptSender();

		ScriptCommand senderCommand = new ScriptCommand("SenderCommand");
		senderCommand.setAsPrimaryProgram();  
		senderCommand.appendLine("speedl("+ tool_speed +","+ acceleration+","+time+")");
		
		sender.sendScriptCommand(senderCommand);
	}

	public double getStopAcceleration() {
		return stopAcceleration;
	}

	public void setStopAcceleration(double stopAcceleration) {
		this.stopAcceleration = stopAcceleration;
	}

	public double getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	public double getFuncionReturnTime() {
		return funcionReturnTime;
	}

	public void setFuncionReturnTime(double funcionReturnTime) {
		this.funcionReturnTime = funcionReturnTime;
	}

}
