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

import java.util.Properties;
import java.util.Vector;

import impresario.IModel;
import model.RemoveScoutTransaction;
import model.Scout;
import model.ScoutCollection;

public class RemoveScoutView extends View {

    // GUI components for search criteria
    protected TextField scoutIDField;
    protected TextField lastNameField;

    // GUI components for displaying search results
    protected ListView<Scout> scoutListView;

    // GUI components for confirmation
    protected TextField confirmScoutIDField;
    protected TextField confirmFirstNameField;
    protected TextField confirmLastNameField;
    protected TextField confirmMiddleNameField;
    protected TextField confirmDateOfBirthField;
    protected TextField confirmPhoneNumberField;
    protected TextField confirmEmailField;
    protected TextField confirmTroopIDField;

    protected Button searchButton;
    protected Button selectButton;
    protected Button confirmButton;
    protected Button cancelButton;

    protected MessageView statusLog;
    protected Vector<Scout> scouts;
    protected Scout selectedScout;

    public RemoveScoutView(IModel model) {
        super(model, "RemoveScoutView");

        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        container.getChildren().add(createTitle());
        container.getChildren().add(createSearchFormContent());
        container.getChildren().add(createStatusLog("             "));

        getChildren().add(container);

        myModel.subscribe("UpdateStatusMessage", this);
    }

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

