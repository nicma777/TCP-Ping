import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



public class Pitcher extends Thread {
	
	int id = 0, port, mps, size, numOfMsgSent = 0;
	String hostName;
	MessageTime messageTime = MessageTime.getInstance();
    List<Integer> sentId = Collections.synchronizedList(new ArrayList<Integer>());

	SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	
	public Pitcher( String hostName, int port, int mps, int size) {
		this.mps = mps;
		this.size = size;
		this.port = port;
		this.hostName = hostName;
	}
	
	public void run() {
		new Timer().scheduleAtFixedRate(new Client(), 0,  1000 / mps);
		new Timer().scheduleAtFixedRate(new DisplayData(), 1000, 1000);
	}
	
private class Client extends TimerTask {
	
	long timeSent, timeRecived, timeDelivered;
			
	public void run() {
		try (
            Socket pitcherSocket = new Socket(InetAddress.getByName(hostName), port);
        		
        	DataOutputStream out = new DataOutputStream(pitcherSocket.getOutputStream());
        	DataInputStream in = new DataInputStream(pitcherSocket.getInputStream());
	        ) {
			synchronized (pitcherSocket) {
				timeSent = System.currentTimeMillis();
        		sendMessage(out, formatter, timeSent);
        		String message = getMessage(in);
    			timeRecived = System.currentTimeMillis();
        		String [] splited = message.split("-");
        		timeDelivered = Long.parseLong(splited[1]);     		
    			checkForLostMessages(Integer.parseInt(splited[0]));
        		messageTime.calculateTime(timeSent, timeDelivered, timeRecived);
	            pitcherSocket.notify();
			}
	        } catch (UnknownHostException e) {
	            System.err.println("Don't know about host " + hostName);
	            System.exit(1);
	        } catch (IOException e) {
	            System.err.println("No response from port " + port);
	        } 
	}
}
	
	//send a message to Catcher and increase id 
	private void sendMessage (DataOutputStream out, SimpleDateFormat formatter, long timeSent) throws IOException {
		String message = new String(id+"-"+timeSent+"-");
		int sizeDifference = size - SizeOf.sizeof(message);
		
		message = sizeAdjustment(sizeDifference, message);
        out.writeUTF(message);
        numOfMsgSent();
        sentId.add(id);
		increseId();
	}
	
	//read message from input
	private String getMessage(DataInputStream in) throws IOException {
		String message =  in.readUTF();
		return message;
	}
	
	//adjust the size of message to match required
	private String sizeAdjustment(int sizeDifference, String message) {
	    StringBuilder sb = new StringBuilder();
	    sb.append(message);
		for(int a = 0; a < (sizeDifference-2); a++)	//remove 2 because writeUTF adds 2 bytes
			sb.append("s");
		
		return sb.toString();
	}
	
	
	private synchronized void numOfMsgSent() {
		numOfMsgSent++;
	}
	
	private synchronized void increseId() {
		id++;
	}
	
	//checking if some packets are lost and if they are write it out
	private synchronized void checkForLostMessages(int check) {
		if (sentId.contains(check)) {
			sentId.remove(new Integer(check));
		}
		if (sentId.size() != 0) {
			System.out.println("Message with the ID of " + sentId + "is lost.");
			sentId.clear();
		}
	}
	
	
	//displaying data
private class DisplayData extends TimerTask {
		public void run() {
			Date date = new Date(System.currentTimeMillis());

			System.out.println("Time      " + "Msg sent total   " + "Msg sent   " +  "Average (A->B->A) time    " + 
			"Total time messages are being sent    " + "Average (A->B) time    " + "Average (B->A) time");
			System.out.println(formatter.format(date) + "\t" + numOfMsgSent + "\t\t" + mps + "\t\t" + (messageTime.getFullCircle()/mps) + 
					"\t\t\t\t" +  messageTime.getTotalTime() + "\t\t\t" + (messageTime.getPToC()/mps) + "\t\t\t" + (messageTime.getCToP()/mps));
			
			messageTime.resetTime();
		}
		
	}
	
}

