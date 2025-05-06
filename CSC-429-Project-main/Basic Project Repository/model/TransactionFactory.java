package model;

// system imports
// none needed unless you pass in data (like a selected Scout, Tree, etc.)

/** Factory class to create the appropriate transaction object */
public class TransactionFactory {

    public static Transaction createTransaction(String transType) throws Exception {
        Transaction retValue = null;

        switch (transType) {
            case "AddScoutTransaction":
                retValue = new AddScoutTransaction();
                break;
            case "ModifyTreeTransaction":
                retValue = new UpdateTreeTransaction();
                break;
            case "ModifyScoutTransaction":
                retValue = new ModifyScoutTransaction();
                break;
            case "AddTreeTypeTransaction":
                retValue = new AddTreeTypeTransaction();
                break;
            case "RemoveTree":
                retValue = new RemoveTreeTransaction();
                break;

            // Add more cases as needed

            default:
                throw new IllegalArgumentException("Unknown transaction type: " + transType);
        }

        return retValue;
    }
}

