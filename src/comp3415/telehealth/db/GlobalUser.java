package comp3415.telehealth.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GlobalUser {

    // set the global loginUser info when the user logs in
    public static boolean setLogInfo(int uID, String uname, String uType, String displayName, boolean loggedIn)
    {
        LogInfo.uID = uID;
        LogInfo.uname = uname;
        LogInfo.uType = uType;
        LogInfo.displayName = displayName;
        LogInfo.isLoggedIn = loggedIn;
        return true;
    }

    // Checks if the user is valid, then logs them in.
    public static boolean loginUser(String username, String password, String userType) throws Exception
    {

        String passwordHash = MySQLConnections.getPasswordHash(password);
        System.out.println(passwordHash);
        Connection sqlConnection = MySQLConnections.getConnection(); //connecting to database

        String query = "SELECT uID, uname, uType, displayName FROM users WHERE uname = ? AND pass = ? AND uType = ?";
        PreparedStatement prepS = sqlConnection.prepareStatement(query);
        prepS.setString(1, username);
        prepS.setString(2, passwordHash);
        prepS.setString(3, userType);

        ResultSet rSet = prepS.executeQuery(); // executing query

        if(rSet.next()){
            // storing global user information
            int uID = rSet.getInt("uID");
            String uname = rSet.getString("uname");
            String uType = rSet.getString("uType");
            String displayName = rSet.getString("displayName");
            setLogInfo(uID, uname, uType, displayName, true);
            return true;
        }


        return false;
    }

    // Utility function to log in the user
    public static boolean setLogInInfo()
    {
        // sets isLoggedIn to true, returns true on success.
        return LogInfo.isLoggedIn = true;
    }

    // Utility function to logout the user
    public static boolean setLogOutInfo()
    {
        // Sets isLoggedIn to false, returns true on success
        return LogInfo.isLoggedIn = false;

    }

    // Returns true if the user is logged in
    public static boolean isLoggedIn(){
        return (LogInfo.isLoggedIn);
    }

    // Returns true if the user is a doctor.
    public static boolean isDoctor(){
        return (LogInfo.uType.equalsIgnoreCase("Doctor"));
    }

}
