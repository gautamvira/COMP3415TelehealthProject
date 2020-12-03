// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package comp3415.telehealth.chat.client;

import comp3415.telehealth.chat.ocsf.client.*;
import comp3415.telehealth.chat.common.*;
import java.io.*;
import java.net.SocketException;

/**
 * COMP 3415 Software Engineering
 * Personal Project Phase 1
 * Ian Cuninghame 0670401
 * October 12, 2020
 * Lakehead University
 */

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************

  // CHATIF interface variables
  ChatIF clientUI;
  int clientID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI, int clientUserID)
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.clientID = clientUserID;
    openConnection();
  }

  // OVERRIDES

  /**
   * Hook method called each time an exception is thrown by the client's
   * thread that is waiting for messages from the server. The method may be
   * overridden by subclasses.
   *
   * @param exception the exception raised.
   */
  protected void connectionException(Exception exception) {

    clientUI.display
            ("Lost connection with the server.  Terminating chat client.");
    quit();

  }
  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg)
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    // Check to see if a command was entered. If so, calls handleCommandFromClientUI
    if (message.matches("#\\w+") || message.matches("#\\w+\\s\\S+")){
      handleCommandFromClientUI(message);
    }
    // Sends the message on to the server:
    try
    {
      sendToServer(message); //Sends the client's msg to server
    }
    catch(IOException e) // If there is a problem with the connection, an IOException will be thrown
    {
      clientUI.display("Could not send message to server.  Terminating chat client. " + e);
      quit(); //Terminates the client
    }
  }

  /**
   * This method handles all user commands coming from the UI
   *
   * @param message The command, including the # prefix, from the UI.
   */
  public void handleCommandFromClientUI(String message)
  {
    //#sethost
    if (message.matches("#sethost\\s\\w+")){ //Checks for proper syntax
      if(!isConnected()){ //Checks to see if the client is disconnected
        clientUI.display("You must be connected to a server to do that.");
        return;
      }
      String[] arr = message.split("\\s"); //splits the input string along the whitespaces
      clientUI.display("Setting hostname to " + arr[1]);
      setHost(arr[1]); //sets the hostname to the argument given
      return;
    }
    //#setport
    if (message.matches("#setport\\s\\d+")){ //Checks for proper syntax
      if (!isConnected()){ //Checks to see if the client is disconnected
        clientUI.display("You must be connected to a server to do that.");
        return;
      }
      String[] arr = message.split("\\s"); //splits the input string along the whitespaces
      clientUI.display("Setting port number to " + arr[1]);
      setPort(Integer.parseInt(arr[1])); //sets the listening port to the argument given
      return;
    }
    //others
    switch(message){
      case "#quit": //Disconnects from the server, then terminates the client.
        clientUI.display("Goodbye!");
        quit();
        break;
      case "#logoff": //Disconnects the user from the server without terminating the client.
        clientUI.display("Closing connection.");
        if (isConnected()){
          try { closeConnection(); } catch(IOException e) { clientUI.display("Couldn't log out for some reason. Try #quit instead."); }
        }else{
          clientUI.display("You are already logged out.");
        }
        break;
      case "#loginUser": //Logs the user back in after a #logoff
        if (!isConnected()){
          clientUI.display("Logging in...");
          try { openConnection(); } catch(IOException e) { clientUI.display("Couldn't log in."); }
        }else{
          clientUI.display("You are already logged in.");
        }
        break;
      case "#gethost": //Displays the server host to the client
        clientUI.display("Server host is " + getHost());
        break;
      case "#getport": //Displays the listening port to the client
        clientUI.display("Port number is " + getPort());
        break;
      default:
        // clientUI.display("Unrecognized Command.");
        break;
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException ignored){}
    System.exit(0);
  }
}