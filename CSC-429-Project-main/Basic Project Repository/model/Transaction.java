package model;

// system imports
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javafx.stage.Stage;
import javafx.scene.Scene;

// project imports
import event.Event;
import impresario.*;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.WindowPosition;

/**
 * The abstract base class for all transactions in the TLC system.
 * Handles common view management, registry subscription, and dependency setup.
 */
abstract public class Transaction implements IView, IModel
{
    // For Impresario event handling and state updates
    protected Properties dependencies;
    protected ModelRegistry myRegistry;

    // GUI components
    protected Stage myStage;
    protected Hashtable<String, Scene> myViews;

    //----------------------------------------------------------
    protected Transaction() throws Exception {
        myStage = MainStageContainer.getInstance();
        myViews = new Hashtable<>();

        myRegistry = new ModelRegistry("Transaction");
        if(myRegistry == null) {
            new Event(Event.getLeafLevelClassName(this), "Transaction",
                    "Could not instantiate Registry", Event.ERROR);
        }

        setDependencies();
    }

    //----------------------------------------------------------
    // Each transaction defines its own dependencies
    protected abstract void setDependencies();

    //----------------------------------------------------------
    // Each transaction builds its own view
    protected abstract Scene createView();

    //----------------------------------------------------------
    // Optional override — if the transaction has any job to perform before/after showing the view
    public void doYourJob() {
        Scene newScene = createView();
        swapToView(newScene);
    }

    //----------------------------------------------------------
    // Retrieve internal state
    public abstract Object getState(String key);

    //----------------------------------------------------------
    // Respond to view or model requests
    public abstract void stateChangeRequest(String key, Object value);

    //----------------------------------------------------------
    // View call to update model
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }

    //----------------------------------------------------------
    public void subscribe(String key, IView subscriber) {
        myRegistry.subscribe(key, subscriber);
    }

    //----------------------------------------------------------
    public void unSubscribe(String key, IView subscriber) {
        myRegistry.unSubscribe(key, subscriber);
    }

    //----------------------------------------------------------
    // Swap view into the main stage
    public void swapToView(Scene newScene) {
        if (newScene == null) {
            System.out.println("Transaction.swapToView(): Missing view for display");
            new Event(Event.getLeafLevelClassName(this), "swapToView",
                    "Missing view for display", Event.ERROR);
            return;
        }

        myStage.setScene(newScene);
        myStage.sizeToScene();
        WindowPosition.placeCenter(myStage);
    }
}