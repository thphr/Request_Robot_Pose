package com.thph.requestrobotpose.impl;

import org.apache.xmlrpc.XmlRpcException;

import com.thph.requestrobotpose.impl.servicedaemon.UnknownResponseException;
import com.thph.requestrobotpose.impl.servicedaemon.XmlRPCdaemonInterface;

public class TestMYProgram {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int PORT = 40405;
		XmlRPCdaemonInterface xmlRPCdaemonInterface = new XmlRPCdaemonInterface("127.0.0.1", PORT);
		
		if(xmlRPCdaemonInterface.isReachable()) {
			try {
				xmlRPCdaemonInterface.setDirectionEnabled("zNegative", false);
				boolean result = xmlRPCdaemonInterface.getDirectionEnabled("zNegative");
				System.out.println("Result: " + result);
			} catch (XmlRpcException e) {
				System.out.println("Not possible to execute");
				e.printStackTrace();
			} catch (UnknownResponseException e) {
				System.out.println("UNKNOWN");
				e.printStackTrace();
			}
		}
	}

}
