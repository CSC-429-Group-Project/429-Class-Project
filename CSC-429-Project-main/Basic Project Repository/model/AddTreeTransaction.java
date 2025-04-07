package model;

import java.util.Properties;
import java.util.Vector;
import java.util.Enumeration;
import java.sql.*;

import database.*;
import exception.InvalidPrimaryKeyException;

public class AddTreeTransaction extends EntityBase {
    private static String tableName = "transaction";
    protected Properties persistentState;
    protected Properties dependencies;
    private String updateStatusMessage = "";

    public AddTreeTransaction(String transactionID) throws InvalidPrimaryKeyException {
        super(tableName);

        String query = "SELECT * FROM " + tableName + " WHERE ID = '" + transactionID + "'";
        Vector<Properties> dataRetrieved = getSelectQueryResult(query);

        if (dataRetrieved != null) {
            int size = dataRetrieved.size();
            if (size != 1) {
                throw new InvalidPrimaryKeyException("Multiple or no transactions found for ID: " + transactionID);
            } else {
                Properties retrievedData = dataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration<?> allKeys = retrievedData.propertyNames();
                while (allKeys.hasMoreElements()) {
                    String nextKey = (String) allKeys.nextElement();
                    String nextValue = retrievedData.getProperty(nextKey);
                    if (nextValue != null) {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }
            }
        } else {
            throw new InvalidPrimaryKeyException("No transaction found for ID: " + transactionID);
        }
    }

    public AddTreeTransaction(Properties props) {
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

//    public Vector<String> getEntryListView() {
//        Vector<String> v = new Vector<>();
//        v.addElement(persistentState.getProperty("ID"));
//        v.addElement(persistentState.getProperty("SessionID"));
//        v.addElement(persistentState.getProperty("TransactionType"));
//        v.addElement(persistentState.getProperty("Barcode"));
//        v.addElement(persistentState.getProperty("TransactionAmount"));
//        v.addElement(persistentState.getProperty("PaymentMethod"));
//        v.addElement(persistentState.getProperty("CustomerName"));
//        v.addElement(persistentState.getProperty("CustomerPhone"));
//        v.addElement(persistentState.getProperty("CustomerEmail"));
//        v.addElement(persistentState.getProperty("TransactionDate"));
//        v.addElement(persistentState.getProperty("TransactionTime"));
//        v.addElement(persistentState.getProperty("DateStatusUpdated"));
//        return v;
//    }

    public void processNewTreeTransaction(Properties p) {
        persistentState = new Properties();
        persistentState.setProperty("SessionID", p.getProperty("SessionID"));
        persistentState.setProperty("CustomerName", p.getProperty("CustomerName"));
        persistentState.setProperty("CustomerPhone", p.getProperty("CustomerPhone"));
        persistentState.setProperty("CustomerEmail", p.getProperty("CustomerEmail"));
        persistentState.setProperty("PaymentMethod", p.getProperty("PaymentMethod"));
        persistentState.setProperty("TransactionAmount", p.getProperty("TransactionAmount"));
        persistentState.setProperty("TransactionType", p.getProperty("TransactionType"));
        persistentState.setProperty("TransactionDate", p.getProperty("TransactionDate"));
        persistentState.setProperty("TransactionTime", p.getProperty("TransactionTime"));
        try {
            updateStateInDatabase();
            System.out.println("Successfully added Tree Transaction to the database!");
        } catch (Exception ex) {
            System.out.println("Failed to add Tree Transaction to the database :(");
            ex.printStackTrace();
        }
    }


    public void updateStateInDatabase() {
        try {
            if (persistentState.getProperty("ID") != null) {
                Properties whereClause = new Properties();
                whereClause.setProperty("ID", persistentState.getProperty("ID"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Transaction data for ID: " + persistentState.getProperty("ID") + " updated successfully!";
            } else {
                insertPersistentState(mySchema, persistentState);
                updateStatusMessage = "New transaction added successfully!";
            }
        } catch (SQLException ex) {
            updateStatusMessage = "Error updating transaction data!";
        }
    }
    //   public void createAndShowTTView() {
//
//        Scene currentScene = (Scene)myTLC.myViews.get("TTView");
//
//        if (currentScene == null) {
//
//            View newView = new TTView(this);
//            currentScene = new Scene(newView);
//            myTLC.myViews.put("TTView", currentScene);
//        }
//        myTLC.swapToView(currentScene);
//    }


    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(tableName);
        }
    }
}
