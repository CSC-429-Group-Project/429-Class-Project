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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import javafx.scene.image.Image;

import java.util.Arrays;
import java.util.Properties;
import java.util.Vector;

import database.*;

// project imports
import impresario.IModel;
import model.*;

/** The class containing the Account View  for the ATM application */
//==============================================================
public class ModifyScoutView extends View
{

    // GUI components
    protected TextField LastName;
    protected TextField FirstName;
    protected TextField MiddleName;
    protected TextField DateOfBirth;
    protected TextField PhoneNumber;
    protected TextField Email;
    protected TextField TroopID;
    protected Text instructions;
    protected HBox instCont;
    protected Image gif = new Image("file:\\C:\\Users\\alexg\\OneDrive\\Documents\\GitHub\\429-Class-Project\\CSC-429-Project-main\\Basic Project Repository\\userinterface\\oldman_optimized.gif");
    protected ImageView gifView = new ImageView(gif);
    protected static String state = "retrieve";

    protected Button cancelButton;
    protected Button submitButton;
    protected ComboBox<String> status;

    // For showing error message
    protected MessageView statusLog;

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public ModifyScoutView(IModel account)
    {
        super(account, "ModifyScoutView");
        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        // Add a title for this panel
        container.getChildren().add(createTitle());

        // create our GUI components, add them to this Container
        container.getChildren().add(createFormContent());

        container.getChildren().add(createStatusLog("             "));

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(container, gifView);
        StackPane.setAlignment(gifView, Pos.CENTER);
        getChildren().add(stackPane);

        populateFields();

        //myModel.subscribe("ServiceCharge", this);
        myModel.subscribe("UpdateStatusMessage", this);
        myModel.subscribe("TransactionError", this);
    }


    // Create the title container
    //-------------------------------------------------------------
    private Node createTitle()
    {

        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text("Modify Scout View");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
        container.getChildren().add(titleText);

        gifView.setPreserveRatio(true);
        gifView.setVisible(false);

        titleText.setOnMouseClicked(event -> {boolean currentlyVisible = gifView.isVisible();
            gifView.setVisible(!currentlyVisible);});
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
        grid.setPadding(new Insets(25, 25, 25, 25));

        // implement
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);

        instCont = new HBox(10);
        instructions = new Text("Enter any Scout information for Scout you want to modify.\nOne field must be filled for search.");
        instructions.setFont(myFont);
        instructions.setTextAlignment(TextAlignment.CENTER);
        instCont.getChildren().add(instructions);
        instCont.setAlignment(Pos.CENTER);
        vbox.getChildren().add(instCont);

        Text FnameLabel = new Text(" First Name : ");
        FnameLabel.setFont(myFont);
        FnameLabel.setWrappingWidth(150);
        FnameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(FnameLabel, 0, 1);  // Row 2
        FirstName = new TextField();
        FirstName.setEditable(true);
        grid.add(FirstName, 1, 1);

        // Middle Name Field
        Text MnameLabel = new Text(" Middle Name : ");
        MnameLabel.setFont(myFont);
        MnameLabel.setWrappingWidth(150);
        MnameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(MnameLabel, 0, 2);  // Row 3
        MiddleName = new TextField();
        MiddleName.setEditable(true);
        grid.add(MiddleName, 1, 2);

