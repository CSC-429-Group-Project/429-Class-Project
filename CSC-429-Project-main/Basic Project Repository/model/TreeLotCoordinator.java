package model;

import event.Event;
import impresario.IModel;
import impresario.IView;
import impresario.ModelRegistry;
import javafx.scene.Scene;
import javafx.stage.Stage;
import userinterface.*;
import database.*;

import java.sql.*;
import java.sql.SQLException;
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

    public TreeLotCoordinator() {
        myStage = MainStageContainer.getInstance();
        myViews = new Hashtable<String, Scene>();

        // STEP 3.1: Create the Registry object - if you inherit from
        // EntityBase, this is done for you. Otherwise, you do it yourself
        myRegistry = new ModelRegistry("Scout");

        try {
            Connection conn = JDBCBroker.getInstance().getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Connected to database: " + conn.getCatalog());
            } else {
                System.out.println("❌ Failed to connect: connection is null or closed.");
            }
        } catch (SQLException e) {
            System.out.println("❌ SQL Exception when testing DB connection");
            e.printStackTrace();
        }


        if(myRegistry == null)
        {
            new Event(Event.getLeafLevelClassName(this), "TreeLotCoordinator",
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

    private void createAndShowScoutCollectionView() {
        Scene currentScene = (Scene)myViews.get("ScoutCollectionView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("ScoutCollectionView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("ScoutCollectionView", currentScene);
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
        if (key.equals("ScoutList")) {
            return "";
        }
        return null;
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
        } else if (key.equals("RemoveScoutView") == true){
            createAndShowRemoveScoutView();
        } else if (key.equals("retrieveInitialScouts") == true) {
            ModifyScoutTransaction allScouts = new ModifyScoutTransaction((Properties)value);
            System.out.println("stateChangeRequest argument props: " + value);
            allScouts.retrieveInitialScouts();
            createAndShowScoutCollectionView();
        }

        myRegistry.updateSubscribers(key, this);
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
