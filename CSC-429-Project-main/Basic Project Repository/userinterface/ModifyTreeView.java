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
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Properties;

// project imports
import impresario.IModel;
import model.*;

/** The class containing the Account View  for the ATM application */
//==============================================================
public class ModifyTreeView extends View
{

    // GUI components
    protected TextField barcode;

    protected Button cancelButton;
    protected Button submitButton;
    protected ComboBox<String> status;



    // For showing error message
    protected MessageView statusLog;

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public ModifyTreeView(IModel account)
    {
        super(account, "ModifyTreeView");

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

        clearFields();
        populateFields();

        myModel.subscribe("TransactionError", this);
        myModel.subscribe("UpdateStatusMessage", this);
    }


    // Create the title container
    //-------------------------------------------------------------
    private Node createTitle()
    {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text("Modify Tree View");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
        container.getChildren().add(titleText);

        return container;
    }

    // Create the main form content
    //-------------------------------------------------------------
    private VBox createFormContent()
    {
        VBox vbox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 25, 25, 25));

        // implement
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);

        HBox topPromptContainer = new HBox(10);
        topPromptContainer.setAlignment(Pos.CENTER);
        Text prompt = new Text("Please enter the tree barcode:");
        prompt.setFont(Font.font("Helvetica", FontWeight.BOLD, 18));
        topPromptContainer.getChildren().add(prompt);
        vbox.getChildren().add(topPromptContainer);


        Text barcodeLabel = new Text(" Barcode : ");
        barcodeLabel.setFont(myFont);
        grid.add(barcodeLabel, 0, 2);
        barcode = new TextField();
        grid.add(barcode, 1, 2);

        HBox buttonContainer = new HBox(75);
        buttonContainer.setAlignment(Pos.CENTER);
        cancelButton = new Button("Back");
        cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                try {
                    myModel.stateChangeRequest("CancelTransaction", null);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        buttonContainer.getChildren().add(cancelButton);


        submitButton = new Button("Submit");
        submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                processAction();
            }
        });

        vbox.getChildren().add(grid);
        buttonContainer.getChildren().add(submitButton);
        vbox.getChildren().add(buttonContainer);


        return vbox;
    }

    public void processAction()
    {
        String Barcode = barcode.getText().trim();
        if (Barcode.isEmpty()) {
            displayErrorMessage("Tree barcode must be entered.");
            barcode.requestFocus();
        } else if (Barcode.length() > 20){
            displayErrorMessage("Tree barcode cannot be longer than 20 characters.");
        } else if (Barcode.length() < 5){
            displayErrorMessage("Tree barcode cannot be shorter than 5 characters.");
        } else {
            Properties props = new Properties();
            props.setProperty("Barcode", Barcode);

            try {
                // Barcode is sent to model for tree retrieval
                myModel.stateChangeRequest("ModifySelectedTree", props);
            }
            catch (Exception ex){
                displayErrorMessage("FAILED");
                ex.printStackTrace();
            }
        }

    }


    // Create the status log field
    //-------------------------------------------------------------
    protected MessageView createStatusLog(String initialMessage)
    {
        statusLog = new MessageView(initialMessage);

        return statusLog;
    }

    //-------------------------------------------------------------
    public void populateFields() {
        barcode.setText("");
        clearErrorMessage();
    }

    // -----------------------------------------------------------
    public void clearFields() {
        barcode.setText("");
        clearErrorMessage();
    }

    /**
     * Update method
     */
    //---------------------------------------------------------
    public void updateState(String key, Object value)
    {
        clearErrorMessage();

        if (key.equals("Status")) {
            String val = (String)value;
            status.setValue(val);
            displayMessage("Status Updated to:  " + val);
        } else if (key.equals("TransactionError")) {
            String msg = (String) myModel.getState("TransactionError");
            displayErrorMessage(msg);
        }
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