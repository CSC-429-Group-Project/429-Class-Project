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
public class AddScoutView extends View
{

    // GUI components
    protected TextField LastName;
    protected TextField FirstName;
    protected TextField MiddleName;
    protected TextField DateOfBirth;
    protected TextField PhoneNumber;
    protected TextField Email;
    protected TextField TroopID;

    protected Button cancelButton;
    protected Button submitButton;
    protected ComboBox<String> status;



    // For showing error message
    protected MessageView statusLog;

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public AddScoutView(IModel account)
    {
        super(account, "AddScoutView");

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

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

        Text titleText = new Text("Enter Scout Information");
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
        grid.setPadding(new Insets(25, 25, 25, 25));

        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 14);

        Text LnameLabel = new Text(" Last Name : ");
        LnameLabel.setFont(myFont);
        LnameLabel.setWrappingWidth(150);
        LnameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(LnameLabel, 0, 0);
        LastName = new TextField();
        LastName.setEditable(true);
        grid.add(LastName, 1, 0);

        Text FnameLabel = new Text(" First Name : ");
        FnameLabel.setFont(myFont);
        FnameLabel.setWrappingWidth(150);
        FnameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(FnameLabel, 0, 1);
        FirstName = new TextField();
        FirstName.setEditable(true);
        grid.add(FirstName, 1, 1);

        Text MnameLabel = new Text(" Middle Name : ");
        MnameLabel.setFont(myFont);
        MnameLabel.setWrappingWidth(150);
        MnameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(MnameLabel, 0, 2);
        MiddleName = new TextField();
        MiddleName.setEditable(true);
        grid.add(MiddleName, 1, 2);

        Text DOBLabel = new Text(" Date of Birth : ");
        DOBLabel.setFont(myFont);
        DOBLabel.setWrappingWidth(150);
        DOBLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(DOBLabel, 0, 3);
        DateOfBirth = new TextField();
        DateOfBirth.setEditable(true);
        grid.add(DateOfBirth, 1, 3);

        Text PhoneLabel = new Text(" Phone Number : ");
        PhoneLabel.setFont(myFont);
        PhoneLabel.setWrappingWidth(150);
        PhoneLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(PhoneLabel, 0, 4);
        PhoneNumber = new TextField();
        PhoneNumber.setEditable(true);
        grid.add(PhoneNumber, 1, 4);

        Text emailLabel = new Text(" Email : ");
        emailLabel.setFont(myFont);
        emailLabel.setWrappingWidth(150);
        emailLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(emailLabel, 0, 5);
        Email = new TextField();
        Email.setEditable(true);
        grid.add(Email, 1, 5);

        Text TidLabel = new Text("Troop ID : ");
        TidLabel.setFont(myFont);
        TidLabel.setWrappingWidth(150);
        TidLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(TidLabel, 0, 6);
        TroopID = new TextField();
        TroopID.setEditable(true);
        grid.add(TroopID, 1, 6);

        Text statusLabel = new Text(" Status : ");
        statusLabel.setFont(myFont);
        statusLabel.setWrappingWidth(150);
        statusLabel.setTextAlignment(TextAlignment.RIGHT);
        status = new ComboBox<String>();
        status.getItems().addAll("Active", "Inactive");
        status.setValue("Active");
        grid.add(statusLabel, 0, 7);
        grid.add(status, 1, 7);

        vbox.getChildren().add(grid);

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

