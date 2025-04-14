// specify the package
package model;

// system imports
import java.util.Vector;
import javax.swing.JFrame;

// project imports

/** The class containing the TransactionFactory for the Tree Lot Coordinator application */
//==============================================================
public class TransactionFactory
{

    /**
     *
     */
    //----------------------------------------------------------
    /**public static Transaction createTransaction(String transType,
                                                AccountHolder cust)
            throws Exception
    {
        Transaction retValue = null;

        if (transType.equals("Deposit"))
        {
            retValue = new AddScoutTransaction(cust);
        }
        else
        if (transType.equals("Withdraw"))
        {
            retValue = new ModifyScoutTransaction(cust);
        }
        else
        if (transType.equals("Transfer"))
        {
            retValue = new RemoveScoutTransaction(cust);
        }

        return retValue;
    }
     **/
}
