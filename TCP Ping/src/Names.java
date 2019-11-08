import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Names {

	public static void main(String[] args) throws UnknownHostException  {
		
		InetAddress address = InetAddress.getByName("192.168.5.16");
		System.out.println(address); 
		System.out.println(InetAddress.getByName("www.google.com"));
	}
}
