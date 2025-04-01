// specify the package
package model;

// system imports
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


/** The class containing the AccountCollection for the ATM application */
//==============================================================
public class ScoutCollection  extends EntityBase implements IView
{
    private static final String myTableName = "Scout";

    private Vector<Scout> scouts;
    // GUI Components

    // constructor for this class
    //----------------------------------------------------------
    public ScoutCollection() {
        super(myTableName);
        scouts = new Vector<>(); // Initialize the collection
    }

    //----------------------------------------------------------------------------------
    private void addScout(Scout s)
    {
        //accounts.add(a);
        int index = findIndexToAdd(s);
        scouts.insertElementAt(s,index); // To build up a collection sorted on some key
    }

    //----------------------------------------------------------------------------------
    private int findIndexToAdd(Scout s)
    {
        //users.add(u);
        int low=0;
        int high = scouts.size()-1;
        int middle;

        while (low <=high)
        {
            middle = (low+high)/2;

            Scout midSession = scouts.elementAt(middle);

            int result = Scout.compare(s,midSession);

            if (result ==0)
            {
                return middle;
            }
            else if (result<0)
            {
                high=middle-1;
            }
            else
            {
                low=middle+1;
            }


        }
        return low;
    }


    /**
     *
     */
    //----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("Scout"))
            return scouts;
        else
        if (key.equals("ScoutList"))
            return this;
        return null;
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        myRegistry.updateSubscribers(key, this);
    }

    //----------------------------------------------------------
    public Scout retrieve(String LastName)
    {
        Scout retValue = null;
        for (int cnt = 0; cnt < scouts.size(); cnt++)
        {
            Scout nextScout = scouts.elementAt(cnt);
            String nextScoutName = (String)nextScout.getState("LastName");
            if (nextScoutName.equals(LastName) == true)
            {
                retValue = nextScout;
                return retValue; // we should say 'break;' here
            }
        }

        return retValue;
    }

    /** Called via the IView relationship */
    //----------------------------------------------------------
    public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }

    //------------------------------------------------------
    protected void createAndShowView()
    {

        Scene localScene = myViews.get("ScoutCollectionView");

        if (localScene == null)
        {
            // create our new view
            View newView = ViewFactory.createView("ScoutCollectionView", this);
            localScene = new Scene(newView);
            myViews.put("ScoutCollectionView", localScene);
        }
        // make the view visible by installing it into the frame
        swapToView(localScene);

    }

    //-----------------------------------------------------------------------------------
    protected void initializeSchema(String tableName)
    {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }
}
