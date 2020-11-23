package comp3415.telehealth.db;

import comp3415.telehealth.view.loginLayout;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class User {

    // Checks if the user is valid, then logs them in.
    public static boolean isLogin(String username, String password, String userType){
        try{
            Connection sqlConnection = MySQLConnections.getConnection();                                    //connecting to database
            String query = "SELECT UserID, UName, UType FROM login WHERE username = '" +                    // query to verify login credentials
                    username + "' AND password = '" + password +
                    "' AND UType = '" + userType + "'";
            PreparedStatement prepS = sqlConnection.prepareStatement(query);
            ResultSet rSet = prepS.executeQuery();                                                          // executing query


            while(rSet.next()){
                // storing user information
                LogInfo.UserID = rSet.getInt("UserID");
                LogInfo.UName = rSet.getString("UName");
                LogInfo.UType = rSet.getString("UType");
            }
            return true;
        }
        catch(Exception e){ //error while connecting to database


        }

        return false;
    }

    // Utility function to log in the user
    public static boolean logIn()
    {
        // sets isLoggedIn to true, returns true on success.
        return LogInfo.isLoggedIn = true;
    }

    // Utility function to logout the user
    public static boolean logOut()
    {
        // Sets isLoggedIn to false, returns true on success
        return LogInfo.isLoggedIn = false;

    }

    // Returns true if the user is a doctor.
    public static boolean isDoctor(){
        return (LogInfo.UType.equalsIgnoreCase("Doctor"));
    }

}