        submitButton = new Button("Submit");
        submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                processAction();
            }
        });

        buttonContainer.getChildren().addAll(cancelButton, submitButton);
        vbox.getChildren().add(buttonContainer);

        return vbox;

    }

    public void processAction()
    {

        String lastName = LastName.getText().trim();
        String firstName = FirstName.getText().trim();
        String middleName = MiddleName.getText().trim();
        String dob = DateOfBirth.getText().trim();
        String phone = PhoneNumber.getText().trim();
        String email = Email.getText().trim();
        String troopId = TroopID.getText().trim();

        Properties props = new Properties();

        // Verify that the author field is not empty
        if (lastName.isEmpty()|| middleName.isEmpty()|| firstName.isEmpty())
        {
            displayErrorMessage("Name fields cannot be empty!");
            LastName.requestFocus();
            MiddleName.requestFocus();
            FirstName.requestFocus();
            // Verify that the title field is not empty
        } else if (lastName.length() > 25) {
            displayErrorMessage("Last name cannot be longer than 25 characters.");
            LastName.requestFocus();
        } else if (middleName.length() > 25){
            displayErrorMessage("Middle name cannot be longer than 25 characters.");
            MiddleName.requestFocus();
        } else if (firstName.length() > 25){
            displayErrorMessage("First name cannot be longer than 25 characters.");
            FirstName.requestFocus();
        } else if (dob.isEmpty()) {
            displayErrorMessage("Date of Birth field cannot be empty!");
            DateOfBirth.requestFocus();
        } else if (dob.length() > 12){
            displayErrorMessage("Date of Birth field cannot be longer than 12 characters.");
            DateOfBirth.requestFocus();
        } else if (phone.isEmpty()) {
            displayErrorMessage("Phone Number field cannot be empty!");
            PhoneNumber.requestFocus();
        } else if (phone.length() > 14) {
            displayErrorMessage("Phone number cannot be longer than 14 characters.");
            PhoneNumber.requestFocus();
        } else if (email.isEmpty()) {
            displayErrorMessage("Email field cannot be empty!");
            Email.requestFocus();
        } else if (email.length() > 30){
            displayErrorMessage("Email cannot be longer than 30 characters.");
            Email.requestFocus();
        } else if (troopId.isEmpty()) {
            displayErrorMessage("TroopID field cannot be empty!");
            TroopID.requestFocus();
        } else if (troopId.length() > 10){
            displayErrorMessage("TroopID cannot be longer than 10 characters.");
            TroopID.requestFocus();
        }

        // else if (Integer.parseInt(dateOfBirth.getText().split("-")[0]) < 1920 || Integer.parseInt(dateOfBirth.getText().split("-")[0]) > 2006) {
        //    displayErrorMessage("Date of birth should be between '1920-01-01' and '2006-01-01'!");
        //    dateOfBirth.requestFocus();
            else {
            props.setProperty("LastName", LastName.getText());
            props.setProperty("FirstName", FirstName.getText());
            props.setProperty("MiddleName", MiddleName.getText());
            props.setProperty("DateOfBirth", DateOfBirth.getText());
            props.setProperty("PhoneNumber", PhoneNumber.getText());
            props.setProperty("Email", Email.getText());
            props.setProperty("TroopID", TroopID.getText()); // Make sure the format is correct
            props.setProperty("Status", status.getValue());  // Assuming 'status' is a TextField, adjust accordingly


            try {
                myModel.stateChangeRequest("AddScout", props);
                displayMessage("Scout successfully added!");
            }
            catch(Exception ex)
            {
                displayErrorMessage("Scout was unable to be added to the database.");
                ex.printStackTrace();
            }
            // state request change with the data
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
            // No data needed to populate fields
            //LastName.setText((String)myModel.getState("LastName"));
            //FirstName.setText((String)myModel.getState("FirstName"));
            //MiddleName.setText((String)myModel.getState("MiddleName"));
            //DateOfBirth.setText((String)myModel.getState("DateOfBirth"));
            //PhoneNumber.setText((String)myModel.getState("PhoneNumber"));
            //Email.setText((String)myModel.getState("Email"));
            //TroopID.setText((String)myModel.getState("TroopID"));
            //status.setValue("Active");
            //status.setValue((String)myModel.getState("status"));
            //dateOfBirth.setText((String)myModel.getState("dateOfBirth"));
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
            System.out.println("displayErrorMessage called with message: " + message);
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
