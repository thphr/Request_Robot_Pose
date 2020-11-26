package com.thph.requestrobotpose.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.thph.requestrobotpose.impl.daemon.MyDaemonInstallationNodeService;
import com.thph.requestrobotpose.impl.daemon.MyDaemonService;
import com.ur.urcap.api.contribution.DaemonService;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;

/**
 * Hello world activator for the OSGi bundle URCAPS contribution
 *
 */
public class Activator implements BundleActivator {
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		
		System.out.println("Activator says Hello Request Robot Pose!");
		
		
		MyDaemonService myDaemonService = new MyDaemonService();
		MyDaemonInstallationNodeService myDaemonInstallationNodeService = new MyDaemonInstallationNodeService(myDaemonService);
		
		bundleContext.registerService(SwingInstallationNodeService.class,myDaemonInstallationNodeService, null);
		bundleContext.registerService(SwingProgramNodeService.class, new RequestProgramNodeService(), null);
		bundleContext.registerService(DaemonService.class, myDaemonService, null);
	}
 
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		System.out.println("Activator says Goodbye Request Robot Pose!");
	}
}

