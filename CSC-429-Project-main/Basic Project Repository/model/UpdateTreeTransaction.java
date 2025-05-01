package model;

import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;
import java.util.Properties;

public class UpdateTreeTransaction extends Transaction {
    private Tree selectedTree;
    private String treeTypeDescription;
    private String transactionErrorMessage = "";
    private String updatedStatusMessage = "";

    public UpdateTreeTransaction() throws Exception {
        super();
    }

    @Override
    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("UpdateSelectedTree", "TransactionError");
        myRegistry.setDependencies(dependencies);
    }

    @Override
    // Creates initial ModifyTreeView
    protected Scene createView() {
        View newView = ViewFactory.createView("ModifyTreeView", this);
        assert newView != null;
        return new Scene(newView);
    }

    // Creates the ModifySelectedTreeView that will be populated with the Tree data
    protected Scene createModifySelectedTreeView() {
        View newView = ViewFactory.createView("ModifySelectedTreeView", this);
        assert newView != null;
        return new Scene(newView);
    }

    @Override
    public void doYourJob() {
        Scene newScene = createView();

        if (myStage.getScene() != newScene) {
            swapToView(newScene);
        }
    }

    @Override
    public Object getState(String key) {
        switch (key) {
            case "SelectedTree":
                return selectedTree;
            case "treeTypeDescription":
                return treeTypeDescription;
            case "TransactionError":
                return transactionErrorMessage;
            case "UpdateStatusMessage":
                return updatedStatusMessage;
        }
        return null;
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        switch (key) {
            case "ModifySelectedTree":  // This key is sent from the ModifyTreeView
                Properties props = (Properties) value;
                String barcode = props.getProperty("Barcode");

                try {
                    selectedTree = new Tree(barcode);
                    treeTypeDescription = selectedTree.retrieveTreeTypeDescription(barcode);
                    Scene newScene = createModifySelectedTreeView();
                    swapToView(newScene);
                } catch (InvalidPrimaryKeyException e) {
                    transactionErrorMessage = "No tree found with barcode: " + barcode;
                    myRegistry.updateSubscribers("TransactionError", this);
                    System.err.println(transactionErrorMessage);
                }
                break;

            case "UpdateSelectedTree":      // This key is sent from the ModifySelectedTreeView

                try {
                    Properties updatedProperties = (Properties) value;
                    String newStatus = updatedProperties.getProperty("Status");
                    String newNotes = updatedProperties.getProperty("Notes");
                    String newUpdateDate = updatedProperties.getProperty("DateStatusUpdated");

                    selectedTree.stateChangeRequest("Status", newStatus);
                    selectedTree.stateChangeRequest("Notes", newNotes);
                    selectedTree.stateChangeRequest("DateStatusUpdated", newUpdateDate);
                    selectedTree.updateStateInDatabase();

                    updatedStatusMessage = "your tree was updated";
                    myRegistry.updateSubscribers("UpdateStatusMessage", this);

                } catch (RuntimeException e){
                    transactionErrorMessage = "Unable to update tree";
                    myRegistry.updateSubscribers("TransactionError", this);
                }

                break;

            case "DoYourJob":  // This key is sent from the TLC
                doYourJob();  // This will create and swap the view from the TransactionChoiceView
                break;
        }

        myRegistry.updateSubscribers(key, this); // Notify other subscribers
    }
}

