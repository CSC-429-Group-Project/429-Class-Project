package userinterface;

import impresario.IModel;

//==============================================================================
public class ViewFactory {

	public static View createView(String viewName, IModel model)
	{
        switch (viewName) {
            case "TransactionChoiceView":
                return new TransactionChoiceView(model);

            case "AddScoutView":
                return new AddScoutView(model);

            case "ModifyScoutView":
                return new ModifyScoutView(model);

            case "RemoveScoutView":
                return new RemoveScoutView(model);
            case "ScoutCollectionView":
                return new ScoutCollectionView(model);
            default:
                return null;
        }
	}


	/*
	public static Vector createVectorView(String viewName, IModel model)
	{
		if(viewName.equals("SOME VIEW NAME") == true)
		{
			//return [A NEW VECTOR VIEW OF THAT NAME TYPE]
		}
		else
			return null;
	}
	*/

}
