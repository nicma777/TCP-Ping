import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Catcher extends Thread {

	String address;
	int port;

	
	public Catcher(String bind, int port) {
		this.port = port;
		this.address = bind;
	}
	
	public void run() {
		try {
			System.out.println("Hello! I am listening and my name is " + InetAddress.getLocalHost().getHostName() + " If you can not find me by name use: "
					+ InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		 try (
	            ServerSocket serverSocket = new ServerSocket(port, 100, InetAddress.getByName(address));	
	         ) {
			 	while(true) {
			 		  Socket pitcherSocket = serverSocket.accept();
			 		  new Server(pitcherSocket).start();
			 	}
	            } catch (IOException e) {
	                try {
						System.out.println("Exception caught when trying to listen on port "
						    + port + " or listening for a connection. Try using " + InetAddress.getLocalHost().getHostAddress() + " for IP.");
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					}
	            }
	}
	
private class Server extends Thread {
		
	Socket pitcherSocket;
		
	public Server(Socket pitcherSocket) {
		this.pitcherSocket = pitcherSocket;
	}
	
	public void run() {
		synchronized (pitcherSocket) {
			try (	         		
		     		DataInputStream in = new DataInputStream(pitcherSocket.getInputStream());
		     		DataOutputStream out = new DataOutputStream(pitcherSocket.getOutputStream());
		        ) {
					recieveAndSendMessage(in,out);
		            pitcherSocket.notify();
		         } catch (IOException e) {
		             System.out.println("Exception caught when trying to listen on port "
		                 + port + " or listening for a connection");
		         } catch (ClassNotFoundException e) {
					e.printStackTrace();
				} 
		}
		 
	}
}
	
	//receiving and sending message back to the Pitcher
	public void recieveAndSendMessage(DataInputStream in, DataOutputStream out) throws ClassNotFoundException, IOException {
   	 	String message =  in.readUTF();
   	 	message = timeAdjustment(message);
		out.writeUTF(message);
	}
	
	//adjusting message timestamp
	private String timeAdjustment(String message) throws IOException{
		String [] splited = message.split("-");
	    StringBuilder sb = new StringBuilder();
	    sb.append(splited[0]+"-");
	    sb.append(System.currentTimeMillis()+"-");
	    sb.append(splited[2]);

		return sb.toString();
	}
}

