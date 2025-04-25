package userinterface;

import impresario.IModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.ScoutCollection;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class ScoutCollectionView extends View {

    protected TableView<ScoutTableModel> tableOfScouts; // this shows the actual table
    protected Button cancelButton;
    protected Button submitButton;

    protected MessageView statusLog;

    public ScoutCollectionView(IModel wsc)
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

    protected void populateFields()
    {
        getEntryTableModelValues();
    }

    protected void getEntryTableModelValues()
    {

        ObservableList<ScoutTableModel> tableData = FXCollections.observableArrayList();
        try
        {
            ScoutCollection scoutCollection = new ScoutCollection();

            Vector entryList = (Vector)scoutCollection.getState("getRetrievedData"); // gets vector from accounts
            Enumeration entries = entryList.elements();

            while (entries.hasMoreElements()) {
                Properties scoutProps = (Properties) entries.nextElement();

                Vector<String> view = new Vector<>();
                view.add(scoutProps.getProperty("ID"));
                view.add(scoutProps.getProperty("LastName"));
                view.add(scoutProps.getProperty("FirstName"));
                view.add(scoutProps.getProperty("MiddleName"));
                view.add(scoutProps.getProperty("DateOfBirth"));
                view.add(scoutProps.getProperty("PhoneNumber"));
                view.add(scoutProps.getProperty("Email"));
                view.add(scoutProps.getProperty("TroopID"));
                view.add(scoutProps.getProperty("Status"));
                view.add(scoutProps.getProperty("DateStatusUpdated"));

                ScoutTableModel nextTableRowData = new ScoutTableModel(view);
                tableData.add(nextTableRowData);
            }

            tableOfScouts.setItems(tableData);
        }
        catch (Exception e) {//SQLException e) {
            System.out.println(e);
            // Need to handle this exception
        }
    }

    // Create the title container
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

        TableColumn FirstNameColumn = new TableColumn("First Name") ;
        FirstNameColumn.setMinWidth(100);
        FirstNameColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("FirstName"));

        TableColumn MiddleNameColumn = new TableColumn("Middle Name") ;
        MiddleNameColumn.setMinWidth(100);
        MiddleNameColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("MiddleName"));

        TableColumn LastNameColumn = new TableColumn("Last Name") ;
        LastNameColumn.setMinWidth(100);
        LastNameColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("LastName"));

        TableColumn DOBColumn = new TableColumn("DOB") ;
        DOBColumn.setMinWidth(100);
        DOBColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("DateOfBirth"));

        TableColumn PhoneColumn = new TableColumn("Phone #") ;
        PhoneColumn.setMinWidth(100);
        PhoneColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("PhoneNumber"));

        TableColumn EmailColumn = new TableColumn("Email") ;
        EmailColumn.setMinWidth(100);
        EmailColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("Email"));

        TableColumn TroopIDColumn = new TableColumn("Troop ID") ;
        TroopIDColumn.setMinWidth(100);
        TroopIDColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("TroopID"));

        TableColumn ScoutIDColumn = new TableColumn("Scout ID") ;
        ScoutIDColumn.setMinWidth(100);
        ScoutIDColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("ScoutId"));

        TableColumn statusColumn = new TableColumn("Status") ;
        statusColumn.setMinWidth(100);
        statusColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("Status"));

        tableOfScouts.getColumns().addAll(FirstNameColumn, MiddleNameColumn, LastNameColumn, DOBColumn, PhoneColumn, EmailColumn, TroopIDColumn, ScoutIDColumn, statusColumn);

        tableOfScouts.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event)
            {
                if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ){
                    try {
                        processScoutSelected();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        ScrollPane scrollPane = new ScrollPane(); // do this to make sure to see everything
        scrollPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        tableOfScouts.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        scrollPane.setContent(tableOfScouts);

        submitButton = new Button("Submit");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                // do the inquiry
                try {
                    processScoutSelected();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        cancelButton = new Button("Back");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                //----------------------------------------------------------
                clearErrorMessage();
                try {
                    myModel.stateChangeRequest("CancelTransaction", null);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(submitButton);
        btnContainer.getChildren().add(cancelButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(scrollPane);
        vbox.getChildren().add(btnContainer);

        return vbox;
    }

    @Override
    public void updateState(String key, Object value) {
        if (key.equals("RefreshScoutList")) {
            tableOfScouts.getItems().clear();
            getEntryTableModelValues();
        }
    }

    protected void processScoutSelected() throws Exception {
        ScoutTableModel selectedScout = tableOfScouts.getSelectionModel().getSelectedItem();
        if (selectedScout != null) {
            Vector<String> scoutData = new Vector<>();
            scoutData.add(selectedScout.getScoutId());

            // Now you can use scoutData as needed
            myModel.stateChangeRequest("ScoutSelected", scoutData);
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
}

