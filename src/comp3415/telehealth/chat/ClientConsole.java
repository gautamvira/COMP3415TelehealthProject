package comp3415.telehealth.chat;

import java.io.*;
import comp3415.telehealth.chat.client.*;
import comp3415.telehealth.chat.common.*;

/**
 * COMP 3415 Software Engineering
 * Personal Project Phase 1
 * Ian Cuninghame 0670401
 * October 12, 2020
 * Lakehead University
 */

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port, String login_id)
  {
    try 
    {
      String user_id = "0";
      client = new ChatClient(host, port, this, Integer.parseInt(user_id));
      // Send the #loginUser <LOGIN_ID> message to the server:
      client.sendToServer("#login " + login_id + " " + user_id);
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
  }

  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {
      BufferedReader fromConsole = 
        new BufferedReader(new InputStreamReader(System.in));
      String message;

      while (true) 
      {
        message = fromConsole.readLine();
        client.handleMessageFromClientUI(message);
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
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
   * This method is responsible for the creation of the Client UI.
   * Hostname and port number can be specified through command line attributes.
   *
   * @param args [0] The host to connect to. [1] the port number to listen to
   */
  public static void main(String[] args) 
  {
    String login_id = "guest";    // Login ID is mandatory. For testing, we will create a default loginUser ID if none is given.
    String host = "localhost";    // The host IP
    String port_s = "5555";       // The port string literal
    int port = DEFAULT_PORT;

    // Set command line arguments to their respective variables:
    if (args.length > 0)
      login_id = args[0];

    if (args.length > 1)
      host = args[1];

    if (args.length > 2){
      port_s = args[2];
      port = Integer.parseInt(port_s);
    }

    // create a client console with the specified host, port #, and loginUser id:
    ClientConsole chat = new ClientConsole(host, port, login_id);

    System.out.println("Welcome to MediChat console! Start messaging by typing below. Press ENTER to send.");
    System.out.println("You can also enter commands by prefixing a #: available commands are listed below.");
    System.out.println("\t 1. #quit \n" +
                      "\t 2. #logoff \n" +
                      "\t 3. #sethost <hostname> \n" +
                      "\t 4. #setport <portnumber> \n" +
                      "\t 5. #loginUser \n" +
                      "\t 6. #gethost \n" +
                      "\t 7. #getport");
    System.out.println("Type below:");

    chat.accept();  //Wait for user input
  }
}