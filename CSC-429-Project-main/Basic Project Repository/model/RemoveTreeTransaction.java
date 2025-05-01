package model;

import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;

public class RemoveTreeTransaction extends Transaction{
    protected RemoveTreeTransaction() throws Exception {
        super();
    }

    @Override
    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("RemoveTree", "TransactionError");
        myRegistry.setDependencies(dependencies);
    }

    @Override
    protected Scene createView() {
        System.out.println("Creating RemoveTreeView scene...");
        View newView = ViewFactory.createView("RemoveTreeView", this);
        return new Scene(newView);
    }

    public void doYourJob() {
        System.out.println("RemoveTreeTransaction.doYourJob() called");
        Scene newScene = createView();

        if (myStage.getScene() != newScene) {
            swapToView(newScene);
        }
    }

    @Override
    public Object getState(String key) {
        return null;
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        if (key.equals("RemoveTreeS")) {
            Tree tree = new Tree();
            tree.deleteTree((Properties) value);
        } else if (key.equals("DoYourJob")) {
            doYourJob();
        }

        myRegistry.updateSubscribers(key, this);
    }
}
