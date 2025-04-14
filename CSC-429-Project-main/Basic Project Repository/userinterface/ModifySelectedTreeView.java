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

import java.time.LocalDate;
import java.util.Properties;

// project imports
import impresario.IModel;
import model.*;

/** The class containing the Account View  for the ATM application */
//==============================================================
public class ModifySelectedTreeView extends View
{

    // GUI components
    protected Text barcode;
    protected Text treeType;
    protected TextArea notes;
    protected ComboBox<String> status;

    protected Button cancelButton;
    protected Button submitButton;


    // For showing error message
    protected MessageView statusLog;

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public ModifySelectedTreeView(IModel account)
    {
        super(account, "ModifySelectedTreeView");

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

        populateFields();

        //myModel.subscribe("ServiceCharge", this);
        myModel.subscribe("UpdateStatusMessage", this);
    }


    // Create the title container
    //-------------------------------------------------------------
    private Node createTitle()
    {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text("Modify Retrieved Tree");
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
        Text prompt = new Text("IT RETRIEVES BUT LETS MAKE IT UPDATE:");
        prompt.setFont(Font.font("Helvetica", FontWeight.BOLD, 18));
        topPromptContainer.getChildren().add(prompt);
        vbox.getChildren().add(topPromptContainer);

        Text barcodeLabel = new Text(" Barcode : ");
        barcodeLabel.setFont(myFont);
        grid.add(barcodeLabel, 0, 1);
        barcode = new Text();
        grid.add(barcode, 1, 1);

        Text treeTypeLabel = new Text(" Tree Type : ");
        treeTypeLabel.setFont(myFont);
        grid.add(treeTypeLabel, 0, 2);
        treeType = new Text();
        grid.add(treeType, 1, 2);

        Text notesLabel = new Text(" Notes : ");
        notesLabel.setFont(myFont);
        grid.add(notesLabel, 0, 3);
        notes = new TextArea();
        notes.setPrefRowCount(3);
        grid.add(notes, 1, 3);

        Text statusLabel = new Text(" Status : ");
        statusLabel.setFont(myFont);
        grid.add(statusLabel, 0, 4);
        status = new ComboBox<>();
        status.getItems().addAll("Available", "Sold", "Damaged");
        status.setValue("Available");
        grid.add(status, 1, 4);

        HBox buttonContainer = new HBox(75);
        buttonContainer.setAlignment(Pos.CENTER);
        cancelButton = new Button("Back");
        cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                goToModifyTreeView();
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
        String notesValue = notes.getText().trim();
        String statusValue = status.getValue();

        if (notesValue.length() > 200) {
            displayErrorMessage("Notes cannot exceed 200 characters.");
            notes.requestFocus();
        } else {
            Properties props = new Properties();
            props.setProperty("Notes", notesValue);
            props.setProperty("Status", statusValue);
            props.setProperty("DateStatusUpdated", LocalDate.now().toString());

            try {
                // change state request
                myModel.stateChangeRequest("UpdateSelectedTree", props);
                displayMessage("SUCCESS!!!");
            }
            catch (Exception ex){
                displayErrorMessage("FAILED TO UPDATE");
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
    public void populateFields()
    {
        Tree selectedTree = (Tree) myModel.getState("SelectedTree");

        if (selectedTree != null) {
            barcode.setText((String) selectedTree.getState("Barcode"));
            treeType.setText((String) selectedTree.getState("Tree_Type"));
            notes.setText((String) selectedTree.getState("Notes"));
            status.setValue((String) selectedTree.getState("Status"));
        }
    }

    /**
     * Update method
     */
    //---------------------------------------------------------
    public void updateState(String key, Object value)
    {
        clearErrorMessage();

        if (key.equals("Status") == true)
        {
            String val = (String)value;
            status.setValue(val);
            displayMessage("Status Updated to:  " + val);
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

    private void goToModifyTreeView() {
        ModifyTreeView modTreeView = new ModifyTreeView(myModel);  // Pass model or any required parameters

        // Create the scene for the Home view
        Scene modTreeScene = new Scene(modTreeView);  // Create a scene from the home view

        // Get the Stage (window) and change the scene back to Home view
        Stage stage = (Stage) getScene().getWindow();  // Get the current window's stage
        stage.setScene(modTreeScene);  // Set the scene to Home (LibrarianView)
    }

}

//---------------------------------------------------------------
//	Revision History:
//
