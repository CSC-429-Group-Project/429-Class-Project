// specify the package
package userinterface;

// system imports

import java.time.LocalDate;



import database.SQLSelectStatement;
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
import model.Tree;

import java.util.Properties;
import java.util.Vector;

/** The class containing the Add Tree View */
public class AddTreeView extends View {

    protected TextField barcode;
    protected TextField treeType;
    protected TextArea notes;
    protected ComboBox<String> status;
    protected Button cancelButton;
    protected Button submitButton;
    protected MessageView statusLog;

    public AddTreeView(IModel tree) {
        super(tree, "AddTreeView");

        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());
        container.getChildren().add(createStatusLog(" "));
        getChildren().add(container);
    }

    private Node createTitle() {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);
        Text titleText = new Text("Tree Lot System//ADDTREEVIEW");
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

        Text barcodeLabel = new Text(" Barcode : ");
        barcodeLabel.setFont(myFont);
        grid.add(barcodeLabel, 0, 1);
        barcode = new TextField();
        grid.add(barcode, 1, 1);


        Text notesLabel = new Text(" Notes : ");
        notesLabel.setFont(myFont);
        grid.add(notesLabel, 0, 3);
        notes = new TextArea();
        notes.setPrefRowCount(3);
        grid.add(notes, 1, 3);

        Text statusLabel = new Text(" Status : ");
        statusLabel.setFont(myFont);
        grid.add(statusLabel, 0, 4);
        status = new ComboBox<>();
        status.getItems().addAll("Available", "Sold", "Damaged");
        status.setValue("Available");
        grid.add(status, 1, 4);

        vbox.getChildren().add(grid);

//        HBox buttonContainer = new HBox(75);
//        buttonContainer.setAlignment(Pos.CENTER);
//        cancelButton = new Button("Back");
//        cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
//        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent e) {
//                clearErrorMessage();
//                goToHomeView();
//            }
//        }
                HBox buttonContainer = new HBox(75);
        buttonContainer.setAlignment(Pos.CENTER);
        cancelButton = new Button("Back");
        cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
                                     @Override
                                     public void handle(ActionEvent e) {
                                         clearErrorMessage();
                                         barcode.clear();
                                         notes.clear();
                                         try {
                                             myModel.stateChangeRequest("CancelTransaction", null);
                                         } catch (Exception ex) {
                                             throw new RuntimeException(ex);
                                         }
                                     }
                                 }




        );
        buttonContainer.getChildren().add(cancelButton);


        submitButton = new Button("Submit");
        submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                processAction();
            }
        });
        buttonContainer.getChildren().add(submitButton);
        vbox.getChildren().add(buttonContainer);

        return vbox;
    }

    public void processAction() {
        String barcodeValue = barcode.getText().trim();
        String treeTypeValue = barcodeValue.substring(0, 2);
        String notesValue = notes.getText().trim();
        String statusValue = status.getValue();

        if (barcodeValue.isEmpty() || treeTypeValue.isEmpty() || notesValue.isEmpty() || statusValue.isEmpty()){
            displayErrorMessage("Fields cannot be empty!");
            return;
        }

        Properties props = new Properties();
        props.setProperty("Barcode", barcodeValue);
        props.setProperty("Tree_Type", treeTypeValue);
        props.setProperty("Notes", notesValue);
        props.setProperty("Status", statusValue);
        props.setProperty("DateStatusUpdated", LocalDate.now().toString());


        try {
            //myModel.stateChangeRequest("AddTree", props);
            Tree newTree = new Tree();
            newTree.processNewTree(props);
            //newTree.save();
            displayMessage("AddTree/AddTreeView Successfully called");
        } catch (Exception ex) {
            displayErrorMessage("Error adding tree.");
            ex.printStackTrace();
        }
    }
    //---------------------------------------------------------
    public void updateState(String key, Object value) {
        clearErrorMessage();
        if (key.equals("Status") == true)
        {
            String val = (String)value;
            status.setValue(val);
            displayMessage("Status Updated to:  " + val);
        }
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
