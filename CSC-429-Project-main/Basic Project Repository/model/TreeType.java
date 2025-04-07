package model;

import java.util.Properties;
import java.util.Vector;
import java.util.Enumeration;
import java.sql.*;

import database.*;
import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;
import userinterface.View;

public class TreeType extends EntityBase {
    private static String tableName = "tree_type";
    protected Properties persistentState;
    protected Properties dependencies;
    private String updateStatusMessage = "";

    public TreeType(String typeId) throws InvalidPrimaryKeyException {
        super(tableName);

        String query = "SELECT * FROM " + tableName + " WHERE (ID = " + typeId + ")";
        Vector<Properties> dataRetrieved = getSelectQueryResult(query);

        if (dataRetrieved != null) {
            int size = dataRetrieved.size();

            if (size != 1) throw new InvalidPrimaryKeyException("Tree Type already exists");
            else {
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
            throw new InvalidPrimaryKeyException("No tree type found with given ID");
        }
    }

    public TreeType(Properties props) {
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
        if (key.equals("UpdateStatusMessage")) return updateStatusMessage;
        return persistentState.getProperty(key);
    }

    public void stateChangeRequest(String key, Object value) {
        myRegistry.updateSubscribers(key, this);
    }

    public Vector<String> getEntryListView() {
        Vector<String> v = new Vector<>();
        v.addElement(persistentState.getProperty("ID"));
        v.addElement(persistentState.getProperty("Type_Description"));
        v.addElement(persistentState.getProperty("Cost"));
        v.addElement(persistentState.getProperty("BarcodePrefix"));
        return v;
    }

    public void updateStateInDatabase() {
        try {
            if (persistentState.getProperty("ID") != null) {
                Properties whereClause = new Properties();
                whereClause.setProperty("ID", persistentState.getProperty("ID"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "TreeType data for ID: " + persistentState.getProperty("ID") + " updated successfully!";
            } else {
                Integer typeId = insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("ID", "" + typeId.intValue());
                updateStatusMessage = "New TreeType " + persistentState.getProperty("ID") + " added successfully!";
            }
        } catch (SQLException ex) {
            updateStatusMessage = "Error updating TreeType data in database!";
        }
    }

    public void processNewTreeType(Properties p) {
        persistentState = new Properties();
        persistentState.setProperty("Type_Description", p.getProperty("Type_Description"));
        persistentState.setProperty("Cost", p.getProperty("Cost"));
        persistentState.setProperty("BarcodePrefix", p.getProperty("BarcodePrefix"));

        try {
            updateStateInDatabase();
            System.out.println("Successfully added TreeType to the database!");
        } catch (Exception ex) {
            System.out.println("Failed to add TreeType to the database :(");
            ex.printStackTrace();
        }
    }

//    public void createAndShowTreeTypeView() {
//
//        Scene currentScene = (Scene)myTLC.myViews.get("TreeTypeView");
//
//        if (currentScene == null) {
//
//            View newView = new TreeTypeView(this);
//            currentScene = new Scene(newView);
//            myTLC.myViews.put("TreeTypeView", currentScene);
//        }
//        myTLC.swapToView(currentScene);
//    }


    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(tableName);
        }
    }
}

