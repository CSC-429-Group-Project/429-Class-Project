package model;

import java.util.Properties;
import java.util.Vector;
import java.util.Enumeration;
import java.sql.*;

import database.*;
import exception.InvalidPrimaryKeyException;

public class ModifyScoutTransaction extends EntityBase {
    private static String tableName = "scout";
    protected Properties persistentState;
    protected Properties dependencies;
    private String updateStatusMessage = "";
    private static Vector<Properties> dataRetrieved;
    private Vector<Properties> dataRetrievedInitial;

    public ModifyScoutTransaction(String transactionID) throws InvalidPrimaryKeyException {
        super(tableName);

        String query = "SELECT * FROM " + tableName + " WHERE ID = '" + transactionID + "';";
        System.out.println(query);
        Vector<Properties> initialData = getSelectQueryResult(query);
        // dataRetrieved = getSelectQueryResult(query);
        System.out.println("initialData: " + initialData);

        if (initialData != null) {
            int size = initialData.size();
            if (size != 1) {
                System.out.println(initialData.size());
                throw new InvalidPrimaryKeyException("Multiple or no transactions found for ID: " + transactionID);
            } else {
                Properties initialData2 = initialData.elementAt(0);
                persistentState = new Properties();

                Enumeration<?> allKeys = initialData2.propertyNames();
                while (allKeys.hasMoreElements()) {
                    String nextKey = (String) allKeys.nextElement();
                    String nextValue = initialData2.getProperty(nextKey);
                    if (nextValue != null) {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }
            }
        } else {
            throw new InvalidPrimaryKeyException("No transaction found for ID: " + transactionID);
        }
    }

    public ModifyScoutTransaction(Properties props) {
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
        } else if (key.equals("getRetrievedData")) {
            System.out.println(dataRetrieved);
            return dataRetrieved;
        } else if (key.equals("ScoutList")) {
            return dataRetrieved;
        }
        return persistentState.getProperty(key);
    }

    public void retrieveInitialScouts() {
        Properties props = new Properties();
        String variablesInTable = "";

        if (!this.getState("FirstName").equals("")) {
            props.setProperty("FirstName", (String)this.getState("FirstName"));
            variablesInTable = "(FirstName LIKE '%" + this.getState("FirstName") + "%' OR FirstName IS NULL)";
        }
        if (!this.getState("MiddleName").equals("")) {
            props.setProperty("MiddleName", (String)this.getState("MiddleName"));
            if (variablesInTable.isEmpty()) {
                variablesInTable = "(MiddleName LIKE '%" + this.getState("MiddleName") + "%' OR MiddleName IS NULL)";
            } else {
                variablesInTable += " AND (MiddleName LIKE '%" + this.getState("MiddleName") + "%' OR MiddleName IS NULL)";
            }
        }
        if (!this.getState("LastName").equals("")) {
            props.setProperty("LastName", (String)this.getState("LastName"));
            if (variablesInTable.isEmpty()) {
                variablesInTable = "(LastName LIKE '%" + this.getState("LastName") + "%' OR LastName IS NULL)";
            } else {
                variablesInTable += " AND (LastName LIKE '%" + this.getState("LastName") + "%' OR LastName IS NULL)";
            }
        }
        if (!this.getState("DateOfBirth").equals("")) {
            props.setProperty("DateOfBirth", (String)this.getState("DateOfBirth"));
            if (variablesInTable.isEmpty()) {
                variablesInTable = "(DateOfBirth LIKE '%" + this.getState("DateOfBirth") + "%' OR DateOfBirth IS NULL)";
            } else {
                variablesInTable += " AND (DateOfBirth LIKE '%" + this.getState("DateOfBirth") + "%' OR DateOfBirth IS NULL)";
            }
        }
        if (!this.getState("PhoneNumber").equals("")) {
            props.setProperty("PhoneNumber", (String)this.getState("PhoneNumber"));
            if (variablesInTable.isEmpty()) {
                variablesInTable = "(PhoneNumber LIKE '%" + this.getState("PhoneNumber") + "%' OR PhoneNumber IS NULL)";
            } else {
                variablesInTable += " AND (PhoneNumber LIKE '%" + this.getState("PhoneNumber") + "%' OR PhoneNumber IS NULL)";
            }
        }
        if (!this.getState("Email").equals("")) {
            props.setProperty("Email", (String)this.getState("Email"));
            if (variablesInTable.isEmpty()) {
                variablesInTable = "(Email LIKE '%" + this.getState("Email") + "%' OR Email IS NULL)";
            } else {
                variablesInTable += " AND (Email LIKE '%" + this.getState("Email") + "%' OR Email IS NULL)";
            }
        }
        if (!this.getState("TroopID").equals("")) {
            props.setProperty("TroopID", (String)this.getState("TroopID"));
            if (variablesInTable.isEmpty()) {
                variablesInTable = "(TroopID LIKE '%" + this.getState("TroopID") + "%' OR TroopID IS NULL)";
            } else {
                variablesInTable += " AND (TroopID LIKE '%" + this.getState("TroopID") + "%' OR TroopID IS NULL)";
            }
        }
        String query = "SELECT * FROM scout WHERE " + variablesInTable + ";";

        System.out.println("Query: " + query);

        dataRetrieved = getSelectQueryResult(query); // Retrieve scout info

        System.out.println("Data Retrieved: " + dataRetrieved.toString());
    }

    public void stateChangeRequest(String key, Object value) {
        // Make this read given values from value String array
        if (key.equals("retrieveScout")) {
        }
        
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

    public void processModifyScoutTransaction(Properties p) {
        persistentState = new Properties();


        // Get all scout info from ID in p argument and set persistentState to the retrieved info

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
            System.out.println("Successfully modified Scout in the database!");
        } catch (Exception ex) {
            System.out.println("Failed to modify Scout in the database :(");
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