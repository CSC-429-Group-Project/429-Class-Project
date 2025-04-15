package model;

import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;

public class AddScoutTransaction extends Transaction {

    public AddScoutTransaction() throws Exception {
        super();
    }

    //----------------------------------------------------------
    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("AddScout", "TransactionError");
        myRegistry.setDependencies(dependencies);
    }

    //----------------------------------------------------------
    protected Scene createView() {
        System.out.println("Creating AddScoutView scene...");
        View newView = ViewFactory.createView("AddScoutView", this);
        return new Scene(newView);
    }

    //----------------------------------------------------------
    public void doYourJob() {
        System.out.println("AddScoutTransaction.doYourJob() called");
        Scene newScene = createView();
        // Check if the scene already exists. If yes, create a new instance and swap it.
        if (myStage.getScene() != newScene) {
            swapToView(newScene); // Swap view only if it's a new scene
        }
    }

    //----------------------------------------------------------
    public Object getState(String key) {
        return null; // Add more logic here if the view needs to retrieve state
    }

    //----------------------------------------------------------
    public void stateChangeRequest(String key, Object value) {
        if (key.equals("AddScout")) {
            Scout newScout = new Scout();
            newScout.processNewScout((Properties) value);
            newScout.save();
        } else if (key.equals("DoYourJob")) {
            doYourJob();  // This will create + swap the view
        }

        myRegistry.updateSubscribers(key, this);
    }
}

