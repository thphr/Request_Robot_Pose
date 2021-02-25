package com.thph.requestrobotpose.impl;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.*;

import org.apache.xmlrpc.XmlRpcException;

import com.thph.requestrobotpose.impl.RobotMotionRequester.Axis;
import com.thph.requestrobotpose.impl.servicedaemon.MyDaemonInstallationNodeContribution;
import com.thph.requestrobotpose.impl.servicedaemon.UnknownResponseException;
import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.domain.ProgramAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.feature.Feature;
import com.ur.urcap.api.domain.feature.FeatureModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoRedoManager;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;
import com.ur.urcap.api.domain.value.Pose;

public class RequestProgramNodeContribution implements ProgramNodeContribution {

	private static final String POPUP_TEXT = "popup";
	private static final String ASSIGNMENT_VARIABLE = "variable";

	private static final String ASSIGMENT_CHOICE = "assignment_choice";
	private static final String DEFAUTL_ASSIGMENT_CHOICE = "pose";

	private static final String SELECTED_FEATURE = "selected_feature";
	private static final String DEFAULT_SELECTED_FEATURE = "Base";

	private static final String DEFAULT_POPUP_TEXT = "default_popup";
	private static final String DEFAULT_ASSIGNMENT_VARIABLE = "default_variable";

	private HashMap<String, Pose> featureMap = new HashMap<String, Pose>();

	private final ProgramAPI programAPI;
	private final ProgramAPIProvider apiProvider;
	private final RequestProgramNodeView view;
	private final DataModel model;
	private final KeyboardInputFactory keyboardFactory;
	private final UndoRedoManager undoRedoManager;
	private final FeatureModel featuremodel;

	private RobotMotionRequester robotMotionRequester;

	private boolean isPopupStillEnabled;

	private Timer uiTimer;

	private HashMap<String, String> assignemntMap = new HashMap<String, String>();

	public RequestProgramNodeContribution(ProgramAPIProvider apiProvider, RequestProgramNodeView view, DataModel model,
			CreationContext context) {

		this.programAPI = apiProvider.getProgramAPI();
		this.apiProvider = apiProvider;
		this.featuremodel = apiProvider.getProgramAPI().getFeatureModel();
		this.keyboardFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();
		this.undoRedoManager = apiProvider.getProgramAPI().getUndoRedoManager();
		this.view = view;
		this.model = model;
		this.isPopupStillEnabled = false;

		this.robotMotionRequester = new RobotMotionRequester();

	}

	private void initMap() {
		assignemntMap.clear();
		assignemntMap.put("pose", "get_actual_tcp_pose()");
		assignemntMap.put("speed", "get_actual_tcp_speed()");
	}

