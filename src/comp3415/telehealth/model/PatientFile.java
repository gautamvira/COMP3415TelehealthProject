package comp3415.telehealth.model;


import comp3415.telehealth.db.MySQLConnections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Database model for the "files" table
 */
public class PatientFile{

    private int fileID;         // unique file id
    private int patientID;      // integer identifying the patient for this file
    private int doctorID;       // integer identifying the doctor for this file
    private String fileURL;     // text containing the fileURL associated with this file (currently unimplemented)
    private String medicalInfo; // text containing this file's associated medicalInfo
    private String medication; // text containing this file's associated medications
    private boolean verified; // whether this file is verified by a doctor or not

    /**
     * Constructor class for a User object, which represents a single row of the "users" table
     */
    public PatientFile(int file_id, int patient_id, int doctor_id, String file_URL, String medical_Info, String medications, boolean verified){
        //set instance variables to the respective database attributes
        this.fileID = file_id;
        this.patientID = patient_id;
        this.doctorID = doctor_id;
        this.fileURL = file_URL;
        this.medicalInfo = medical_Info;
        this.medication = medications;
        this.verified = verified;
    }

    public PatientFile(){
        // Used to get a PatientFile object with no values set
    }

    /**
     * "Getter" methods
     * @return the value of the respective variables
     */
    public int getID(){ return this.fileID; }
    public int getPatientID(){ return this.patientID; }
    public int getDoctorID(){ return this.doctorID; }
    public String getFileURL(){ return this.fileURL; }
    public String getMedicalInfo(){ return this.medicalInfo; }
    public String getMedication() { return this.medication; }
    public boolean getVerified() { return this.verified; }

    /**
     * "Setter" methods
     */
    public void setID(int newID){ this.fileID = newID; }
    public void setPatientID(int newID){ this.patientID = newID; }
    public void setDoctorID(int newID){ this.doctorID = newID; }
    public void setFileURL(String newURL){ this.fileURL = newURL; }
    public void setMedicalInfo(String newInfo){ this.medicalInfo = newInfo; }
    public void setMedication(String newMeds) { this.medication = newMeds; }
    public void setVerified(boolean newVal) { this.verified = newVal; }

    /**
     * Save to database: saves the local changes of this object to the database
     * @return true if the sync was successful
     */
    public boolean sync(){

        return updateFile(this.fileID, this.patientID, this.doctorID, this.fileURL, this.medicalInfo, this.medication, this.verified);

    }

    /**
     * Add to database: adds this object as a respective row in the "files" table
     * @return true on success
     */
    public boolean upload(){
        return insertFile(this.patientID, this.doctorID, this.fileURL, this.medicalInfo, this.medication, this.verified);
    }

    /**
     * Static function that returns an array of all files in the database
     * @return ArrayList of all PatientFiles in the database
     */
    public static ArrayList<PatientFile> getAllFiles(){
        ArrayList<PatientFile> allFiles = new ArrayList<PatientFile>();
        try{
            Connection sqlConnection = MySQLConnections.getConnection(); //connecting to database
            String query = "SELECT * FROM files";
            PreparedStatement prepS = sqlConnection.prepareStatement(query);
            ResultSet rSet = prepS.executeQuery(); // executing query

            // Loops through each result row and adds a user to the array with the respective information:
            while(rSet.next()){
                allFiles.add(new PatientFile(
                        rSet.getInt("fileID"),
                        rSet.getInt("patientID"),
                        rSet.getInt("doctorID"),
                        rSet.getString("fileURL"),
                        rSet.getString("medicalInfo"),
                        rSet.getString("medication"),
                        rSet.getBoolean("verified")));
            }
        }
        catch(Exception e){ //error while connecting to database
        }
        return allFiles;
    }

