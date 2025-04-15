package model;

import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;

public class UpdateTreeTransaction extends Transaction {
    private Tree selectedTree;

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
    protected Scene createView() {
        System.out.println("Creating UpdateTreeView scene...");
        View newView = ViewFactory.createView("ModifyTreeView", this);
        return new Scene(newView);
    }

    protected Scene createModifySelectedTreeView() {
        View newView = ViewFactory.createView("ModifySelectedTreeView", this);
        return new Scene(newView);
    }

    @Override
    public void doYourJob() {
        System.out.println("UpdateTreeTransaction.doYourJob() called");
        Scene newScene = createView();

        if (myStage.getScene() != newScene) {
            swapToView(newScene);
        }
    }

    @Override
    public Object getState(String key) {
        if (key.equals("SelectedTree")) {
            return selectedTree;
        }
        return null;
    }


    @Override
    public void stateChangeRequest(String key, Object value) {
        if (key.equals("ModifySelectedTree")){
            Properties props = (Properties)value;
            String barcode = props.getProperty("Barcode");

            try {
                selectedTree = new Tree(barcode);
                Scene newScene = createModifySelectedTreeView();
                swapToView(newScene);
            } catch (InvalidPrimaryKeyException e) {
                System.err.println("Tree not found with barcode: " + barcode);
                e.printStackTrace();
            }

        } else if (key.equals("UpdateSelectedTree")) {
            Properties updatedProperties = (Properties) value;
            String newStatus = updatedProperties.getProperty("Status");
            String newNotes = updatedProperties.getProperty("Notes");
            String newUpdateDate = updatedProperties.getProperty("DateStatusUpdated");

            if (selectedTree != null) {
                selectedTree.stateChangeRequest("Status", newStatus);
                selectedTree.stateChangeRequest("Notes", newNotes);
                selectedTree.stateChangeRequest("DateStatusUpdated", newUpdateDate);
                selectedTree.updateStateInDatabase();
            } else {
                System.err.println("No tree selected to update.");
            }

            System.out.println("Tree updated successfully!");
        } else if (key.equals("DoYourJob")) {
            doYourJob();  // This will create + swap the view
        }

        myRegistry.updateSubscribers(key, this); // Notify other subscribers
    }
}

