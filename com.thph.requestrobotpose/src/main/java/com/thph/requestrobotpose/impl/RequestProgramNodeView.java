package com.thph.requestrobotpose.impl;

import com.thph.requestrobotpose.impl.RobotMotionRequester.Axis;
import com.thph.requestrobotpose.impl.servicedaemon.UnknownResponseException;
import com.ur.style.URSpacingSize;
import com.ur.style.URTypegraphy;
import com.ur.style.components.URButtons;
import com.ur.style.components.URDropdowns;
import com.ur.style.components.URSliders;
import com.ur.style.components.URSpacing;
import com.ur.style.components.URTextFields;
import com.ur.test.PreviewUI;
import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.xmlrpc.XmlRpcException;

import java.awt.Button;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class RequestProgramNodeView implements SwingProgramNodeView<RequestProgramNodeContribution> {

	final JPopupMenu popup = new JPopupMenu();
	private JPanel programnnodeViewPanel;

	// Initiate command sender.
	final RobotMotionRequester robotMotionRequester = new RobotMotionRequester();

	// Style guide library
	URButtons urButtons = new URButtons();
	URTextFields urTextFields = new URTextFields();
	URTypegraphy urTypegraphy = new URTypegraphy();
	URDropdowns urDropdowns = new URDropdowns();
	PreviewUI previewUI = new PreviewUI();
	URSpacing urSpacing = new URSpacing();
	URSpacingSize urSpacingSize = new URSpacingSize();

	//popup panel
	JLabel labelonPopup = new JLabel();
	
	// Create TPC position buttons
	JButton buttonZNegative = urButtons.getSmallButtonEnabled("Z-", 100);
	JButton buttonZPositive = urButtons.getSmallButtonEnabled("Z+", 100);
	JButton buttonXNegative = urButtons.getSmallButtonEnabled("X-", 100);
	JButton buttonXPositive = urButtons.getSmallButtonEnabled("X+", 100);
	JButton buttonYPositive = urButtons.getSmallButtonEnabled("Y+", 100);
	JButton buttonYNegative = urButtons.getSmallButtonEnabled("Y-", 100);
	JButton buttonOK = urButtons.getSmallButtonEnabled("OK", 100);

	// Create TPC orientation buttons
	JButton buttonRZNegative = urButtons.getSmallButtonEnabled("RZ-", 100);
	JButton buttonRZPositive = urButtons.getSmallButtonEnabled("RZ+", 100);
	JButton buttonRXNegative = urButtons.getSmallButtonEnabled("RX-", 100);
	JButton buttonRXPositive = urButtons.getSmallButtonEnabled("RX+", 100);
	JButton buttonRYPositive = urButtons.getSmallButtonEnabled("RY+", 100);
	JButton buttonRYNegative = urButtons.getSmallButtonEnabled("RY-", 100);

	// Buttoon Image path
	private static final String IMAGE_ZNEGATIVE = "/image/positionznegative.png";
	private static final String IMAGE_ZPOSITIVE = "/image/positionzpositive.png";
	private static final String IMAGE_YNEGATIVE = "/image/positionynegative.png";
	private static final String IMAGE_YPOSITIVE = "/image/positionypositive.png";
	private static final String IMAGE_XNEGATIVE = "/image/positionxnegative.png";
	private static final String IMAGE_XPOSITIVE = "/image/positionxpositive.png";

	// Program node components
	JLabel featureLabel = new JLabel("Robot feature:");
	JLabel popuptextLabel = new JLabel("Popup text:");
	JLabel assignmentLabel = new JLabel("Assign variable:");
	JLabel equalLabel = new JLabel(":=");

	String[] featureValues = { "Base", "Tool" };
	String[] assigmentValues = { "Pose" };
	JComboBox featureDropdown = urDropdowns.getDropDownEnabled(100);
	JComboBox assignmentDropdown = urDropdowns.getDropDownEnabled(100);

	JTextField assignmentInput = urTextFields.getTextFieldEnabled(200);
	JTextField popupInput = urTextFields.getTextFieldEnabled(300);

	//TODO: currently unused - serve no purpose
	JButton popupButton = urButtons.getSmallButtonEnabled("OK", 50);

	@Override
	public void buildUI(JPanel panel, ContributionProvider<RequestProgramNodeContribution> provider) {
		this.setProgramnnodeViewPanel(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(this.createPopup(panel, provider));
		panel.add(createProgramnodeview(provider));

	}

	/**
	 * Method for activating the popup. Calls by the RequestProgramNodeConribution
	 * class in openview method.
	 * 
	 * @param panel
	 */
	public void openPopopView(JPanel panel) {
		buttonOK.getModel().setPressed(false);
		popup.show(panel, panel.getWidth() / 4, 0);
	}

	/**
	 * create a popup on the programnode JPanel x,y(0,0).
	 * 
	 * @param panel
	 * @return
	 */
	private Box createPopup(final JPanel panel, ContributionProvider<RequestProgramNodeContribution> provider) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Create a jpanel with a title.
		JPanel jpanel = previewUI.AddComponentsToUI("Popup");
		jpanel.add(createTPCpositionButtons());
		jpanel.add(createTCPorientationButtons());

		// Calls the button handler.
		this.handleButtonEvents(provider);

		// add the panel with buttons to the popup.
		popup.add(jpanel);

		box.add(popup);

		return box;
	}

	private void addImageToButtons() {
		this.buttonZNegative.setIcon(createImageIcon(IMAGE_ZNEGATIVE));
		this.buttonZPositive.setIcon(createImageIcon(IMAGE_ZPOSITIVE));
		this.buttonYNegative.setIcon(createImageIcon(IMAGE_YNEGATIVE));
		this.buttonYPositive.setIcon(createImageIcon(IMAGE_YPOSITIVE));
		this.buttonXNegative.setIcon(createImageIcon(IMAGE_XNEGATIVE));
		this.buttonXPositive.setIcon(createImageIcon(IMAGE_XPOSITIVE));
	}

	private Box createProgramnodeview(final ContributionProvider<RequestProgramNodeContribution> provider) {
		Box box = Box.createVerticalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);

		assignmentInput.setEditable(true);
		popupInput.setEditable(true);
		assignmentInput.setHorizontalAlignment(SwingConstants.LEFT);
		popupInput.setHorizontalAlignment(SwingConstants.LEFT);

		featureDropdown.setModel(new DefaultComboBoxModel<String>(featureValues));
		assignmentDropdown.setModel(new DefaultComboBoxModel<String>(assigmentValues));

		assignmentInput.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = provider.get().getKeyboardForAssignmentInput();
				keyboardInput.show(assignmentInput, provider.get().getCallbackForAssignmentInput());
			}
		});
		
		popupInput.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = provider.get().getKeyboardForPopupInput();
				keyboardInput.show(popupInput, provider.get().getCallbackForPopupInput());
			}
		});
		
		

		Box featureBox = Box.createHorizontalBox();
		featureBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		Box assignmentBox = Box.createHorizontalBox();
		assignmentBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		Box popupBox = Box.createHorizontalBox();
		popupBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		featureBox.add(featureLabel);
		featureBox.add(urSpacing.createHorizontalSpacing());
		featureBox.add(featureDropdown);

		assignmentBox.add(assignmentInput);
		assignmentBox.add(urSpacing.createHorizontalSpacing());
		assignmentBox.add(equalLabel);
		assignmentBox.add(urSpacing.createHorizontalSpacing());
		assignmentBox.add(assignmentDropdown);

		popupBox.add(popuptextLabel);
		popupBox.add(urSpacing.createHorizontalSpacing());
		popupBox.add(popupInput);
		popupBox.add(urSpacing.createHorizontalSpacing());
		popupBox.add(popupButton);

		box.add(featureBox);
		box.add(urSpacing.createVerticalSpacing(50));
		box.add(assignmentLabel);
		box.add(urSpacing.createVerticalSpacing(10));
		box.add(assignmentBox);
		box.add(urSpacing.createVerticalSpacing(50));
		box.add(popupBox);

		return box;
	}

	/**
	 * Creates a box with five TCP position buttons.
	 * 
	 * @return Box containing the buttons.
	 */
	private Box createTPCpositionButtons() {

		labelonPopup.setHorizontalAlignment(SwingConstants.LEFT);
		
		Box box = Box.createVerticalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		Box boxPopupLabel = Box.createHorizontalBox();

		Box boxXNegative = Box.createHorizontalBox();

		Box boxXPostive = Box.createHorizontalBox();

		Box boxZ = Box.createHorizontalBox();

		Box boxY = Box.createHorizontalBox();
		
		boxPopupLabel.add(labelonPopup);

		boxZ.add(buttonZPositive);
		boxZ.add(createCustomizedHorizontalSpacing(100));
		boxZ.add(buttonZNegative);

		boxXNegative.add(createCustomizedHorizontalSpacing(1));
		boxXNegative.add(buttonXNegative);

		boxXPostive.add(createCustomizedHorizontalSpacing(1));
		boxXPostive.add(buttonXPositive);

		boxY.add(buttonYNegative);
		boxY.add(createCustomizedHorizontalSpacing(100));
		boxY.add(buttonYPositive);

		box.add(boxPopupLabel);
		box.add(urSpacing.createVerticalSpacing(50));
		box.add(boxZ);
		box.add(urSpacing.createVerticalSpacing(20));
		box.add(boxXNegative);
		box.add(urSpacing.createVerticalSpacing(20));
		box.add(boxY);
		box.add(urSpacing.createVerticalSpacing(20));
		box.add(boxXPostive);
		box.add(urSpacing.createVerticalSpacing(50));

		return box;
	}

	/**
	 * Creates a box with five TCP orientation buttons.
	 * 
	 * @return Box containing the buttons.
	 */
	private Box createTCPorientationButtons() {
		Box box = Box.createVerticalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);

		Box boxRYNegative = Box.createHorizontalBox();

		Box boxRYPostive = Box.createHorizontalBox();

		Box boxZ = Box.createHorizontalBox();

		Box boxX = Box.createHorizontalBox();

		Box boxSTOP = Box.createHorizontalBox();

		boxZ.add(buttonRZPositive);
		boxZ.add(createCustomizedHorizontalSpacing(100));
		boxZ.add(buttonRZNegative);

		boxRYNegative.add(createCustomizedHorizontalSpacing(1));
		boxRYNegative.add(buttonRYNegative);

		boxRYPostive.add(createCustomizedHorizontalSpacing(1));
		boxRYPostive.add(buttonRYPositive);

		boxSTOP.add(createCustomizedHorizontalSpacing(200));
		boxSTOP.add(buttonOK);

		boxX.add(buttonRXNegative);
		boxX.add(createCustomizedHorizontalSpacing(100));
		boxX.add(buttonRXPositive);

		box.add(boxZ);
		box.add(urSpacing.createVerticalSpacing(20));
		box.add(boxRYNegative);
		box.add(urSpacing.createVerticalSpacing(20));
		box.add(boxX);
		box.add(urSpacing.createVerticalSpacing(20));
		box.add(boxRYPostive);
		box.add(urSpacing.createVerticalSpacing(20));
		box.add(boxSTOP);

		return box;
	}

	/**
	 * Create handlers for buttons.
	 */
	private void handleButtonEvents(final ContributionProvider<RequestProgramNodeContribution> provider) {

		// TODO: without script-level --> remove provider and Direction

		// Button listener for TCP position buttons.
		this.createChangeListener(buttonZNegative, Axis.Z_Axis, -0.3, provider, Direction.ZNEGATIVE.label);
		this.createChangeListener(buttonZPositive, Axis.Z_Axis, 0.3, provider, Direction.ZPOSITIVE.label);
		this.createChangeListener(buttonYNegative, Axis.Y_Axis, -0.3, provider, Direction.YNEGATIVE.label);
		this.createChangeListener(buttonYPositive, Axis.Y_Axis, 0.3, provider, Direction.YPOSITIVE.label);
		this.createChangeListener(buttonXNegative, Axis.X_Axis, -0.3, provider, Direction.XNEGATIVE.label);
		this.createChangeListener(buttonXPositive, Axis.X_Axis, 0.3, provider, Direction.XPOSITIVE.label);

		// Button listener for TCP orientation buttons.
		this.createChangeListener(buttonRZNegative, Axis.RZ_Axis, -0.3, provider, Direction.RZNEGATIVE.label);
		this.createChangeListener(buttonRZPositive, Axis.RZ_Axis, 0.3, provider, Direction.RZPOSITIVE.label);
		this.createChangeListener(buttonRYNegative, Axis.RY_Axis, -0.3, provider, Direction.RYNEGATIVE.label);
		this.createChangeListener(buttonRYPositive, Axis.RY_Axis, 0.3, provider, Direction.RYPOSITIVE.label);
		this.createChangeListener(buttonRXNegative, Axis.RX_Axis, -0.3, provider, Direction.RXNEGATIVE.label);
		this.createChangeListener(buttonRXPositive, Axis.RX_Axis, 0.3, provider, Direction.RXPOSITIVE.label);

		// Button listener for OK buttons.
		this.buttonOK.getModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {

				ButtonModel model = (ButtonModel) e.getSource();

				if (model.isPressed()) {
					try {
						provider.get().getInstallation().getXmlRpcDaemonInterface().cancelPopup();
					} catch (XmlRpcException e1) {
						e1.printStackTrace();
					} catch (UnknownResponseException e1) {
						e1.printStackTrace();
					}

					provider.get().setPopupStillEnabled(false);
					popup.setVisible(false);
				}

			}
		});

	}

	// TODO: without script-level --> remove provider and Direction from method
	// parameters
	/**
	 * Generic handlers for button event.
	 * 
	 * @param button
	 * @param axis
	 * @param speed
	 * @param provider
	 * @param direction
	 */
	private void createChangeListener(final JButton button, final Axis axis, final double speed,
			final ContributionProvider<RequestProgramNodeContribution> provider, final String direction) {
		button.getModel().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				ButtonModel model = (ButtonModel) e.getSource();

				// TODO: without script-level --> remove the provider logic and insert:
				// robotMotionRequester.requestRobotMove(axis, speed);
				if (model.isPressed()) {
					try {
						provider.get().getInstallation().getXmlRpcDaemonInterface().setDirectionEnabled(direction,
								true);
					} catch (XmlRpcException e1) {
						e1.printStackTrace();
					} catch (UnknownResponseException e1) {
						e1.printStackTrace();
					}

					// TODO: without script-level --> remove the provider logic and insert:
					// robotMotionRequester.stopRobotMove();;
				} else if (!model.isPressed()) {
					try {
						provider.get().getInstallation().getXmlRpcDaemonInterface().setDirectionEnabled(direction,
								false);
					} catch (XmlRpcException e1) {
						e1.printStackTrace();
					} catch (UnknownResponseException e1) {
						e1.printStackTrace();
					}
				}

			}
		});
	}

	/**
	 * Create a horizontal spacing.
	 * 
	 * @param spacesize
	 * @return
	 */
	private Component createCustomizedHorizontalSpacing(int spacesize) {
		return Box.createRigidArea(new Dimension(spacesize, 0));
	}

	public JPanel getProgramnnodeViewPanel() {
		return programnnodeViewPanel;
	}

	private void setProgramnnodeViewPanel(JPanel programnnodeViewPanel) {
		this.programnnodeViewPanel = programnnodeViewPanel;
	}
	
	/**
	 * Sets the name of the varible 
	 * for assigning value to.
	 * @param text
	 */
	public void setAssignmentInputText(String text) {
		assignmentInput.setText(text);
	}
	
	/**
	 * Sets the text on the input text field for 
	 * displaying on the popup
	 * @param text
	 */
	public void setPopupInputText(String text) {
		popupInput.setText(text);
	}
	
	
	/**
	 * Sets the text on the popup during 
	 * the program execution.
	 * @param text
	 */
	public void setTextOnShowingPopup(String text) {
		labelonPopup.setText(text);
	}

	/**
	 * Creates an imageicon based on the path. Images must be located in the
	 * resources map of the project.
	 * 
	 * @param path
	 * @return image icon.
	 */
	private ImageIcon createImageIcon(String path) {
		ImageIcon icon = null;
		try {
			BufferedImage imgURL = ImageIO.read(getClass().getResourceAsStream(path));

			if (imgURL != null) {
				icon = new ImageIcon(imgURL);

			}
		} catch (IOException e) {
			System.out.println("NO IMAGE FOUND");
		}

		return icon;
	}

}