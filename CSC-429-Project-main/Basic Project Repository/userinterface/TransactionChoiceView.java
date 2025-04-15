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

/** The class containing the Account View  for the ATM application */
//==============================================================
public class TransactionChoiceView extends View
{

	// GUI components
	protected Button ScoutAddButton;
	protected Button ScoutRemoveButton;
	protected Button ScoutModifyButton;
	protected Button TreeAddButton;
	protected Button TreeModifyButton;

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

		ScoutAddButton = createButton("Add Scout");
		ScoutAddButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					myModel.stateChangeRequest("AddScoutView", null);
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
					myModel.stateChangeRequest("ModifyTreeView", null);
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
				ScoutAddButton,
				ScoutModifyButton,
				ScoutRemoveButton,
				TreeAddButton,
				TreeModifyButton,
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
