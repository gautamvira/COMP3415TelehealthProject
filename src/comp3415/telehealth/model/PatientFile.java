package comp3415.telehealth.model;


import comp3415.telehealth.db.MySQLConnections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class PatientFile{

    private int fileID;
    private int patientID;
    private int doctorID;
    private String fileURL;
    private String medicalInfo;
    private String medication;

    /**
     * Constructor class for a User object, which represents a single row of the "users" table
     */
    public PatientFile(int file_id, int patient_id, int doctor_id, String file_URL, String medical_Info, String medications){
        //set instance variables to the respective database attributes
        this.fileID = file_id;
        this.patientID = patient_id;
        this.doctorID = doctor_id;
        this.fileURL = file_URL;
        this.medicalInfo = medical_Info;
        this.medication = medications;
    }

    public PatientFile(){
        // Used to get a PatientFile object with no values set
    }

    /**
     * Static function that returns an array of all users in the database
     * @return ArrayList of all Users in the database
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
                        rSet.getString("medication")));
            }
        }
        catch(Exception e){ //error while connecting to database
        }
        return allFiles;
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

    /**
     * "Setter" methods
     */
    public void setID(int newID){ this.fileID = newID; }
    public void setPatientID(int newID){ this.patientID = newID; }
    public void setDoctorID(int newID){ this.doctorID = newID; }
    public void setFileURL(String newURL){ this.fileURL = newURL; }
    public void setMedicalInfo(String newInfo){ this.medicalInfo = newInfo; }
    public void setMedication(String newMeds) { this.medication = newMeds; }

    /**
     * Save to database: saves the local changes to the database
     */
    public void saveInfo(){

        // Todo: Query database to sync local changes
        // Se


    }

}