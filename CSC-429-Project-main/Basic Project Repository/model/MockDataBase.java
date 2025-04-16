package model;

import java.util.*;

public class MockDataBase {

    public static Vector<Properties> getSelectQueryResult(String query) {
        Vector<Properties> results = new Vector<>();

        // Create mock scouts
        Properties scout1 = new Properties();
        scout1.setProperty("ID", "24");
        scout1.setProperty("FirstName", "John");
        scout1.setProperty("LastName", "Doe");
        scout1.setProperty("MiddleName", "A");
        scout1.setProperty("DateOfBirth", "2005-04-01");
        scout1.setProperty("PhoneNumber", "555-1234");
        scout1.setProperty("Email", "john.doe@example.com");
        scout1.setProperty("TroopID", "101");
        scout1.setProperty("Status", "Active");
        scout1.setProperty("DateStatusUpdated", "2025-04-01");

        Properties scout2 = new Properties();
        scout2.setProperty("ID", "42");
        scout2.setProperty("FirstName", "Jane");
        scout2.setProperty("LastName", "Smith");
        scout2.setProperty("MiddleName", "B");
        scout2.setProperty("DateOfBirth", "2006-07-12");
        scout2.setProperty("PhoneNumber", "555-5678");
        scout2.setProperty("Email", "jane.smith@example.com");
        scout2.setProperty("TroopID", "102");
        scout2.setProperty("Status", "Inactive");
        scout2.setProperty("DateStatusUpdated", "2025-03-12");

        Properties scout3 = new Properties();
        scout3.setProperty("ID", "99");
        scout3.setProperty("FirstName", "Alex");
        scout3.setProperty("LastName", "Johnson");
        scout3.setProperty("MiddleName", "C");
        scout3.setProperty("DateOfBirth", "2004-10-25");
        scout3.setProperty("PhoneNumber", "555-9999");
        scout3.setProperty("Email", "alex.johnson@example.com");
        scout3.setProperty("TroopID", "103");
        scout3.setProperty("Status", "Active");
        scout3.setProperty("DateStatusUpdated", "2025-02-10");

        // Add all to mock DB
        List<Properties> mockScouts = Arrays.asList(scout1, scout2, scout3);

        // Extract ScoutID from the query
        String idSearch = null;
        String lowerQuery = query.toLowerCase();
        if (lowerQuery.contains("where") && lowerQuery.contains("id")) {
            int whereIndex = lowerQuery.indexOf("where");
            int idIndex = lowerQuery.indexOf("id", whereIndex);
            int equalsIndex = lowerQuery.indexOf("=", idIndex);
            int quoteStart = lowerQuery.indexOf("'", equalsIndex);
            int quoteEnd = lowerQuery.indexOf("'", quoteStart + 1);
            if (quoteStart != -1 && quoteEnd != -1) {
                idSearch = query.substring(quoteStart + 1, quoteEnd).trim();
            }
        }

        // Filter results
        for (Properties scout : mockScouts) {
            if (idSearch != null && idSearch.equals(scout.getProperty("ID"))) {
                results.add(scout);
                break; // Return only the matching scout
            }
        }

        return results;
    }


    //public static Integer updatePersistentState(Properties mySchema, Properties updateValues, Properties whereValues) {
        // Simulate a successful update
      //  System.out.println("Mock update called for ID: " + whereValues.getProperty("ID"));
        //return 1; // return 1 to indicate one row was updated
    //}


    public static Integer updatePersistentState(Properties mySchema, Properties updateValues, Properties whereValues) {
        System.out.println("Mock update called for ID: " + whereValues.getProperty("ID"));
        return 1;
    }
}
