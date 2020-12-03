package comp3415.telehealth;

import comp3415.telehealth.db.GlobalUser;
import comp3415.telehealth.db.LogInfo;
import comp3415.telehealth.model.PatientFile;
import comp3415.telehealth.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for viewpatientfile.fxml
 */
public class ViewPatientFileController extends Controller implements Initializable {

    @FXML Label patientIdLabel;
    @FXML Label doctorIdLabel;
    @FXML Label bottomLabel;
    @FXML Button backButton;
    @FXML TextField patientIdField;
    @FXML TextField doctorIdField;
    @FXML TextArea medicalInfoText;
    @FXML TextArea medicationText;
    @FXML CheckBox verifiedCheckbox;

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

        // Authorize the user:
        if (!GlobalUser.isLoggedIn()) {
            try {
                redirectToLogin();
            } catch (IOException e) {
                System.exit(0);
            }
        }

        bottomLabel.setText("");
        patientIdField.setText("");
        doctorIdField.setText("");
        medicalInfoText.setText("");
        medicationText.setText("");
        // disables the checkbox without greying it out:
        verifiedCheckbox.setMouseTransparent(true);
        verifiedCheckbox.setFocusTraversable(false);

        if (GlobalUser.isDoctor())
            initDoctorView();
        else
            initPatientView();

    }

    /**
     * Initializes the view when the logged in user is a doctor
     */
    public void initDoctorView()
    {
        // Unimplemented for now
    }

    /**
     * Initializes the view when the logged in user is a patient
     */
    public void initPatientView()
    {

        // Get all files belonging to this patient:
        int patientId = LogInfo.uID;
        ArrayList<PatientFile> patientFiles = PatientFile.getAllFiles(patientId);

        // If empty, stop here.
        if (patientFiles.isEmpty())
            return;

        // If not, get the first file to display:
        PatientFile mainFile = patientFiles.get(0);
        User doctor = User.getUser(mainFile.getDoctorID());
        User patient = User.getUser(mainFile.getPatientID());

        // Set the appropriate values in the view
        patientIdLabel.setText("Name:");
        patientIdField.setText(patient.getDisplayName());
        doctorIdField.setText("Doctor: ");
        doctorIdField.setText(doctor.getDisplayName());
        medicalInfoText.setText(mainFile.getMedicalInfo());
        medicationText.setText(mainFile.getMedication());
        verifiedCheckbox.setSelected(mainFile.getVerified());

    }

    /**
     * Called when the back button is pressed
     */
    public void back()
    {
        try {
            redirectToDashboard();
        } catch (IOException ignored) {

        }
    }

}
