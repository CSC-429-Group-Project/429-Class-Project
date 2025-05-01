package model;

import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;

public class AddTreeTypeTransaction extends Transaction {

    protected AddTreeTypeTransaction() throws Exception {
        super();
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("AddTreeType", "TransactionError");
        dependencies.setProperty("AddTreeType", "UpdateStatusMessage");
        myRegistry.setDependencies(dependencies);
    }

    protected Scene createView() {
        View newView = ViewFactory.createView("AddTreeTypeView", this);
        return new Scene(newView);
    }

    public void doYourJob() {
        Scene newScene = createView();

        if (myStage.getScene() != newScene) {
            swapToView(newScene);
        }
    }

    public Object getState(String key) {
        return null;
    }

    public void stateChangeRequest(String key, Object value) throws Exception {
        if (key.equals("DoYourJob")) {
            doYourJob();
        } else if (key.equals("AddTreeType")) {
            TreeType treeType = new TreeType((Properties) value);
            treeType.processNewTreeType((Properties) value);
        }
        myRegistry.updateSubscribers(key, this);
    }
}
