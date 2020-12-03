// This file contains material supporting section 3.8 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

package comp3415.telehealth.chat.ocsf.server;

import java.io.*;
import java.net.*;
import java.util.HashMap;

/**
 * An instance of this class is created by the server when a cuninghame.comp3415projectphase1.client connects. It
 * accepts messages coming from the cuninghame.comp3415projectphase1.client and is responsible for sending data
 * to the cuninghame.comp3415projectphase1.client since the socket is private to this class. The AbstractServer
 * contains a set of instances of this class and is responsible for adding and
 * deleting them.
 * <p>
 * Project Name: OCSF (Object Client-Server Framework)
 * <p>
 * 
 * @author Dr Robert Lagani&egrave;re
 * @author Dr Timothy C. Lethbridge
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version February 2001 (2.12)
 */
public class ConnectionToClient extends Thread {
	// INSTANCE VARIABLES ***********************************************

	/**
	 * A reference to the Server that created this instance.
	 */
	private AbstractServer		server;

	/**
	 * Sockets are used in the operating system as channels of communication
	 * between two processes.
	 * 
	 * @see java.net.Socket
	 */
	private Socket				clientSocket;

	/**
	 * Stream used to read from the cuninghame.comp3415projectphase1.client.
	 */
	private ObjectInputStream	input;

	/**
	 * Stream used to write to the cuninghame.comp3415projectphase1.client.
	 */
	private ObjectOutputStream	output;

	/**
	 * Indicates if the thread is ready to stop. Set to true when closing of the
	 * connection is initiated.
	 */
	private boolean				readyToStop;

	/**
	 * Map to save information about the cuninghame.comp3415projectphase1.client such as its loginUser ID. The
	 * initial size of the map is small since it is not expected that concrete
	 * servers will want to store many different types of information about each
	 * cuninghame.comp3415projectphase1.client. Used by the setInfo and getInfo methods.
	 */
	private HashMap				savedInfo	= new HashMap(10);

	// CONSTRUCTORS *****************************************************

	/**
	 * Constructs a new connection to a cuninghame.comp3415projectphase1.client.
	 * 
	 * @param group
	 *            the thread groupSystem.out.println("Client at "+ cuninghame.comp3415projectphase1.client +
	 *            "connected"); that contains the connections.
	 * @param clientSocket
	 *            contains the cuninghame.comp3415projectphase1.client's socket.
	 * @param server
	 *            a reference to the server that created this instance
	 * @exception IOException
	 *                if an I/O error occur when creating the connection.
	 */
	ConnectionToClient(ThreadGroup group, Socket clientSocket, AbstractServer server) throws IOException {
		super(group, (Runnable) null);
		// Initialize variables
		this.clientSocket = clientSocket;
		this.server = server;

		clientSocket.setSoTimeout(0); // make sure timeout is infinite

		// Initialize the objects streams
		try {
			input = new ObjectInputStream(clientSocket.getInputStream());
			output = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (IOException ex) {
			try {
				closeAll();
			} catch (Exception exc) {
			}

			throw ex; // Rethrow the exception.
		}

		readyToStop = false;
		start(); // Start the thread waits for data from the socket
	}

	// INSTANCE METHODS *************************************************

	/**
	 * Sends an object to the client.
	 * 
	 * @param msg
	 *            the message to be sent.
	 * @exception IOException
	 *                if an I/O error occur when sending the message.
	 */
	final public void sendToClient(Object msg) throws IOException {
		if (clientSocket == null || output == null)
			throw new SocketException("socket does not exist");

		output.writeObject(msg);
	}

	/**
	 * Reset the output stream so we can use the same
	 * buffer repeatedly. This would not normally be used, but is necessary
    * in some circumstances when Java refuses to send data that it thinks has been sent.
	 */
	final public void forceResetAfterSend() throws IOException {
      output.reset();
	}

	/**
	 * Closes the cuninghame.comp3415projectphase1.client. If the connection is already closed, this call has no
	 * effect.
	 * 
	 * @exception IOException
	 *                if an error occurs when closing the socket.
	 */
	final public void close() throws IOException {
		readyToStop = true; // Set the flag that tells the thread to stop

		try {
			closeAll();
		} finally {
			server.clientDisconnected(this);
		}
	}

	// ACCESSING METHODS ------------------------------------------------

	/**
	 * Returns the address of the cuninghame.comp3415projectphase1.client.
	 * 
	 * @return the cuninghame.comp3415projectphase1.client's Internet address.
	 */
	final public InetAddress getInetAddress() {
		return clientSocket == null ? null : clientSocket.getInetAddress();
	}

	/**
	 * Returns a string representation of the cuninghame.comp3415projectphase1.client.
	 * 
	 * @return the cuninghame.comp3415projectphase1.client's description.
	 */
	public String toString() {
		return clientSocket == null ? null : clientSocket.getInetAddress().getHostName() + " ("
				+ clientSocket.getInetAddress().getHostAddress() + ")";
	}

	/**
	 * Saves arbitrary information about this cuninghame.comp3415projectphase1.client. Designed to be used by
	 * concrete subclasses of AbstractServer. Based on a hash map.
	 * 
	 * @param infoType
	 *            identifies the type of information
	 * @param info
	 *            the information itself.
	 */
	public void setInfo(String infoType, Object info) {
		savedInfo.put(infoType, info);
	}

	/**
	 * Returns information about the cuninghame.comp3415projectphase1.client saved using setInfo. Based on a hash
	 * map.
	 * 
	 * @param infoType
	 *            identifies the type of information
	 */
	public Object getInfo(String infoType) {
		return savedInfo.get(infoType);
	}

	// RUN METHOD -------------------------------------------------------

	/**
	 * Constantly reads the cuninghame.comp3415projectphase1.client's input stream. Sends all objects that are
	 * read to the server. Not to be called.
	 */
	final public void run() {
		server.clientConnected(this);

		// This loop reads the input stream and responds to messages
		// from clients
		try {
			// The message from the cuninghame.comp3415projectphase1.client
			Object msg;

			while (!readyToStop) {
				// This block waits until it reads a message from the cuninghame.comp3415projectphase1.client
				// and then sends it for handling by the server
				msg = input.readObject();
				server.receiveMessageFromClient(msg, this);
			}
		} catch (Exception exception) {
			if (!readyToStop) {
				try {
					closeAll();
				} catch (Exception ex) {
				}

				server.clientException(this, exception);
			}
		}
	}

	// METHODS TO BE USED FROM WITHIN THE FRAMEWORK ONLY ----------------

	/**
	 * Closes all connection to the server.
	 * 
	 * @exception IOException
	 *                if an I/O error occur when closing the connection.
	 */
	private void closeAll() throws IOException {
		try {
			// Close the socket
			if (clientSocket != null)
				clientSocket.close();

			// Close the output stream
			if (output != null)
				output.close();

			// Close the input stream
			if (input != null)
				input.close();
		} finally {
			// Set the streams and the sockets to NULL no matter what
			// Doing so allows, but does not require, any finalizers
			// of these objects to reclaim system resources if and
			// when they are garbage collected.
			output = null;
			input = null;
			clientSocket = null;
		}
	}

	/**
	 * This method is called by garbage collection.
	 */
	protected void finalize() {
		try {
			closeAll();
		} catch (IOException e) {
		}
	}
}
// End of ConnectionToClient class