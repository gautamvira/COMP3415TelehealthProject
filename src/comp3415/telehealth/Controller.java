package comp3415.telehealth;


import comp3415.telehealth.db.AppInfo;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Contains methods that can be common to all Controllers.
 * ( All the controllers extend from this class)
 */
public class Controller {


    public void redirectToLogin() throws IOException
    {
        // Prepare the scene and stage:
        Parent loginViewParent = FXMLLoader.load(getClass().getResource("view/welcome.fxml"));
        Scene loginViewScene = new Scene(loginViewParent);
        // Gets the window
        Stage window = AppInfo.APP_WINDOW;
        window.setScene(loginViewScene);
        window.show();
    }

    public void redirectToRegister() throws IOException
    {
        // Prepare the scene and stage:
        Parent registerViewParent = FXMLLoader.load(getClass().getResource("view/register.fxml"));
        Scene registerViewScene = new Scene(registerViewParent);

        // This line gets the Stage (window) info from the button being clicked
        Stage window = AppInfo.APP_WINDOW;

        // Set the window to the new scene:
        window.setScene(registerViewScene);
        window.show();
    }

    public void redirectToDashboard() throws IOException
    {
        // Prepare the scene and stage:
        Parent dashViewParent = FXMLLoader.load(getClass().getResource("view/dashboard.fxml"));
        Scene dashViewScene = new Scene(dashViewParent);
        // Gets the window
        Stage window = AppInfo.APP_WINDOW;
        window.setScene(dashViewScene);
        window.show();
    }

    public void redirectToChat() throws IOException{
        // Prepare the scene and stage:
        Parent chatViewParent = FXMLLoader.load(getClass().getResource("view/chat.fxml"));
        Scene chatViewScene = new Scene(chatViewParent);
        // Gets the window
        Stage window = AppInfo.APP_WINDOW;
        window.setScene(chatViewScene);
        window.show();
    }

    public void redirectToNewPatientFile() throws IOException
    {

        // Prepare the scene and stage:
        Parent newFileViewParent = FXMLLoader.load(getClass().getResource("view/newpatientfile.fxml"));
        Scene newFileViewScene = new Scene(newFileViewParent);
        // Gets the window
        Stage window = AppInfo.APP_WINDOW;
        window.setScene(newFileViewScene);
        window.show();

    }

    public void redirectToFileSearch() throws IOException
    {
        // Prepare the scene and stage:
        Parent newFileViewParent = FXMLLoader.load(getClass().getResource("view/searchforpatientfile.fxml"));
        Scene newFileViewScene = new Scene(newFileViewParent);
        // Gets the window
        Stage window = AppInfo.APP_WINDOW;
        window.setScene(newFileViewScene);
        window.show();
    }

    public void redirectToViewPatientFile() throws IOException
    {
        // Prepare the scene and stage:
        Parent fileViewParent = FXMLLoader.load(getClass().getResource("view/viewpatientfile.fxml"));
        Scene fileViewScene = new Scene(fileViewParent);
        // Gets the window
        Stage window = AppInfo.APP_WINDOW;
        window.setScene(fileViewScene);
        window.show();
    }

}
