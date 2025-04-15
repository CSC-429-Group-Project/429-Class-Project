package userinterface;

import impresario.IModel;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class RemoveScoutView extends View {

    protected TextField scoutIdField;
    protected Button searchButton;
    protected Button cancelButton;
    protected MessageView statusLog;

    public RemoveScoutView(IModel model) {
        super(model, "RemoveScoutView");

        // Set up the container
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 15, 15, 15));

        // Title
        Text title = new Text("Remove Scout");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setFill(Color.DARKGREEN);
        container.getChildren().add(title);

        // Form contents
        container.getChildren().add(createFormContents());

        // Status log
        statusLog = new MessageView("");
        container.getChildren().add(statusLog);

        getChildren().add(container);
    }

    private VBox createFormContents() {
        VBox form = new VBox(10);

        // Scout ID
        HBox idBox = new HBox(10);
        Label idLabel = new Label("Enter Scout ID:");
        scoutIdField = new TextField();
        idBox.getChildren().addAll(idLabel, scoutIdField);

        // Buttons
        HBox buttonBox = new HBox(10);
        searchButton = new Button("Search");
        cancelButton = new Button("Cancel");

        buttonBox.getChildren().addAll(searchButton, cancelButton);

        searchButton.setOnAction(e -> processSearchAction());
        cancelButton.setOnAction(e -> {
            try {
                myModel.stateChangeRequest("TransactionChoiceView", null);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        form.getChildren().addAll(idBox, buttonBox);
        return form;
    }



    private void processSearchAction() {
        String scoutId = scoutIdField.getText().trim();

        if (scoutId.isEmpty()) {
            statusLog.displayErrorMessage("Please enter a valid Scout ID.");
            return;
        }

        // Ask the model to perform the search. Let the model handle the next view.
        try {
            myModel.stateChangeRequest("Search", scoutId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateState(String key, Object value) {
        // Optional: Handle status updates
    }

    //@Override
    protected MessageView createStatusLog(String initialMessage) {
        return new MessageView(initialMessage);
    }
}