	@Override
	public void openView() {
		// Gets all features.
		this.extractAllFeatures();

		this.initMap();
		uiTimer = new Timer(true);
		uiTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						if (getInstallation().getXmlRpcDaemonInterface().isReachable()) {
							if (!isPopupStillEnabled() || !view.framePopup.isVisible()) {
								try {
									if (getInstallation().getXmlRpcDaemonInterface().isEnabled()) {
										view.setTextOnShowingPopup(model.get(POPUP_TEXT, DEFAULT_POPUP_TEXT));
										view.openPopopView(view.getProgramnnodeViewPanel(), getAllFeature());
										setPopupStillEnabled(true);
									}
								} catch (XmlRpcException e) {
									e.printStackTrace();
								} catch (UnknownResponseException e) {
									e.printStackTrace();
								}
							}
						}

					}
				});
			}
		}, 0, 1000);
	}

	/**
	 * Gets all feature defined in this installation and puts its name and pose into
	 * a map.
	 */
	private void extractAllFeatures() {

		Collection<Feature> features = featuremodel.getGeomFeatures();
		features.add(this.featuremodel.getBaseFeature());
		features.add(this.featuremodel.getToolFeature());

		for (Feature feature : features) {
			featureMap.put(feature.getName(), feature.getPose());

		}

	}

	/**
	 * Adds feature names to string array.
	 * 
	 * @return string array with all feature names.
	 */
	private String[] getAllFeature() {
		String[] featureNames = new String[featureMap.size()];

		int i = 0;
		for (Map.Entry<String, Pose> entry : featureMap.entrySet()) {
			featureNames[i] = entry.getKey();
			i++;
		}

		return featureNames;
	}

	@Override
	public void closeView() {
		if (uiTimer != null) {
			uiTimer.cancel();
		}
	}

	@Override
	public String getTitle() {
		return "Request Robot Pose";
	}

	@Override
	public boolean isDefined() {
		return true;
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		writer.appendLine(getInstallation().getXMLRPCVariable() + ".showpopup()");

		writer.assign("isEnabled", "True");
		writer.appendLine("while(isEnabled == True):");
		writer.assign("isEnabled", getInstallation().getXMLRPCVariable() + ".isEnabled()");

		// TCP position
		writer.assign(Direction.ZNEGATIVE.label,
				getInstallation().getXMLRPCVariable() + ".getDirectionEnabled(\"" + Direction.ZNEGATIVE.label + "\")");
		writer.assign(Direction.ZPOSITIVE.label,
				getInstallation().getXMLRPCVariable() + ".getDirectionEnabled(\"" + Direction.ZPOSITIVE.label + "\")");
		writer.assign(Direction.YNEGATIVE.label,
				getInstallation().getXMLRPCVariable() + ".getDirectionEnabled(\"" + Direction.YNEGATIVE.label + "\")");
		writer.assign(Direction.YPOSITIVE.label,
				getInstallation().getXMLRPCVariable() + ".getDirectionEnabled(\"" + Direction.YPOSITIVE.label + "\")");
		writer.assign(Direction.XNEGATIVE.label,
				getInstallation().getXMLRPCVariable() + ".getDirectionEnabled(\"" + Direction.XNEGATIVE.label + "\")");
		writer.assign(Direction.XPOSITIVE.label,
				getInstallation().getXMLRPCVariable() + ".getDirectionEnabled(\"" + Direction.XPOSITIVE.label + "\")");

		// TCP orientation
		writer.assign(Direction.RZNEGATIVE.label,
				getInstallation().getXMLRPCVariable() + ".getDirectionEnabled(\"" + Direction.RZNEGATIVE.label + "\")");
		writer.assign(Direction.RZPOSITIVE.label,
				getInstallation().getXMLRPCVariable() + ".getDirectionEnabled(\"" + Direction.RZPOSITIVE.label + "\")");
		writer.assign(Direction.RYNEGATIVE.label,
				getInstallation().getXMLRPCVariable() + ".getDirectionEnabled(\"" + Direction.RYNEGATIVE.label + "\")");
		writer.assign(Direction.RYPOSITIVE.label,
				getInstallation().getXMLRPCVariable() + ".getDirectionEnabled(\"" + Direction.RYPOSITIVE.label + "\")");
		writer.assign(Direction.RXNEGATIVE.label,
				getInstallation().getXMLRPCVariable() + ".getDirectionEnabled(\"" + Direction.RXNEGATIVE.label + "\")");
		writer.assign(Direction.RXPOSITIVE.label,
				getInstallation().getXMLRPCVariable() + ".getDirectionEnabled(\"" + Direction.RXPOSITIVE.label + "\")");

		// TCP position
		setDirection(writer, Direction.ZNEGATIVE.label, Axis.Z_Axis, -0.3);
		setDirection(writer, Direction.ZPOSITIVE.label, Axis.Z_Axis, 0.3);
		setDirection(writer, Direction.YNEGATIVE.label, Axis.Y_Axis, -0.3);
		setDirection(writer, Direction.YPOSITIVE.label, Axis.Y_Axis, 0.3);
		setDirection(writer, Direction.XNEGATIVE.label, Axis.X_Axis, -0.3);
		setDirection(writer, Direction.XPOSITIVE.label, Axis.X_Axis, 0.3);

		// TCP orientation
		setDirection(writer, Direction.RZNEGATIVE.label, Axis.RZ_Axis, -0.3);
		setDirection(writer, Direction.RZPOSITIVE.label, Axis.RZ_Axis, 0.3);
		setDirection(writer, Direction.RYNEGATIVE.label, Axis.RY_Axis, -0.3);
		setDirection(writer, Direction.RYPOSITIVE.label, Axis.RY_Axis, 0.3);
		setDirection(writer, Direction.RXNEGATIVE.label, Axis.RX_Axis, -0.3);
		setDirection(writer, Direction.RXPOSITIVE.label, Axis.RX_Axis, 0.3);

		writer.appendLine("sync()");
		writer.sleep(0.3);
		writer.appendLine("textmsg(\"test\")");
		writer.appendLine("end");

		// Assignment of the pose when the OK button is pressed.
		assignVariable(writer, model.get(ASSIGNMENT_VARIABLE, DEFAULT_ASSIGNMENT_VARIABLE),
				model.get(ASSIGMENT_CHOICE, DEFAUTL_ASSIGMENT_CHOICE));

	}

	/**
	 * Method for assigning a variable for the chosen assignment option.
	 * 
	 * @param writer
	 * @param assignment_choice
	 */
	private void assignVariable(ScriptWriter writer, String assignment_variable, String assignment_choice) {
		writer.appendLine("if(isEnabled != True):");
		writer.assign(assignment_variable, assignemntMap.get(assignment_choice));
		writer.appendLine("end");
	}

	/**
	 * Movethe robot based on the triggered button with the corresponding moving
	 * direction and stops the robot when the button is longer pressed down.
	 * 
	 * @param writer
	 * @param directionlabel
	 * @param axis
	 * @param speed
	 */
	private void setDirection(ScriptWriter writer, String directionlabel, Axis axis, double speed) {
		writer.appendLine("if(" + directionlabel + " == True):");
		writer.appendLine(robotMotionRequester.requestScriptRobotMove(axis, speed, getPoseBasedOnFeature()));
		writer.appendLine("elif(" + directionlabel + " == False):");
		writer.appendLine(robotMotionRequester.generateScriptStopCommand());
		writer.appendLine("end");
	}

	/**
	 * Triggers a keyboard on the assignment input field.
	 * 
	 * @return
	 */
	public KeyboardTextInput getKeyboardForAssignmentInput() {
		KeyboardTextInput keyboardInput = keyboardFactory.createStringKeyboardInput();
		return keyboardInput;
	}

	private Pose getPoseBasedOnFeature() {
		String feature = model.get(SELECTED_FEATURE, DEFAULT_SELECTED_FEATURE);
		Pose pose = null;

		pose = featureMap.get(feature);

		return pose;

	}

	/**
	 * Enables the keyboard call back.
	 * 
	 * @return
	 */
	public KeyboardInputCallback<String> getCallbackForAssignmentInput() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(final String value) {
				view.setAssignmentInputText(value);

				undoRedoManager.recordChanges(new UndoableChanges() {
					@Override
					public void executeChanges() {

						model.set(ASSIGNMENT_VARIABLE, value);
					}
				});

			}
		};
	}

	/**
	 * Triggers a keyboard on the popupinput field.
	 * 
	 * @return
	 */
	public KeyboardTextInput getKeyboardForPopupInput() {
		KeyboardTextInput keyboardInput = keyboardFactory.createStringKeyboardInput();
		return keyboardInput;
	}

	/**
	 * Enables the keyboard call back.
	 * 
	 * @return
	 */
	public KeyboardInputCallback<String> getCallbackForPopupInput() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(final String value) {
				view.setPopupInputText(value);

				undoRedoManager.recordChanges(new UndoableChanges() {
					@Override
					public void executeChanges() {
						model.set(POPUP_TEXT, value);
					}
				});
			}
		};
	}

	public MyDaemonInstallationNodeContribution getInstallation() {
		return apiProvider.getProgramAPI().getInstallationNode(MyDaemonInstallationNodeContribution.class);
	}

	public void setPopupStillEnabled(boolean isPopupStillEnabled) {
		this.isPopupStillEnabled = isPopupStillEnabled;
	}

	public boolean isPopupStillEnabled() {
		return isPopupStillEnabled;
	}

	public void setAssignmentChoiceModel(final String value) {
		undoRedoManager.recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				model.set(ASSIGMENT_CHOICE, value);
			}
		});
	}

	public void setSelectedFeature(final String value) {
		undoRedoManager.recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				model.set(SELECTED_FEATURE, value);
			}
		});
	}

}