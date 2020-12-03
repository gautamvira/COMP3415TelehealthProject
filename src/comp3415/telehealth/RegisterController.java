package comp3415.telehealth;

import comp3415.telehealth.model.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController extends Controller implements Initializable {

    @FXML private ChoiceBox userTypePicker;

    @FXML private TextField nameField;

    @FXML private TextField usernameField;

    @FXML private PasswordField passwordField;

    @FXML private Label outputText;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        userTypePicker.setTooltip(new Tooltip("Select the type of user you are."));
        userTypePicker.setItems(FXCollections.observableArrayList(
                "Patient",
                new Separator(),
                "Doctor")
        );
        userTypePicker.setValue("Patient");
        outputText.setText(" ");


    }

    public void register()
    {
        try {
            User.register(nameField.getText(), userTypePicker.getValue().toString(), usernameField.getText(),
                    passwordField.getText());
            outputText.setText("Registration Successful");
        }
        catch (Exception e) {
            outputText.setText("Registration Failed");
        }

    }
    public void back()
    {
        try {
            redirectToLogin();
        } catch (IOException ignored) {

        }
    }
}
