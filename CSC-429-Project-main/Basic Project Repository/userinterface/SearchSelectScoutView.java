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
import javafx.beans.property.SimpleStringProperty;

import java.util.Properties;
import java.util.Vector;

import impresario.IModel;
import model.Scout;
import model.ScoutCollection;

public class SearchSelectScoutView extends View {

    // GUI components for displaying search results
    protected TableView<Scout> scoutTableView;

    protected Button selectButton;
    protected Button cancelButton;

    protected MessageView statusLog;
    protected Vector<Scout> scouts;

    public SearchSelectScoutView(IModel model, Vector<Scout> scouts) {
        super(model, "SearchSelectScoutView");
        this.scouts = scouts;

        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        container.getChildren().add(createTitle());
        container.getChildren().add(createTableContent());
        container.getChildren().add(createStatusLog("             "));

        getChildren().add(container);

        myModel.subscribe("UpdateStatusMessage", this);
    }

    private Node createTitle() {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text("Select Scout");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
        container.getChildren().add(titleText);

        return container;
    }

    private VBox createTableContent() {
        VBox vbox = new VBox(10);

        scoutTableView = new TableView<>();
        scoutTableView.getItems().addAll(scouts);

        TableColumn<Scout, String> scoutIDColumn = new TableColumn<>("Scout ID");
        scoutIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState("ScoutID").toString()));

        TableColumn<Scout, String> firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState("FirstName").toString()));

        TableColumn<Scout, String> lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState("LastName").toString()));

        TableColumn<Scout, String> middleNameColumn = new TableColumn<>("Middle Name");
        middleNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState("MiddleName").toString()));

        TableColumn<Scout, String> dateOfBirthColumn = new TableColumn<>("Date of Birth");
        dateOfBirthColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState("DateOfBirth").toString()));

        TableColumn<Scout, String> phoneNumberColumn = new TableColumn<>("Phone Number");
        phoneNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState("PhoneNumber").toString()));

        TableColumn<Scout, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState("Email").toString()));

        TableColumn<Scout, String> troopIDColumn = new TableColumn<>("Troop ID");
        troopIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState("TroopID").toString()));

        scoutTableView.getColumns().addAll(scoutIDColumn, firstNameColumn, lastNameColumn, middleNameColumn, dateOfBirthColumn, phoneNumberColumn, emailColumn, troopIDColumn);

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
        vbox.getChildren().addAll(scoutTableView, doneCont);

        return vbox;
    }

    private void processSelection() {
        Scout selectedScout = scoutTableView.getSelectionModel().getSelectedItem();
        if (selectedScout == null) {
            displayErrorMessage("Please select a scout.");
            return;
        }

        goToConfirmRemoveScoutView(selectedScout);
    }

    private void goToConfirmRemoveScoutView(Scout scout) {
        ConfirmRemoveScoutView confirmRemoveScoutView = new ConfirmRemoveScoutView(myModel, scout);
        Scene confirmRemoveScoutScene = new Scene(confirmRemoveScoutView);
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(confirmRemoveScoutScene);
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
