package model;

import java.sql.SQLException;
import java.util.Properties;
import java.time.LocalDate;

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

        Properties updateValues = new Properties();
        updateValues.setProperty("Status", "Inactive");
        updateValues.setProperty("DateStatusUpdated", LocalDate.now().toString());

        try {
            Integer result = updatePersistentState(mySchema, updateValues, whereValues);
            if (result != null && result == 0) {
                updateStatusMessage = "No scout found with ID: " + scoutID;
                System.out.println(updateStatusMessage);
            } else {
                updateStatusMessage = "Scout with ID: " + scoutID + " deactivated successfully!";
                System.out.println(updateStatusMessage);
            }
        } catch (SQLException ex) {
            updateStatusMessage = "Error deactivating scout with ID: " + scoutID;
            System.out.println(updateStatusMessage);
            ex.printStackTrace();
            new Event(Event.getLeafLevelClassName(this), "processRemoveScoutTransaction", "SQLException: " + ex.getMessage(), Event.ERROR);
            throw new Exception("Failed to deactivate scout.");
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
