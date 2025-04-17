// specify the package
package userinterface;

// system imports
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

// project imports
import impresario.IModel;

/** The class containing the Transaction Choice View */
//==============================================================
public class TransactionChoiceView extends View
{

	// GUI components
	protected Button ScoutAddButton;
	protected Button ScoutRemoveButton;
	protected Button ScoutModifyButton;
	protected Button TreeAddButton;
	protected Button TreeModifyButton;
	protected Button TreeRemoveButton;
	protected Button TreeTypeAddButton;
	protected Button TreeTypeModifyButton;
	protected Button StartShiftButton;
	protected Button EndShiftButton;
	protected Button SellTreeButton;

	protected Button doneButton;

	// For showing error message
	protected MessageView statusLog;

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public TransactionChoiceView(IModel account)
	{
		super(account, "TransactionChoiceView");

		// create a container for showing the contents
		VBox container = new VBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));
		container.setPrefWidth(500);

		// Add a title for this panel
		container.getChildren().add(createTitle());

		// create our GUI components, add them to this Container
		container.getChildren().add(createFormContent());

		container.getChildren().add(createStatusLog("             "));

		getChildren().add(container);

		// Will need to be changed
		myModel.subscribe("ServiceCharge", this);
		myModel.subscribe("UpdateStatusMessage", this);
	}



	// Create the title container
	//-------------------------------------------------------------
	private Node createTitle()
	{
		HBox container = new HBox();
		container.setAlignment(Pos.CENTER);

		Text titleText = new Text(" Scout Tree Organizer ");
		titleText.setFont(Font.font("Garamond", FontWeight.BOLD, 20));
		titleText.setWrappingWidth(300);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.DARKGREEN);
		container.getChildren().add(titleText);

		return container;
	}

	// Create the main form content
	//-------------------------------------------------------------
	private VBox createFormContent() {
		VBox vbox = new VBox(10);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(20));

		Font myFont = Font.font("Helvetica", FontWeight.BOLD, 18);

		VBox scout = new VBox(15);
		scout.setAlignment(Pos.CENTER);

		Text scoutButtonsLabel = new Text("Scout");
		scoutButtonsLabel.setFont(myFont);

		HBox scoutButtonsContainer = new HBox(50);
		scoutButtonsContainer.setAlignment(Pos.CENTER);


		ScoutAddButton = createButton("Add Scout");
		ScoutAddButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					myModel.stateChangeRequest("AddScoutTransaction", null);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		});

		ScoutModifyButton = createButton("Modify Scout");
		ScoutModifyButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					myModel.stateChangeRequest("ModifyScoutView", null);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		});

		ScoutRemoveButton = createButton("Remove Scout");
		ScoutRemoveButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					myModel.stateChangeRequest("RemoveScoutView", null);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		});

		scoutButtonsContainer.getChildren().add(ScoutAddButton);
		scoutButtonsContainer.getChildren().add(ScoutModifyButton);
		scoutButtonsContainer.getChildren().add(ScoutRemoveButton);

		scout.getChildren().add(scoutButtonsLabel);
		scout.getChildren().add(scoutButtonsContainer);


		VBox tree = new VBox(15);
		tree.setAlignment(Pos.CENTER);
		tree.setPadding(new Insets(20, 0, 0, 0));

		Text treeButtonsLabel = new Text("Tree");
		treeButtonsLabel.setFont(myFont);

		HBox treeButtonsContainer = new HBox(50);
		treeButtonsContainer.setAlignment(Pos.CENTER);

		TreeAddButton = createButton("Add Tree");
		TreeAddButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					myModel.stateChangeRequest("AddTreeView", null);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		});

		TreeModifyButton = createButton("Modify Tree");
		TreeModifyButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					myModel.stateChangeRequest("ModifyTreeTransaction", null);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		});

		TreeRemoveButton = createButton("Remove Tree");
		TreeRemoveButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					System.out.println("Not implemented.");
					//myModel.stateChangeRequest("", null);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		});

		treeButtonsContainer.getChildren().add(TreeAddButton);
		treeButtonsContainer.getChildren().add(TreeModifyButton);
		treeButtonsContainer.getChildren().add(TreeRemoveButton);

		tree.getChildren().add(treeButtonsLabel);
		tree.getChildren().add(treeButtonsContainer);


		VBox treeType = new VBox(15);
		treeType.setAlignment(Pos.CENTER);
		treeType.setPadding(new Insets(20, 0, 0, 0));

		HBox treeTypeButtonsContainer = new HBox(50);
		treeTypeButtonsContainer.setAlignment(Pos.CENTER);

		Text treeTypeButtonsLabel = new Text("Tree Type");
		treeTypeButtonsLabel.setFont(myFont);

		TreeTypeAddButton = createButton("Add Tree Type");
		TreeTypeAddButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					System.out.println("Not implemented.");
					//myModel.stateChangeRequest("", null);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		});

		TreeTypeModifyButton = createButton("Modify Tree Type");
		TreeTypeModifyButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					System.out.println("Not implemented.");
					//myModel.stateChangeRequest("", null);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		});

		treeTypeButtonsContainer.getChildren().add(TreeTypeAddButton);
		treeTypeButtonsContainer.getChildren().add(TreeTypeModifyButton);

		treeType.getChildren().add(treeTypeButtonsLabel);
		treeType.getChildren().add(treeTypeButtonsContainer);


		VBox shift = new VBox(15);
		shift.setAlignment(Pos.CENTER);
		shift.setPadding(new Insets(20, 0, 0, 0));

		Text shiftsLabel = new Text("Shift");
		shiftsLabel.setFont(myFont);

		HBox shiftButtonsContainer = new HBox(50);
		shiftButtonsContainer.setAlignment(Pos.CENTER);

		StartShiftButton = createButton("Start Shift");
		StartShiftButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					System.out.println("Not implemented.");
					//myModel.stateChangeRequest("", null);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		});

		EndShiftButton = createButton("End Shift");
		EndShiftButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					System.out.println("Not implemented.");
					//myModel.stateChangeRequest("", null);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		});

		shiftButtonsContainer.getChildren().add(StartShiftButton);
		shiftButtonsContainer.getChildren().add(EndShiftButton);

		shift.getChildren().add(shiftsLabel);
		shift.getChildren().add(shiftButtonsContainer);

		SellTreeButton = createButton("Sell Tree");
		SellTreeButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					System.out.println("Not implemented.");
					//myModel.stateChangeRequest("", null);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		});


		// Done Button
		HBox doneCont = new HBox(10);
		doneCont.setAlignment(Pos.CENTER);
		doneButton = createButton("Done");
		doneButton.setPrefWidth(100);
		doneButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				System.exit(0);
			}
		});
		doneCont.getChildren().add(doneButton);
		vbox.getChildren().add(doneCont);

		// Add all buttons to VBox
		vbox.getChildren().addAll(
				scout,
				tree,
				treeType,
				shift,
				SellTreeButton,
				doneButton
		);

		return vbox;
	}


	// Create formatted button
	//-------------------------------------------------------------
	private Button createButton(String text) {
		Button button = new Button(text);
		button.setFont(Font.font("Garamond", FontWeight.BOLD, 14));
		button.setPrefWidth(200);
		button.setPrefHeight(30);
		return button;
	}

	// Create the status log field
	//-------------------------------------------------------------
	protected MessageView createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

	/**
	 * Update method
	 */
	//---------------------------------------------------------
	public void updateState(String key, Object value)
	{

	}

	/**
	 * Display error message
	 */
	//----------------------------------------------------------
	public void displayErrorMessage(String message)
	{
		statusLog.displayErrorMessage(message);
	}

	/**
	 * Display info message
	 */
	//----------------------------------------------------------
	public void displayMessage(String message)
	{
		statusLog.displayMessage(message);
	}

	/**
	 * Clear error message
	 */
	//----------------------------------------------------------
	public void clearErrorMessage()
	{
		statusLog.clearErrorMessage();
	}

}

//---------------------------------------------------------------
//	Revision History:
//
