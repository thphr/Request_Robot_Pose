package com.thph.requestrobotpose.impl;

import org.apache.xmlrpc.XmlRpcException;

import com.thph.requestrobotpose.impl.servicedaemon.UnknownResponseException;
import com.thph.requestrobotpose.impl.servicedaemon.XmlRPCdaemonInterface;

public class TestMYProgram {

	public static void main(String[] args) {

		StringBuilder scriptCommandBuilder = new StringBuilder();
		String name = "feature_rot";
		
		scriptCommandBuilder.append( "local "+name+"="+ "[0,0,0,0,0,0]\n"); 
		scriptCommandBuilder.append(name+"[0]=0\n");
		scriptCommandBuilder.append(name+"[1]=0\n");
		scriptCommandBuilder.append(name+"[2]=0\n");
		scriptCommandBuilder.append("speedl(pose_trans(feature_rot, tool_speed), acceleration,time)");
		
		System.out.println(scriptCommandBuilder.toString());
		
//		int PORT = 40405;
//		XmlRPCdaemonInterface xmlRPCdaemonInterface = new XmlRPCdaemonInterface("127.0.0.1", PORT);
//		
//		if(xmlRPCdaemonInterface.isReachable()) {
//			try {
//				xmlRPCdaemonInterface.setDirectionEnabled("zNegative", false);
//				boolean result = xmlRPCdaemonInterface.getDirectionEnabled("zNegative");
//				System.out.println("Result: " + result);
//			} catch (XmlRpcException e) {
//				System.out.println("Not possible to execute");
//				e.printStackTrace();
//			} catch (UnknownResponseException e) {
//				System.out.println("UNKNOWN");
//				e.printStackTrace();
//			}
//		}
	}

}
