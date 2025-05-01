
package model;


import event.Event;
import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import impresario.IView;
import impresario.ModelRegistry;
import javafx.scene.Scene;
import javafx.stage.Stage;
import userinterface.*;

import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

public class TreeLotCoordinator implements IView, IModel {

    private Properties dependencies;
    private ModelRegistry myRegistry;


    // GUI Components
    private Hashtable<String, Scene> myViews;
    private Stage myStage;

    // Error messages
    private String loginErrorMessage = "";
    private String transactionErrorMessage = "";

    private Tree selectedTree;
    //private Tree newTree;
    private Scout selectedScout;

    public TreeLotCoordinator() {
        myStage = MainStageContainer.getInstance();
        myViews = new Hashtable<String, Scene>();

        // STEP 3.1: Create the Registry object - if you inherit from
        // EntityBase, this is done for you. Otherwise, you do it yourself
        myRegistry = new ModelRegistry("Scout");
        if(myRegistry == null)
        {
            new Event(Event.getLeafLevelClassName(this), "Scout",
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

    @Override
    public void updateState(String key, Object value) throws Exception {
        // DEBUG System.out.println("Teller.updateState: key: " + key);
        stateChangeRequest(key, value);
    }

    @Override
    public Object getState(String key) {
        if (key.equals("SelectedTree")) {
            return selectedTree;
        } else if (key.equals("selectedScout")) {
            return selectedScout;
        }
        else {
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
    public void stateChangeRequest(String key, Object value) throws Exception {
        // STEP 4: Write the sCR method component for the key you
        // just set up dependencies for
        // DEBUG
        System.out.println("Teller.sCR: key = " + key);
        if (key.equals("TransactionChoiceView") == true) {
            createAndShowTransactionChoiceView();
        } else if (key.equals("RemoveScoutView") == true) {
            createAndShowRemoveScoutView();
        } else if (key.equals("AddTreeView") == true) {
            createAndShowAddTreeView();
        } else if (key.equals("AddScoutTransaction")) {
            doTransaction(key);
        }  else if (key.equals("ModifyTreeTransaction")) {
            doTransaction(key);
        } else if (key.equals("CancelTransaction")) {
            createAndShowTransactionChoiceView();
        } else if (key.equals("ConfirmRMV")) {
            createAndShowComfirmRemove();
        }else if (key.equals("RemoveTree")){
            doTransaction("RemoveTree");
        }
        else if (key.equals("ModifyTreeView")){
            //createAndShowModifyTreeView();
        } else if (key.equals("AddScout") == true) {
            //createNewScout();
            //newScout.processNewScout((Properties)value);
            //newScout.save();
        }else if (key.equals("AddTree") == true) {
            Tree newTree = new Tree(); // or whatever the correct class name and constructor is
            newTree.processNewTree((Properties)value);
            //newTree.save();
        }
        else if (key.equals("SearchSelectScout") == true) {
            createAndShowSearchSelectScoutView();
        }
        else if (key.equals("Search")) {
            try {
                RemoveScoutTransaction removeTrans = new RemoveScoutTransaction();
                removeTrans.stateChangeRequest("Search", value);
            } catch (Exception e) {
                new Event(Event.getLeafLevelClassName(this), "stateChangeRequest",
                        "Error handling 'Search': " + e.getMessage(), Event.ERROR);
                e.printStackTrace();
            }
        } else if (key.equals("ModifyScoutTransaction")){
            doTransaction(key);
        }
        myRegistry.updateSubscribers(key, this);
    }


    /**
     * Create a Transaction depending on the Transaction type (deposit,
     * withdraw, transfer, etc.). Use the AccountHolder holder data to do the
     * create.
     */
    //----------------------------------------------------------
    public void doTransaction(String transactionType)
    {
        try
        {
            Transaction trans = TransactionFactory.createTransaction(
                    transactionType);

            trans.subscribe("CancelTransaction", this);
            trans.stateChangeRequest("DoYourJob", "");
        }
        catch (Exception ex)
        {
            transactionErrorMessage = "FATAL ERROR: TRANSACTION FAILURE: Unrecognized transaction!!";
            new Event(Event.getLeafLevelClassName(this), "createTransaction",
                    "Transaction Creation Failure: Unrecognized transaction " + ex.toString(),
                    Event.ERROR);
        }
    }

    private void setDependencies() {
        dependencies = new Properties();
        myRegistry.setDependencies(dependencies);
    }

    private void createAndShowSearchSelectScoutView() {
        Scene currentScene = (Scene) myViews.get("SearchSelectScoutView");

        if (currentScene == null) {
            View newView = ViewFactory.createView("SearchSelectScoutView", this);
            currentScene = new Scene(newView);
            myViews.put("SearchSelectScoutView", currentScene);
        }

        swapToView(currentScene);
    }

    private void createAndShowComfirmRemove(){
        Scene currentScene = (Scene) myViews.get("ConfirmRMV");

        if(currentScene == null){
            View newView = ViewFactory.createView("ConfirmRMV", this);
            currentScene = new Scene(newView);
            myViews.put("ConfirmRMV", currentScene);
        }
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
