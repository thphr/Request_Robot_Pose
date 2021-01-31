package com.thph.requestrobotpose.impl;

import java.util.Arrays;
import java.util.HashMap;

import com.thph.requestrobotpose.impl.script.ScriptCommand;
import com.thph.requestrobotpose.impl.script.ScriptSender;
import com.ur.urcap.api.domain.value.Pose;

public class RobotMotionRequester {
	
	private double acceleration;
	private double stopAcceleration;
	private double funcionReturnTime;

	private double[] tool_speed_map;
	private HashMap<Axis, Integer> AxisToToolMap = new HashMap<Axis, Integer>();
	
	
	public enum Axis{
		Z_Axis,
		Y_Axis,
		X_Axis,
		RZ_Axis,
		RY_Axis,
		RX_Axis
		
	}
	
	
	public RobotMotionRequester() {
		this.resetToolmap();
		setupAxisToToolMap();
		setAcceleration(0.5);
		setStopAcceleration(20.0);
		setFuncionReturnTime(0.5);
	}
	
	/**
	 * Method to indicate which index of the toolspeed map 
	 * to modify based on the TCP vector [X, Y, Z, Rx, Ry, Rz].
	 */
	private void setupAxisToToolMap() {
		AxisToToolMap.put(Axis.Z_Axis, 2);
		
		AxisToToolMap.put(Axis.Y_Axis, 1);	
	
		AxisToToolMap.put(Axis.X_Axis, 0);
		
		
		AxisToToolMap.put(Axis.RZ_Axis, 5);
	
		AxisToToolMap.put(Axis.RY_Axis, 4);
		
		AxisToToolMap.put(Axis.RX_Axis, 3);
	}
	
	/**
	 * The toolspeed map based on 
	 * TCP vector [X, Y, Z, Rx, Ry, Rz]
	 */
	private void resetToolmap() {
		tool_speed_map = new double[] {0.0,0.0,0.0,0.0,0.0,0.0};
	}
	 
	/**
	 * Mapping the axis to the speed.
	 * @param axis
	 * @param speed
	 */
	private void buildRobotMoveRequest(Axis axis, double speed) {
		tool_speed_map[AxisToToolMap.get(axis)] = speed;
	}
	
	
	/**
	 * Converts the toolspeed map to string.
	 * @return a string of the toolspeed: TCP vector [X, Y, Z, Rx, Ry, Rz]
	 */
	private String ConvertToolMapToString() {
		return Arrays.toString(tool_speed_map);
	}
	
	
	/**
	 * Moves the robot based on the requested axis and speed
	 * by sending the script command to robot port over socket.
	 * @param axis
	 * @param speed
	 */
	public void requestRobotMove(Axis axis, double speed) {
		this.resetToolmap();
		buildRobotMoveRequest(axis, speed);
		moveRobot(ConvertToolMapToString(), getAcceleration(), getFuncionReturnTime());
	}
	
	/**
	 * Public method for generating the complete move script command.
	 * @param axis
	 * @param speed
	 * @return
	 */
	public String requestScriptRobotMove(Axis axis, double speed) {
		this.resetToolmap();
		buildRobotMoveRequest(axis, speed);
		String scriptCommand = generateScriptMoveCommand(ConvertToolMapToString(), getAcceleration(), getFuncionReturnTime());
		return scriptCommand;
	}
	
	/**
	 * Method for generating the stop command for script level.
	 * @return
	 */
	public String generateScriptStopCommand() {
		String scriptCommand = "stopl("+ getStopAcceleration()+")" ;
		return scriptCommand;
	}
	
	/**
	 * Method for generating the move command for script level.
	 * @param tool_speed
	 * @param acceleration
	 * @param time
	 * @return
	 */
	private String generateScriptMoveCommand(String tool_speed, double acceleration, double time) {
		String scriptcommand = "speedl("+ tool_speed +","+ acceleration+","+time+")";
		return scriptcommand;
	}
//	private String generateScriptMoveCommand(String tool_speed, double acceleration, double time, Pose feature) {
//		String feature_name = "feature_rot";
//		String scriptCommand = "local "+feature_name+"="+feature.toString(); 
//		scriptCommand += feature_name+"[0]=0";
//		scriptCommand += feature_name+"[1]=0";
//		scriptCommand += feature_name+"[2]=0";
//		scriptCommand += "speedl(pose_trans(feature_rot,"+ tool_speed +"),"+ acceleration+","+time+")" ;
//		return scriptCommand;
//	}
	
	
	
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
