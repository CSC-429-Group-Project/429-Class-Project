// specify the package
package model;

// system imports
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;
import javafx.scene.Scene;

// project imports
import exception.InvalidPrimaryKeyException;
import event.Event;
import database.*;

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;

public class ScoutCollection  extends EntityBase implements IView {
    private static final String myTableName = "scout";

    private Vector<Scout> scouts;
    // GUI Components

    // constructor for this class
    //----------------------------------------------------------
    public ScoutCollection() {
        super(myTableName);
        scouts = new Vector<>(); // Initialize the collection
    }

    //----------------------------------------------------------------------------------
    private void addScout(Scout s) {
        int index = findIndexToAdd(s);
        scouts.insertElementAt(s, index); // To build up a collection sorted on some key
        System.out.println("DEBUG: Added scout, total scouts = " + scouts.size());
    }

    public Vector<Scout> findScouts(Properties searchCriteria) throws SQLException {
        String query = buildSelectQuery(searchCriteria);
        Vector<Properties> results = getSelectQueryResult(query);

        scouts.clear();
        System.out.println("DEBUG: Number of rows returned = " + results.size());
        for (Properties result : results) {
            Scout scout = new Scout(result);
            addScout(scout);
        }
        return scouts;
    }

    public ScoutCollection(String scoutId) throws Exception {
        super("Scout"); // If needed
        findScoutsById(scoutId); // <- implement this method
    }

    private void findScoutsById(String scoutId) throws Exception {
        Properties props = new Properties();
        props.setProperty("ID", scoutId);
        System.out.println("DEBUG: scoutId = " + scoutId);

        String query = buildSelectQuery(props);
        System.out.println("DEBUG: query = " + query);

        Vector allData = getSelectQueryResult(query);
        System.out.println("DEBUG: Number of rows returned = " + allData.size());

        if (allData.size() > 0) {
            for (int i = 0; i < allData.size(); i++) {
                Properties p = (Properties) allData.get(i);
                Scout scout = new Scout(p);
                addScout(scout);
            }
        } else {
            System.err.println("No scouts found for ID: " + scoutId);
        }
    }

    public Properties findScoutIdRemove(String scoutId) throws Exception {
        Properties props = new Properties();
        props.setProperty("ID", scoutId);
        System.out.println("DEBUG: scoutId = " + scoutId);

        // Construct query
        String query = buildSelectQuery(props);
        System.out.println("DEBUG: query = " + query);

        // Execute the query
        Vector allData = getSelectQueryResult(query);

        // Check if data was returned
        System.out.println("DEBUG: Number of rows returned = " + allData.size());

        if (allData != null && allData.size() == 1) {
            Properties scoutData = (Properties) allData.get(0);
            Scout scout = new Scout(scoutData);
            addScout(scout); // Ensure the scout is added to the collection
            return scoutData;
        } else if (allData.size() > 1) {
            System.err.println("WARNING: Multiple scouts found with ID: " + scoutId);
            // For now, returning the first scout's data
            Properties scoutData = (Properties) allData.get(0);
            Scout scout = new Scout(scoutData);
            addScout(scout); // Ensure the scout is added to the collection
            return scoutData;
        } else {
            System.err.println("No scout found with ID: " + scoutId);
            return null;
        }
    }


    //-----------------------------------------------------------------------------------
    private String buildSelectQuery(Properties searchCriteria) {
        StringBuilder query = new StringBuilder("SELECT * FROM " + myTableName + " WHERE ");
        boolean first = true;
        for (String key : searchCriteria.stringPropertyNames()) {
            if (!first) {
                query.append(" AND ");
            }
            query.append(key).append(" = '").append(searchCriteria.getProperty(key)).append("'");
            first = false;
        }
        return query.toString();
    }

    //----------------------------------------------------------------------------------
    private int findIndexToAdd(Scout s) {
        int low = 0;
        int high = scouts.size() - 1;
        int middle;

        while (low <= high) {
            middle = (low + high) / 2;

            Scout midSession = scouts.elementAt(middle);
            int result = Scout.compare(s, midSession);

            if (result == 0) {
                return middle;
            } else if (result < 0) {
                high = middle - 1;
            } else {
                low = middle + 1;
            }
        }
        return low;
    }

    public Object getState(String key) {
        if (key.equals("Scout"))
            return scouts;
        else if (key.equals("ScoutList"))
            return this;
        return null;
    }

    public void stateChangeRequest(String key, Object value) {
        myRegistry.updateSubscribers(key, this);
    }

    public Scout retrieve(String LastName) {
        Scout retValue = null;
        for (int cnt = 0; cnt < scouts.size(); cnt++) {
            Scout nextScout = scouts.elementAt(cnt);
            String nextScoutName = (String) nextScout.getState("LastName");
            if (nextScoutName.equals(LastName)) {
                retValue = nextScout;
                return retValue;
            }
        }
        return retValue;
    }

    /** Called via the IView relationship */
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }

    protected void createAndShowView() {
        Scene localScene = myViews.get("ScoutCollectionView");

        if (localScene == null) {
            // create our new view
            View newView = ViewFactory.createView("ScoutCollectionView", this);
            localScene = new Scene(newView);
            myViews.put("ScoutCollectionView", localScene);
        }
        // make the view visible by installing it into the frame
        swapToView(localScene);
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
