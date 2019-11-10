import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Names {

	public static void main(String[] args) throws UnknownHostException  {
		
		String local = InetAddress.getLocalHost().getHostName();
		
		System.out.println(local); 

		    
		String a = new String("192.168.5.16");
		String kiki = new String("Kristijans-MBP");
		
		InetAddress ip = InetAddress.getByName(a);
		byte[] bytes = ip.getAddress();
		for (byte b : bytes) {
		    System.out.println(b & 0xFF);
		}
		InetAddress address = InetAddress.getByAddress(kiki, bytes);
		System.out.println(address); 
		System.out.println(InetAddress.getByName("www.google.com"));
	}
}
