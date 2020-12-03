package comp3415.telehealth;

import comp3415.telehealth.db.GlobalUser;
import comp3415.telehealth.db.LogInfo;
import comp3415.telehealth.model.PatientFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NewPatientFileController extends Controller implements Initializable {

    // Components from newpatientfile.fxml:
    @FXML TextField patientIDField;
    @FXML TextField doctorIDField;
    @FXML TextArea medicalInfoField;
    @FXML TextArea medicationField;
    @FXML Label statusText;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Redirect the user if they are not authorized:
        if (!GlobalUser.isLoggedIn()) {
            try {
                redirectToDashboard();
            } catch (IOException e) {
                System.exit(0);
            }
        }

        // Set the bottom status text to invisible until we need it
        statusText.setVisible(false);

        // Initialize views based on logged in user type:
        if (GlobalUser.isDoctor())
            initDoctorView();
        else
            initPatientView();

    }

    public void initDoctorView()
    {
        doctorIDField.setText(String.valueOf(LogInfo.uID));
    }

    public void initPatientView()
    {
        patientIDField.setDisable(true);
        patientIDField.setText(String.valueOf(LogInfo.uID));
    }

    /**
     * Click listener for back button press
     * @param e the action event that led to this method call
     */
    public void back(ActionEvent e){
        try {
            redirectToDashboard();
        }catch (IOException ioe){
            statusText.setText("Couldn't load the dashboard! Try closing and reopening the app.");
        }
    }

    /**
     * Called when the submit button is pressed.
     * @param e the action event that led to this method call
     */
    public void submitForm(ActionEvent e)
    {
        statusText.setText("Uploading your file...");
        statusText.setVisible(true);

        // Get form entries:
        String patientID_s = patientIDField.getText();
        String doctorID_s = doctorIDField.getText();
        String medicalInfo_s = medicalInfoField.getText();
        String medications_s = medicationField.getText();
        boolean verified = GlobalUser.isDoctor(); // if the user is a doctor, sets "verified" to true

        // Abort if the form input is invalid:
        if (!validateFormInput(patientID_s, doctorID_s, medicalInfo_s, medications_s)){
            statusText.setText("Invalid information entered. PLease try again.");
            return;
        }

        // Insert the file into the database:
        if (PatientFile.insertFile(Integer.parseInt(patientID_s), Integer.parseInt(doctorID_s), null, medicalInfo_s, medications_s, verified))
            statusText.setText("Uploaded successfully!");
        else
            statusText.setText("Problem uploading your information. Please try again.");

    }

    /**
     * Returns true if the form input is valid, false otherwise.
     * @param pid_s the raw string of the patient id field
     * @param did_s the raw string of the doctor id field
     * @param medInfo_s the raw string of the medical info field
     * @param meds_s the raw string of the medication field
     * @return
     */
    public boolean validateFormInput(String pid_s, String did_s, String medInfo_s, String meds_s)
    {
        // If input passes all the following tests, the function will return true:
        try{
            int i = Integer.parseInt(pid_s);
            int j = Integer.parseInt(did_s);
        }catch (Exception e){
            // Error parsing the integers
            return false;
        }
        return true;
    }

}
