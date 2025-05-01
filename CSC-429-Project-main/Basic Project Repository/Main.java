
// specify the package

// system imports

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;

// project imports
import event.Event;
import model.TreeLotCoordinator;
import userinterface.MainStageContainer;
import userinterface.WindowPosition;


/** The class containing the main program for the Tree Lot Coordinator application c */
//==============================================================
public class Main extends Application
{
    private TreeLotCoordinator myTreeLotCoordinator;		// the main behavior for the application

    /** Main frame of the application */
    private Stage mainStage;

    // start method for this class, the main application object
    //----------------------------------------------------------
    public void start(Stage primaryStage)
    {
        System.out.println("Tree Lot Coordinator Version 1.10");
        System.out.println("Copyright 2025 Alex Gookin, Trinity Thiele, Val, Arion");

        // Create the top-level container (main frame) and add contents to it.
        MainStageContainer.setStage(primaryStage, "Tree Lot Coordinator Version 1.10");
        mainStage = MainStageContainer.getInstance();

        // Finish setting up the stage (ENABLE THE GUI TO BE CLOSED U hSING THE TOP RIGHT
        // 'X' IN THE WINDOW), and show it.
        mainStage.setOnCloseRequest(new EventHandler <javafx.stage.WindowEvent>() {
            @Override
            public void handle(javafx.stage.WindowEvent event) {
                System.exit(0);
            }
        });

        try
        {
            myTreeLotCoordinator = new TreeLotCoordinator();
            // creates Tree Lot Coordinator, Tree Lot Coordinator constructor is going to create the first scene and put it on the main stage
        }
        catch(Exception exc)
        {
            System.err.println("Main.Main - could not create Tree Lot Coordinator!");
            new Event(Event.getLeafLevelClassName(this), "Main.<init>", "Unable to create Tree Lot Coordinator object", Event.ERROR);
            exc.printStackTrace();
        }
        WindowPosition.placeCenter(mainStage);
        mainStage.show(); // displays the stage
    }
    /**
     * The "main" entry point for the application. Carries out actions to
     * set up the application
     */
    // We are going to keep this function EXACTLY as is.
    //----------------------------------------------------------
    public static void main(String[] args)
    {
        launch(args); // method is in the super class Application, this method creates the stage for us
    }
}

