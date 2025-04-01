package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

public class ScoutTableModel {
    private final SimpleStringProperty ScoutId;
    private final SimpleStringProperty LastName;
    private final SimpleStringProperty FirstName;
    private final SimpleStringProperty MiddleName;
    private final SimpleStringProperty DateofBirth;
    private final SimpleStringProperty PhoneNumber;
    private final SimpleStringProperty Email;
    private final SimpleStringProperty TroopID;
    private final SimpleStringProperty Status;
    private final SimpleStringProperty DateStatusUpdated;

    //
    // Assuming the database is set up like the document, might have to change indexes later
    // ----------------------------------------------------------------------------
    public ScoutTableModel(Vector<String> scoutData)
    {
        ScoutId =  new SimpleStringProperty(scoutData.elementAt(0));
        LastName =  new SimpleStringProperty(scoutData.elementAt(1));
        FirstName =  new SimpleStringProperty(scoutData.elementAt(2));
        MiddleName =  new SimpleStringProperty(scoutData.elementAt(3));
        DateofBirth = new SimpleStringProperty(scoutData.elementAt(4));
        PhoneNumber = new SimpleStringProperty(scoutData.elementAt(5));
        Email = new SimpleStringProperty(scoutData.elementAt(6));
        TroopID = new SimpleStringProperty(scoutData.elementAt(7));
        Status = new SimpleStringProperty(scoutData.elementAt(8));
        DateStatusUpdated = new SimpleStringProperty(scoutData.elementAt(9));
    }

    //----------------------------------------------------------------------------
    public String getScoutId() {
        return ScoutId.get();
    }

    public String getLastName() {
        return LastName.get();
    }

    //----------------------------------------------------------------------------
    public void setLastName(String number) {
        LastName.set(number);
    }

    //----------------------------------------------------------------------------
    public String getFirstName() {
        return FirstName.get();
    }

    //----------------------------------------------------------------------------
    public void setFirstName(String aType) {
        FirstName.set(aType);
    }

    //----------------------------------------------------------------------------
    public String getMiddleName() {
        return MiddleName.get();
    }

    //----------------------------------------------------------------------------
    public void setMiddleName(String bal) {
        MiddleName.set(bal);
    }

    //----------------------------------------------------------------------------
    public String getDateOfBirth() {
        return DateofBirth.get();
    }

    //----------------------------------------------------------------------------
    public void setDateofBirth(String charge)
    {
        DateofBirth.set(charge);
    }

    public String getPhoneNumber() {
        return PhoneNumber.get();
    }

    //----------------------------------------------------------------------------
    public void setPhoneNumber(String charge)
    {
        PhoneNumber.set(charge);
    }

    public String getEmail() {
        return Email.get();
    }

    //----------------------------------------------------------------------------
    public void setEmail(String charge)
    {
        Email.set(charge);
    }

    public String getTroopID() {
        return TroopID.get();
    }

    //----------------------------------------------------------------------------
    public void setTroopID(String charge)
    {
        TroopID.set(charge);
    }

    public String getStatus() {
        return Status.get();
    }

    //----------------------------------------------------------------------------
    public void setStatus(String charge)
    {
        Status.set(charge);
    }

    public String getDateStatusUpdated(){
        return DateStatusUpdated.get();
    }

    public void setDateStatusUpdated(String date){
        DateStatusUpdated.set(date);
    }
}
