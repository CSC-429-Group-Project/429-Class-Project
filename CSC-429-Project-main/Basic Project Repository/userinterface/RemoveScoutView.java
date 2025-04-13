package userinterface;

// system imports
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

/** The class containing the Remove Scout View for the application */
public class RemoveScoutView extends View {

    // GUI components
    protected TextField scoutIDField;
    protected Button cancelButton;
    protected Button submitButton;

    // For showing error message
    protected MessageView statusLog;

    // constructor for this class -- takes a model object
    public RemoveScoutView(IModel account) {
        super(account, "RemoveScoutView");

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

        myModel.subscribe("UpdateStatusMessage", this);
    }

    // Create the title container
    private Node createTitle() {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text("Remove Scout View");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
        container.getChildren().add(titleText);

        return container;
    }

    // Create the main form content
    private VBox createFormContent() {
        VBox vbox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);

        Text scoutIDLabel = new Text("Scout ID: ");
        scoutIDLabel.setFont(myFont);
        scoutIDLabel.setWrappingWidth(150);
        scoutIDLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(scoutIDLabel, 0, 1);
        scoutIDField = new TextField();
        scoutIDField.setEditable(true);
        grid.add(scoutIDField, 1, 1);

        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);
        submitButton = new Button("Submit");
        submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                processAction();
            }
        });
        cancelButton = new Button("Back");
        cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                goToHomeView();
            }
        });
        doneCont.getChildren().addAll(submitButton, cancelButton);
        vbox.getChildren().add(doneCont);

        vbox.getChildren().add(grid);
        return vbox;
    }

    public void processAction() {
        String scoutID = scoutIDField.getText();
        if (scoutID == null || scoutID.isEmpty()) {
            displayErrorMessage("Scout ID is required.");
            return;
        }

        try {
            RemoveScoutTransaction temp = new RemoveScoutTransaction();
            temp.processRemoveScoutTransaction(scoutID);
            displayMessage("Scout removed successfully!");
        } catch (Exception ex) {
            displayErrorMessage("Failed to remove scout: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Create the status log field
    protected MessageView createStatusLog(String initialMessage) {
        statusLog = new MessageView(initialMessage);
        return statusLog;
    }

    // Populate fields (if needed)
    public void populateFields() {
        // Implement if needed
    }

    /**
     * Update method
     */
    public void updateState(String key, Object value) {
        clearErrorMessage();

        if (key.equals("UpdateStatusMessage")) {
            String val = (String) value;
            displayMessage(val);
        }
    }

    /**
     * Display error message
     */
    public void displayErrorMessage(String message) {
        statusLog.displayErrorMessage(message);
    }

    /**
     * Display info message
     */
    public void displayMessage(String message) {
        statusLog.displayMessage(message);
    }

    /**
     * Clear error message
     */
    public void clearErrorMessage() {
        statusLog.clearErrorMessage();
    }

    private void goToHomeView() {
        // Create the Home (Librarian) view
        TransactionChoiceView homeView = new TransactionChoiceView(myModel);  // Pass model or any required parameters

        // Create the scene for the Home view
        Scene homeScene = new Scene(homeView);  // Create a scene from the home view

        // Get the Stage (window) and change the scene back to Home view
        Stage stage = (Stage) getScene().getWindow();  // Get the current window's stage
        stage.setScene(homeScene);  // Set the scene to Home (LibrarianView)
    }
}
