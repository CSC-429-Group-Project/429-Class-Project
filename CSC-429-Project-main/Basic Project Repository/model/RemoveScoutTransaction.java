package model;

import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JFrame;

import exception.InvalidPrimaryKeyException;
import event.Event;
import database.*;

public class RemoveScoutTransaction extends EntityBase {

    private static final String myTableName = "Scout";
    private String updateStatusMessage = "";

    public RemoveScoutTransaction() {
        super(myTableName);
    }

    public void processRemoveScoutTransaction(String scoutID) throws Exception {
        if (scoutID == null || scoutID.isEmpty()) {
            new Event(Event.getLeafLevelClassName(this), "processRemoveScoutTransaction", "Missing scout ID", Event.ERROR);
            throw new Exception("Scout ID is required to remove a scout.");
        }

        Properties whereValues = new Properties();
        whereValues.setProperty("ScoutID", scoutID);

        try {
            Integer result = updatePersistentState(mySchema, new Properties(), whereValues);
            if (result != null && result == 0) {
                updateStatusMessage = "No scout found with ID: " + scoutID;
                System.out.println(updateStatusMessage);
            } else {
                updateStatusMessage = "Scout with ID: " + scoutID + " removed successfully!";
                System.out.println(updateStatusMessage);
            }
        } catch (SQLException ex) {
            updateStatusMessage = "Error removing scout with ID: " + scoutID;
            System.out.println(updateStatusMessage);
            ex.printStackTrace();
            new Event(Event.getLeafLevelClassName(this), "processRemoveScoutTransaction", "SQLException: " + ex.getMessage(), Event.ERROR);
            throw new Exception("Failed to remove scout from the database.");
        }
    }

    public Object getState(String key) {
        if (key.equals("UpdateStatusMessage")) {
            return updateStatusMessage;
        }
        return null;
    }

    public void stateChangeRequest(String key, Object value) {
        // This method can be used to handle state changes if needed
    }

    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }

    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(tableName);
        }
    }
}
