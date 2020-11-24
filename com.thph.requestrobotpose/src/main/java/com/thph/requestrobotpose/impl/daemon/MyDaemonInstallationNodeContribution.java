package com.thph.requestrobotpose.impl.daemon;

import java.awt.EventQueue;
import java.util.Timer;
import java.util.TimerTask;



import com.ur.urcap.api.contribution.DaemonContribution;
import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;

public class MyDaemonInstallationNodeContribution implements InstallationNodeContribution {
	
	private MyDaemonService myDaemonService;
	private XmlRPCdaemonInterface xmlRPCdaemonInterface;
	private final MyDaemonInstallationNodeView view;
	private DataModel model;
	
	private Timer uiTimer;
	private boolean pauseTimer = false;
	public static final int PORT = 40405;
	private static final String ENABLED_KEY = "enabled";
	

	public MyDaemonInstallationNodeContribution(InstallationAPIProvider apiProvider, MyDaemonInstallationNodeView view,
			DataModel model, CreationContext context,MyDaemonService myDaemonService) {
		this.myDaemonService = myDaemonService;
		this.view = view;
		this.model = model;
		this.xmlRPCdaemonInterface = new XmlRPCdaemonInterface("127.0.0.1", PORT);
	
	} 

	@Override
	public void openView() {
		//UI updates from non-GUI threads must use EventQueue.invokeLater (or SwingUtilities.invokeLater)
		uiTimer = new Timer(true);
		uiTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						if (!pauseTimer) {
							updateUI();
						}
					}
				});
			}
		}, 0, 1000);
	}

	@Override
	public void closeView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void generateScript(ScriptWriter writer) {
		// TODO Auto-generated method stub

	}
	
	private void updateUI() {
		DaemonContribution.State state = getDaemonState();

		if (state == DaemonContribution.State.RUNNING) {
			view.setStartButtonEnabled(false);
			view.setStopButtonEnabled(true);
		} else {
			view.setStartButtonEnabled(true);
			view.setStopButtonEnabled(false);
		}

		String text = "";
		switch (state) {
		case RUNNING:
			text = "My Daemon Swing runs";
			break;
		case STOPPED:
			text = "My Daemon Swing stopped";
			break;
		case ERROR:
			text = "My Daemon Swing failed";
			break;
		}

		view.setStatusLabel(text);
	}
	
	private DaemonContribution.State getDaemonState() {
		return myDaemonService.getDaemon().getState();
	}

	private Boolean isDaemonEnabled() {
		return model.get(ENABLED_KEY, true); //This daemon is enabled by default
	}
	
	public void onStartClick() {
		model.set(ENABLED_KEY, true);
		applyDesiredDaemonStatus();
	}

	public void onStopClick() {
		model.set(ENABLED_KEY, false);
		applyDesiredDaemonStatus();
	}
	public XmlRPCdaemonInterface getXmlRpcDaemonInterface() {
		return xmlRPCdaemonInterface;
	}
	
	private void applyDesiredDaemonStatus() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (isDaemonEnabled()) {
					// Download the daemon settings to the daemon process on initial start for real-time preview purposes
					try {
						pauseTimer = true;
						awaitDaemonRunning(5000);
						xmlRPCdaemonInterface.showPopup();
					} catch(Exception e){
						System.err.println("Could not set the title in the daemon process.");
					} finally {
						pauseTimer = false;
					}
				} else {
					myDaemonService.getDaemon().stop();
				}
			}
		}).start();
	}
	
	private void awaitDaemonRunning(long timeOutMilliSeconds) throws InterruptedException {
		myDaemonService.getDaemon().start();
		long endTime = System.nanoTime() + timeOutMilliSeconds * 1000L * 1000L;
		while(System.nanoTime() < endTime && (myDaemonService.getDaemon().getState() != DaemonContribution.State.RUNNING || !xmlRPCdaemonInterface.isReachable())) {
			Thread.sleep(100);
		}
	}

}