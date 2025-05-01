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

public class RemoveTreeView extends View{

    protected TextField Barcode;

    protected Button cancelButton;
    protected Button submitButton;
    protected ComboBox<String> status;

    protected MessageView statusLog;

    public RemoveTreeView(IModel account) {
        super(account, "RemoveTreeView");

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        // Add a title for this panel
        container.getChildren().add(createTitle());

        // create our GUI components, add them to this Container
        container.getChildren().add(createFormContent());

        container.getChildren().add(createStatusLog("             "));

        getChildren().add(container);

        //populateFields();

        //myModel.subscribe("ServiceCharge", this);
        myModel.subscribe("UpdateStatusMessage", this);
    }

    private Node createTitle()
    {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text("Enter Tree Information");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
        container.getChildren().add(titleText);

        return container;
    }


    private VBox createFormContent() {
        VBox vbox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 14);

        Text LnameLabel = new Text(" Barcode : ");
        LnameLabel.setFont(myFont);
        LnameLabel.setWrappingWidth(150);
        LnameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(LnameLabel, 0, 0);
        Barcode = new TextField();
        Barcode.setEditable(true);
        grid.add(Barcode, 1, 0);

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

        String barcode = Barcode.getText().trim();

        Properties props = new Properties();

        // Verify that the author field is not empty
        if (barcode.isEmpty())
        {
            displayErrorMessage("Barcode field cannot be empty!");
            Barcode.requestFocus();
            // Verify that the title field is not empty
        } else if (barcode.length() < 5 || barcode.length() >6) {
            displayErrorMessage("Barcode needs to be 5 characters long");
            Barcode.requestFocus();
        }


        // else if (Integer.parseInt(dateOfBirth.getText().split("-")[0]) < 1920 || Integer.parseInt(dateOfBirth.getText().split("-")[0]) > 2006) {
        //    displayErrorMessage("Date of birth should be between '1920-01-01' and '2006-01-01'!");
        //    dateOfBirth.requestFocus();
        else {
            props.setProperty("Barcode", Barcode.getText());
            //props.setProperty("Status", status.getValue());  // Assuming 'status' is a TextField, adjust accordingly


            try {
                myModel.stateChangeRequest("RemoveTreeS", props);
                displayMessage("SUCCESS!");
            }
            catch(Exception ex)
            {
                displayErrorMessage("FAILED");
                ex.printStackTrace();
            }
            // state request change with the data
        }
    }

    protected MessageView createStatusLog(String initialMessage)
    {
        statusLog = new MessageView(initialMessage);

        return statusLog;
    }

    public void displayMessage(String message)
    {
        statusLog.displayMessage(message);
    }

    public void clearErrorMessage()
    {
        statusLog.clearErrorMessage();
    }



    public void displayErrorMessage(String message)
    {
        System.out.println("displayErrorMessage called with message: " + message);
        statusLog.displayErrorMessage(message);
    }




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
}