    /**
     * Static function that returns an array of all files in the database where the patientID matches
     * @return ArrayList of all PatientFiles in the database
     */
    public static ArrayList<PatientFile> getAllFiles(int patientID){
        ArrayList<PatientFile> allFiles = new ArrayList<PatientFile>();
        try{
            Connection sqlConnection = MySQLConnections.getConnection(); //connecting to database
            String query = "SELECT * FROM files WHERE patientID = ?";
            PreparedStatement prepS = sqlConnection.prepareStatement(query);
            prepS.setInt(1, patientID);
            ResultSet rSet = prepS.executeQuery(); // executing query

            // Loops through each result row and adds a user to the array with the respective information:
            while(rSet.next()){
                allFiles.add(new PatientFile(
                        rSet.getInt("fileID"),
                        rSet.getInt("patientID"),
                        rSet.getInt("doctorID"),
                        rSet.getString("fileURL"),
                        rSet.getString("medicalInfo"),
                        rSet.getString("medication"),
                        rSet.getBoolean("verified")));
            }
        }
        catch(Exception e){ //error while connecting to database
        }
        return allFiles;
    }

    /**
     * Static function that returns a specific patient file
     * @return PatientFile with values if found, empty PatientFile if no match found.
     */
    public static PatientFile getFile(int fileID){
        PatientFile file = new PatientFile();
        try{
            Connection sqlConnection = MySQLConnections.getConnection(); //connecting to database
            String query = "SELECT * FROM files WHERE fileID = ?";
            PreparedStatement prepS = sqlConnection.prepareStatement(query);
            prepS.setInt(1, fileID);
            ResultSet rSet = prepS.executeQuery(); // executing query

            // Loops through each result row and adds a user to the array with the respective information:
            if(rSet.next()){
                file = new PatientFile(
                        rSet.getInt("fileID"),
                        rSet.getInt("patientID"),
                        rSet.getInt("doctorID"),
                        rSet.getString("fileURL"),
                        rSet.getString("medicalInfo"),
                        rSet.getString("medication"),
                        rSet.getBoolean("verified"));
            }
        }
        catch(Exception e){ //error while connecting to database
        }
        return file;
    }

    /**
     * Static function that allows insertion of a new patient file into the database
     * to-do: include fileURL atrribute for image or file upload
     * @returns true on success
     */
    public static boolean insertFile(int patientID, int doctorID, String fileURL, String medicalInfo, String medication, boolean verified)
    {
        try{
            Connection sqlConnection = MySQLConnections.getConnection(); //connecting to database
            String query = "INSERT INTO files (patientID, doctorID, fileURL, medicalInfo, medication, verified)" +
                            "VALUES (?, ?, ?, ?, ?, ?)"; // question marks will be replace by the following:
            PreparedStatement prepS = sqlConnection.prepareStatement(query);
            prepS.setInt(1, patientID); // sets 1st ? in query to patientID
            prepS.setInt(2, doctorID); // similarly...
            prepS.setString(3, fileURL);
            prepS.setString(4, medicalInfo);
            prepS.setString(5, medication);
            prepS.setBoolean(6, verified);
            int resultCount = prepS.executeUpdate(); // executing the prepared query
            return true;
        }catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Static function that allows updating of a row in the "files" table in the database
     * to-do: include fileURL atrribute for image or file upload
     * @returns true on success
     */
    public static boolean updateFile(int fileID, int patientID, int doctorID, String fileURL, String medicalInfo, String medication, boolean verified)
    {
        try{
            Connection sqlConnection = MySQLConnections.getConnection(); //connecting to database
            // be careful changing this: can update *all* rows if "WHERE" clause is omitted or incorrect
            String query = "UPDATE files " +
                            "SET patientID = ?, doctorID = ?, fileURL = ?, medicalInfo = ?, medication = ?, verified = ? " +
                            "WHERE fileID = ?"; // question marks will be replace by the following:
            PreparedStatement prepS = sqlConnection.prepareStatement(query);
            prepS.setInt(1, patientID);
            prepS.setInt(2, doctorID);
            prepS.setString(3, fileURL);
            prepS.setString(4, medicalInfo);
            prepS.setString(5, medication);
            prepS.setBoolean(6, verified);
            prepS.setInt(7, fileID);
            int resultCount = prepS.executeUpdate(); // executing the prepared query
            return true;
        }catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



}
