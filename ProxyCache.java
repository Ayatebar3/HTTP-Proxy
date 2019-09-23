
/** ProxyCache.java */

/**
 * ProxyCache.java - Simple caching proxy
 */

// DataInputSteam can handle both text and binary simultaneously
	// use for reading the response
// BufferedReader - pure text
// BufferedInputStream - pure binary

// Issues: 
// 1) we didn't check the cache for old values and then
// restore the old values
// 2) didn't store the whole response

// If you cache a random file the key hostname
// then when we tried to get index.html, it gave us a cached
// random file


import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.Thread;

class SocketThread implements Runnable
{
	private Socket client = null;
	
	public SocketThread(Socket socket)
	{
		this.client = socket;
	}
	@Override
	public void run() {
		ProxyCache.handle(client);
	}
}

public class ProxyCache {

	/** Port for the proxy */
	private static int port;

	/** Socket for client connections */
	private static ServerSocket socket;
	
	private static Hashtable<String, HttpResponse> cache;
	private static Enumeration<String> cachePages;

	/** Create the ProxyCache object and the socket */
	public static void init(int p) {
		port = p;
		cache = new Hashtable<String, HttpResponse>();
		// cachePages = cache.elements();

		try {
			socket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Error creating socket: " + e);
			System.exit(-1);
		}
	}

	public static void cachePage(String uri, HttpResponse page){

		cache.put(uri, page);

	}

	public static void handle(Socket client) {
		Socket server = null;
		HttpRequest request = null;
		HttpResponse response = null;

		/* Process request. If there are any exceptions, then simply
		* return and end this request. This unfortunately means the
		* client will hang for a while, until it timeouts. */

		/* Read request */
		try {
			DataInputStream fromClient = 
				new DataInputStream(client.getInputStream());
			System.out.println("DataInputStream.getInputStream()\n");

			request = new HttpRequest(fromClient);

		} catch (IOException e) {
			System.out.println("Error reading request from client: " + e);
			return;
		}
		

		if (cache.containsKey(request.getURI())) {
			// key is URI
			// is it already here, if it is gimme the page
			try {
				DataOutputStream toClient = new DataOutputStream(client.getOutputStream());
				response = cache.get(request.getURI());

				toClient.writeBytes(response.toString()); 
				toClient.write(response.body);
				client.close();

				System.out.println("\n/* This page was fetched from the cache */\n");


			} catch (IOException e) {
				System.out.println("Error writing response to client: " + e);
			}

		} else {
		/* Send request to server */
			try {
				/* Open socket and write request to socket */
				// Note: Socket socket = new Socket(IP_Address, Port)
				server = new Socket(request.getHost(), request.getPort());
				
				DataOutputStream toServer = new DataOutputStream(server.getOutputStream());
				/* write bytes to server */
				toServer.writeBytes(request.toString());


			} catch (UnknownHostException e) {
				System.out.println("Unknown host: " + request.getHost());
				System.out.println(e);
				return;

			} catch (IOException e) {
				System.out.println("Error writing request to server: " + e);
				return;
			}

			/* Read response and forward it to client */
			try {
				DataInputStream fromServer = new DataInputStream(server.getInputStream());
				response = new HttpResponse(fromServer);
				DataOutputStream toClient = new DataOutputStream(client.getOutputStream());
				
				/* Write response to client. First headers, then body */
				toClient.writeBytes(response.toString()); 
				toClient.write(response.body);

				client.close();
				server.close();

				cachePage(request.getURI(), response);

			} catch (IOException e) {
				System.out.println("Error writing response to client: " + e);
			}
		}
}
	


  /** Read command line arguments and start proxy */
	public static void main(String args[]) {
		int myPort = 1515;

		try {
			myPort = Integer.parseInt(args[0]);
		
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Need port number as argument");
			System.exit(-1);
		
		} catch (NumberFormatException e) {
			System.out.println("Please give port number as integer.");
			System.exit(-1);
		}
		
		init(myPort);

		/** Main loop. Listen for incoming connections and spawn a new
		* thread for handling them */
		Socket client = null;
		
		while (true) {
			try {
				client = socket.accept();

				if(client == null) {
					System.out.println("Did not accept the socket");
				}
				System.out.println("Accepted socket of " + client);
				// spawn a new thread for handling them
				// handle is a value that uniquely identifies an object

				SocketThread request = new SocketThread(client);
				Thread newThread = new Thread(request);				
				newThread.start();
				
			} catch (IOException e) {
				System.out.println("Error reading request from client: " + e);
				/* Definitely cannot continue processing this request,
				* so skip to next iteration of while loop. */
				continue;
			}
		}
	}
}

