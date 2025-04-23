package model;

import impresario.IView;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;
import java.util.Properties;
import java.util.Vector;
import java.util.Enumeration;
import java.sql.*;

import database.*;
import exception.InvalidPrimaryKeyException;
import java.util.Properties;
import java.util.Vector;

public class ModifyScoutTransactionv2 extends Transaction {
    private static String tableName = "scout";
    protected Properties persistentState;
    private Scout selectedScout;
    private static Vector<Properties> dataRetrieved;
    private Vector<Properties> dataRetrievedInitial;

    public ModifyScoutTransactionv2() throws Exception {
        super();
    }

    //----------------------------------------------------------
    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("ModifyScout", "TransactionError");
        myRegistry.setDependencies(dependencies);
    }

    //----------------------------------------------------------
    protected Scene createView() {
        System.out.println("Creating ModifyScoutView scene...");
        View newView = ViewFactory.createView("ModifyScoutView", this);
        return new Scene(newView);
    }

    private Scene createAndShowModifyScoutView() throws Exception {
        View newView = ViewFactory.createView("ModifyScoutView", this);
        return new Scene(newView);
    }
    private Scene createAndShowScoutCollectionView() throws Exception {
        View newView = ViewFactory.createView("ScoutCollectionView", this);
        return new Scene(newView);
    }

    //----------------------------------------------------------
    public void doYourJob() {
        System.out.println("ModifyScoutTransaction.doYourJob() called");
        Scene newScene = createView();

        if (myStage.getScene() != newScene) {
            swapToView(newScene);
        }
    }

    //----------------------------------------------------------
    public Object getState(String key) {
        if (key.equals("selectedScout")) {
            return selectedScout;
        }
        else {
            return null;
        }
    }

    //----------------------------------------------------------
    public void stateChangeRequest(String key, Object value) throws Exception {
        if (key.equals("retrieveInitialScouts")) {
            ModifyScoutTransactionHelper allScouts = new ModifyScoutTransactionHelper((Properties)value);
            System.out.println("stateChangeRequest argument props: " + value);
            allScouts.retrieveInitialScouts(); // Stores retrieved scout data into dataRetrieved static variable in ModifyScoutTransaction
            Scene newScene = createAndShowScoutCollectionView();
            swapToView(newScene);
        } else if (key.equals("DoYourJob")) {
            doYourJob();
        } else if (key.equals("ScoutSelected")) { // value = String vector from ScoutCollectionView that contains only the scout ID
            Vector<String> data = (Vector<String>) value;
            String scoutId = data.get(0); // the selected ScoutId
            System.out.println("ScoutID: " + scoutId);
            try {
                selectedScout = new Scout(scoutId);
                System.out.println(selectedScout);
            } catch (Exception e) {
                System.out.println("Error loading scout with ID: " + scoutId);
                e.printStackTrace();
            }
            Scene newScene = createAndShowModifyScoutView();
            swapToView(newScene);
        } else if (key.equals("UpdateScout") == true) {
            if (selectedScout != null) {
                System.out.println("Selected scout ID:" + selectedScout.getState("ID"));
                ((Properties) value).setProperty("ScoutId", (String)selectedScout.getState("ID"));
                System.out.println("value properties " + ((Properties)value));
                selectedScout.processModifyScoutTransaction((Properties) value);
            }
        }

        myRegistry.updateSubscribers(key, this);
    }
}