package userinterface;

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
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

import java.util.Properties;

public class AddTreeTypeView extends View{

    protected Text descText;
    protected TextField descField;
    protected ComboBox<String> valueBox;
    protected Text valueText;
    protected Text costText;
    protected TextField costField;
    protected Text barcodeText;
    protected TextField barcodeField;
    protected Button submitButton;
    protected Button cancelButton;

    protected MessageView statusLog;


    public AddTreeTypeView(IModel model) {
        super(model, "AddTreeTypeView");
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());
        container.getChildren().add(createStatusLog(" "));
        getChildren().add(container);

        myModel.subscribe("UpdateStatusMessage", this);
        myModel.subscribe("TransactionError", this);
    }

    private Node createTitle() {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);
        Text titleText = new Text("Add Tree Type");
        titleText.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
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
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);

        descText = new Text("Description: ");
        descText.setFont(myFont);
        grid.add(descText, 0, 0);
        descField = new TextField();
        grid.add(descField, 1, 0);

        valueText = new Text("Value: ");
        valueText.setFont(myFont);
        grid.add(valueText, 0, 1);
        valueBox = new ComboBox<>();
        valueBox.getItems().addAll("Regular", "Premium");
        valueBox.setValue("Regular");
        grid.add(valueBox, 1, 1);

        costText = new Text("Cost: ");
        costText.setFont(myFont);
        grid.add(costText, 0, 2);
        costField = new TextField();
        grid.add(costField, 1, 2);

        barcodeText = new Text("Barcode prefix: ");
        barcodeText.setFont(myFont);
        grid.add(barcodeText, 0, 3);
        barcodeField = new TextField();
        grid.add(barcodeField, 1, 3);

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
        buttonContainer.getChildren().add(cancelButton);

        submitButton = new Button("Submit");
        submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                try {
                    processAction();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        buttonContainer.getChildren().add(submitButton);
        vbox.getChildren().add(buttonContainer);

        return vbox;
    }

    private void processAction() throws Exception {
        Properties props = new Properties();
        String fullDesc = descField.getText() + " - " + valueBox.getValue();
        if (descField.getText().isEmpty() || costField.getText().isEmpty() || barcodeField.getText().isEmpty()) {
            displayErrorMessage("You must fill out each field!");
        } else if (barcodeField.getLength() > 2 || !checkNum(barcodeField.getText())) {
            displayErrorMessage("The barcode field must be 2 digits!");
        } else if (!checkNum(costField.getText())) {
            displayErrorMessage("The cost must be a number!");
        } else if (fullDesc.length() >= 25) {
            displayErrorMessage("The description must be shorter!");
        } else {
            displayMessage("Tree Type successfully added!");
            props.setProperty("Type_Description", fullDesc);
            props.setProperty("Cost", costField.getText());
            props.setProperty("BarcodePrefix", barcodeField.getText());
            myModel.stateChangeRequest("AddTreeType", props);
        }
    }

    private boolean checkNum(String input) {
        try {
            try {
                Double.parseDouble(input);
            } catch (NumberFormatException ex) {
                Integer.parseInt(input);
            }
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public void updateState(String key, Object value) throws Exception {

    }

    protected MessageView createStatusLog(String initialMessage) {
        statusLog = new MessageView(initialMessage);
        return statusLog;
    }

    public void clearErrorMessage() {statusLog.clearErrorMessage(); }

    public void displayErrorMessage(String message)
    {
        statusLog.displayErrorMessage(message);
    }

    public void displayMessage(String message)
    {
        statusLog.displayMessage(message);
    }

    private void goToHomeView() {
        // Create the Transaction Choice view
        TransactionChoiceView homeView = new TransactionChoiceView(myModel);

        // Create the scene for the Home view
        Scene homeScene = new Scene(homeView);  // Create a scene from the home view

        // Get the Stage (window) and change the scene back to Home view
        Stage stage = (Stage) getScene().getWindow();  // Get the current window's stage
        stage.setScene(homeScene);  // Set the scene to Home
    }
}
