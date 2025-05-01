package userinterface;

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

import impresario.IModel;
import model.RemoveScoutTransaction;
import model.Scout;

public class ConfirmRemoveScoutView extends View {

    protected TextField confirmScoutIDField;
    protected TextField confirmFirstNameField;
    protected TextField confirmLastNameField;
    protected TextField confirmMiddleNameField;
    protected TextField confirmDateOfBirthField;
    protected TextField confirmPhoneNumberField;
    protected TextField confirmEmailField;
    protected TextField confirmTroopIDField;

    protected Button confirmButton;
    protected Button cancelButton;

    protected MessageView statusLog;
    protected Scout scout;

    // ✅ Constructor now accepts a Scout object
    public ConfirmRemoveScoutView(IModel model, Scout selectedScout) {
        super(model, "ConfirmRemoveScoutView");
        this.scout = selectedScout;

        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());
        container.getChildren().add(createStatusLog("             "));

        getChildren().add(container);

        populateFields(); // now this works correctly

        myModel.subscribe("UpdateStatusMessage", this);
    }

    private Node createTitle() {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text("Confirm Remove Scout");
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

        confirmScoutIDField = addLabelAndField("Scout ID: ", grid, myFont, 0);
        confirmFirstNameField = addLabelAndField("First Name: ", grid, myFont, 1);
        confirmLastNameField = addLabelAndField("Last Name: ", grid, myFont, 2);
        confirmMiddleNameField = addLabelAndField("Middle Name: ", grid, myFont, 3);
        confirmDateOfBirthField = addLabelAndField("Date of Birth: ", grid, myFont, 4);
        confirmPhoneNumberField = addLabelAndField("Phone Number: ", grid, myFont, 5);
        confirmEmailField = addLabelAndField("Email: ", grid, myFont, 6);
        confirmTroopIDField = addLabelAndField("Troop ID: ", grid, myFont, 7);

        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);
        confirmButton = new Button("Confirm");
        confirmButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        confirmButton.setOnAction(e -> processConfirmation());

        cancelButton = new Button("Cancel");
        cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cancelButton.setOnAction(e -> {
            clearErrorMessage();
            goToHomeView();
        });

        doneCont.getChildren().addAll(confirmButton, cancelButton);
        vbox.getChildren().addAll(grid, doneCont);
        return vbox;
    }

    // Helper to add labels and fields
    private TextField addLabelAndField(String labelText, GridPane grid, Font font, int row) {
        Text label = new Text(labelText);
        label.setFont(font);
        label.setWrappingWidth(150);
        label.setTextAlignment(TextAlignment.RIGHT);
        grid.add(label, 0, row);

        TextField field = new TextField();
        field.setEditable(false);
        grid.add(field, 1, row);
        return field;
    }

    private void processConfirmation() {
        try {
            RemoveScoutTransaction removeScoutTransaction = new RemoveScoutTransaction();
            removeScoutTransaction.processRemoveScoutTransaction(scout.getState("ID").toString());
            displayMessage("Scout removed successfully!");
            goToHomeView();
        } catch (Exception ex) {
            displayErrorMessage("Failed to remove scout: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void populateFields() {
        confirmScoutIDField.setText(scout.getState("ID").toString());
        confirmFirstNameField.setText(scout.getState("FirstName").toString());
        confirmLastNameField.setText(scout.getState("LastName").toString());
        confirmMiddleNameField.setText(scout.getState("MiddleName").toString());
        confirmDateOfBirthField.setText(scout.getState("DateOfBirth").toString());
        confirmPhoneNumberField.setText(scout.getState("PhoneNumber").toString());
        confirmEmailField.setText(scout.getState("Email").toString());
        confirmTroopIDField.setText(scout.getState("TroopID").toString());
    }

    protected MessageView createStatusLog(String initialMessage) {
        statusLog = new MessageView(initialMessage);
        return statusLog;
    }

    public void updateState(String key, Object value) {
        clearErrorMessage();
        if (key.equals("UpdateStatusMessage")) {
            displayMessage((String) value);
        }
    }

    public void displayErrorMessage(String message) {
        statusLog.displayErrorMessage(message);
    }

    public void displayMessage(String message) {
        statusLog.displayMessage(message);
    }

    public void clearErrorMessage() {
        statusLog.clearErrorMessage();
    }

    private void goToHomeView() {
        TransactionChoiceView homeView = new TransactionChoiceView(myModel);
        Scene homeScene = new Scene(homeView);
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(homeScene);
    }
}
