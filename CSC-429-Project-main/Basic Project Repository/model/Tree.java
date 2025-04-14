package model;

import java.util.Properties;
import java.util.Vector;
import java.util.Enumeration;
import java.sql.*;

import database.*;
import exception.InvalidPrimaryKeyException;
import javafx.stage.Stage;

public class Tree extends EntityBase {
    private static String tableName = "tree";
    protected Properties persistentState;
    protected Properties dependencies;
    private String updateStatusMessage = "";

    public Tree() {
        super(tableName);
        setDependencies();
        persistentState = new Properties();
    }

    public Tree(String barcode) throws InvalidPrimaryKeyException {
        super(tableName);

        String query = "SELECT * FROM " + tableName + " WHERE Barcode = '" + barcode + "'";
        Vector<Properties> dataRetrieved = getSelectQueryResult(query);

        if (dataRetrieved != null) {
            int size = dataRetrieved.size();
            if (size != 1) {
                throw new InvalidPrimaryKeyException("Multiple trees found for barcode: " + barcode);
            } else {
                Properties retrievedTreeData = dataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration<?> allKeys = retrievedTreeData.propertyNames();
                while (allKeys.hasMoreElements()) {
                    String nextKey = (String) allKeys.nextElement();
                    String nextValue = retrievedTreeData.getProperty(nextKey);
                    if (nextValue != null) {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }
            }
        } else {
            throw new InvalidPrimaryKeyException("No tree found for barcode: " + barcode);
        }
    }

    public Tree(Properties props) {
        super(tableName);
        setDependencies();

        persistentState = new Properties();
        Enumeration<?> allKeys = props.propertyNames();
        while (allKeys.hasMoreElements()) {
            String nextKey = (String) allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);
            if (nextValue != null) {
                persistentState.setProperty(nextKey, nextValue);
            }
        }
    }

    private void setDependencies() {
        dependencies = new Properties();
        myRegistry.setDependencies(dependencies);
    }

    public Object getState(String key) {
        if (key.equals("UpdateStatusMessage")) {
            return updateStatusMessage;
        }
        return persistentState.getProperty(key);
    }

    public void stateChangeRequest(String key, Object value) {
        persistentState.setProperty(key, value.toString());
        myRegistry.updateSubscribers(key, this);
    }

    public Vector<String> getEntryListView() {
        Vector<String> v = new Vector<>();
        v.addElement(persistentState.getProperty("Barcode"));
        v.addElement(persistentState.getProperty("Tree_Type"));
        v.addElement(persistentState.getProperty("Notes"));
        v.addElement(persistentState.getProperty("Status"));
        v.addElement(persistentState.getProperty("DateStatusUpdated"));
        return v;
    }

    public void updateStateInDatabase() {
        try {
            if (persistentState.getProperty("Barcode") != null) {
                Properties whereClause = new Properties();
                whereClause.setProperty("Barcode", persistentState.getProperty("Barcode"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Tree data for Barcode: " + persistentState.getProperty("Barcode") + " updated successfully!";
            } else {
                insertPersistentState(mySchema, persistentState);
                updateStatusMessage = "New Tree added successfully!";
            }
        } catch (SQLException ex) {
            updateStatusMessage = "Error updating Tree data!";
        }
    }

    public void processNewTree(Properties p) {
        persistentState = new Properties();
        persistentState.setProperty("Tree_Type", p.getProperty("Tree_type"));
        persistentState.setProperty("Notes", p.getProperty("Notes"));
        persistentState.setProperty("Status", p.getProperty("Status"));
        persistentState.setProperty("DateStatusUpdated", p.getProperty("DateStatusUpdated"));


        try {
            updateStateInDatabase();
            System.out.println("Successfully added TreeType to the database!");
        } catch (Exception ex) {
            System.out.println("Failed to add TreeType to the database :(");
            ex.printStackTrace();
        }
    }

        public void processNewTreeTransaction(Properties p){
            persistentState = new Properties();
            persistentState.setProperty("SessionID", p.getProperty("SessionID"));
            persistentState.setProperty("TransactionType", p.getProperty("TransactionType"));
            persistentState.setProperty("Barcode", p.getProperty("Barcode"));
            persistentState.setProperty("TransactionAmount", p.getProperty("TransactionAmount"));
            persistentState.setProperty("PaymentMethod", p.getProperty("PaymentMethod"));
            persistentState.setProperty("CustomerName", p.getProperty("CustomerName"));
            persistentState.setProperty("CustomerPhone", p.getProperty("CustomerPhone"));
            persistentState.setProperty("CustomerEmail", p.getProperty("CustomerEmail"));
            persistentState.setProperty("TransactionDate", p.getProperty("TransactionDate"));
            persistentState.setProperty("TransactionTime", p.getProperty("TransactionTime"));
            persistentState.setProperty("DateStatusUpdated", p.getProperty("DateStatusUpdated"));

            try {
                updateStateInDatabase();
                System.out.println("Successfully added Tree Transaction to the database!");
            } catch (Exception ex) {
                System.out.println("Failed to add Tree Transaction to the database :(");
                ex.printStackTrace();
            }
        }

    //   public void createAndShowTreeView() {
    //
    //        Scene currentScene = (Scene)myTLC.myViews.get("TreeView");
    //
    //        if (currentScene == null) {
    //
    //            View newView = new TreeView(this);
    //            currentScene = new Scene(newView);
    //            myTLC.myViews.put("TreeView", currentScene);
    //        }
    //        myTLC.swapToView(currentScene);
    //    }

    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(tableName);
        }
    }
}
