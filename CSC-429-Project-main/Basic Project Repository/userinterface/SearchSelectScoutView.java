package userinterface;

import impresario.IModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.ModifyScoutTransaction;
import model.RemoveScoutTransaction;
import model.ScoutCollection;
import model.Scout;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class SearchSelectScoutView extends View {

    protected TableView<ScoutTableModel> tableOfScouts; // this shows the actual table
    protected Button cancelButton;

    protected MessageView statusLog;

    //--------------------------------------------------------------------------
    public SearchSelectScoutView(IModel wsc)
    {
        super(wsc, "ScoutCollectionView");

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        // create our GUI components, add them to this panel
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());

        // Error message area
        container.getChildren().add(createStatusLog("                                            "));

        getChildren().add(container);

        populateFields();
    }

    //--------------------------------------------------------------------------
    protected void populateFields()
    {
        getEntryTableModelValues();
    }

    //--------------------------------------------------------------------------
    protected void getEntryTableModelValues()
    {
        ObservableList<ScoutTableModel> tableData = FXCollections.observableArrayList();

        try {
            // Pull the data that was passed in from the model
            ScoutCollection scoutCollection = (ScoutCollection) myModel.getState("ScoutList");

            if (scoutCollection == null) {
                System.out.println("ScoutCollection is null (SearchSelectScoutView line 77)");
                return;
            }

            // Ensure that the ScoutCollection is populated with Scout objects, not Properties
            Vector<Scout> entryList = scoutCollection.getScouts();  // Assumes ScoutCollection has a method getScouts()

            for (Scout scout : entryList) {
                Vector<String> view = new Vector<>();

                // Retrieve data from Scout object's persistentState
                view.add(scout.getState("ID").toString());
                view.add(scout.getState("LastName").toString());
                view.add(scout.getState("FirstName").toString());
                view.add(scout.getState("MiddleName").toString());
                view.add(scout.getState("DateOfBirth").toString());
                view.add(scout.getState("PhoneNumber").toString());
                view.add(scout.getState("Email").toString());
                view.add(scout.getState("TroopID").toString());
                view.add(scout.getState("Status").toString());
                view.add(scout.getState("DateStatusUpdated").toString());

                // Create table row data and add it to the table
                ScoutTableModel nextTableRowData = new ScoutTableModel(view);
                tableData.add(nextTableRowData);
            }

            // Set the table items with the processed table data
            tableOfScouts.setItems(tableData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Create the title container
    //-------------------------------------------------------------
    private Node createTitle()
    {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" Scouts ");
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

        Text prompt = new Text("LIST OF SCOUTS");
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        tableOfScouts = new TableView<ScoutTableModel>();
        tableOfScouts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); // only one row can be selected

        TableColumn FirstNameColumn = new TableColumn("First Name");
        FirstNameColumn.setMinWidth(100);
        FirstNameColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("FirstName"));

        TableColumn MiddleNameColumn = new TableColumn("Middle Name");
        MiddleNameColumn.setMinWidth(100);
        MiddleNameColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("MiddleName"));

        TableColumn LastNameColumn = new TableColumn("Last Name");
        LastNameColumn.setMinWidth(100);
        LastNameColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("LastName"));

        TableColumn DOBColumn = new TableColumn("DOB");
        DOBColumn.setMinWidth(100);
        DOBColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("DateOfBirth"));

        TableColumn PhoneColumn = new TableColumn("Phone #");
        PhoneColumn.setMinWidth(100);
        PhoneColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("PhoneNumber"));

        TableColumn EmailColumn = new TableColumn("Email");
        EmailColumn.setMinWidth(100);
        EmailColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("Email"));

        TableColumn TroopIDColumn = new TableColumn("Troop ID");
        TroopIDColumn.setMinWidth(100);
        TroopIDColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("TroopID"));

        TableColumn ScoutIDColumn = new TableColumn("Scout ID");
        ScoutIDColumn.setMinWidth(100);
        ScoutIDColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("ScoutId"));

        TableColumn statusColumn = new TableColumn("Status");
        statusColumn.setMinWidth(100);
        statusColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("Status"));

        tableOfScouts.getColumns().addAll(FirstNameColumn, MiddleNameColumn, LastNameColumn, DOBColumn, PhoneColumn, EmailColumn, TroopIDColumn, ScoutIDColumn, statusColumn);

        // Double-click row event handler to navigate to ConfirmRemoveScoutView
        tableOfScouts.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !tableOfScouts.getSelectionModel().isEmpty()) {
                ScoutTableModel selectedScoutModel = tableOfScouts.getSelectionModel().getSelectedItem();
                if (selectedScoutModel != null) {
                    String scoutId = selectedScoutModel.getScoutId();
                    Scout selectedScout = ScoutCollection.findScoutById(scoutId); // You need this utility method or similar logic
                    ConfirmRemoveScoutView confirmView = new ConfirmRemoveScoutView(myModel, selectedScout);
                    Scene scene = new Scene(confirmView);
                    Stage stage = (Stage) tableOfScouts.getScene().getWindow();
                    stage.setScene(scene);
                }
            }
        });



        ScrollPane scrollPane = new ScrollPane(); // do this to make sure to see everything
        scrollPane.setPrefSize(115, 150);
        scrollPane.setContent(tableOfScouts);

        cancelButton = new Button("Back");
        cancelButton.setOnAction(e -> {
            try {
                System.out.println("Attempting to transition to TransactionChoiceView");
                myModel.stateChangeRequest("TransactionChoiceView", null); // Ensure this state exists in your model
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(cancelButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(scrollPane);
        vbox.getChildren().add(btnContainer);

        return vbox;
    }

    //--------------------------------------------------------------------------
    public void updateState(String key, Object value)
    {
    }

    //--------------------------------------------------------------------------
    protected void processScoutSelected()
    {
        ScoutTableModel selectedScout = tableOfScouts.getSelectionModel().getSelectedItem();

        if (selectedScout != null) {
            Vector<String> scoutData = new Vector<>();
            scoutData.add(selectedScout.getScoutId());

            // Now you can use scoutData as needed
            System.out.println("Selected row as Vector: " + scoutData);
            try {
                myModel.stateChangeRequest("ConfirmRMV", scoutData);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    //--------------------------------------------------------------------------
    protected MessageView createStatusLog(String initialMessage)
    {
        statusLog = new MessageView(initialMessage);
        return statusLog;
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
