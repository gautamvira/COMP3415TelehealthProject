package comp3415.telehealth;

import comp3415.telehealth.chat.client.ChatClient;
import comp3415.telehealth.chat.common.ChatIF;
import comp3415.telehealth.chat.server.EchoServer;
import comp3415.telehealth.db.AppInfo;
import comp3415.telehealth.db.LogInfo;
import comp3415.telehealth.db.GlobalUser;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController extends Controller implements Initializable, ChatIF {

    @FXML private TextField textInput;
    @FXML private ListView<Text> textOutput;
    @FXML private Button sendButton;
    @FXML private Label bottomLabel;

    // Chat objects
    String chatName; // Will be set to GlobalUser.uname
    String chatID; // String representation of GlobalUser.uid
    ObservableList<Text> outputContent = FXCollections.observableArrayList();
    ChatClient patientChatClient;
    EchoServer doctorChatServer;

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
        // if the user is not logged in, set their name to "Guest", otherwise set it to their display name:
        if (!LogInfo.isLoggedIn){
            chatName = "GUEST";
            chatID = "0";
        }
        else{
            chatName = LogInfo.uname;
            chatID = String.valueOf(LogInfo.uID);
        }

        // Bind the textOutput to whatever we put in the ObservableList outputContent:
        textOutput.setItems(outputContent);

        //clear the bottom label
        bottomLabel.setText("");

        // Customize chat view based on User Type:
        if (GlobalUser.isDoctor())
            initDoctorChatService();
        else
            initPatientChatService();

    }

    /**
     * Initializes a chat server, for when the user is logged in as a doctor.
     */
    public void initDoctorChatService()
    {
        // Start the chat server
        try {
            doctorChatServer = new EchoServer(AppInfo.CHAT_PORT, this, LogInfo.uID, LogInfo.uname);
            doctorChatServer.listen(); //Start listening for connections
            bottomLabel.setText("Server started");
        } catch (Exception ex){
            bottomLabel.setText("Error starting the chat server.");
        }
    }

    /**
     * Initializes a chat client, for when the logged-in user is not a doctor
     */
    public void initPatientChatService()
    {
        // Give welcome message:
        outputContent.add(new Text("Welcome to MediChat!"));
        outputContent.add(new Text("Start messaging by typing below. Press ENTER to send."));

        // Listen for the chat server, setting "loginUser id" to the patient's username
        try {
            patientChatClient = new ChatClient(AppInfo.CHAT_HOST, AppInfo.CHAT_PORT, this, LogInfo.uID);
            patientChatClient.sendToServer("#login " + chatName + " " + chatID);
            bottomLabel.setText("Connected");
        } catch(ConnectException cex){ //error connecting to the server
            bottomLabel.setText("Not connected");
        } catch (Exception ex) {
            bottomLabel.setText(ex.toString());
        }
    }

    /**
     * Click listener for back button press
     * @param e the action event that led to this method call
     */
    public void back(ActionEvent e){
        if (GlobalUser.isLoggedIn())
            try {
                redirectToDashboard();
            }catch (IOException ioe){
                bottomLabel.setText("Couldn't load the dashboard! Try closing and reopening the app.");
            }
        else
            try {
                redirectToLogin();
            }catch (IOException ioe){
                System.exit(0);
            }
    }

    // Chat functionality:

    /**
     * Click handler for the "send" button
     */
    public void send(ActionEvent e){
        if(GlobalUser.isDoctor()) // send their message to the patient.
            sendDoctorMsg(textInput.getText());
        else // user is a patient, so send their message to the doctor
            sendPatientMsg(textInput.getText());
        // clear the message box after sending the message:
        textInput.setText("");
    }

    public void sendPatientMsg(String message)
    {
        try{
            patientChatClient.handleMessageFromClientUI(message);
        } catch (Exception ex) {
            bottomLabel.setText("Error sending your message.");
        }
    }

    public void sendDoctorMsg(String message)
    {
        try{
            doctorChatServer.handleMessageFromServerUI(message);
        } catch (Exception ex) {
            bottomLabel.setText("Error sending your message.");
        }
    }


    /**
     * Method that when overriden is used to display objects onto
     * a UI. (From ChatIF)
     *
     * @param message
     */
    @Override
    public void display(String message) {

        // "Lambda expression" to run in a new Thread to avoid JavaFX throwing an IllegalStateException
        Platform.runLater(
                () -> {
                    // Cast the incoming message to "Text"
                    Text msg = new Text (message);
                    // Sets the text wrapping to fit the window:
                    msg.setWrappingWidth(textOutput.getWidth() * 0.95);
                    // add the message to the output view:
                    outputContent.add(msg);
                    textOutput.scrollTo(msg);
                }
        );

    }
}
