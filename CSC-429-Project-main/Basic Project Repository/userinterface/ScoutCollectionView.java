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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.ScoutCollection;
import model.Scout;

import java.util.Enumeration;
import java.util.Vector;

public class ScoutCollectionView extends View {

    protected TableView<ScoutTableModel> tableOfScouts; // this shows the actual table
    protected Button cancelButton;
    protected Button submitButton;

    protected MessageView statusLog;


    //--------------------------------------------------------------------------
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

    //--------------------------------------------------------------------------
    protected void populateFields()
    {
        getEntryTableModelValues();
    }

    //--------------------------------------------------------------------------
    protected void getEntryTableModelValues()
    {

        ObservableList<ScoutTableModel> tableData = FXCollections.observableArrayList();
        try
        {
            ScoutCollection scoutCollection = (ScoutCollection)myModel.getState("ScoutList");

            Vector entryList = (Vector)scoutCollection.getState("Scout"); // gets vector from accoutns
            Enumeration entries = entryList.elements();

            while (entries.hasMoreElements() == true)
            {
                Scout nextScout = (Scout)entries.nextElement();
                Vector<String> view = nextScout.getEntryListView();

                // add this list entry to the list
                ScoutTableModel nextTableRowData = new ScoutTableModel(view);
                tableData.add(nextTableRowData);
            }
            tableOfScouts.setItems(tableData);
        }
        catch (Exception e) {//SQLException e) {
            // Need to handle this exception
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

        TableColumn ScoutIdColumn = new TableColumn("ScoutId") ;
        ScoutIdColumn.setMinWidth(100);
        ScoutIdColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("ScoutId"));

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

        TableColumn statusColumn = new TableColumn("Status") ;
        statusColumn.setMinWidth(100);
        statusColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("Status"));

        TableColumn StatusUpdateColumn = new TableColumn("Date Status Updated") ;
        StatusUpdateColumn.setMinWidth(100);
        StatusUpdateColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("DateStatusUpdated"));

        tableOfScouts.getColumns().addAll(ScoutIdColumn, FirstNameColumn, MiddleNameColumn, LastNameColumn, DOBColumn, PhoneColumn, EmailColumn, TroopIDColumn, statusColumn, StatusUpdateColumn);

        tableOfScouts.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event)
            {
                if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ){
                    processScoutSelected();
                }
            }
        });
        ScrollPane scrollPane = new ScrollPane(); // do this to make sure to see everything
        scrollPane.setPrefSize(115, 150);
        scrollPane.setContent(tableOfScouts);

        submitButton = new Button("Submit");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                // do the inquiry
                processScoutSelected();
            }
        });

        cancelButton = new Button("Back");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                //----------------------------------------------------------
                clearErrorMessage();
                myModel.stateChangeRequest("CancelScoutList", null);
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



    //--------------------------------------------------------------------------
    public void updateState(String key, Object value)
    {
    }

    //--------------------------------------------------------------------------
    protected void processScoutSelected()
    {
        ScoutTableModel selectedItem = tableOfScouts.getSelectionModel().getSelectedItem();

        if(selectedItem != null)
        {
            String selectedScoutId = selectedItem.getScoutId();
            myModel.stateChangeRequest("ScoutSelected", selectedScoutId);
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

