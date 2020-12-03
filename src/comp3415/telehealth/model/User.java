package comp3415.telehealth.model;


import comp3415.telehealth.db.MySQLConnections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class User{

    private int uID;
    private String uname;
    private String pass;
    private String uType;
    private String displayName;

    /**
     * Constructor class for a User object, which represents a single row of the "users" table
     */
    public User(int userID, String userLogin, String userPassword, String userType, String userDisplayName){
        //set instance variables to the respective database attributes
        this.uID = userID; // this line should be changed to auto-increment with the database
        this.uname = userLogin;
        this.pass = userPassword;
        this.uType = userType;
        this.displayName = userDisplayName;
    }

    public User(){
        // Used to get a User object with no values set
    }


    /**
     * Function that creates a user and inserts it into the database
     */
    public static boolean register(String nameField, String userType, String username, String password)
    {
        try{
            String passwordHash = MySQLConnections.getPasswordHash(password);

            Connection sqlConnection = MySQLConnections.getConnection();      // connecting to database

            String userQuery = "INSERT INTO users(uname, pass, uType, displayName)" +
                                "VALUES(?,?,?,?)";
            PreparedStatement prepU = sqlConnection.prepareStatement(userQuery);

            prepU.setString(1, username);
            prepU.setString(2, passwordHash);
            prepU.setString(3, userType);
            prepU.setString(4, nameField);

            int resultCount = prepU.executeUpdate();
            return true;

        }
        catch(Exception e){ // error while connecting to database

            return false;
        }

    }


    /**
     * Static function that returns an array of all users in the database
     * @return ArrayList of all Users in the database
     */
    public static ArrayList<User> getAllUsers(){
        ArrayList<User> allUsers = new ArrayList<User>();
        try{
            Connection sqlConnection = MySQLConnections.getConnection(); //connecting to database
            String query = "SELECT * FROM users";
            PreparedStatement prepS = sqlConnection.prepareStatement(query);
            ResultSet rSet = prepS.executeQuery(); // executing query

            // Loops through each result row and adds a user to the array with the respective information:
            while(rSet.next()){
                allUsers.add(new User(
                        rSet.getInt("uID"),
                        rSet.getString("uname"),
                        rSet.getString("pass"),
                        rSet.getString("uType"),
                        rSet.getString("displayName")));
            }
        }
        catch(Exception e){ //error while connecting to database
        }
        return allUsers;
    }

    /**
     * Static function that returns a specific user specified by their uID
     * @param uID the id of the user to retrieve from the database
     * @return User
     */
    public static User getUser(int uID){
        User user = new User();
        try{
            Connection sqlConnection = MySQLConnections.getConnection();
            String query = "SELECT * FROM users WHERE uID = ?";
            PreparedStatement prepS = sqlConnection.prepareStatement(query);
            prepS.setInt(1, uID);
            ResultSet rSet = prepS.executeQuery();

            if(rSet.next()){
                user = new User(
                        rSet.getInt("uID"),
                        rSet.getString("uname"),
                        rSet.getString("pass"),
                        rSet.getString("uType"),
                        rSet.getString("displayName"));
            }
        }
        catch(Exception e){ //error while connecting to database
        }
        return user;
    }

    /**
     * "Getter" methods
     * @return the value of the respective variables
     */
    public int getID(){ return this.uID; }
    public String getUsername(){ return this.uname; }
    public String getPassword(){ return this.pass; }
    public String getType(){ return this.uType; }
    public String getDisplayName(){ return this.displayName; }

    /**
     * "Setter" methods
     */
    public void setID(int newID){ this.uID = newID; }
    public void setUsername(String newUsername){ this.uname = newUsername;}
    public void setPassword(String newPassword){ this.pass = newPassword;}
    public void setType(String newType){ this.uType = newType;}
    public void setDisplayName(String newName){ this.displayName = newName;}

    /**
     * Save to database: saves the local changes to the database
     * (for example, to change the password we would call setPassword(newPassword),
     *  then call this function to save the changes.)
     */
    public void saveInfo(){

        // Todo: Query database to sync local changes
        // Se


    }

}
