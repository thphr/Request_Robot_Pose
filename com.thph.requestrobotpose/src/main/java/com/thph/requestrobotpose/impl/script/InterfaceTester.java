package com.thph.requestrobotpose.impl.script;

public class InterfaceTester {

	/**
	 * This class can be used to test the Exporter and Sender Hit the Play button in
	 * your IDE, to execute this
	 * 
	 */

	/**
	 * Code source: https://github.com/BomMadsen/URCap-ScriptCommunicator
	 * @author ur
	 *
	 */
	public static void main(String[] args) {
		/** 
		 * Testing Sender
		 */
		ScriptSender sender = new ScriptSender();

		ScriptCommand senderCommand = new ScriptCommand("SenderCommand");
		senderCommand.setAsPrimaryProgram();
//		senderCommand.appendLine("textmsg(\"value=\",3)");
//		senderCommand.appendLine("speedl([0.0,0.0,1.0,0.0,0.0,0.0], 0.5,1.0)");
		senderCommand.appendLine("speedl([0.0,0.0,-1.0,0.0,0.0,0.0], 0.5,1.0)");
		
		sender.sendScriptCommand(senderCommand);

//		/**
//		 * Testing Exporter
//		 */
//		ScriptExporter export = new ScriptExporter();
//
//		ScriptCommand commandString = new ScriptCommand("Command1");
//		commandString.appendLine("pose = get_actual_tcp_pose()");
//		commandString.appendLine("z_value = pose[2]");
//
//		String resultString = export.exportStringFromURScript(commandString, "z_value");
//		System.out.println("String result is: " + resultString);
//
//		ScriptCommand commandInt = new ScriptCommand("Command2");
//		commandInt.appendLine("var_1 = 25 + 17");
//
//		int resultInt = export.exportIntegerFromURScript(commandInt, "var_1");
//		System.out.println("Integer result is: " + resultInt);
	}

}