        Text LnameLabel = new Text(" Last Name : ");
        LnameLabel.setFont(myFont);
        LnameLabel.setWrappingWidth(150);
        LnameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(LnameLabel, 0, 3);  // Row 3
        LastName = new TextField();
        LastName.setEditable(true);
        grid.add(LastName, 1, 3);

// Date of Birth Field
        Text DOBLabel = new Text(" Date of Birth : ");
        DOBLabel.setFont(myFont);
        DOBLabel.setWrappingWidth(150);
        DOBLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(DOBLabel, 0, 4);  // Row 4
        DateOfBirth = new TextField();
        DateOfBirth.setEditable(true);
        grid.add(DateOfBirth, 1, 4);

// Phone Number Field
        Text PhoneLabel = new Text(" Phone Number : ");
        PhoneLabel.setFont(myFont);
        PhoneLabel.setWrappingWidth(150);
        PhoneLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(PhoneLabel, 0, 5);  // Row 5
        PhoneNumber = new TextField();
        PhoneNumber.setEditable(true);
        grid.add(PhoneNumber, 1, 5);

// Email Field
        Text emailLabel = new Text(" Email : ");
        emailLabel.setFont(myFont);
        emailLabel.setWrappingWidth(150);
        emailLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(emailLabel, 0, 6);  // Row 6
        Email = new TextField();
        Email.setEditable(true);
        grid.add(Email, 1, 6);

// Troop ID Field
        Text TidLabel = new Text("Troop ID : ");
        TidLabel.setFont(myFont);
        TidLabel.setWrappingWidth(150);
        TidLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(TidLabel, 0, 7);  // Row 7
        TroopID = new TextField();
        TroopID.setEditable(true);
        grid.add(TroopID, 1, 7);

        vbox.getChildren().add(grid);

        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);
        cancelButton = new Button("Back");
        submitButton = new Button("Submit");
        cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                state = "retrieve";
                try {
                    myModel.stateChangeRequest("CancelTransaction", null);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("state: " + state);
                try {
                    processAction();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        doneCont.getChildren().add(cancelButton);
        doneCont.getChildren().add(submitButton);
        vbox.getChildren().add(doneCont);
        return vbox;
    }

    public void processAction() throws Exception {
        // Create string array to pass to stateChangeRequest to make query statement and retrieve Scout data
        Properties props = new Properties();

        if (!FirstName.getText().equals("")) {
            props.setProperty("FirstName", FirstName.getText());
        } else {
            props.setProperty("FirstName", "");
        }
        if (!MiddleName.getText().equals("")) {
            props.setProperty("MiddleName", MiddleName.getText());
        } else {
            props.setProperty("MiddleName", "");
        }
        if (!LastName.getText().equals("")) {
            props.setProperty("LastName", LastName.getText());
        } else {
            props.setProperty("LastName", "");
        }
        if (!DateOfBirth.getText().equals("")) {
            props.setProperty("DateOfBirth", DateOfBirth.getText());
        } else {
            props.setProperty("DateOfBirth", "");
        }
        if (!PhoneNumber.getText().equals("")) {
            props.setProperty("PhoneNumber", PhoneNumber.getText());
        } else {
            props.setProperty("PhoneNumber", "");
        }
        if (!Email.getText().isEmpty()) {
            props.setProperty("Email", Email.getText());
        } else {
            props.setProperty("Email", "");
        }
        if (!TroopID.getText().isEmpty()) {
            props.setProperty("TroopID", TroopID.getText());
        } else {
            props.setProperty("TroopID", "");
        }
        System.out.println("Props: " + props); // good
        if (state.equals("retrieve")) {
            state = "update";
            myModel.stateChangeRequest("retrieveInitialScouts", props);

        } else if (state.equals("update")) {
            state = "retrieve";
            myModel.stateChangeRequest("UpdateScout", props);
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
        Scout selectedScout = (Scout) myModel.getState("selectedScout");
        System.out.println("SelectedScout: " + selectedScout);
        if (selectedScout != null && state.equals("update")) {
            FirstName.setText((String)selectedScout.getState("FirstName"));
            MiddleName.setText((String)selectedScout.getState("MiddleName"));
            LastName.setText((String)selectedScout.getState("LastName"));
            DateOfBirth.setText((String)selectedScout.getState("DateOfBirth"));
            PhoneNumber.setText((String)selectedScout.getState("PhoneNumber"));
            Email.setText((String)selectedScout.getState("Email"));
            TroopID.setText((String)selectedScout.getState("TroopID"));
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
        } else if (key.equals("TransactionError")) {
            String val = (String)myModel.getState("TransactionError");
            displayErrorMessage(val);
        } else if  (key.equals("UpdateStatusMessage")) {
            System.out.println("Success message output: " + (String)value);
            String success = (String)myModel.getState("successMessage");
            displayMessage(success);
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
