package model;

import java.util.Properties;
import java.util.Vector;
import java.util.Enumeration;
import java.sql.*;

import database.*;
import exception.InvalidPrimaryKeyException;

public class AddScoutTransaction extends EntityBase {
    private static String tableName = "transaction";
    protected Properties persistentState;
    protected Properties dependencies;
    private String updateStatusMessage = "";

    public AddScoutTransaction(String transactionID) throws InvalidPrimaryKeyException {
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

    public AddScoutTransaction(Properties props) {
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
//        v.addElement(persistentState.getProperty("scoutId"));
//        v.addElement(persistentState.getProperty("LastName"));
//        v.addElement(persistentState.getProperty("FirstName"));
//        v.addElement(persistentState.getProperty("MiddleName"));
//        v.addElement(persistentState.getProperty("DateOfBirth"));
//        v.addElement(persistentState.getProperty("PhoneNumber"));
//        v.addElement(persistentState.getProperty("Email"));
//        v.addElement(persistentState.getProperty("TroopID"));
//        v.addElement(persistentState.getProperty("Status"));
//        v.addElement(persistentState.getProperty("DateStatusUpdated"));
//        return v;
//    }

    public void processNewScoutTransaction(Properties p) {
        persistentState = new Properties();
        persistentState.setProperty("ScoutID", p.getProperty("scoutID"));
        persistentState.setProperty("LastName", p.getProperty("LastName"));
        persistentState.setProperty("FirstName", p.getProperty("FirstName"));
        persistentState.setProperty("MiddleName", p.getProperty("MiddleName"));
        persistentState.setProperty("DateOfBirth", p.getProperty("DateOfBirth"));
        persistentState.setProperty("PhoneNumber", p.getProperty("PhoneNumber"));
        persistentState.setProperty("Email", p.getProperty("Email"));
        persistentState.setProperty("TroopID", p.getProperty("TroopID"));
        persistentState.setProperty("Status", p.getProperty("Status"));
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

    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(tableName);
        }
    }
}
