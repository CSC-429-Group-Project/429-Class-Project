package model;

import java.sql.SQLException;
import java.util.Properties;
import java.time.LocalDate;

import event.Event;
import impresario.IView;
import javafx.scene.Scene;
import userinterface.SearchSelectScoutView;
import userinterface.View;

public class RemoveScoutTransaction extends EntityBase implements IView {

    private static final String myTableName = "scout";
    private String updateStatusMessage = "";

    private Scout currentScout;
    private ScoutCollection scoutList;

    public static final boolean USE_MOCK_DB = EntityBase.useMockDatabase;

    public RemoveScoutTransaction() {
        super(myTableName);
    }

    public void processRemoveScoutTransaction(String scoutID) throws Exception {
        if (scoutID == null || scoutID.isEmpty()) {
            new Event(Event.getLeafLevelClassName(this), "processRemoveScoutTransaction", "Missing scout ID", Event.ERROR);
            throw new Exception("Scout ID is required to remove a scout.");
        }

        // Retrieve scout before attempting to update
        ScoutCollection scoutCollection = new ScoutCollection();
        Properties scoutData = scoutCollection.findScoutIdRemove(scoutID);

        if (scoutData == null) {
            updateStatusMessage = "No scout found with ID: " + scoutID;
            System.out.println(updateStatusMessage);
            return;
        }

        // Save reference to current scout
        currentScout = new Scout(scoutData);

        Properties whereValues = new Properties();
        whereValues.setProperty("ID", scoutID);

        Properties updateValues = new Properties();
        updateValues.setProperty("Status", "Inactive");
        updateValues.setProperty("DateStatusUpdated", LocalDate.now().toString());

        try {
            Integer result = USE_MOCK_DB?
                    MockDataBase.updatePersistentState(mySchema, updateValues, whereValues):
                    updatePersistentState(mySchema, updateValues, whereValues);
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

    @Override
    public Object getState(String key) {
        if (key.equals("UpdateStatusMessage")) {
            return updateStatusMessage;
        } else if (key.equals("Scout")) {
            return currentScout;
        } else if (key.equals("ScoutList"))
            return scoutList;
        return null;
    }

    @Override
    public void stateChangeRequest(String key, Object value) throws Exception {
        if (key.equals("Search")) {
            String id = (String) value;

            ScoutCollection scoutCollection = new ScoutCollection();
            Properties scoutData = scoutCollection.findScoutIdRemove(id);

            if (scoutData != null) {
                currentScout = new Scout(scoutData);

                ScoutCollection resultCollection = new ScoutCollection();
                resultCollection.addScout(currentScout);
                scoutList = resultCollection; // 👈 so getState("ScoutList") works

                stateChangeRequest("ScoutList", resultCollection);
                createAndShowSearchSelectScoutView();
            } else {
                updateStatusMessage = "No scout found with ID: " + id;
                scoutList = null;
                stateChangeRequest("ScoutList", null);
                createAndShowSearchSelectScoutView();
            }

        }
    }

    private void createAndShowSearchSelectScoutView() {
        View view = new SearchSelectScoutView(this);
        Scene currentScene = new Scene(view);
        swapToView(currentScene);
    }

    public void updateState(String key, Object value) throws Exception {
        stateChangeRequest(key, value);
    }

    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(tableName);
            if (mySchema == null) {
                System.err.println("ScoutCollection.initializeSchema - Could not initialize schema for table: " + tableName);
            }
        }
    }
}
