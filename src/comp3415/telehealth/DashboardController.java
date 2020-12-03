package comp3415.telehealth;

import comp3415.telehealth.db.LogInfo;
import comp3415.telehealth.db.GlobalUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController extends Controller implements Initializable {

    @FXML private Label welcomeLabel;
    @FXML private Label bottomLabel;
    @FXML private Label statusLabel;
    @FXML private Button startMeetingButton;
    @FXML private Button viewPatientButton;
    @FXML private Button newPatientButton;
    @FXML private Button emergencyButton;

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

        statusLabel.setVisible(false);

        // button onAction's set in dashboard.fxml

        // Ensure user is logged in, if not, redirect them to loginUser view:
        if (!GlobalUser.isLoggedIn())
            try {
                redirectToLogin();
            } catch (IOException e){ // if can't redirect, exit the program.
                System.exit(0);
            }

        // Customize dashboard view:
        welcomeLabel.setText("Welcome to LU TeleHealth. Choose from one of the options below.");
        bottomLabel.setText("Signed in as " + LogInfo.displayName);
        if (GlobalUser.isDoctor())
            initDoctorDashboard();
        else
            initPatientDashboard();

    }

    /**
     * Called to initialize the view when the logged in user is a doctor
     */
    public void initDoctorDashboard()
    {
        startMeetingButton.setText("Start a Meeting");
        viewPatientButton.setText("Search for a Patient's File");
        newPatientButton.setText("Create a Patient's File");
        emergencyButton.setVisible(false);
    }

    /**
     * Call to initialize the view when the logged in user is a patient
     */
    public void initPatientDashboard()
    {
        startMeetingButton.setText("Join a Meeting");
        viewPatientButton.setText("View your File");
        newPatientButton.setText("Register new File");
        emergencyButton.setVisible(true);
    }

    /**
     * Called when the logout button is pressed.
     * @param e the mouseclick
     */
    public void logoutUser(ActionEvent e){
        // Logout the user, then redirect to loginUser screen
        GlobalUser.setLogInfo(0, "GUEST", "Patient", "GUEST", false);
        try {
            redirectToLogin();
        } catch (IOException ioe) {
            bottomLabel.setText("Error logging out");
        }
    }

    /**
     * Redirect to the chat view.
     */
    public void openChat(ActionEvent e)
    {
        try{
            redirectToChat();
        }catch(IOException ioe){
            if(GlobalUser.isDoctor())
                bottomLabel.setText("Error starting the chat service.");
            else
                bottomLabel.setText("Couldn't connect to chat. Maybe your meeting hasn't started yet?");
        }
    }

    /**
     * Redirect to the "new patient file" form view.
     */
    public void newPatientFile(ActionEvent e)
    {
        try{
            redirectToNewPatientFile();
        }catch(IOException ioe){
            bottomLabel.setText("Error loading the form.");
        }
    }

    /**
     * If the user is a doctor, redirects them to the "search for patient file" view.
     * Otherwise, redirects them to the "view your own file" view.
     */
    public void viewPatientFile(ActionEvent e)
    {
        if (GlobalUser.isDoctor()) //if the user is a doctor, allow them to search for files:
            try {
                redirectToFileSearch();
            } catch (IOException ioe) {
                statusLabel.setVisible(true);
                statusLabel.setText("Error loading the search for patient file form.");
                ioe.printStackTrace();
            }
        else // and if the user is a patient, show them their own file:
            try {
                redirectToViewPatientFile();
            } catch (IOException ioe) {
                statusLabel.setVisible(true);
                statusLabel.setText("Error loading your patient file.");
                ioe.printStackTrace();
            }
    }

    // Class to manipulate the dashboard

}