    private VBox createSearchFormContent() {
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
        grid.add(scoutIDLabel, 0, 0);
        scoutIDField = new TextField();
        grid.add(scoutIDField, 1, 0);

        Text lastNameLabel = new Text("Last Name: ");
        lastNameLabel.setFont(myFont);
        lastNameLabel.setWrappingWidth(150);
        lastNameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(lastNameLabel, 0, 1);
        lastNameField = new TextField();
        grid.add(lastNameField, 1, 1);

        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);
        searchButton = new Button("Search");
        searchButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                processSearch();
            }
        });
        cancelButton = new Button("Cancel");
        cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                goToHomeView();
            }
        });
        doneCont.getChildren().addAll(searchButton, cancelButton);
        vbox.getChildren().add(doneCont);

        vbox.getChildren().add(grid);
        return vbox;
    }

    private void processSearch() {
        Properties searchCriteria = new Properties();
        if (!scoutIDField.getText().isEmpty()) searchCriteria.setProperty("ScoutID", scoutIDField.getText());
        if (!lastNameField.getText().isEmpty()) searchCriteria.setProperty("LastName", lastNameField.getText());

        try {
            ScoutCollection scoutCollection = new ScoutCollection();
            scouts = scoutCollection.findScouts(searchCriteria);
            if (scouts.isEmpty()) {
                displayMessage("No scouts found matching the criteria.");
            } else {
                displaySearchResults();
            }
        } catch (Exception ex) {
            displayErrorMessage("Failed to search for scouts: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void displaySearchResults() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        scoutListView = new ListView<>();
        scoutListView.getItems().addAll(scouts);
        scoutListView.setCellFactory(param -> new ListCell<Scout>() {
            @Override
            protected void updateItem(Scout item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getState("ScoutID") + " - " + item.getState("FirstName") + " " + item.getState("LastName"));
                }
            }
        });

        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);
        selectButton = new Button("Select");
        selectButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        selectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                processSelection();
            }
        });
        cancelButton = new Button("Cancel");
        cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                goToHomeView();
            }
        });
        doneCont.getChildren().addAll(selectButton, cancelButton);

        container.getChildren().addAll(scoutListView, doneCont);
        getChildren().clear();
        getChildren().add(container);
    }

    private void processSelection() {
        selectedScout = scoutListView.getSelectionModel().getSelectedItem();
        if (selectedScout == null) {
            displayErrorMessage("Please select a scout.");
            return;
        }

        displayConfirmationView();
    }

    private void displayConfirmationView() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

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
        grid.add(scoutIDLabel, 0, 0);
        confirmScoutIDField = new TextField(selectedScout.getState("ScoutID").toString());
        confirmScoutIDField.setEditable(false);
        grid.add(confirmScoutIDField, 1, 0);

        Text firstNameLabel = new Text("First Name: ");
        firstNameLabel.setFont(myFont);
        firstNameLabel.setWrappingWidth(150);
        firstNameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(firstNameLabel, 0, 1);
        confirmFirstNameField = new TextField(selectedScout.getState("FirstName").toString());
        confirmFirstNameField.setEditable(false);
        grid.add(confirmFirstNameField, 1, 1);

        Text lastNameLabel = new Text("Last Name: ");
        lastNameLabel.setFont(myFont);
        lastNameLabel.setWrappingWidth(150);
        lastNameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(lastNameLabel, 0, 2);
        confirmLastNameField = new TextField(selectedScout.getState("LastName").toString());
        confirmLastNameField.setEditable(false);
        grid.add(confirmLastNameField, 1, 2);

        Text middleNameLabel = new Text("Middle Name: ");
        middleNameLabel.setFont(myFont);
        middleNameLabel.setWrappingWidth(150);
        middleNameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(middleNameLabel, 0, 3);
        confirmMiddleNameField = new TextField(selectedScout.getState("MiddleName").toString());
        confirmMiddleNameField.setEditable(false);
        grid.add(confirmMiddleNameField, 1, 3);

        Text dateOfBirthLabel = new Text("Date of Birth: ");
        dateOfBirthLabel.setFont(myFont);
        dateOfBirthLabel.setWrappingWidth(150);
        dateOfBirthLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(dateOfBirthLabel, 0, 4);
        confirmDateOfBirthField = new TextField(selectedScout.getState("DateOfBirth").toString());
        confirmDateOfBirthField.setEditable(false);
        grid.add(confirmDateOfBirthField, 1, 4);

        Text phoneNumberLabel = new Text("Phone Number: ");
        phoneNumberLabel.setFont(myFont);
        phoneNumberLabel.setWrappingWidth(150);
        phoneNumberLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(phoneNumberLabel, 0, 5);
        confirmPhoneNumberField = new TextField(selectedScout.getState("PhoneNumber").toString());
        confirmPhoneNumberField.setEditable(false);
        grid.add(confirmPhoneNumberField, 1, 5);

        Text emailLabel = new Text("Email: ");
        emailLabel.setFont(myFont);
        emailLabel.setWrappingWidth(150);
        emailLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(emailLabel, 0, 6);
        confirmEmailField = new TextField(selectedScout.getState("Email").toString());
        confirmEmailField.setEditable(false);
        grid.add(confirmEmailField, 1, 6);

        Text troopIDLabel = new Text("Troop ID: ");
        troopIDLabel.setFont(myFont);
        troopIDLabel.setWrappingWidth(150);
        troopIDLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(troopIDLabel, 0, 7);
        confirmTroopIDField = new TextField(selectedScout.getState("TroopID").toString());
        confirmTroopIDField.setEditable(false);
        grid.add(confirmTroopIDField, 1, 7);

        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);
        confirmButton = new Button("Confirm");
        confirmButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                processConfirmation();
            }
        });
        cancelButton = new Button("Cancel");
        cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                goToHomeView();
            }
        });
        doneCont.getChildren().addAll(confirmButton, cancelButton);

        container.getChildren().addAll(grid, doneCont);
        getChildren().clear();
        getChildren().add(container);
    }

    private void processConfirmation() {
        try {
            RemoveScoutTransaction removeScoutTransaction = new RemoveScoutTransaction();
            removeScoutTransaction.processRemoveScoutTransaction(selectedScout.getState("ScoutID").toString());
            displayMessage("Scout removed successfully!");
            goToHomeView();
        } catch (Exception ex) {
            displayErrorMessage("Failed to remove scout: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    protected MessageView createStatusLog(String initialMessage) {
        statusLog = new MessageView(initialMessage);
        return statusLog;
    }

    public void updateState(String key, Object value) {
        clearErrorMessage();
        if (key.equals("UpdateStatusMessage")) {
            String val = (String) value;
            displayMessage(val);
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
