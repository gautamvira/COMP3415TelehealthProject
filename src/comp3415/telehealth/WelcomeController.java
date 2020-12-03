package comp3415.telehealth;

import comp3415.telehealth.db.GlobalUser;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController extends Controller implements Initializable {

    // Variable names used here *must match* the fx:id set in the FXML file for that component.
    @FXML private Label welcomeText;
    @FXML private Button loginBtn;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button sosButton;
    @FXML private ChoiceBox userTypePicker;

    /**
     * Initializes the controller class
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        // (Initial attributes set in the welcome.fxml file)

        welcomeText.setText("Welcome to LU Telehealth! \n" + "Please log in or register below.");
        userTypePicker.setTooltip(new Tooltip("Select the type of user you are."));
        userTypePicker.setItems(FXCollections.observableArrayList(
                "Patient",
                new Separator(),
                "Doctor")
        );
        userTypePicker.setValue("Patient");

    }

    /**
     * This method is called when the SOS button is pushed.
     * Starts an anonymous chat with a doctor for an emergency situation.
     * @param event
     *
     */
    public void sosChat(ActionEvent event)
    {
        try{
            redirectToChat();
        }catch(IOException e){
            welcomeText.setText("Error loading the chat. Should probably call your doctor or 911 if its an emergency.");
            e.printStackTrace();
        }
    }


    /**
     * This method is called when the login button is pushed.
     * Handles the user's log in request.
     * @param event the (Mouse)Event associated with this method call
     */
    public void loginUser(ActionEvent event)
    {
        // Required variables
        Parent dashViewParent;

        try{
            // loginUser information from user input:
            String username = usernameField.getText();
            String password = passwordField.getText();
            String userType = userTypePicker.getValue().toString();

            // Login user, then show the dashboard.
            if(GlobalUser.loginUser(username, password, userType)){
                redirectToDashboard();
            }
            else{ // if username password incorrect
                welcomeText.setText("Invalid credentials. Please verify and try again.");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
    /**
     * This method is called when the register button is pushed.
     * Calls the redirect function.
     * @param event the (Mouse)Event associated with this method call
     */
    public void registerUser(ActionEvent event)
    {
        try{
            redirectToRegister();
        }catch(IOException e){
            welcomeText.setText("Error loading the registration form.");
        }
    }



}
