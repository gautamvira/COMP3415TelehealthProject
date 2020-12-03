package comp3415.telehealth.chat;

import java.io.*;
import comp3415.telehealth.chat.common.*;
import comp3415.telehealth.chat.server.EchoServer;

/**
 * COMP 3415 Software Engineering
 * Personal Project Phase 1
 * Ian Cuninghame 0670401
 * October 12, 2020
 * Lakehead University
 */

/**
 * This class constructs the UI for a server.  It implements the
 * ChatIF interface in order to activate the display() method.
 */
public class ServerConsole implements ChatIF
{
    //Class variables *************************************************

    /**
     * The default port to connect on.
     */
    final public static int DEFAULT_PORT = 5555;

    //Instance variables **********************************************

    /**
     * The instance of the server that created this ServerConsole.
     */
    EchoServer server;

    //Constructors ****************************************************

    /**
     * Constructs an instance of the ServerConsole UI.
     *
     * @param port The port to connect on.
     */
    public ServerConsole(int port)
    {
        //"servername" sets the name to be displayed on messages from this server
        server = new EchoServer(port, this, 0, "drjames");
        try
        {
            server.listen(); //Start listening for connections
        }
        catch (Exception ex)
        {
            System.out.println("ERROR - Could not listen for clients!");
        }
    }

    //Instance methods ************************************************

    /**
     * This method waits for input from the console.  Once it is
     * received, it sends it to the server's message handler.
     */
    public void accept()
    {
        try
        {
            BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
            String message;

            while (true)
            {
                message = fromConsole.readLine();
                server.handleMessageFromServerUI(message); //sends the message to this method to be handled by the server
            }
        }
        catch (Exception ex)
        {
            System.out.println("Unexpected error while reading from console!");
        }
    }

    /**
     * This method overrides the method in the ChatIF interface.  It
     * displays a message onto the screen.
     *
     * @param message The string to be displayed.
     */
    public void display(String message)
    {
        System.out.println(" " + message);
    }

    //Class methods ***************************************************

    /**
     * This method is responsible for the creation of the Server UI.
     * Port number can be specified through command line arguments.
     *
     * @param args [0] The port number to use
     */
    public static void main(String[] args)
    {
        String port_s = "";  //The port string literal
        int port = DEFAULT_PORT;

        try
        {
            // Set the port string literal to the ones entered by the user
            port_s = args[0];
        }
        catch(Exception e)
        {
            // Error with arguments entered, or none entered, so use defaults:
            port_s = String.valueOf(DEFAULT_PORT);
        }
        port = Integer.parseInt(port_s); // set the port for the server to the one entered in the command line
        ServerConsole console = new ServerConsole(port); // create a server console with the specified port #
        System.out.println("Welcome to MediChat Server! Start messaging by typing below. Press ENTER to send.");
        System.out.println("You can also enter commands by prefixing a #: available commands are listed below.");
        System.out.println("\t 1. #quit \n" +
                "\t 2. #stop \n" +
                "\t 3. #close \n" +
                "\t 4. #setport <portnumber> \n" +
                "\t 5. #start \n" +
                "\t 6. #getport");
        console.accept();  //Wait for console data
    }
}
//End of ConsoleChat class
