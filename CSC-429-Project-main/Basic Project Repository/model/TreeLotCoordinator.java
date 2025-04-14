package model;

import event.Event;
import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import impresario.IView;
import impresario.ModelRegistry;
import javafx.scene.Scene;
import javafx.stage.Stage;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;

import java.util.Hashtable;
import java.util.Properties;

public class TreeLotCoordinator implements IView, IModel {

    private Properties dependencies;
    private ModelRegistry myRegistry;


    // GUI Components
    private Hashtable<String, Scene> myViews;
    private Stage myStage;

    // Error messages
    private String loginErrorMessage = "";
    private String transactionErrorMessage = "";

    private Scout newScout;
    private Tree selectedTree;

    public TreeLotCoordinator() {
        myStage = MainStageContainer.getInstance();
        myViews = new Hashtable<String, Scene>();

        // STEP 3.1: Create the Registry object - if you inherit from
        // EntityBase, this is done for you. Otherwise, you do it yourself
        myRegistry = new ModelRegistry("Scout");
        if(myRegistry == null)
        {
            new Event(Event.getLeafLevelClassName(this), "Librarian",
                    "Could not instantiate Registry", Event.ERROR);
        }

        // STEP 3.2: Be sure to set the dependencies correctly
        setDependencies();

        // Set up the initial view
        createAndShowTransactionChoiceView();
    }

    private void createAndShowTransactionChoiceView() {
        Scene currentScene = (Scene)myViews.get("TransactionChoiceView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("TransactionChoiceView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("TransactionChoiceView", currentScene);
        }
        // make the view visible by installing it into the frame
        swapToView(currentScene);
    }

    private void createAndShowAddScoutView() {
        Scene currentScene = (Scene)myViews.get("AddScoutView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("AddScoutView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("AddScoutView", currentScene);
        }
        // make the view visible by installing it into the frame
        swapToView(currentScene);
    }

    private void createAndShowModifyScoutView() {
        Scene currentScene = (Scene)myViews.get("ModifyScoutView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("ModifyScoutView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("ModifyScoutView", currentScene);
        }
        // make the view visible by installing it into the frame
        swapToView(currentScene);
    }

    private void createAndShowRemoveScoutView() {
        Scene currentScene = (Scene)myViews.get("RemoveScoutView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("RemoveScoutView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("RemoveScoutView", currentScene);
        }
        // make the view visible by installing it into the frame
        swapToView(currentScene);
    }

    private void createAndShowAddTreeView() {
        Scene currentScene = (Scene)myViews.get("AddTreeView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("AddTreeView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("AddTreeView", currentScene);
        }
        // make the view visible by installing it into the frame
        swapToView(currentScene);
    }

    private void createAndShowModifyTreeView() {
        Scene currentScene = (Scene)myViews.get("ModifyTreeView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("ModifyTreeView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("ModifyTreeView", currentScene);
        }
        // make the view visible by installing it into the frame
        swapToView(currentScene);
    }

    private void createAndShowModifySelectedTreeView(){
        Scene currentScene = (Scene)myViews.get("ModifySelectedTreeView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("ModifySelectedTreeView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("ModifySelectedTreeView", currentScene);
        }
        // make the view visible by installing it into the frame
        swapToView(currentScene);
    }

    @Override
    public void updateState(String key, Object value) {
        // DEBUG System.out.println("Teller.updateState: key: " + key);
        stateChangeRequest(key, value);
    }

    @Override
    public Object getState(String key) {
        if (key.equals("SelectedTree")) {
            return selectedTree;
        } else {
            return null;
        }
    }

    @Override
    public void subscribe(String key, IView subscriber) {
        myRegistry.subscribe(key, subscriber);
    }

    @Override
    public void unSubscribe(String key, IView subscriber) {
        myRegistry.unSubscribe(key, subscriber);
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        // STEP 4: Write the sCR method component for the key you
        // just set up dependencies for
        // DEBUG System.out.println("Teller.sCR: key = " + key);
        if (key.equals("TransactionChoiceView") == true) {
            createAndShowTransactionChoiceView();
        } else if (key.equals("AddScoutView") == true ){
            createAndShowAddScoutView();
        } else if (key.equals("ModifyScoutView") == true){
            createAndShowModifyScoutView();
        } else if (key.equals("RemoveScoutView") == true) {
            createAndShowRemoveScoutView();
        } else if (key.equals("AddTreeView") == true) {
            createAndShowAddTreeView();
        } else if (key.equals("ModifyTreeView")){
            createAndShowModifyTreeView();
        } else if (key.equals("AddScout") == true) {
            createNewScout();
            newScout.processNewScout((Properties)value);
            newScout.save();
        } else if (key.equals("ModifySelectedTree")){
            Properties props = (Properties)value;
            String barcode = props.getProperty("Barcode");
            updateATree(barcode);
            createAndShowModifySelectedTreeView();
        } else if (key.equals("UpdateSelectedTree")){
            Properties updatedProperties = (Properties) value;
            String newStatus = updatedProperties.getProperty("Status");
            String newNotes = updatedProperties.getProperty("Notes");

            selectedTree.stateChangeRequest("Status", newStatus);
            selectedTree.stateChangeRequest("Notes", newNotes);
            selectedTree.updateStateInDatabase();
        }
        myRegistry.updateSubscribers(key, this);
    }

    private void updateATree(String barcode) {
        try {
            selectedTree = new Tree(barcode);
        } catch (InvalidPrimaryKeyException e) {
            e.printStackTrace();
        }
    }

    private void createNewScout() {
        newScout = new Scout();
    }

    private void setDependencies() {
        dependencies = new Properties();
        myRegistry.setDependencies(dependencies);
    }

    public void swapToView(Scene newScene)
    {
        if (newScene == null)
        {
            System.out.println("Scout.swapToView(): Missing view for display");
            new Event(Event.getLeafLevelClassName(this), "swapToView",
                    "Missing view for display ", Event.ERROR);
            return;
        }

        myStage.setScene(newScene);
        myStage.sizeToScene();
        //Place in center
        WindowPosition.placeCenter(myStage);
    }
}
