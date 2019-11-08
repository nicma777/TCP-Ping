import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TCPPing {
	
	public static final String IPv4_REGEX =
			"^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
			"(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
			"(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
			"(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
	
	public static final Pattern IPv4_PATTERN = Pattern.compile(IPv4_REGEX);

	public static void main(String[] args) throws IOException, UnknownHostException, ClassNotFoundException, InterruptedException {
		
    	//args = new String[] {"-c", "-bind", "192.168.5.16", "-port", "9900"};
    	args = new String[] {"-p", "-port", "9900", "-mps", "10", "-size", "2000", "192.168.5.16"};

    	int size = 300, mps = 1, port;
    	String  bind = null;
    	
    	List<String> argsArray = new ArrayList<String>(Arrays.asList(args));
    	    	
		if(argsArray.get(0).equals("-c")) {
			if(argsArray.contains("-bind") && argsArray.contains("-port")) {
				 bind = argsArray.get(argsArray.indexOf("-bind")+1);
				 port = Integer.parseInt(argsArray.get(argsArray.indexOf("-port")+1));
			}else {
				throw new IllegalArgumentException("You must specify -bind address and -port number, use " +
						InetAddress.getLocalHost().getHostAddress() + " for -bind IP.");
			}		
			checkCatcher(bind,port);
			new Catcher(bind, port).start();			
		}
		else if(argsArray.get(0).equals("-p")) {
			if(argsArray.contains("-port")) {
				 port = Integer.parseInt(argsArray.get(argsArray.indexOf("-port")+1));
			}else {
				throw new IllegalArgumentException("You must specify a -port number and a hostname as the last parametar.");
			}		
			if(argsArray.contains("-mps"))
				mps = Integer.parseInt(argsArray.get(argsArray.indexOf("-mps")+1));
			if(argsArray.contains("-size"))
				size = Integer.parseInt(argsArray.get(argsArray.indexOf("-size")+1));
			bind = argsArray.get(argsArray.size()-1);

			checkPitcher(port, size);
			new Pitcher(bind, port , mps, size).start();
		}
		else {
			throw new IllegalArgumentException("First parametar must be -c or -p");
		}
		
	}
	
	// checking Catcher arguments
	public static void checkCatcher(String bind, int port) {
		if (port < 0 || port > 65535) throw new IllegalArgumentException("Invalid port! Use number between 1 and 65535");
		Matcher matcher = IPv4_PATTERN.matcher(bind);
		if (!matcher.matches()) throw new IllegalArgumentException("Invalid IP address");
	}
	
	//checking Pitcher arguments
	public static void checkPitcher(int port,  int size){
		if (port < 0 || port > 65535) throw new IllegalArgumentException("Invalid port! Use number between 1 and 65535");
		if (size < 50 || size > 3000) throw new IllegalArgumentException("Invalid size! Use number between 50 and 3000");

	}
}