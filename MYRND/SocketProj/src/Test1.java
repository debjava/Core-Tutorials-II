import java.net.InetAddress;
import java.net.UnknownHostException;


public class Test1 
{
	public static void main(String[] args) 
	{
		try {
//			InetAddress netAdrs = InetAddress.getByName("localhost");
//			System.out.println(netAdrs.getHostName());
			
			InetAddress address = InetAddress.getLocalHost();
			System.out.println(address.toString());
		      System.out.println(address.getHostName());

			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
