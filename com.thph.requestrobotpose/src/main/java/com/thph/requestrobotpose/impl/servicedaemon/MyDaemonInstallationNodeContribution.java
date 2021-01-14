package com.thph.requestrobotpose.impl.servicedaemon;

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

	private final MyDaemonService myDaemonService;
	private XmlRPCdaemonInterface xmlRPCdaemonInterface;
	private final MyDaemonInstallationNodeView view;
	private DataModel model;

	private Timer uiTimer;
	private boolean pauseTimer;
	public static final int PORT = 40405;
	private static final String ENABLED_KEY = "enabled";
	private static final String XMLRPC_VARIABLE = "my_daemon_swing";

	public MyDaemonInstallationNodeContribution(InstallationAPIProvider apiProvider, MyDaemonInstallationNodeView view,
			DataModel model, CreationContext context, MyDaemonService myDaemonService) {
		this.pauseTimer = false;
		
		this.myDaemonService = myDaemonService;
		this.view = view;
		this.model = model;
		this.xmlRPCdaemonInterface = new XmlRPCdaemonInterface("127.0.0.1", PORT);

		applyDesiredDaemonStatus();

	}


	private boolean getCB() {
		return Boolean.valueOf((model.get(ENABLED_KEY, true)));
	}

	@Override
	public void openView() {
		if (getCB() && (DaemonContribution.State.STOPPED == this.myDaemonService.getDaemon().getState())) {
			this.myDaemonService.getDaemon().start();
		} else if (getCB() == false) {
			this.myDaemonService.getDaemon().stop();
		}

		// UI updates from non-GUI threads must use EventQueue.invokeLater (or
		// SwingUtilities.invokeLater)
		uiTimer = new Timer(true);
		uiTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						updateUI();
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
	public void generateScript(ScriptWriter writer) {
		writer.assign(XMLRPC_VARIABLE, "rpc_factory(\"xmlrpc\", \"http://127.0.0.1:40405/RPC2\")");
		writer.appendLine(XMLRPC_VARIABLE + ".showpopup()");
	}


	private void updateUI() {
		DaemonContribution.State state = getDaemonState();

		if (state == DaemonContribution.State.RUNNING || state == DaemonContribution.State.ERROR) {
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
		return this.myDaemonService.getDaemon().getState();

	}

	private Boolean isDaemonEnabled() {
		return  Boolean.valueOf(model.get(ENABLED_KEY, true)); // This daemon is enabled by default
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
		return this.xmlRPCdaemonInterface;
	}

	private void applyDesiredDaemonStatus() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (isDaemonEnabled().booleanValue()) {
					// Download the daemon settings to the daemon process on initial start for
					// real-time preview purposes
					System.out.println("Starting daemon");
					try {
						pauseTimer = true;
						awaitDaemonRunning(5000);
						boolean test = xmlRPCdaemonInterface.isReachable();
						if(test) {
							System.out.println("Daemon is running");
						}else {
							System.out.println("Daemon is not running");
						}
					} catch (Exception e) {
						System.err.println("Could not reach the daemon process.");
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
		this.myDaemonService.getDaemon().start();
		long endTime = System.nanoTime() + timeOutMilliSeconds * 1000L * 1000L;
		while (System.nanoTime() < endTime
				&& (this.myDaemonService.getDaemon().getState() != DaemonContribution.State.RUNNING
						|| !xmlRPCdaemonInterface.isReachable())) {

			Thread.sleep(100);

		}
	}

	public String getXMLRPCVariable() {
		return XMLRPC_VARIABLE;
	}

}