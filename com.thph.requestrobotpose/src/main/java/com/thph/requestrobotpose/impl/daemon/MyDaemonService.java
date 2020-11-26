package com.thph.requestrobotpose.impl.daemon;

import java.net.MalformedURLException;
import java.net.URL;

import com.ur.urcap.api.contribution.DaemonContribution;
import com.ur.urcap.api.contribution.DaemonService;

public class MyDaemonService implements DaemonService {

	private DaemonContribution daemonContribution;

	@Override
	public void init(DaemonContribution daemonContribution) {
		this.daemonContribution = daemonContribution;
		
		try {
			daemonContribution.installResource(new URL("file:com/thph/requestrobotpose/impl/daemon/"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	} 

	@Override
	public URL getExecutable() {
		try {
			return new URL("file:com/thph/requestrobotpose/impl/daemon/popupxmlrpc.py");
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public DaemonContribution getDaemon() {
		return daemonContribution;
	}

}
